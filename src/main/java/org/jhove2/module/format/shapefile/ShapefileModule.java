/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */

package org.jhove2.module.format.shapefile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.geotools.data.shapefile.ShapefileFeatureExtractor;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;

/**
 * JHOVE2 Shapefile module.  Used to extract information from shapefiles.
 * 
 * @author rnanders
 */
public class ShapefileModule
	extends BaseFormatModule
	implements  Validator
{
	/** Shapefile module version identifier. */
	public static final String VERSION = "2.0.0";

	/** Shapefile module release date. */
	public static final String RELEASE = "2010-09-10";

	/** Shapefile module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Board of " +
		"Trustees of the Leland Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	
	/** Directory module validation coverage. */
	public static final Coverage COVERAGE = Coverage.Selective;

	/** Shapefile validation status. */
	protected Validity isValid;

    /** Features Extracted from the shapefile. */
    protected ShapefileFeatures shapefileFeatures = new ShapefileFeatures();
    
    /** Member files indexed by file type. */
    protected TreeMap<String,File> memberFileMap = new TreeMap<String,File>();
    

	/**
	 * Instantiates a new shapefile module.
	 */
	public ShapefileModule() {
		this(null);
	}

	/**
	 * Instantiate a new <code>ShapefileModule</code>.
	 * 
	 * @param format
	 *            Shapefile format
	 */
	public ShapefileModule(Format format) {
		super(VERSION, RELEASE, RIGHTS, format);
		this.isValid = Validity.Undetermined;
	}

	/**
	 * Parse an Shapefile source unit.
	 *
	 * @param jhove2 JHOVE2 framework
	 * @param source Shapefile source unit
	 * @param input  Shapefile source input
	 * @return 0
	 * @throws IOException If an I/O exception is raised reading the source unit
	 * @throws JHOVE2Exception If parse is non-fuctional
	 * @see {org.jhove2.module.format.Parser#parse(org.jhove2.core.JHOVE2,
	 * org.jhove2.core.source.Source, core.jhove2.core.io.Input)}
	 */
	@Override
	public long parse(JHOVE2 jhove2, Source source, Input input)
		throws IOException, JHOVE2Exception
	{
		if (source instanceof ClumpSource) {
		    inventoryMemberFiles((ClumpSource) source);
		    String fileErr = verifyMemberFiles();
		    if (fileErr.length() > 0) {
	            setErrorMessage(jhove2, source, fileErr);
	            return -1;
	        }
            ShapefileFeatureExtractor sfe = null;
            try {
                sfe = new ShapefileFeatureExtractor(memberFileMap.get("SHP"));
                sfe.extractFeatures(shapefileFeatures);
            } catch (Exception e) {
                setErrorMessage(jhove2, source, "Shapefile could not be parsed: " + e.getMessage());
                return -1;
            }
 		}

		return 0;
	}

	/**
	 * Validate a Shapefile source unit.
	 *
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @param input  Source input
	 * @return UTF-8 validation status
	 * @throws JHOVE2Exception the jHOV e2 exception
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2,
	 * org.jhove2.core.source.Source, org.jhove2.core.io.Input)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source, Input input)
	    throws JHOVE2Exception
	{
	    boolean hasConsistentRecordCount = false;
	    long mainRecordCount = this.getShapefileFeatures().getShapefileRecordCount();
	    long dbfRecordCount = this.getShapefileFeatures().getDbfHeader().getRecordCount();
	    hasConsistentRecordCount = ((mainRecordCount > 0) && (mainRecordCount == dbfRecordCount));
	    if ((source.getMessages().size() == 0) && hasConsistentRecordCount) {
	        this.isValid = Validity.True;
	    } else {
	        this.isValid = Validity.False;
	    }
	    return this.isValid;
	}

	/** Get Shapefile module validation coverage.
	 * @return Shapefile module validation coverage
	 */
	@Override
	public Coverage getCoverage() {
		return COVERAGE;
	}
	

	/**
	 * Get Shapefile validation status.
	 * 
	 * @return Shapefile validation status
	 * @see org.jhove2.module.format.Validator#isValid()
	 */
	@Override
	public Validity isValid() {
		return this.isValid;
	}
	
	/**
	 * Extract a list of the member files.
	 *
	 * @param clump the clump
	 */
	private void inventoryMemberFiles(ClumpSource clump) {
       for (Source child : clump.getChildSources()) {
            if (child instanceof FileSource) {
                FileSource fileSource = (FileSource) child;
                File file = fileSource.getFile();
                String filename = file.getName().toUpperCase();
                if (filename.endsWith(".SHP.XML")) {
                    memberFileMap.put("SHP.XML", file);
                } else {
                    String extension = filename.substring(filename.lastIndexOf(".")+1);
                    memberFileMap.put(extension, file);
                }
                File mainFile = memberFileMap.get("SHP");  
                if (mainFile != null) {
                    String mainFilename = mainFile.getName();
                    shapefileFeatures.shapefileStem = mainFilename.substring(0, (mainFilename.length()-4));
                }
            }    
        }  
       shapefileFeatures.memberFiles = new ArrayList<String>();
        for (Entry<String, File> entry : memberFileMap.entrySet()) {
            shapefileFeatures.memberFiles.add(entry.getKey() + " => " + entry.getValue().getName());
        }
	}
	
	/**
	 * Verify member files.
	 *
	 * @return the string
	 */
	private String verifyMemberFiles() {
	    StringBuffer sb = new StringBuffer();
	    sb.append(verifyFile("SHP", 100));
        sb.append(verifyFile("SHX", 100));
        sb.append(verifyFile("DBF", 100));
        return sb.toString();
	}
	
	/**
	 * Verify file existence and minimum size.
	 * Geotools will enter infinte loop trying to parse too small a file
	 *
	 * @param type the type
	 * @param minSize the minimum size
	 * @return the string
	 */
	private String verifyFile(String type, int minSize) {
        File file = memberFileMap.get(type);
        if (! file.exists())
            return type + " file does not exist; ";
        if (file.length() < minSize)
            return type + " file smaller than " + minSize + " bytes; ";
        return "";
	    
	}

    /**
     * Gets the extracted shapefile features.
     * 
     * @return the reportable properties for the shapefile
     */
    @ReportableProperty(order = 1, value ="Shapefile Features")
    public ShapefileFeatures getShapefileFeatures() {
        return shapefileFeatures;
    }

    /**
     * Sets the error message.
     *
     * @param jhove2 the JHOVE2 object
     * @param source the Source object
     * @param errMsg the error message
     * @throws JHOVE2Exception if getConfigInfo were to fail
     */
    private void setErrorMessage(JHOVE2 jhove2, Source source, String errMsg) throws JHOVE2Exception {
        Object[]messageArgs = new Object[]{errMsg};
        source.addMessage(new Message(Severity.ERROR,
                Context.PROCESS,
                "org.jhove2.module.format.shapefile.ShapefileModule.parseMessage",
                messageArgs, jhove2.getConfigInfo()));

    }
}

