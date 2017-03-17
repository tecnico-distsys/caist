package pt.ist.certlib;


//provides helper methods to print byte[]
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;

import javax.crypto.Cipher;

/**
 * Generate a digital signature by performing all the steps separately (for
 * learning purposes)
 */
public class DigitalSignatureStepByStep {

	public static void main(String[] args) throws Exception {

		// check args and get plaintext
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

		// generate RSA KeyPair
		KeyPair key = generate();

		// make digital signature
		System.out.println("Signing ...");
		byte[] cipherDigest = makeDigitalSignature(plainBytes, key);

		// verify the signature
		System.out.println("Verifying ...");
		boolean result = verifyDigitalSignature(cipherDigest, plainBytes, key);
		System.out.println("Signature is " + (result ? "right" : "wrong"));

		// data modification ...
		plainBytes[3] = 12;
		System.out.println("Tampered bytes: (look closely around the 7th hex character)");
		System.out.println(printHexBinary(plainBytes));

		// verify the signature
		System.out.println("Verifying ...");
		result = verifyDigitalSignature(cipherDigest, plainBytes, key);
		System.out.println("Signature is " + (result ? "right" : "wrong"));
	}

	/** auxiliary method to generate KeyPair */
	public static KeyPair generate() throws Exception {
		// generate an RSA key pair
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair key = keyGen.generateKeyPair();

		return key;
	}

	/** auxiliary method to calculate digest from text and cipher it */
	public static byte[] makeDigitalSignature(byte[] bytes, KeyPair keyPair) throws Exception {

		// get a message digest object using the specified algorithm
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

		// calculate the digest and print it out
		messageDigest.update(bytes);
		byte[] digest = messageDigest.digest();
		System.out.println("Digest:");
		System.out.println(printHexBinary(digest));

		// get an RSA cipher object
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		// encrypt the plaintext using the private key
		cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
		byte[] cipherDigest = cipher.doFinal(digest);

		System.out.println("Cipher digest:");
		System.out.println(printHexBinary(cipherDigest));

		return cipherDigest;
	}

	/**
	 * auxiliary method to calculate new digest from text and compare it to the
	 * to deciphered digest
	 */
	public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] text, KeyPair keyPair) throws Exception {

		// get a message digest object using the SHA-1 algorithm
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

		// calculate the digest and print it out
		messageDigest.update(text);
		byte[] digest = messageDigest.digest();
		System.out.println("New digest:");
		System.out.println(printHexBinary(digest));

		// get an RSA cipher object
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		// decrypt the ciphered digest using the public key
		cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
		byte[] decipheredDigest = cipher.doFinal(cipherDigest);
		System.out.println("Deciphered digest:");
		System.out.println(printHexBinary(decipheredDigest));

		// compare digests
		if (digest.length != decipheredDigest.length)
			return false;

		for (int i = 0; i < digest.length; i++)
			if (digest[i] != decipheredDigest[i])
				return false;
		return true;
	}

}
