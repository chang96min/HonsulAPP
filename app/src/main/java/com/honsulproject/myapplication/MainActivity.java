package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

// 앱 메인
public class MainActivity extends AppCompatActivity {
    // 로그 확인
    private final String TAG="MainActivity";

    // xml variable
    private ImageView logo_img;
    private Button joinBTN,loginBTN;
    private EditText idEDIT,pwdEDIT;
    private String id,pwd,a;

    //    firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuthException auth;

    public ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            id=String.valueOf(idEDIT.getText());
            pwd=String.valueOf(pwdEDIT.getText());
            a = String.valueOf(dataSnapshot.child(id).child("userPwd").getValue());
            while (child.hasNext()) {
//                존재하는 아이디인지 확인
                if (id.equals(child.next().getKey())) {
//                    databaseReference.addValueEventListener();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child(id).child("userPwd");
                    if(pwd.equals(a)){
//                        비번 확인
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent movINT = new Intent(MainActivity.this, MainActivity2.class);
                        movINT.putExtra("userId",id);
                        startActivity(movINT);
                        return;
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"비밀번호가 틀렸습니다!",Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    databaseReference.removeEventListener(this);
//                    return;
                }
            }
            Toast.makeText(getApplicationContext(),"존재하지 않는 아이디입니다!",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
    }
    // 변수 초기화
    private void init(){
        logo_img=findViewById(R.id.logo_img);
        joinBTN=findViewById(R.id.joinBTN);
        loginBTN=findViewById(R.id.loginBTN);
        idEDIT=findViewById(R.id.idEDIT);
        pwdEDIT=findViewById(R.id.pwdEDIT);
    }
    // 버튼 선택
    public void onClick(View v){
        if(v.getId()==R.id.joinBTN){
            // 회원가입 선택
            Intent joinINT=new Intent(MainActivity.this,JoinActivity.class);
            startActivity(joinINT);
        }
        else if (v.getId()==R.id.loginBTN){
            databaseReference.addListenerForSingleValueEvent(checkRegister);
        }
    }
}