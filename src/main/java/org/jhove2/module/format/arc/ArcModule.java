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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.I8R;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.format.FormatIdentification.Confidence;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.module.Module;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.Validator.Coverage;
import org.jhove2.module.format.arc.properties.ArcRecordData;
import org.jhove2.module.format.gzip.GzipModule;
import org.jhove2.persist.FormatModuleAccessor;
import org.jwat.arc.ArcReader;
import org.jwat.arc.ArcReaderFactory;
import org.jwat.arc.ArcRecord;
import org.jwat.arc.ArcRecordBase;
import org.jwat.arc.ArcValidationError;
import org.jwat.arc.ArcVersionBlock;
import org.jwat.common.HttpResponse;
import org.jwat.common.Payload;

import com.sleepycat.persist.model.Persistent;


/**
 * JHOVE2 ARC module.
 *
 * @author lbihanic, selghissassi
 */
@Persistent
public class ArcModule extends BaseFormatModule implements Validator {

	/** Module version identifier. */
    public static final String VERSION = "0.8.0";

    /** Module release date. */
    public static final String RELEASE = "2010-12-01";

    /** Module rights statement. */
    public static final String RIGHTS =
        "Copyright 2010 by Bibliotheque nationale de France. " +
        "Available under the terms of the BSD license.";

	/** Module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Selective;

    private static final String CHARACTERIZATION_ERROR = "characterizationError";

    /**
     * Stores a mapping of all Format aliases in the
     * {@link org.jhove2.core.I8R.Namespace.MIME MIME namespace}
     * available via configuration to the JHOVE2
     * {@link org.jhove2.core.I8R} for that Format
     */
    private static transient Map<String, FormatIdentification> jhove2Ids = null; 

    /** Validation status. */
    private volatile Validity isValid = Validity.Undetermined;

    /** Used protocols. */
    //private ConcurrentMap<String, AtomicInteger> protocols =
    //                            new ConcurrentHashMap<String,AtomicInteger>();
    /** Validation errors. */
    //private ConcurrentMap<String, AtomicInteger> errors =
    //                            new ConcurrentHashMap<String,AtomicInteger>();

    /** The number or ARC records. */
    //private AtomicInteger arcRecordNumber = new AtomicInteger(0);
    private int arcRecordNumber;
    /** The name of the ARC file. */
    private String arcFileName;
    /** The size of the ARC file, in bytes. */
    private long arcFileSize;

    /** Whether to recursively characterize ARC record objects. */
    private boolean recurse = true;
    /** Thread pool size for parallel characterization of ARC records. */
    //private int nThreads = 0;

    /**
     * Instantiate a new <code>ArcModule</code> instance.
     * @param format ARC format.
     * @param formatModuleAccessor FormatModuleAccessor to manage access to Format Profiles
     */
    public ArcModule(Format format, 
    		FormatModuleAccessor formatModuleAccessor) {
        super(VERSION, RELEASE, RIGHTS, format, formatModuleAccessor);
		this.isValid = Validity.Undetermined;
    }

    /**
     * Instantiate a new <code>ArcModule</code> instance.
     * @throws JHOVE2Exception 
     */
    public ArcModule() {
      this(null, null);
    }

    //------------------------------------------------------------------------
    // BaseFormatModule contract support
    //------------------------------------------------------------------------

