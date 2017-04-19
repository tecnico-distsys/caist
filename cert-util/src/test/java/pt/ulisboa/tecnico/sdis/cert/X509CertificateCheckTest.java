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

	final static String CA_CERTIFICATE = "ca.cer";
	final static String CERTIFICATE = "example.cer";

	/**
	 * Check if the certificate was properly signed by CA. Certificates are
	 * loaded as files.
	 */
	@Test
	public void testCertificateCheck() throws Exception {
		Certificate certificate = CertUtil.getX509CertificateFromResource(CERTIFICATE);
		Certificate caCertificate = CertUtil.getX509CertificateFromResource(CA_CERTIFICATE);

		boolean result = CertUtil.verifySignedCertificate(certificate, caCertificate);
		assertTrue(result);
	}

	/**
	 * Check if the the verification detects a wrong CA certificate.
	 */
	@Test
	public void testCertificateFromWrongCA() throws Exception {
		Certificate certificate = CertUtil.getX509CertificateFromResource(CERTIFICATE);
		// using wrong certificate here
		Certificate caCertificate = CertUtil.getX509CertificateFromResource(CERTIFICATE);

		boolean result = CertUtil.verifySignedCertificate(certificate, caCertificate);
		assertFalse(result);
	}

}
