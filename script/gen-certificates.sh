#!/bin/sh

#A22_Mediator, A22_Supplier1

# author: Professores de Sistemas Distribuídos - Instituto Superior Técnico
################################################################################
# Script to generate signed X509 certificates
# usage:
#     ./gen_keys.sh <users-file>
# More information regarding signed certificates consult:
# - https://docs.oracle.com/cd/E19509-01/820-3503/ggeyj/index.html
# - https://docs.oracle.com/cd/E19509-01/820-3503/ggezy/index.html
################################################################################

#constants
NOW=$(date +"%Y_%m_%d__%H_%M_%S")
CA_ALIAS="CA-Distributed-Systems-2017"
CA_CERTIFICATE_PASS="3v9xZUEHG45gbTGGn0"

CA_CSR_FILE="ca.csr"
SUBJ="/CN=DistributedSystems/OU=DEI/O=IST/L=Lisbon/C=PT"
KEYS_VALIDITY=1095
OUTPUT_FOLDER="keys_$NOW"
CA_FOLDER="$OUTPUT_FOLDER/ca"
STORE_FILE="$CA_FOLDER/ca-keystore.jks"
CA_PEM_FILE="$CA_FOLDER/ca-certificate.pem.txt"
CA_KEY_FILE="$CA_FOLDER/ca-key.pem.txt"


NUM_OF_SUPPLIERS=3

#
# $1 -> name_of_certificate
# $2 -> password
# $3 -> directory
#
function generate_signed_certificates(){
  name_of_certificate=$1
  password=$2
  server_folder=$3
  echo ""
  echo ""
  echo "Creating keys for user '$name_of_certificate' using password '$password' "

  d_name="CN=$name_of_certificate,OU=DEI,O=IST,L=Lisbon,S=Lisbon,C=PT"
  server_keystore_file="$server_folder/$server_name.jks"
  csr_file="$server_folder/$name_of_certificate.csr"
  echo "Generating keypair of $name_of_certificate..."
  keytool -keystore $server_keystore_file -genkey -alias $name_of_certificate -keyalg RSA -keysize 2048 -keypass $password -validity $KEYS_VALIDITY -storepass $password  -dname $d_name
  echo "Generating the Certificate Signing Request of $name_of_certificate..."
  keytool -keystore $server_keystore_file -certreq -alias $name_of_certificate -keyalg rsa -file $csr_file -storepass $password -keypass $password
  echo "Generating the signed certificate of $name_of_certificate..."
  openssl  x509  -req  -CA $CA_PEM_FILE -CAkey $CA_KEY_FILE -passin pass:$CA_CERTIFICATE_PASS -in $csr_file -out "$server_folder/$name_of_certificate.cer"  -days $KEYS_VALIDITY -CAcreateserial
  echo "Importing the CA certificate to the keystore of $name_of_certificate..."
  keytool -import -keystore $server_keystore_file -file $CA_PEM_FILE  -alias $CA_ALIAS -keypass $password -storepass $password -noprompt
  echo "Importing the signed certificate of $name_of_certificate to its keystore"
  keytool -import -keystore $server_keystore_file -file "$server_folder/$name_of_certificate.cer" -alias $name_of_certificate -storepass $password -keypass $password
  echo "Removing the Certificate Signing Request (.csr file)..."
  rm "$server_folder/$name_of_certificate.csr"
}





################################################################################
# 1 - First the CA Certificate is generated
# This certificate is used to sign other certificates
# This procedure is done once for the CA and the generated files (*.pem.txt)
# are used to sign the certificates of the other entities
################################################################################
mkdir $OUTPUT_FOLDER
mkdir $CA_FOLDER
echo "Generating the CA certificate..."
openssl req -new -x509 -keyout $CA_KEY_FILE -out $CA_PEM_FILE -days $KEYS_VALIDITY -passout pass:$CA_CERTIFICATE_PASS -subj $SUBJ
echo "CA Certificate generated."

################################################################################
# 2 - Then, for each entity (given as an argument) the certificates argument
# generated, signed and imported into the entities keystore
################################################################################

while read line; do
    server_name=${line%% *}
    server_name=$(echo $server_name | tr 'a-z' 'A-Z')
    password=${line##* }



    server_folder=$OUTPUT_FOLDER/$server_name
    echo "mkdir server_folder"
    mkdir $server_folder


    name_of_certificate=$server_name"_Mediator"
    generate_signed_certificates $name_of_certificate $password  $server_folder


    for (( c=1; c<=$NUM_OF_SUPPLIERS; c++ ))
    do
      name_of_certificate=$server_name"_Supplier$c"
      generate_signed_certificates $name_of_certificate $password  $server_folder
    done



done < $1
