package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// 회원가입 Act


public class JoinActivity extends AppCompatActivity {
    // 로그 확인
    private final String TAG="JoinActivity";
    
    //    variable
    private Button joinokBTN,joincancelBTN;
    private EditText joinidEDIT,joinpwdEDIT,joinnameEDIT,joinpwdokEDIT;

    //    firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuthException auth;

    private ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            while (child.hasNext()) {
                if (joinidEDIT.getText().toString().equals(child.next().getKey())) {
                    Toast.makeText(getApplicationContext(), "존재하는 아이디 입니다.", Toast.LENGTH_LONG).show();
                    databaseReference.removeEventListener(this);
                    return;
                }
            }
            makeNewId();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        init();

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

    }
    // 변수 초기화
    private void init(){
        joinokBTN=findViewById(R.id.joinokBTN);
        joincancelBTN=findViewById(R.id.joincancelBTN);
        joinnameEDIT=findViewById(R.id.joinnameEDIT);
        joinidEDIT=findViewById(R.id.joinidEDIT);
        joinpwdEDIT=findViewById(R.id.joinpwdEDIT);
        joinpwdokEDIT=findViewById(R.id.joinpwdokEDIT);
    }
    // 버튼 선택
    public void onClick(View v){
        if(v.getId()==R.id.joinokBTN){
            // 가입 선택
            if(joinidEDIT.getText().length()>0 && joinpwdEDIT.getText().length()>0 && joinpwdokEDIT.getText().length()>0&& joinnameEDIT.getText().length()>0) {
                if (String.valueOf(joinpwdEDIT.getText()).equals(String.valueOf(joinpwdokEDIT.getText().toString()))) {
                    databaseReference.addListenerForSingleValueEvent(checkRegister);
                    Log.i(TAG, "db 추가");
                    finish();
//                Intent joinINT=new Intent(JoinActivity.this,MainActivity.class);
//                startActivity(joinINT);
                } else {
                    Log.i(TAG, "비번 다름" + joinpwdEDIT.getText().toString() + "," + joinpwdokEDIT.getText().toString());
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT);
                }
            }
            else{
                Toast.makeText(this,"입력해주세요",Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getId()==R.id.joincancelBTN){
            // 가입 취소 선택
            finish();

        }
    }
    void makeNewId()
    {
        databaseReference.child(joinidEDIT.getText().toString()).child("userName").setValue(joinnameEDIT.getText().toString());
        databaseReference.child(joinidEDIT.getText().toString()).child("userPwd").setValue(joinpwdEDIT.getText().toString());
        databaseReference.child(joinidEDIT.getText().toString()).child("roomId").setValue("0");
        databaseReference.child(joinidEDIT.getText().toString()).child("value").setValue("0");
        Toast.makeText(getApplicationContext(), "가입 완료", Toast.LENGTH_SHORT).show();
    }
}