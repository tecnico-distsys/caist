package pt.ulisboa.tecnico.sdis.cert;

import static javax.xml.bind.DatatypeConverter.printHexBinary;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Test;

/**
 * Test suite to show how the Java Security API can be used for digital
 * signatures with X509 certificates. The certificates are generated using the
 * keytool included in the JDK.
 *
 * STEP 1:
 * 
 * To begin, first generate a keystore file with a RSA keypair executing the
 * following command in the shell:
 * 
 * keytool -genkeypair -alias "example" -keyalg RSA -keysize 2048 -keypass
 * "passwd" -validity 90 -storepass "passwd" -keystore keystore.jks -dname
 * "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
 * 
 * -genkeypair: Generates a key pair (a public key and associated private key).
 * Wraps the public key into an X.509 v3 self-signed certificate, which is
 * stored as a single-element certificate chain. This certificate chain and the
 * private key are stored in a new keystore entry identified by alias.
 * 
 * -alias: an alias name for this keypair
 * 
 * -keyalg: specifies the algorithm to be used to generate the key pair, and
 * keysize specifies the size of each key to be generated. sigalg specifies the
 * algorithm that should be used to sign the self-signed certificate; this
 * algorithm must be compatible with keyalg.
 * 
 * -keypass: password for the generated key -storepass: password for the
 * keystore
 * 
 * -keystore: identifies the keystore file in which the keypair will be stored
 * 
 * -dname: (optional) dname specifies the X.500 Distinguished Name to be
 * associated with alias, and is used as the issuer and subject fields in the
 * self-signed certificate. If no distinguished name is provided at the command
 * line, the user will be prompted for one.
 * 
 * -validity: the number of days for which the certificate should be considered
 * valid.
 * 
 * STEP 2:
 * 
 * Execute the following command to export the certificate created in STEP 1 to
 * a .cer file:
 * 
 * keytool -export -keystore keystore.jks -alias example -storepass "passwd"
 * -file example.cer
 * 
 */
public class X509DigitalSignatureTest extends BaseTest {

	final static String CERTIFICATE = "example.cer";

	final static String KEYSTORE = "example.jks";
	final static String KEYSTORE_PASSWORD = "1nsecure";

	final static String KEY_ALIAS = "example";
	final static String KEY_PASSWORD = "ins3cur3";

	/** Plain text to digest. */
	private final String plainText = "This is the plain text!";
	/** Plain text bytes. */
	private final byte[] plainBytes = plainText.getBytes();

	/** Digital signature algorithm. */
	private static final String SIGNATURE_ALGO = "SHA256withRSA";

	/**
	 * Generate a digital signature using the signature object provided by Java.
	 */
	@Test
	public void testSignature() throws Exception {
		System.out.print("TEST ");
		System.out.print(SIGNATURE_ALGO);
		System.out.print(" digital signature");
		System.out.println(" with public key in X509 certificate and private key in JDK keystore");

		System.out.print("Text: ");
		System.out.println(plainText);
		System.out.print("Bytes: ");
		System.out.println(printHexBinary(plainBytes));

		// make digital signature
		System.out.println("Signing ...");
		PrivateKey privateKey = CertUtil.getPrivateKeyFromKeyStoreResource(KEYSTORE, KEYSTORE_PASSWORD.toCharArray(),
				KEY_ALIAS, KEY_PASSWORD.toCharArray());
		byte[] digitalSignature = CertUtil.makeDigitalSignature(SIGNATURE_ALGO, privateKey, plainBytes);
		assertNotNull(digitalSignature);

		// verify the signature
		System.out.println("Verifying ...");
		PublicKey publicKey = CertUtil.getX509CertificateFromResource(CERTIFICATE).getPublicKey();
		boolean result = CertUtil.verifyDigitalSignature(SIGNATURE_ALGO, publicKey, plainBytes, digitalSignature);
		assertTrue(result);

		// data modification ...
		plainBytes[3] = 12;
		System.out.println("Tampered bytes: (look closely around the 7th hex character)");
		System.out.println(printHexBinary(plainBytes));
		System.out.println("      ^");

		// verify the signature
		System.out.println("Verifying ...");
		boolean resultAfterTamper = CertUtil.verifyDigitalSignature(SIGNATURE_ALGO, publicKey, plainBytes,
				digitalSignature);
		assertFalse(resultAfterTamper);
	}

}
