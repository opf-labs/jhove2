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
 * o Neither the category of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the categorys of
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

package org.jhove2.module.format.wave.bwf.field;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** Broadcast Wave Format (BWF) MPEG-1 layer.
 * 
 * @author slabrams
 */
public class MPEGLayer
    implements Comparable<MPEGLayer>
{
    /** Singleton MPEG layer layers. */
    protected static Set<MPEGLayer> layers;

    /** MPEG layer layer. */
    protected int layer;

    /** MPEG description. */
    protected String description;

    /**
     * Instantiate a new <code>MPEGLayer</code> object.
     * 
     * @param layer
     *            MPEG layer
     * @param description
     *            MPEG layer description
     */
    public MPEGLayer(int layer, String description) {
        this.layer   = layer;
        this.description = description;
    }
    
    /** Initialize the layers.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (layers == null) {
            /* Initialize the Layer layers from a Java resource bundle. */
            layers = new TreeSet<MPEGLayer>();
            Properties props = jhove2.getConfigInfo().getProperties("MPEGLayers");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String lay  = iter.next();
                    String des = props.getProperty(lay);
                    MPEGLayer f =
                        new MPEGLayer(Integer.valueOf(lay, 16), des);
                    layers.add(f);
                }
            }
        }
    }

    /**
     * Get the description for a layer. 
     * @param layer   MPEG layer
     * @param jhove2 JHOVE2 framework
     * @return Layer MPEG layer description, or null if the layer is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized MPEGLayer getMPEGLayer(int layer, JHOVE2 jhove2)
        throws JHOVE2Exception 
    {
        init(jhove2);
        MPEGLayer lay = null;
        Iterator<MPEGLayer> iter = layers.iterator();
        while (iter.hasNext()) {
            MPEGLayer l = iter.next();
            if (l.getLayer() == layer) {
                lay = l;
                break;
            }
        }
        return lay;
    }

    /**
     * Get the MPEG layers.
     * @param jhove2 JHOVE2 framework
     * @return MPEG layers
     * @throws JHOVE2Exception 
     */
    public static Set<MPEGLayer> getLayers(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return layers;
    }

    /**
     * Get the MPEG layer.
     * @return MPEG layer 
     */
    public int getLayer() {
        return this.layer;
    }

    /**
     * Get the layer description.
     * @return Layer description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the MPEG layer to a Java string in the form:
     * "layer: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getLayer() + ": " + this.getDescription();
    }

    /**
     * Compare MPEG layer layer.
     * @param layer
     *             MPEG layer to be compared
     * @return -1, 0, or 1 if this MPEG layer layer is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(MPEGLayer layer) {
        int lay = layer.getLayer();
        if (this.layer < lay) {
            return -1;
        }
        else if (this.layer > lay) {
            return 1;
        }
        return 0;
    }
}
