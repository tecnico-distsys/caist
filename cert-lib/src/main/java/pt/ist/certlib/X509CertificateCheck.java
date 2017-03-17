package pt.ist.certlib;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Esta classe contém os métodos necessários para validar certificados (X509)
 * de chave pública. Os certificados devem ser gerados recorrendo ao script
 * gen_keys.sh Executando o comando: '$ ./script/gen-ca-servers-keys.sh example'
 * 
 */
public class X509CertificateCheck {

	final static String CA_CERTIFICATE_FILE = "keys/ca/ca-certificate.pem.txt";
	final static String CERTIFICATE_FILE = "keys/example/example.cer";

	public static void main(String[] args) throws Exception {

		File f = new File(".");
		System.out.println(f.getAbsolutePath());

		Certificate certificate = readCertificateFile(CERTIFICATE_FILE);
		Certificate caCertificate = readCertificateFile(CA_CERTIFICATE_FILE);
		PublicKey caPublicKey = caCertificate.getPublicKey();

		if (verifySignedCertificate(certificate, caPublicKey)) {
			System.out.println("The signed certificate is valid");
		} else {
			System.err.println("The signed certificate is not valid");
		}

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
			System.err.println("Certificate file <" + certificateFilePath + "> not found.");
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
	 * Verifica se um certificado foi devidamente assinado pela CA
	 * 
	 * @param certificate
	 *            certificado a ser verificado
	 * @param caPublicKey
	 *            certificado da CA
	 * @return true se foi devidamente assinado
	 */
	public static boolean verifySignedCertificate(Certificate certificate, PublicKey caPublicKey) {
		try {
			certificate.verify(caPublicKey);
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			// O método Certifecate.verify() não retorna qualquer valor (void).
			// Quando um certificado é inválido, isto é, não foi devidamente
			// assinado pela CA
			// é lançada uma excepção: java.security.SignatureException:
			// Signature does not match.
			// também são lançadas excepções caso o certificado esteja num
			// formato incorrecto ou tenha uma
			// chave inválida.

			return false;
		}
		return true;
	}

}
