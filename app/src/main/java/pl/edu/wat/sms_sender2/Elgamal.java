package pl.edu.wat.sms_sender2;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by olszewskmate2 on 17.03.2017.
 */

public class Elgamal {
    private int nbits;
    private Elgamal_keyset kset;
    BigInteger p,y,g,x;
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
        if(arrayc1.length==129){
            arrayc1=Arrays.copyOfRange(arrayc1,1,arrayc1.length);
        }
        if(arrayc2.length==129){
            arrayc2=Arrays.copyOfRange(arrayc2,1,arrayc2.length);
        }
        this.c1=arrayc1;
        this.c2=arrayc2;
    }


    public byte[] decrypt(byte[] c1,byte[] c2, BigInteger p, BigInteger x){
        byte[] outkey=new byte[56];
        BigInteger c11=new BigInteger(c1);
        BigInteger c22=new BigInteger(c2);
        this.p=p;
        this.x=x;

        BigInteger tmp=c11.modPow(x,p);
        BigInteger plaintext=c22.multiply(tmp.modInverse(p)).mod(p);

        byte[] key=plaintext.toByteArray();

        return key;
    }

    public byte[] getc1() {
        return c1;
    }

    public byte[] getc2() {
        return c2;
    }





}
