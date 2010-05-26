/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
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
	
	protected List<IFDEntry> entries;
	protected long nextIFD;
	protected int numEntries;
	protected long offset;
	protected Validity isValid;
    private List<Message> invalidFieldMessage;
	
	@ReportableProperty(order = 3, value="IFD entries.")
	public List<IFDEntry> getIFDEntries() {
		return entries;}
	  
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
        int numberOfEntries = input.readUnsignedShort();
        if (numberOfEntries < 1){
            this.isValid = Validity.False;
            Object[]messageArgs = new Object[]{0, input.getPosition(), numberOfEntries};
            this.invalidFieldMessage.add(new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.IFD.zeroIFDEntriesMessage",
                    messageArgs, jhove2.getConfigInfo()));  
        }
        // parse through the list of IFDs
        for (int i=0; i<numberOfEntries; i++) {
            IFDEntry ifdEntry = new IFDEntry();
            ifdEntry.parse(input);
            entries.add(ifdEntry);
        }
        
            
        
    }
	
    
    public void parse(long offset) {
        this.isValid = Validity.Undetermined;
        
     }
}
