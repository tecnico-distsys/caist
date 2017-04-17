package pt.ulisboa.tecnico.sdis.ws.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.cert.CertUtil;

/**
 * Test suite
 */
public class GetCertificateIT extends BaseIT {

	/**
	 * This test suite requires that the CA is running with a certificates
	 * folder containing TESTE certificates.
	 */

	private static Certificate caCertificate;

	@BeforeClass
	public static void oneTimeSetUp() throws FileNotFoundException, CertificateException {
		caCertificate = CertUtil.getX509CertificateFromFile("../ca-ws/src/test/resources/ca/ca-certificate.pem.txt");
	}

	/**
	 * Check that CA correctly returns a known certificate.
	 * 
	 * @throws CertificateException
	 */
	@Test
	public void validateGetExistingCertificate() throws CertificateException {
		String certificateString = CLIENT.getCertificate("TESTE_Mediator");
		assertNotNull(certificateString);
		Certificate certificate = CertUtil.getX509CertificateFromPEMString(certificateString);
		assertNotNull(certificate);
		assertTrue(CertUtil.verifySignedCertificate(certificate, caCertificate));
	}

	/**
	 * Check that CA will return a null for a non existing certificate.
	 */
	@Test
	public void validateGetInexistingCertificate() {
		assertNull(CLIENT.getCertificate("RogueOne"));
		// :)
	}

}
