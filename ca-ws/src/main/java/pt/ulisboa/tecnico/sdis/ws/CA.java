package pt.ulisboa.tecnico.sdis.ws;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface CA {

	@WebResult(name="certificateBytes") String getCertificate(@WebParam(name="certificateName") String certificateName);

}
