package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractReportable;


public class XmlNotation extends AbstractReportable {
	protected String notationName;
	protected String notationPublicID;
	protected String notationSystemID;
	
	@ReportableProperty(order = 1, value = "Notation Name")
	public String getNotationName() {
		return notationName;
	}
	
	@ReportableProperty(order = 2, value = "Notation PublicID")
	public String getNotationPublicID() {
		return notationPublicID;
	}
	
	@ReportableProperty(order = 3, value = "NotationSystem ID")
	public String getNotationSystemID() {
		return notationSystemID;
	}

}
