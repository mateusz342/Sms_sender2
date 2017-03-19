package pl.edu.wat.sms_sender2;

import java.math.BigInteger;

/**
 * Created by olszewskmate2 on 18.03.2017.
 */

public class Elgamal_PublicKey {
    private BigInteger p;
    private BigInteger g;
    private BigInteger y;


    public Elgamal_PublicKey(BigInteger p, BigInteger h,BigInteger g){
        this.p=p;
        this.y=h;
        this.g=g;
    }

    public BigInteger getP(){
        return p;
    }

    public BigInteger getY(){
        return y;
    }

    public BigInteger getG(){
        return g;
    }

}
