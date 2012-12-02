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

package org.jhove2.module.format.arc;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.format.FormatIdentification.Confidence;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.Reportable;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.module.Module;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.arc.properties.ArcRecordData;
import org.jhove2.module.format.gzip.GzipModule;
import org.jhove2.module.format.gzip.GzipModule.GZipOffsetProperty;
import org.jhove2.persist.FormatModuleAccessor;
import org.jwat.arc.ArcReader;
import org.jwat.arc.ArcReaderFactory;
import org.jwat.arc.ArcRecordBase;
import org.jwat.common.Diagnosis;
import org.jwat.common.HttpHeader;
import org.jwat.common.Payload;
import org.jwat.common.PayloadWithHeaderAbstract;

import com.sleepycat.persist.model.Persistent;

/**
 * JHOVE2 ARC module. This class is mostly a JHOVE2 wrapper that uses
 * the JWAT package for the actual ARC validation.
 *
 * @author lbihanic, selghissassi, nicl
 */
@Persistent
public class ArcModule extends BaseFormatModule implements Validator {

    /** Module version identifier. */
    public static final String VERSION = "2.1.0";

    /** Module release date. */
    public static final String RELEASE = "2012-10-31";

    /** Module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Selective;

    /** Whether to recursively characterize ARC record objects. */
    private boolean recurse = true;

    private boolean bComputeBlockDigest = false;
    private String blockDigestAlgorithm;
    private String blockDigestEncoding;

    private boolean bComputePayloadDigest = false;
    private String payloadDigestAlgorithm;
    private String payloadDigestEncoding;

    /**
     * Stores a mapping of all Format aliases in the
     * {@link org.jhove2.core.I8R.Namespace.MIME MIME namespace}
     * available via configuration to the JHOVE2
     * {@link org.jhove2.core.I8R} for that Format
     */
    private static transient Map<String, FormatIdentification> jhove2Ids = null;

    /** Validation status. */
    private Validity isValid;

    /** Used protocols. */
    private Map<String, Integer> protocols =
                                new HashMap<String, Integer>();

    /** The name of the ARC file. */
    private String arcFileName;

    /** The size of the ARC file, in bytes. */
    private Long arcFileSize;

    /** Last modified date of the ARC file. */
    private Date arcFileLastModified;

    /** The number of records. */
    private int recordNumber;

    /** The number or ARC records. */
    private int arcRecordNumber;

    /** The amount of bytes consumed by the ArcReader. */
    private long arcReaderConsumedBytes;

    /** File version from version block. */
    private String arcFileVersion;

    /** Block description version, null unless all records have the same block description version. */
    private Integer arcBlockDescVersion;

    /** Versions encountered and their usage count. */
    private Map<Integer, Integer> blockDescVersions = new TreeMap<Integer, Integer>();

    /**
     * Instantiate a new <code>ArcModule</code> instance.
     * This constructor is used by the Spring framework.
     * @param format Jhove2 Format used by this module to handle ARC
     * @param formatModuleAccessor FormatModuleAccessor to manage access to Format Profiles
     */
    public ArcModule(Format format,
            FormatModuleAccessor formatModuleAccessor) {
        super(VERSION, RELEASE, RIGHTS, format, formatModuleAccessor);
        this.isValid = Validity.Undetermined;
    }

    /**
     * Instantiate a new <code>ArcModule</code> instance.
     * This constructor is used by the persistence layer.
     */
    public ArcModule() {
      this(null, null);
    }

    //------------------------------------------------------------------------
    // BaseFormatModule contract support
    //------------------------------------------------------------------------

