package com.example.yoshitake.bloothtootjaplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DeviceListActivity extends Activity {

    private DeviceAdapter dAdapter;
    private BluetoothAdapter mBtAdapter;

    private BluetoothDevice sumaho;
    private List<BluetoothDevice> bdList;

    @InjectView(R.id.deviceList)
    ListView bdListView;


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                dAdapter.add(device);
                bdListView.setAdapter(dAdapter);
                //TODO:ここもいらないかも。
                /*if (device.getBluetoothClass().getDeviceClass() == 524) {
                    Log.d("DEBUG", "UOOO");
                    sumaho = device;
                } else {
                    return;
                }*/
            }
        }
    };

    static String getBondState(int state) {
        String strState;

        switch (state) {
            case BluetoothDevice.BOND_BONDED:
                strState = "接続履歴あり";
                break;
            case BluetoothDevice.BOND_BONDING:
                strState = "接続中";
                break;
            case BluetoothDevice.BOND_NONE:
                strState = "接続履歴なし";
                break;
            default:
                strState = "エラー";
        }
        return strState;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        ButterKnife.inject(this);
        //mScanResult = (TextView) findViewById(R.id.nonPairedListTitle);
        // インテントフィルタの作成
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // ブロードキャストレシーバの登録
        registerReceiver(mReceiver, filter);
        //ListView。
        bdList = new ArrayList<BluetoothDevice>();
        //dAdapter = new DeviceAdapter(this,R.layout.activity_device_list,bdList);

        // BluetoothAdapterのインスタンス取得
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        // Bluetooth有効
        if (!mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        }
        // 周辺デバイスの検索開始
        mBtAdapter.startDiscovery();

        dAdapter = new DeviceAdapter(this,R.layout.activity_device_list,bdList);
        bdListView.setAdapter(dAdapter);

        bdListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListView listView = (ListView) parent;
                // クリックされたアイテムを取得します
                BluetoothDevice item = (BluetoothDevice) bdListView.getItemAtPosition(position);
                //TODO:intentにputExtraして渡す
                Intent intent = new Intent(getApplicationContext(),StreamActivity.class);
                intent.putExtra("serverDevice",item);
                intent.putExtra("message","LAStCAT");
                startActivity(intent);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device_list, menu);
        return true;
    }

    @OnClick(R.id.permission_button)
    void permissionDialog() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        int discoverable_sec = 0;
        int request_code = 0;
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, discoverable_sec);
        startActivityForResult(discoverableIntent, request_code);
    }

    @OnClick(R.id.stream_activity_button)
    void gotoStreamActivity() {
        Intent intent = new Intent(this, StreamActivity.class);
        intent.putExtra("sumaho", sumaho);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 0) {
            return;
        }

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "デバイス可視化に失敗しました", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "デバイス可視化に成功しました", Toast.LENGTH_LONG);
        }
    }


    private static class DeviceAdapter extends ArrayAdapter<BluetoothDevice> {
        private LayoutInflater mLayoutInflater;

        public DeviceAdapter(Context context, int textViewResourceId, List<BluetoothDevice> DeviceList) {
            super(context, textViewResourceId, DeviceList);
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BluetoothDevice item = (BluetoothDevice) getItem(position);
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.list_item, null);
            }

            TextView deviceName;
            TextView deviceClass;
            TextView deviceAddress;
            TextView deviceState;

            deviceName = (TextView) convertView.findViewById(R.id.deviceName);
            deviceClass = (TextView) convertView.findViewById(R.id.r_deviceClass);
            deviceAddress = (TextView) convertView.findViewById(R.id.deviceAddress);
            deviceState = (TextView) convertView.findViewById(R.id.r_deviceState);

            deviceName.setText("POYO: "+ item.getName());
            deviceClass.setText("HOGE: " + item.getBluetoothClass().getDeviceClass());
            deviceAddress.setText("PIYO: " + item.getAddress());
            deviceState.setText("FUGA" + getBondState(item.getBondState()));

            return convertView;

        }

    }

}