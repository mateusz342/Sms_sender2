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
    BigInteger p,y,g,x;
    byte[] c1;
    byte[] c2;





    public static BigInteger getPrime(){
        BigInteger variable=new BigInteger("2");
        BigInteger p,p_prime;
        do{
            p=BigInteger.probablePrime(1024, new Random());
            //p=p_prime.multiply(variable).add(BigInteger.ONE);
            p_prime=p.subtract(BigInteger.ONE).divide(variable);
        }while(!p_prime.isProbablePrime(100));
        return p;
    }

    public void encrypt(byte[] in,BigInteger p,BigInteger y, BigInteger g) {
        this.p = p;
        this.y = y;
        this.g = g;
        BigInteger dividor=new BigInteger("2");
        BigInteger table = new BigInteger(in);
        BigInteger c2;
        BigInteger p_prime;
        p_prime= p.subtract(BigInteger.ONE).divide(dividor);
        BigInteger k;
        int lenghtc1;
        int lenghtc2;
        BigInteger i;
do {
    do {
        k = new BigInteger(p_prime.bitCount() - 1, new SecureRandom());
         i=gcd(p.subtract(BigInteger.ONE),k);
    } while (p.compareTo(k) == -1 || !i.equals(BigInteger.ONE));

    //k=new BigInteger("5007045949397099653079893236726853196796153793203906418691238721694914186752524453451067688305258794553464612147433593571828622176303584541499565656856569651583");
    c2 = (table.multiply(y.modPow(k, p))).mod(p);
    lenghtc2 = c2.bitLength();

    BigInteger c1 = g.modPow(k, p);
    lenghtc1 = c1.bitLength();

    byte[] arrayc1 = c1.toByteArray();
    byte[] arrayc2 = c2.toByteArray();

        /*if(arrayc1.length==129){
            arrayc1=Arrays.copyOfRange(arrayc1,1,arrayc1.length);
        }
        if(arrayc2.length==129){
            arrayc2=Arrays.copyOfRange(arrayc2,1,arrayc2.length);
        }*/
    this.c1 = arrayc1;
    this.c2 = arrayc2;


}while(lenghtc1==1024||lenghtc2==1024);
    }

    private static BigInteger gcd(BigInteger pminus1, BigInteger k) {
        BigInteger q;
        BigInteger c;
        int comp1;
        do{
            q=pminus1.divide(k);
            c=k;
            k=pminus1.subtract(q.multiply(k));
            pminus1=c;
            comp1=k.compareTo(BigInteger.ZERO);
        }
        while(comp1>0);
        return pminus1;
    }


    public byte[] decrypt(byte[] c1,byte[] c2, BigInteger p, BigInteger x){
        byte[] outkey=new byte[32];
        BigInteger c11=new BigInteger(c1);
        BigInteger c22=new BigInteger(c2);
        int plaintextlength;
        this.p=p;
        this.x=x;
        byte[] x1=x.toByteArray();
        int lenght1=x1.length;
        BigInteger tmp=c11.modPow(x,p);
        byte[] tmpc1=tmp.toByteArray();
        int length=tmpc1.length;
        byte[] tmp2=new byte[length-1];

        BigInteger modinv=tmp.modInverse(p);
        byte[] modinvbyte=modinv.toByteArray();
        BigInteger plaintext=(c22.multiply(modinv)).mod(p);

        plaintextlength=plaintext.bitLength();
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
