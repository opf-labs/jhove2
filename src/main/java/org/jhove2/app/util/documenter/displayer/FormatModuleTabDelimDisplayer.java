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
package org.jhove2.app.util.documenter.displayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.jhove2.app.util.documenter.FormatIdDoc;
import org.jhove2.app.util.documenter.FormatModuleDocumenter;
import org.jhove2.app.util.documenter.FormatModuleIdDoc;
import org.jhove2.app.util.documenter.ModuleDocumenter;
import org.jhove2.app.util.documenter.ReferenceDoc;
import org.jhove2.app.util.traverser.PropertyDoc;
import org.jhove2.app.util.traverser.ReportableTypeTraverser;
import org.jhove2.core.Document;
import org.jhove2.core.JHOVE2Exception;

/**
 * Create a set of tab-delimited files for import into the Module Documentation Word Template
 * For the identification section, the following files are created (depending on values in 
 * the Spring beans for the module, format, and profiles being documented)
 * (with Format, ProfileFormat, Module, and Profile id info (Section 2: Identification)
 * 	ModuleName_Id.txt 
 *
 * For the Reference section (Section 3, the following file is
 * ModuleName_Ref.txt
 * 
 * For the reportable properties section (7), the following file is created, based on static analysis of
 * the Module class being documented (i.e. traverse the reportable type, not the values of the Spring bean)
 * 
 * 	ModuleName_Reportable_properties.txt
 *
 * @author smorrissey
 *
 */
public class FormatModuleTabDelimDisplayer implements DocumentationDisplayer {

	public static final String FORMAT_CLASS = "org.jhove2.core.format.Format";	
	public static final String DOCUMENT_CLASS = "org.jhove2.core.Document";

	public static final String SEP = "\t";

	public static final String EXT = ".txt";

	public static final String FN_Id_IdInfo = "_Id";
	public static final String FN_Ref_ ="_Ref";
	public static final String FN_Props = "_Reportable_properties";

	public static final String HDR_Id_Primaryformat = "Primary format or format family";
	public static final String HDR_Id_Profileformat = "Format profile or specify format with a family";
	public static final String R1_ID_Format = "Canonical format name";
	public static final String R2_ID_Format = "Alias format name(s)";
	public static final String R3_ID_Format = "Canonical format identifier";
	public static final String R4_ID_Format = "Alias format identifier(s)";

	public static final String HDR_Id_Module = " module";
	public static final String HDR_Id_Profile = " profile";
	public static final String R1_Id_module = "JHOVE2 module name";
	public static final String R2_Id_module = "JHOVE2 module identifier";
	public static final String R3_Id_module = "JHOVE2 module class";

	public static final String HDR_Prop_class = "Class";     /* " [class/interface]"; */
	public static final String HDR_Prop_prop = "Property";   /* Remove leading SP */
	public static final String HDR_Prop_message = "Message"; /* Remove leading SP */
	public static final String R1_Prop="Identifier";
	public static final String R2_Prop="Type";
	public static final String R3_Prop="Description";
	public static final String R4_Prop="Reference";
	public static final String MESSAGE_ID_PREFIX = "http://jhove2.org/terms/message/";
	
	public static final String HDR_Ref_Authoritative_pre = "For the purposes of the JHOVE2 ";
	public static final String HDR_Ref_Authoritative_post = " module the following is considered authoratative:\n";
	public static final String HDR_Ref_Other_pre = "The following ";
	public static final String HDR_Ref_Other_post = " specifications are considered ";

	protected File outputDir;
	protected String moduleName;
	protected String formatName;
	protected String pathSep;
	protected String dirPath;
	protected String baseFileName;

	public FormatModuleTabDelimDisplayer(){
		super();
		this.pathSep = System.getProperty("file.separator");
	}


