package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ServerActivitiy extends AppCompatActivity {
    private EditText ip_edit_server,port_edit_server;
    private Button serveraddBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_activitiy);
        init();
    }

    private void init(){
        ip_edit_server=findViewById(R.id.ip_edit_server);
        port_edit_server=findViewById(R.id.port_edit_server);
        serveraddBTN=findViewById(R.id.serveraddBTN);
    }
    public void onClick(View v){
        if(v.getId()==R.id.serveraddBTN){
//            서버 열고
            Intent serveraddINT=new Intent(ServerActivitiy.this,UserListActivity.class);
            startActivity(serveraddINT);

        }
    }
}