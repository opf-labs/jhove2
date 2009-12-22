package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;


public class Notation extends AbstractReportable {
	protected String name;
	protected String publicId;
	protected String systemId;
	
	@ReportableProperty(order = 1, value = "Notation Name")
	public String getName() {
		return name;
	}
	
	@ReportableProperty(order = 2, value = "Notation PublicID")
	public String getPublicID() {
		return publicId;
	}
	
	@ReportableProperty(order = 3, value = "Notation SystemID")
	public String getSystemID() {
		return systemId;
	}

}
