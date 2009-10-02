package org.jhove2.module.format.xml;

import java.util.ArrayList;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractReportable;

public class XmlNamespace extends AbstractReportable {
	protected String namespaceURI;
	protected String schemaLocation;
	protected ArrayList<String> namespacePrefixes;
	
	@ReportableProperty(order = 1, value = "Namespace URI")
	public String getNamespaceURI() {
		return namespaceURI;
	}
	
	@ReportableProperty(order = 2, value = "Schema Location")
	public String getSchemaLocation() {
		return schemaLocation;
	}
	
	@ReportableProperty(order = 3, value = "Namespace Prefixes")
	public ArrayList<String> getNamespacePrefixes() {
		return namespacePrefixes;
	}


}
