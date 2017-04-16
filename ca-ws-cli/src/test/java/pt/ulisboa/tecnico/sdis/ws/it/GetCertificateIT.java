package pt.ulisboa.tecnico.sdis.ws.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.junit.Before;
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

	@Before
	public void setUp() {

	}

	/**
	 * Check that CA correctly returns a known certificate.
	 * @throws CertificateException 
	 */
	@Test
	public void validateGetExistingCertificate() throws CertificateException {
		String certificateString = CLIENT.getCertificate("TESTE_Mediator");
		assertNotNull(certificateString);
		Certificate certificate = CertUtil.getX509CertificateFromPEMString(certificateString);
		assertNotNull(certificate);
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
