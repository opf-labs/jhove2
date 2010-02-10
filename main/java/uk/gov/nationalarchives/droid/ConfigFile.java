/*
 * � The National Archives 2005-2006.  All rights reserved.
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
 * $Id: ConfigFile.java,v 1.8 2006/03/13 15:15:25 linb Exp $
 *
 * $Log: ConfigFile.java,v $
 * Revision 1.8  2006/03/13 15:15:25  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.7  2006/02/08 11:42:24  linb
 * - make saveConfiguration throw an IOException
 *
 * Revision 1.6  2006/01/31 16:47:29  linb
 * Added log messages that were missing due to the log keyword being added too late
 *
 * Revision 1.5  2006/01/31 16:21:20  linb
 * Removed the dollars from the log lines generated by the previous message, so as not to cause problems with subsequent commits
 *
 * Revision 1.4  2006/01/31 16:19:07  linb
 * Added Log: and Id: tags to these files
 *
 * Revision 1.3  2006/01/31 16:11:37  linb
 * Add support for XML namespaces to:
 * 1) The reading of the config file, spec file and file-list file
 * 2) The writing of the config file and file-list file
 * - The namespaces still need to be set to their proper URIs (currently set to example.com...)
 * - Can still read in files without namespaces
 *
 * Revision 1.2  2006/01/31 12:00:37  linb
 * - Added new text field to option dialog for proxy setting
 * - Added new get/set methods to AnalysisController for proxy settings (from ConfigFile) *
 *
 * $History: ConfigFile.java $
 *
 * *****************  Version 8  *****************
 * User: Walm         Date: 20/10/05   Time: 15:16
 * Updated in $/PRONOM4/FFIT_SOURCE
 * proxy-enabling DROID: read proxy settings from config file
 *
 * *****************  Version 7  *****************
 * User: Walm         Date: 6/06/05    Time: 11:45
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Ensure config file is saved in UTF8
 * Resolve JIRA bug PRON-15
 *
 * *****************  Version 6  *****************
 * User: Mals         Date: 20/04/05   Time: 12:17
 * Updated in $/PRONOM4/FFIT_SOURCE
 * +Saves date in XML in format yyyy-MM-ddTHH:mm:ss
 *
 * *****************  Version 5  *****************
 * User: Walm         Date: 4/04/05    Time: 17:44
 * Updated in $/PRONOM4/FFIT_SOURCE
 * move saveConfig code to ConfigFile class from AnalysisControlle
 *
 * *****************  Version 4  *****************
 * User: Walm         Date: 31/03/05   Time: 15:26
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Initialise all configuration parameters to default values
 * 
 * ******************* Version 5 **********************
 * S. Morrissey For JHOVE2  Date 09/12/2009
 * refactored to use IAnalaysis Controller for constants, 
 * and AnalysisControllerUtil for static methods
 * 
 */

package uk.gov.nationalarchives.droid;

import org.jdom.Namespace;
import uk.gov.nationalarchives.droid.xmlReader.SimpleElement;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.logging.*;

/**
 * Class to hold configuration data for the uk.
 * This data is read from and saved to an XML configuration file.
 *
 * @author Martin Waller
 * @version 1.0.0
 */
public class ConfigFile extends SimpleElement {

    /**
     * The full path of the configuration file
     */
    private String fileName = JHOVE2IAnalysisController.CONFIG_FILE_NAME;
    /**
     * The full path of the signature file
     */
    private String sigFileName = JHOVE2IAnalysisController.SIGNATURE_FILE_NAME;
    /**
     * The version of the signature file referred to by SigFileName
     */
    private int sigFileVersion = 0;
    /**
     * The full URL for the PRONOM web service
     */
    private String sigFileURL = JHOVE2IAnalysisController.PRONOM_WEB_SERVICE_URL;

    /**
     * Default browser for non-window client
     */
    private String browserPath = JHOVE2IAnalysisController.BROWSER_PATH;

    /**
     * Base URL for puid resolution
     */
    private String puidResolutionURL = JHOVE2IAnalysisController.PUID_RESOLUTION_URL;

