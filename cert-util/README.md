# Cert-Util

**Cert**ificate **Util**ities

This is a Java library that helps in the use of Public-Key Certificates in X.509 format.

The [code](src/main/java/) of the library is in the source folder.
The [tests](src/test/java/) use the library for signing and verifying.

The set of functionalities available in Java for using X.509 certificates is incomplete, so a set of OpenSSL [scripts](/script/) are also necessary that run on Linux.


## Instructions using Maven

To generate certificates:
```
./script/gen-ca-servers-keys.sh example
```
(generates keys for a CA, and keys for an 'example' entity)
(there are some test resources already in place that were generated with this script)

To compile and execute all tests:
```
mvn test
```

To install the cert-util module:
```
mvn install
```

### To configure the Maven project in Eclipse

'File', 'Import...', 'Maven'-'Existing Maven Projects'
'Select root directory' and 'Browse' to the project base folder.
Check that the desired POM is selected and 'Finish'.
