package com.example.yoshitake.bloothtootjaplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//TODO:途中で切るとヤバイので殺す？
//TODO:メッセージの扱いをもっと自由にする。あと取得順番が距離なのか適当なのかチエックする必要があるかも

public class MainActivity extends Activity {


    boolean btEnable;
    private final int REQUEST_ENABLE_BLUETOOTH = 10;
    BluetoothAdapter Bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Bt = BluetoothAdapter.getDefaultAdapter();
        if(!Bt.equals(null)){
            //Bluetooth対応端末の場合の処理
            Log.d("BUG","Bluetoothがサポートされてます。");
        }else{
            //Bluetooth非対応端末の場合の処理
            Log.d("BUG","Bluetoothがサポートれていません。");
            finish();
        }
        btEnable = Bt.isEnabled();
        if(btEnable == true){
            Intent intent = new Intent(this,DeviceListActivity.class);
            startActivity(intent);
            }else{
            //OFFだった場合、ONにすることを促すダイアログを表示する画面に遷移
            Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);

            Intent intent = new Intent(this,DeviceListActivity.class);
            startActivity(intent);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int ResultCode, Intent date){
        //ダイアログ画面から結果を受けた後の処理を記述
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(ResultCode == Activity.RESULT_OK){
                //BluetoothがONにされた場合の処理
                Log.d("MES","BluetoothをONにしてもらえました。");
            }else{
                Log.d("mes","BluetoothがONにしてもらえませんでした。");
                finish();
            }
        }
    }

    @OnClick(R.id.button)
    void OnClickButton(){
        Intent intent = new Intent(this,DeviceListActivity.class);
        startActivity(intent);
    }
}