    /**
     * Proxy server (IP address)
     */
    private String proxyHost = "";
    /**
     * Proxy server port
     */
    private int proxyPort = 0;
    /**
     * Time interval (in days) after which to check whether a newer signature file exists
     */
    private int downloadFreq = JHOVE2IAnalysisController.CONFIG_DOWNLOAD_FREQ;
    /**
     * Date of last signature file download
     */
    private Date lastDownloadDate;

    // path to the last database used
    private String profileDatabasePath;

    // path containing the jasper reports
    private String profileReportPath;
    
    //path containing temporary files produced by the Japser report engine
    private String reportTemporaryDir;

    // the default start mode
    private String startMode = JHOVE2IAnalysisController.START_MODE_DROID;

    //After how many files to pause profiling
    private int pauseFrequency = 0;

    //How long to pause for
    private long pauseLength = 0L;

    private Log log = LogFactory.getLog(this.getClass());
    /**
     * Set the configuration file name
     *
     * @param theFileName file name
     */
    public void setFileName(String theFileName) {
        fileName = theFileName;
    }

    /**
     * set the signature file name
     *
     * @param theFileName file name
     */
    public void setSigFile(String theFileName) {
        this.sigFileName = theFileName;
    }


    /**
     * Set the puid resolution URL
     *
     * @param theBaseURL URL
     */
    public void setPuidResolution(String theBaseURL) {
        this.puidResolutionURL = theBaseURL;
        if (!this.puidResolutionURL.endsWith("/")) {
            this.puidResolutionURL += "/";
        }
    }

    /**
     * Set the path for web browser
     *
     * @param path path
     */
    public void setBrowserPath(String path) {
        this.browserPath = path;
    }

    /**
     * get the web browser path
     *
     * @return browser
     */
    public String getBrowserPath() {
        return this.browserPath;
    }

    /**
     * Get the PUID resolution Base URL
     *
     * @return String
     */
    public String getPuidResolution() {
        return this.puidResolutionURL;
    }

    /**
     * Set the signature file version
     *
     * @param theVersion version
     */
    public void setSigFileVersion(String theVersion) {
        try {
            this.sigFileVersion = Integer.parseInt(theVersion);
        } catch (Exception e) {
            //
        }
    }

    /**
     * Set the signature file version
     *
     * @param theVersion version
     */
    public void setSigFileVersion(int theVersion) {
        this.sigFileVersion = theVersion;
    }

    /**
     * Set the URL for PRONOM web service
     *
     * @param theURL URL
     */
    public void setSigFileURL(String theURL) {
        this.sigFileURL = theURL;
    }

    /**
     * Set the Proxy Server name (IP address)
     *
     * @param theProxyHost the host
     */
    public void setProxyHost(String theProxyHost) {
        this.proxyHost = theProxyHost;
    }

    /**
     * Set the proxy server port
     *
     * @param theProxyPort the port
     */
    public void setProxyPort(String theProxyPort) {
        if (theProxyPort.trim().length() > 0) {
            try {
                this.proxyPort = Integer.parseInt(theProxyPort);
            } catch (NumberFormatException e) {
                //the port number is not translatable to an integer
                this.proxyPort = 0;
                MessageDisplay.generalWarning("Unable to read the proxy server port settings\nMake sure that the <ProxyPort> element in the configuration file is an integer.");
            }
        }
    }

    /**
     * Set the interval (in days) after which to check for newer signature file
     *
     * @param theFreq number of days
     */
    public void setSigFileCheckFreq(String theFreq) {
        try {
            this.downloadFreq = Integer.parseInt(theFreq);
        } catch (Exception e) {
            MessageDisplay.generalWarning("Unable to read the signature file download frequency\nMake sure that the <SigFileCheckFreq> element in the configuration file is an integer number of days.\nThe default value of " + Integer.toString(JHOVE2IAnalysisController.CONFIG_DOWNLOAD_FREQ) + " days will be used");
        }
    }

