package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;


public class ProcessingInstruction extends AbstractReportable {
	
	protected String target;
	protected String data;
	
	@ReportableProperty(order = 1, value = "Processing Instruction Target")
	public String getTarget() {
		return target;
	}
	
	@ReportableProperty(order = 2, value = "Processing Instruction Data")
	public String getData() {
		return data;
	}

}
