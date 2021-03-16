package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class UserListActivity extends AppCompatActivity {
    private ListView user_listview;
    private Button halfBTN,fullBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        init();
    }
    private void init(){
        user_listview=findViewById(R.id.user_listview);
        halfBTN=findViewById(R.id.halfBTN);
        fullBTN=findViewById(R.id.fullBTN);
//        유저 리스트 계속 동기화
    }
    public void onClick(View v){
        if(v.getId()==R.id.halfBTN){

        }
        else if (v.getId()==R.id.fullBTN){

        }
    }
}