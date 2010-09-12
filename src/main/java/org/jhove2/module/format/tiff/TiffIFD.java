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

import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.tiff.type.Long;
import org.jhove2.module.format.tiff.type.LongArray;
import org.jhove2.module.format.tiff.type.Short;
import org.jhove2.module.format.tiff.type.ShortArray;

/**
 * @author mstrong
 *
 */
public class TiffIFD 
extends IFD 
{    
    /** Partial list of Tiff Tags */
    public static final int
    ARTIST = 315,
    BITSPERSAMPLE = 258,
    CELLLENGTH = 265,
    CLIPPATH =343,
    COLORMAP = 320,
    COMPRESSION = 259,
    DATETIME = 306,
    DOTRANGE = 336,
    EXTRASAMPLES = 338,
    ICCPROFILE = 34675,
    IMAGEWIDTH = 256,
    IMAGELENGTH = 257,
    JPEGPROC = 512,
    ORIENTATION = 274,
    NEWSUBFILETYPE = 254,
    PLANARCONFIGURATION = 284,
    PHOTMETRIC_INTERPRETATION = 262,
    SAMPLESPERPIXEL = 277,
    STRIPBYTECOUNTS = 279,
    STRIPOFFSETS = 273,
    THRESHHOLDING = 263,
    TILEBYTECOUNTS = 325, 
    TILELENGTH = 323,
    TILEWIDTH = 322,
    TILEOFFSETS = 324,
    XCLIPPATHUNITS = 344;

    /** compression tag value */
    private long compression;

    /* dot range tag value */
    private int[] dotRange;

    /* dot range out of range message */
    private Message dotRangeOutofRangeMessage;
    
    /** flag indicating if Photometric Interpreation is present */
    boolean hasPhotometricInterpretation = false;

    /** indicates if TileByteCounts tag present */
    private boolean hasTileByteCounts;

    /** indicates if TileLength tag present */
    private boolean hasTileLength;

    /** indicates if TileOffsets tag present */
    private boolean hasTileOffsets;
    
    /** indicates if TileWidth tag present */
    private boolean hasTileWidth;

    /** image length tag value */
    private long imageLength;

    /** image width tag value */
    private long imageWidth;

    /** Missing required tag message. */
    protected List<Message> missingRequiredTagMessages;

    /** photometric interpretation value */
    private int photometricInterpretation;

    /** strip byte counts value */
    long[] stripByteCounts;

    /** strip offsets value */
    private long[] stripOffsets;

    /** tile byte counts tag value */
    private long[] tileByteCounts;

    /** tile length tag value */
    private long tileLength;

    /** tile offsets tag value */
    private long [] tileOffsets;

    /** tiles and strips are both defined message */
    private Message tilesAndStripsDefinedMessage;

    /** neither tile and strips not defined message */
    private Message tilesAndStripsNotDefinedMessage;

    /** tileWidth tag value */
    private long tileWidth;

    /** strip byte counts tag not defined message */
    private Message stripByteCountsNotDefinedMessage;

    /** strip offsets tag not defined message */
    private Message stripOffsetsNotDefinedMessage;

    /** Planar configuration tag value */
    private int planarConfiguration;

    /** samples per pixel tag value */
    private int samplesPerPixel;

    /** strip lengths inconsisten message */
    private Message StripsLengthInconsistentMessage;

    /** invalid strip offset message */
    private Message invalidStripOffsetMessage;

    /** tile byte counts not defined message */
    private Message tileByteCountsNotDefinedMessage;

    /** tile offsets not defined message */
    private Message tileOffsetsNotDefinedMessage;

    /** tile width not defined message */
    private Message tileWidthNotDefinedMessage;

    /** tile length not defined message */
    private Message tileLengthNotDefinedMessage;

    /** tile offset values are insufficient message */
    private Message tileOffsetValuesInsufficientMessage;

    /** tile byte counts values insufficient message */
    private Message tileByteCountsValuesInsufficientMessage;

    /** transparency mask value is inconsistent message */
    private Message transparencyMaskValueInconsistentMessage;

    /** Bits Per Sample Invalid for Transparency Mask Message */
    private Message BPSInvalidForTransparencyMaskMessage;

    /** photometric interpretation Samples Per Pixel Less Than 1 Message */
    private Message photometricInterpretationSppLT1InvalidMessage;

    /** photometric interpretation samples per pixel less than 3 Message */
    private Message photometricInterpretationSppLT3InvalidMessage;

    /** color map bit code value */
    private int[] colorMapBitCode;
    
    /** color map red value */
    private int[] colorMapRed;

    /** color map green value */
    private int[] colorMapGreen;

    /** color map blue value */
    private int[] colorMapBlue;
    
    /** color map not defined for pallete color message */
    private Message colorMapNotDefinedForPalleteColorMessage;

    /** Samples Per Pixel must equal one for Pallete color message */
    private Message sppMustEqualOneForPalleteColorMessage;

    /** newSubfileType tag value */
    private long newSubfileType;

    /** bit per sample tag value */
    private int[] bitsPerSample;

    /** colorMap tag value */
    private int[] colorMap;

    /** insufficient color map values for Pallete color message */
    private Message insufficientColorMapValuesForPalleteColorMessage;

    /** color map not defined for pallete folor message */
    private Message colorMapNotDefinedForPalletteColorMessage;

    /** cell length should not be present message */
    private Message cellLengthShouldNotBePresentMessage;

    /** Samples per pixel and extral samples value are invalid message */
    private Message sppExtraSamplesValueInvalidMessage;

    /** bits per sample value invalid for CIE L*a*b* message */
    private Message bpsValueInvalidforCIELabMessage;

    /** XCLipPathUnits not defined when ClipPaths defined message */
    private Message xClipPathUnitsNotDefinedMessage;


    /** Instantiate a <code>TiffIFD</code> object
     *  represents a Tiff IFD 
     */
    public TiffIFD() {
        super();

        this.isValid = Validity.Undetermined;
        this.missingRequiredTagMessages = new ArrayList<Message>();
    }


    public Validity validate(JHOVE2 jhove2, Source source) throws JHOVE2Exception
    {
        IFDEntry entry = null;

        boolean hasImageLength = false;
        boolean hasImageWidth = false;
        boolean hasStripByteCounts = false;
        boolean hasStripOffsets = false;

        /* Validate The ImageLength (tag 257), ImageWidth (256), and PhotometricInterpretation (262) tags are defined */
        if ((entry = entries.get(IMAGELENGTH)) != null) {
            this.imageLength = ((Long) entry.getValue()).getValue();
            hasImageLength = true;
        }
        else {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"Image Length"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.TiffIFD.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);

        }
        if ((entry = entries.get(IMAGEWIDTH)) != null) {
            this.imageWidth = ((Long) entry.getValue()).getValue();
            hasImageWidth = true;
        }
        else {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"Image Width"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.TiffIFD.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }
        if ((entry = entries.get(PHOTMETRIC_INTERPRETATION)) != null) {
            this.photometricInterpretation = ((Short) entry.getValue()).getValue();
            hasPhotometricInterpretation = true;
        }
        else {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"PhotometricInterpretation"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.TiffIFD.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }


        /* Validate If version 4.0 or 5.0 then StripByteCounts (279) and StripOffsets (273) are defined; 
         * if version 6.0 then either all of StripByteCounts and StripOffsets or 
         * TileByteCounts (325), TileLength (323), TileOffsets (324), and TileWidth (322) are defined
         */
        if ((entry = entries.get(STRIPBYTECOUNTS)) != null) {
            this.stripByteCounts = ((LongArray) entry.getValue()).getLongArrayValue();
            hasStripByteCounts = true;
        }
        if ((entry = entries.get(STRIPBYTECOUNTS)) != null) {
            this.stripOffsets = ((LongArray) entry.getValue()).getLongArrayValue();
            hasStripOffsets = true;
        }

        boolean hasStripsDefined = (hasStripByteCounts || hasStripOffsets);

        if (hasStripsDefined) {
            if (!hasStripByteCounts) {
                this.isValid = Validity.False;
                this.stripByteCountsNotDefinedMessage = new Message(Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.tiff.TiffIFD.StripByteCountsNotDefinedMessage",
                        jhove2.getConfigInfo());   
            }
            else if (!hasStripOffsets){
                this.isValid = Validity.False;
                this.stripOffsetsNotDefinedMessage = new Message(Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.tiff.TiffIFD.StripOffsetsNotDefinedMessage",
                        jhove2.getConfigInfo());   

            }
            int length = stripOffsets.length;
            if (stripByteCounts.length != length) {
                this.isValid = Validity.False;
                Object[] args = new Object[]{stripByteCounts.length, length};
                this.StripsLengthInconsistentMessage = new Message(Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.tiff.TiffIFD.StripsLengthInconsistentMessage",
                        args, jhove2.getConfigInfo());                   
            }

            long fileLength = source.getInput().getSize();
            for (int i=0; i<length; i++) {
                long offset = stripOffsets[i];
                long count = stripByteCounts[i];
                if (offset+count > fileLength) {
                    this.isValid = Validity.False;
                    this.invalidStripOffsetMessage = new Message(Severity.ERROR, Context.OBJECT,
                            "org.jhove2.module.format.tiff.TiffIFD.InvalidStripOffsetMessage",
                            jhove2.getConfigInfo());                   
                }
            }
        }

        if ((entry = entries.get(TILEWIDTH)) != null) {
            this.tileWidth = ((Long) entry.getValue()).getValue();
            hasTileWidth = true;
        }
        if ((entry = entries.get(TILELENGTH)) != null) {
            this.tileLength = ((Long) entry.getValue()).getValue();
            hasTileLength = true;
        }
        if ((entry = entries.get(TILEOFFSETS)) != null) {
            this.tileOffsets = ((LongArray) entry.getValue()).getLongArrayValue();
            hasTileOffsets = true;
        }
        if ((entry = entries.get(TILEBYTECOUNTS)) != null) {
            this.tileByteCounts = ((LongArray) entry.getValue()).getLongArrayValue();
            hasTileByteCounts = true;
        }
        boolean hasTilesDefined = (hasTileWidth || hasTileLength || 
                hasTileOffsets || hasTileByteCounts);

        if (hasTilesDefined){
            if (!hasTileWidth) {
                this.isValid = Validity.False;
                this.tileWidthNotDefinedMessage = new Message(Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.tiff.TiffIFD.TileWidthNotDefinedMessage",
                        jhove2.getConfigInfo());                       
            }
            if (!hasTileLength) {
                this.isValid = Validity.False;
                this.tileLengthNotDefinedMessage = new Message(Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.tiff.TiffIFD.TileLengthNotDefinedMessage",
                        jhove2.getConfigInfo());                   
            }
            if (!hasTileByteCounts) {
                this.isValid = Validity.False;
                this.tileByteCountsNotDefinedMessage = new Message(Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.tiff.TiffIFD.TileByteCountsNotDefinedMessage",
                        jhove2.getConfigInfo());                   
            }
            if (!hasTileOffsets) {
                this.isValid = Validity.False;
                this.tileOffsetsNotDefinedMessage = new Message(Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.tiff.TiffIFD.TileOffsetsNotDefinedMessage",
                        jhove2.getConfigInfo());                   

            }
        }

        if (hasStripsDefined && hasTilesDefined) {
            this.isValid = Validity.False;
            this.tilesAndStripsDefinedMessage = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.TiffIFD.TilesAndStripsDefinedMessage",
                    jhove2.getConfigInfo());   
        }
        if (!hasStripsDefined && !hasTilesDefined) {
            this.isValid = Validity.False;
            this.tilesAndStripsNotDefinedMessage = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.TiffIFD.TilesAndStripsNotDefinedMessage",
                    jhove2.getConfigInfo());   
        }

        if (hasTilesDefined) {
            if ((entry = entries.get(PLANARCONFIGURATION)) != null) {
                this.planarConfiguration = ((Short) entry.getValue()).getValue();
            }

            if ((entry = entries.get(SAMPLESPERPIXEL)) != null) {
                this.samplesPerPixel = ((Short)entry.getValue()).getValue();
            }
            long tilesPerImage = ((this.imageWidth  + this.tileWidth  - 1)/this.tileWidth) *
            ((this.imageLength + this.tileLength - 1)/this.tileLength);
            if (planarConfiguration == 2) {
                long spp_tpi = samplesPerPixel* tilesPerImage;
                if (this.tileOffsets != null && this.tileOffsets.length < spp_tpi) {
                    this.isValid = Validity.False;
                    Object[] args = new Object[] {tileOffsets.length, spp_tpi};
                    this.tileOffsetValuesInsufficientMessage = new Message(Severity.ERROR, Context.OBJECT,
                            "org.jhove2.module.format.tiff.TiffIFD.TileOffsetValuesInsufficientMessage",
                            args, jhove2.getConfigInfo());   
                }
                if (this.tileByteCounts != null &&
                        this.tileByteCounts.length < spp_tpi) {
                    this.isValid = Validity.False;
                    Object[] args = new Object[] {tileOffsets.length, spp_tpi};
                    this.tileByteCountsValuesInsufficientMessage = new Message(Severity.ERROR, Context.OBJECT,
                            "org.jhove2.module.format.tiff.TiffIFD.TileByteCountsValuesInsufficientMessage",
                            args, jhove2.getConfigInfo());
                }
                else {
                    if (tileOffsets != null &&
                            tileOffsets.length < tilesPerImage) {
                        this.isValid = Validity.False;
                        Object[] args = new Object[] {tileOffsets.length, tilesPerImage};
                        this.tileByteCountsValuesInsufficientMessage = new Message(Severity.ERROR, Context.OBJECT,
                                "org.jhove2.module.format.tiff.TiffIFD.TileByteCountsValuesInsufficientMessage",
                                args, jhove2.getConfigInfo());
                    }
                    if (tileByteCounts != null &&
                            tileByteCounts.length < tilesPerImage) {
                        this.isValid = Validity.False;
                        Object[] args = new Object[] {tileOffsets.length, tilesPerImage};
                        this.tileByteCountsValuesInsufficientMessage = new Message(Severity.ERROR, Context.OBJECT,
                                "org.jhove2.module.format.tiff.TiffIFD.TileByteCountsValuesInsufficientMessage",
                                args, jhove2.getConfigInfo());
                    }
                }

            }
        }

        if (hasPhotometricInterpretation) {
            if ((entry = entries.get(SAMPLESPERPIXEL)) != null) {
                this.samplesPerPixel = ((Short)entry.getValue()).getValue();
            }
            if ((entry = this.entries.get(NEWSUBFILETYPE)) != null) {
                this.newSubfileType = ((Long) entry.getValue()).getValue();

                /* If PhotometricInterpretation = 4, then bit 2 of NewSubfileType (254) = 1, and vice versa */
                if ((photometricInterpretation == 4 && (newSubfileType & 4) == 0) || 
                        (photometricInterpretation != 4) && (newSubfileType & 4) != 0)  {
                    this.isValid = Validity.False;
                    this.transparencyMaskValueInconsistentMessage = new Message(Severity.ERROR, Context.OBJECT,
                            "org.jhove2.module.format.tiff.TiffIFD.TransparencyMaskValueInconsistentMessage",
                            jhove2.getConfigInfo());
                }
                /* If PhotometricInterpretation = 4, then SamplesPerPixel = 1 and BitsPerSample = 1 */
                if ((entry = this.entries.get(BITSPERSAMPLE)) != null) {
                    this.bitsPerSample = ((ShortArray) entry.getValue()).getShortArrayValue();
                    if (photometricInterpretation == 4) {
                        if (samplesPerPixel < 1 || bitsPerSample[0] != 1) {
                            this.isValid = Validity.False;
                            this.BPSInvalidForTransparencyMaskMessage = new Message(Severity.ERROR, Context.OBJECT,
                                    "org.jhove2.module.format.tiff.TiffIFD.BPSInvalidForTransparencyMask",
                                    jhove2.getConfigInfo());
                        }
                    }
                }
                /* If PhotometricInterpretation = 0,1,3, or 4, then SamplesPerPixel >= 1 */
                if (photometricInterpretation != 2 && photometricInterpretation <= 4 ) {
                    if (samplesPerPixel < 1) {
                        this.isValid = Validity.False;
                        this.photometricInterpretationSppLT1InvalidMessage = new Message(Severity.ERROR, Context.OBJECT,
                                "org.jhove2.module.format.tiff.TiffIFD.photometricInterpretationSppLT1InvalidMessage",
                                jhove2.getConfigInfo());                        
                    }
                }
                /*  If PhotometricInterpretation = 2,6, or 8, then SamplesPerPixel >= 3 */
                if (photometricInterpretation == 2 || photometricInterpretation == 6 || photometricInterpretation == 8) {
                    if (samplesPerPixel < 3) {
                        this.isValid = Validity.False;
                        this.photometricInterpretationSppLT3InvalidMessage = new Message(Severity.ERROR, Context.OBJECT,
                                "org.jhove2.module.format.tiff.TiffIFD.PhotometricInterpretationSppLT3InvalidMessage",
                                jhove2.getConfigInfo());                        

                    }
                }
                /* If PhotometricInterpretation = 3  (Pallette Color, 
                 * then ColorMap is defined with 
                 * 2BitsPerSample[0] + 2BitsPerSample[1] + 2BitsPerSample[2] values
                 */
                if (photometricInterpretation == 3) {
                    if ((entry = entries.get(COLORMAP)) != null) {
                        this.colorMap = ((ShortArray) entry.getValue()).getShortArrayValue();
                        this.colorMapBitCode = new int [colorMap.length];
                        this.colorMapRed     = new int [colorMap.length];
                        this.colorMapGreen   = new int [colorMap.length];
                        this.colorMapBlue    = new int [colorMap.length];
                        int len = colorMap.length/3;
                        int len2= 2*len;
                        for (int i=0; i<len; i++) {
                            colorMapBitCode[i] = i;
                            colorMapRed[i]     = colorMap[i];
                            colorMapGreen[i]   = colorMap[i + len];
                            colorMapBlue[i]    = colorMap[i + len2];
                        }
                        if (colorMapBitCode == null || colorMapRed  == null ||
                                colorMapGreen   == null || colorMapBlue == null) {
                            this.isValid = Validity.False;
                            this.colorMapNotDefinedForPalletteColorMessage = new Message (Severity.ERROR, Context.OBJECT,
                                    "org.jhove2.module.format.tiff.TiffIFD.ColorMapNotDefinedForPalletteColorMessage",
                                    jhove2.getConfigInfo());
                        }

                        if (samplesPerPixel != 1) {
                            this.isValid = Validity.False;
                            this.sppMustEqualOneForPalleteColorMessage = new Message (Severity.ERROR, Context.OBJECT,
                                    "org.jhove2.module.format.tiff.TiffIFD.SppMustEqualOneForPalleteColorMessage",
                                    jhove2.getConfigInfo());
                        }
                        len = (1<<bitsPerSample[0]);
                        if (colorMapBitCode.length < len) {
                            this.isValid = Validity.False;
                            this.insufficientColorMapValuesForPalleteColorMessage = new Message (Severity.ERROR, Context.OBJECT,
                                    "org.jhove2.module.format.tiff.TiffIFD.InsufficientColorMapValuesForPalleteColorMessage",
                                    jhove2.getConfigInfo());
                        }
                    }
                }

            }
            /* CIE L*a*b* or ICCLab Validation */
            if (photometricInterpretation == 8 || photometricInterpretation == 9) {

                int len = 0;
                /* SamplesPerPixel-ExtraSamples = 1 or 3 */
                if ((entry = entries.get(EXTRASAMPLES)) != null) {    
                    int[] extraSamples = ((ShortArray) entry.getValue()).getShortArrayValue();
                    len = extraSamples.length;

                    int in = samplesPerPixel - len;
                    if (in != 1 && in != 3) {
                        this.isValid = Validity.False;
                        this.sppExtraSamplesValueInvalidMessage = new Message (Severity.ERROR, Context.OBJECT,
                                "org.jhove2.module.format.tiff.TiffIFD.SppExtraSamplesValueInvalidMessage",
                                jhove2.getConfigInfo());
                    }
                }

                /* BitsPerSample = 8 or 16 */
                for (int i=0; i<bitsPerSample.length; i++) {
                    if (bitsPerSample[i] != 8 && bitsPerSample[i] != 16) {
                        this.isValid = Validity.False;
                        this.bpsValueInvalidforCIELabMessage = new Message (Severity.ERROR, Context.OBJECT,
                                "org.jhove2.module.format.tiff.TiffIFD.BpsValueInvalidforCIELabMessage",
                                jhove2.getConfigInfo());
                    }
                }
            }
        }   /* end of PhotmetricInterpretation Validation */

        /* If Compression Scheme = 6 (JPEG) then JPEGProc is defined */
        if ((entry = entries.get(COMPRESSION)) != null) {
            compression = ((Short) entry.getValue()).getValue(); 
            if (compression == 6) {                
                if ((entry = entries.get(JPEGPROC)) == null) {
                    this.isValid = Validity.False;
                    this.insufficientColorMapValuesForPalleteColorMessage = new Message (Severity.ERROR, Context.OBJECT,
                            "org.jhove2.module.format.tiff.TiffIFD.InsufficientColorMapValuesForPalleteColorMessage",
                            jhove2.getConfigInfo());
                }
            }
        }

        /* CellLength should only be present if Threshholding = 2 . */
        if ((entry = entries.get(THRESHHOLDING)) != null) {
            int threshholding = ((Short) entry.getValue()).getValue();
            if (threshholding != 2 && 
                    ((entry = entries.get(CELLLENGTH)) != null)) {
                this.isValid = Validity.False;
                this.cellLengthShouldNotBePresentMessage = new Message (Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.tiff.TiffIFD.CellLengthShouldNotBePresentMessage",
                        jhove2.getConfigInfo());
            }
        }

        /* The values for DotRange (336) are in the range [0, (2BitsPerSample[i])-1] */
        if ((entry = entries.get(DOTRANGE)) != null) {
            this.dotRange = ((ShortArray) entry.getValue()).getShortArrayValue();
            if (dotRange != null && bitsPerSample != null) {
                int sampleMax = 1 << bitsPerSample[0];
                if (dotRange.length < 2 || dotRange[0] >= sampleMax ||
                        dotRange[1] >= sampleMax) {
                    this.isValid = Validity.False;
                    this.dotRangeOutofRangeMessage = new Message (Severity.ERROR, Context.OBJECT,
                            "org.jhove2.module.format.tiff.TiffIFD.dotRangeOutofRangeMessage",
                            jhove2.getConfigInfo());
                }
            }
        }


        /* If ClipPath (343) is defined, then XClipPathUnits (344) is defined */
        if ((entry = entries.get(CLIPPATH)) != null) {
            if ((entry = entries.get(XCLIPPATHUNITS)) == null) {
                this.isValid = Validity.False;
                this.xClipPathUnitsNotDefinedMessage = new Message (Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.tiff.TiffIFD.xClipPathUnitsNotDefinedMessage",
                        jhove2.getConfigInfo());
            }
        }

        if (this.isValid != Validity.False)
            this.isValid = Validity.True;
        return isValid;
    }
}



