package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ClientListViewActivity extends AppCompatActivity {
    private Button clientaddBTN;
    private ListView client_listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list_view);
        init();
    }

    private void init(){
        client_listview=findViewById(R.id.client_listview);
        clientaddBTN=findViewById(R.id.clientaddBTN);
    }
    public void onClick(View v){
        if(v.getId()==R.id.clientaddBTN){
            Intent clientaddINT=new Intent(ClientListViewActivity.this,ClientAddActivity.class);
            startActivity(clientaddINT);

        }
    }
}