package pt.ulisboa.tecnico.sdis.ws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.ws.CA")
public class CAPortImpl implements CA {

	/** Certificates folder. */
	private File certFolder;

	/** Constructor from folder path. */
	public CAPortImpl(String certFolderPath) {
		this(new File(certFolderPath));
	}

	/** Constructor from folder object. */
	public CAPortImpl(File certFolder) {
		this.certFolder = certFolder;

		// Check certificates folder and its contents
		if (!certFolder.isDirectory()) {
			String message = "Cannot find certificates folder: " + certFolder;
			CAEndpointManager.LOGGER.severe(message);
			throw new IllegalArgumentException(message);
		} else {
			String[] filesInCertificateFolder = certFolder.list();
			CAEndpointManager.LOGGER
					.info("Found " + filesInCertificateFolder.length + " items in " + certFolder + ".");
		}
	}

	/**
	 * Reads a certificate from a file
	 * 
	 * @param certificateName
	 *            should be in this format: CXX_Service1
	 * @return the certificate
	 */
	@Override
	public String getCertificate(String certificateName) {
		// check null
		if (certificateName == null) {
			CAEndpointManager.LOGGER.fine("Received request for null certificate name.");
			return null;
		}

		// sanitize name: replace any character that is not a number, letter or
		// underscore with nothing
		certificateName = certificateName.replaceAll("\\W+", "");
		// trim name: remove whitespace at beginning and at the end of the
		// string
		certificateName = certificateName.trim();

		CAEndpointManager.LOGGER.fine("Certificate name: " + certificateName);

		// check empty
		if (certificateName.length() == 0) {
			CAEndpointManager.LOGGER.fine("Received request for empty certificate name.");
			return null;
		}

		// certificate name convention specific check
		if (!certificateName.contains("_")) {
			CAEndpointManager.LOGGER.fine("Missing _ separator.");
			return null;
		}
		String[] nameParts = certificateName.split("_");
		if (nameParts.length < 2) {
			CAEndpointManager.LOGGER.fine("Missing two parts separated by _.");
			return null;
		}

		// folder name is the group name: CXX
		String folderName = nameParts[0];
		// file name still includes the group name CXX and ends with .cer
		String fileName = certificateName + ".cer";

		File certSubFolder = new File(certFolder, folderName);
		if (!certSubFolder.exists() || !certSubFolder.isDirectory()) {
			CAEndpointManager.LOGGER.fine("Subfolder does not exist or is not a folder.");
			CAEndpointManager.LOGGER.fine("Try with lowercase.");
			certSubFolder = new File(certFolder, folderName.toLowerCase());
		}
		if (!certSubFolder.exists() || !certSubFolder.isDirectory()) {
			CAEndpointManager.LOGGER.fine("Subfolder does not exist or is not a folder.");
			return null;
		}
		
		File certFile = new File(certSubFolder, fileName);
		if (!certFile.exists() || certFile.isDirectory()) {
			CAEndpointManager.LOGGER.fine("File does not exist or is a folder.");
			return null;
		}

		try {
			return readFile(certFile);
		} catch (IOException e) {
			CAEndpointManager.LOGGER.fine("Caught exception reading file " + e);
			return null;
		}
	}

	// readFile credits: http://stackoverflow.com/q/326390/129497
	private String readFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		final String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

}
