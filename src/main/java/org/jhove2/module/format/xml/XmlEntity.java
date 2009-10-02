package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractReportable;

public class XmlEntity extends AbstractReportable {
	
	protected enum EntityType {
		Internal, ExternalParsed, ExternalUnparsed
	}
	protected String entityName;
	protected EntityType entityType;
	protected String entityValue;
	protected String entityPublicID;
	protected String entitySystemID;
	protected String entityNotation;
	
	@ReportableProperty(order = 1, value = "Entity Name")
	public String getEntityName() {
		return entityName;
	}
	
	@ReportableProperty(order = 2, value = "Entity Type")
	public EntityType getEntityType() {
		return entityType;
	}
	
	@ReportableProperty(order = 3, value = "Entity Value")
	public String getEntityValue() {
		return entityValue;
	}
	
	@ReportableProperty(order = 4, value = "Entity PublicID")
	public String getEntityPublicID() {
		return entityPublicID;
	}
	
	@ReportableProperty(order = 5, value = "Entity SystemID")
	public String getEntitySystemID() {
		return entitySystemID;
	}
	
	@ReportableProperty(order = 6, value = "Entity Notation")
	public String getEntityNotation() {
		return entityNotation;
	}

}
