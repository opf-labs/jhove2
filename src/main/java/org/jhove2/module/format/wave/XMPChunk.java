/**
 * 
 */

package org.jhove2.module.format.wave;

import java.io.EOFException;
import java.io.IOException;
import java.util.Map;
import org.jhove2.config.spring.SpringConfigInfo;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.format.FormatIdentification.Confidence;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.ByteStreamSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.riff.GenericChunk;

/** WAVE format Extensible Metadata Platform (XMP) chunk.
 * 
 * @author slabrams
 */
public class XMPChunk
    extends GenericChunk
{
    /** Instantiate a new <code>XMPChunk</code>. */
    public XMPChunk() {
        super();
    }
    
    /** 
     * Parse a WAVE chunk.
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
     * @see org.jhove2.module.format.Parser#parse(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source, org.jhove2.core.io.Input)
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = super.parse(jhove2, source, input);
        
        /* The chunk contents are in XML; invoke the XML module. */
        ByteStreamSource child =
            new ByteStreamSource(jhove2, source, input.getPosition(), this.size);
        Map<String, Object> i8r = SpringConfigInfo.getObjectsForType(I8R.class);
        I8R xml = (I8R) i8r.get("XmlIdentifier");;
        FormatIdentification id = new FormatIdentification(xml, Confidence.PositiveGeneric);
        child.addPresumptiveFormat(id);
        source.addChildSource(child);
        jhove2.characterize(child, input);      
        consumed += this.size;
        
        return consumed;
    }
}