    /*
     * Parse an ARC file/record.
     * @see org.jhove2.module.format.BaseFormatModule#parse(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.core.io.Input)
     */
    @Override
    public long parse(JHOVE2 jhove2,Source source, Input input)
                        throws IOException, EOFException, JHOVE2Exception {
        /*
         * Cache Content-Types to JHove2 FormatIdentifications.
         */
        if (jhove2Ids == null) {
            Map<String,String> ids =
                jhove2.getConfigInfo().getFormatAliasIdsToJ2Ids(I8R.Namespace.MIME);
            TreeMap<String, FormatIdentification> idsTemp =
                new TreeMap<String, FormatIdentification>();
            for (Entry<String, String> e : ids.entrySet()){
                idsTemp.put(e.getKey().toLowerCase(),
                            new FormatIdentification(new I8R(e.getValue()),
                                                     Confidence.Tentative));
            }
            jhove2Ids = Collections.unmodifiableSortedMap(idsTemp);
        }
        /*
         * SourceFactory for later use.
         */
        SourceFactory sourceFactory = jhove2.getSourceFactory();
        if (sourceFactory == null) {
            throw new JHOVE2Exception("INTERNAL ERROR - JHOVE2 SourceFactory is null");
        }
        /*
         * Module init.
         */
        long consumed = 0L;
        isValid = Validity.Undetermined;
        // No effect unless read methods on the input object are called.
        input.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        /*
         * Module context.
         */
        GzipModule gzipMod = null;
        ArcModule arcMod = null;
        Module mod;
        Source parentSrc = source.getParentSource();
        if (parentSrc != null) {
            List<Module> parentMods = parentSrc.getModules();
            for (int i=0; i<parentMods.size(); ++i) {
                mod = parentMods.get(i);
                if (mod instanceof GzipModule) {
                    gzipMod = (GzipModule)mod;
                    // Lookup the the GZipModule which is on the call stack.
                    // Required since the JHove2 lookup returns a new instance
                    // populated with persisted values and not the instance on
                    // the call stack.
                    gzipMod = GzipModule.gzipMap.get(gzipMod.instanceId);
                }
                if (mod instanceof ArcModule) {
                    // The same goes for the WarcModule except we do not need
                    // any transient fields here.
                    arcMod = (ArcModule)mod;
                }
            }
        }
        /*
         * Reportable: Filename, file size, etc.
         */
        if (!source.isTemp()) {
            arcFileName = source.getFile().getName();
            arcFileSize = source.getFile().length();
            arcFileLastModified = new Date(source.getFile().lastModified());
        } else if (parentSrc != null && !parentSrc.isTemp()) {
            arcFileName = parentSrc.getFile().getName();
            arcFileSize = parentSrc.getFile().length();
            arcFileLastModified = new Date(parentSrc.getFile().lastModified());
        }
        /*
         * Read some ARC records.
         */
        ArcReader reader = null;
        if (gzipMod != null) {
        	// Obtain GZip startOffset from dummy property.
        	long offset = -1;
        	List<Reportable> gzipProps = source.getExtraProperties();
        	Reportable prop;
        	int i = 0;
        	while (i<gzipProps.size()) {
        		prop = gzipProps.get(i);
        		if (prop instanceof GZipOffsetProperty) {
        			offset = ((GZipOffsetProperty)prop).offset;
        			gzipProps.remove(i);
        			// ...
        			source.getSourceAccessor().persistSource(source);
        		}
        		else {
        			++i;
        		}
        	}
        	// This should probably be changed according to success reading VersionBlock.
            gzipMod.presumptiveFormat = new FormatIdentification(format.getIdentifier(), Confidence.Tentative);
            /*
             * GZip compressed.
             */
            reader = (ArcReader)gzipMod.reader;
            if (reader == null) {
                reader = ArcReaderFactory.getReaderUncompressed();
                setDigestOptions(reader);
                gzipMod.reader = reader;
            }
            if (arcMod == null) {
                /*
                 * First record. (Unless the parent modules are not correct!)
                 */
                mod = parentSrc.addModule(this);
                parseRecordsCompressed(jhove2, sourceFactory, source, reader, offset, true);
            } else {
                arcMod.parseRecordsCompressed(jhove2, sourceFactory, source, reader, offset, false);
                // Validity
                if (arcMod.isValid != Validity.False) {
                    if (reader.isCompliant()) {
                        arcMod.isValid = Validity.True;
                    } else {
                        arcMod.isValid = Validity.False;
                    }
                }
                arcMod.arcReaderConsumedBytes = reader.getConsumed();
                if (arcMod.blockDescVersions.size() == 1) {
                	Entry<Integer, Integer> entry = arcMod.blockDescVersions.entrySet().iterator().next();
                    if (entry.getValue() == arcMod.recordNumber) {
                    	arcMod.arcBlockDescVersion = entry.getKey();
                    }
                }
                arcMod = (ArcModule)arcMod.getModuleAccessor().persistModule(arcMod);
                // Remove ArcModule from source instance since we added one to the parent source.
                this.setParentSourceId(null);
                source = source.getSourceAccessor().persistSource(source);
            }
            consumed = reader.getConsumed();
        } else {
            /*
             * Not GZip compressed.
             */
            reader = ArcReaderFactory.getReaderUncompressed(source.getInputStream(), 8192);
            setDigestOptions(reader);
            parseRecordsUncompressed(jhove2, sourceFactory, source, reader, true);
            reader.close();
            consumed = reader.getConsumed();
            // Reportable.
            arcReaderConsumedBytes = reader.getConsumed();
            if (blockDescVersions.size() == 1) {
            	Entry<Integer, Integer> entry = blockDescVersions.entrySet().iterator().next();
                if (entry.getValue() == recordNumber) {
                	arcBlockDescVersion = entry.getKey();
                }
            }
            /*
             * Validity.
             */
            if (isValid != Validity.False) {
                if (reader.isCompliant()) {
                    isValid = Validity.True;
                } else {
                    isValid = Validity.False;
                }
            }
        }
        /*
         * Consumed.
         */
        return consumed;
    }

