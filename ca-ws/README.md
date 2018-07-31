# CA-WS

This is the **C**ertificate **A**uthority **W**eb **S**ervice implemented in Java.

The service is defined by the Java code with annotations
(code-first approach, also called bottom-up approach).
The service runs in a stand-alone HTTP server.

The certificates to be served by the CA should be stored in an external folder with the following example structure:
```
target/certificates/
```

CA public-key certificate:
```
target/certificates/ca/
target/certificates/ca/ca-certificate.pem.txt
```

User TESTE public-key certificates:
```
target/certificates/teste/
target/certificates/teste/TESTE_Mediator.cer
```

User A22 certificates:
```
target/certificates/a22/A22_*.cer
```

User T03 certificates:
```
target/certificates/t03/T03_*.cer
```

Certificates like these can be generated with a script.
See [gen-certificates.sh](/script/gen-certificates.sh) for details.


## Instructions using Maven

To compile:

```
mvn compile
```

To run using exec plugin with default arguments:

```
mvn exec:java
```

To run with UDDI (arguments are uddiURL wsName wsURL):

```
mvn exec:java -Pexec.args="http://localhost:9090 CA http://localhost:8080/ca-ws/endpoint target/certificates/"
```

To run without UDDI (argument is wsURL):

```
mvn exec:java -Pexec.args="http://localhost:8080/ca-ws/endpoint target/certificates/"
```

When running, the web service awaits connections from clients.
You can check if the service is running using your web browser 
to see the generated WSDL file:
http://localhost:8080/ca-ws/endpoint?WSDL

<!--
This address is defined in CAApp when the publish() method is called.
-->

To call the service you will need a web service client,
including code generated from the WSDL.


### To configure the project in Eclipse

'File', 'Import...', 'Maven'-'Existing Maven Projects'.
'Browse' to the project base folder.
Check that the desired POM is selected and 'Finish'.
