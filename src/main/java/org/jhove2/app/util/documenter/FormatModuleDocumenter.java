/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jhove2.app.util.documenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.jhove2.app.util.documenter.displayer.DocumentationDisplayer;
import org.jhove2.app.util.documenter.displayer.DocumentationDisplayerFactory;
import org.jhove2.app.util.traverser.ReportableTypeTraverser;
import org.jhove2.config.spring.SpringConfigInfo;
import org.jhove2.core.Document;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.reportable.Reportable;
import org.jhove2.module.Module;
import org.jhove2.module.format.FormatModule;
import org.jhove2.module.format.FormatProfile;

/**
 * Intended to collection information about FormatModule, its Format, any Profiles and Profile Formats,
 * any specifications associated with the format, and the Reportable properties of the FormatModule. 
 * The latter comes from static analysis of the Java class; other information comes for the Spring
 * Bean in the JHOVE2 configuration files 
 * @author smorrissey
 *
 */
public class FormatModuleDocumenter implements ModuleDocumenter {

	public static final String DEFAULT_DISPLAY = 
		"org.jhove2.app.util.documenter.displayer.FormatModuleTabDelimDisplayer";
	/** Usage string */
	public static final String USAGE = 
		"USAGE:  java -cp CLASSPATH" + FormatModuleDocumenter.class.getName() 
		+ " fully-qualified-format-module-class-name output-dir-path"
		+ " {optional} fully-qualified-display-class-name (default "
		+ DEFAULT_DISPLAY + ")}";
	/** Fully-qualified class name for which properties file is to be generated */
	/** Error return code for erroneous command line invocation */
	public static final int EUSAGE = 1;
	/** Error return code if any exception is thrown while executing program */
	public static final int EEXCEPTION = 2;
	/** Return code for successful execution */
	public static final int SUCCESS = 0;

	/** FormatModule instance to be documented */
	protected FormatModule formatModule;
	/** FormatProfiles associated with module */
	protected List<FormatProfile> formatProfiles;
	/** Format associated with module */
	protected Format format;
	/** Formats associated with module profiles */
	protected List<Format> formatProfileFormats;
	/** container for Format info*/
	protected FormatIdDoc formatDoc;
	/** list of containers for profile formats info */
	protected List<FormatIdDoc> formatProfilesDocs;
	/** container for information about format module */
	protected FormatModuleIdDoc formatModuleIdDoc;
	/** list of containers for information about FormatProfiles */
	protected List<FormatModuleIdDoc> formatProfilesIdDocs;
	/** traverser that collections reportable properties documentation information */
	protected ReportableTypeTraverser properties;
	/** displayer that serializes documentation information*/
	protected DocumentationDisplayer displayer;

	/**
	 * main method
	 * Extracts information and displays it
	 * Requires Spring bean to have been configured for the FormatModule being documented
	 * @param args arguments 
	 *	           fully-qualified-format-module-class-name 
	 *             output-dir-path
	 *             {optional} fully-qualified-display-class-name 
	 */
	public static void main(String[]args){
		if (args.length < 2){
			System.out.println(USAGE);
			System.exit(EUSAGE);
		}
		FormatModuleDocumenter fdoc = new FormatModuleDocumenter();
		String classname = args[0];
		String outputDirName = args[1];
		File outputDir = new File(outputDirName);
		FormatModule module = null;
		SpringConfigInfo config = new SpringConfigInfo();
		if (!outputDir.exists()){
			System.out.println("Output directory " + outputDirName + " does not exist");
			System.exit(EEXCEPTION);
		}
		try {
			Class modClass = Class.forName(classname);
			module = (FormatModule) config.getReportable(modClass);
		} catch (Exception e) {
			System.out.println("Unable to get bean for class " + classname);
			System.exit(EEXCEPTION);
		}
		String displayClass = DEFAULT_DISPLAY;
		if (args.length > 2){
			displayClass = args[2];
		}
		try {
			fdoc.displayer = new DocumentationDisplayerFactory().getDocumentationDisplayer(displayClass);
			fdoc.displayer.setOutputDir(outputDir);
		} catch (JHOVE2Exception e) {
			System.out.println("Exception thrown while creating documentation displayer " 
					+ displayClass + ": " + e.getMessage());
			e.printStackTrace();
			System.exit(EEXCEPTION);
		}
		try {
			fdoc.documentModule(module);
		} catch (JHOVE2Exception e) {
			System.out.println("Exception thrown while documenting module " + classname
					+ ": " + e.getMessage());		
			e.printStackTrace();
			System.exit(EEXCEPTION);
		}
		try {
			fdoc.displayer.displayDocumentation(fdoc);
		} catch (JHOVE2Exception e) {
			System.out.println("Exception thrown while displaying documention: " + e.getMessage());	
			e.printStackTrace();
			System.exit(EEXCEPTION);
		}
		System.out.println("FormatModuleDocumenter successfully completed:");
		System.out.println("\t generated files can be found in directory " + outputDirName);
		System.exit(SUCCESS);
	}

