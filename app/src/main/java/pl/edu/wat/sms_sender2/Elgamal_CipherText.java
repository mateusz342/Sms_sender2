package pl.edu.wat.sms_sender2;

import java.math.BigInteger;

/**
 * Created by olszewskmate2 on 19.03.2017.
 */

public class Elgamal_CipherText {
    private BigInteger ciphertext1;
    public byte[] ciphertext2;

    public Elgamal_CipherText(BigInteger c1,byte[] c2){
        ciphertext1=c1;
        ciphertext2=c2;
    }

    public byte[] getC2(){
        return ciphertext2;
    }

    public BigInteger  getC1(){
        return ciphertext1;
    }

}
