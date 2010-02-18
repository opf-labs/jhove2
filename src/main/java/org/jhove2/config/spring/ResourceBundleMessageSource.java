/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * 
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
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

package org.jhove2.config.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

/**
 * Class to find all JHOVE2 messages.properties files on the classpath
 * Path to these files should conform to the pattern
 * "classpath/0 or more directories/messages/0 or more directories/someFileName_BASENAME.properties"
 * In the Spring config file, the basenames property for a bean of this class conventionally would 
 * be set to "messages"
 * 
 * This enables creators of new modules, by following the naming conventions for message properties files,
 * and by putting such files on the classpath, to expose these files to discovery without
 * any changes required to the Spring configuration files
 * @author smorrissey
 */
public class ResourceBundleMessageSource extends
		org.springframework.context.support.ResourceBundleMessageSource {
	
	public static final String CLASSPATH_DIRS_PATH = "classpath*:";
	
	public static final String MESSAGE_FILE_BASE_MASK = "classpath*:/messages/**/*_";
	
	public static final String EXTENSION = ".properties";

	/**
	 * Constructs array of basenames for properties files, consisting of dotted path
	 * notation to the files.  For example, messages/org/jhove2/core/JHOVE2_messages.properties
	 * would be converted to the basename messages.org.jhove2.core.JHOVE2_messages
	 * 
	 * Method finds all files on classpath that match the MESSAGE_FILE_BASE_MASK + each basename
	 * + EXTENSION; then strips off classpath portion of the file's path, and the EXTENSION,
	 * replacing all file separator characters with "."
	 * 
	 * @param String[] of basenames to be converted
	 */
	@Override
	public void setBasenames(String[] basenames)  {
		ArrayList<String> trimmedBasenames = null;
		String [] filteredBasenames = null;
		if (basenames != null){
			trimmedBasenames = new ArrayList<String>();
			for (int i = 0; i < basenames.length; i++) {
				String basename = basenames[i];
				Assert.hasText(basename, "Basename must not be empty");
				trimmedBasenames.add(basename.trim());
			}
			TreeSet<URI> classpathDirs = this.findClassPathDirectories();
			TreeSet<URI> messageFiles = this.findMessageFiles(trimmedBasenames);
			ArrayList<String> dottedPaths = 
				this.fileURIs2Classpath(classpathDirs, messageFiles);
			int mfSize = messageFiles.size();
			int dfSize = dottedPaths.size();
			boolean isEqualSize = (mfSize==dfSize);
			Assert.isTrue(isEqualSize,
					"Failed to find a directory prefix for one or more message files");
			filteredBasenames = dottedPaths.toArray(new String[0]);
		}
		else {
			filteredBasenames = new String[0];
		}	
		super.setBasenames(filteredBasenames);
	}
	/**
	 * Strips classpath prefix in any file;s path to get relative path to file; strips
	 * extension, and replaces "/" with "."
	 * @param messageDirs TreeSet of directories on classpath that match pattern
	 * @param messageFiles TreeSet of properites files on classpath that match pattern
	 * @return ArrayList<String> of dotted-notation relative paths to message.properties files
	 */
	public ArrayList<String> fileURIs2Classpath(
			TreeSet<URI> messageDirs, TreeSet<URI> messageFiles){		
		ArrayList<String> dottedPaths = new ArrayList<String>();	
		Iterator<URI> fIter = messageFiles.descendingIterator();		
		while (fIter.hasNext()){
			URI fileURI = fIter.next();
			String fileStr = fileURI.toString();
			Iterator<URI> dIter = messageDirs.descendingIterator();
			boolean foundPrefix = false;
			String dirPrefix = null;
			while (dIter.hasNext()){
				URI dirURI = dIter.next();
				String dirStr = dirURI.toString();
				if (fileStr.startsWith(dirStr)){
					foundPrefix = true;
					dirPrefix = dirStr;
					break;
				}
			}
			Assert.isTrue(foundPrefix, 
					"Failed to find directory prefix for " + fileStr);
			String relFilePath = fileStr.substring(dirPrefix.length());
			relFilePath = relFilePath.replace("/", ".");
			int i= relFilePath.lastIndexOf(EXTENSION);
				if (i>0){
					relFilePath = relFilePath.substring(0, i);
				}
			dottedPaths.add(relFilePath);
		}
		return dottedPaths;		
	}
	
	/**
	 * Finds any basename.properties files on the masked path
	 * @param baseNames  ArrayList<String> of basenames to be discoverd
	 * @return TreeSet<URI> of all matching properites files
	 */
	public TreeSet<URI> findMessageFiles (ArrayList<String> baseNames){
		TreeSet<URI> files = new TreeSet<URI>();
		PathMatchingResourcePatternResolver resolver = 
			new PathMatchingResourcePatternResolver();
		for (String baseName:baseNames){
			String resourceMask = MESSAGE_FILE_BASE_MASK + baseName + EXTENSION;
			try {
				Resource[] resources = resolver.getResources(resourceMask);
				for (Resource resource:resources){
					URI uri = resource.getURI();
					files.add(uri);
				}
			} catch (IOException e) {
				Assert.isTrue(false, "IO exception when attempting to resolve resources for " +
						resourceMask);
			}
		}
		return files;
	}
	/**
	 * Gets list of all directories on the classpath
	 * @return list of all directories on the classpath
	 */
	public TreeSet<URI> findClassPathDirectories(){
		TreeSet<URI> messageDirs = new TreeSet<URI>();
		PathMatchingResourcePatternResolver resolver = 
			new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = resolver.getResources(CLASSPATH_DIRS_PATH);
			for (Resource resource: resources){
				URI uri = resource.getURI();
				File file = null;
				try {
					file = resource.getFile();
					if (file.isDirectory()){
						messageDirs.add(uri);
					}
				}catch (IOException e) {
					// not a file, or not an accessible one}
				}
			}
		} catch (IOException e) {
			Assert.isTrue(false, "IO exception when attempting to resolve resources for " +
					CLASSPATH_DIRS_PATH);
		}
		return messageDirs;
	}

}
