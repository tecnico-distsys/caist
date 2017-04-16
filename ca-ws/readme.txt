This is Certificate Authority Web Service implemented in Java.

Setup before running CA-WS:
  1 - Create folder: "target/certificates/"
  2 - Create folder: "target/certificates/ca/"
  3 - Copy and paste CA certificate to this path: "target/certificates/ca/ca-certificate.pem.txt"
  4 - Copy users' certificates to "target/certificates/" (user T03 will have his certificates in target/certificates/T03)

The service is defined by the Java code with annotations
(code-first approach, also called bottom-up approach).

The service runs in a stand-alone HTTP server.


Instructions using Maven:
------------------------

To compile:
  mvn compile

To run using exec plugin with default arguments:
  mvn exec:java

To run with UDDI (uddiURL wsName wsURL):
  mvn exec:java -Pexec.args="http://localhost:9090 CA http://localhost:8080/ca-ws/endpoint target/certificates/"

To run without UDDI (wsURL):
  mvn exec:java -Pexec.args="http://localhost:8080/ca-ws/endpoint target/certificates/"


When running, the web service awaits connections from clients.
You can check if the service is running using your web browser 
to see the generated WSDL file:

    http://localhost:8080/ca-ws/endpoint?WSDL

This address is defined in CAApp when the publish() method is called.

To call the service you will need a web service client,
including code generated from the WSDL.


To configure the project in Eclipse:
-----------------------------------

'File', 'Import...', 'Maven'-'Existing Maven Projects'.
'Browse' to the project base folder.
Check that the desired POM is selected and 'Finish'.


--
Revision date: 2017-04-16
leic-sod@disciplinas.tecnico.ulisboa.pt
 
  