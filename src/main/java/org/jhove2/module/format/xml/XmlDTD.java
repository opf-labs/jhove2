package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractReportable;

public class XmlDTD extends AbstractReportable {
	protected String dtdName;
	protected String dtdPublicID;
	protected String dtdSystemID;
	protected boolean dtdInternalSubset;
	
	@ReportableProperty(order = 1, value = "DTD Name")
	public String getDtdName() {
		return dtdName;
	}
	
	@ReportableProperty(order = 2, value = "DTD PublicID")
	public String getDtdPublicID() {
		return dtdPublicID;
	}
	
	@ReportableProperty(order = 3, value = "DTD SystemID")
	public String getDtdSystemID() {
		return dtdSystemID;
	}

	@ReportableProperty(order = 4, value = "DTD Internal Subset")
	public boolean isDtdInternalSubset() {
		return dtdInternalSubset;
	}
	

}
