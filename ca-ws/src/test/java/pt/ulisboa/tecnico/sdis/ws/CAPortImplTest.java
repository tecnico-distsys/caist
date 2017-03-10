package pt.ulisboa.tecnico.sdis.ws;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.cert.Certificate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.ws.CAPortImpl;
import pt.ulisboa.tecnico.sdis.ws.CAUtils;

/**
 * Unit Test suite The purpose of this class is to test CalcPort locally.
 */
public class CAPortImplTest {

	// members

	private CAPortImpl localPort;

	// initialization and clean-up for each test

	@Before
	public void setUp() {
		localPort = new CAPortImpl();
	}

	@After
	public void tearDown() {
		localPort = null;
	}

	@Test
	public void testGetCertificate() throws Exception {
		byte[] certificateBytes = localPort.getCertificate("U01_Supplier1");

		Certificate certificate = CAUtils.getCertificateFromBytes(certificateBytes);
		boolean isCertificateNull = certificate == null;
		assertFalse(isCertificateNull);

	}

	// /**
	// * O objectivo deste teste é verificar se a CA confirma que
	// u01-fornecedor-1
	// * é um certificado válido.
	// *
	// * @throws Exception
	// */
	// @Test
	// public void testisValidCertificateValid() throws Exception {
	// Certificate validCertificate =
	// CAUtils.readCertificateFile("src/test/resources/u01/u01-fornecedor-1.cer");
	// assertTrue(localPort.isCertificateValid(validCertificate.getEncoded()));
	// }
	//
	// /**
	// * O objectivo deste teste é verificar se a CA confirma que
	// u02-fornecedor-1
	// * é um certificado válido. (não é porque foi assinado por outra
	// entidade).
	// *
	// * @throws Exception
	// */
	// @Test
	// public void testisValidCertificateInvalid() throws Exception {
	// Certificate validCertificate =
	// CAUtils.readCertificateFile("src/test/resources/u02/u02-fornecedor-1.cer");
	// assertFalse(localPort.isCertificateValid(validCertificate.getEncoded()));
	// }

}
