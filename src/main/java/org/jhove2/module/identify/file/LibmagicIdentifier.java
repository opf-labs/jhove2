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

package org.jhove2.module.identify.file;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.format.FormatIdentification.Confidence;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.identify.AbstractFileSourceIdentifier;
import org.jhove2.module.identify.SourceIdentifier;
import org.jhove2.persist.ModuleAccessor;

import com.sleepycat.persist.model.Persistent;

/**
 * A {@link SourceIdentifier source identifier} that wraps the
 * libmagic dynamic library of the UNIX <code>file</code> identifier
 * tool.
 *
 * @author hbian
 */
@Persistent
public class LibmagicIdentifier extends AbstractFileSourceIdentifier
	implements SourceIdentifier {

	/** Module version identifier. */
    public final static String VERSION = "2.0.0";
    /** Module release date. */
    public final static String RELEASE = "2011-01-31";
    /** Module rights statement. */
    public final static String RIGHTS =
            "Copyright 2010-2011 by Bibliotheque nationale de France. " +
            "Available under the terms of the BSD license.";

    /** Magic database file extension. */
    private final static String MAGIC_DB_EXTENSION = ".mgc";

    /** Map MIME types to JHOVE2 format identifiers. */
    private static Map<String,I8R> mimeTypesToFormatIds;

    /** The directory where source Magic definition files are stored. */
    private File magicFileDir = null;
    /** The compiled Magic file generated by this class. */
    private File compiledMagic = null;

    /** Instance of Libmagic JNA wrapper. */
    private static transient LibmagicJnaWrapper libmagicWrapper;

	/**Instantiate a new <code>DROIDIdentifier</code> module that wraps DROID.
	 * @throws JHOVE2Exception 
	 */
	public LibmagicIdentifier() throws JHOVE2Exception {
		this(null);
	}
	
	/**Instantiate a new <code>DROIDIdentifier</code> module that wraps DROID.
	 * @param moduleAccessor persistence manager
	 * @throws JHOVE2Exception 
	 */
	public LibmagicIdentifier(ModuleAccessor moduleAccessor)
			throws JHOVE2Exception {
		super(VERSION, RELEASE, RIGHTS, Scope.Generic, moduleAccessor);
	}
	
    /**
     * Initializes this instance. This method should be called once
     * all the configuration properties (dependency injection) set.
     *
     * @throws JHOVE2Exception if any error occurred.
     */
    public void init() throws JHOVE2Exception {
    }

    /**
     * Initialize module if it has not been done yet.
     * @throws JHOVE2Exception if an error occurs during initialization
     */
    public void checkIfInitialized() throws JHOVE2Exception {
    	if (libmagicWrapper == null) {
        	// Initialize libmagic wrapper.
            libmagicWrapper = new LibmagicJnaWrapper();

            String compiledMagicPath = null;
            if (this.magicFileDir != null) {
                // Magic source directory set. => Compile magic files.
            	if (libmagicWrapper.compile(
                                    magicFileDir.getAbsolutePath()) != 0) {
                    throw new JHOVE2Exception("Magic file compile error: "
                                              + libmagicWrapper.getError());
                }
                // Look for compiled magic file. Its location varies according to
                // some compilation options of libmagic.
                // Was it stored in the current directory?
                String dbName = magicFileDir.getName() + MAGIC_DB_EXTENSION;
                File magicPath = new File(dbName);
                if (! magicPath.isFile()) {
                    // Nope! Must be in the parent dir. of the source definitions.
                    magicPath = new File(magicFileDir.getParentFile(), dbName);
                }
                compiledMagicPath = magicPath.getAbsolutePath();
                // Keep compiled file ref. for shutdown time cleanup.
                compiledMagic = magicPath;
                compiledMagic.deleteOnExit();
            }
            // Load magic definitions. 
            if (libmagicWrapper.load(compiledMagicPath) != 0) {
                String fileRef = (compiledMagicPath != null)?
                                    "Magic database \"" + compiledMagicPath + '"':
                                    "Default magic database";
                throw new JHOVE2Exception(fileRef + " load error: "
                                                  + libmagicWrapper.getError());
            }
    	}
    }

    /**
     * Shuts down this instance, releasing used resources.
     */
    public void shutdown() {
    	if (libmagicWrapper != null) {
            libmagicWrapper.close();
            libmagicWrapper = null;
    	}
        if (compiledMagic != null) {
            // Delete compiled magic file.
            compiledMagic.delete();
            compiledMagic = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Set<FormatIdentification> identify(JHOVE2 jhove2, Source source,
    		Input input) throws IOException, JHOVE2Exception {
    	checkIfInitialized();
        // Extract MIME type and encoding using libmagic.
        String mimeType = null;
        /* The following code is not compatible with the current JHove2 core.
        if ((input != null) && (input.getSize() < input.getMaxBufferSize())) {
            // Memory buffer
            mimeType = libmagicWrapper.getMimeType(
                                            input.getBuffer(), input.getSize());
        }
        else {
            // Too large to fit in a single buffer. => Use temp. file.
            mimeType = libmagicWrapper.getMimeType(
                                            source.getFile().getAbsolutePath());
        }
        */
        if (input != null) {
            mimeType = libmagicWrapper.getMimeType(
                    source.getFile().getAbsolutePath());
        }

        String typeWithEncoding = null;
        if (mimeType != null) {
            // Extract character encoding.
            int i = mimeType.indexOf("; charset");
            if (i != -1) {
                // Found! => Reconstruct charset information the JHove2 way...
                String charset = mimeType.substring(
                                                mimeType.indexOf('=', i+1) + 1);
                mimeType = mimeType.substring(0, i);
                typeWithEncoding = mimeType;
                if ("text/plain".equals(mimeType)) {
                    typeWithEncoding +=
                                "; charset=\"" + charset.toUpperCase() + '"';
                }
                // Else: ignore "charset=BINARY" present for non-text files.
            }
            else {
                typeWithEncoding = mimeType;
            }
        }

        I8R jhoveId = null;
        I8R fileId  = null;
        Confidence confidence = Confidence.Negative;
        // Convert MIME type into JHove2 format identifier.
        if ((mimeType != null) && (mimeType.length() != 0)) {
            fileId  = new I8R(mimeType, I8R.Namespace.MIME);
            jhoveId = getJhoveFormatId(jhove2, typeWithEncoding);
            if (jhoveId != null) {
                confidence = Confidence.PositiveSpecific;
            }
        }
        Set<FormatIdentification> presumptiveFormatIds =
                                            new TreeSet<FormatIdentification>();
        presumptiveFormatIds.add(
                new FormatIdentification(jhoveId, confidence,
                                getReportableIdentifier(), fileId, null));
        return presumptiveFormatIds;
    }

    /**
     * Returns the {@link I8R identifier} of the JHove2 format
     * corresponding to the specified MIME type.
     * @param  jhove2     the JHove2 characterization context.
     * @param  mimeType   the MIME type to convert.
     *
     * @return the JHove2 format identifier or <code>null</code> if no
     *         matching JHove2 format was defined in the configuration
     *         for the specified MIME type.
     * @throws JHOVE2Exception if any error occurred while reading the
     *         JHove2 configuration.
     */
    private static I8R getJhoveFormatId(JHOVE2 jhove2, String mimeType)
    		throws JHOVE2Exception {
        if (mimeTypesToFormatIds == null) {
            Map<String,I8R> m = new HashMap<String,I8R>();

            Map<String,String> ids = jhove2.getConfigInfo()
                                  .getFormatAliasIdsToJ2Ids(I8R.Namespace.MIME);
            for(Map.Entry<String,String> e : ids.entrySet()) {
                m.put(e.getKey(), new I8R(e.getValue()));
            }
            mimeTypesToFormatIds = Collections.unmodifiableMap(m);
        }
        return mimeTypesToFormatIds.get(mimeType);
    }

    /**
     * <i>Dependency injection</i> Sets the directory where to look
     * for Magic definition files.  If this property is set, this class
     * compiles the definition files prior attempting identification.
     * @param  magicDir   the directory holding the magic definition
     *                    files or <code>null</code> to use the default
     *                    already-compiled definitions.
     */
    public void setMagicFileDir(File magicDir) {
        magicFileDir = magicDir;
    }
}