    /**
     * Set digest options for ARC reader.
     * @param reader ARC reader instance
     */
    protected void setDigestOptions(ArcReader reader) throws JHOVE2Exception {
        reader.setBlockDigestEnabled(bComputeBlockDigest);
        reader.setPayloadDigestEnabled(bComputePayloadDigest);
        if (!reader.setBlockDigestAlgorithm(blockDigestAlgorithm)) {
            throw new JHOVE2Exception("Invalid block digest algorithm: " + blockDigestAlgorithm);
        }
        if (!reader.setPayloadDigestAlgorithm(payloadDigestAlgorithm)) {
            throw new JHOVE2Exception("Invalid payload digest algorithm: " + payloadDigestAlgorithm);
        }
        reader.setBlockDigestEncoding(blockDigestEncoding);
        reader.setPayloadDigestEncoding(payloadDigestEncoding);
    }

    /**
     * Parse ARC records that are not encased in GZip entries. Parsing should
     * should be straight forward with all records accessible through the same
     * source. The version block is only read if the reader was initialized
     * during this module call.
     * @param jhove2 the JHove2 characterization context
     * @param sourceFactory JHove2 source factory
     * @param parentSource ARC source unit
     * @param reader ARC reader used to parse records
     * @param bReadVersion read version block first or go straight to parsing records
     * @throws EOFException if EOF occurs prematurely
     * @throws IOException if an IO error occurs while processing
     * @throws JHOVE2Exception if a serious problem needs to be reported
     */
    protected void parseRecordsUncompressed(JHOVE2 jhove2, SourceFactory sourceFactory,
            Source parentSource, ArcReader reader, boolean bReadVersion)
                    throws EOFException, IOException, JHOVE2Exception {
        ArcRecordBase record;
        // Ensure a ARC reader could be instantiated.
        if (reader != null) {
            parentSource.setIsAggregate(true);
            /*
             * Loop through available records.
             */
            while ((record = reader.getNextRecord()) != null) {
                processRecord(jhove2, sourceFactory, parentSource, record);
            }
        } else {
            throw new JHOVE2Exception("ArcReader is null");
        }
    }

    /**
     * Parse ARC record(s) where the source has been identified as a source of
     * a GZip module instance. Since each record will presumably be parse from
     * a different source alternative methods in the ARC reader will be used.
     * The version block is only read if the reader was initialized
     * during this module call.
     * @param jhove2 the JHove2 characterization context
     * @param sourceFactory JHove2 source factory
     * @param parentSource ARC source unit
     * @param reader ARC reader used to parse records
     * @param offset record offset relative to input stream
     * @param bReadVersion read version block first or go straight to parsing records
     * @throws EOFException if EOF occurs prematurely
     * @throws IOException if an IO error occurs while processing
     * @throws JHOVE2Exception if a serious problem needs to be reported
     */
    protected void parseRecordsCompressed(JHOVE2 jhove2, SourceFactory sourceFactory,
            Source parentSource, ArcReader reader, Long offset, boolean bReadVersion)
                    throws EOFException, IOException, JHOVE2Exception {
        ArcRecordBase record;
        // Ensure a ARC reader could be instantiated.
        if (reader != null) {
            parentSource.setIsAggregate(true);
            InputStream in = parentSource.getInputStream();
            /*
             * Loop through available records.
             */
            while ((record = reader.getNextRecordFrom(in, offset, 8192)) != null) {
                processRecord(jhove2, sourceFactory, parentSource, record);
            }
        } else {
            throw new JHOVE2Exception("ArcReader is null");
        }
    }

