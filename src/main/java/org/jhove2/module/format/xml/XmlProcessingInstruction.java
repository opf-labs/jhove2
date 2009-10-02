package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractReportable;


public class XmlProcessingInstruction extends AbstractReportable {
	
	protected String processingInstructionName;
	protected String processingInstructionTarget;
	protected String processingInstructionData;
	
	@ReportableProperty(order = 1, value = "Processing Instruction Name")
	public String getProcessingInstructionName() {
		return processingInstructionName;
	}
	
	@ReportableProperty(order = 2, value = "Processing Instruction Target")
	public String getProcessingInstructionTarget() {
		return processingInstructionTarget;
	}
	
	@ReportableProperty(order = 3, value = "Processing Instruction Data")
	public String getProcessingInstructionData() {
		return processingInstructionData;
	}

}
