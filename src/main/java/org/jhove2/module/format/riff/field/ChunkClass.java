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

/** RIFF chunk class.
 * 
 * @author slabrams
 */
public class ChunkClass
    implements Comparable<ChunkClass>
{
    /** Singleton RIFF chunk form classes. */
    protected static Set<ChunkClass> classes;

    /** RIFF chunk form identifier. */
    protected String identifier;

    /** RIFF chunk class package-qualified class name. */
    protected String className;

    /**
     * Instantiate a new <code>ChunkClass</code> object.
     * 
     * @param identifier
     *            RIFF chunk form identifier
     * @param name
     *            RIFF chunk package qualified class name
     */
    public ChunkClass(String identifier, String name) {
        this.identifier = identifier;
        this.className  = name;
    }

    /**
     * Get the class name for a identifier. 
     * @param identifier RIFF chunk identifier
     * @param jhove2     JHOVE2 framework
     * @return RIFF chunk class, or null if the identifier is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized ChunkClass getChunkClass(String identifier, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (classes == null) {
            /* Initialize the RIFF classes from a Java resource bundle. */
            classes = new TreeSet<ChunkClass>();
            Properties props = jhove2.getConfigInfo().getProperties("ChunkClasses");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String id  = iter.next();
                    String nam = props.getProperty(id);
                    ChunkClass chunkClass = new ChunkClass(id, nam);
                    classes.add(chunkClass);
                }
            }
        }
        ChunkClass chunkClass = null;
        Iterator<ChunkClass> iter = classes.iterator();
        while (iter.hasNext()) {
            ChunkClass cls = iter.next();
            if (cls.getIdentifier().equals(identifier)) {
                chunkClass = cls;
                break;
            }
        }
        return chunkClass;
    }

    /**
     * Get the RIFF chunk classes.
     * @return RIFF chunk classes
     */
    public static Set<ChunkClass> getChunkClasss() {
        return classes;
    }

    /**
     * Get the RIFF chunk identifier.
     * @return RIFF chunk identifier 
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Get the RIFF chunk package-qualified class name.
     * @return RIFF chunk package-qualified class name
     */
    public String getName() {
        return this.className;
    }

    /**
     * Convert the RIFF chunk class name to a Java string in the form:
     * "identifier: name".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getIdentifier() + ": " + this.getName();
    }

    /**
     * Compare RIFF chunk form identifier.
     * @param chunk
     *            RIFF chunk class to be compared
     * @return -1, 0, or 1 if this RIFF chunk form identifier is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(ChunkClass chunk) {
        return this.identifier.compareTo(chunk.getIdentifier());
    }
}
