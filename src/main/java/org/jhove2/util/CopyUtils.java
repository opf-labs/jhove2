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
package org.jhove2.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Utility methods for making deep copies of objects
 * @author smorrissey
 *
 */
public class CopyUtils {

	/**
	 * Copy items from source to target list; then clear source list and set it to null
	 * @param sourceList List to be copied
	 * @return deep copy of list
	 */
	public static List<String> copyAndClearList(List<String> sourceList) {
		ArrayList<String> newList = null;
		if (sourceList != null){
			newList = new ArrayList<String>();
			for (String listItem : sourceList){
				newList.add(listItem);
			}
			sourceList.clear();
			sourceList = null;
		}	
		return newList;
	}
	/**
	 *  Copy items from source to target map; then clear source map and set it to null
	 * @param sourceMap Map to be copied
	 * @return deep copy of map
	 */
	public static Map<String, Integer> copyAndClearIntMap(Map<String, Integer> sourceMap) {
		HashMap<String, Integer> map = null;
		if (sourceMap != null){
			map = new HashMap<String, Integer>();
			for (Map.Entry<String, Integer> entry:sourceMap.entrySet()){
				map.put(entry.getKey(), entry.getValue());
			}
			sourceMap.clear();
			sourceMap = null;
		}		
		return map;
	}
	/**
	 *  Copy items from source to target map; then clear source map and set it to null
	 * @param sourceMap Map to be copied
	 * @return deep copy of map
	 */
	public static Map<String, String> copyAndClearStringMap(Map<String, String> sourceMap) {
		HashMap<String, String> map = null;
		if (sourceMap != null){
			map = new HashMap<String, String>();
			for (Map.Entry<String, String> entry:sourceMap.entrySet()){
				map.put(entry.getKey(), entry.getValue());
			}
			sourceMap.clear();
			sourceMap = null;
		}		
		return map;
	}
	/**
	 *  Copy items from source to target map; then clear source map and set it to null
	 * @param sourceMap Map to be copied
	 * @return deep copy of map
	 */
	public static Map<String, List<String>> copyAndClearListMap(Map<String, List<String>> sourceMap){
		HashMap<String, List<String>> newMap = null;
		if (sourceMap != null){
			newMap = new HashMap<String, List<String>>();
			for (Map.Entry<String, List<String>> entry:sourceMap.entrySet()){
				String key = entry.getKey();
				List<String> values = entry.getValue();
				ArrayList<String> newValues = null;
				if (values !=null){
					newValues = new ArrayList<String>();
					for(String stValue:values){
						newValues.add(stValue);
					}
					values.clear();
				}
				newMap.put(key, newValues);
			}
			sourceMap.clear();
			sourceMap = null;
		}		
		return newMap;
	}
	/**
	 *  Copy items from source to target set; then clear source set and set it to null
	 * @param sourceSet set to be copied
	 * @return deep copy of set
	 */
	public static SortedSet<String> copyAndClearSet(SortedSet<String> sourceSet){
		TreeSet<String> newSet = null;
		if (sourceSet != null){
			newSet = new TreeSet<String>();
			for (String element:sourceSet){
				newSet.add(element);
			}
			sourceSet.clear();
			sourceSet = null;
		}		
		return newSet;
	}

}
