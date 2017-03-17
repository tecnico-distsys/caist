package pt.ulisboa.tecnico.sdis.ws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;

public class CAUtils {

	/** path for the directory containing all the certificates */
	private static String certificatesDirectory = null;

	/**
	 * Returns the public key from a certificate
	 * 
	 * @param certificate
	 * @return
	 */
	public static PublicKey getPublicKeyFromCertificate(Certificate certificate) {
		if (certificate == null) {
			return null;
		}
		return certificate.getPublicKey();
	}

	/**
	 * Reads a certificate from a file
	 * 
	 * @param certificateName:
	 *            Should be in this format: u01_Supplier1 or u01-Mediator
	 * @return
	 */
	public static String getCertificate(String certificateName) {
		if (!certificateName.contains("_")) {
			return null;
		}
		String[] nameParts = certificateName.split("_");
		if (nameParts.length < 2) {
			return null;
		}
		String folder = nameParts[0];
		// String file = nameParts[1];

		String certificatesDirectory = getCertificatesDirectory();

		String certificateFilePath = certificatesDirectory + "/" + folder + "/" + certificateName + ".cer";
		return readCertificateFile(certificateFilePath);
	}

	public static String getCACertificate() {
		return readCertificateFile(getCaCertificateFilePath());
	}

	public static String getCertificatesDirectory() {
		if (certificatesDirectory == null) {
			ClassLoader classLoader = CAUtils.class.getClassLoader();
			certificatesDirectory = classLoader.getResource("certificates").getFile();
		}

		return certificatesDirectory;

	}

	public static String getCaCertificateFilePath() {
		return getCertificatesDirectory() + "/" + "ca/ca-certificate.pem.txt";
	}

	public static void setCertificatesDirectory(String newCertificatesDirectory) {
		certificatesDirectory = newCertificatesDirectory;
	}

	/**
	 * Reads a certificate from a file
	 * 
	 * @param certificateName:
	 *            Should be in this format: u01-fornecedor-1 or u01-mediador
	 * @return
	 * @throws Exception
	 */
	public static String readCertificateFile(String filePathString) {
		File file = null;

		file = new File(filePathString);

		if (!file.exists()) {
			System.err.println("Certificate file <" + filePathString + "> not found.");
			return null;
		}

		String certificateString = "";
		Path filePath = Paths.get(filePathString);
		try {
			List<String> fileLines = Files.readAllLines(filePath);
			for (String line : fileLines) {
				certificateString += line + "\n";
			}

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		return certificateString;
	}


}