	@Override
	public void displayDocumentation(ModuleDocumenter documenter)
	throws JHOVE2Exception {
		FormatModuleDocumenter fmDocumenter = (FormatModuleDocumenter)documenter;
		this.moduleName = fmDocumenter.getFormatModule().getReportableName();
		this.formatName = fmDocumenter.getFormat().getName();
		try {
			dirPath = this.outputDir.getCanonicalPath();
			dirPath = dirPath.concat(this.pathSep);
		} catch (IOException e1) {
			throw new JHOVE2Exception("IOException creating dirPath",e1);
		}
		baseFileName = dirPath.concat(this.moduleName);
		PrintWriter out = null;
		// Section 2 (Identification)
		String docFilePath = baseFileName.concat(FN_Id_IdInfo).concat(EXT);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(docFilePath)));			
		} catch (IOException e) {
			throw new JHOVE2Exception("IOException creating PrintWriter for path "
					+ docFilePath, e);
		}
		this.displayFormatsIdInfo(fmDocumenter.getFormatDoc(), 
				fmDocumenter.getFormatProfilesDocs(),
				out);
		this.displayModuleIdInfo(
				fmDocumenter.getFormatModuleIdDoc(),
				fmDocumenter.getFormatProfilesIdDocs(),
				out);
		out.flush();
		out.close();
		// Section 3  References
		docFilePath = baseFileName.concat(FN_Props).concat(EXT);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(docFilePath)));			
		} catch (IOException e) {
			throw new JHOVE2Exception("IOException creating PrintWriter for path "
					+ docFilePath, e);
		}
		this.displayReportableProperties(fmDocumenter.getProperties(), out);
		out.flush();
		out.close();
		// Section 7  Reportable properties
		docFilePath = baseFileName.concat(FN_Ref_).concat(EXT);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(docFilePath)));			
		} catch (IOException e) {
			throw new JHOVE2Exception("IOException creating PrintWriter for path "
					+ docFilePath, e);
		}
		this.displayReferences(this.formatName, fmDocumenter.getFormatDoc(), 
				fmDocumenter.getFormatProfilesDocs(),
				out);
		out.flush();
		out.close();
		return;
	}
	/** 
	 * Display reference information (Section 3)
	 * @param formatName Name of format to which references apply
	 * @param formatDoc FormatIdDoc with information about the Format
	 * @param formatProfilesDocs List<FormatIdDoc> with information about profile Formats
	 * @param out PrintWriter to which output is displayed
	 * @throws JHOVE2Exception
	 */
	protected void displayReferences(String formatName, FormatIdDoc formatDoc,
			List<FormatIdDoc> formatProfilesDocs, PrintWriter out) 
	throws JHOVE2Exception{
		TreeSet<Document.Intention>intents = new TreeSet<Document.Intention>();
		if (formatDoc.getReferences()!= null){
			intents.addAll(formatDoc.getReferences().keySet());
		}
		for (FormatIdDoc profile: formatProfilesDocs){
			if (profile.getReferences()!= null){
				intents.addAll(profile.getReferences().keySet());
			}
		}
		for (Document.Intention intent:intents){
			ArrayList<ReferenceDoc>refs = new ArrayList<ReferenceDoc>();
			if (formatDoc.getReferences()!= null && formatDoc.getReferences().containsKey(intent)){
				refs.addAll(formatDoc.getReferences().get(intent));
			}
			for (FormatIdDoc profile: formatProfilesDocs){
				if (profile.getReferences()!= null && profile.getReferences().containsKey(intent)){
					refs.addAll(profile.getReferences().get(intent));
				}
			}
			this.printReferences(formatName, intent, refs, out);
		}
		return;
	}
	/**
	 * Formats and prints reference information for all Documents associated with a given Format and Document.Intention
	 * @param formatName Name of format to which references apply
	 * @param intent Document.Intention of the reference
	 * @param refs List of all references of this Document.Intention for this format
	 * @param out PrintWriter to which output is displayed
	 * @throws JHOVE2Exception
	 */
	protected void printReferences(String formatName, Document.Intention intent, List<ReferenceDoc>refs,
			PrintWriter out)
	throws JHOVE2Exception{
		String headerPre;
		String headerPost;
		if (intent == Document.Intention.Authoritative){
			headerPre = HDR_Ref_Authoritative_pre;
			headerPost = HDR_Ref_Authoritative_post;
		}
		else {
			headerPre = HDR_Ref_Other_pre;
			headerPost = HDR_Ref_Other_post + intent.toString() + ";\n";
		}
		StringBuffer sb = new StringBuffer(headerPre);
		sb.append(formatName);
		sb.append(headerPost);
		out.print(sb.toString());
		for (ReferenceDoc ref:refs){
			sb = new StringBuffer(SEP);
			sb.append(ref.getBiblioInfo());
			sb.append("\n");
			for (String id:ref.getIdentifiers()){
				sb.append(SEP);
				sb.append(id);
				sb.append("\n");
			}
			out.print(sb.toString());
		}
		return;
	}
	/**
	 * Displays identfication information for Module Format and Profile Formats
	 * @param formatDoc dentfication information for Module Format
	 * @param formatProfilesDocs dentfication information for Profile Formats
	 * @param out PrintWriter to which output is displayed
	 * @throws JHOVE2Exception
	 */
	protected void displayFormatsIdInfo(FormatIdDoc formatDoc, 
			List<FormatIdDoc> formatProfilesDocs, PrintWriter out)
	throws JHOVE2Exception{
		this.printFormatIdInfo(HDR_Id_Primaryformat, out, formatDoc);	
		for (int i=0; i<formatProfilesDocs.size();i++){
			this.printFormatIdInfo(HDR_Id_Profileformat, out, formatProfilesDocs.get(i));
		}
		return;
	}
	/**
	 * Displays identification information for Module and associated profiles 
	 * @param formatModuleIdDoc FormatModuleIdDoc with id info for module
	 * @param formatProfilesIdDocs List<FormatModuleIdDoc> with id info for profiles
	 * @param out PrintWriter to which output is displayed
	 * @throws JHOVE2Exception
	 */
	protected void displayModuleIdInfo(
			FormatModuleIdDoc formatModuleIdDoc,
			List<FormatModuleIdDoc> formatProfilesIdDocs,
			PrintWriter out)
	throws JHOVE2Exception{
		String hdrString = formatModuleIdDoc.getModuleName().concat(HDR_Id_Module);
		this.printModuleIdInfo(hdrString, out, formatModuleIdDoc);
		for (FormatModuleIdDoc fmid:formatProfilesIdDocs){
			hdrString = fmid.getModuleName().concat(HDR_Id_Profile);
			this.printModuleIdInfo(hdrString, out, fmid);
		}
		return;
	}
	/**
	 * Displays reportable properties of Format Module
	 * @param properties ReportableTypeTraverser with reportable properties informations
	 * @param out PrintWriter to which output is displayed
	 * @throws JHOVE2Exception
	 */
	protected void displayReportableProperties(ReportableTypeTraverser properties, PrintWriter out)
	throws JHOVE2Exception{
		for (Map.Entry<String, ReportableTypeTraverser> entry:
			properties.getReportableFeaturesDocumenter().entrySet()){
			String className = entry.getKey();
			int i = className.lastIndexOf(".");
			if (i>0){
				className = className.substring(i+1);
			}
			this.printReportablePropInfo(className, out, 
					entry.getValue().getReportablePropertiesInfo());
		}
		return;	
	}
	/**
	 * Prints reportable property information for single class
	 * @param className Simple name of class
	 * @param out  PrintWriter to which output is displayed
	 * @param reportablePropertiesInfo List<PropertyDoc> with information about reportable properties
	 * @throws JHOVE2Exception
	 */
	protected void printReportablePropInfo(String className,
			PrintWriter out, List<PropertyDoc> reportablePropertiesInfo)
	throws JHOVE2Exception{
		String propNameSuffix;
		out.println(HDR_Prop_class + SEP + className); /* out.println(className.concat(HDR_Prop_class)); */
		StringBuffer sb;
		for (PropertyDoc prop:reportablePropertiesInfo){
			if (prop.getId().startsWith(MESSAGE_ID_PREFIX)){
				propNameSuffix = HDR_Prop_message;
			}
			else {
				propNameSuffix = HDR_Prop_prop;
			}
			sb = new StringBuffer(propNameSuffix); /* prop.getName()); */
			sb.append(SEP);                        /* Added */
			sb.append(prop.getName());             /* propNameSuffix); */
			out.println(sb.toString());
			sb = new StringBuffer(R1_Prop);
			sb.append(SEP);
			sb.append(prop.getId());
			sb.append("\n");
			sb.append(R2_Prop);
			sb.append(SEP);
			sb.append(prop.getTypeString());
			sb.append("\n");
			sb.append(R3_Prop);
			sb.append(SEP);
			sb.append(prop.getDesc());
			sb.append("\n");
			sb.append(R4_Prop);
			sb.append(SEP);
			sb.append(prop.getRef());
			out.println(sb.toString());
		}			
		return;
	}

	/**
	 * Prints identification information for single module or profile
	 * @param hdrString Title for output information
	 * @param out PrintWriter to which output is displayed
	 * @param formatModDoc FormatModuleIdDoc with id information for module or profile
	 * @throws JHOVE2Exception
	 */
	protected void printModuleIdInfo(String hdrString, PrintWriter out, 
			FormatModuleIdDoc formatModDoc)
	throws JHOVE2Exception{
		out.println(hdrString);
		StringBuffer sb = new StringBuffer(R1_Id_module);
		sb.append(SEP);
		sb.append(formatModDoc.getModuleName());
		out.println(sb.toString());
		sb = new StringBuffer(R2_Id_module);
		sb.append(SEP);
		sb.append(formatModDoc.getModuleId());
		out.println(sb.toString());
		sb = new StringBuffer(R3_Id_module);
		sb.append(SEP);
		sb.append(formatModDoc.getModuleClassName());
		out.println(sb.toString());
		return;

	}
	/**
	 * Prints identification information for single Format
	 * @param hdrString Title for output information
	 * @param out PrintWriter to which output is displayed
	 * @param formatDoc FormatIdDoc with id info for Format
	 * @throws JHOVE2Exception
	 */
	protected void printFormatIdInfo(String hdrString, PrintWriter out, FormatIdDoc formatDoc)
	throws JHOVE2Exception{
		out.println(hdrString);
		StringBuffer sb = new StringBuffer(R1_ID_Format);
		sb.append(SEP);
		sb.append(formatDoc.getCanonicalFormatName());
		out.println(sb.toString());
		sb = new StringBuffer(R2_ID_Format);
		sb.append(SEP);
		int i=0;
		for (String aliasName:formatDoc.getAliasFormatNames()){
			sb.append(aliasName);
			out.println(sb.toString());
			i++;
			sb = new StringBuffer(SEP);				
		}
		if (i==0){
			out.println(sb.toString());
		}
		sb = new StringBuffer(R3_ID_Format);
		sb.append(SEP);
		sb.append(formatDoc.getCanonicalFormatIdentifier());
		out.println(sb.toString());
		sb = new StringBuffer(R4_ID_Format);
		sb.append(SEP);
		i=0;
		for (String aliasId:formatDoc.getAliasFormatIdentifiers()){
			sb.append(aliasId);
			out.println(sb.toString());
			i++;
			sb = new StringBuffer(SEP);				
		}
		if (i==0){
			out.println(sb.toString());
		}
		return;
	}

	@Override
	public File getOutputDir() {
		return this.outputDir;
	}


	@Override
	public void setOutputDir(File dir) {
		this.outputDir = dir;
	}

}
