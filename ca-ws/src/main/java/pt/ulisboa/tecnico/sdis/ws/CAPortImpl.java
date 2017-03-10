package pt.ulisboa.tecnico.sdis.ws;

import java.security.cert.CertificateEncodingException;

import javax.jws.WebService;
import java.security.cert.Certificate;

@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.ws.CA")
public class CAPortImpl implements CA {

	public CAPortImpl() {
	
	}

	@Override
	public byte[] getCertificate(String certificateName) {
		if (certificateName == null) {
			return null;
		}
		try {
			if (CAUtils.getCertificate(certificateName) != null) {
				Certificate certificate = CAUtils.getCertificate(certificateName);
				if (certificate == null) {
					CAEndpointManager.LOGGER.info("Received request for certificate '" + certificateName + "' and it does not exist.");	
					return null;
				}

				byte[] certificateBytes = certificate.getEncoded();
				if (certificateBytes == null){
					CAEndpointManager.LOGGER.info("Received request for certificate '" + certificateName + "' and it does not have content.");	
				} else {
					CAEndpointManager.LOGGER.info("Received request for certificate '" + certificateName + "' and it exists.");
				}
				
				return certificateBytes;
			}
		} catch (CertificateEncodingException e) {
			CAEndpointManager.LOGGER.info("Certificate '" + certificateName + "' has an incorrect encoding.");
			//e.printStackTrace();
		}
		return null;
	}

}
