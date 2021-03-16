package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ClientAddActivity extends AppCompatActivity {
    private EditText ip_edit_client,port_edit_client;
    private Button clientaddBTN2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add);
        init();
    }
    private void init(){
        clientaddBTN2=findViewById(R.id.clientaddBTN2);
        ip_edit_client=findViewById(R.id.ip_edit_client);
        port_edit_client=findViewById(R.id.port_edit_client);
    }
    public void onClick(View v){
        if(v.getId()==R.id.clientaddBTN2){
            Intent clientaddINT=new Intent(ClientAddActivity.this,ClientListViewActivity.class);
            startActivity(clientaddINT);

        }
    }
}