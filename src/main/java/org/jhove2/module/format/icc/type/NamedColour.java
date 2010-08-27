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

import java.util.List;

/** ICC named colour, as defined in ICC.1:2004-10, Table 45.
 * 
 * @author slabrams
 */
public class NamedColour
{
    /** Device coordinates. */
    protected List<Integer> deviceCoordinates;
    
    /** Profile Connection Space (PCS) coordinates. */
    protected List<Integer> pcsCoordinates;
    
    /** Root name. */
    protected String rootName;
    
    /** Instantiate a new <code>NamedColour</code>.
     * @param root   Root name
     * @param pcs    Profile Connection Space (PCS) coordinates
     * @param device Device coordinate
     */
    public NamedColour(String root, List<Integer> pcs, List<Integer> device) {
        this.rootName          = root;
        this.pcsCoordinates    = pcs;
        this.deviceCoordinates = device;
    }
    
    /** Get device coordinates.
     * @return Device coordinates
     */
    public List<Integer> getDeviceCoordinates() {
        return this.deviceCoordinates;
    }
    
    /** Get Profile Connection Space (PCS) coordinates.
     * @return PCS coordinates
     */
    public List<Integer> getPCSCoordinates() {
        return this.pcsCoordinates;
    }
    
    /** Get root name.
     * @return root name
     */
    public String getRootName() {
        return this.rootName;
    }
}
