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

/** ICC fixed unsigned 2 byte/16 bit number with 8 fractional bits. The number
 * is organized as:
 * iiiiiiiiffffffff, with the value:
 * iiiiiiii + ffffffff/256. See ICC.1:2004-10, \u00a7 5.1.6.
 * 
 * @author slabrams
 */
public class U8Fixed8Number
{
    /** Fractional denominator. */
    public static final int DENOMINATOR = 256;
    
    /** Fractional part: 0x00ff. */
    protected int fractional;
    
    /** Unsigned integral part: 0xff00. */
    protected int integral;
    
    /** Floating point value. */
    protected double value;
    
    /** Instantiate a new <code>U15Fixed16Number</code>.
     */
    public U8Fixed8Number(int in) {
        this.integral   = (in & 0xff00) >> 8;
        this.fractional =  in & 0x00ff;
        this.value      = this.integral +
                 ((double)this.fractional/(double)DENOMINATOR);
        /* Only keep 5 significant fractional digits. */
        long ln     = (long)(this.value*100000);
        this.value  = ln;
        this.value /= 100000;
    }
    
    /** Get fractional part.
     * @return Fractional part
     */
    public int getFractional() {
        return this.fractional;
    }
    
    /** Get integral part.
     * @return Integral part 
     */
    public int getIntegral() {
        return this.integral;
    }
    
    /** Get floating point value.
     * @return Floating point value
     */
    public double getValue() {
        return this.value;
    }
    
    /** String representation of the arithmetic representation of the
     * number: integral + fractional/256.
     * @return String representation of the arithmetic representation
     */
    public String toArithmeticString() {
        return Integer.toString(this.integral)   + " + " +
               Integer.toString(this.fractional) +  "/"  +
               Integer.toString(DENOMINATOR);
    }
    
    /** String representation of the floating point value of the number.
     * @return String representation of the floating point value of number
     */
    @Override
    public String toString() {
        return Double.toString(this.value);
    }
}
