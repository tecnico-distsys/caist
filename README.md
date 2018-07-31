# CAIST

**C**ertificate **A**uthority implemented at **I**nstituto **S**uperior **T**écnico, Universidade de Lisboa, Portugal

CAIST is a a simplified certification authority SOAP web service.

CAIST is implemented in the Java programming language using the JAX-WS library (Java API for XML Web Services.


## Getting Started

CAIST is composed of 3 modules:
* cert-util - certificate utility library
* ca-ws - the certificate authority server
* ca-ws-cli - the certificate authority client

There are also bash scripts to generate the required keys and certificates.


### Prerequisites

CAIST requires Java Developer Kit 8 running on Linux, Windows or Mac.
Maven 3 is also required.

To confirm that you have it installed, open a terminal and type:

```
javac -version

mvn -version
```


### Installing

You can find instructions for each component in their folder:
* [cert-util](cert-util/)
* [ca-ws](ca-ws/)
* [ca-ws-cli](ca-ws-cli/).

<!--
mention compilation with super POM
-->

<!--
## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```
-->

## Deployment

To deploy a CAIST server, the program should be started with a URL containing a public DNS name or IP address instead of the default 'localhost' used for development.


## Built With

* [Maven](https://maven.apache.org/) - Build Tool and Dependency Management
* [JAX-WS](https://javaee.github.io/metro-jax-ws/) - SOAP Web Services implementation for Java

<!--
## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.
-->

## Versioning

We use [SemVer](http://semver.org/) for versioning.


## Authors

* **Miguel L. Pardal** - *structure and interfaces* - [miguelpardal](https://github.com/miguelpardal)
* **David R. Matos** - *server implementation and scripts* - [miguelpardal](https://github.com/miguelpardal)

See also the list of [contributors](https://github.com/tecnico-distsys/caist/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* All the Distributed Systems students for their feedback
* Other members of the Distributed Systems teaching staff
