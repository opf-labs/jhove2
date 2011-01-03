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
import org.jhove2.core.io.InputFactory;
import org.jhove2.core.io.Input.Type;
import org.jhove2.core.reportable.AbstractReportable;
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

	/** Delete temporary files flag; if true, delete files. */
	protected boolean deleteTempFiles;

	/** Source unit backing file. */
	protected File file;

	/**
	 * Source unit backing file temporary status: true if the source unit
	 * backing file is a temporary file.
	 */
	protected boolean isTemp;
	
	/** Source unit messages. */
	protected List<Message> messages;

	/** Presumptive identifications for the source unit. */
	protected Set<FormatIdentification> presumptiveFormatIdentifications;
    
	/** Starting offset, in bytes. */
	protected long startingOffset;
	
    /**
     * Timer info  used to track elapsed time for running of this module
     */
    protected TimerInfo timerInfo;
    
    /**
     * Mechanism for providing per-source information
     */
    protected Map<String, String>sourceParams;
    
	/**
	 * Instantiate a new <code>AbstractSource</code>.
	 */
	protected AbstractSource() {		
		this.deleteTempFiles = Invocation.DEFAULT_DELETE_TEMP_FILES;
		this.messages        = new ArrayList<Message>();		
		this.presumptiveFormatIdentifications = new TreeSet<FormatIdentification>();
		this.startingOffset  = 0L;
        this.timerInfo       = new TimerInfo();
	}

	/**
	 * Instantiate a new <code>AbstractSource</code> backed by a file.
	 * 
	 * @param file
	 *            File underlying the source unit
	 */
	protected AbstractSource(File file) {
		this();
		this.file   = file;
		this.isTemp = false;
	}

	/**
	 * Instantiate a new <code>AbstractSource</code> backed by an input stream
	 * by creating a temporary file.
	 * 
     * @param tmpPrefix Temporary file prefix
     * @param tmpSuffix Temporary file suffix
     * @param bufferSize Buffer size used during transfer to a temporary file 
	 * @param stream
	 *            Input stream backing the source unit
	 * @throws IOException
	 */
	public AbstractSource(String tmpPrefix, String tmpSuffix,
			              int bufferSize, InputStream stream)
		throws IOException
	{
		this();
		this.file   = createTempFile(tmpPrefix, tmpSuffix, bufferSize, stream);
		this.isTemp = true;
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
		if (this.getSourceAccessor()==null){
			throw new JHOVE2Exception("SourceAccessor is null");
		}
		return this.sourceAccessor.addChildSource(this, child);
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
		if (this.getSourceAccessor()==null){
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
	public Source addPresumptiveFormat(FormatIdentification fi) throws JHOVE2Exception{
		return this.getSourceAccessor().addPresumptiveFormat(this, fi);
	}
	
	/** Add set of presumptively-identified formats for this source unit.
	 * @param fis Presumptively-identified formats
	 * @return Source with format added
	 * @throws JHOVE2Exception 
	 */
	@Override
	public Source addPresumptiveFormats(Set<FormatIdentification> fis) throws JHOVE2Exception{
		return this.getSourceAccessor().addPresumptiveFormats(this, fis);
	}

	/**
	 * Close the source unit. If the source unit is backed by a temporary file,
	 * delete the file.
	 */
	@Override
	public void close() {
		if (this.file != null && this.isTemp && this.deleteTempFiles) {
			this.file.delete();
		}
	}

	/**
	 * Create a temporary backing file from an input stream.
	 * 
     * @param tmpPrefix Temporary file prefix
     * @param tmpSuffix Temporary file suffix
     * @param  bufferSize Buffer size used during transfer to temporary file 
	 * @param inStream
	 *            Input stream
	 * @return file Temporary backing file
	 * @throws IOException
	 */
	protected File createTempFile(String tmpPrefix, String tmpSuffix,
			                      int bufferSize, InputStream inStream)
		throws IOException
	{
		File tempFile = File.createTempFile(tmpPrefix, tmpSuffix);
		OutputStream outStream = new FileOutputStream(tempFile);
		ReadableByteChannel in = Channels.newChannel(inStream);
		WritableByteChannel out = Channels.newChannel(outStream);
		final ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);

		while ((in.read(buffer)) > 0) {
			buffer.flip();
			out.write(buffer);
			buffer.compact(); /* in case write was incomplete. */
		}
		buffer.flip();
		while (buffer.hasRemaining()) {
			out.write(buffer);
		}

		/* Closing the channel implicitly closes the stream. */
		in.close();
		out.close();

		return tempFile;
	}

    /**
     * Create a temporary backing file that is a subset of an input stream.
     * 
     * @param tmpPrefix Temporary file prefix
     * @param tmpSuffix Temporary file suffix
     * @param  bufferSize Buffer size used during transfer to temporary file 
     * @param inFile Input file
     * @param offset Starting offset of the subset
     * @param size   Size of the subset
     * @return file Temporary backing file
     * @throws IOException
     */
    protected File createTempFile(String tmpPrefix, String tmpSuffix,
                                  int bufferSize, File inFile, 
                                  long offset, long size)
        throws IOException
    {
        /* Position input stream to starting offset. */
        InputStream inStream = new FileInputStream(inFile);
        inStream.skip(offset);
        ReadableByteChannel in = Channels.newChannel(inStream);
        
        /* Create temporary file. */
        File tempFile = File.createTempFile(tmpPrefix, tmpSuffix);
        OutputStream outStream = new FileOutputStream(tempFile);
        WritableByteChannel out = Channels.newChannel(outStream);
        
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);

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

        /* Closing the channel implicitly closes the stream. */
        in.close();
        out.close();

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
		if (this.getSourceAccessor()==null){
			throw new JHOVE2Exception("SourceAccessor is null");
		}
		return this.sourceAccessor.deleteChildSource(this, child);
	}

	/**
	 * Get delete temporary files flag; if true, delete files.
	 * 
	 * @return Delete temporary files flag
	 * @see org.jhove2.core.source.Source#getDeleteTempFiles()
	 */
	@Override
	public boolean getDeleteTempFiles() {
		return this.deleteTempFiles;
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

	
	/**
	 * Get Map of per-source parameters
	 * @return Map of per-source parameter name/parameter value pairs
	 */
	@Override
	public Map<String, String> getSourceParams() {
		return sourceParams;
	}

	/**
	 * Get {@link java.io.File} backing the source unit.
	 * 
	 * @return File backing the source unit
	 * @see org.jhove2.core.source.Source#getFile()
	 */
	@Override
	public File getFile() {
		return this.file;
	}

    /**
     * Get little-endian {@link org.jhove2.core.io.Input} for the source unit
     * with the buffer size and type specified by the 
     * {@link org.jhove2.core.Invocation}.
     * Input is returned if it exists.
     * @param jhove2 JHOVE2 framework
     * @return Input for the source unit
     * @throws IOException 
     * @throws FileNotFoundException 
     */
	@Override
    public Input getInput(JHOVE2 jhove2)
	    throws FileNotFoundException, IOException
	{
        return this.getInput(jhove2, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Get {@link org.jhove2.core.io.Input} for the source unit with the 
     * buffer size and type specified by the {@link org.jhove2.core.Invocation}
     * @param jhove2 JHOVE2 framework
     * @param order  Byte order
     * @return Input for the source unit
     */
	@Override
    public Input getInput(JHOVE2 jhove2, ByteOrder order)
        throws FileNotFoundException, IOException
    {
	    Invocation invocation = jhove2.getInvocation();
	    
        return this.getInput(invocation.getBufferSize(),
                             invocation.getBufferType(),
                             order);
    }
    
	/**
	 * Create and get little-endian {@link org.jhove2.core.io.Input} for the
	 * source unit. Concrete classes extending this abstract class must
	 * provide an implementation of this method if they are are based on
	 * parsable input. Classes without parsable input
	 * (e.g. {@link org.jhove2.core.source.ClumpSource} or
	 * {@link org.jhove2.core.source.DirectorySource} can let this inherited
	 * method return null.
	 * 
	 * @param bufferSize
	 *            Input maximum buffer size
	 * @param bufferType
	 *            Input buffer type
	 * @return Input
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception getting input
	 */
	@Override
	public Input getInput(int bufferSize, Type bufferType)
		throws FileNotFoundException, IOException
	{
		return this.getInput(bufferSize, bufferType, ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Create and get {@link org.jhove2.core.io.Input} for the source unit. Concrete
	 * classes extending this abstract class must provide an implementation of
	 * this method if they are are based on parsable input. Classes without
	 * parsable input (e.g. {@link org.jhove2.core.source.ClumpSource} or
	 * {@link org.jhove2.core.source.DirectorySource} can let this inherited
	 * method return null.
	 * 
	 * @param bufferSize
	 *            Input maximum buffer size, in bytes
	 * @param bufferType
	 *            Input buffer type
	 * @param order
	 *            Byte order
	 * @return null
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception getting input
	 */
	@Override
	public Input getInput(int bufferSize, Type bufferType, ByteOrder order)
		throws FileNotFoundException, IOException
	{
		return InputFactory.getInput(this.file, bufferSize, bufferType, order);
	}

	/**
	 * Get {@link java.io.InputStream} backing the source unit
	 * 
	 * @return Input stream backing the source unit
	 * @throws FileNotFoundException
	 * @see org.jhove2.core.source.Source#getInputStream()
	 */
	@Override
	public InputStream getInputStream()
		throws FileNotFoundException
	{
		return new FileInputStream(this.file);
	}

	/** Get source unit messages.
	 * @return Source unit messages
	 */
	@Override
	public List<Message> getMessages() {
	    return this.messages;
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
    
    /** Get starting offset of the source unit, in bytes.
     * @return Starting offset of the source unit
     * Except for {@link ByteStreamSource}s, this will generally be 0.
     */
    @Override
    public long getStartingOffset() {
        return this.startingOffset;
    }
    
	/**
	 * Get elapsed time processing the source unit.
	 * @return Elapsed time
	 */
	@Override
	public TimerInfo getTimerInfo() {
		return timerInfo;
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
	 * @see org.jhove2.core.source.Source#setDeleteTempFiles(boolean)
	 */
	@Override
	public Source setDeleteTempFiles(boolean flag) throws JHOVE2Exception {
		this.deleteTempFiles = flag;
		return this.getSourceAccessor().persistSource(this);
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
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        result = prime * result + ((modules == null) ? 0 : modules.hashCode());
        return result;
    }
	@Override
	public SourceAccessor getSourceAccessor() {
		return sourceAccessor;
	}

	@Override
	public void setSourceAccessor(SourceAccessor sourceAccessor) {
		this.sourceAccessor = sourceAccessor;
	}
	
	@Override
	public void setTimerInfo(TimerInfo timer){
		this.timerInfo = timer;
	}
	@Override
	public Source startTimer() throws JHOVE2Exception{
		return this.sourceAccessor.startTimerInfo(this);
	}
	
	@Override
	public Source endTimer() throws JHOVE2Exception{
		return this.sourceAccessor.endTimerInfo(this);
	}

	@Override
	public Long getSourceId() {
		return sourceId;
	}


	@Override
	public Long getParentSourceId() {
		return parentSourceId;
	}

	@Override
	public void setParentSourceId(Long parentSourceId) {
		this.parentSourceId = parentSourceId;
	}

	/**
	 * @return the moduleIDs
	 */
	public static Set<String> getModuleIDs() {
		return moduleIDs;
	}
}
