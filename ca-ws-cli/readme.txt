This is a Java Web Service client with JUnit tests.

The client uses the wsimport tool
to generate classes that can invoke the web service and
perform the Java to XML data conversion.

The client needs access to the WSDL file,
either using HTTP or using the local file system.


Instructions using Maven:
------------------------

You must start ca-ws first.
The CA server should be started pointing to a certificates folder where the TESTE_Mediator certificate exists.
Suggestion: -Dcert.dir=../ca-ws/src/test/resources/


The CA certificate file location is ${basedir}/../ca-ws/src/test/resources/ca/ca-certificate.pem.txt
i.e. the CA certificate is retrieved from the server test resources,
assuming they are next to the client project.

The CA file could be copied for the test resources of this project.


The default WSDL file location is ${basedir}/../ca-ws/src/main/resources,
i.e. the WSDL is retrieved from the server sources,
assuming they are next to the client project.

The WSDL could be copied to the client project and
stored at ${basedir}/src/main/resources.

The WSDL location is specified in pom.xml
/project/build/plugins/plugin[artifactId="jaxws-maven-plugin"]/configuration/

The jaxws-maven-plugin is run at the "generate-sources" Maven phase
(which happens before the compile phase).

To generate stubs using wsimport:
  mvn generate-sources

To compile:
  mvn compile

To verify remote server (run integration tests):
  mvn verify


To configure the project in Eclipse:
-----------------------------------

'File', 'Import...', 'Maven'-'Existing Maven Projects'.
'Browse' to the project base folder.
Check that the desired POM is selected and 'Finish'.


--
Revision date: 2017-04-17
leic-sod@disciplinas.tecnico.ulisboa.pt
