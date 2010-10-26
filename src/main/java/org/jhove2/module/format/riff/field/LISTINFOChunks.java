/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California
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

package org.jhove2.module.format.riff.field;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** RIFF format LIST INFO chunk.
 * 
 * @author slabrams
 */
public class LISTINFOChunks
implements Comparable<LISTINFOChunks>
{
    /** Singleton LIST INFO chunks. */
    protected static Set<LISTINFOChunks> chunks;

    /** LIST INFO chunk identifier. */
    protected String identifier;

    /** LIST INFO chunk description. */
    protected String description;

    /**
     * Instantiate a new <code>LISTINFOChunk</code> object.
     * 
     * @param identifier
     *            LIST INFO chunk identifier
     * @param description
     *            LIST INFO chunk description
     */
    public LISTINFOChunks(String identifier, String description) {
        this.identifier  = identifier;
        this.description = description;
    }

    /**
     * Get the chunk for an identifier. 
     * @param identifier LIST INFO chunk identifier
     * @param jhove2     JHOVE2 framework
     * @return LIST INFO chunk, or null if the type is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized LISTINFOChunks getLISTINFOChunk(String identifier, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (chunks == null) {
            /* Initialize the LIST INFO chunks from a Java resource bundle. */
            chunks = new TreeSet<LISTINFOChunks>();
            Properties props = jhove2.getConfigInfo().getProperties("LISTINFOChunks");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String id  = iter.next();
                    String des = props.getProperty(id);
                    LISTINFOChunks chunk = new LISTINFOChunks(id, des);
                    chunks.add(chunk);
                }
            }
        }
        LISTINFOChunks chunk = null;
        Iterator<LISTINFOChunks> iter = chunks.iterator();
        while (iter.hasNext()) {
            LISTINFOChunks ch = iter.next();
            if (ch.getIdentifier().equals(identifier)) {
                chunk = ch;
                break;
            }
        }
        return chunk;
    }

    /**
     * Get the LIST INFO chunks.
     * @return LIST INFO chunks
     */
    public static Set<LISTINFOChunks> getLISTINFOChunks() {
        return chunks;
    }

    /**
     * Get the LIST INFO chunk description.
     * @return LIST INFO chunk description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the LIST INFO chunk identifier.
     * @return LIST INFO chunk identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Convert the LIST INFO chunk description to a Java string in the form:
     * "identifier: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getIdentifier() + ": " + this.getDescription();
    }

    /**
     * Compare LIST INFO chunk.
     * @param identifier
     *            LIST INFO chunk identifier to be compared
     * @return -1, 0, or 1 if this LIST INFO chunk is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(LISTINFOChunks chunk) {
        return this.identifier.compareTo(chunk.getIdentifier());
    }
}
