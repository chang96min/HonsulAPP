package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child(id).child("userPwd");
                    if(pwd.equals(a)){
//                        비번 확인
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent movINT = new Intent(MainActivity.this, RoomListActivity.class);
                        movINT.putExtra("userId",id);
                        startActivity(movINT);
                        idEDIT.setText("");
                        pwdEDIT.setText("");
                        return;
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"비밀번호가 틀렸습니다!",Toast.LENGTH_SHORT).show();
                        return;
                    }
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

        Intent intent = new Intent(MainActivity.this, BT_Activity.class);
        startActivity(intent);
    }
    // 변수 초기화
    private void init(){
        logo_img=findViewById(R.id.logo_img);
        loginBTN=findViewById(R.id.loginBTN);
        joinBTN=findViewById(R.id.joinBTN);
        idEDIT=findViewById(R.id.idEDIT);
        pwdEDIT=findViewById(R.id.pwdEDIT);

        pwdEDIT.setImeOptions(EditorInfo.IME_ACTION_DONE); // 키보드 확인 버튼 클릭시
        idEDIT.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(idEDIT.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });
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