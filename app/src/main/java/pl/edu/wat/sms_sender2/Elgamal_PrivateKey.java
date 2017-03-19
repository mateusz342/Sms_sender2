package pl.edu.wat.sms_sender2;

import java.math.BigInteger;

/**
 * Created by olszewskmate2 on 18.03.2017.
 */

public class Elgamal_PrivateKey {
    private BigInteger p;
    private BigInteger x;


    public Elgamal_PrivateKey(BigInteger p, BigInteger x){
        this.p=p;
        this.x=x;
    }

   public BigInteger getP(){
       return p;
   }

    public BigInteger getX(){
        return x;
    }
}
