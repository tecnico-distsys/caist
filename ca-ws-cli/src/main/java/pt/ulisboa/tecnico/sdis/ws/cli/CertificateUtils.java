package pt.ulisboa.tecnico.sdis.ws.cli;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class CertificateUtils {

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
	 * Reads a certificate from a file
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Certificate getCertificate(File file) {
		// TODO improve exception handling
		FileInputStream fis;

		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file not found.");
			return null;
		}

		try {

			BufferedInputStream bis = new BufferedInputStream(fis);

			CertificateFactory cf = null;

			cf = CertificateFactory.getInstance("X.509");

			if (bis.available() > 0) {
				Certificate cert = cf.generateCertificate(bis);
				return cert;
				// It is possible to print the content of the certificate file:
				// System.out.println(cert.toString());
			}

			bis.close();

			fis.close();

		} catch (Exception e) {
			System.err.println("Caught exception reading certificate from file.");
			e.printStackTrace(System.err);
		}
		return null;
	}

}