    /**
     * Parses an ARC record source unit.
     * @param  jhove2   the JHove2 characterization context.
     * @param  source   ARC source unit
     * @param  param    ARC source input
     * @return <code>0</code> always.
     * @throws JHOVE2Exception 
     * @throws IOException 
     */
    @Override
    public long parse(JHOVE2 jhove2,Source source, Input input) 
                                           throws JHOVE2Exception, IOException {
		/*
		 * Cache Content-Types to J2 FormatIdentifications.
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
    	 * Module init.
    	 */
	    long consumed = 0L;
        isValid = Validity.Undetermined;
	    input.setByteOrder(ByteOrder.LITTLE_ENDIAN);

	    // Reset state.
        arcFileName = null;
        arcFileSize = -1L;
        //this.arcRecordNumber.set(0);
        //this.errors.clear();
        //this.protocols.clear();

        // Assume the source is valid.
        isValid = Validity.True;
	    /*
	     * Module context.
	     */
		GzipModule gzipMod = null;
		ArcModule arcMod = null;
		Module mod;
		Source parentSrc = source.getSourceAccessor().retrieveSource(source.getParentSourceId());
		if (parentSrc != null) {
			List<Module> parentMods = parentSrc.getModules();
			for (int i=0; i<parentMods.size(); ++i) {
				mod = parentMods.get(i); 
				if (mod instanceof GzipModule) {
					gzipMod = (GzipModule)mod;
					gzipMod = GzipModule.gzipMap.get(gzipMod.instanceId);
				}
				if (mod instanceof ArcModule) {
					arcMod = (ArcModule)mod;
				}
			}
		}

    	Invocation cfg = jhove2.getInvocation();

    	ArcReader reader = null;
    	if (gzipMod != null) {
    		// This should probably be changed according to success reading VersionBlock.
    		gzipMod.presumptiveFormat = new FormatIdentification(format.getIdentifier(), Confidence.Tentative);
    		/*
	    	 * GZip compressed.
	    	 */
    		reader = (ArcReader)gzipMod.reader;
			if (reader == null) {
			    reader = ArcReaderFactory.getReaderUncompressed();
			    reader.setBlockDigestEnabled(false);
			    reader.setPayloadDigestEnabled(false);
			    gzipMod.reader = reader;
			}
			if (arcMod == null) {
				/*
				 * First record. (Unless the parent modules are not correct!)
				 */
				mod = parentSrc.addModule(this);
			    parseRecordsCompressed(jhove2, source, reader, true);
			}
			else {
				arcMod.parseRecordsCompressed(jhove2, source, reader, false);
				// May be useless as the caller may do this each time anyway.
				arcMod = (ArcModule)arcMod.getModuleAccessor().persistModule(arcMod);
				// Remove WarcModule from source instance since we added one to the parent source.
				List<Module> sourceMods = source.getModules();
				Iterator<Module> iter = sourceMods.iterator();
				while (iter.hasNext()) {
					mod = iter.next();
					if (mod instanceof ArcModule) {
						iter.remove();
					}
				}
				// Persist for some reason.
				source = source.getSourceAccessor().persistSource(source);
			}
		}
	    else {
	    	/*
	    	 * Not GZip compressed.
	    	 */
		    reader = ArcReaderFactory.getReaderUncompressed(source.getInputStream(), 8192);
		    reader.setBlockDigestEnabled(false);
		    reader.setPayloadDigestEnabled(false);
		    parseRecordsUncompressed(jhove2, source, reader, true);
	        reader.close();
	    }
	    /*
        ExecutorService threadPool = null;
        if (source instanceof ClumpSource) {
            if (source.hasChildSources() &&
                (source.getChildSources().get(0) instanceof GzipMemberSource)) {
                // Invoked from aggrefier module to validate clump source
                // recognized from characterizing a GZip ARC file.
                this.populateModule((ClumpSource)source);
            }
        }
        else {
	        InputStream in = null;
	        try {
	        	boolean isCompressedArc = 
	        		(source instanceof GzipMemberSource) ? true : false;
	        	//initialize arc record parser
	        	in = source.getInputStream();
	        	ArcParser arc = new ArcParser(in);
	        	//get the version block
	        	VersionBlock versionBlock = arc.getVersionBlock();
	          	processArcRecord(jhove2,source,versionBlock,isCompressedArc,null);
	        	Version version = versionBlock.version;
	        	this.arcFileName = versionBlock.path;
	        	String [] fieldDesc = versionBlock.desc.fieldDesc;
	        	if (isCompressedArc) {
	        		// Compressed ARC file => ARC records are in subsequent GZip
	        		// members.
	          		((GzipMemberSource)source).getParentModule().setWovenFormatParser(new GzipRecordParser(version, fieldDesc));
	        	}
	        	else {
	        	    if (this.nThreads > 1) {
	                    threadPool = Executors.newFixedThreadPool(this.nThreads);
	                }
	        		this.arcFileSize = input.getSize();
	        		// Regular ARC file => Read the whole file content
		        	ArcRecord arcRecord;
		        	while ((arcRecord = arc.getNextArcRecord(version,fieldDesc)) != null) {
		        		processArcRecord(jhove2, source,arcRecord,isCompressedArc,threadPool);
		        	}
	        	}
	        }
	        catch(Exception ex){
	        	ex.printStackTrace();
	        	handleException(ex,jhove2,source);
	        	//return -1L;
	        }
	        finally {
	        	if(in != null){
	        		try { in.close(); } catch (Exception e) { /* Ignore... *//* }
	        	}
	        	// Shutdown thread pool (if any).
	            if (threadPool != null) {
	                threadPool.shutdown();
	                // Wait for completion of all characterization tasks.
	                boolean shutdownComplete = false;
	                do {
	                    try {
	                        threadPool.awaitTermination(2L, TimeUnit.HOURS);
	                        shutdownComplete = true;
	                    }
	                    catch (InterruptedException e) { /* Ignore... *//* }
	                }
	                while (! shutdownComplete);
	            }
	        }
        }
        */
	    /*
	     * Consumed.
	     */
        // TODO Should probably be implemented
        //consumed = reader.getOffset();
        return consumed;
    }

