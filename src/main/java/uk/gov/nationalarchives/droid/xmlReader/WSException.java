/*
 * Copyright The National Archives 2005-2006.  All rights reserved.
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
 * $History: WSException.java $
 * 
 * *****************  Version 2  *****************
 * User: Walm         Date: 24/03/05   Time: 9:36
 * Updated in $/PRONOM4/FFIT_SOURCE/xmlReader
 * Tidy up comments
 * 
 *
 * Created on 23 March 2005, 15:29
 */

package uk.gov.nationalarchives.droid.xmlReader;

/**
 * Web service related errors
 *
 * @author walm
 */
public class WSException extends Exception {
    WSException() {
        super();
    }

    WSException(String desc) {
        super(desc);
    }
}