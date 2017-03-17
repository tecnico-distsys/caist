# commands required to generate key pair and public key digital certificate
keytool -genkeypair -alias "example" -keyalg RSA -keysize 2048 -keypass "ins3cur3" -validity 90 -storepass "1nsecure" -keystore keystore.jks -dname "CN=DistributedSystems, OU=DEI, O=IST, L=Lisbon, S=Lisbon, C=PT"
keytool -export -keystore keystore.jks -alias example -storepass "1nsecure" -file example.cer
