package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

public class Namespace extends AbstractReportable {
	protected String prefix;
	protected String uri;
	
	@ReportableProperty(order = 1, value = "Namespace Prefix")
	public String getPrefix() {
		return prefix;
	}
	@ReportableProperty(order = 2, value = "Namespace URI")
	public String getURI() {
		return uri;
	}
	
	


}
