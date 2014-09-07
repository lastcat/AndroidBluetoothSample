package com.example.yoshitake.bloothtootjaplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StreamActivity extends Activity {

    BluetoothAdapter mBtAdapter;
    String message;
    Intent intent;

    @InjectView(R.id.message)
    TextView messageText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        // TODO techboosterの方の実装を比較して差を吸収する
        //多分クライアントはちゃんとしたdeviceを渡さないとダメっぽい
        //表示に使ってる情報から取った方がよさそう
        //そもそもforで出してる気がするからそれ渡せばいいんでね？
        BluetoothDevice device = intent.getParcelableExtra("sumaho");
        Log.d("SUMAHONAME:",device.getName());
        //ListView listView = (ListView) parent;
        //BluetoothDevice device = foundDeviceList.get(offSet + position);
        BluetoothClientThread BtClientThread = new BluetoothClientThread(this, "hogehoge", device, mBtAdapter);
        BtClientThread.start();
    }
}
