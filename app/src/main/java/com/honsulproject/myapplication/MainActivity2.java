package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// 방 생성, 입장 Act

public class MainActivity2 extends AppCompatActivity {
    // 로그 확인
    private final String TAG="MainActivity2";

    //    variable
    private Button newBTN,enterBTN;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();

        // Intent로 넘어온 userId 값
        Intent userINT=this.getIntent();
        userId=userINT.getStringExtra("userId");
    }
    private void init(){
        newBTN=findViewById(R.id.newBTN);
        enterBTN=findViewById(R.id.enterBTN);
    }
    public void onClick(View v){
        if(v.getId()==R.id.newBTN){
            // 방 생성
            Intent movINT=new Intent(MainActivity2.this, AddRoomActivity.class);
            movINT.putExtra("userId",userId);
            startActivity(movINT);
        }
        else if (v.getId()==R.id.enterBTN){
            // 방 입장
            Intent movINT=new Intent(MainActivity2.this,RoomListActivity.class);
            movINT.putExtra("userId",userId);
            startActivity(movINT);

        }
    }
}