    /**
     * Process an ARC record. Adds an <code>ArcRecordSource</code> child
     * to the supplied input source. Relevant reportable properties are added
     * to the <code>ArcRecordSource</code>. If there is a payload present in
     * the record, steps are taken to characterize it. The content-type of the
     * payload is established from the record itself or a leading
     * http response. The content-type is added as a presumptive format on the
     * embedded source.
     * @param jhove2 the JHove2 characterization context
     * @param sourceFactory JHove2 source factory
     * @param parentSource ARC source unit
     * @param record ARC record from ARC reader
     * @throws EOFException if EOF occurs prematurely
     * @throws IOException if an IO error occurs while processing
     * @throws JHOVE2Exception if a serious problem needs to be reported
     */
    protected void processRecord(JHOVE2 jhove2, SourceFactory sourceFactory,
            Source parentSource, ArcRecordBase record)
                    throws EOFException, IOException, JHOVE2Exception {
        Payload payload;
        PayloadWithHeaderAbstract payloadHeaderWrapped;
        HttpHeader httpHeader;
        InputStream payload_stream;
        ArcRecordData recordData;
        String contentType;
        FormatIdentification formatId;

        contentType = record.header.contentTypeStr;
        /*
         * ARC Record Source.
         */
        Source recordSrc = new ArcRecordSource();
        recordSrc.setSourceAccessor(sourceFactory.createSourceAccessor(recordSrc));
        recordSrc.setDeleteTempFileOnClose(jhove2.getInvocation().getDeleteTempFilesOnClose());
        recordSrc = parentSource.addChildSource(recordSrc);
        ++recordNumber;
        /*
         * Version. (The first ARC record should be a (filedesc) version block record)
         */
        if (recordNumber == 1) {
        	if (record.recordType == ArcRecordBase.RT_VERSION_BLOCK) {
        		if (record.versionHeader.bValidVersionFormat) {
        			arcFileVersion = record.versionHeader.versionStr;
        		}
        	}
        }
        if (record.recordType == ArcRecordBase.RT_ARC_RECORD) {
        	++arcRecordNumber;
        }
        Integer count = blockDescVersions.get(record.header.recordFieldVersion);
        if (count == null) {
        	count = 0;
        }
        ++count;
        blockDescVersions.put(record.header.recordFieldVersion, count);
        /*
         * Prepare payload.
         */
        payload = record.getPayload();
        httpHeader = null;
        payload_stream = null;
        if (payload != null) {
        	payloadHeaderWrapped = payload.getPayloadHeaderWrapped();
            if (payloadHeaderWrapped instanceof HttpHeader) {
                httpHeader = (HttpHeader)payloadHeaderWrapped;
            }
            if (httpHeader == null) {
                payload_stream = payload.getInputStream();
            } else {
                contentType = httpHeader.getProtocolContentType();
                payload_stream = httpHeader.getPayloadInputStream();
            }
        }
        /*
         * Decide on Jhove2 format from contentType information.
         */
        if (contentType != null) {
            int idx = contentType.indexOf(';');
            if (idx >= 0) {
                    contentType = contentType.substring(0, idx);
            }
        }
        formatId = null;
        if (contentType != null) {
            formatId = jhove2Ids.get(contentType.toLowerCase());
        }
        /*
         * Characterize payload.
         */
        if (recurse && payload_stream != null && !record.hasPseudoEmptyPayload()) {
            characterizePayload(jhove2, sourceFactory, recordSrc, payload_stream, formatId);
        }
        if (payload_stream != null) {
            payload_stream.close();
        }
        /*
         * Close record to finish validation.
         */
        if (payload != null) {
            payload.close();
        }
        record.close();
        /*
         * Properties.
         */
        recordData = new ArcRecordData(record);
        recordSrc.addExtraProperties(recordData.getArcRecordBaseProperties());
        switch (record.recordType) {
        case ArcRecordBase.RT_VERSION_BLOCK:
            recordSrc.addExtraProperties(recordData.getArcVersionBlockProperties());
        	break;
        case ArcRecordBase.RT_ARC_RECORD:
            recordSrc.addExtraProperties(recordData.getArcRecordProperties());
        	break;
        }
        // Update protocol statistics.
        if (recordData.protocol != null) {
            updateProtocols(recordData);
        }
        /*
         * Report errors.
         */
        reportValidationErrors(recordSrc, record, jhove2);
        recordSrc.close();
    }

