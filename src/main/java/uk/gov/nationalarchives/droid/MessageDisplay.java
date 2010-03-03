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
 * $History: MessageDisplay.java $
 * 
 * *****************  Version 7  *****************
 * User: Walm         Date: 26/04/05   Time: 17:29
 * Updated in $/PRONOM4/FFIT_SOURCE
 * allow different messages in GUI and on command line
 * 
 * *****************  Version 6  *****************
 * User: Mals         Date: 19/04/05   Time: 9:36
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Tessella Ref: NPD/4305/PR/IM/2005APR18/09:51:03 Issue 36
 * +Changed extension warning to text in email from A.Brown (Tessella Ref:
 * NPD/4305/CL/CSC/2005FEB17/16:34:13)
 * 
 * +Changed any reference of uk to DROID
 * 
 * *****************  Version 5  *****************
 * User: Walm         Date: 4/04/05    Time: 17:44
 * Updated in $/PRONOM4/FFIT_SOURCE
 * code for responding to missing signature file on startup
 * 
 * *****************  Version 4  *****************
 * User: Walm         Date: 29/03/05   Time: 17:02
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Display messages in the GUI if one has been defined
 *
 * Created on 21 February 2005, 14:47
 */

package uk.gov.nationalarchives.droid;

//import java.awt.Component;
//import javax.swing.JDialog;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
import org.apache.commons.logging.*;
//import uk.gov.nationalarchives.droid.GUI.FileIdentificationPane;

/**
 * Allows messages to be displayed in the most appropriate manner to the user.
 *
 * @author Martin Waller
 * @version 1.0.0
 */
public final class MessageDisplay {
    public static String FILEEXTENSIONWARNING = "Possible file extension mismatch";
    public static String POSITIVEIDENTIFICATIONSTATUS = "Positively identified";
    public static String TENTATIVEIDENTIFICATIONSTATUS = "Tentatively identified";
    public static String UNIDENTIFIEDSTATUS = "Unable to identify";

//    private static JFrame myMainPane = null;
//    private static JDialog statusResultsDialog = null;
    private static boolean isGUIdisplay = false;
    private static boolean hideXMLWarnings = false;
    private static int numXMLWarnings = 0;
    
    private static Log log = LogFactory.getLog(MessageDisplay.class);

    /**
     * Displays a special warning for unknown XML elements when reading XML files
     *
     * @param unknownElement   The name of the element which was not recognised
     * @param containerElement The name of the element which contains the unrecognised element
     */
    public static void unknownElementWarning(String unknownElement, String containerElement) {
        String theCMDMessage = "WARNING: Unknown XML element " + unknownElement + " found under " + containerElement + " ";
        String theGUIMessage = theCMDMessage + "\nDo you wish to hide any further XML reading errors for this file?";
        numXMLWarnings++;
//        if (isGUIdisplay) {
//            if (!hideXMLWarnings) {
//                hideXMLWarnings = (javax.swing.JOptionPane.showConfirmDialog(myMainPane, theGUIMessage, "DROID warning", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE) == 0);
//            }
//        } else {
//            System.out.println(theCMDMessage);
            log.info(theCMDMessage);
//        }
    }

    /**
     * Displays a special warning for unknown XML attributes when reading XML files
     *
     * @param unknownAttribute The name of the attribute which was not recognised
     * @param containerElement The name of the element which contains the unrecognised attribute
     */
    public static void unknownAttributeWarning(String unknownAttribute, String containerElement) {
        String theCMDMessage = "WARNING: Unknown XML attribute " + unknownAttribute + " found for " + containerElement + " ";
        String theGUIMessage = theCMDMessage + "\nDo you wish to hide any further XML reading errors for this file?";
        numXMLWarnings++;
//        if (isGUIdisplay) {
//            if (!hideXMLWarnings) {
//                hideXMLWarnings = (javax.swing.JOptionPane.showConfirmDialog(myMainPane, theGUIMessage, "DROID warning", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE) == 0);
//            }
//        } else {
//            System.out.println(theCMDMessage);
             log.info(theCMDMessage);
//        }
    }

    /**
     * Displays a general warning
     *
     * @param theWarning The text to be displayed
     */
    public static void generalWarning(String theWarning) {
        String theMessage = "WARNING: " + theWarning.replaceFirst("java.lang.Exception: ", "");
       
//        if (isGUIdisplay) {
//            javax.swing.JOptionPane.showMessageDialog(myMainPane, theMessage, "DROID warning", javax.swing.JOptionPane.WARNING_MESSAGE);
//        } else {
//            System.out.println(theMessage);
             log.info(theMessage);
//        }
    }


    /**
     * Displays general information
     *
     * @param theMessage The text to be displayed
     */
    public static void generalInformation(String theMessage) {
//        if (isGUIdisplay) {
//            javax.swing.JOptionPane.showMessageDialog(myMainPane, theMessage, "DROID information", javax.swing.JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            System.out.println(theMessage);
             log.info(theMessage);
//        }
    }

