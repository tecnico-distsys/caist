package pt.ulisboa.tecnico.sdis.ws;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.cert.CertUtil;

/**
 * Unit Test suite The purpose of this class is to test CalcPort locally.
 */
public class CAPortImplTest {

	// members

	private static CAPortImpl localPort;
	private static Certificate caCertificate;

	// initialization and clean-up for each test

	@BeforeClass
	public static void oneTimeSetUp() throws FileNotFoundException, CertificateException {
		localPort = new CAPortImpl("./src/test/resources");
		caCertificate = CertUtil.getX509CertificateFromFile("./src/test/resources/ca/ca-certificate.pem.txt");
	}

	@AfterClass
	public static void oneTimeTearDown() {
		localPort = null;
		caCertificate = null;
	}

	@Test
	public void testGetCertificate() throws Exception {
		String certificateString = localPort.getCertificate("CXX_Service1");
		assertNotNull(certificateString);
		Certificate certificate = CertUtil.getX509CertificateFromPEMString(certificateString);
		assertNotNull(certificate);
		assertTrue(CertUtil.verifySignedCertificate(certificate, caCertificate));
	}

	@Test
	public void testGetCertificateWrongCA() throws Exception {
		String certificateString = localPort.getCertificate("CXX_Service1");
		assertNotNull(certificateString);
		Certificate certificate = CertUtil.getX509CertificateFromPEMString(certificateString);
		assertNotNull(certificate);
		// use incorrect CA certificate
		Certificate notCACertificate = certificate;
		assertFalse(CertUtil.verifySignedCertificate(certificate, notCACertificate));
	}

	@Test
	public void testGetCertificateDoesNotExist() throws Exception {
		String certificateString = localPort.getCertificate("CXX_Service9");
		assertNull(certificateString);
	}

	@Test
	public void testGetCertificateNull() throws Exception {
		String certificateString = localPort.getCertificate(null);
		assertNull(certificateString);
	}

	@Test
	public void testGetCertificateEmpty() throws Exception {
		String certificateString = localPort.getCertificate("");
		assertNull(certificateString);
	}

	@Test
	public void testGetCertificateWrongName() throws Exception {
		String certificateString = localPort.getCertificate("DoesNotExist");
		assertNull(certificateString);
	}

	@Test
	public void testGetCertificateDirTraversal() throws Exception {
		String certificateString = localPort.getCertificate("./src/test/resources/CXX_Service1.cer");
		assertNull(certificateString);
	}

	@Test
	public void testGetCertificateParentDirTraversal() throws Exception {
		String certificateString = localPort.getCertificate("../ca-ws/src/test/resources/CXX_Service1.cer");
		assertNull(certificateString);
	}

	@Test
	public void testGetCertificateLowercase() throws Exception {
		String certificateString = localPort.getCertificate("TESTE_Mediator");
		assertNotNull(certificateString);
		Certificate certificate = CertUtil.getX509CertificateFromPEMString(certificateString);
		assertNotNull(certificate);
		assertTrue(CertUtil.verifySignedCertificate(certificate, caCertificate));
	}

}
