package pt.ulisboa.tecnico.sdis.ws;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

/** End point manager */
public class CAEndpointManager {

	public static final Logger LOGGER = Logger.getLogger(CAEndpointManager.class.getName());

	/** UDDI naming server location */
	private String uddiURL = null;
	/** Web Service name */
	private String wsName = null;

	/** Get Web Service UDDI publication name */
	public String getWsName() {
		return wsName;
	}

	/** Web Service location to publish */
	private String wsURL = null;

	/** Port implementation */
	private CAPortImpl portImpl;

	/** Obtain Port implementation */
	public CA getPort() {
		return portImpl;
	}

	/** Web Service end point */
	private Endpoint endpoint = null;
	/** UDDI Naming instance for contacting UDDI server */
	private UDDINaming uddiNaming = null;

	/** Get UDDI Naming instance for contacting UDDI server */
	UDDINaming getUddiNaming() {
		return uddiNaming;
	}

	/** output option **/
	private boolean verbose = true;

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/** constructor with provided UDDI location, WS name, and WS URL */
	public CAEndpointManager(String uddiURL, String wsName, String wsURL, File certFolder) {
		this.uddiURL = uddiURL;
		this.wsName = wsName;
		this.wsURL = wsURL;
		initPortImpl(certFolder);
	}

	/** constructor with provided web service URL */
	public CAEndpointManager(String wsURL, File certFolder) {
		if (wsURL == null)
			throw new NullPointerException("Web Service URL cannot be null!");
		this.wsURL = wsURL;
		initPortImpl(certFolder);
	}

	private void initPortImpl(File certFolder) {
		portImpl = new CAPortImpl(certFolder);
	}

	/* end point management */

	public void start() throws Exception {
		try {
			// publish end point
			endpoint = Endpoint.create(this.portImpl);
			if (verbose) {
				LOGGER.info("Starting " + wsURL);

			}
			endpoint.publish(wsURL);
		} catch (Exception e) {
			endpoint = null;
			if (verbose) {
				LOGGER.severe("Caught exception when starting: " + e.toString());
				e.printStackTrace();
			}
			throw e;
		}
		publishToUDDI();
	}

	public void awaitConnections() {
		if (verbose) {
			CAEndpointManager.LOGGER.info("Awaiting connections");
			CAEndpointManager.LOGGER.info("Press enter to shutdown");
		}
		try {
			System.in.read();
		} catch (IOException e) {
			if (verbose) {
				LOGGER.severe("Caught i/o exception when awaiting requests: " + e.toString());
			}
		}
	}

	public void stop() throws Exception {
		try {
			if (endpoint != null) {
				// stop end point
				endpoint.stop();
				if (verbose) {
					LOGGER.info("Stopped " + wsURL);
				}
			}
		} catch (Exception e) {
			if (verbose) {
				LOGGER.severe("Caught exception when stopping: " + e.toString());
			}
		}
		this.portImpl = null;
		unpublishFromUDDI();
	}

	/* UDDI */

	void publishToUDDI() throws Exception {
		try {
			// publish to UDDI
			if (uddiURL != null) {
				if (verbose) {
					LOGGER.info("Publishing '" + wsName + "' to UDDI at " + uddiURL);
				}
				uddiNaming = new UDDINaming(uddiURL);
				uddiNaming.rebind(wsName, wsURL);
			}
		} catch (Exception e) {
			uddiNaming = null;
			if (verbose) {
				LOGGER.severe("Caught exception when binding to UDDI: " + e.toString());
			}
			throw e;
		}
	}

	void unpublishFromUDDI() {
		try {
			if (uddiNaming != null) {
				// delete from UDDI
				uddiNaming.unbind(wsName);
				if (verbose) {
					LOGGER.info("Unpublished '" + wsName + "' from UDDI%n");
				}
				uddiNaming = null;
			}
		} catch (Exception e) {
			if (verbose) {
				LOGGER.info("Caught exception when unbinding: " + e.toString());
			}
		}
	}

}
