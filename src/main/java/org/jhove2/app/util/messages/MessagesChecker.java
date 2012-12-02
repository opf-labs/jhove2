/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2012 by The Regents of the University of California,
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

package org.jhove2.app.util.messages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author smorrissey
 *
 */
public class MessagesChecker {
	
	public static String SEPCHAR;
	
	static{
		SEPCHAR = System.getProperty("file.separator");
	}

	/**
	 * 
	 */
	public MessagesChecker() {
		super();
	}

	 protected SortedSet<String> getPropertiesMessageKeys(String propertiesFilePath) 
	 throws IOException{
		 TreeSet<String> messageKeys = new TreeSet<String>();
		 File propsFile = new File(propertiesFilePath);
		 if (!propsFile.exists()){
			 throw new IOException ("File " + propertiesFilePath + " does not exist");
		 }
		 if (!propsFile.isDirectory()){
			 throw new IOException ("File " + propertiesFilePath + " is not a directory");
		 }		 
		 PropertyFileFilter pFilter = new PropertyFileFilter();
		 String[]propFiles = propsFile.list(pFilter);
		 if (propFiles != null){	
			 for (String propFile:propFiles){
				 String propFilePath = propertiesFilePath.concat(SEPCHAR).concat(propFile);
				 FileInputStream in = new FileInputStream(propFilePath);
				 Properties props = new Properties();
				 props.load(in);
				 if (in != null){
					 in.close();
				 }
				Set<String> propNames = props.stringPropertyNames();
				messageKeys.addAll(propNames);
			 }
		 }
		return messageKeys;
	 }
	 
	 protected SortedSet<String> getJavaMessageKeys(String javaPath) 
	 throws IOException{
		 SortedSet<String> messageKeys = new TreeSet<String>();
		 File javaFile = new File(javaPath);
		 if (!javaFile.exists()){
			 throw new IOException ("File " + javaPath + " does not exist");
		 }
		 if (javaFile.isFile()){
			 messageKeys = this.findMessages(javaPath);
		 }
		 else {
			 JavaFileFilter jFilter = new JavaFileFilter();
			 DirFileFilter dFilter = new DirFileFilter();
			 String[]javaFiles = javaFile.list(jFilter);
			 String[]subDirs = javaFile.list(dFilter);
			 if (javaFiles != null && javaFiles.length>0){
				 for (String jFile:javaFiles){
					 String jFileName = javaPath.concat(SEPCHAR).concat(jFile);
					 messageKeys.addAll(this.findMessages(jFileName));
				 }
			 }
			 if (subDirs != null && subDirs.length>0){
				 for (String dir : subDirs){
					 String dirName = javaPath.concat(SEPCHAR).concat(dir);
					 messageKeys.addAll(this.getJavaMessageKeys(dirName));
				 }
			 }
		 }
		 return messageKeys;
	 }
	 
	 protected SortedSet<String> findMessages (String javaPath) 
	 throws IOException{
		 SortedSet<String> messageKeys = new TreeSet<String>();
		 MessageFinder mf = new MessageFinder();
		 messageKeys = mf.findMessageCodes(javaPath);		 
		 return messageKeys;		 
	 }
	 
	 protected void compareKeySets(SortedSet<String> propsKeys, SortedSet<String> javaKeys) 
	 throws Exception{
		 if (propsKeys==null ){
			 throw new Exception ("null properties keys");
		 }
		 if (javaKeys == null){
			 throw new Exception ("null java keys");
		 }
		 // list counts
		 int propKeyCnt = propsKeys.size();
		 int javaKeyCnt = javaKeys.size();
		 System.out.println("Number of properties file keys: " + propKeyCnt);
		 System.out.println("Number of Java file keys:       " + javaKeyCnt);		 
		 // display keys in Java code but not in properties file
		 TreeSet<String> unMatchedKeys = new TreeSet<String>();
		 for (String jKey : javaKeys){
			 if (! propsKeys.contains(jKey)) {
				 unMatchedKeys.add(jKey);
			 }
		 }
		 if (unMatchedKeys.size()>0){
			 System.out.println("\n\nThe following message keys were found in Java code but not in properties file");
			 for (String jKey : unMatchedKeys){
				 System.out.println("\t" + jKey);
			 }
		 }
		 else {
			 System.out.println("\n\nAll message keys in Java code were found in the properties file");
		 }
		 // display keys in properties file but not in Java code
		 unMatchedKeys.clear();
		 for (String pKey : propsKeys){
			 if (! javaKeys.contains(pKey)){
				 unMatchedKeys.add(pKey);
			 }
		 }
		 if (unMatchedKeys.size()>0){
			 System.out.println("\n\nThe following message keys were found in the properties file but not in Java code");
			 for (String pKey : unMatchedKeys){
				 System.out.println("\t" + pKey);
			 }
		 }
		 else {
			 System.out.println("\n\nAll message keys inthe properties file were found in Java code");
		 }
		 return;
	 }
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2){
			System.err.println("usage: MessagesChecker <propertyFilePath> <javaFileOrBaseDirPath");
			System.exit(1);
		}
		String propertiesFilePath = args[0];
		String javaPath = args[1];
		System.out.println("MessagesChecker:  Property file path = " + propertiesFilePath);
		System.out.println("MessagesChecker:  Java file-or-dir path = " + javaPath);
		MessagesChecker mc = new MessagesChecker();
		try {
			SortedSet<String> propsKeys = mc.getPropertiesMessageKeys(propertiesFilePath);
			SortedSet<String> javaKeys = mc.getJavaMessageKeys(javaPath);
			mc.compareKeySets(propsKeys, javaKeys);
		} catch (IOException e) {
			System.out.println("Exception thrown extracting key sets");
			e.printStackTrace();
			System.exit(2);
		} catch (Exception e) {
			System.out.println("Exception thrown comparing key sets");
			e.printStackTrace();
			System.exit(3);
		}
		System.exit(0);
	}

}
