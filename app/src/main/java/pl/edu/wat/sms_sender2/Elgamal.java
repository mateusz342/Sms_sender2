package pl.edu.wat.sms_sender2;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by olszewskmate2 on 17.03.2017.
 */

public class Elgamal {
    private int nbits;
    private Elgamal_keyset kset;
    BigInteger p,y,g;
    byte[] c1;
    byte[] c2;





    public static BigInteger getPrime(){
        BigInteger variable=new BigInteger("2");
        BigInteger p,p_prime;
        do{
            p=BigInteger.probablePrime(1024, new Random());
            //p=p_prime.multiply(variable).add(BigInteger.ONE);

        }while(!p.isProbablePrime(100));
        return p;
    }

    public void encrypt(byte[] in,BigInteger p,BigInteger y, BigInteger g){
        this.p=p;
        this.y=y;
        this.g=g;
        BigInteger table=new BigInteger(in);
        BigInteger k;

        do{
            k=new BigInteger(p.bitCount()-1,new SecureRandom());
        }while(p.compareTo(k)==-1);

        table=(table.multiply(y.modPow(k,p))).mod(p);

        BigInteger c1=g.modPow(k,p);
        byte[] arrayc1=c1.toByteArray();
        byte[] arrayc2=table.toByteArray();
        this.c1=arrayc1;
        this.c2=arrayc2;
    }

    public byte[] getc1() {
        return c1;
    }

    public byte[] getc2() {
        return c2;
    }





}
