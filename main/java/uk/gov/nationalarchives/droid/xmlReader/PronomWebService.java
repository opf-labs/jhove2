/*
 * © The National Archives 2005-2006.  All rights reserved.
 * See Licence.txt for full licence details.
 *
 * Developed by:
 * Tessella Support Services plc
 * 3 Vineyard Chambers
 * Abingdon, OX14 3PX
 * United Kingdom
 * http://www.tessella.com
 *
 * Tessella/NPD/4305
 * PRONOM 4
 *
 * $History: PronomWebService.java $
 * 
 * *****************  Version 4  *****************
 * User: Walm         Date: 20/10/05   Time: 15:16
 * Updated in $/PRONOM4/FFIT_SOURCE/xmlReader
 * Provide a public property indicating whether or not the web service
 * connection was successful.
 * 
 * *****************  Version 3  *****************
 * User: Walm         Date: 10/05/05   Time: 19:15
 * Updated in $/PRONOM4/FFIT_SOURCE/xmlReader
 * Use apache SOAP library
 *
 * *****************  Version 2  *****************
 * User: Walm         Date: 29/03/05   Time: 11:12
 * Updated in $/PRONOM4/FFIT_SOURCE/xmlReader
 * Review formatting
 *
 *
 * Created on 23 March 2005, 15:35
 */

package uk.gov.nationalarchives.droid.xmlReader;

import org.apache.soap.Body;
import org.apache.soap.Envelope;
import org.apache.soap.messaging.Message;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

/**
 * Provides the necessary infrastructure to communicate with the PRONOM web service.
 *
 * @author Martin Waller
 * @version 1.0.0
 */
public class PronomWebService {

    /**
     * flag to indicate whether a successful communication was established via the web service
     */
    public static boolean isCommSuccess = false;

    /**
     * Exposes the PRONOM web service.  This is the only method required to call a
     * web service.  The URL of the web service must be provided.  The web server
     * name or IP address is then extracted from the URL.
     *
     * @param theWebServiceURL Full URL to webservice
     * @param theProxyHost
     * @param theProxyPort
     * @param theMethodName    Name of the web service method.  The suffix "request"
     *                         is appended for the SOAP request and the suffix "response" is appended for the
     *                         SOAP response.
     * @param theXMLWrapper    Name of XML Element which wraps the result inside the web service response
     * @return
     * @throws Exception
     */
    public static Element sendRequest(String theWebServiceURL,
                                      String theProxyHost,
                                      int theProxyPort,
                                      String theMethodName,
                                      String theXMLWrapper) throws Exception {

        //assume that the communication fails.  If it succeeds, the flag will be reset at the end
        isCommSuccess = false;

        String theResult = "";

        //set up the URL
        URL myUrl = null;
        try {
            myUrl = new URL(theWebServiceURL);
        }
        catch (MalformedURLException e) {
            throw new WSException("Invalid URL for PRONOM web services" + theWebServiceURL + "\n" + e.getMessage());
        }


        try {
            // Create and send a message
            Message aMessage = new Message();

            //If a proxy server is required for connecting to web service, then record its settings
            try {
                if (theProxyHost.length() > 0 && theProxyPort > 0) {
                    org.apache.soap.transport.http.SOAPHTTPConnection aSOAPConnection = new org.apache.soap.transport.http.SOAPHTTPConnection();
                    aSOAPConnection.setProxyHost(theProxyHost);
                    aSOAPConnection.setProxyPort(theProxyPort);
                    aMessage.setSOAPTransport(aSOAPConnection);
                }
            } catch (Exception e) {
                throw new WSException("Error while creating the proxy settings" + "\n" + e.getMessage());
            }

            //send the SOAP message to webservice
            aMessage.send(myUrl, "http://pronom.nationalarchives.gov.uk:" + theMethodName + "In", createEnvelope(theMethodName));

            //Receive the message response
            org.apache.soap.transport.SOAPTransport st = aMessage.getSOAPTransport();
            BufferedReader br = st.receive();
            String line = br.readLine();
            if (line == null) {
            } else {
                while (line != null) {
                    theResult += line;
                    line = br.readLine();
                }
            }
        }
        catch (Exception e) {
            throw new WSException("Error while sending message to PRONOM web services" + "\n" + e.getMessage());
        }

        // theResult now contains the complete result from the
        // webservice in XML format.
        Element result;
        SAXBuilder builder = new SAXBuilder();
        StringReader s = new StringReader(theResult);
        Document doc = builder.build(s);
        result = doc.getRootElement();
        try {
            result = PronomWebService.extractXMLelement(result, theMethodName + "Response");
        } catch (Exception e) {
            throw new WSException("Unexpected response from PRONOM web service");
        }
        try {
            if (theXMLWrapper != null) {
                result = PronomWebService.extractXMLelement(result, theXMLWrapper);
            }
        } catch (Exception e) {
            throw new WSException("Response from PRONOM web service did not contain the expected element");
        }

        //indicate that the communication was successful
        isCommSuccess = true;

        return result;
    }


    /**
     * Create a SOAP envelope containing the web services request
     *
     * @param theMethodName Name of the SOAP operation to call
     * @return
     */
    private static Envelope createEnvelope(String theMethodName) {
        // Create the required SOAP elements
        Envelope anEnvelope = new Envelope();
        Body aBody = new Body();
        Vector entries = new Vector();
        org.w3c.dom.Document doc = null;

        // Put the request element into the body of the SOAP
        javax.xml.parsers.DocumentBuilder xdb = org.apache.soap.util.xml.XMLParserUtils.getXMLDocBuilder();
        try {
            doc = xdb.newDocument();
            doc.appendChild(doc.createElementNS("pron:", theMethodName));
        }
        catch (Exception e) {
        }
        entries.add(doc.getDocumentElement());

        // Set up the SOAP components
        aBody.setBodyEntries(entries);
        anEnvelope.setBody(aBody);

        return anEnvelope;
    }

    /**
     * Extracts the contents of the named XML element from a parent
     *
     * @param parentElement The parent element
     * @param theElement    Name of the XML element to be extracted
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    public static Element extractXMLelement(Element parentElement, String theElement) throws JDOMException, IOException {

        Filter elementFilter = new ElementFilter();
        List<Element> children = parentElement.getContent(elementFilter);
        for (Element element : children) {
            if (element.getName().equalsIgnoreCase(theElement)) {
                return element;
            }
            Element child = extractXMLelement(element, theElement);
            if (child != null) {
                return child;
            }
        }
        return null;
    }

}
