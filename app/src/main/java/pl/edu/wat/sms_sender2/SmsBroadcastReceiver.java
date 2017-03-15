package pl.edu.wat.sms_sender2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.security.KeyException;

public class SmsBroadcastReceiver extends BroadcastReceiver{
    public static final String SMS_BUNDLE="pdus";
    Blowfish blowfish=new Blowfish();
    //String out4="        ";
    //byte[] out3=out4.getBytes();
    String out2="        ";
    byte[] out1=out2.getBytes();
    int i =0;
    public void onReceive(Context context, Intent intent){
        Bundle intentExtras=intent.getExtras();

        if(intentExtras!=null){
            Object[] sms =(Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr="";
            for(int i=0;i<sms.length;++i){
                String format=intentExtras.getString("format");
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                out1=smsBody.getBytes();
                byte[] in=decodingfunction(out1,0);
                byte[] out3=new byte[in.length];
                byte[]  out = new byte[0];
                try {
                    out = blowfish.blowfishDecrypt(in,0,out3,0);
                } catch (KeyException e) {
                    e.printStackTrace();
                }

                String o=new String(out);
                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += o /*smsBody*/ + "\n";
            }

            Toast.makeText(context, "Message Received!", Toast.LENGTH_SHORT).show();


            if(Send_activity.active){
                Send_activity inst=Send_activity.instance();
                inst.updateInbox(smsMessageStr);
            }else{
                Intent i=new Intent(context,Send_activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }



        }
    }



    //byte[] newin=new byte[8];
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
