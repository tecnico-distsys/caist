This is a Java library that helps in the use of Public-Key Certificates in X.509 format.

The code of the library is in: 
./src/main/java/

There are tests that use the library for signing and verifying:
./src/test/java/

The set of functionalities available in Java for using X.509 certificates is incomplete,
so a set of OpenSSL scripts are also necessary.
The Linux scripts are in: ./script/


Definitions:
------------

An X509 Certificate is a type of public key in a public/private key pair. 
These key pairs can be used for different things, like encryption via SSL, or for identification. 
SSL Certificates are a type of X509 certificate

A Java KeyStore (JKS) is a repository of security certificates 
– either authorization certificates or public key certificates – 
plus corresponding private keys, used for instance in SSL encryption. 

Tools:
------

keytool
The keytool command is a key and certificate management utility. 
It enables users to administer their own public/private key pairs and associated certificates 
for use in self-authentication (where the user authenticates himself or herself to other users and services) or 
data integrity and authentication services, using digital signatures.

openssl
The openssl binary (usually /usr/bin/openssl on linux) is an entry point for many functions,
also related to digital certificates.


Instructions using Maven:
------------------------

To generate certificates:
    ./script/gen-ca-servers-keys.sh example
    (generates keys for a CA, and keys for an 'example' entity)
    (there are some test resources already in place that were generated with this script)

To compile and execute all tests:
    mvn test

To install the cert-util module:
    mvn install


To configure the Maven project in Eclipse:
-----------------------------------------

'File', 'Import...', 'Maven'-'Existing Maven Projects'
'Select root directory' and 'Browse' to the project base folder.
Check that the desired POM is selected and 'Finish'.


--
Revision date: 2017-04-10
leic-sod@disciplinas.tecnico.ulisboa.pt
