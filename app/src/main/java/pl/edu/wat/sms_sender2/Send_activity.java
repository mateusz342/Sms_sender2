package pl.edu.wat.sms_sender2;

import android.Manifest;
import android.Manifest.permission;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.PhoneLookup;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Send_activity extends AppCompatActivity {

    ArrayList<String> smsMessagesList = new ArrayList<>();
    ListView messages;
    EditText input;
    ArrayAdapter arrayAdapter;
    SmsManager smsManager = SmsManager.getDefault();
    EditText tvNumber;
    private static Send_activity inst;
    String out2 = "        ";
    byte[] out = out2.getBytes();
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    public static boolean active = false;
    Blowfish blowfish = new Blowfish();
    Elgamal_PublicKey pk;
    Elgamal_PrivateKey prk;
    Elgamal_keyset kset=new Elgamal_keyset(pk,prk);
    Elgamal elgamal=new Elgamal();
   // Elgamal_PublicKey elgamal_publicKey=new Elgamal_PublicKey();
    Button generate;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    BigInteger p1,y,g;
    BigInteger x;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

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
        generate=(Button) findViewById(R.id.generate);

        if (ContextCompat.checkSelfPermission(this, permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadContacts();
        }
        if (ContextCompat.checkSelfPermission(this, permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();

        }
        if (ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            verifyStoragePermissions();

        }

        else {
            refreshSmsInbox();
        }


        generate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                kset.Elgamal_Keyset();
                BigInteger p = kset.getPk().getP();
                BigInteger g=kset.getPk().getG();
                BigInteger y=kset.getPk().getY();

                BigInteger x=kset.getPrk().getX();
                if(p!=null && g!=null & y!=null & x!=null){
                    Context context = getApplicationContext();
                Toast.makeText(context, "Public and private keys successfully generated!" , Toast.LENGTH_SHORT).show();}
            //private key save to file
                File myFilex = new File("/sdcard/myprivatekeyx.txt");
                File myFilep = new File("/sdcard/myprivatekeyp.txt");
                try {

                    if(!myFilex.exists() && !myFilep.exists()) {
                        myFilex.createNewFile();
                        myFilep.createNewFile();

                        FileWriter fwx = new FileWriter(myFilex);
                        FileWriter fwp = new FileWriter(myFilep);
                        fwx.write(x.toString());
                        fwx.flush();
                        fwx.close();

                        fwp.write(p.toString());
                        fwp.flush();
                        fwp.close();

                        Toast.makeText(getBaseContext(), "Done writing SD 'myprivatekeyx.txt' and 'myprivatekeyp'", Toast.LENGTH_SHORT).show();

                    }
                    } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                //publickey save to file
                File myFilep1 = new File("/sdcard/mypublickeyp.txt");
                File myFiley = new File("/sdcard/mypublickeyy.txt");
                File myFileg=  new File("/sdcard/mypublickeyg.txt");

                try {

                    if(!myFilep1.exists() && !myFiley.exists() && !myFileg.exists()) {
                        myFilep1.createNewFile();
                        myFiley.createNewFile();
                        myFileg.createNewFile();


                        FileWriter fwp = new FileWriter(myFilep1);
                        FileWriter fwy = new FileWriter(myFiley);
                        FileWriter fwg = new FileWriter(myFileg);

                        fwp.write(p.toString());
                        fwp.flush();
                        fwp.close();

                        fwy.write(y.toString());
                        fwy.flush();
                        fwy.close();

                        fwg.write(g.toString());
                        fwg.flush();
                        fwg.close();

                        Toast.makeText(getBaseContext(), "Done writing SD mypublickey", Toast.LENGTH_SHORT).show();
                    }
                    } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
        /// / ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        active = true;
        inst = this;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client2, getIndexApiAction());
    }

    public void updateInbox(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    public static String StringtoBinary(EditText input) {
        byte[] bytes = input.getText().toString().getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }

        }
        return binary.toString();
    }


    public void onSendClick(View view) throws KeyException, NoSuchAlgorithmException {
        tvNumber = (EditText) findViewById(R.id.tvNumber);
        String theNumber = tvNumber.getText().toString();
        byte[] bytes = input.getText().toString().getBytes();
        byte[] out1 = blowfish.blowfishEncrypt(bytes, 0, out, 0);
        byte[] out3=new byte[32];
        System.arraycopy(out1,out1.length-32,out3,0,out3.length);

        File sdcard= Environment.getExternalStorageDirectory();
        File filep=new File(sdcard,"mypublickeyp.txt");
        File filey=new File(sdcard,"mypublickeyy.txt");
        File fileg=new File(sdcard,"mypublickeyg.txt");
        try {
            BufferedReader brp=new BufferedReader(new FileReader(filep));
            BufferedReader bry=new BufferedReader(new FileReader(filey));
            BufferedReader brg=new BufferedReader(new FileReader(fileg));

            String p1=brp.readLine();
            brp.close();
            BigInteger p=new BigInteger(p1);

            String y1=bry.readLine();
            bry.close();
            BigInteger y=new BigInteger(y1);

            String g1=brg.readLine();
            brg.close();
            BigInteger g=new BigInteger(g1);

            elgamal.encrypt(out3,p /*kset.getPk().getP()*/, y/*kset.getPk().getY()*/,g /*kset.getPk().getG()*/);
            byte[] c1 = elgamal.getc1();
            byte[] c2 = elgamal.getc2();
            byte[] outtodecode = new byte[c1.length + c2.length + out1.length - 32];
            System.arraycopy(out1, 0, outtodecode, 0, out1.length - 32);
            System.arraycopy(c1, 0, outtodecode, 0 + out1.length - 32, c1.length);
            System.arraycopy(c2, 0, outtodecode, out1.length - 32 + c1.length, c2.length);
            //byte[] out2 = encodingfunction(out1, 0);
            byte[] out2 = encodingfunction(outtodecode, 0);


            String aString = new String(out2);
            if (ContextCompat.checkSelfPermission(this, permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                getPermissionToReadSMS();
            } else {
                SmsManager sms = SmsManager.getDefault();
                ArrayList<String> parts = sms.divideMessage(aString);
                sms.sendMultipartTextMessage(theNumber, null, parts, null, null);
                //smsManager.sendTextMessage(theNumber, null, aString, null, null);
                Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }


    public byte[] encodingfunction(byte[] intoencode, int out) {
        byte[] table = new byte[intoencode.length * 2];
        for (int i = 0; i < intoencode.length; i++) {
            table[out] = (byte) (intoencode[i] & 0x0F);
            table[out + 1] = (byte) ((intoencode[i] >>> 4) & 0x0F);
            out += 2;
        }

        for (int i = 0; i < table.length; i++) {
            if (table[i] == 0)
                table[i] = 64;
            if (table[i] == 1)
                table[i] = 65;
            if (table[i] == 2)
                table[i] = 66;
            if (table[i] == 3)
                table[i] = 67;
            if (table[i] == 4)
                table[i] = 68;
            if (table[i] == 5)
                table[i] = 69;
            if (table[i] == 6)
                table[i] = 70;
            if (table[i] == 7)
                table[i] = 71;
            if (table[i] == 8)
                table[i] = 72;
            if (table[i] == 9)
                table[i] = 73;
            if (table[i] == 10)
                table[i] = 74;
            if (table[i] == 11)
                table[i] = 75;
            if (table[i] == 12)
                table[i] = 76;
            if (table[i] == 13)
                table[i] = 77;
            if (table[i] == 14)
                table[i] = 78;
            if (table[i] == 15)
                table[i] = 79;
        }
        return table;
    }


    public void onSendClick1(View view) {
        tvNumber = (EditText) findViewById(R.id.tvNumber);
        String theNumber = tvNumber.getText().toString();

        File sdcard= Environment.getExternalStorageDirectory();
        File filep=new File(sdcard,"mypublickeyp.txt");
        File filey=new File(sdcard,"mypublickeyy.txt");
        File fileg=new File(sdcard,"mypublickeyg.txt");

        try {
            BufferedReader brp = new BufferedReader(new FileReader(filep));
            BufferedReader bry = new BufferedReader(new FileReader(filey));
            BufferedReader brg = new BufferedReader(new FileReader(fileg));

            String p1 = brp.readLine();
            brp.close();

            String y1 = bry.readLine();
            bry.close();


            String g1 = brg.readLine();
            brg.close();

            String message=new String("p="+p1+"y="+y1+"g="+g1);
            if (ContextCompat.checkSelfPermission(this, permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                getPermissionToReadSMS();
            } else {
            /*smsManager.sendTextMessage(theNumber, null, input.getText().toString() + " ", null, null);
            Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show();*/
                SmsManager sms = SmsManager.getDefault();
                ArrayList<String> parts = sms.divideMessage(message);
                sms.sendMultipartTextMessage(theNumber, null, parts, null, null);
                //smsManager.sendTextMessage(theNumber, null, aString, null, null);
                Toast.makeText(this, "Public key send!", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    public void getPermissionToReadContacts() {
        if (ContextCompat.checkSelfPermission(this, permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    permission.READ_CONTACTS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);

        }
    }
    public void verifyStoragePermissions() {
        // Check if we have write permission
        if(ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE)
        !=PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,"Please allow permission!",Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{permission.WRITE_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);
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

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Write file permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Write file permission denied", Toast.LENGTH_SHORT).show();
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
            String str = "SMS From: " + getContactName(this, smsInboxCursor.getString(indexAddress)) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            //if(smsInboxCursor.getString(indexAddress).equals("PHONE NUMBER HERE")){
            arrayAdapter.add(str);//}
        } while (smsInboxCursor.moveToNext());

        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            if (i == arrayAdapter.getCount() - 1) {
                Object obj = arrayAdapter.getItem(i);
                Object oo = obj;
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client2, getIndexApiAction());
        active = false;


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.disconnect();
    }

    public static String getContactName(Context context, String phoneNo) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
        Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return phoneNo;
        }
        String Name = phoneNo;
        if (cursor.moveToFirst()) {
            Name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));

        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return Name;

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Send_activity Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}