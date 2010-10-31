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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.tiff.type.Long;
import org.jhove2.module.format.tiff.type.Byte;
import org.jhove2.module.format.tiff.type.LongArray;
import org.jhove2.module.format.tiff.type.Rational;
import org.jhove2.module.format.tiff.type.Short;
import org.jhove2.module.format.tiff.type.ShortArray;

/**
 * @author mstrong
 *
 */
public class TiffIFD 
extends IFD 
{    
    public static final int NULL = -1;
    
    /** Partial list of Tiff Tags */
    public static final int
    ARTIST = 315,
    BACKGROUNDCOLORINDICATOR = 34024,
    BACKGROUNDCOLORVALUE = 34026,
    BITSPERRUNLENGTH = 34020,
    BITSPEREXTENDEDRUNLENGTH = 34021,
    BITSPERSAMPLE = 258,
    CELLLENGTH = 265,
    CFAREPEATPATTTERNDIM = 33421,
    CFAPATTERN = 33422,
    CLIPPATH =343,
    CMYKEQUIVALENT = 34032,
    COLORMAP = 320,
    COLORSEQUENCE = 34017,
    COLORTABLE = 34022,
    COPYRIGHT = 33432,
    COMPRESSION = 259,
    DATETIME = 306,
    DATETIMEORIGINAL = 36867,
    DOCUMENTNAME = 269,
    DOTRANGE = 336,
    EXTRASAMPLES = 338,
    FILLORDER = 266,
    GEOKEYDIRECTORY = 34735,
    HALFTONEHINTS = 321, 
    HOSTCOMPUTER = 316,
    ICCPROFILE = 34675,
    IT8HEADER = 34018,
    IMAGEWIDTH = 256,
    IMAGECOLORINDICATOR = 34023,
    IMAGECOLORVALUE = 34025,
    IMAGEDESCRIPTION = 270,
    IMAGELENGTH = 257,
    INDEXED = 346,
    INKNAMES = 333,
    INKSET = 332,
    JPEGACTABLES = 521,
    JPEGDCTABLES = 520,
    JPEGINTERCHANGEFORMAT = 513,
    JPEGINTERCHANGEFORMATLENGTH = 514,
    JPEGLOSSLESSPREDICTORS = 517,
    JPEGPOINTTRANSFORMS = 518,
    JPEGPROC = 512,
    JPEG_RESTART_INTERVAL = 515,
    JPEG_QTABLES = 519,
    MAKE = 271, 
    MODEL = 272,
    MODELTIEPOINT = 33922,
    MODELTRANSFORMATION = 34264,
    NUMBEROFINKS = 334,
    ORIENTATION = 274,
    PAGENAME = 285,
    NEWSUBFILETYPE = 254,
    PLANARCONFIGURATION = 284,
    PREDICTOR = 317,
    PRIMARY_CHROMATACITIES = 319,
    REFERENCEBLACKWHITE = 532,
    PHOTMETRIC_INTERPRETATION = 262,
    PIXELINTENSITYRANGE = 34027,
    RASTERPADDING = 34019,
    RESOLUTIONUNIT = 296,
    ROWSPERSTRIP = 278,
    SAMPLEFORMAT = 339,
    SAMPLESPERPIXEL = 277,
    SENSINGMETHOD = 37399,
    SITE = 34016,
    SMINSAMPLEVALUE = 340,
    SMAXSAMPLEVALUE = 341,
    SOFTWARE = 305,
    STRIPBYTECOUNTS = 279,
    STRIPOFFSETS = 273,
    TARGETPRINTER = 337,
    THRESHHOLDING = 263,
    TIFFEPSTANDARDID = 37398, 
    TILEBYTECOUNTS = 325, 
    TILELENGTH = 323,
    TILEWIDTH = 322,
    TILEOFFSETS = 324,
    TRANSFERRANGE = 342,
    TRANSPARENCYINDICATOR = 34028,
    TRAPINDICATOR = 34031,
    WHITEPOINT = 318,
    XCLIPPATHUNITS = 344,
    XMP = 700,
    XRESOLUTION = 282,
    YCBCRCOEFFICIENTS = 529,
    YCBCRSUBSAMPLING = 530,
    YCBCRPOSITIONING = 531,
    YRESOLUTION = 283;

    /** compression tag value */
    private int compression = NULL;

    /* dot range tag value */
    private int[] dotRange = null;

    /* dot range out of range message */
    private Message dotRangeOutofRangeMessage;
    
    /** flag indicating if Photometric Interpreation is present */
    boolean hasPhotometricInterpretation = false;

    /** flag indicating if strip byte counts tag is present */
    private boolean hasStripByteCounts = false;

    /** flag indicating if strip offsets are present */
    public boolean hasStripOffsets = false;

    /** indicates if TileByteCounts tag present */
    private boolean hasTileByteCounts;

    /** indicates if TileLength tag present */
    private boolean hasTileLength;

    /** indicates if TileOffsets tag present */
    private boolean hasTileOffsets;
    
    /** indicates if TileWidth tag present */
    private boolean hasTileWidth;

    /** image length tag value */
    private long imageLength = NULL;

    /** image width tag value */
    private long imageWidth = NULL;

    /** Missing required tag message. */
    protected List<Message> missingRequiredTagMessages;

    /** photometric interpretation value */
    private int photometricInterpretation = NULL;

    /** strip byte counts value */
    long[] stripByteCounts = null;

    /** strip offsets value */
    private long[] stripOffsets = null;

    /** tile byte counts tag value */
    private long[] tileByteCounts = null;

    /** tile length tag value */
    private long tileLength = NULL;

    /** tile offsets tag value */
    private long [] tileOffsets = null;

    /** tiles and strips are both defined message */
    private Message tilesAndStripsDefinedMessage;

    /** neither tile and strips not defined message */
    private Message tilesAndStripsNotDefinedMessage;

    /** tileWidth tag value */
    private long tileWidth = NULL;

    /** strip byte counts tag not defined message */
    private Message stripByteCountsNotDefinedMessage;

    /** strip offsets tag not defined message */
    private Message stripOffsetsNotDefinedMessage;

    /** Planar configuration tag value */
    private int planarConfiguration = NULL;

    /** samples per pixel tag value */
    private int samplesPerPixel = NULL;

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
    private int[] colorMapBitCode = null;
    
    /** color map red value */
    private int[] colorMapRed = null;

    /** color map green value */
    private int[] colorMapGreen = null;

    /** color map blue value */
    private int[] colorMapBlue = null;
    
    /** color map not defined for pallete color message */
    private Message colorMapNotDefinedForPalleteColorMessage;

    /** Samples Per Pixel must equal one for Pallete color message */
    private Message sppMustEqualOneForPalleteColorMessage;

    /** newSubfileType tag value */
    private long newSubfileType = NULL;

    /** bit per sample tag value */
    private int[] bitsPerSample = null;

    /** colorMap tag value */
    private int[] colorMap = null;

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

    private int resolutionUnit = NULL;

    private long xResolution = NULL;

    private Rational yResolution = null;

    private int orientation = NULL;

    private int imageColorIndicator = NULL;

    private int backgroundColorIndicator = NULL;

    private int indexed = NULL;

    private int fillOrder = NULL;

    private boolean hasImageLength = false;

    private boolean hasImageWidth = false;

    private boolean hasXResolution = false;

    private boolean hasYResolution = false;

    private boolean hasRowsPerStrip = false;

    private long rowsPerStrip = NULL;


    /** Instantiate a <code>TiffIFD</code> object
     *  represents a Tiff IFD 
     */
    public TiffIFD() {
        super();

        this.isValid = Validity.Undetermined;
        this.missingRequiredTagMessages = new ArrayList<Message>();
    }


    public void postParse() 
    {
        IFDEntry entry = null;
    
        if ((entry = entries.get(RESOLUTIONUNIT)) != null) {
            this.resolutionUnit = ((Short) entry.getValue()).getValue();
            }
        if ((entry = entries.get(XRESOLUTION)) != null) {
            this.xResolution = (((Rational) entry.getValue()).toLong());
            hasXResolution = true;
            }
        if ((entry = entries.get(YRESOLUTION)) != null) {
            this.yResolution = (Rational) entry.getValue();
            hasYResolution = true;
            }
        if ((entry = entries.get(IMAGECOLORINDICATOR)) != null) {
            this.imageColorIndicator = ((Byte) entry.getValue()).getValue();
            }
        if ((entry = entries.get(ORIENTATION)) != null) {
            this.orientation = ((Short) entry.getValue()).getValue();
            }
        if ((entry = entries.get(BACKGROUNDCOLORINDICATOR)) != null) {
            this.backgroundColorIndicator = ((Short) entry.getValue()).getValue();
            }
        if ((entry = entries.get(INDEXED)) != null) {
            this.indexed = ((Short) entry.getValue()).getValue();
            }
        if ((entry = entries.get(FILLORDER)) != null) {
            this.fillOrder = ((Short) entry.getValue()).getValue();
            }
        if ((entry = entries.get(ROWSPERSTRIP)) != null) {
            this.rowsPerStrip = ((Long) entry.getValue()).getValue();
            hasRowsPerStrip = true;
            }
    }
    
    public Validity validate(JHOVE2 jhove2, Source source) throws JHOVE2Exception, FileNotFoundException, IOException
    {
        IFDEntry entry = null;

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
            hasImageWidth  = true;
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
        if ((entry = entries.get(STRIPOFFSETS)) != null) {
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

            long fileLength = source.getInput(jhove2).getSize();
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


    /**
     * @return the compression
     */
    public int getCompression() {
        return compression;
    }


    /**
     * @return the dotRange
     */
    public int[] getDotRange() {
        return dotRange;
    }


    /**
     * @return the dotRangeOutofRangeMessage
     */
    @ReportableProperty(order = 1, value="Dot Range out of range message.")
    public Message getDotRangeOutofRangeMessage() {
        return dotRangeOutofRangeMessage;
    }


    /**
     * @return the missingRequiredTagMessages
     */
    @ReportableProperty(order = 2, value = "Missing Required tag message.")
    public List<Message> getMissingRequiredTagMessages() {
        return missingRequiredTagMessages;
    }


    /**
     * @return the photometricInterpretation
     */
    public int getPhotometricInterpretation() {
        return photometricInterpretation;
    }


    /**
     * @return the stripByteCounts
     */
    public long[] getStripByteCounts() {
        return stripByteCounts;
    }


    /**
     * @return the stripOffsets
     */
    public long[] getStripOffsets() {
        return stripOffsets;
    }


    /**
     * @return the tileByteCounts
     */
    public long[] getTileByteCounts() {
        return tileByteCounts;
    }


    /**
     * @return the tileLength
     */
    public long getTileLength() {
        return tileLength;
    }


    /**
     * @return the tileOffsets
     */
    public long[] getTileOffsets() {
        return tileOffsets;
    }


    /**
     * @return the tilesAndStripsDefinedMessage
     */
    @ReportableProperty(order = 3, value = "Tiles and strips defined message.")
    public Message getTilesAndStripsDefinedMessage() {
        return tilesAndStripsDefinedMessage;
    }


    /**
     * @return the tilesAndStripsNotDefinedMessage
     */
    @ReportableProperty(order = 4, value = "tiles and strips not defined message.")
    public Message getTilesAndStripsNotDefinedMessage() {
        return tilesAndStripsNotDefinedMessage;
    }


    /**
     * @return the tileWidth
     */
    public long getTileWidth() {
        return tileWidth;
    }


    /**
     * @return the stripByteCountsNotDefinedMessage
     */
    @ReportableProperty(order = 5, value = "StripByteCounts not defined message.")
    public Message getStripByteCountsNotDefinedMessage() {
        return stripByteCountsNotDefinedMessage;
    }


    /**
     * @return the stripOffsetsNotDefinedMessage
     */
    @ReportableProperty(order = 6, value = "StripOffsets not defined message.")
    public Message getStripOffsetsNotDefinedMessage() {
        return stripOffsetsNotDefinedMessage;
    }


    /**
     * @return the planarConfiguration
     */
    public int getPlanarConfiguration() {
        return planarConfiguration;
    }


    /**
     * @return the samplesPerPixel
     */
    public int getSamplesPerPixel() {
        return samplesPerPixel;
    }


    /**
     * @return the stripsLengthInconsistentMessage
     */
    @ReportableProperty(order = 7, value = "StripsLength is inconsistent message.")
    public Message getStripsLengthInconsistentMessage() {
        return StripsLengthInconsistentMessage;
    }


    /**
     * @return the invalidStripOffsetMessage
     */
    @ReportableProperty(order = 8, value = "Invalid StripOffset message.")
    public Message getInvalidStripOffsetMessage() {
        return invalidStripOffsetMessage;
    }


    /**
     * @return the tileByteCountsNotDefinedMessage
     */
    @ReportableProperty(order = 9, value = "TileByteCounts not defined message")
    public Message getTileByteCountsNotDefinedMessage() {
        return tileByteCountsNotDefinedMessage;
    }


    /**
     * @return the tileOffsetsNotDefinedMessage
     */
    @ReportableProperty(order = 10, value = "TileOffsets not defined message.")
    public Message getTileOffsetsNotDefinedMessage() {
        return tileOffsetsNotDefinedMessage;
    }


    /**
     * @return the tileWidthNotDefinedMessage
     */
    @ReportableProperty(order = 11, value = "TileWidth not defined message")
    public Message getTileWidthNotDefinedMessage() {
        return tileWidthNotDefinedMessage;
    }


    /**
     * @return the tileLengthNotDefinedMessage
     */
    @ReportableProperty(order = 12, value = "TileLength not defined message")
    public Message getTileLengthNotDefinedMessage() {
        return tileLengthNotDefinedMessage;
    }


    /**
     * @return the tileOffsetValuesInsufficientMessage
     */
    @ReportableProperty(order = 13, value = "TileOffsetValues are insufficient message.")
    public Message getTileOffsetValuesInsufficientMessage() {
        return tileOffsetValuesInsufficientMessage;
    }


    /**
     * @return the tileByteCountsValuesInsufficientMessage
     */
    @ReportableProperty(order = 14, value = "TilesByteCountsValues are insufficient message.")
    public Message getTileByteCountsValuesInsufficientMessage() {
        return tileByteCountsValuesInsufficientMessage;
    }


    /**
     * @return the transparencyMaskValueInconsistentMessage
     */
    @ReportableProperty(order = 15, value = "Transparency Mask value inconsistent message.")
    public Message getTransparencyMaskValueInconsistentMessage() {
        return transparencyMaskValueInconsistentMessage;
    }


    /**
     * @return the bPSInvalidForTransparencyMaskMessage
     */
    @ReportableProperty(order = 16, value = "BitsPerSample Invalid for transparency mask message.")
    public Message getBPSInvalidForTransparencyMaskMessage() {
        return BPSInvalidForTransparencyMaskMessage;
    }


    /**
     * @return the photometricInterpretationSppLT1InvalidMessage
     */
    @ReportableProperty(order = 17, value = "PhotometricInterpretation SamplesPerPixel less than one invalid message.")
    public Message getPhotometricInterpretationSppLT1InvalidMessage() {
        return photometricInterpretationSppLT1InvalidMessage;
    }


    /**
     * @return the photometricInterpretationSppLT3InvalidMessage
     */
    @ReportableProperty(order = 17, value = "PhotometricInterpretation SamplesPerPixel less than three invalid message.")
    public Message getPhotometricInterpretationSppLT3InvalidMessage() {
        return photometricInterpretationSppLT3InvalidMessage;
    }


    /**
     * @return the colorMapBitCode
     */
    public int[] getColorMapBitCode() {
        return colorMapBitCode;
    }


    /**
     * @return the colorMapRed
     */
    public int[] getColorMapRed() {
        return colorMapRed;
    }


    /**
     * @return the colorMapGreen
     */
    public int[] getColorMapGreen() {
        return colorMapGreen;
    }


    /**
     * @return the colorMapBlue
     */
    public int[] getColorMapBlue() {
        return colorMapBlue;
    }


    /**
     * @return the colorMapNotDefinedForPalleteColorMessage
     */
    @ReportableProperty(order = 18, value = "ColorMap not defined for pallete color message.")
    public Message getColorMapNotDefinedForPalleteColorMessage() {
        return colorMapNotDefinedForPalleteColorMessage;
    }


    /**
     * @return the sppMustEqualOneForPalleteColorMessage
     */
    @ReportableProperty(order = 19, value = "Samples Per Pixel must equal one for pallete color message.")
    public Message getSppMustEqualOneForPalleteColorMessage() {
        return sppMustEqualOneForPalleteColorMessage;
    }


    /**
     * @return the newSubfileType
     */
    public long getNewSubfileType() {
        return newSubfileType;
    }


    /**
     * @return the bitsPerSample
     */
    public int[] getBitsPerSample() {
        return bitsPerSample;
    }


    /**
     * @return the colorMap
     */
    public int[] getColorMap() {
        return colorMap;
    }


    /**
     * @return the insufficientColorMapValuesForPalleteColorMessage
     */
    @ReportableProperty(order = 20, value = "Insufficient colormpa values for pallete color message.")
    public Message getInsufficientColorMapValuesForPalleteColorMessage() {
        return insufficientColorMapValuesForPalleteColorMessage;
    }


    /**
     * @return the colorMapNotDefinedForPalletteColorMessage
     */
    @ReportableProperty(order = 21, value = "Color map not defined for pallete color message.")
    public Message getColorMapNotDefinedForPalletteColorMessage() {
        return colorMapNotDefinedForPalletteColorMessage;
    }


    /**
     * @return the cellLengthShouldNotBePresentMessage
     */
    @ReportableProperty(order = 22, value = "CellLength should not be present message.")
    public Message getCellLengthShouldNotBePresentMessage() {
        return cellLengthShouldNotBePresentMessage;
    }


    /**
     * @return the sppExtraSamplesValueInvalidMessage
     */
    @ReportableProperty(order = 23, value = "Samples per pixel-extra samples value invalid message.")
    public Message getSppExtraSamplesValueInvalidMessage() {
        return sppExtraSamplesValueInvalidMessage;
    }


    /**
     * @return the bpsValueInvalidforCIELabMessage
     */
    @ReportableProperty(order = 24, value = "Bits per sample value invalid for CIE L*a*b* message.")
    public Message getBpsValueInvalidforCIELabMessage() {
        return bpsValueInvalidforCIELabMessage;
    }


    /**
     * @return the xClipPathUnitsNotDefinedMessage
     */
    @ReportableProperty(order = 25, value = "XClipPlathUnits not defined message.")
    public Message getXClipPathUnitsNotDefinedMessage() {
        return xClipPathUnitsNotDefinedMessage;
    }

    /**
     * @return the hasPhotometricInterpretation
     */
    public boolean hasPhotometricInterpretation() {
        return hasPhotometricInterpretation;
    }

    /**
     * @return the hasImageLength
     */
    public boolean hasImageLength() {
        return hasImageLength;
    }


    /**
     * @return the hasImageWidth
     */
    public boolean hasImageWidth() {
        return hasImageWidth;
    }


    /**
     * @return the hasStripByteCounts
     */
    public boolean hasStripByteCounts() {
        return hasStripByteCounts;
    }


    /**
     * @return the hasStripOffsets
     */
    public boolean hasStripOffsets() {
        return hasStripOffsets;
    }


    /**
     * @return the hasTileByteCounts
     */
    public boolean hasTileByteCounts() {
        return hasTileByteCounts;
    }


    /**
     * @return the hasTileLength
     */
    public boolean hasTileLength() {
        return hasTileLength;
    }


    /**
     * @return the hasTileOffsets
     */
    public boolean hasTileOffsets() {
        return hasTileOffsets;
    }


    /**
     * @return the hasTileWidth
     */
    public boolean hasTileWidth() {
        return hasTileWidth;
    }

    /**
     * 
     * @return
     */
    public boolean hasXResolution() {
        return this.hasXResolution;
    }
    
    /**
     * 
     * @return boolean hasYResolution 
     */
    public boolean hasYResolution() {
        return this.hasYResolution;
    }

    /**
     * @return the resolutionUnit
     */
    public int getResolutionUnit() {
        return resolutionUnit;
    }

    public long getXResolution() {
        return xResolution;
    }


    /**
     * @return the yResolution
     */
    public Rational getYResolution() {
        return yResolution;
    }

    /**
     * @return the orientation
     */
    public int getOrientation() {
        return this.orientation;
    }

    /**
     * @return the image color indicator tag value
     */
    public int getImageColorIndicator() {
        return this.imageColorIndicator;
    }

    /**
     * @return the background color indicator tag value 
     */
    public int getBackgroundColorIndicator() {
        return this.backgroundColorIndicator;
    }


    /**
     * @return the imageLength
     */
    public long getImageLength() {
        return imageLength;
    }


    /**
     * @return the imageWidth
     */
    public long getImageWidth() {
        return imageWidth;
    }


    /**
     * @return the indexed tag value
     */
    public int getIndexed() {
        return indexed;
    }

    /**
     * @return the fillOrder
     */
    public int getFillOrder() {
        return this.fillOrder;
    }


    public boolean hasRowsPerStrip() {
        return this.hasRowsPerStrip;
    }


}

