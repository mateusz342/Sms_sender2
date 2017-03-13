package pl.edu.wat.sms_sender2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.security.KeyException;

import static pl.edu.wat.sms_sender2.R.id.decrypt;

public class Decrypt extends AppCompatActivity {
    Button button;
    EditText ciphertextp;
    Blowfish blowfish=new Blowfish();
    Send_activity send_activity=new Send_activity();
    String out3;
    String out1="        ";
    byte[] out=out1.toString().getBytes();



    public Decrypt() throws KeyException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        ciphertextp=(EditText) findViewById(R.id.ciphertextp);
        button=(Button) findViewById(decrypt);


       // byte[] bytes=ciphertextp.getText().toString().getBytes();

    }


}
