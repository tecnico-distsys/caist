package pt.ulisboa.tecnico.sdis.ws.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.security.cert.Certificate;

import org.junit.Before;
import org.junit.Test;

import pt.ist.certlib.CertificateUtils;

/**
 * Test suite
 */
public class GetCertificateIT extends BaseIT {

	/**
	 * This test suite requires that the CA is running with a certificates
	 * folder containing TESTE certificates.
	 */

	@Before
	public void setUp() {

	}

	/**
	 * Check that CA correctly returns a known certificate.
	 */
	@Test
	public void validateGetExistingCertificate() {
		String certificateString = CLIENT.getCertificate("TESTE_Mediator");
		assertNotNull(certificateString);
		assertTrue(CertificateUtils.getCertificateFromString(certificateString) instanceof Certificate);
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
