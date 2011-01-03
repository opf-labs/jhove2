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

import com.sleepycat.persist.model.Persistent;

/** Profile Connection Space (PCS) values.
 * 
 * @author slabrams
 */
@Persistent
public class PCSNumber
{
    /** PCS X value. */
    protected int x;
    
    /** PCS Y value. */
    protected int y;
    
    /** PCS Z value. */
    protected int z;
    
    private PCSNumber(){
    	super();
    }
    /** Instantiate a new <code>PCSNumber</code>.
     * @param x PCS X value
     * @param y PCS Y value
     * @param z PCS Z value
     */
    public PCSNumber(int x, int y, int z) {
    	this();
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /** Get X value.
     * @return X value
     */
    public int getX() {
        return this.x;
    }
    
    /** Get Y value.
     * @return Z value
     */
    public int getY() {
        return this.y;
    }
    
    /** Get Z value.
     * @return Z value
     */
    public int getZ() {
        return this.z;
    }
    
    /** String representation of the numbers.
     * @return String representation of the numbers
     */
    public String toString() {
        return x + ", " + y + ", " +z;
    }
}