    /**
     * Displays general information
     *
     * @param theGUIMessage The text to be displayed in GUI mode
     * @param theCMDMessage The text to be displayed in command line mode
     */
    public static void generalInformation(String theGUIMessage, String theCMDMessage) {
//        if (isGUIdisplay) {
//            javax.swing.JOptionPane.showMessageDialog(myMainPane, theGUIMessage, "DROID information", javax.swing.JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            System.out.println(theCMDMessage);
             log.info(theCMDMessage);
//        }
    }

    /**
     * Displays general information in the status bar
     *
     * @param theMessage The text to be displayed
     */
    public static void setStatusText(String theMessage) {
//        if (isGUIdisplay) {
//            if(myMainPane.getClass().getName().equalsIgnoreCase("uk.gov.nationalarchives.droid.GUI.FileIdentificationPane"))
//            {
//              
//               JPanel jPanelStatusBar =   (JPanel)myMainPane.getContentPane().getComponents()[2];
//               JLabel jStatus = (JLabel)jPanelStatusBar.getComponents()[0];
//               jStatus.setText(theMessage);
//                
//            }
//            javax.swing.JOptionPane.showMessageDialog(myMainPane,theMessage,"uk information",javax.swing.JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            System.out.println(theMessage);
             log.info(theMessage);
//        }
    }

    /**
     * Displays general information in the status bar
     *
     * @param theGUIMessage The text to be displayed in the status bar
     * @param theCMDMessage The text to be displayed in command line mode
     */
    public static void setStatusText(String theGUIMessage, String theCMDMessage) {
//        if (isGUIdisplay) {
//            if(myMainPane.getClass().getName().equalsIgnoreCase("uk.gov.nationalarchives.droid.GUI.FileIdentificationPane"))
//            {
//              
//               JPanel jPanelStatusBar =   (JPanel)myMainPane.getContentPane().getComponents()[2];
//               JLabel jStatus = (JLabel)jPanelStatusBar.getComponents()[0];
//               jStatus.setText(theGUIMessage);
//                
//            }
//        } else {
//            System.out.println(theCMDMessage);
             log.info(theCMDMessage);
//        }
    }

    /**
     * Displays a general error
     *
     * @param theWarning The text to be displayed
     * @throws Exception on error
     */
    public static void generalError(String theWarning) throws Exception {
        String theMessage = "Error: " + theWarning;
//        if (isGUIdisplay) {
//            javax.swing.JOptionPane.showMessageDialog(myMainPane, theMessage, "DROID error", javax.swing.JOptionPane.ERROR_MESSAGE);
//        } else {
//            System.out.println(theMessage);
             log.error(theMessage);
//        }
        throw new Exception(theWarning);
    }

//        /**
//     * Displays a general error for the StatusResultsDialog 
//     *
//     * @param theWarning The text to be displayed
//     * @throws Exception on error
//     */
//    public static void generalErrorjDialog(String theWarning) throws Exception {
//        String theMessage = "Error: " + theWarning;
//        javax.swing.JOptionPane.showMessageDialog(statusResultsDialog, theMessage, "DROID error", javax.swing.JOptionPane.ERROR_MESSAGE);
//    }
//    
    
    /**
     * Displays a fatal error and then exits
     *
     * @param theWarning The text to be displayed
     */
    public static void fatalError(String theWarning) {
        String theMessage = "Fatal Error: " + theWarning;
//        if (isGUIdisplay) {
//            javax.swing.JOptionPane.showMessageDialog(myMainPane, theMessage, "DROID fatal error", javax.swing.JOptionPane.ERROR_MESSAGE);
//        } else {
//            System.out.println(theMessage);
             log.fatal(theMessage);
//        }
//        System.exit(0);
    }

    /**
     * Returns the accumulated number of XML warnings that have been received since this was last reset
     *
     * @return int
     */
    public static int getNumXMLWarnings() {
        return numXMLWarnings;
    }

    /**
     * Method to be called each time an XML file read is about to start:
     * sets the number of warnings to 0 and ensures that warnings are displayed again
     */
    public static void resetXMLRead() {
        numXMLWarnings = 0;
        hideXMLWarnings = false;
    }

//    /**
//     * Define the main pane to use for a jDialog box e.g. StatusResultsDialog
//     *
//     * @param theMainPane The jDialog to use for displaying GUI messages
//     */
//    public static void initialiseMainPane(JDialog statusDialog) {
//       statusResultsDialog = statusDialog;
//    }

//    /**
//     * Define the main pane to use for displaying GUI messages
//     *
//     * @param theMainPane The pane to use for displaying GUI messages
//     */
//    public static void initialiseMainPane(JFrame theMainPane) {
//        myMainPane = theMainPane;
//        isGUIdisplay = true;
//    }

//    /**
//     * Respond to missing signature file on startup
//     *
//     * @return bool
//     */
//    public static boolean exitDueToMissigSigFile() {
//        if (isGUIdisplay) {
//            //Confirm with user whether they would like to download new signature file
//            return javax.swing.JOptionPane.showConfirmDialog(myMainPane, "Signature file was not found.\nDo you wish to download a new one from PRONOM web service?\nSelecting NO will exit the application.", "Signature file not found", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE) != javax.swing.JOptionPane.YES_OPTION;
//        } else {
//            System.out.println("Signature file not loaded");
//             log.error("Signature file not loaded");
//            return true;
//        }
//    }


}
