package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractReportable;

public class Entity extends AbstractReportable {
	
	protected enum EntityType {
		Internal, ExternalParsed, ExternalUnparsed
	}
	protected String name;
	protected EntityType type;
	protected String value;
	protected String publicId;
	protected String systemId;
	protected String notationName;
	
	@ReportableProperty(order = 1, value = "Entity Name")
	public String getName() {
		return name;
	}
	
	@ReportableProperty(order = 2, value = "Entity Type")
	public EntityType getType() {
		return type;
	}
	
	@ReportableProperty(order = 3, value = "Entity Value")
	public String getValue() {
		return value;
	}
	
	@ReportableProperty(order = 4, value = "Entity PublicID")
	public String getPublicID() {
		return publicId;
	}
	
	@ReportableProperty(order = 5, value = "Entity SystemID")
	public String getSystemID() {
		return systemId;
	}
	
	@ReportableProperty(order = 6, value = "Entity Notation Name")
	public String getNotationName() {
		return notationName;
	}

}
