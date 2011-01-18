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

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.format.riff.Chunk;
import org.jhove2.module.format.riff.ChunkFactory;

import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.Persistent;

/** Spring factory for RIFF chunks.
 * 
 * @author slabrams
 */
@Persistent
public class SpringChunkFactory
        implements ChunkFactory
{
    /** Map from chunk identifiers to Spring bean names. */
	@NotPersistent
    protected static ConcurrentMap<String, String> chunkMap;
	
	/**
	 * No-argument constructor
	 */
	public SpringChunkFactory(){
		super();
	}

    /** Get a RIFF chunk based on its identifier.
     * @param identifier Chunk identifier
     * @return Chunk
     * @see org.jhove2.module.format.riff.ChunkFactory#getChunk(java.lang.String)
     */
    @Override
    public Chunk getChunk(String identifier)
        throws JHOVE2Exception
    {
        Chunk chunk = null;
        String bean = SpringChunkFactory.getChunkMap().get(identifier);
        if (bean != null) {
            chunk = SpringConfigInfo.getReportable(Chunk.class, bean);
        }
        else {
            /* Instantiate the generic chunk to handle chunks without
             * specific classes.
             */
            bean = SpringChunkFactory.getChunkMap().get("_J2_");
            if (bean != null) {
                chunk = SpringConfigInfo.getReportable(Chunk.class, bean);
                if (chunk != null) {
                    chunk.setIdentifier(identifier);
                }
            }
        }
        return chunk;
    }  

    /**
     * Gets the mapping from chunk identifiers to Spring beans. Initializes the
     * static map on first invocation.
     * @return Map from chunk identifiers to Spring bean name
     * @throws JHOVE2Exception
     */
    public static ConcurrentMap<String, String> getChunkMap()
        throws JHOVE2Exception
    {
        if (chunkMap == null) {
            chunkMap = new ConcurrentHashMap<String, String>();
            
            /* Use Spring to get instances of all objects inheriting from
             * GenericChunk.
             */
            Map<String, Object> map =
                SpringConfigInfo.getObjectsForType(Chunk.class);
            /* For each of the chunk... */
            for (Entry<String, Object> entry : map.entrySet()) {
                /* Get the Spring bean name for the chunk. */
                String bean = entry.getKey();
                
                /* Get the chunk identifier. */
                Chunk chunk = (Chunk) entry.getValue();
                String identifier = chunk.getIdentifier();
                
                /* Add an entry into the identifier-to-bean map */
                chunkMap.put(identifier, bean);
            }
        }
        return chunkMap;
    }
}
