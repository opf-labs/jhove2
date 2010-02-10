
package org.jhove2.module.identify;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import uk.gov.nationalarchives.droid.JHOVE2AnalysisControllerUtil;
import uk.gov.nationalarchives.droid.ConfigFile;
import uk.gov.nationalarchives.droid.FileCollection;
import uk.gov.nationalarchives.droid.JHOVE2IAnalysisController;
import uk.gov.nationalarchives.droid.IdentificationFile;
import uk.gov.nationalarchives.droid.MessageDisplay;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;
import uk.gov.nationalarchives.droid.xmlReader.PronomWebService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import uk.gov.nationalarchives.droid.signatureFile.FileFormat;

/**
 * This is a condensation and adaptation of {@link uk.gov.nationalarchives.droid.AnalysisController}
 * Essentially, methods here have been copied from the altered version of that class included
 * in the JHOVE2 distribution, with some additional accessor/mutator and convenience methods,
 * and  a few additional modification to suppress unwanted features.
 * 
 * Please see the file DROID-LICENSE.txt in the JHOVE2 distribution for a complete statement
 *  of the BSD license rights governing the use of DROID source code.
 *  
 * @author smorrissey
 *
 */
public class DROIDAnalysisController implements JHOVE2IAnalysisController {

	/**
	 * Contains a list of all format names from the current signature file 
	 */
	public List<String> formatName = new ArrayList<String>();
	/**
	 * Contains a list of all PUIDs from the current signature file 
	 */
	public List<String> PUID = new ArrayList<String>();
	/**
	 * Contains a list of all Mime Types from the current signature file 
	 */
	public List<String> mimeType = new ArrayList<String>();
	/**
	 * Contains a list of all format details (PUID, format name etc) from the current signature file 
	 */
	public List<String> version = new ArrayList<String>();
	/**
	 * Contains a list of all format details (PUID, format name etc) from the current signature file 
	 */
	public List<String> fileFormatDetail = new ArrayList<String>();

	/**
	 *  Indicates whether fileFormatDetail was successfully populated
	 */
	public boolean isFileFormatPopulated = false;

	//class variables:
	private ConfigFile configFile = new ConfigFile();
	private FileCollection fileCollection = new FileCollection();
	private FFSignatureFile sigFile;

	/**
	 * output formats to be used for saving results at end of run
	 */
	/**
	 * base file name to be used for saving results at end of run
	 */

	public DROIDAnalysisController() {
		fileCollection = new FileCollection();
	}

	/**
	 *  This is used to collect file format information from the current signature file
	 */
	public void getFileFormatsDetails() {
		formatName = new ArrayList<String>();
		PUID = new ArrayList<String>();
		mimeType = new ArrayList<String>();
		fileFormatDetail = new ArrayList<String>();
		version = new ArrayList<String>();
		try {
			for (int i = 0; i < sigFile.getNumFileFormats(); i++) {
				try {
					FileFormat ff = sigFile.getFileFormat(i);
					String strFormatName = ff.getName();
					String strPUID = ff.getPUID();
					String strMimeType = ff.getMimeType();
					String strVersion = ff.getVersion();
					String strFormatDetail = "";

					if (strPUID != null) {
						PUID.add(strPUID.trim());
					}
					if (strFormatDetail != null) {
						if (!formatName.contains(strFormatName.trim())) {
							formatName.add(strFormatName.trim());
						}
					}
					if(strVersion != null && !strVersion.equals("")){
						if (!strVersion.equals("") && !version.contains(strVersion.trim())) {
							version.add(strVersion.trim());
						}
					}
					if (strMimeType != null && !strMimeType.equals("")) {
						if (!mimeType.contains(strMimeType.trim())) {
							mimeType.add(strMimeType.trim());
						}
					}
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}

			}
		} catch (Exception ex) {
			isFileFormatPopulated= false;
		}
		Collections.sort(PUID);
		Collections.sort(formatName);
		Collections.sort(mimeType);
		isFileFormatPopulated=true;
	}

	/**
	 * Reads a configuration file, and loads the contents into memory.
	 *
	 * @param theFileName The name of the configuration file to open
	 * @throws Exception on error
	 */
	public void readConfiguration(String theFileName) throws Exception {
		configFile = JHOVE2AnalysisControllerUtil.loadConfigFile(theFileName);
		return;
	}

	/**
	 * Reads in and parses the signature file
	 * Updates the configuration file with this signature file
	 *
	 * @param theSigFileName Name of the signature file
	 * @return name of sig file
	 * @throws Exception on error
	 */
	public String readSigFile(String theSigFileName) throws Exception {
		sigFile = JHOVE2AnalysisControllerUtil.loadSigFile(configFile, theSigFileName);
		return sigFile.getVersion();
	}

	/**
	 * Empties the file list
	 */
	public void resetFileList() {
		fileCollection.removeAll();
	}

