/**
  * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */

package org.jhove2.module.format.icc.type;

import com.sleepycat.persist.model.Persistent;


/** ICC XYZ number, a set of three fixed signed s15Fixed16Numbers.
 * See ICC.1:2004-10, \u00a7 5.1.11.
 * 
 * @author slabrams
 */
@Persistent
public class XYZNumber
{
    /** X number. */
    protected S15Fixed16Number x;

    /** Y number. */
    protected S15Fixed16Number y;

    /** Z number. */
    protected S15Fixed16Number z;
    
    /** Instantiate a new <code>XYZNumber</code>
     */
    public XYZNumber(int x, int y, int z) {
    	this();
        this.x = new S15Fixed16Number(x);
        this.y = new S15Fixed16Number(y);
        this.z = new S15Fixed16Number(z);
    }
    
    private XYZNumber(){
    	super();
    }
    
    /** Get X value.
     * @return X value
     */
    public S15Fixed16Number getX() {
        return this.x;
    }
    
    /** Get Y value.
     * @return Z value
     */
    public S15Fixed16Number getY() {
        return this.y;
    }
    
    /** Get Z value.
     * @return Z value
     */
    public S15Fixed16Number getZ() {
        return this.z;
    }
    
    /** String representation of the numbers.
     * @return String representation of the numbers
     */
    public String toString() {
        return this.x.toString() + ", " + this.y.toString() + ", " +
               this.z.toString();
    }
}
