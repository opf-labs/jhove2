/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
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
package org.jhove2.module.format.shapefile;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * This class holds feature information extracted from record structures in the main
 * shapefile
 * 
 * @author rnanders
 */
public class ShapefileRecord extends AbstractReportable {
    
    /** The record offset. */
    public long offset;
    
    /** The numeric code for the shape type. */
    public long shapeTypeID;
    
    /** The name of the shape type (e.g. polygon). */
    public String shapeType;
    
    /** The minimum X bounding box coordinate. */
    public double minX;
    
    /** The maximum X bounding box coordinate. */
    public double maxX;
    
    /** The minimum Y bounding box coordinate. */
    public double minY;
    
    /** The maximum Y bounding box coordinat. */
    public double maxY;


    /**
     * Gets the record offset.
     *
     * @return the offset
     */
    @ReportableProperty(order = 1, value ="Record offset")
    public long getOffset() {
        return offset;
    }

    /**
     * Gets the numeric code for the shape type.
     *
     * @return the shape type id
     */
    @ReportableProperty(order = 2, value ="Shape Type Code")
    public long getShapeTypeID() {
        return shapeTypeID;
    }

    /**
     * Gets the name of the shape type (e.g. polygon).
     *
     * @return the shape type
     */
    @ReportableProperty(order = 3, value ="Shape Type Name")
    public String getShapeType() {
        return shapeType;
    }

    /**
     * Gets the minimum X bounding box coordinate.
     *
     * @return the min x
     */
    @ReportableProperty(order = 4, value ="Min X")
    public double getMinX() {
        return minX;
    }

    /**
     * Gets the maximum X bounding box coordinate.
     *
     * @return the max x
     */
    @ReportableProperty(order = 5, value ="Max X")
    public double getMaxX() {
        return maxX;
    }

    /**
     * Gets the minimum Y bounding box coordinate.
     *
     * @return the min y
     */
    @ReportableProperty(order = 6, value ="Min Y")
    public double getMinY() {
        return minY;
    }

    /**
     * Gets the maximum Y bounding box coordinat.
     *
     * @return the max y
     */
    @ReportableProperty(order = 7, value ="Max Y")
    public double getMaxY() {
        return maxY;
    }


}