	protected void parseRecordsUncompressed(JHOVE2 jhove2, Source source, ArcReader reader, boolean bReadVersion)
			throws EOFException, IOException, JHOVE2Exception {
		ArcVersionBlock versionBlock;
	    ArcRecord record;

        // Ensure a ARC reader could be instantiated.
        if (reader != null) {
	        source.setIsAggregate(true);
	        if (bReadVersion) {
		        versionBlock = reader.getVersionBlock();
		        if (versionBlock != null) {
			        processVersionBlock(jhove2, source, versionBlock);
		        }
	        }
	        /*
	         * Loop through available records.
	         */
	        while ((record = reader.getNextRecord()) != null) {
	        	processRecord(jhove2, source, record);
	        }

	        // TODO real validity.
	        this.isValid = Validity.True;
        }
        else {
        	// No WARC reader. Oh the horror!
        }
	}

	protected void parseRecordsCompressed(JHOVE2 jhove2, Source source, ArcReader reader, boolean bReadVersion)
			throws EOFException, IOException, JHOVE2Exception {
		ArcVersionBlock versionBlock;
	    ArcRecord record;

        // Ensure a ARC reader could be instantiated.
        if (reader != null) {
	        source.setIsAggregate(true);
	    	InputStream in = source.getInputStream();
	        if (bReadVersion) {
		        versionBlock = reader.getVersionBlock(in);
		        if (versionBlock != null) {
			        processVersionBlock(jhove2, source, versionBlock);
		        }
	        }
	        /*
	         * Loop through available records.
	         */
	        while ((record = reader.getNextRecordFrom(in, 0)) != null) {
	        	processRecord(jhove2, source, record);
	        }

	        // TODO real validity.
	        this.isValid = Validity.True;
        }
        else {
        	// No WARC reader. Oh the horror!
        }
	}

	protected void processVersionBlock(JHOVE2 jhove2, Source source, ArcVersionBlock versionBlock)
							throws EOFException, IOException, JHOVE2Exception {
        SourceFactory factory = jhove2.getSourceFactory();
		if (factory == null) {
			throw new JHOVE2Exception("JHOVE2 SourceFactory is null");
		}

        ArcRecordData recordData;

		/*
         * Arc Record Source.
         */
        Source versionBlockSrc = new ArcRecordSource();
        versionBlockSrc.setSourceAccessor(factory.createSourceAccessor(versionBlockSrc));
        versionBlockSrc = source.addChildSource(versionBlockSrc);
    	/*
    	 * Properties.
    	 */
        recordData = new ArcRecordData(versionBlock);
        versionBlockSrc.addExtraProperties(recordData.getArcRecordBaseProperties());
        versionBlockSrc.addExtraProperties(recordData.getArcVersionBlockProperties());
	}

