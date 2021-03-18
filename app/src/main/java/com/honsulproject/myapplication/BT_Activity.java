package com.honsulproject.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BT_Activity extends AppCompatActivity {

    // Member Variable
    private static final String TAG = "BT_Activity";
    public static final int REQUEST_ENABLE_BT = 1000;
    private final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ListView BT_list;

    //private ArrayList<String> name_data;
    private ArrayList<String> address_data;
    private ArrayAdapter<String> BTArrayAdapter;

    private BluetoothAdapter BTAdapter;
    private BluetoothSocket btSocket = null;


    // Member Method - override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_t_);
        Log.i(TAG, "onCreate");

        init();

        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        // 블루투스가 비활성화 상태이면 활성화를 위한 Intent 시작
        if (!BTAdapter.isEnabled()) {
            Log.i(TAG, "onCreate - if");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else {
            Log.i(TAG, "onCreate - else");
            SelectBT(); // 페어링된 블루투스 선택 메소드
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            // 블루투스를 허용하면 페어링된 블루투스 선택 메소드 호출
            if (resultCode == RESULT_OK) {
                SelectBT();
            }
            else {
                // 블루투스 허용 안 했을때 코드
            }
        }

    }

    // Member Method - custom
    private void init() {
        BT_list = findViewById(R.id.BT_list);

        // listItem 클릭시 블루투스 연결
        BT_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String BTname = BTArrayAdapter.getItem(position);
                String BTaddress = address_data.get(position);
                boolean flag = true;

                BluetoothDevice device = BTAdapter.getRemoteDevice(BTaddress);
                // 소켓 생성 및 연결
                try {
                    btSocket = createBluetoothSocket(device);
                    btSocket.connect();
                } catch (IOException e) {
                    flag = false;
                    Toast.makeText(BT_Activity.this,"연결 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if (flag) {
                    Toast.makeText(BT_Activity.this, "Bluetooth 연결 성공!",Toast.LENGTH_SHORT);
                    Util.connectedThread = new ConnectedThread(btSocket);
                    Util.connectedThread.start();
                    finish();
                }
            }
        });

        //name_data = new ArrayList<>();
        BTArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        address_data = new ArrayList<>();

        BT_list.setAdapter(BTArrayAdapter);
    }

    private void SelectBT() {
        Log.i(TAG, "SelectBT");

        BTArrayAdapter.clear();
        if ((address_data!=null) && !address_data.isEmpty()) {address_data.clear();}

        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            Log.i(TAG, "SelectBT - paired if");

            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.i(TAG, "SelectBT - name : " + deviceName + " address : " + deviceHardwareAddress);

                BTArrayAdapter.add(deviceName);
                address_data.add(deviceHardwareAddress);
            }
        }
    }

    public void onClick(View v) {
        if(BTAdapter.isDiscovering()){
            BTAdapter.cancelDiscovery();
        } else {
            if (BTAdapter.isEnabled()) {
                BTAdapter.startDiscovery();
                BTArrayAdapter.clear();
                if (BTArrayAdapter != null && !BTArrayAdapter.isEmpty()) {
                    BTArrayAdapter.clear();
                }
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);
            } else {
                Toast.makeText(getApplicationContext(), "bluetooth not on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                BTArrayAdapter.add(deviceName);
                address_data.add(deviceHardwareAddress);
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        try {
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection",e);
        }
        return  device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }

}