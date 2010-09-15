/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California.
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

package org.jhove2.module.format.icc.type;

/** ICC colorant, as defined in ICC.1:2004-10, \u00a7 10.4.
 * 
 * @author slabrams
 */
public class Colorant
{
    /** Colorant name. */
    protected String name;
    
    /** Colorant Profile Connection Space (PCS) value. */
    protected PCSNumber pcs;
    
    /** Instantiate a new <code>Colorant</code>.
     * @param name Colorant name
     * @param pcs  Colorant Profile Connection Space (PCS) value.
     */
    public Colorant(String name, PCSNumber pcs) {
        this.name = name;
        this.pcs  = pcs;
    }
    
    /** Get colorant name.
     * @return Colorant name
     */
    public String getName() {
        return this.name;
    }
    
    /** Get colorant Profile Connection Space (PCS) value.
     * @return PCS value
     */
    public PCSNumber getPCS() {
        return this.pcs;
    }
    
    /** String representation of colorant, in the form
     * "name: x, y, z".
     */
    public String toString() {
        return this.name + ": " + this.pcs;
    }
}
