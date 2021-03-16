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

    //    firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuthException auth;

    private ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            String id=String.valueOf(idEDIT.getText());
            String pwd=String.valueOf(pwdEDIT.getText());
            String a = dataSnapshot.child(id).child("userPwd").getValue().toString();
            while (child.hasNext()) {
//                존재하는 아이디인지 확인
                if (id.equals(child.next().getKey())) {
//                    databaseReference.addValueEventListener();
                    Log.i(TAG,"값 확인"+databaseReference.child(id).child("userPwd"));
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child(id).child("userPwd");
                    Log.i(TAG,"값 확인"+a);
                    if(pwd.equals(a)){
//                        비번 확인

                        Log.i(TAG,"로그인 완료");
                        Intent moveINT = new Intent(MainActivity.this, MainActivity2.class);
                        moveINT.putExtra("userId",id);
                        startActivity(moveINT);
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
            // 로그인 선택
            // 만약 idEDIT가 User에 있고, if pwdEDIT가 일치하면
            databaseReference.addListenerForSingleValueEvent(checkRegister);
        }
    }
}