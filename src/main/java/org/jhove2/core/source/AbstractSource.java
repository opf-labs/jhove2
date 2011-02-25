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

package org.jhove2.core.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.core.reportable.Reportable;
import org.jhove2.module.Module;
import org.jhove2.persist.SourceAccessor;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;
import static com.sleepycat.persist.model.Relationship.*;
import static com.sleepycat.persist.model.DeleteAction.*;

/**
 * An abstract JHOVE2 source unit. A source unit is a formatted object that can
 * be characterized, which may be a file, a subset of a file, or a group of
 * files.
 * 
 * @author mstrong, slabrams, smorrissey
 */
@Entity
public abstract class AbstractSource
	extends AbstractReportable
	implements Source, Comparable<Source>
{
	/** key for persistence managers that require key */
	@PrimaryKey(sequence="SOURCE_ID")
	protected Long sourceId;
	
	/** secondary index relates child source to parent source*/
	@SecondaryKey(relate=MANY_TO_ONE, relatedEntity=AbstractSource.class,
			onRelatedEntityDelete=NULLIFY)
	protected Long parentSourceId;
	
	/** manages persistence of source */
	protected SourceAccessor sourceAccessor;
	
	/** Identifiers of generic modules registered with the Source. */
	protected static Set<String> moduleIDs = new HashSet<String>();

	/** Temporary file deletion flag; if true, delete on close. */
	protected boolean deleteTempFileOnClose;
	
	/** Extra properties.  Extra properties are those not known at the time a
	 * source unit is instantiated and are not associated with a particular
	 * {@link org.jhove2.module.format.FormatModule}. */
	protected List<Reportable> extraProperties;
    
	/** Source unit backing file. This may be an actual file system
	 * file or a temporary file created from an {@link java.io.InputStream}.
	 */
	//protected File file;
	
	/** File system properties, if the source is a physical file or directory
	 * in the file system.
	 */
	protected FileSystemProperties fileSystemProperties;

	/** Source unit aggregate flag: true if an aggregate. */
	protected boolean isAggregate;
	
	/**
	 * Temporary file flag: true if the file backing the source unit is
	 * a temporary file.
	 */
	protected boolean isTemp;
	
	/** Source unit messages. */
	protected List<Message> messages;

	/** Presumptive identifications for the source unit. */
	protected Set<FormatIdentification> presumptiveFormatIdentifications;
 
    /**
     * Timer info used to track elapsed time for running of this module
     */
    protected TimerInfo timerInfo;
    
    /**
     * Mechanism for providing per-source information
     */
    protected Map<String, String> sourceParams;
    
	/**
	 * Instantiate a new <code>AbstractSource</code>.
	 */
	protected AbstractSource() {       
        this.deleteTempFileOnClose   = Invocation.DEFAULT_DELETE_TEMP_FILES_ON_CLOSE;
        this.extraProperties = new ArrayList<Reportable>();
		this.isAggregate     = false;
        this.isTemp          = false;
		this.messages        = new ArrayList<Message>();		
		this.presumptiveFormatIdentifications = new TreeSet<FormatIdentification>();
        this.timerInfo       = new TimerInfo();
	}

	/**
	 * Instantiate a new <code>AbstractSource</code>. 
	 * @param jhove2 JHOVE2 framework object
	 */
	public AbstractSource(JHOVE2 jhove2)
	{
		this();
		
		/* Set the temporary file deletion flag. */
		Invocation inv = jhove2.getInvocation();
		this.deleteTempFileOnClose = inv.getDeleteTempFilesOnClose();
		
		/* Create the source accessor. */
		this.setSourceAccessor(jhove2.getSourceFactory().createSourceAccessor(this));
	}

	/**
	 * Add a child source unit.
	 * 
	 * @param child
	 *            Child source unit
	 * @return child Source
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.core.source.Source#addChildSource(org.jhove2.core.source.Source)
	 */
	@Override
	public Source addChildSource(Source child) throws JHOVE2Exception {
		if (this.getSourceAccessor() == null){
			throw new JHOVE2Exception("SourceAccessor is null");
		}
		return this.sourceAccessor.addChildSource(this, child);
	}

	   
    /** Add an extra properties {@link org.jhove2.core.reportable.Reportable}
     * to be associated with the source unit.  Extra properties are those not
     * known at the time the source unit is instantiated and which are not
     * associated with a particular {@link org.jhove2.module.format.FormatModule}.
     * @param properties Extra properties reportable
     * @return Source with extra properties added
     * @throws JHOVE2Exception
     */
	@Override
    public Source addExtraProperties(Reportable properties)
        throws JHOVE2Exception
    {
	    return this.getSourceAccessor().addExtraProperties(this, properties);
    }
    
	/** Add a message to be associated with the source unit.
	 * @param message Message to be associated with the source unit
	 * @return Source with Message added
	 * @throws JHOVE2Exception 
	 */
	@Override
	public Source addMessage(Message message) throws JHOVE2Exception {
		return this.getSourceAccessor().addMessage(this, message);
	}

	/**
	 * Add a module that processed the source unit.
	 * Generic modules are only add once, to the first Source upon which they are invoked
	 * Specific modules are added to each Source upon which they are invoked
	 * @param module
	 *            Module that processed the source unit
	 * @return Module that has been added to Source
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.core.source.Source#addModule(org.jhove2.module.Module)
	 */
	@Override
	public Module addModule(Module module) throws JHOVE2Exception {
		if (this.getSourceAccessor() == null){
			throw new JHOVE2Exception("SourceAccessor is null");
		}
		if (module.getScope()== Module.Scope.Specific){
			module = this.sourceAccessor.addModule(this, module);
		}
		else {
			String id = module.getReportableIdentifier().toString();
			if (!moduleIDs.contains(id)) {
					moduleIDs.add(id);
					module = this.sourceAccessor.addModule(this, module);
			}
        }
		return module;
	}
	
	/** Add a presumptively-identified format for this source unit.
	 * @param fi Presumptively-identified format
	 * @return Source with format added
	 * @throws JHOVE2Exception 
	 */
	@Override
	public Source addPresumptiveFormat(FormatIdentification fi)
	    throws JHOVE2Exception
	{
		return this.getSourceAccessor().addPresumptiveFormat(this, fi);
	}
	
	/** Add set of presumptively-identified formats for this source unit.
	 * @param fis Presumptively-identified formats
	 * @return Source with format added
	 * @throws JHOVE2Exception 
	 */
	@Override
	public Source addPresumptiveFormats(Set<FormatIdentification> fis)
	    throws JHOVE2Exception
	{
		return this.getSourceAccessor().addPresumptiveFormats(this, fis);
	}

	/**
	 * Close the source unit.
	 */
	@Override
	public void close() {
	}

	/**
	 * Create a temporary backing file from an input stream.
     * @param inStream Input stream
     * @param name Temporary file name
	 * @param tmpDirectory Temporary directory
	 * @param tmpPrefix Temporary file prefix
	 * @param tmpSuffix Temporary file suffix
     * @param  bufferSize Buffer size used during transfer to temporary file 
	 * @return file Temporary backing file
	 * @throws IOException
	 */
	protected static synchronized File createTempFile(InputStream inStream,
	                                                  String name,
	                                                  File tmpDirectory,
	                                                  String tmpPrefix,
	                                                  String tmpSuffix,
	                                                  int bufferSize)
		throws IOException
	{
	    File tempFile = null;
	    if (name == null || name.length() == 0) {
	        tempFile = File.createTempFile(tmpPrefix, tmpSuffix, tmpDirectory);;
	    }
	    else {
	        tempFile = new File(tmpDirectory.getPath() + File.separator + name);
	    }
		OutputStream outStream = new FileOutputStream(tempFile);
		ReadableByteChannel in = Channels.newChannel(inStream);
		WritableByteChannel out = Channels.newChannel(outStream);
		final ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
		try {
		    while ((in.read(buffer)) > 0) {
		        buffer.flip();
		        out.write(buffer);
		        buffer.compact(); /* in case write was incomplete. */
		    }
		    buffer.flip();
		    while (buffer.hasRemaining()) {
		        out.write(buffer);
		    }
		}
		finally {
		    /* Close I/O resources. */
		    in.close();
		    out.close();
		    outStream.close();
		}

		return tempFile;
	}

    /**
     * Create a temporary backing file that is a subset of an input stream.
     * @param inFile Input file
     * @param offset Starting offset of the subset
     * @param size   Size of the subset
     * @param tmpDirectory Temporary directory
     * @param tmpPrefix Temporary file prefix
     * @param tmpSuffix Temporary file suffix
     * @param  bufferSize Buffer size used during transfer to temporary file 
     * @return file Temporary backing file
     * @throws IOException
     */
    protected static synchronized File createTempFile(File inFile, long offset,
                                                      long size, File tmpDirectory,
                                                      String tmpPrefix,
                                                      String tmpSuffix,
                                                      int bufferSize)
        throws IOException
    {
        /* Position input stream to starting offset. */
        InputStream inStream = new FileInputStream(inFile);
        inStream.skip(offset);
        ReadableByteChannel in = Channels.newChannel(inStream);
        
        /* Create temporary file. */
        File tempFile = File.createTempFile(tmpPrefix, tmpSuffix, tmpDirectory);
        OutputStream outStream = new FileOutputStream(tempFile);
        WritableByteChannel out = Channels.newChannel(outStream);
        
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
        try {
            long consumed = 0;
            int n = 0;
            while (consumed < size) {
                long remaining = size - consumed;
                if (remaining < bufferSize) {
                    buffer = ByteBuffer.allocateDirect((int) remaining);
                }
                if ((n = in.read(buffer)) > 0) {
                    buffer.flip();
                    out.write(buffer);
                    buffer.compact(); /* in case write was incomplete. */
                    consumed += n;
                }
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                out.write(buffer);
            }
        }
        finally {
            /* Close I/O resources). */
            in.close();
            out.close();
            inStream.close();
            outStream.close();
        }

        return tempFile;
    }

	/**
	 * Delete child source unit.
	 * 
	 * @param child
	 *            Child source unit
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.core.source.Source#deleteChildSource(Source)
	 */
	@Override
	public Source deleteChildSource(Source child) throws JHOVE2Exception {
		if (this.getSourceAccessor() == null){
			throw new JHOVE2Exception("SourceAccessor is null");
		}
		return this.sourceAccessor.deleteChildSource(this, child);
	}

	/**
	 * Get delete temporary files flag; if true, delete files.
	 * 
	 * @return Delete temporary files flag
	 * @see org.jhove2.core.source.Source#getDeleteTempFileOnClose()
	 */
	@Override
	public boolean getDeleteTempFileOnClose() {
		return this.deleteTempFileOnClose;
	}

	/**
	 * Get child source units.
	 * 
	 * @return Child source units
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.core.source.Source#getChildSources()
	 */
	@Override
	public List<Source> getChildSources() throws JHOVE2Exception {
		if (this.getSourceAccessor()==null){
			throw new JHOVE2Exception("SourceAccessor is null");
		}
		return this.sourceAccessor.getChildSources(this);
	}
    
    /** Get extra properties.  Extra properties are those not known at the
     * time the source unit is instantiated but which are not associated with
     * a particular {@link org.jhove2.module.format.FormatModule}.
     * @return Extra properties
     * @throws JHOVE2Exception
     */
    @Override
    public List<Reportable> getExtraProperties()
        throws JHOVE2Exception
    {
        return this.extraProperties;
    }

    /**
     * Get {@link java.io.File} backing the source unit.
     * Sources with an underlying tangible backing file
     * (e.g. {@link org.jhove2.core.source.FileSource},
     * {@link org.jhove2.core.source.ByteStreamSource}, and
     * {@link org.jhove2.core.source.URLSource} should override this method
     * return the appropriate input. 
     * 
     * @return File backing the source unit
     * @see org.jhove2.core.source.Source#getFile()
     */
    @Override
    public File getFile() {
        return null;
    }
    
    /** Get {@link org.jhove2.core.source.FileSystemProperties}.
     * @return File system properties
     */
    @Override
    public FileSystemProperties getFileSystemProperties() {
        return this.fileSystemProperties;
    }


    /**
     * Get little-endian {@link org.jhove2.core.io.Input} for the source unit
     * with the buffer size and type specified by the 
     * {@link org.jhove2.core.Invocation}.
     * If this method is called explicitly, then the corresponding Input.close()
     * method must be called to avoid a resource leak.
     * @param jhove2 JHOVE2 framework
     * @return Input for the source unit
     * @throws IOException 
     * @throws FileNotFoundException 
     */
	@Override
    public Input getInput(JHOVE2 jhove2)
	    throws IOException
	{
        return this.getInput(jhove2, ByteOrder.BIG_ENDIAN);
    }

	/**
	 * Create and get {@link org.jhove2.core.io.Input} for the source unit.
	 * Sources with tangible input (e.g. {@link org.jhove2.core.source.FileSource},
	 * {@link org.jhove2.core.source.ByteStreamSource}, and
	 * {@link org.jhove2.core.source.URLSource} should override this method
	 * return the appropriate input.
     * If this method is called explicitly, then the corresponding Input.close()
     * method must be called to avoid a resource leak.
	 * @param jhove2 JHOVE2 framework object
	 * @param order
	 *            Byte order
	 * @return null
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception getting input
	 */
	@Override
	public Input getInput(JHOVE2 jhove2, ByteOrder order)
        throws IOException
	{
		return null;
	}

	/**
	 * Get {@link java.io.InputStream} backing the source unit.  Sources with
     * tangible input (e.g. {@link org.jhove2.core.source.FileSource},
     * {@link org.jhove2.core.source.ByteStreamSource}, and
     * {@link org.jhove2.core.source.DirectorySource} should override this method
     * return the appropriate stream.
     * If this method is called explicitly, then the corresponding
     * InputStream.close() method must be called to avoid a resource leak. 
	 * 
	 * @return Input stream backing the source unit, or null if a Clump,
	 *         Directory, or FileSet source
	 * @throws FileNotFoundException  Backing file not found
	 * @throws IOException Backing file could not be created
	 * @see org.jhove2.core.source.Source#getInputStream()
	 */
	@Override
	public InputStream getInputStream()
	    throws IOException
	{
	    return null;
	}

	/** Get source unit messages.
	 * @return Source unit messages
	 */
	@Override
	public List<Message> getMessages() {
	    return this.messages;
	}

    /**
     * @return the moduleIDs
     */
    public static Set<String> getModuleIDs() {
        return moduleIDs;
    }
    
	/**
	 * Get modules that processed the source unit.
	 * 
	 * @return Modules that processed the source unit
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.core.source.Source#getModules()
	 */
	@Override
	public List<Module> getModules() throws JHOVE2Exception {
		if (this.getSourceAccessor()==null){
			throw new JHOVE2Exception("SourceAccessor is null");
		}
		return this.sourceAccessor.getModules(this);
	}

	/**
	 * Get number of child source units.
	 * 
	 * @return Number of child source units
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.core.source.Source#getNumChildSources()
	 */
	@Override
	public int getNumChildSources() throws JHOVE2Exception {
		if (this.getSourceAccessor()==null){
			throw new JHOVE2Exception("SourceAccessor is null");
		}
		return this.sourceAccessor.getNumChildSources(this);
	}

	/**
	 * Get number of modules.
	 * 
	 * @return Number of modules
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.core.source.Source#getNumModules()
	 */
	@Override
	public int getNumModules() throws JHOVE2Exception {
		if (this.getSourceAccessor()==null){
			throw new JHOVE2Exception("SourceAccessor is null");
		}
		return this.sourceAccessor.getNumModules(this);
	}
	
	/**
	 * Get set of presumptively-identified formats.
	 * @return Presumptively-identified formats
	 */
	@Override
	public Set<FormatIdentification> getPresumptiveFormats() {
		return presumptiveFormatIdentifications;
	}

    @Override
    public SourceAccessor getSourceAccessor() {
        return sourceAccessor;
    }

    /**
     * Get Map of per-source parameters
     * @return Map of per-source parameter name/parameter value pairs
     */
    @Override
    public Map<String, String> getSourceParams() {
        return sourceParams;
    }

    @Override
    public Long getSourceId() {
        return sourceId;
    }

    @Override
    public Long getParentSourceId() {
        return parentSourceId;
    }
         
	/**
	 * Get elapsed time processing the source unit.
	 * @return Elapsed time
	 */
	@Override
	public TimerInfo getTimerInfo() {
		return timerInfo;
	}
	
	/** Get aggregate flag: true if source is an aggregate.
	 * @return Aggregate flag
	 */
	@Override
	public boolean isAggregate() {
	    return this.isAggregate;
	}
	
	/**
	 * Get source unit backing file temporary status.
	 * 
	 * @return True if the source unit backing file is a temporary file
	 * @see org.jhove2.core.source.Source#isTemp()
	 */
	@Override
	public boolean isTemp() {
		return this.isTemp;
	}
	
	/**
	 * Set delete temporary files flag; if true, delete files.
	 * 
	 * @param flag
	 *            Delete temporary files flag
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.core.source.Source#setDeleteTempFileOnClose(boolean)
	 */
	@Override
	public Source setDeleteTempFileOnClose(boolean flag)
	    throws JHOVE2Exception
	{
		this.deleteTempFileOnClose = flag;
		return this.getSourceAccessor().persistSource(this);
	}
    
    /**
     * Set aggregate flag; if true, source is an aggregate.
     * 
     * @param flag
     *            Aggregate flag
     * @throws JHOVE2Exception 
     * @see org.jhove2.core.source.Source#setIsAggregate(boolean)
     */
    @Override
    public Source setIsAggregate(boolean flag)
        throws JHOVE2Exception
    {
        this.isAggregate = flag;
        return this.getSourceAccessor().persistSource(this);
    }
    
    /** Set temporary file flag.
     * @param flag Temporary file flag; true if the backing file is temporary
     */
    @Override
    public Source setIsTemp(boolean flag)
        throws JHOVE2Exception
    {
        this.isTemp = flag;
        return this.getSourceAccessor().persistSource(this);
    }

    @Override
    public void setParentSourceId(Long parentSourceId) {
        this.parentSourceId = parentSourceId;
    }

    @Override
    public void setSourceAccessor(SourceAccessor sourceAccessor) {
        this.sourceAccessor = sourceAccessor;
    }

    @Override
    public Source startTimer() throws JHOVE2Exception{
        return this.sourceAccessor.startTimerInfo(this);
    }
    
    @Override
    public Source endTimer() throws JHOVE2Exception{
        return this.sourceAccessor.endTimerInfo(this);
    }
    
	/**
	 * Set Map of per-source parameters
	 * @param sourceParams Map of per-source parameter name/parameter value pairs
	 */
	@Override
	public Source setSourceParams(Map<String, String> sourceParams) throws JHOVE2Exception {
		this.sourceParams = sourceParams;
		return this.getSourceAccessor().persistSource(this);
	}

    @Override
    public void setTimerInfo(TimerInfo timer){
        this.timerInfo = timer;
    }
    
	/**
	 * Lexically compare format identifications.
	 * 
	 * @param src source to be compared
	 * @return -1, 0, or 1 if this Source value is less than, equal to, or
	 *         greater than the second
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(Source src){
		if (src==null){
			return 1;
		}
		if (this == src){
			return 0;
		}
		//same class?
		AbstractSource absObj = (AbstractSource) src;
		int idCompare = 
			this.getReportableIdentifier().compareTo(absObj.getReportableIdentifier());
		if (idCompare != 0){
			return idCompare;
		}
		File thisFile = this.getFile();
		File objFile = absObj.getFile();
		if (thisFile==null){
			if (objFile != null){
				return -1;
			}
		}
		else if (objFile == null){
			return 1;
		}
		else {
			int fileCompare = thisFile.compareTo(objFile);
			if (fileCompare != 0){
				return fileCompare;
			}
		}
		int thisChildSize;
		int srcChildSize;

		try {
			thisChildSize = this.getNumChildSources();
			srcChildSize = absObj.getNumChildSources();
			if (thisChildSize < srcChildSize){
				return -1;
			}
			else if (thisChildSize > srcChildSize){
				return 1;
			}
		}
		catch (JHOVE2Exception e){
			return -1;
		}

		int containsCount = 0;

		List<Source> absObjChildSources = null;
		try {
			absObjChildSources = absObj.getChildSources();
			for (Source childsrc : this.getChildSources()){
				AbstractSource childSource = (AbstractSource)childsrc;
				if (absObjChildSources.contains(childSource)){
					containsCount++;
				}
			}
		}
		catch (JHOVE2Exception e){
			return -1;
		}
		if (thisChildSize < containsCount){
			return -1;
		}
		else if (thisChildSize > containsCount){
			return 1;
		}

		try {
			thisChildSize = this.getModules().size();
			srcChildSize = absObj.getModules().size();
		} catch (JHOVE2Exception e) {
			return -1;
		}
		if (thisChildSize < srcChildSize){
			return -1;
		}
		else if (thisChildSize > srcChildSize){
			return 1;
		}
		containsCount = 0;
		List<Module> absObjModules;
		try {
			absObjModules = absObj.getModules();

			for (Module module : this.getModules()){
				if (absObjModules.contains(module)){
					containsCount++;
				}
			}	
			if (thisChildSize < containsCount){
				return -1;
			}
			else if (thisChildSize > containsCount){
				return 1;
			}	
		} catch (JHOVE2Exception e) {
			return -1;
		}
		return 0;
	}

	/**
	 * Test for equality.
	 * @param obj Object to test equality against
	 * @return True if equal; otherwise false
	 */
	@Override
	public boolean equals (Object obj){
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (! (obj instanceof AbstractSource)){
			return false;
		}
		//same class?
		AbstractSource absObj = (AbstractSource) obj;
		if(!(this.getReportableIdentifier().equals(absObj.getReportableIdentifier()))){
			return false;
		}
		File thisFile = this.getFile();
		File objFile = absObj.getFile();
		if (thisFile==null){
			if (objFile != null){
				return false;
			}
		}
		else if (objFile==null){
			return false;
		}
		else if (!(this.getFile().equals(absObj.getFile()))){
			return false;
		}
		int thisChildSize;
		int srcChildSize;	
		try {
			thisChildSize = this.getNumChildSources();

			srcChildSize = absObj.getNumChildSources();	
			if (thisChildSize != srcChildSize){
				return false;
			}
		} catch (JHOVE2Exception e) {
			return false;
		}

		int containsCount = 0;
		List<Source> absObjChildSources;
		try {
			absObjChildSources = absObj.getChildSources();
			if (thisChildSize > 0){	
				for (Source src : this.getChildSources()){
					AbstractSource childSource = (AbstractSource)src;
					if (absObjChildSources.contains(childSource)){
						containsCount++;
					}
				}			
			}
		} catch (JHOVE2Exception e) {
			return false;
		}
		if (thisChildSize != containsCount){
			return false;
		}		
		try {
			thisChildSize = this.getModules().size();

			srcChildSize = absObj.getModules().size();
			if (thisChildSize != srcChildSize){
				return false;
			}
		} catch (JHOVE2Exception e) {
			return false;
		}
		containsCount = 0;

		List<Module> absObjModules;
		try {
			absObjModules = absObj.getModules();

			for (Module module : this.getModules()){
				if (absObjModules.contains(module)){
					containsCount++;
				}
			}
		} catch (JHOVE2Exception e) {
			return false;
		}
		return (thisChildSize == containsCount);
	}

    /** Generate unique hash code for the source.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        List<Source> children = null;
        try {
        	children = this.getChildSources();
        }
        catch (Exception e){}
        List<Module> modules = null;
        try {
        	modules = this.getModules();
        }
        catch (Exception e){}        
        result = prime * result
                + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((modules == null) ? 0 : modules.hashCode());
        return result;
    }
}
