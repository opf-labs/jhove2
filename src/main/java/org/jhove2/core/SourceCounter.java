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
package org.jhove2.core;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.DirectorySource;
import org.jhove2.core.source.FileSetSource;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.ZipDirectorySource;
import org.jhove2.core.source.ZipFileSource;

/**
 * Convenvience class.  Data structure for keeping tally of
 * number and kind of sources processed by JHOVE2 engine
 * 
 * @author mstrong, slabrams, smorrissey
 * 
 */
public class SourceCounter extends AbstractReportable {

	/** Number of bytestream source units. */
	protected int numBytestreams;

	/** Number of clump source units. */
	protected int numClumps;

	/** Number of directory source units. */
	protected int numDirectories;

	/** Number of file source units. */
	protected int numFiles;

	/** Number of pseudo-directory source units. */
	protected int numFileSets;
	
	/**
	 * Constructor
	 */
	public SourceCounter(){};
	
	/**
	 * Increment appropriate counter, depending on type of Source
	 * @param source Source whose type determines which counter to increment
	 */
	public void incrementSourceCounter(Source source){
		if (source instanceof ClumpSource) {
			this.numClumps++;
		} else if (source instanceof DirectorySource
				|| source instanceof ZipDirectorySource) {
			this.numDirectories++;
		} else if (source instanceof FileSource
				|| source instanceof ZipFileSource) {
			this.numFiles++;
		} else if (source instanceof FileSetSource) {
			this.numFileSets++;
		}
	}
	
	/**
	 * reinitializes counters 
	 */
	public void clearCounters(){
		this.numBytestreams = 0;
		this.numClumps = 0;
		this.numDirectories = 0;
		this.numFiles = 0;
		this.numFileSets = 0; 
	}

	/**
	 * Increment the number of bytestream source units.
	 */
	public void incrementNumBytestreams() {
		this.numBytestreams++;
	}

	/**
	 * Increment the number of clump source units.
	 */
	public void incrementNumClumps() {
		this.numClumps++;
	}

	/**
	 * Increment the number of directory source units, including source units
	 * for both file system files and Zip entry files.
	 */
	public void incrementNumDirectories() {
		this.numDirectories++;
	}

	/**
	 * Increment the number of file source units, including source units for
	 * both file system files and Zip entry files.
	 */
	public void incrementNumFiles() {
		this.numFiles++;
	}

	/**
	 * Increment the number of file set source units.
	 */
	public void incrementNumFileSets() {
		this.numFileSets++;
	}

	
	/**
	 * Get number of aggregate source units processed.
	 * 
	 * @return Number of aggregate source units processed
	 */
	@ReportableProperty(order = 46, value = "Number of bytestream source units "
		+ "processed.")
		public int getNumBytestreamSources() {
		return this.numBytestreams;
	}

	/**
	 * Get number of clump source units processed.
	 * 
	 * @return Number of clump source units processed
	 */
	@ReportableProperty(order = 42, value = "Number of clump source units "
		+ "processed.")
		public int getNumClumpSources() {
		return this.numClumps;
	}

	/**
	 * Get number of directory source units processed, including both file
	 * system directories and Zip entry directories.
	 * 
	 * @return Number of directory source units processed
	 */
	@ReportableProperty(order = 44, value = "Number of directory source units "
		+ "processed, including both file system directories and Zip "
		+ "entry directories.")
		public int getNumDirectorySources() {
		return this.numDirectories;
	}

	/**
	 * Get number of file source units processed, including both file system
	 * files and Zip entry files.
	 * 
	 * @return Number of file source units processed
	 */
	@ReportableProperty(order = 45, value = "Number of file source units "
		+ "processed, including both file system files and Zip entry "
		+ "files.")
		public int getNumFileSources() {
		return this.numFiles;
	}

	/**
	 * Get number of source units processed.
	 * 
	 * @return Number of source units processed
	 */
	@ReportableProperty(order = 41, value = "Number of source units processed.")
	public int getNumSources() {
		return this.numFileSets + this.numDirectories + this.numClumps
		+ this.numFiles + this.numBytestreams;
	}

	/**
	 * Get number of file set source units processed.
	 * 
	 * @return Number of file set source units processed
	 */
	@ReportableProperty(order = 45, value = "Number of file set source units "
		+ "processed.")
		public int getNumFileSetSources() {
		return this.numFileSets;
	}


	
}
