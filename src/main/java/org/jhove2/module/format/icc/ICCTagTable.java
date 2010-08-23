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

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;

/** ICC tag table.  See ICC.1:2004-10, \u00a7 7.3.
 * 
 * @author slabrams
 */
public class ICCTagTable
        extends AbstractReportable
{
    /** Tag count. */
    protected long count;
    
    /** Tag table validity. */
    protected Validity isValid;
    
    /** Tags. */
    protected List<ICCTag> tags;
    
    /** Instantiate a new <code>ICCTagTable</code>.
     */
    public ICCTagTable()
    {
        super();
        
        this.isValid = Validity.Undetermined;
        this.tags    = new ArrayList<ICCTag>();
    }
    
    /** Parse an ICC tag table.
     * @param jhove2 JHOVE2 framework
     * @param input  ICC input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    public long parse(JHOVE2 jhove2, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        this.isValid = Validity.True;
        
        this.count = input.readUnsignedInt();
        for (int i=0; i<this.count; i++) {
            ICCTag tag = new ICCTag();
            consumed += tag.parse(jhove2, input);
            
            this.tags.add(tag);
        }
        
        return consumed;
    }

    /** Get tag count.
     * @return Tag count
     */
    @ReportableProperty(order=1, value="Tag count.",
            ref="ICC.1:2004-10, \u00a7 7.3.2")
    public long getCount() {
        return this.count;
    }
    
    /** Get tags.
     * @return Tags
     */
    @ReportableProperty(order=2, value="Tags.",
            ref="ICC.1:2004-10, \u00a7 7.3")
    public List<ICCTag> getTags() {
        return this.tags;
    }

    /** Get validity.
     * @return Validity
     */
    @ReportableProperty(order=3, value="Tag table validity",
            ref="ICC.1:2004-10, \u00a7 7.3")
    public Validity isValid()
    {
         return this.isValid;
    }
}
