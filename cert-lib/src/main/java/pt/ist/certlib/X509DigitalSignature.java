package pt.ist.certlib;


import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;

/**
 * This class contains a digital signature example using X509 Certificates
 * generated using keytool from JDK.
 *
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
 * 
 * STEP 2:
 * 
 * Execute the following command to export the certificate created in STEP1 to a
 * .cer file:
 * 
 * keytool -export -keystore keystore.jks -alias example -storepass "passwd"
 * -file example.cer
 * 
 * 
 */
public class X509DigitalSignature {

	final static String CERTIFICATE_FILE = "example.cer";

	final static String KEYSTORE_FILE = "keystore.jks";
	final static String KEYSTORE_PASSWORD = "1nsecure";

	final static String KEY_ALIAS = "example";
	final static String KEY_PASSWORD = "ins3cur3";

	public static void main(String[] args) throws Exception {

		// check arguments and get plain-text
		if (args.length != 1) {
			System.err.println("args: (text)");
			return;
		}
		final String plainText = args[0];
		final byte[] plainBytes = plainText.getBytes();

		System.out.println("Text:");
		System.out.println(plainText);

		System.out.println("Bytes:");
		System.out.println(printHexBinary(plainBytes));

		// make digital signature
		System.out.println("Signing ...");
		byte[] digitalSignature = makeDigitalSignature(plainBytes, getPrivateKeyFromKeystore(KEYSTORE_FILE,
				KEYSTORE_PASSWORD.toCharArray(), KEY_ALIAS, KEY_PASSWORD.toCharArray()));

		System.out.println("Signature Bytes:");
		System.out.println(printHexBinary(digitalSignature));

		Certificate certificate = readCertificateFile(CERTIFICATE_FILE);
		PublicKey publicKey = certificate.getPublicKey();

		// verify the signature
		System.out.println("Verifying ...");
		boolean isValid = verifyDigitalSignature(digitalSignature, plainBytes, publicKey);

		if (isValid) {
			System.out.println("The digital signature is valid");
		} else {
			System.out.println("The digital signature is NOT valid");
		}

		// data modification ...
		plainBytes[3] = 12;
		System.out.println("Tampered bytes: (look closely around the 7th hex character)");
		System.out.println(printHexBinary(plainBytes));

		// again verify the signature
		System.out.println("Verifying again ...");
		isValid = verifyDigitalSignature(digitalSignature, plainBytes, publicKey);

		if (isValid) {
			System.out.println("The digital signature is valid");
		} else {
			System.out.println("The digital signature is NOT valid");
		}
	}

	/**
	 * Returns the public key from a certificate
	 * 
	 * @param certificate
	 * @return
	 */
	public static PublicKey getPublicKeyFromCertificate(Certificate certificate) {
		return certificate.getPublicKey();
	}

	/**
	 * Reads a certificate from a file
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Certificate readCertificateFile(String certificateFilePath) throws Exception {
		FileInputStream fis;

		try {
			fis = new FileInputStream(certificateFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + certificateFilePath + "> not fount.");
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		if (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			return cert;
			// It is possible to print the content of the certificate file:
			// System.out.println(cert.toString());
		}
		bis.close();
		fis.close();
		return null;
	}

	/**
	 * Reads a collections of certificates from a file
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Collection<Certificate> readCertificateList(String certificateFilePath) throws Exception {
		FileInputStream fis;

		try {
			fis = new FileInputStream(certificateFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + certificateFilePath + "> not fount.");
			return null;
		}
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		@SuppressWarnings("unchecked")
		Collection<Certificate> c = (Collection<Certificate>) cf.generateCertificates(fis);
		fis.close();
		return c;

	}

	/**
	 * Reads a PrivateKey from a key-store
	 * 
	 * @return The PrivateKey
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromKeystore(String keyStoreFilePath, char[] keyStorePassword,
			String keyAlias, char[] keyPassword) throws Exception {

		KeyStore keystore = readKeystoreFile(keyStoreFilePath, keyStorePassword);
		PrivateKey key = (PrivateKey) keystore.getKey(keyAlias, keyPassword);

		return key;
	}

	/**
	 * Reads a KeyStore from a file
	 * 
	 * @return The read KeyStore
	 * @throws Exception
	 */
	public static KeyStore readKeystoreFile(String keyStoreFilePath, char[] keyStorePassword) throws Exception {
		FileInputStream fis;
		try {
			fis = new FileInputStream(keyStoreFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Keystore file <" + keyStoreFilePath + "> not fount.");
			return null;
		}
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(fis, keyStorePassword);
		return keystore;
	}

	/** auxiliary method to calculate digest from text and cipher it */
	public static byte[] makeDigitalSignature(byte[] bytes, PrivateKey privateKey) throws Exception {

		// get a signature object using the SHA-1 and RSA combo
		// and sign the plain-text with the private key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(privateKey);
		sig.update(bytes);
		byte[] signature = sig.sign();

		return signature;
	}

	/**
	 * auxiliary method to calculate new digest from text and compare it to the
	 * to deciphered digest
	 */
	public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey publicKey)
			throws Exception {

		// verify the signature with the public key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initVerify(publicKey);
		sig.update(bytes);
		try {
			return sig.verify(cipherDigest);
		} catch (SignatureException se) {
			System.err.println("Caught exception while verifying signature " + se);
			return false;
		}
	}

}
