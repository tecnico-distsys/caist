package pt.ulisboa.tecnico.sdis.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.registry.JAXRException;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import pt.ulisboa.tecnico.sdis.ws.cli.CAClient;
import pt.ulisboa.tecnico.sdis.ws.cli.CAClientException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

/**
 * Test suite
 */
public class CAClientIT {

	// static members

	private static final String uddiURL = "http://mockhost:9090";
	private static final String wsName = "CreditCard";
	private static final String wsURL = "http://mockhost:8080/cc-ws/endpoint";

	// tests
	// assertEquals(expected, actual);

	@Test
	public void testMockUddi(@Mocked final UDDINaming uddiNaming) throws Exception {

		// Preparation code not specific to JMockit, if any.

		// an "expectation block"
		// One or more invocations to mocked types, causing expectations to be
		// recorded.
		new Expectations() {
			{
				new UDDINaming(uddiURL);
				uddiNaming.lookup(wsName);
				result = wsURL;
			}
		};

		// Unit under test is exercised.
		new CAClient(uddiURL, wsName);

		// a "verification block"
		// One or more invocations to mocked types, causing expectations to be
		// verified.
		new Verifications() {
			{
				// Verifies that zero or one invocations occurred, with the
				// specified argument value:
				new UDDINaming(uddiURL);
				uddiNaming.lookup(wsName);
				maxTimes = 1;
				uddiNaming.unbind(null);
				maxTimes = 0;
				uddiNaming.bind(null, null);
				maxTimes = 0;
				uddiNaming.rebind(null, null);
				maxTimes = 0;
			}
		};

		// Additional verification code, if any, either here or before the
		// verification block.
	}

	@Test
	public void testMockUddiNameNotFound(@Mocked final UDDINaming uddiNaming) throws Exception {

		// Preparation code not specific to JMockit, if any.

		// an "expectation block"
		// One or more invocations to mocked types, causing expectations to be
		// recorded.
		new Expectations() {
			{
				new UDDINaming(uddiURL);
				uddiNaming.lookup(wsName);
				result = null;
			}
		};

		// Unit under test is exercised.
		try {
			new CAClient(uddiURL, wsName);
			fail();

		} catch (CAClientException e) {
			final String expectedMessage = String.format("Service with name %s not found on UDDI at %s", wsName,
					uddiURL);
			assertEquals(expectedMessage, e.getMessage());
		}

		// a "verification block"
		// One or more invocations to mocked types, causing expectations to be
		// verified.
		new Verifications() {
			{
				// Verifies that zero or one invocations occurred, with the
				// specified argument value:
				new UDDINaming(uddiURL);
				uddiNaming.lookup(wsName);
				maxTimes = 1;
			}
		};

		// Additional verification code, if any, either here or before the
		// verification block.
	}

	@Test
	public void testMockUddiServerNotFound(@Mocked final UDDINaming uddiNaming) throws Exception {

		// Preparation code not specific to JMockit, if any.

		// an "expectation block"
		// One or more invocations to mocked types, causing expectations to be
		// recorded.
		new Expectations() {
			{
				new UDDINaming(uddiURL);
				result = new JAXRException("created for testing");
			}
		};

		// Unit under test is exercised.
		try {
			new CAClient(uddiURL, wsName);
			fail();

		} catch (CAClientException e) {
			assertTrue(e.getCause() instanceof JAXRException);
			final String expectedMessage = String.format("Client failed lookup on UDDI at %s!", uddiURL);
			assertEquals(expectedMessage, e.getMessage());
		}

		// a "verification block"
		// One or more invocations to mocked types, causing expectations to be
		// verified.
		new Verifications() {
			{
				// Verifies that zero or one invocations occurred, with the
				// specified argument value:
				new UDDINaming(uddiURL);
			}
		};

		// Additional verification code, if any, either here or before the
		// verification block.
	}

}
