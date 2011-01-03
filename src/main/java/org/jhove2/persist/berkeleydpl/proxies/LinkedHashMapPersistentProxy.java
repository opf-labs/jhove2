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
package org.jhove2.persist.berkeleydpl.proxies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PersistentProxy;

/**
 * Peristent proxy for LinkedHashMap (needed for Spring initialization)
 * ALWAYS preserves insertion order.
 * @author smorrissey
 *
 */
@SuppressWarnings("unchecked")
@Persistent(proxyFor=LinkedHashMap.class)
public class LinkedHashMapPersistentProxy implements PersistentProxy<LinkedHashMap> {

	protected HashMap map = null;
	protected ArrayList keyList = null;
	/**
	 * 
	 */
	public LinkedHashMapPersistentProxy() {}

	@Override
	public LinkedHashMap convertProxy() {
		LinkedHashMap linkedHashMap = null;
		if (map != null && keyList != null){
			linkedHashMap = new LinkedHashMap(map.size());
			for (int i=0; i<keyList.size(); i++){
				linkedHashMap.put(keyList.get(0), map.get(keyList.get(0)));
			}
		}
		return linkedHashMap;
	}

	@Override
	public void initializeProxy(LinkedHashMap linkedHashMap) {		
		if (linkedHashMap != null){
			map = new HashMap();
			keyList = new ArrayList();
			 Set<Map.Entry> entrySet = linkedHashMap.entrySet();
			 for (Map.Entry entry:entrySet){
				 keyList.add(entry.getKey());
				 map.put(entry.getKey(), entry.getValue());
			 }
		}
	}

}
