package pt.ist.certlib;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;



public class CAUtils {

	/** path for the directory containing all the certificates */
	private static String certificatesDirectory = null;

	/** path for the certificate file of the CA */
	private static String caCertificateFile = null;

	/**
	 * public key of the CA. It is read from a file only once and, after that,
	 * it is kept in memory
	 */
	private static PublicKey publicKey = null;

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
	public static Certificate getCertificate(String certificateName) {
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

	public static Certificate getCACertificate() {
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
	
	
	public static void setCertificatesDirectory(String newCertificatesDirectory){
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
	public static Certificate readCertificateFile(String filePath) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + filePath + "> not found.");
			return null;
		}

		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(fis);

			CertificateFactory cf = null;
			cf = CertificateFactory.getInstance("X.509");

			if (bis.available() > 0) {
				Certificate cert = cf.generateCertificate(bis);
				return cert;
				// It is possible to print the content of the certificate file:
				// System.out.println(cert.toString());
			}

		} catch (Exception e) {
			System.err.println("Caught exception when reading certificate file <" + filePath + ">.");
			e.printStackTrace(System.err);
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (fis != null)
					fis.close();
			} catch (IOException ioe) {
				// ignore exception
			}
		}
		return null;
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
	 * Verifica se um certificado foi devidamente assinado pela CA
	 * 
	 * @param certificate
	 *            certificado a ser verificado
	 * @param caPublicKey
	 *            certificado da CA
	 * @return true se foi devidamente assinado
	 */
	public static boolean verifySignedCertificate(Certificate certificate) {
		if (certificate == null) {
			return false;
		}
		if (publicKey == null) {
			publicKey = getPublicKeyFromCertificate(getCACertificate());
		}

		try {
			certificate.verify(publicKey);
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			// O método Certifecate.verify() não retorna qualquer valor (void).
			// Quando um certificado é inválido, isto é, não foi devidamente
			// assinado pela CA
			// é lançada uma excepção: java.security.SignatureException:
			// Signature does not match.
			// também são lançadas excepções caso o certificado esteja num
			// formato incorrecto ou tenha uma
			// chave inválida.

			return false;
		}
		return true;
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
}
