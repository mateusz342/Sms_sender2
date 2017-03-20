package pl.edu.wat.sms_sender2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SmsBroadcastReceiver extends BroadcastReceiver{
    public static final String SMS_BUNDLE="pdus";
    Blowfish blowfish=new Blowfish();
    String out2="        ";
    byte[] out1=out2.getBytes();
    Elgamal elgamal=new Elgamal();
    Elgamal_PublicKey pk;
    Elgamal_PrivateKey prk;
    Elgamal_keyset kset=new Elgamal_keyset(pk,prk);
    int j=0;
    String smsBody;
    public void onReceive(Context context, Intent intent){
        Bundle intentExtras=intent.getExtras();

        if(intentExtras!=null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String format;
            SmsMessage smsMessage;
            String smsBody;
            String address1;
            StringBuilder bodyText = new StringBuilder();
            String body;
            String address;
            for (int i = 0; i < sms.length; ++i) {
                format = intentExtras.getString("format");
                smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);
                address = smsMessage.getOriginatingAddress().toString();
                bodyText.append(smsMessage.getMessageBody().toString());
                }
                 body=bodyText.toString();
                out1 = body.getBytes();
                //out1=smsBody.toByteArray();
                int length = out1.length;
                if (out1[length - 1] == 32) {
                   // smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += body + "\n";
                } else {
                    byte[] in = decodingfunction(out1, 0);
                    byte[] ciphertextonly = Arrays.copyOfRange(in, 0, in.length - 256);
                    byte[] c1 = Arrays.copyOfRange(in, in.length - 256, in.length - 128);
                    byte[] c2 = Arrays.copyOfRange(in, in.length - 128, in.length);
                    byte[] key = elgamal.decrypt(c1, c2, kset.getPrk().getP(), kset.getPrk().getX());
                    byte[] keytoblowfish = Arrays.copyOfRange(in, in.length - 56, in.length);
                    byte[] out3 = new byte[ciphertextonly.length];
                    byte[] out = new byte[0];
                    try {
                        out = blowfish.blowfishDecrypt(ciphertextonly, 0, out3, 0, keytoblowfish);
                    } catch (KeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    String o = new String(out);
                   // smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += o + "\n";
                }


            Toast.makeText(context, "Message Received!", Toast.LENGTH_SHORT).show();


            if (Send_activity.active) {
                Send_activity inst = Send_activity.instance();
                inst.updateInbox(smsMessageStr);
            } else {
                Intent i = new Intent(context, Send_activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }


        }

    }

    public byte[] decodingfunction(byte[] table, int index){

        byte[] newin=new byte[table.length/2];

        for(int i=0;i<table.length;i++){
            if(table[i]==64)
                table[i]=0;
            if(table[i]==65)
                table[i]=1;
            if(table[i]==66)
                table[i]=2;
            if(table[i]==67)
                table[i]=3;
            if(table[i]==68)
                table[i]=4;
            if(table[i]==69)
                table[i]=5;
            if(table[i]==70)
                table[i]=6;
            if(table[i]==71)
                table[i]=7;
            if(table[i]==72)
                table[i]=8;
            if(table[i]==73)
                table[i]=9;
            if(table[i]==74)
                table[i]=10;
            if(table[i]==75)
                table[i]=11;
            if(table[i]==76)
                table[i]=12;
            if(table[i]==77)
                table[i]=13;
            if(table[i]==78)
                table[i]=14;
            if(table[i]==79)
                table[i]=15;
        }

        for(int i=0;i<newin.length;i++){
            newin[i]=(byte)((table[index+1]<<4)|table[index]);
            index+=2;
        }
        return newin;
    }
}
