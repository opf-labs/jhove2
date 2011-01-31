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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.icc.field.DeviceAttribute;
import org.jhove2.module.format.icc.field.TechnologySignature;

import com.sleepycat.persist.model.Persistent;

/** ICC profile description, as defined in ICC.1:2004-10, Table 49.
 * 
 * @author slabrams
 */
@Persistent
public class ProfileDescription
    extends AbstractReportable
{
    /** Device attributes in raw form. */
    protected long deviceAttributes;
    
    /** Device attributes in descriptive form. */
    protected List<String> deviceAttributes_d;
    
    /** Device manufacturer signature. */
    protected String deviceManufacturer;
    
    /** Device model signature. */
    protected String deviceModel;
    
    /** Device technology signature in raw form. */
    protected String technology;
    
    /** Device technology signature in descriptive form. */
    protected String technology_d;
    
    private ProfileDescription(){
    	super();
    }
    /** Instantiate a new <code>ProfileDescription</code>.
     * @param manufacturer Device manufacturer
     * @param model        Device model
     * @param attributess  Device attributes
     * @param technology   Device technology signature
     * @param jhove2       JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    public ProfileDescription(String manufacturer, String model, long attributes,
                              String technology, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        this();
        
        this.deviceManufacturer = manufacturer;
        this.deviceModel        = model;
        this.deviceAttributes   = attributes;
        this.technology         = technology;
        
        /* Expand device attributes into descriptive form. */
        this.deviceAttributes_d = new ArrayList<String>();
        
        Set<DeviceAttribute> attrs = DeviceAttribute.getDeviceAttributes(jhove2);
        Iterator<DeviceAttribute> daIter = attrs.iterator();
        while (daIter.hasNext()) {
            DeviceAttribute attr = daIter.next();
            int bitPosition = attr.getPosition();
            long mask = 1L << bitPosition;
            if ((this.deviceAttributes & mask) == 0L) {
                this.deviceAttributes_d.add(attr.getNegativeValue());
            }
            else {
                this.deviceAttributes_d.add(attr.getPositiveValue());
            }
        }
        
        /** Expand technology signature into descriptive form. */
        TechnologySignature tech =
            TechnologySignature.getTechnology(technology, jhove2); 
        if (tech != null) {
            this.technology_d = tech.getTechnology();
        }
    }
  
    /** Get device attributes in raw form.
     * @return Device attributes in raw form
     */
    @ReportableProperty(order=1, value="Device attributes in raw form.",
    		ref="ICC.1:2004-10, 7.2.14")
    public long getDeviceAttributes() {
        return this.deviceAttributes;
    }
    
    /** Get device manufacturer.
     * @return Device manufacturer
     */
    @ReportableProperty(order=2, value="Device manufacturer.",
            ref="ICC.1:2004-10, 7.2.12")
    public String getDeviceManufacturer() {
        return this.deviceManufacturer;
    }
    
    /** Get device model.
     * @return Device model
     */
    @ReportableProperty(order=3, value="Device model.",
            ref="ICC.1:2004-10, 7.2.13")
    public String getDeviceModel() {
        return this.deviceModel;
    }
    
    /** Get device technology signature in descriptive form.
     * @return Device technology signature
     */
    @ReportableProperty(order=5, value="Device technology signature in descriptive form.",
            ref="ICC.1:2004-10, 9.2.35")
    public String getTechnologySignature_descriptive() {
        return this.technology_d;
    }
    /** Get device technology signature in raw form.
     * @return Device technology signature
     */
    @ReportableProperty(order=4, value="Device technology signature in raw form.",
            ref="ICC.1:2004-10, 9.2.35")
    public String getTechnologySignature_raw() {
        return this.technology;
    }
}
