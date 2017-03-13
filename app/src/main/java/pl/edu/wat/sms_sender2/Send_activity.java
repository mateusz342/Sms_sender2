package pl.edu.wat.sms_sender2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.security.KeyException;
import java.util.ArrayList;

public class Send_activity extends AppCompatActivity {

    ArrayList<String> smsMessagesList = new ArrayList<>();
    ListView messages;
    EditText input;
    //String input="hihihihi";
    ArrayAdapter arrayAdapter;
    SmsManager smsManager = SmsManager.getDefault();
    Button button;
    EditText tvNumber;
    private static Send_activity inst;
    String out2="        ";
    byte[] out=out2.getBytes();
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST=1;
    public static boolean active=false;
    Blowfish blowfish=new Blowfish();
    Button decrypt;




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public Send_activity() throws KeyException {
    }

    public static Send_activity instance() {
        return inst;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_activity);
        this.startService(new Intent(this, QuickResponseService.class));
        messages = (ListView) findViewById(R.id.messages);
        input = (EditText) findViewById(R.id.input);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        messages.setAdapter(arrayAdapter);
        decrypt=(Button) findViewById(R.id.decrypt);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadContacts();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();

        } else {
            refreshSmsInbox();
        }

        decrypt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Decrypt.class);
                startActivity(i);
            }
        });


    }





    @Override
    public void onStart() {
        super.onStart();
        active=true;
        inst = this;
    }

    public void updateInbox(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }
    public static String StringtoBinary(EditText input){
        byte[] bytes=input.getText().toString().getBytes();
        StringBuilder binary=new StringBuilder();
        for(byte b:bytes){
            int val=b;
            for(int i=0;i<8;i++){
                binary.append((val&128)==0 ? 0:1);
                val<<=1;
            }

        }
        return binary.toString();
    }



    public void onSendClick(View view) throws KeyException {
        tvNumber=(EditText) findViewById(R.id.tvNumber);
        String theNumber=tvNumber.getText().toString();
        byte[] bytes=input.getText().toString().getBytes();

        //blowfish.makeKey();
        //String out1=blowfish.blowfishEncrypt( bytes,0,out, 0);
        byte[] out1=blowfish.blowfishEncrypt( bytes,0,out, 0);
        byte[] out2= encodingfunction(out1,0);
        //String aString=new String(out1);
        String aString=new String(out2);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            smsManager.sendTextMessage(theNumber/*"+48602890836"*/, null, aString/*blowfish.blowfishEncrypt( bytes,0,out, 0)*/ /*input.getText().toString()*/, null, null);
            Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show();
        }
    }

   byte [] table=new byte[16];

    public byte[] encodingfunction(byte[] intoencode,int out){
        for(int i=0;i<intoencode.length;i++){
            //table[out]= (byte) (intoencode[i] & 0xF0);
            //table[out+1]= (byte) (intoencode[i] & 0x0F);
            table[out]=(byte) (intoencode[i] & 0x0F);
            table[out+1]= (byte)((intoencode[i]>>>4)&0x0F);
            out+=2;
        }

        for(int i=0;i<table.length;i++){
            if(table[i]==0)
                table[i]=64;
            if(table[i]==1)
                table[i]=65;
            if(table[i]==2)
                table[i]=66;
            if(table[i]==3)
                table[i]=67;
            if(table[i]==4)
                table[i]=68;
            if(table[i]==5)
                table[i]=69;
            if(table[i]==6)
                table[i]=70;
            if(table[i]==7)
                table[i]=71;
            if(table[i]==8)
                table[i]=72;
            if(table[i]==9)
                table[i]=73;
            if(table[i]==10)
                table[i]=74;
            if(table[i]==11)
                table[i]=75;
            if(table[i]==12)
                table[i]=76;
            if(table[i]==13)
                table[i]=77;
            if(table[i]==14)
                table[i]=78;
            if(table[i]==15)
                table[i]=79;
        }
        return table;
    }



    public void onSendClick1(View view){
        tvNumber=(EditText) findViewById(R.id.tvNumber);
        String theNumber=tvNumber.getText().toString();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            smsManager.sendTextMessage(theNumber, null, input.getText().toString(), null, null);
            Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show();
        }
    }




    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    public void getPermissionToReadContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + getContactName(this,smsInboxCursor.getString(indexAddress)) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            //if(smsInboxCursor.getString(indexAddress).equals("PHONE NUMBER HERE")){
            arrayAdapter.add(str);//}
        } while (smsInboxCursor.moveToNext());

        for(int i=0; i<arrayAdapter.getCount();i++){
            if(i==arrayAdapter.getCount()-1){
            Object obj=arrayAdapter.getItem(i);
                Object oo=obj;}
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        active=false;


    }

    public static String getContactName(Context context, String phoneNo) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return phoneNo;
        }
        String Name = phoneNo;
        if (cursor.moveToFirst()) {
            Name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return Name;

    }
}