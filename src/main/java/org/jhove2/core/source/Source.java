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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.Reportable;
import org.jhove2.module.Module;
import org.jhove2.persist.SourceAccessor;

/**
 * Interface for JHOVE2 source units. A source unit is a formatted object that
 * can be characterized, which may be a file, a subset of a file, or a group of
 * files.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public interface Source
	extends Reportable, Comparable<Source>
{
	/**
	 * Add a child source unit.
	 * 
	 * @param child
	 *            Child source unit
	 * @return Child Source
	 * @throws JHOVE2Exception 
	 */
	public Source addChildSource(Source child) throws JHOVE2Exception;
	
	/** Add a message to be associated with the source unit.
	 * @param message Message to be associated with the source unit
	 * @return Source with message added
	 * @throws JHOVE2Exception 
	 */
	public Source addMessage(Message message) throws JHOVE2Exception;

	/**
	 * Add a module that processed the source unit.
	 * Generic modules are only add once, to the first Source upon which they are invoked
	 * Specific modules are added to each Source upon which they are invoked
	 * @param module
	 *            Module that processed the source unit
	 * @return Module that processed the source unit
	 * @throws JHOVE2Exception 
	 */
	public Module addModule(Module module) throws JHOVE2Exception;
	
	/**
	 * Add presumptive format to the source unit.
	 * @param fi Presumptive format
	 * @return Source with added format
	 * @throws JHOVE2Exception 
	 */
	public Source addPresumptiveFormat(FormatIdentification fi) throws JHOVE2Exception;
	
	/**
	 * Add set of presumptive formats to the source unit.
	 * @param fis Set of presumptive formats
	 * @return Source with added formats
	 * @throws JHOVE2Exception 
	 */
	public Source addPresumptiveFormats(Set<FormatIdentification> fis) throws JHOVE2Exception;

    /**
     * Close the source unit and release all underlying system I/O resources,
     * including a temporary backing file, if one exists.
     */
    public void close();

	/**
	 * Delete child source unit.
	 * 
	 * @param child
	 *            Child source unit
	 * @return Child source deleted from parent
	 * @throws JHOVE2Exception 
	 */
	public Source deleteChildSource(Source child) throws JHOVE2Exception;

	/**
	 * Get child source units.
	 * 
	 * @return Child source units
	 * @throws JHOVE2Exception 
	 */
	@ReportableProperty(order=6, value="Child source units.")
	public List<Source> getChildSources() throws JHOVE2Exception;

	/**
	 * Get temporary file deletion flag; if true, delete on close.
	 * 
	 * @return Deletion flag
	 */
	public boolean getDeleteTempFileOnClose();
	   
    /**
     * Get {@link java.io.File} backing the source unit.
     * 
     * @return File backing the source unit, of null if a Clump or FileSet source
     */
    public File getFile();

    /**
     * Get little-endian {@link org.jhove2.core.io.Input} for the source unit
     * with the buffer size and type specified by the
     * {@link org.jhove2.core.Invocation}.
     * If this method is called explicitly, then the corresponding close()
     * method must be called to avoid a resource leak.
     * @param jhove2 JHOVE2 framework
     * @return Input for the source unit
     */
    public Input getInput(JHOVE2 jhove2)
        throws FileNotFoundException, IOException;

    /**
     * Get {@link org.jhove2.core.io.Input} for the source unit with the 
     * buffer size and type specified by the {@link org.jhove2.core.Invocation}.
     * If this method is called explicitly and the source unit is not
     * processed by the JHOVE2.characterize() method, then the corresponding
     * close() method must be called to avoid a resource leak.
     * @param jhove2 JHOVE2 framework
     * @param order  Byte order
     * @return Input for the source unit
     */
    public Input getInput(JHOVE2 jhove2, ByteOrder order)
        throws FileNotFoundException, IOException;
  
	/**
	 * Get {@link java.io.InputStream} for the file backing the source unit.
	 * 
	 * @return Input stream for the file backing the source unit, or null if
	 *         a Clump, Directory, or FileSet source
	 * @throws FileNotFoundException Backing file not found
	 * @throws IOException Backing file cannot be created
	 */
	public InputStream getInputStream() throws FileNotFoundException, IOException;

	/**
	 * Get copy of List of modules that processed the source unit.
	 * 
	 * @return Modules that processed the source unit
	 * @throws JHOVE2Exception 
	 */
	@ReportableProperty(order=3, value="Modules that processed the source unit")
	public List<Module> getModules() throws JHOVE2Exception;

	/**
	 * Get number of child source units.
	 * 
	 * @return Number of child source units
	 * @throws JHOVE2Exception 
	 */
	@ReportableProperty(order=5, value="Number of child source units.")
	public int getNumChildSources() throws JHOVE2Exception;
	
	/** Get messages associated with the source unit.
	 * @return Source unit messages
	 */
	@ReportableProperty(order=2, value="Source unit messages.")
	public List<Message> getMessages();

	/**
	 * Get number of modules.
	 * 
	 * @return Number of modules
	 * @throws JHOVE2Exception 
	 */
	public int getNumModules() throws JHOVE2Exception;

    /**
     * @return the moduleParentSourceId
     */
    public Long getParentSourceId();
    
	/**
	 * Get list of presumptive formats for the source unit.
	 * @return List of presumptive formats
	 */
	@ReportableProperty(order=1, value="Presumptive formats for the source.")
	public Set<FormatIdentification> getPresumptiveFormats();
	   
    /**
     * @return the sourceId
     */
    public Long getSourceId();

	/**
	 * Get SourceAccessor that manages persistence for this Source
	 * @return SourceAccessor  that manages persistence for this Source
	 */
	public SourceAccessor getSourceAccessor();
	
	/**
	 * Get Map of per-source parameters
	 * @return Map of per-source parameter name/parameter value pairs
	 */
	public Map<String, String> getSourceParams();
	
	/**
	 * Get elapsed time processing this source unit.
	 * @return Elapsed time
	 */
	@ReportableProperty(order=7, value="Timer info for this Source.")
	public TimerInfo getTimerInfo();

    /** Aggregate source flag: true if an aggregate source.
     * @return Aggregate source flag
     */
    @ReportableProperty(order=4, value="Aggregate source status: true if an " +
        "aggregate source.")
    public boolean isAggregate();
    
	/**
	 * Get temporary flag: true if the file backing the source unit is a 
	 * temporary file.
	 * 
	 * @return True if the source unit backing file is a temporary file
	 */
	public boolean isTemp();

	/**
	 * Set temporary file deletion flag; if true, delete on close.
	 * 
	 * @param flag
	 *            Delete temporary files flag
	 * @return Source with new deletion flag set
	 * @throws JHOVE2Exception 
	 */
	public Source setDeleteTempOnClose(boolean flag) throws JHOVE2Exception;

    /** Set aggregate flag.
     * @param flag Aggregate flag: true if an aggregate
     */
    public Source setIsAggregate(boolean flag) throws JHOVE2Exception;

    /**
     * @param moduleParentSourceId the moduleParentSourceId to set
     */
    public void setParentSourceId(Long parentSourceId);

	/**
	 * Set SourceAccessor that manages Source persistence
	 * @param accessor SourceAccessor for this Source
	 */
	public void setSourceAccessor(SourceAccessor accessor);
	
	/**
	 * Set Map of per-source parameters
	 * @param sourceParams Map of per-source parameter name/parameter value pairs
	 * @return Source with new sourceParams
	 * @throws JHOVE2Exception 
	 */
	public Source setSourceParams(Map<String, String> sourceParams) throws JHOVE2Exception;
	
	/**
	 * Set timerInfo on Source
	 * @param timer TimerInfo
	 */
	public void setTimerInfo(TimerInfo timer);
	
	/**
	 * Start TimerInfo
	 * @return Source with TimerInfo started
	 * @throws JHOVE2Exception
	 */
	public Source startTimer() throws JHOVE2Exception;
	
	/**
	 * Stop TimeInfo
	 * @return Source with TimerInfo stopped
	 * @throws JHOVE2Exception
	 */
	public Source endTimer() throws JHOVE2Exception;	
}
