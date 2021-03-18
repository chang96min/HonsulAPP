package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

// 방 생성 화면

public class AddRoomActivity extends AppCompatActivity {
    // 로그 확인
    private final String TAG="AddRoomActivity";

    //    variable
    private EditText roomnameEDIT,roompwdEDIT;
    private ImageButton addroomBTN,cancelroomBTN;
    private String roomId;
    private String userId;
    private Random random=new Random();

    //    firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference_user = firebaseDatabase.getReference();

    private FirebaseAuthException auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        init();
        // Intent로 넘어온 userId 값
        Intent userINT=this.getIntent();
        userId=userINT.getStringExtra("userId");

        databaseReference = FirebaseDatabase.getInstance().getReference("Room");
        databaseReference_user = FirebaseDatabase.getInstance().getReference("User");
    }
    private void init(){
        roomnameEDIT=findViewById(R.id.roomnameEDIT);
        roompwdEDIT=findViewById(R.id.roompwdEDIT);
        addroomBTN=findViewById(R.id.addroomBTN);
        cancelroomBTN=findViewById(R.id.cancelroomBTN);

        roomnameEDIT.setImeOptions(EditorInfo.IME_ACTION_DONE); // 키보드 확인 버튼 클릭시
        roompwdEDIT.setImeOptions(EditorInfo.IME_ACTION_DONE); // 키보드 확인 버튼 클릭시

    }
    public void onClick(View v){
        if(v.getId()==R.id.addroomBTN){
            // DB에 방 추가
            if(roomnameEDIT.getText().length()>0 && roompwdEDIT.getText().length()>0) {
                makeNewRoom();
                Intent movINT=new Intent(AddRoomActivity.this, UserListActivity.class);
                movINT.putExtra("userId",userId);
                movINT.putExtra("roomId",roomId);
                startActivity(movINT);
                roomnameEDIT.setText("");
                roompwdEDIT.setText("");

            }
                 else {
                    Toast.makeText(this,"입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        else if (v.getId()==R.id.cancelroomBTN){
            finish();
        }
    }
    void makeNewRoom()
    {
        // 랜덤으로 방 아이디 생성
        roomId=String.valueOf(random.nextInt(200));
        databaseReference.child(roomId).child("roomName").setValue(roomnameEDIT.getText().toString());
        databaseReference.child(roomId).child("roomPwd").setValue(roompwdEDIT.getText().toString());
        databaseReference.child(roomId).child("userId").setValue(userId);
        databaseReference_user.child(userId).child("roomId").setValue(roomId);
        Toast.makeText(getApplicationContext(), "방 생성 완료", Toast.LENGTH_SHORT).show();
    }
}