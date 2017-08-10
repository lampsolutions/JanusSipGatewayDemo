package de.lamp_solutions.janussipgatewaydemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import de.lamp_solutions.janussipgatewaydemo.Domain.SipGateway;

public class MainActivity extends AppCompatActivity {

    private static final String JANUSURI="ws://192.168.17.20:8188";

    private EditText proxy;
    private EditText identity;
    private EditText domain;
    private EditText name;
    private EditText secret;
    private EditText target;

    private LinearLayout callContainer;

    SipGateway sipGateway;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            Log.v(TAG, "Permission granted");
        }
    }


    public void register(View arg0){
        proxy   = (EditText)findViewById(R.id.proxy);
        identity   = (EditText)findViewById(R.id.identity);
        domain   = (EditText)findViewById(R.id.domain);
        name   = (EditText)findViewById(R.id.name);
        secret   = (EditText)findViewById(R.id.secret);
        callContainer   = (LinearLayout) findViewById(R.id.callContainer);


        sipGateway = new SipGateway();
        sipGateway.setJanusUri(JANUSURI);
        sipGateway.configureCredentials(proxy.getText().toString(),
                identity.getText().toString(),
                domain.getText().toString(),
                name.getText().toString(),
                secret.getText().toString());

        sipGateway.initializeMediaContext(MainActivity.this, true, true, false, null,this);

        sipGateway.Start();
        callContainer.setVisibility(View.VISIBLE);


    }

    public void call(View arg0){
        Log.v(TAG, "calling");

        target   = (EditText)findViewById(R.id.target);

        sipGateway.call(target.getText().toString());
    }


}