    protected void updateProtocols(ArcRecordData recordData) {
        int number = 1;
        if (protocols.containsKey(recordData.protocol)) {
            number += protocols.get(recordData.protocol);
        }
        protocols.put(recordData.protocol, number);
    }

    /**
     * Process a ARC record payload, recursively if configured to do so.
     * @param jhove2 the JHove2 characterization context
     * @param sourceFactory JHove2 source factory
     * @param recordSrc ARC record source unit
     * @param payload_stream payload inputstream
     * @param formatId JHove2 format identification based on contentType
     * @throws EOFException if EOF occurs prematurely
     * @throws IOException if an IO error occurs while processing
     * @throws JHOVE2Exception if a serious problem needs to be reported
     */
    protected void characterizePayload(JHOVE2 jhove2, SourceFactory sourceFactory,
            Source recordSrc, InputStream payload_stream, FormatIdentification formatId)
                    throws EOFException, IOException, JHOVE2Exception {
        // Not all properties are ready yet, they are added as extras.
    	String name = null;
        Source payloadSrc = sourceFactory.getSource(jhove2, payload_stream, name, null);
        if (payloadSrc != null) {
        	payloadSrc.setDeleteTempFileOnClose(jhove2.getInvocation().getDeleteTempFilesOnClose());
            payloadSrc = recordSrc.addChildSource(payloadSrc);
            // Add presumptive format based on content-type.
            if(formatId != null){
                payloadSrc = payloadSrc.addPresumptiveFormat(formatId);
            }
            /* Make sure to close the Input after
             * characterization is completed.
             */
            Input src_input = payloadSrc.getInput(jhove2);
            try {
                payloadSrc = jhove2.characterize(payloadSrc, src_input);
            } finally {
                if (src_input != null) {
                    src_input.close();
                }
                payloadSrc.close();
            }
        }
    }

    /**
     * Checks ARC record validity and reports validation errors.
     * @param src ARC source unit
     * @param record the ARC record to characterize.
     * @param jhove2 the JHove2 characterization context.
     * @throws IOException if an IO error occurs while processing
     * @throws JHOVE2Exception if a serious problem needs to be reported
     */
    private void reportValidationErrors(Source src, ArcRecordBase record,
                        JHOVE2 jhove2) throws JHOVE2Exception, IOException {
        if (record.diagnostics.hasErrors()) {
            // Report errors on source object.
           for (Diagnosis d : record.diagnostics.getErrors()) {
               src.addMessage(newValidityError(jhove2, Message.Severity.ERROR,
                       d.type.toString().toLowerCase(), d.getMessageArgs()));
               //updateMap(e.error.toString() + '-' + e.field, this.errors);
           }
        }
        if (record.diagnostics.hasWarnings()) {
            // Report warnings on source object.
            for (Diagnosis d : record.diagnostics.getWarnings()) {
                src.addMessage(newValidityError(jhove2, Message.Severity.WARNING,
                        d.type.toString().toLowerCase(), d.getMessageArgs()));
            }
         }
    }

    /**
     * Instantiates a new localized message.
     * @param jhove2 the JHove2 characterization context.
     * @param severity message severity
     * @param id the configuration property relative name.
     * @param params the values to add in the message
     * @return the new localized message
     * @throws JHOVE2Exception if a serious problem needs to be reported
     */
    private Message newValidityError(JHOVE2 jhove2, Severity severity, String id,
                                     Object[] messageArgs) throws JHOVE2Exception {
        return new Message(severity, Message.Context.OBJECT,
                           this.getClass().getName() + '.' + id, messageArgs,
                           jhove2.getConfigInfo());
    }

    //------------------------------------------------------------------------
    // Validator interface support
    //------------------------------------------------------------------------

    /**
     * Validates the ARC file, which in this case amounts to returning the
     * result since validation has already been done.
     * @param  jhove2 the JHove2 characterization context.
     * @param  source ARC file source unit.
     * @param  input ARC file source input.
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
                                                        throws JHOVE2Exception {
        return this.isValid();
    }

    /**
     * Gets the validation coverage.
     * @return {@link Coverage.Selective selective}, always.
     */
    @Override
    public Coverage getCoverage() {
        return COVERAGE;
    }

