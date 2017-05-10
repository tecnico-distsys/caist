package pt.ulisboa.tecnico.sdis.cert;

import static org.junit.Assert.assertNotNull;

import java.security.KeyStore;
import java.security.PrivateKey;

import org.junit.Test;

/**
 * Test suite for key store methods.
 */
public class KeyStoreTest extends BaseTest {

	final static String KEYSTORE = "example.jks";
	final static String KEYSTORE_PASSWORD = "1nsecure";

	final static String KEY_ALIAS = "example";
	final static String KEY_PASSWORD = "ins3cur3";

	@Test
	public void testGetPrivateKeyFromKeyStoreResource() throws Exception {
		PrivateKey privateKey = CertUtil.getPrivateKeyFromKeyStoreResource(KEYSTORE, KEYSTORE_PASSWORD.toCharArray(),
				KEY_ALIAS, KEY_PASSWORD.toCharArray());
		assertNotNull(privateKey);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetPrivateKeyFromInexistingKeyStoreResource() throws Exception {
		CertUtil.getPrivateKeyFromKeyStoreResource("notthere", KEYSTORE_PASSWORD.toCharArray(),
				KEY_ALIAS, KEY_PASSWORD.toCharArray());
	}

	@Test
	public void testReadKeystoreFromResource() throws Exception {
		KeyStore keyStore = CertUtil.readKeystoreFromResource(KEYSTORE, KEYSTORE_PASSWORD.toCharArray());
		assertNotNull(keyStore);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testReadKeystoreFromInexistingResource() throws Exception {
		CertUtil.readKeystoreFromResource("notthere", KEYSTORE_PASSWORD.toCharArray());
	}

	@Test
	public void testReadKeystoreFromResourceAbsolutePath() throws Exception {
		KeyStore keyStore = CertUtil.readKeystoreFromResource("/" + KEYSTORE, KEYSTORE_PASSWORD.toCharArray());
		assertNotNull(keyStore);
	}
	
}
