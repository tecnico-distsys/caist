package pt.ulisboa.tecnico.sdis.cert;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.security.cert.Certificate;

import org.junit.Test;

/**
 * Test suite to show how the Java Security API can be used for X509 certificate
 * verification.
 * 
 * The certificates should be generated with the script gen_keys.sh
 * 
 * '$ ./script/gen-ca-servers-keys.sh example'
 * 
 */
public class X509CertificateCheckTest extends BaseTest {

	final static String CA_CERTIFICATE_FILE = "ca-certificate.pem.txt";
	final static String CERTIFICATE_FILE = "example.cer";

	/**
	 * Check if the certificate was properly signed by CA.
	 */
	@Test
	public void testCertificateCheck() throws Exception {
		System.out.println("TEST X509 certificate signature check");

		Certificate certificate = CertUtil.getX509CertificateFromFile(new File(folder, CERTIFICATE_FILE));
		Certificate caCertificate = CertUtil.getX509CertificateFromFile(new File(folder, CA_CERTIFICATE_FILE));

		boolean result = CertUtil.verifySignedCertificate(certificate, caCertificate);
		assertTrue(result);
	}

	/**
	 * Check if the the verification detects a wrong CA certificate.
	 */
	@Test
	public void testCertificateCheck2() throws Exception {
		System.out.println("TEST X509 certificate signature check with wrong certificate");

		Certificate certificate = CertUtil.getX509CertificateFromFile(new File(folder, CERTIFICATE_FILE));

		boolean result = CertUtil.verifySignedCertificate(certificate, certificate);
		assertFalse(result);
	}

}
