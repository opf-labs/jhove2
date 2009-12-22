package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

public class XmlDeclaration   extends AbstractReportable {
	protected String version;
	protected String encoding;
	protected String standalone;
	
	@ReportableProperty(order = 1, value = "XML Version")
	public String getVersion() {
		return version;
	}

	@ReportableProperty(order = 2, value = "Character Encoding")
	public String getEncoding() {
		return encoding;
	}
	
	@ReportableProperty(order = 3, value = "Standalone")
	public String getStandalone() {
		return standalone;
	}

}
