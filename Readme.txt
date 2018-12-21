//This sample code tests the VAE Rest calls to the Vormetric Crypto Server.
//It will first encrypt a file and create an output file with enc at the end of it.
//You can then select the file that you just encrypted that ends with an enc and
//the application will then decrypt it.
//The input file format should be a single entry for each line.
//This is for testing only so it will only at most process the first 20 records
//in the file.


// It is important to set the following environment variables before running:
//this debug is optional
set vtsdebug=1
SET vtsuserid=vtsuser
set vtspassword=yourvtsuserpwd
set vtstokenserver=192.168.159.141
set vtsalg=A128CTR
set vtsivnumber=0123456789012345
//make sure you have the correct permissions in vts to encrypt/decrypt for the key you pick below.
set vtsencryptdecryptkey=yourencryptionkeydefinedinvts
start JavaVAEUI.jar