    /**
     * Gets ARC file validation status.
     * @return the {@link Validity validity status}.
     */
    @Override
    public Validity isValid() {
        return this.isValid;
    }

    //------------------------------------------------------------------------
    // Reportable properties
    //------------------------------------------------------------------------

    /**
     * arcFile getter.
     * @return the arcFile
     */
    @ReportableProperty(order=1, value="ARC file name")
    public String getArcFileName() {
        return arcFileName;
    }

    /**
     * arcFileSize getter.
     * @return the arcFileSize
     */
    @ReportableProperty(order=2, value="ARC file size, in bytes")
    public long getArcFileSize() {
        return arcFileSize;
    }

    /**
     * Returns ARC file last modified date.
     * @return ARC file last modified date
     */
    @ReportableProperty(order=3, value="ARC file last modified date")
    public Date getLastModified() {
        return arcFileLastModified;
    }

    /**
     * Returns ARC record number.
     * @return the number of arc record
     */
    @ReportableProperty(order=4, value="The number of ARC records")
    public int getArcRecordNumber() {
        return arcRecordNumber;
    }

    /**
     * arcReaderConsumedBytes getter.
     * @return the arcReaderConsumedBytes
     */
    @ReportableProperty(order=5, value="ARC reader consumed bytes, in bytes")
    public long getArcReaderConsumedBytes() {
         return arcReaderConsumedBytes;
    }

    /**
     * Returns the file version if it is the same for all records.
     * @return the file version
     */
    @ReportableProperty(order=6, value="File version")
    public String getFileVersion() {
        return arcFileVersion;
    }

    /**
     * Returns the block description version if it is the same for all records.
     * @return the block description version
     */
    @ReportableProperty(order=7, value="Block description version")
    public Integer getArcBlockDescVersion() {
        return arcBlockDescVersion;
    }

    /**
     * protocols getter.
     * @return the protocols
     */
    @ReportableProperty(order=8, value="URL record protocols")
    public Map<String, Integer> getProtocols() {
        return protocols;
    }

     /**
     * errors getter.
     * @return the errors
     */
    /*
    @ReportableProperty(order=9, value="The number of errors by error type")
    public Map<String,AtomicInteger> getErrors() {
        return errors;
    }
    */

    //------------------------------------------------------------------------
    // Specific implementation
    //------------------------------------------------------------------------

    /**
     * Sets whether to recursively characterize ARC record objects.
     * @param recurse whether to recursively characterize ARC record objects
     */
    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    /**
     * Enable or disable block digest computation.
     * @param bComputeBlockDigest block digest computation toggle
     */
    public void setComputeBlockDigest(boolean bComputeBlockDigest) {
        this.bComputeBlockDigest = bComputeBlockDigest;
    }

    /**
     * Set the block digest algorithm to be used in case no digest is present
     * in the WARC header.
     * @param blockDigestAlgorithm block digest algorithm
     */
    public void setBlockDigestAlgorithm(String blockDigestAlgorithm) {
        this.blockDigestAlgorithm = blockDigestAlgorithm;
    }

    /**
     * Set the block digest encoding scheme to be used in case no digest
     * is present in the WARC header.
     * @param blockDigestEncoding block digest encoding scheme
     */
    public void setBlockDigestEncoding(String blockDigestEncoding) {
        this.blockDigestEncoding = blockDigestEncoding;
    }

    /**
     * Enable or disable payload digest computation.
     * @param bComputePayloadDigest payload digest computation toggle
     */
    public void setComputePayloadDigest(boolean bComputePayloadDigest) {
        this.bComputePayloadDigest = bComputePayloadDigest;
    }

    /**
     * Set the payload digest algorithm to be used in case no digest is present
     * in the WARC header.
     * @param payloadDigestAlgorithm payload digest algorithm
     */
    public void setPayloadDigestAlgorithm(String payloadDigestAlgorithm) {
        this.payloadDigestAlgorithm = payloadDigestAlgorithm;
    }

    /**
     * Set the payload digest encoding scheme to be used in case no digest
     * is present in the WARC header.
     * @param payloadDigestEncoding payload digest encoding scheme
     */
    public void setPayloadDigestEncoding(String payloadDigestEncoding) {
        this.payloadDigestEncoding = payloadDigestEncoding;
    }

}
