package com.honsulproject.myapplication;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    // Member Variable
    private static final String TAG = "ConnectedThread";

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;


    // Constructor
    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG,"소켓이 생성되지 않았습니다.",e);
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    // run()의 경우 정확히 어떤 동작을 하는지 이해하지 못했음. 좀 더 학습이 필요함
    @Override
    public void run() {
        Log.i(TAG, "Run mconnectedThread");
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs

        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(String input) {
        byte[] bytes = input.getBytes();           // 입력받은 문자열을 바이트 배열로 변환
        Log.i(TAG, "Write");
        try {
            Log.i(TAG, "write - try");
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.d(TAG,"전송에 실패했습니다.");
        }
    }

    // 버튼을 클릭했을 때 실행되는 메소드
    public void ClickBTN(char bytes) {
        Log.i(TAG, "ClickBTN");
        try {
            Log.i(TAG, "ClickBTN - try");
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.d(TAG,"전송에 실패했습니다.");
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG,"소켓을 닫을 수 없습니다.");
        }
    }
}