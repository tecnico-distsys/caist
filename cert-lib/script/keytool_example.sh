#!/bin/sh

###############################################################################
# Script to generate a key pair in a key store and
# export a public-key certificate
###############################################################################
# author: Sistemas Distribuídos - Instituto Superior Técnico

# Command to generate key pair and public key digital certificate
keytool -genkeypair -alias "example" -keyalg RSA -keysize 2048 -keypass "ins3cur3" -validity 90 -storepass "1nsecure" -keystore ../target/keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
# The generated key is stored in a JKS (Java Key Store). 
# There are separate passwords for the key and for the JKS. 

# Command to extract public key certificate from created keystore
keytool -export -keystore ../target/keystore.jks -alias example -storepass "1nsecure" -file ../target/example.cer
