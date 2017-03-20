package pl.edu.wat.sms_sender2;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by olszewskmate2 on 18.03.2017.
 */
public class Elgamal_keyset {
    private Elgamal_PublicKey pk;
    private Elgamal_PrivateKey prk;


    public Elgamal_keyset(Elgamal_PublicKey _pk, Elgamal_PrivateKey _prk){
        this.pk=_pk;
        this.prk=_prk;
    }

    public void Elgamal_Keyset(){

        BigInteger p=Elgamal.getPrime();
        BigInteger p_prime=(p.subtract(BigInteger.ONE).divide(new BigInteger("2")));
        BigInteger g;

        boolean found=false;

        do{
            g=new BigInteger(p.bitCount()-1,new SecureRandom());
            if(p.compareTo(g)==1 && g.modPow(p_prime,p).equals(BigInteger.ONE) && !g.modPow(new BigInteger("2"),p).equals(BigInteger.ONE)){
                found=true;
            }
        }while(!found);

        BigInteger x;

        do{
            x=new BigInteger(p_prime.bitCount()-1,new SecureRandom());
        }while(p_prime.compareTo(x)==-1);

        BigInteger y=g.modPow(x,p);

        Elgamal_PublicKey pk=new Elgamal_PublicKey(p,y,g);
        Elgamal_PrivateKey prk=new Elgamal_PrivateKey(p,x);

        this.pk=pk;
        this.prk=prk;

    }

    public  Elgamal_PublicKey getPk() {
        return pk;
    }

    public Elgamal_PrivateKey getPrk() {
        return prk;
    }
}
