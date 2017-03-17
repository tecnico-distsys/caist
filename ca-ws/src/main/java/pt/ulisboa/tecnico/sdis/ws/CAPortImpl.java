package pt.ulisboa.tecnico.sdis.ws;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.ws.CA")
public class CAPortImpl implements CA {

	

	public CAPortImpl() {

	}

	@Override
	public String getCertificate(String certificateName) {
		if (certificateName == null) {
			return null;
		}

		if (CAUtils.getCertificate(certificateName) != null) {

			String certificate = CAUtils.getCertificate(certificateName);
			if (certificate == null) {
				CAEndpointManager.LOGGER
						.info("Received request for certificate '" + certificateName + "' and it does not exist.");
				return null;
			}

			return certificate;
		}

		return null;
	}

}
