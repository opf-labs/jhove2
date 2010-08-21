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

package org.jhove2.module.format.icc;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/** ICC tag.  See ICC.1:2004-10, \u00a7 7.3.1.
 * 
 * @author slabrams
 */
public class ICCTag
        extends AbstractReportable
{
    /** Tag offset. */
    protected long offset;
    
    /** Tag signature. */
    protected String signature;
    
    /** Tag size. */
    protected long size;
    
    /** Instantiate a new <code>ICCTag</code.
     */
    public ICCTag() {
        super();
    }
    
    /** Get tag offset.
     * @return Tag offset
     */
    @ReportableProperty(order=2, value="Tag offset.", ref="ICC.1:2004-10, \u00a7 7.3.1")
    public long getOffset() {
        return this.offset;
    }
    
    /** Get tag signature.
     * @return Tag signature
     */
    @ReportableProperty(order=1, value="Tag signature.", ref="ICC.1:2004-10, \u00a7 7.3.1")
    public String getSignature() {
        return this.signature;
    }
    
    /** Get tag size.
     * @return Tag size
     */
    @ReportableProperty(order=3, value="Tag size.", ref="ICC.1:2004-10, \u00a7 7.3.1")
    public long getSize() {
        return this.size;
    }
}
