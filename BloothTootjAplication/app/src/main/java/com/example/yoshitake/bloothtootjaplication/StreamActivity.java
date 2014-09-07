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

    @InjectView(R.id.message)
    TextView messageText;
    @InjectView(R.id.sendedMessage)
    EditText sendedMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("STATE","OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        ButterKnife.inject(this);
        message = "HOGEHOGE";
        intent = getIntent();

        String mes = intent.getStringExtra("message");
        if(mes!=null){
            messageText.setText(mes);
        }
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

    @OnClick(R.id.client_button)
    void clientStart(){
        //TODO:これonCreateでいいのではないか
        if(intent.getParcelableExtra("serverDevice")==null){
            Toast.makeText(this,"Getting server device failsd",Toast.LENGTH_LONG).show();
            //return;
        }
        //TODO:ボタン押すことで相互に送受信する（まあ送りっぱでもいいけど。）
        //今でもできてるんですかね。=>ちょっとできてた気はするけど、なんか落ちる
        //多分遷移してるんだけどそのときonCreate通ってなくて死んでる気がする。
        //OnCeate、通ってるっぽい。ただしintentに渡されてないからアレるのかな
       // if(device==null) {
            BluetoothDevice device = intent.getParcelableExtra("serverDevice");
        //    Log.d("BUG","NULLやで");
        //}
        message = sendedMessage.getText().toString();
        //Log.d("SUMAHONAME:",device.getName());
        BluetoothClientThread BtClientThread = new BluetoothClientThread(this, message, device, mBtAdapter);
        BtClientThread.start();
    }
}
