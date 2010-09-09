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
package org.jhove2.module.format.tiff;

import org.jhove2.core.JHOVE2;
import org.jhove2.module.format.Validator.Validity;


/**
 * @author mstrong
 *
 */
public class TiffIFD 
extends IFD
implements Comparable {
    
    public long compression;

    
    public static final int
        DATETIME = 306,
        COMPRESSION = 259,
        ORIENTATION = 274,
        PHOTMETRIC_INTERPRETATION = 262;
    
    public TiffIFD() {
        super();
    }
    
    public Validity validate(JHOVE2 jhove2){

        /*
        #  The ImageLength (tag 257), ImageWidth (256), and PhotometricInterpretation (262) tags are defined
        # If version 4.0 or 5.0 then StripByteCounts (279) and StripOffsets (273) are defined; if version 6.0 then either all of StripByteCounts and StripOffsets or TileByteCounts (325), TileLength (323), TileOffsets (324), and TileWidth (322) are defined
        # If PhotometricInterpretation = 4, then bit 2 of NewSubfileType (254) = 1, and vice versa
        # If PhotometricInterpretation = 4, then SamplesPerPixel = 1 and BitsPerSample = 1
        # If PhotometricInterpretation = 0,1,3, or 4, then SamplesPerPixel = 1
        # If PhotometricInterpretation = 2,6, or 8, then SamplesPerPixel = 3
        # If PhotometricInterpretation = 3, then ColorMap is defined with 2BitsPerSample[0] + 2BitsPerSample[1] + 2BitsPerSample[2] values
        # The values for DotRange (336) are in the range [0, (2BitsPerSample[i])-1]
        # CellLength (265) defined only if Threshholding (263) = 2
        # If PhotometricInterpretation = 6, then JPEGProc is defined
        # If PhotometricInterpretation = 8 or 9, then BitsPerSample = 8 or 16 and SamplesPerPixel-ExtraSamples = 1 or 3
        # If ClipPath (343) is defined, then XClipPathUnits (344) is defined
        # TileWidth (322) and TileLength (323) values are integral multiples of 16
        # DateTime (306) tag is properly formatted: "YYYY:MM:DD HH:MM:SS"
        */ 
        
        return isValid;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

}
