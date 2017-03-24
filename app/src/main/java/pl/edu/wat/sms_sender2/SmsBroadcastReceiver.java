package pl.edu.wat.sms_sender2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
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
            StringBuilder address=new StringBuilder();
            String body;
            byte[] out = new byte[0];
            for (int i = 0; i < sms.length; ++i) {
                format = intentExtras.getString("format");
                smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);
                //address = smsMessage.getOriginatingAddress().toString();
                address.append(smsMessage.getOriginatingAddress().toString());
                bodyText.append(smsMessage.getMessageBody().toString());
                }
                body=bodyText.toString();
                address1=address.substring(3,12);
                out1 = body.getBytes();
                //out1=smsBody.toByteArray();
                int length = out1.length;
                if (body.substring(0,1).equals("p")) {
                    smsMessageStr += "SMS From: " + address1 + "\n";
                    smsMessageStr += body + "\n";
                    Toast.makeText(context, "Public key received!", Toast.LENGTH_SHORT).show();


                    File myFilep = new File("/sdcard/"+address1+"p.txt");
                    File myFiley = new File("/sdcard/"+address1+"y.txt");
                    File myFileg=  new File("/sdcard/"+address1+"g.txt");

                    try {


                            myFilep.createNewFile();
                            myFiley.createNewFile();
                            myFileg.createNewFile();


                            FileWriter fwp = new FileWriter(myFilep);
                            FileWriter fwy = new FileWriter(myFiley);
                            FileWriter fwg = new FileWriter(myFileg);

                        int i=bodyText.indexOf("y");
                        String p=bodyText.substring(1,i);
                        fwp.write(p.toString());
                        fwp.flush();
                        fwp.close();

                        int i1=bodyText.indexOf("g");
                        String y=bodyText.substring(i+1,i1);
                        fwy.write(y.toString());
                        fwy.flush();
                        fwy.close();

                        String g=bodyText.substring(i1+1,bodyText.length());
                        fwg.write(g.toString());
                        fwg.flush();
                        fwg.close();

                        Toast.makeText(context, "Done writing SD publickey of receiver", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    byte[] in = decodingfunction(out1, 0);
                    byte[] ciphertextonly = Arrays.copyOfRange(in, 0, in.length - 256);
                    byte[] c1 = Arrays.copyOfRange(in, in.length - 256, in.length - 128);
                    byte[] c2 = Arrays.copyOfRange(in, in.length - 128, in.length);
                    File sdcard= Environment.getExternalStorageDirectory();
                    File filep=new File(sdcard,"myprivatekeyp.txt");
                    File filex=new File(sdcard,"myprivatekeyx.txt");
                    try {
                        BufferedReader brp = new BufferedReader(new FileReader(filep));
                        BufferedReader brx = new BufferedReader(new FileReader(filex));

                        String p1 = brp.readLine();
                        brp.close();
                        BigInteger p = new BigInteger(p1);

                        String x1 = brx.readLine();
                        brx.close();
                        BigInteger x = new BigInteger(x1);

                        byte[] key = elgamal.decrypt(c1, c2, p/*kset.getPrk().getP()*/, x /*kset.getPrk().getX()*/);

                    //byte[] keytoblowfish = Arrays.copyOfRange(in, in.length - 56, in.length);
                    byte[] out3 = new byte[ciphertextonly.length];

                    try {
                        out = blowfish.blowfishDecrypt(ciphertextonly, 0, out3, 0,key /*keytoblowfish*/);
                    } catch (KeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    }catch (Exception e){

                    }
                    String o = new String(out);
                    smsMessageStr += "SMS From: " + address1 + "\n";
                    smsMessageStr += o + "\n";
                    Toast.makeText(context, "Message Received!", Toast.LENGTH_SHORT).show();
                }






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
