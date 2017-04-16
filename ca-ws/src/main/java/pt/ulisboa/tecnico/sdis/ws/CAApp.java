package pt.ulisboa.tecnico.sdis.ws;

import java.io.File;

public class CAApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length != 2 && args.length != 4) {
			CAEndpointManager.LOGGER.severe("Argument(s) missing!");
			CAEndpointManager.LOGGER.severe("Usage: java " + CAApp.class.getName()
					+ " wsURL certificatesDirectory OR uddiURL wsName wsURL certificatesDirectory");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		File certFolder = null;

		// Create server implementation object, according to options
		CAEndpointManager endpoint = null;
		if (args.length == 2) {
			wsURL = args[0];
			certFolder = new File(args[1]);
			endpoint = new CAEndpointManager(wsURL, certFolder);
		} else if (args.length == 4) {
			uddiURL = args[0];
			wsName = args[1];
			wsURL = args[2];
			certFolder = new File(args[3]);
			endpoint = new CAEndpointManager(uddiURL, wsName, wsURL, certFolder);
		}

		try {
			endpoint.start();
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}

	}

}
