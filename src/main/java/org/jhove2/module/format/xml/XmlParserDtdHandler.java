package org.jhove2.module.format.xml;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A Handler for capturing and storing SAXParseException events generated during
 * XML Validation
 */
public class XmlParserDtdHandler extends DefaultHandler {

	/**
	 * The module that is doing the parsing also contains the data structures
	 * that store the reportable data
	 */
	private XmlModule xmlModule;

	/** Initializer */
	public XmlParserDtdHandler(XmlModule xmlModule) {
		this.xmlModule = xmlModule;
	}

	@Override
	public void notationDecl(String name, String publicId, String systemId)
			throws SAXException {
		Notation notation = new Notation();
		notation.name = name;
		notation.publicId = publicId;
		notation.systemId = systemId;
		this.xmlModule.notations.add(notation);
	}

	@Override
	public void unparsedEntityDecl(String name, String publicId,
			String systemId, String notationName) throws SAXException {
		Entity entity = new Entity();
		entity.type = Entity.EntityType.ExternalUnparsed;
		entity.name = name;
		entity.publicId = publicId;
		entity.systemId = systemId;
		entity.notationName = notationName;
		this.xmlModule.entities.add(entity);

	}

}