	public FormatModuleDocumenter (){
		super();
		this.formatProfiles = new ArrayList<FormatProfile>();
		this.formatProfileFormats = new ArrayList<Format>();
		this.formatProfilesDocs = new ArrayList<FormatIdDoc>();
		this.formatProfilesIdDocs = new ArrayList<FormatModuleIdDoc>();

	}
	/**
	 * Entry point for extraction of documentation information about module
	 * @param Module to be documented
	 */
	public void documentModule(Reportable reportable)
	throws JHOVE2Exception {
		this.formatModule = (FormatModule)reportable;
		this.format = this.formatModule.getFormat();
		this.formatProfiles = this.formatModule.getProfiles();		
		this.formatModuleIdDoc = this.documentFormatModuleIdInfo(this.formatModule);
		for (FormatProfile profile:this.formatProfiles){
			this.getFormatProfileFormats().add(profile.getFormat());
			this.getFormatProfilesIdDocs().add(this.documentFormatModuleIdInfo(profile));
		}
		this.formatDoc = this.documentFormatIdInfo(this.format);
		for (Format format:this.getFormatProfileFormats()){
			this.getFormatProfilesDocs().add(this.documentFormatIdInfo(format));
		}
		this.properties = new ReportableTypeTraverser(reportable.getClass().getCanonicalName(),true);
		this.properties.extractDocInfo();
		return;
	}

	/**
	 * Extracts basic identifying information about format module or profile
	 * Intended for Section 2 (Identification) in Format Module Specification Document
	 * @param formatModule
	 * @return FormatModuleIdDoc  basic identifying information about format module
	 * @throws JHOVE2Exception
	 */
	public FormatModuleIdDoc documentFormatModuleIdInfo(Module formatModule)
	throws JHOVE2Exception {
		FormatModuleIdDoc formatIdDoc = new FormatModuleIdDoc();
		formatIdDoc.moduleName = formatModule.getReportableName();
		formatIdDoc.moduleId = formatModule.getReportableIdentifier().toString();
		formatIdDoc.moduleClassName = formatModule.getClass().getCanonicalName();
		return formatIdDoc;
	}
	/**
	 * Extracts basic identifying information about format or profile format
	 * Intended for Section 2 (Identification) in Format Module Specification Document
	 * @param format Format to be documented 
	 * @return FormatIdDoc with basic identifying information about format or profile format
	 * @throws JHOVE2Exception
	 */
	public FormatIdDoc documentFormatIdInfo(Format format)
	throws JHOVE2Exception {
		FormatIdDoc formatIdDoc = new FormatIdDoc();
		formatIdDoc.canonicalFormatName = format.getName();
		formatIdDoc.canonicalFormatIdentifier = format.getIdentifier().toString();
		if (format.getAliasNames()!= null){
			formatIdDoc.aliasFormatNames.addAll(format.getAliasNames());
		}
		if (format.getAliasIdentifiers()!= null){
			for (I8R id:format.getAliasIdentifiers()){
				String idString = id.toString();
				formatIdDoc.aliasFormatIdentifiers.add(idString);
			}
		}
		if (format.getSpecifications()!= null){
			for (Document doc:format.getSpecifications()){
				Document.Intention intent = doc.getIntention();
				if (!formatIdDoc.references.containsKey(intent)){
					List<ReferenceDoc> refDocs = new ArrayList<ReferenceDoc>();
					formatIdDoc.references.put(intent, refDocs);
				}
				List<ReferenceDoc> refDocs = formatIdDoc.references.get(intent);
				ReferenceDoc refDoc = new ReferenceDoc();
				refDoc.refName = doc.getTitle();
				refDoc.biblioInfo = this.createBiblioInfo(doc);
				for (I8R id:doc.getIdentifiers()){
					String idString = id.toString();
					refDoc.identifiers.add(idString);
				}
				refDocs.add(refDoc);
			}
		}
		return formatIdDoc;
	}
	/**
	 * Create bibliographic string with information about a Format reference
	 * Intended for section 3 (References) of format module specification 
	 * @param doc Document object for a reference associated with a Format
	 * @return bibliographic string with information about a Format reference
	 */
	public String createBiblioInfo(Document doc) {
		String authors = doc.getAuthor();
		String date = doc.getDate();
		String edition = doc.getEdition();
		String publisher = doc.getPublisher();
		String title = doc.getTitle();
		StringBuffer sbBib = new StringBuffer();
		if (authors != null && authors.length()>0){
			sbBib.append(authors);
			sbBib.append(". ");
		}
		if (title != null && title.length()>0){
			sbBib.append(title);
			if (edition != null && edition.length()>0){
				sbBib.append(" ");
				sbBib.append(edition);
			}
			sbBib.append(". ");
		}
		if (!(date==null && publisher==null)){
			sbBib.append("(");
			if (publisher !=null){
				sbBib.append(publisher);
				if (date != null){
					sbBib.append(", ");
					sbBib.append(date);
				}
			}
			else {
				sbBib.append(date);
			}
			sbBib.append(")");
		}
		return sbBib.toString();
	}

