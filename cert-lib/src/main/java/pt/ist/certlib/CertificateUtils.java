package pt.ist.certlib;

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

public class CertificateUtils {

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
			ClassLoader classLoader = CertificateUtils.class.getClassLoader();
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

	/** auxiliary method to calculate digest from text and cipher it */
	public static byte[] makeDigitalSignature(byte[] bytes, PrivateKey privateKey) throws Exception {

		// get a signature object using the SHA-1 and RSA combo
		// and sign the plain-text with the private key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(privateKey);
		sig.update(bytes);
		byte[] signature = sig.sign();

		return signature;
	}

	/**
	 * auxiliary method to calculate new digest from text and compare it to the
	 * to deciphered digest
	 */
	public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey publicKey)
			throws Exception {

		// verify the signature with the public key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initVerify(publicKey);
		sig.update(bytes);
		try {
			return sig.verify(cipherDigest);
		} catch (SignatureException se) {
			System.err.println("Caught exception while verifying signature " + se);
			return false;
		}
	}

	/**
	 * Converts a byte array to a Certificate object. Returns null if the bytes
	 * do not correspond to a certificate.
	 * 
	 * @param bytes
	 *            the byte array to convert
	 * @return the certificate
	 */
	public static Certificate getCertificateFromBytes(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		try {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			InputStream in = new ByteArrayInputStream(bytes);
			Certificate cert = certFactory.generateCertificate(in);
			return cert;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Returns a Certificate object given a string with a certificate in the PEM
	 * format
	 * 
	 * @param certificateString
	 *            the String with the certificate
	 * @return the Certificate
	 */
	public static Certificate getCertificateFromString(String certificateString) {
		InputStream is = new ByteArrayInputStream(certificateString.getBytes(StandardCharsets.UTF_8));
		CertificateFactory cf;
		try {
			cf = CertificateFactory.getInstance("X.509");

			Certificate certificate;

			certificate = cf.generateCertificate(is);
			return certificate;
		} catch (CertificateException e) {
			e.printStackTrace();
			e.printStackTrace();
		}
		return null;

	}

}
