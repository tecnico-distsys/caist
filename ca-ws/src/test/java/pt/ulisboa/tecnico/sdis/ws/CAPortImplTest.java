package pt.ulisboa.tecnico.sdis.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.security.cert.Certificate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.cert.CertUtil;

/**
 * Unit Test suite The purpose of this class is to test CalcPort locally.
 */
public class CAPortImplTest {

	// members

	private CAPortImpl localPort;

	// initialization and clean-up for each test

	@Before
	public void setUp() {
		localPort = new CAPortImpl(new File("./src/test/resources"));
	}

	@After
	public void tearDown() {
		localPort = null;
	}

	@Test
	public void testGetCertificate() throws Exception {
		String certificateString = localPort.getCertificate("CXX_Service1");
		assertNotNull(certificateString);
		Certificate certificate = CertUtil.getX509CertificateFromPEMString(certificateString);
		assertNotNull(certificate);
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

}
