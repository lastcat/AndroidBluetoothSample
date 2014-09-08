package com.example.yoshitake.bloothtootjaplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StreamActivity extends Activity {

    BluetoothAdapter mBtAdapter;
    String message;
    Intent intent;
    BluetoothDevice device;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("STATE","OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        ButterKnife.inject(this);
        message = "Thankyou";
        intent = getIntent();

        String mes = intent.getStringExtra("message");
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerThread BtServerThread = new BluetoothServerThread(this,message, mBtAdapter);
        BtServerThread.start();
    }

    protected void onResume(Bundle savedInstanceState){
        Log.d("STATE","OnResume");
        BluetoothDevice device = intent.getParcelableExtra("serverDevice");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button1)
    void b1ClientStart(){
        BluetoothDevice device = intent.getParcelableExtra("sumaho");
        message = "ありがとう";
        BluetoothClientThread BtClientThread = new BluetoothClientThread(this, message, device, mBtAdapter);
        BtClientThread.start();
    }

    @OnClick(R.id.button2)
    void b2ClientStart(){
        BluetoothDevice device = intent.getParcelableExtra("sumaho");
        message = "やさしいね";
        BluetoothClientThread BtClientThread = new BluetoothClientThread(this, message, device, mBtAdapter);
        BtClientThread.start();
    }

    @OnClick(R.id.button3)
    void b3ClientStart(){
        BluetoothDevice device = intent.getParcelableExtra("sumaho");
        message = "おつかれさま";
        BluetoothClientThread BtClientThread = new BluetoothClientThread(this, message, device, mBtAdapter);
        BtClientThread.start();
    }
}
