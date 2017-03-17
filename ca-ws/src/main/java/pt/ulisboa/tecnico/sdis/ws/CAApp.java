package pt.ulisboa.tecnico.sdis.ws;

import java.io.File;

import pt.ist.certlib.CAUtils;

public class CAApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length != 2 && args.length != 4) {
			
			CAEndpointManager.LOGGER.severe("Argument(s) missing!");
			CAEndpointManager.LOGGER.severe("Usage: java " + CAApp.class.getName() + " wsURL certificatesDirectory OR uddiURL wsName wsURL certificatesDirectory");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		

		// Create server implementation object, according to options
		CAEndpointManager endpoint = null;
		if (args.length == 2) {
			wsURL = args[0];
			endpoint = new CAEndpointManager(wsURL);
			CAUtils.setCertificatesDirectory(args[1]);
		} else if (args.length == 4) {
			uddiURL = args[0];
			wsName = args[1];
			wsURL = args[2];
			CAUtils.setCertificatesDirectory(args[3]);
			endpoint = new CAEndpointManager(uddiURL, wsName, wsURL);
			endpoint.setVerbose(true);
		}

		// Checks if the certificates directory exists
		if(!new File(CAUtils.getCertificatesDirectory()).isDirectory()) {
			CAEndpointManager.LOGGER.severe("Cannot find certificates directory - " + CAUtils.getCertificatesDirectory());
			System.exit(-1	);
		}else{
			String[] filesInCertificateFolder = new File(CAUtils.getCertificatesDirectory()).list();
			
			
			CAEndpointManager.LOGGER.info("Found " + filesInCertificateFolder.length + " certificates in " +  CAUtils.getCertificatesDirectory() + ".");
			
		}
		
		
		try {
			endpoint.start();
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}

	}

}
