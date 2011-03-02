/**
 * 
 */
package org.jhove2.core.source;

import org.jhove2.annotation.ReportableProperty;

/** Interface for measurable {@link org.jhove2.core.source.Source} units, that
 * is, those that have a knowable size.
 * 
 * @author slabrams
 */
public interface MeasurableSource
    extends Source
{
    /** Get ending offset of the source unit, in bytes, relative to the
     * parent source.  If there is no parent, the ending offset is the
     * size.
     * @return Starting offset of the source unit
     */
    @ReportableProperty(order=2, value="Ending offset, in bytes, relative to " +
            "the parent source.  If there is no parent, the offset is the size.")
    public long getEndingOffset();
    
    /** Get size, in bytes.
     * @return Size
     */
    @ReportableProperty(order=3, value="Size, in bytes.")
    public long getSize();
    
    /** Get starting offset of the source unit, in bytes, relative to the
     * parent source.  If there is no parent, the starting offset is 0.
     * @return Starting offset of the source unit
     */
    @ReportableProperty(order=1, value="Starting byte offset, in bytes, relative " +
            "to the parent source.  If there is no parent, the offset is 0.")
    public long getStartingOffset();
}