    /**
     * Set the date of the last signature file download
     *
     * @param theDate the date to use entered as a string
     */
    public void setDateLastDownload(String theDate) {
        java.text.DateFormat df;
        try {
            this.lastDownloadDate = JHOVE2AnalysisControllerUtil.parseXMLDate(theDate);
        } catch (ParseException pe) {
            try {
                df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.MEDIUM, java.text.DateFormat.MEDIUM);
                this.lastDownloadDate = df.parse(theDate);
            } catch (ParseException e1) {
                try {
                    df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.FULL, java.text.DateFormat.FULL);
                    this.lastDownloadDate = df.parse(theDate);
                } catch (ParseException e2) {
                    this.lastDownloadDate = null;
                }
            }
        }
    }

    public String getStartMode() {
        return startMode;
    }

    public void setStartMode(String startMode) {
        this.startMode = startMode;
    }

    public long getPauseLength() {
        return pauseLength;
    }

    public void setPauseLength(String pauseLength) {
        this.pauseLength = Long.valueOf(pauseLength);
    }

    public int getPauseFrequency() {
        return pauseFrequency;
    }

    public void setPauseFrequency(String pauseFrequency) {
        this.pauseFrequency = Integer.valueOf(pauseFrequency);
    }

    public String getProfileReportPath() {
        return profileReportPath;
    }

    public void setProfileReportPath(String profileReportPath) {
        this.profileReportPath = profileReportPath;
    }

   public String getReportTemporaryDir(){
       return reportTemporaryDir;
   }
   
    public void setReportTemporaryDir(String reportTempDir){
        this.reportTemporaryDir = reportTempDir;
    }
    public String getProfileDatabasePath() {
        return profileDatabasePath;
    }

    public void setProfileDatabasePath(String profileDatabasePath) {
        this.profileDatabasePath = profileDatabasePath;
    }

    /**
     * Set the date of the last signature file download to now
     */
    public void setDateLastDownload() {
        java.util.Date now = new java.util.Date();
        try {
            this.lastDownloadDate = now;
        } catch (Exception e) {
            this.lastDownloadDate = null;
        }
    }

    /**
     * Get the name of the configuration file
     *
     * @return name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the name of the signature file
     *
     * @return name
     */
    public String getSigFileName() {
        return sigFileName;
    }

    /**
     * Get the version of the current signature file
     *
     * @return version
     */
    public int getSigFileVersion() {
        return sigFileVersion;
    }

    /**
     * Get the URL for the PRONOM web service
     *
     * @return URL
     */
    public String getSigFileURL() {
        return sigFileURL;
    }

    /**
     * Get the proxy server IP address
     *
     * @return IP address
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * Get the proxy server port
     *
     * @return the port
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * Get the interval in days after which to check whether a newer signature file exists
     *
     * @return checking interval
     */
    public int getSigFileCheckFreq() {
        return downloadFreq;
    }

    /**
     * Get the date of the last signature file download
     *
     * @return date
     */
    public java.util.Date getLastDownloadDate() {
        return lastDownloadDate;
    }

    /**
     * Check whether a newer signature file is available on the PRONOM web service
     *
     * @return boolean
     */
    public boolean isDownloadDue() {
        if (lastDownloadDate == null) {
            return true;
        }
        Date theNow = new Date();
        long elapsedTime = (theNow.getTime() - lastDownloadDate.getTime());
        long theThreshold = downloadFreq * 24L * 3600L * 1000L;

        return elapsedTime > theThreshold;
    }

    /**
     * Saves the current configuration to file in XML format
     *
     * @throws java.io.IOException on error
     */
    public void saveConfiguration() throws IOException {
        java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(fileName), "UTF8"));
        out.write(getConfigurationXMLJDOM());
        out.close();
    }

    /**
     * Creates a Configuration XML Document representing the current configuration
     * Uses JDOM
     *
     * @return the xml document
     */
    private String getConfigurationXMLJDOM() {

        //create all elements that will be used
        Namespace ns = Namespace.getNamespace(JHOVE2IAnalysisController.CONFIG_FILE_NS);
        org.jdom.Element config_file = new org.jdom.Element("ConfigFile", ns);
        org.jdom.Element sig_file = new org.jdom.Element("SigFile", ns);
        org.jdom.Element sig_file_version = new org.jdom.Element("SigFileVersion", ns);
        org.jdom.Element sig_file_url = new org.jdom.Element("SigFileURL", ns);
        org.jdom.Element proxy_host = new org.jdom.Element("ProxyHost", ns);
        org.jdom.Element proxy_port = new org.jdom.Element("ProxyPort", ns);
        org.jdom.Element puid_resolution = new org.jdom.Element("PuidResolution", ns);
        org.jdom.Element browser_path = new org.jdom.Element("BrowserPath", ns);

        org.jdom.Element sig_file_check_freq = new org.jdom.Element("SigFileCheckFreq", ns);
        org.jdom.Element date_last_download = new org.jdom.Element("DateLastDownload", ns);

        org.jdom.Element profile_DatabasePath = new org.jdom.Element("ProfileDatabasePath", ns);
        org.jdom.Element profile_ReportPath = new org.jdom.Element("ProfileReportPath", ns);
        org.jdom.Element start_Mode = new org.jdom.Element("StartMode", ns);
        org.jdom.Element pause_Frequency = new org.jdom.Element("PauseFrequency", ns);
        org.jdom.Element pause_Length = new org.jdom.Element("PauseLength", ns);
        org.jdom.Element report_Temp_Dir = new org.jdom.Element("ReportTemporaryDir", ns);
        
        //populate the elements
        profile_DatabasePath.setText(getProfileDatabasePath());
        profile_ReportPath.setText(getProfileReportPath());
        start_Mode.setText(getStartMode());
        pause_Frequency.setText(Integer.toString(getPauseFrequency()));
        pause_Length.setText(Long.toString(getPauseLength()));
        report_Temp_Dir.setText(getReportTemporaryDir());


        sig_file.setText(getSigFileName());
        sig_file_version.setText(Integer.toString(getSigFileVersion()));
        sig_file_url.setText(getSigFileURL());
        browser_path.setText(getBrowserPath());
        puid_resolution.setText(getPuidResolution());
        proxy_host.setText(getProxyHost());

        int aProxyPort = getProxyPort();
        if (aProxyPort > 0) {
            proxy_port.setText(Integer.toString(aProxyPort));
        } else {
            proxy_port.setText("");
        }
        sig_file_check_freq.setText(Integer.toString(getSigFileCheckFreq()));
        try {
            date_last_download.setText(JHOVE2AnalysisControllerUtil.writeXMLDate(getLastDownloadDate()));
        } catch (Exception e) {
            date_last_download.setText("");
        }
        config_file.addContent(sig_file);
        config_file.addContent(sig_file_version);
        config_file.addContent(sig_file_url);
        config_file.addContent(proxy_host);
        config_file.addContent(proxy_port);
        config_file.addContent(puid_resolution);
        config_file.addContent(browser_path);

        config_file.addContent(sig_file_check_freq);
        config_file.addContent(date_last_download);

        config_file.addContent(profile_DatabasePath);
        config_file.addContent(profile_ReportPath);
        config_file.addContent(report_Temp_Dir);
        config_file.addContent(start_Mode);

        config_file.addContent(pause_Frequency);
        config_file.addContent(pause_Length);

        org.jdom.Document theJDOMdocument = new org.jdom.Document(config_file);

        //write it all to a String
        org.jdom.output.Format xmlFormat = org.jdom.output.Format.getPrettyFormat();
        org.jdom.output.XMLOutputter outputter = new org.jdom.output.XMLOutputter(xmlFormat);
        java.io.StringWriter writer = new java.io.StringWriter();

        try {
            outputter.output(theJDOMdocument, writer);
            writer.close();
        } catch (java.io.IOException e) {
//            System.out.println("Error creating xml file");
            log.info("Error creating xml file");
        }
        return writer.toString();
    }
}