	/**
	 * @return the formatModule
	 */
	public FormatModule getFormatModule() {
		return formatModule;
	}

	/**
	 * @return the formatProfiles
	 */
	public List<FormatProfile> getFormatProfiles() {
		return formatProfiles;
	}

	/**
	 * @return the format
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * @return the formatProfileFormats
	 */
	public List<Format> getFormatProfileFormats() {
		return formatProfileFormats;
	}

	/**
	 * @return the formatDoc
	 */
	public FormatIdDoc getFormatDoc() {
		return formatDoc;
	}

	/**
	 * @return the formatProfilesDocs
	 */
	public List<FormatIdDoc> getFormatProfilesDocs() {
		return formatProfilesDocs;
	}

	/**
	 * @return the formatModuleIdDoc
	 */
	public FormatModuleIdDoc getFormatModuleIdDoc() {
		return formatModuleIdDoc;
	}

	/**
	 * @return the formatProfilesIdDocs
	 */
	public List<FormatModuleIdDoc> getFormatProfilesIdDocs() {
		return formatProfilesIdDocs;
	}

	/**
	 * @return the properties
	 */
	public ReportableTypeTraverser getProperties() {
		return properties;
	}

	/**
	 * @param formatModule the formatModule to set
	 */
	public void setFormatModule(FormatModule formatModule) {
		this.formatModule = formatModule;
	}

	/**
	 * @param formatProfiles the formatProfiles to set
	 */
	public void setFormatProfiles(List<FormatProfile> formatProfiles) {
		this.formatProfiles = formatProfiles;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(Format format) {
		this.format = format;
	}

	/**
	 * @param formatProfileFormats the formatProfileFormats to set
	 */
	public void setFormatProfileFormats(List<Format> formatProfileFormats) {
		this.formatProfileFormats = formatProfileFormats;
	}

	/**
	 * @param formatDoc the formatDoc to set
	 */
	public void setFormatDoc(FormatIdDoc formatDoc) {
		this.formatDoc = formatDoc;
	}

	/**
	 * @param formatProfilesDocs the formatProfilesDocs to set
	 */
	public void setFormatProfilesDocs(List<FormatIdDoc> formatProfilesDocs) {
		this.formatProfilesDocs = formatProfilesDocs;
	}

	/**
	 * @param formatModuleIdDoc the formatModuleIdDoc to set
	 */
	public void setFormatModuleIdDoc(FormatModuleIdDoc formatModuleIdDoc) {
		this.formatModuleIdDoc = formatModuleIdDoc;
	}

	/**
	 * @param formatProfilesIdDocs the formatProfilesIdDocs to set
	 */
	public void setFormatProfilesIdDocs(List<FormatModuleIdDoc> formatProfilesIdDocs) {
		this.formatProfilesIdDocs = formatProfilesIdDocs;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(ReportableTypeTraverser properties) {
		this.properties = properties;
	}

}