	protected void processRecord(JHOVE2 jhove2, Source source, ArcRecord record)
							throws EOFException, IOException, JHOVE2Exception {
        SourceFactory factory = jhove2.getSourceFactory();
		if (factory == null) {
			throw new JHOVE2Exception("JHOVE2 SourceFactory is null");
		}

	    Payload payload;
	    HttpResponse httpResponse;
        InputStream payload_stream;
        ArcRecordData recordData;
        String contentType;
        FormatIdentification formatId;

        contentType = record.recContentType;
        /*
         * Arc Record Source.
         */
        Source recordSrc = new ArcRecordSource();
        recordSrc.setSourceAccessor(factory.createSourceAccessor(recordSrc));
    	recordSrc = source.addChildSource(recordSrc);
        ++arcRecordNumber;
        /*
         * Prepare payload.
         */
        payload = record.getPayload();
        httpResponse = null;
        payload_stream = null;
        if (payload != null) {
        	httpResponse = payload.getHttpResponse();
        	if (httpResponse == null) {
        		payload_stream = payload.getInputStream();
        	}
        	else {
        		contentType = httpResponse.getProtocolContentType();
        		payload_stream = httpResponse.getPayloadInputStream();
        	}
        }
        /*
         * Presumptive format.
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
        if (payload_stream != null) {
            Source payloadSrc = factory.getSource(jhove2, payload_stream, name, null);		// properties
            if (payloadSrc != null) {
            	payloadSrc = recordSrc.addChildSource(payloadSrc);
    			if(formatId != null){
    				payloadSrc = payloadSrc.addPresumptiveFormat(formatId);
    			}
            	/* Make sure to close the Input after
            	 * characterization is completed.
            	 */
            	Input src_input = payloadSrc.getInput(jhove2);
            	try {
            		payloadSrc = jhove2.characterize(payloadSrc, src_input);
            	}
            	finally {
            		if (src_input != null) {
            			src_input.close();
            		}
            	}
            }
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
    	recordSrc.addExtraProperties(recordData.getArcRecordProperties());
        // TODO Check for errors and attach.
	}

	/**
     * Characterizes and checks the validity of the ARC record.
     * @param jhove2 the JHove2 characterization context.
     * @param source ARC file source unit.
     * @param source ARC record source unit. 
     * @param arcRecordBase the ARC record to characterize and validate.
     * @param isCompressedArc specifies whether the processed ARC file 
     * is a compressed ARC file
     * @throws JHOVE2Exception
     * @throws IOException
     */
    private void execute(JHOVE2 jhove2, Source source,Source child,
                         ArcRecordBase arcRecordBase,
                         boolean isCompressedArc) throws JHOVE2Exception, IOException{
    	this.characterizeRecord(jhove2, source,child, arcRecordBase);
    	checkArcRecordValidity(child, arcRecordBase, jhove2, isCompressedArc);
    	if(arcRecordBase instanceof ArcRecord){
    		//arcRecordNumber.incrementAndGet();
    		//updates protocols
    		//updateMap(arcRecordBase.getProtocol(), protocols);
    	}
    }
    
    //------------------------------------------------------------------------
    // Validator interface support
    //------------------------------------------------------------------------
	
    /**
     * Validates the ARC file.
     * @param  jhove2   the JHove2 characterization context.
     * @param  source   ARC file source unit.
     * @param  input    ARC file source input.
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
	 * Returns ARC record number
	 * @return the number of arc record
	 */
    @ReportableProperty(order=1, value="The number of ARC records")
	public int getArcRecordNumber() {
		//return this.arcRecordNumber.get();
    	return arcRecordNumber;
    }

	/**
	 * arcFile getter
	 * @return the arcFile
	 */
    @ReportableProperty(order=2, value="ARC file name")
	public String getArcFileName() {
		return arcFileName;
	}

    /**
	 * arcFileSize getter
	 * @return the arcFileSize
	 */
    @ReportableProperty(order=3, value="ARC file size, in bytes")
	public long getArcFileSize() {
		return arcFileSize;
	}

    /**
	 * protocols getter
	 * @return the protocols
	 */
    /*
    @ReportableProperty(order=4, value="URL record protocols")
	public Map<String,AtomicInteger> getProtocols() {
		return protocols;
	}
	*/

 	/**
	 * errors getter
	 * @return the errors
	 */
    /*
    @ReportableProperty(order=5, value="The number of errors by error type")
	public Map<String,AtomicInteger> getErrors() {
		return errors;
	}
	*/

    //------------------------------------------------------------------------
    // Specific implementation
    //------------------------------------------------------------------------

    /**
     * Characterizes ARC record network doc.
     * @param jhove2 the JHove2 characterization context.
     * @param parent ARC source unit.
     * @param arcRecordBase the arc record to process.
     * @return ARC record source unit
     * @throws JHOVE2Exception
     * @throws IOException
     */
    private Source characterizeRecord(JHOVE2 jhove2, Source parent,Source child,
    		                         ArcRecordBase arcRecordBase) 
                                     throws JHOVE2Exception, IOException{
    	// TODO convert
    	/*
        if (this.recurse) {
        	Input in = child.getInput(jhove2);
        	if(in != null){
        		try{
        			//get the presumptive format using network doc content type.
        			// jhove2Ids.get(mimeType)
        			FormatIdentification format = 
        				getFormatIdentification(arcRecordBase.getFormat(),jhove2);
        			if(format != null){
        				child.addPresumptiveFormat(format);
        			}
        			// Characterize the ARC record.
        			jhove2.characterize(child, in);
        		}catch(Exception e){
        			//add characterization error to the source
        			child.addMessage(
        	        		this.newValidityError(jhove2,Message.Severity.ERROR,
        	        				              CHARACTERIZATION_ERROR,
        				                          e.toString()));
        			this.updateMap(CHARACTERIZATION_ERROR, this.errors);
					//read network doc remaining bytes
					//arcRecordBase.readRemainingBytes();	
				}
				finally{
        			try {
        				in.close();
        			}catch(Exception e){
        				/ * Ignore... * / 
        			}
        		}
        	}
        }
        */
        return child;
    }

    /**
     * Checks ARC record validity and reports validation errors.
     * @param src ARC source unit
     * @param arcRecord the ARC record to characterize.
     * @param jhove2 the JHove2 characterization context.
     * @throws JHOVE2Exception
     * @throws IOException 
     */
    private void checkArcRecordValidity(Source src, ArcRecordBase arcRecord,
    		                            JHOVE2 jhove2,boolean validateContent) 
                                            throws JHOVE2Exception, IOException{
    	// TODO convert
    	/*
    	if((this.recurse) && (validateContent)){
    		arcRecord.validateNetworkDocContent();
    	}
    	if (!arcRecord.isValid()) {
           this.isValid = Validity.False;
           // Report errors on source object.
           this.reportValidationErrors(src,jhove2,
        		                      arcRecord.getValidationErrors());
        }
    	if (arcRecord.hasWarnings()) {
            // Report warnings on source object.
            this.reportValidationWarnings(src,jhove2,
         		                      arcRecord.getWarnings());
         }
         */
    }
  
    /**
     * Handles generic exceptions
     * @param e the exception
     * @param jhove2 the JHove2 characterization context.
     * @param source ARC file source unit.
     * @throws JHOVE2Exception
     */
    private void handleException(Exception e, JHOVE2 jhove2,Source source) throws JHOVE2Exception{
    	this.isValid = Validity.False;
    	// TODO convert
    	/*
    	source.addMessage(this.newValidityError(jhove2,Message.Severity.ERROR,
    				                            ArcErrorType.INVALID.toString(),
    				                            ArcRecordBase.ARC_FILE,e.toString()));
        */
    }

    /**
     * Sets whether to recursively characterize ARC record objects.
     * @param  recurse  whether to recursively characterize ARC record objects.
     */
    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }
 
    /** Reports validation errors. */
    private void reportValidationErrors(Source src,JHOVE2 jhove2,
    		                            Collection<ArcValidationError> errors) 
                                                        throws JHOVE2Exception {
    	for(ArcValidationError e : errors){
    		src.addMessage(newValidityError(jhove2,Message.Severity.ERROR,
    				                        e.error.toString(),e.field,e.value));
    		//updateMap(e.error.toString() + '-' + e.field, this.errors);   		
    	}
    }

    /** Reports warnings. */
    private void reportValidationWarnings(Source src,JHOVE2 jhove2,
    		                            Collection<String> warnings) 
                                                        throws JHOVE2Exception {
    	for(String warning : warnings){
    		src.addMessage(newValidityError(jhove2,Message.Severity.WARNING,
    				                        "warning",warning));	
    	}
    }

    /**
     * Increments the corresponding value of the key in the specified map.
     * @param key the key in the map.
     * @param map the map to update.
     */
    private void updateMap(String key, ConcurrentMap<String,AtomicInteger> map){
    	if ((key != null) && (key.length() != 0)) {
    	    if (! map.containsKey(key)) {
    	        map.putIfAbsent(key, new AtomicInteger());
    	    }
            map.get(key).incrementAndGet();
    	}
    }

    /**
     * Instantiates a new localized message.
     * @param jhove2 the JHove2 characterization context.
     * @param id the configuration property relative name.
     * @param params the values to add in the message 
     * @return the new localized message
     * @throws JHOVE2Exception
     */
    private Message newValidityError(JHOVE2 jhove2,Severity severity,String id, 
    		                         Object... params)throws JHOVE2Exception {
    return new Message(severity,
                       Message.Context.OBJECT,
                       this.getClass().getName() + '.' + id,params,
                       jhove2.getConfigInfo());
    }

	/**
	 * ARC record counter getter.
	 * @return the ARC record counter
	 */
	//public AtomicInteger getArcRecordCounter() {
	public int getArcRecordCounter() {
		//return this.arcRecordNumber;
		return 0;
    }

	/*
	public void setParallelCharacterization(int level) {
	        if (level < 0) {
	            level = 0;
	        }
	        this.nThreads = level;
	}
	*/
	/**
	 * nThreads getter
	 * @return nThreads
	 */
	/*
	public int getParallelCharacterization(){
		return this.nThreads;
	}
	*/
}
