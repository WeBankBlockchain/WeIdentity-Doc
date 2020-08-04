package xxx.yyy.zzz;

import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.Keys;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


public class ECKeySampleApp {

    public static void main (String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair keyPair = Keys.createEcKeyPair();
        System.out.println("public key " + keyPair.getPublicKey());
        System.out.println("private key " + keyPair.getPrivateKey());
        // public key
        // 2826353706326430136059766899918547268257144433345028935544246672544715811531698763009967557019653807523504447872634462259780101707992526761608737256788009
        // private key
        // 51801066929398358250268966823436564939107125383375289829603669124463475610644
    }
}
