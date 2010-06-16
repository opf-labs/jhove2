/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.config.ConfigInfo;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;

/**
 * @author mstrong
 *
 */
public class IFD 
    extends AbstractReportable {
	
    /** IFD Entries in the IFD */
	protected List<IFDEntry> entries = new ArrayList<IFDEntry>();
	
    /** True if this is the first IFD. */
    private boolean first;
    
    /** validity of IFD */
    protected Validity isValid;
    
    /** offset to the next IFD */
	protected long nextIFD;
	
	/** number of IFD Entries in the IFD */ 
	protected int numEntries;
	
    /** offset of the IFD */ 
	protected long offset;
	
    /** True if the is the "thumbnail" IFD. */
    private boolean thumbnail;

    /** TIFF version determined by data in IFD */
    private int version;
    
    /* Message for Zero IFD Entries */
    private Message zeroIFDEntriesMessage;
        

	
	@ReportableProperty(order = 3, value="IFD entries.")
	public List<IFDEntry> getIFDEntries() {
		return entries;
	}
	  
	@ReportableProperty(order = 4, value = "Offset of next IFD.")
	public long getNextIFD() {
		return nextIFD;
	}
	
	@ReportableProperty (order = 2, value = "Number of IFD entries.")
	public int getNumEntries() {
		return numEntries;
	}
	
	@ReportableProperty(order = 1, value = "Byte offset of IFD.")
	public long getOffset() {
		return offset;
	}
		
	   /**
     * Parse a source unit input. Implicitly set the start and end elapsed time.
     * 
     * @param input
     *            JHOVE2 framework
     * @param input
     *            Input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    public void parse(JHOVE2 jhove2, Input input, long offset) throws EOFException,
            IOException, JHOVE2Exception {
        this.isValid = Validity.Undetermined;

        /* Read the first byte. */
        input.setPosition(offset);
        numEntries = input.readUnsignedShort();
        if (numEntries < 1){
            this.isValid = Validity.False;
            Object[]messageArgs = new Object[]{0, input.getPosition(), numEntries};
            this.zeroIFDEntriesMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.IFD.zeroIFDEntriesMessage",
                    messageArgs, jhove2.getConfigInfo());  
        }

        // parse through the list of IFDs
        for (int i=0; i<numEntries; i++) {
            IFDEntry ifdEntry = new IFDEntry();
            ifdEntry.parse(jhove2, input);
            version = ifdEntry.getVersion();
            entries.add(ifdEntry);
        }
    }
	
    public void parse(long offset) {
        this.isValid = Validity.Undetermined;
        
     }

    public int getVersion() {
        // TODO Auto-generated method stub
        return this.version;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

    public void setThumbnail(boolean thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isThumbnail() {
        return thumbnail;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
    

}