	/**
	 * Add files to list of files ready for identifier.
	 * Calls addFile(fileFolderName, false)
	 *
	 * @param fileFolderName file or folder to add to
	 */
	public void addFile(String fileFolderName) {
		addFile(fileFolderName, false);
	}

	/**
	 * Add file to list of files ready for identifier.
	 * If the file is already in list, then does not add it.
	 * If the file is a folder, then adds all files it contains.
	 * If isRecursive is set to true, then it also searches recursively through any subfolders.
	 *
	 * @param fileFolderName file or folder to add to
	 * @param isRecursive    whether or not to search folders recursively
	 */
	public void addFile(String fileFolderName, boolean isRecursive) {
		fileCollection.addFile(fileFolderName, isRecursive);
	}

	/**
	 * Remove file from the file list
	 *
	 * @param theFileName the name of the file to remove
	 */
	public void removeFile(String theFileName) {
		fileCollection.removeFile(theFileName);
	}

	public void removeFile(int theIndex) {
		fileCollection.removeFile(theIndex);
	}

	/**
	 * Returns an identificationFile object based on its index in the list
	 *
	 * @param theIndex index of file in file collection
	 * @return identifier file
	 */
	public IdentificationFile getFile(int theIndex) {
		IdentificationFile theFile = null;
		try {
			theFile = fileCollection.getFile(theIndex);
		} catch (Exception e){
			//
		}
		return theFile;
	}

	/**
	 * Returns the number of files in identifier file list
	 *
	 * @return num of files
	 */
	public int getNumFiles() {
		int theNumFiles = 0;
		try {
			theNumFiles = fileCollection.getNumFiles();
		} catch (Exception e) {
			//
		}
		return theNumFiles;
		//return fileCollection.getNumFiles() ;
	}

	/**
	 * Get an iterator for the file collection
	 *
	 * @return iterator
	 */
	public Iterator<IdentificationFile> getFileIterator() {
		return this.fileCollection.getIterator();
	}

	/**
	 * Return the version of the currently loaded signature file
	 *
	 * @return version
	 */
	public int getSigFileVersion() {
		int theVersion = 0;
		try {
			theVersion = Integer.parseInt(sigFile.getVersion());
		} catch (Exception e) {}
		return theVersion;
	}

	public FFSignatureFile getSigFile() {
		return sigFile;
	}

	/**
	 * Return the version of the uk application
	 *
	 * @return string
	 */
	public static String getDROIDVersion() {
		String theVersion = DROID_VERSION;
		//remove number after last .  This is a development version, not to be displayed in About box
		int theLastDot = theVersion.lastIndexOf(".");
		if (theLastDot > -1) {
			if (theVersion.indexOf(".") < theLastDot) {
				theVersion = theVersion.substring(0, theLastDot);
			}
		}
		return theVersion;
	}

	/**
	 * Access to the file collection
	 *
	 * @return the current file collection
	 */
	public FileCollection getFileCollection() {
		return fileCollection;
	}

	/**
	 * checks whether there is a signature file available through the PRONOM web service
	 * which is a later version than the one currently loaded.
	 *
	 * @return boolean
	 */
	public boolean isNewerSigFileAvailable() {
		return isNewerSigFileAvailable(this.getSigFileVersion());
	}

	/**
	 * checks whether there is a signature file available through the PRONOM web service
	 * which is a later version than the specified version number.
	 *
	 * @param currentVersion the version
	 * @return boolean
	 */
	public boolean isNewerSigFileAvailable(int currentVersion) {
		int theLatestVersion;
		try {
			Element versionXML = PronomWebService.sendRequest(configFile.getSigFileURL(), configFile.getProxyHost(), configFile.getProxyPort(), "getSignatureFileVersionV1", null);
			Boolean deprecated = Boolean.parseBoolean(PronomWebService.extractXMLelement(versionXML, "Deprecated").getValue());
			if (deprecated) {
				String message = "A new version of DROID is available.\nPlease visit http://droid.sourceforge.net";
				MessageDisplay.generalInformation(message);
			}
			theLatestVersion = Integer.parseInt(PronomWebService.extractXMLelement(versionXML, "Version").getValue());
			int sigFileLatestVersion = theLatestVersion;
			MessageDisplay.setStatusText("The latest signature file available is V" + sigFileLatestVersion);
		} catch (Exception e) {
			MessageDisplay.generalWarning("Unable to get signature file version from PRONOM website:\n" + e.getMessage());
			return false;
		}
		return (theLatestVersion > currentVersion);
	}

