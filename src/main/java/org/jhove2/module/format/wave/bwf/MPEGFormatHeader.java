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

package org.jhove2.module.format.wave.bwf;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.annotation.ReportableProperty.PropertyType;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Parser;
import org.jhove2.module.format.wave.bwf.field.MPEGEmphasis;
import org.jhove2.module.format.wave.bwf.field.MPEGFlag;
import org.jhove2.module.format.wave.bwf.field.MPEGLayer;
import org.jhove2.module.format.wave.bwf.field.MPEGMode;
import org.jhove2.module.format.wave.bwf.field.MPEGModeExtension;

import com.sleepycat.persist.model.Persistent;

/** WAVE format MPEG-1 header.
 * 
 * @author slabrams
 */
@Persistent
public class MPEGFormatHeader
    extends AbstractReportable
    implements Parser
{
    /** Bit rate, in bits/second. */
    protected long bitRate;
    
    /** Decoder de-emphasis in raw form. */
    protected int emphasis;
    
    /** Decoder de-emphasis in descriptive form. */
    protected String emphasis_d;
    
    /** Flags in raw form. */
    protected int flags;
    
    /** Flags in descriptive form. */
    protected List<String> flags_d;
 
    /** MPEG audio layer(s) in raw form. */
    protected int layers;
    
    /** MPEG audio layer(s) in descriptive form. */
    protected List<String> layers_d;
    
    /** Stream mode in raw form. */
    protected int modes;
    
    /** Stream mode(s) in descriptive form. */
    protected List<String> modes_d;
    
    /** Extra parameters for joint-stereo coding in raw form. */
    protected int modeExt;
    
    /** Extra parameters for joint-stereo coding in descriptive form. */
    protected List<String> modeExt_d;
    
    /** Presentation time stamp (PTS) most significant bit (MSB). */
    protected long ptsHigh;
    
    /** Presentation time stamp (PTS) 32 least significant bits (LSB). */
    protected long ptsLow;
 
    /** Instantiate a new <code>MPEGFormatHeader</code>. */
    public MPEGFormatHeader() {
        super();
        
        this.flags_d    = new ArrayList<String>();
        this.layers_d   = new ArrayList<String>();
        this.modes_d    = new ArrayList<String>();
        this.modeExt_d  = new ArrayList<String>();
    }
    
    /** 
     * Parse a WAVE format MPEG header.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            WAVE source unit
     * @param input  WAVE source input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        
        /* MPEG audio layer. */
        this.layers = input.readUnsignedShort();
        Set<MPEGLayer> layers = MPEGLayer.getLayers(jhove2);
        Iterator<MPEGLayer> iter1 = layers.iterator();
        while (iter1.hasNext()) {
            MPEGLayer layer = iter1.next();
            int lay = layer.getLayer();
            if ((this.layers & lay) != 0) {
                this.layers_d.add(layer.getDescription());
            }
        }
        consumed += 2;
        
        /* Bit rate. */
        this.bitRate = input.readUnsignedInt();
        consumed += 4;
        
        /* Stream mode. */
        this.modes = input.readUnsignedShort();
        Set<MPEGMode> modes = MPEGMode.getModes(jhove2);
        Iterator<MPEGMode> iter2 = modes.iterator();
        while (iter2.hasNext()) {
            MPEGMode mode = iter2.next();
            int mod = mode.getMode();
            if ((this.modes & mod) != 0) {
                this.modes_d.add(mode.getDescription());
            }
        }
        consumed += 2;
        
        /* Extra parameters for joint-stereo coding. */
        this.modeExt = input.readUnsignedShort();
        Set<MPEGModeExtension> extensions = MPEGModeExtension.getModeExtensions(jhove2);
        Iterator<MPEGModeExtension> iter3 = extensions.iterator();
        while (iter3.hasNext()) {
            MPEGModeExtension extension = iter3.next();
            int ext = extension.getModeExtension();
            if ((this.modeExt & ext) != 0) {
                this.modeExt_d.add(extension.getDescription());
            }
        }
        consumed += 2;
        
        /* Decoding de-emphasis. */
        this.emphasis = input.readUnsignedShort();
        MPEGEmphasis emphasis = MPEGEmphasis.getMPEGEmphasis(this.emphasis, jhove2);
        if (emphasis != null) {
            this.emphasis_d = emphasis.getDescription();
        }
        consumed += 2;
        
        /* Flags. */
        this.flags = input.readUnsignedShort();
        Set<MPEGFlag> flags = MPEGFlag.getFlags(jhove2);
        Iterator<MPEGFlag> iter4 = flags.iterator();
        while (iter4.hasNext()) {
            MPEGFlag flag = iter4.next();
            int flg = flag.getFlag();
            if ((this.flags & flg) != 0) {
                this.flags_d.add(flag.getDescription());
            }
        }
        consumed += 2;
        
        /* PTS 32 LSBs. */
        this.ptsLow = input.readUnsignedInt();
        consumed += 4;
        
        /* PTS MSB. */
        this.ptsHigh = input.readUnsignedInt();
        consumed += 4;
        
        return consumed;
    }
    
    /** Get bit rate, in bits/second.
     * @return Bit rate
     */
    @ReportableProperty(order=3, value="Bitrate, in bits/second.")
    public long getBitRate() {
        return this.bitRate;
    }
    
    /** Get decoder de-emphasis in descriptive form.
     * @return Decoder de-emphasis
     */
    @ReportableProperty(order=8, value="Decoder de-emphasis in descriptive form.",
            type=PropertyType.Descriptive)
    public String getEmphasis_descriptive() {
        return this.emphasis_d;
    }
    
    /** Get decoder de-emphasis in raw form.
     * @return Decoder de-emphasis
     */
    @ReportableProperty(order=7, value="Decoder de-emphasis in raw form.",
            type=PropertyType.Raw)
    public int getEmphasis_raw() {
        return this.emphasis;
    }
    
    /** Get flags in descriptive form.
     * @return Flags
     */
    @ReportableProperty(order=10, value="Flags in descriptive form.",
            type=PropertyType.Descriptive)
    public List<String> getFlags() {
        return this.flags_d;
    }
    
    /** Get flags in raw form. 
     * @return Flags
     */
    @ReportableProperty(order=9, value="Flags in raw form.",
            type=PropertyType.Raw)
    public int getFlags_raw() {
        return this.flags;
    }
    
    /** Get extra parameters for joint-stereo coding in descriptive form.
     * @return Extra parameters for joint-stereo coding
     */
    @ReportableProperty(order=6, value="Extra parameters for joint-stereo coding in descriptive form.",
            type=PropertyType.Descriptive)
    public List<String> getJointStereoCodingParameters() {
        return this.modeExt_d;
    }
    
    /** Get extra parameters for joint-stereo coding in raw form.
     * @return Extra parameters for joint-stereo coding
     */
    @ReportableProperty(order=6, value="Extra parameters for joint-stereo coding in raw form.",
            type=PropertyType.Raw)
    public int getJointStereoCodingParameters_raw() {
        return this.modeExt;
    }
    
    /** Get presentation time stamp (PTS) most significant bis (MSB).
     * @return PTS 32 LSB
     */
    @ReportableProperty(order=12, value="Presentation time stamp (PTS) most significant bit (MSB).")
    public long getPTSHigh() {
        return this.ptsHigh;
    }
    
    /** Get presentation time stamp (PTS) 32 least significant bits (LSB).
     * @return PTS 32 LSB
     */
    @ReportableProperty(order=11, value="Presentation time stamp (PTS) 32 least significant bits (LSB).")
    public long getPTSLow() {
        return this.ptsLow;
    }
    
    /** Get MPEG audio layers in descriptive form.
     * @return MPEG audio layers in descriptive form
     */
    @ReportableProperty(order=2, value="MPEG audio layers in descriptive form.",
            type=PropertyType.Descriptive)
    public List<String> getMPEGAudioLayers() {
        return this.layers_d;
    }
    
    /** Get MPEG audio layers in raw form.
     * @return MPEG audio layers
     */
    @ReportableProperty(order=1, value="MPEG audio layers in raw form.",
            type=PropertyType.Raw)
    public int getMPEGAudioLayers_raw() {
        return this.layers;
    }
    
    /** Get stream modes in descriptive form.
     * @return Stream modes
     */
    @ReportableProperty(order=5, value="Stream modes in descriptive form.",
            type=PropertyType.Descriptive)
    public List<String> getStreamModes() {
        return this.modes_d;
    }
    
    /** Get stream modes in raw form.
     * @return Stream modes
     */
    @ReportableProperty(order=4, value="Stream modes in raw form.",
            type=PropertyType.Raw)
    public int getStreamModes_raw() {
        return this.modes;
    }
 
}
