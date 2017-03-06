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
    String out2="        ";
    byte[] out1=out2.getBytes();
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
                byte[] bytes=smsBody.getBytes();
                String out = null;
                try {
                    out = blowfish.blowfishDecrypt(bytes,0,out1,0);
                } catch (KeyException e) {
                    e.printStackTrace();
                }
                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += out/*smsBody*/ + "\n";
            }

            Toast.makeText(context, "Message Received!", Toast.LENGTH_SHORT).show();

            if(MainActivity.active){
                MainActivity inst=MainActivity.instance();
                inst.updateInbox(smsMessageStr);
            }else{
                Intent i=new Intent(context,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }


        }
    }
}