	/**
	 * Download the latest signature file from the PRONOM web service, save it to file
	 * An input flag determines whether or not to load it in to the current instance of uk
	 *
	 * @param theFileName   file where to save signature file
	 * @param isLoadSigFile Flag indicating whether to load the signature file into the current instance of uk
	 */
	public void downloadwwwSigFile(String theFileName, boolean isLoadSigFile) {
		try {
			Element sigFile = PronomWebService.sendRequest(configFile.getSigFileURL(), configFile.getProxyHost(), configFile.getProxyPort(), "getSignatureFileV1", "FFSignatureFile");
			try {
				XMLOutputter outputter = new XMLOutputter();
				java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.FileWriter(theFileName));
				out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				outputter.output(sigFile, out);
				out.close();
				if (isLoadSigFile) {
					try {
						configFile.setDateLastDownload();
						readSigFile(theFileName);
						getFileFormatsDetails();
					} catch (Exception e) {
						MessageDisplay.generalWarning("Unable to read in downloaded signature file ");
					}
				}
			} catch (Exception e) {
				String locaion = theFileName.substring(0,theFileName.lastIndexOf(File.separatorChar));
				MessageDisplay.generalWarning("Unable to save downloaded signature file to location: "+locaion+".\nEither the location does not exist or DROID does not have Write permissions to this location");
			}

		} catch (Exception e) {
			MessageDisplay.generalWarning("Unable to download signature file from the PRONOM web service");
		}
	}

	/**
	 * Download the latest signature file from the PRONOM web service, save it to file
	 * and load it in to the current instance of uk
	 *
	 * @param theFileName file where to save signature file
	 */
	public void downloadwwwSigFile(String theFileName) {
		downloadwwwSigFile(theFileName, true);
	}

	/**
	 * Download the latest signature file from the PRONOM web service, save it to a DEFAULT location
	 * and load it in to the current instance of uk
	 * Saves to same folder as current signature file but as
	 * DROID_Signature_V[X].xml , where [X] is the version number of the signature file
	 */
	public void downloadwwwSigFile() {

		try {
			int theLatestVersion = Integer.parseInt(PronomWebService.sendRequest(configFile.getSigFileURL(), configFile.getProxyHost(), configFile.getProxyPort(), "getSignatureFileVersionV1", "Version").getValue());

			java.io.File currentSigFile = new java.io.File(configFile.getSigFileName());

			String currentPath = "";

			if (currentSigFile != null) {
				currentPath = (currentSigFile.getAbsoluteFile()).getParent() + java.io.File.separator;
			}

			final String newSigFileName = currentPath
			+ "DROID_SignatureFile_V"
			+ theLatestVersion
			+ ".xml";

			downloadwwwSigFile(newSigFileName);
		} catch (Exception e) {
			MessageDisplay.generalWarning("Unable to download signature file from the PRONOM web service");
		}

	}

	/**
	 * Checks whether a new signature file download is due
	 * based on current date and settings in the configuration file
	 *
	 * @return boolean
	 */
	public boolean isSigFileDownloadDue() {

		return configFile.isDownloadDue();
	}

	/**
	 * Returns the date current signature file was created
	 *
	 * @return date signature file was created
	 */
	public String getSignatureFileDate() {
		String theDate = "";
		try {
			theDate = sigFile.getDateCreated();
		} catch (Exception e) {
			//
		}
		if (theDate.equals("")) {
			theDate = "No date given";
		}
		return theDate;
		//return sigFile.getDateCreated() ;
	}

	/**
	 * Returns the file path of the signature file
	 *
	 * @return signature file file path
	 */
	public String getSignatureFileName() {
		return configFile.getSigFileName();
	}

	/**
	 * Get the PUID resolution base URL
	 *
	 * @return PUID resolution
	 */
	public String getPuidResolutionURL() {
		return configFile.getPuidResolution();
	}

	/**
	 * Sets the URL of the signature file webservices
	 *
	 * @param sigFileURL signature file
	 */
	public void setSigFileURL(String sigFileURL) {
		configFile.setSigFileURL(sigFileURL);
	}

	/**
	 * Gets the number of days after which user should be alerted for new signature file
	 *
	 * @return number of days after which user should be alerted for new signature file
	 */
	public int getSigFileCheckFreq() {
		return configFile.getSigFileCheckFreq();
	}

	/**
	 * Updates the configuration parameter which records the interval after which
	 * the signature file should be updated.
	 *
	 * @param theFreq The number of days after which the user will be prompted to check for a newer signature file
	 */
	public void setSigFileCheckFreq(String theFreq) {
		configFile.setSigFileCheckFreq(theFreq);
	}

	/**
	 * updates the DateLastDownload element of the configuration file and updates
	 * the configuration file.  This is to be used whenever the user checks for
	 * a signature file update, but one is not found
	 */
	public void updateDateLastDownload() {
		//set the DateLastDownload to now
		configFile.setDateLastDownload();
		//save to file
		try {
			configFile.saveConfiguration();
		} catch (IOException e) {
			MessageDisplay.generalWarning("Unable to save configuration updates");
		}
	}

	public ConfigFile getConfigFile() {
		return configFile;
	}

	public void setConfigFile(ConfigFile configFile) {
		this.configFile = configFile;
	}
	
	public ConfigFile loadConfigFile(String theFileName) throws Exception{
		this.readConfiguration(theFileName);
		return this.configFile;
	}

	public void setSigFile(FFSignatureFile sigFile) {
		this.sigFile = sigFile;
	}

}