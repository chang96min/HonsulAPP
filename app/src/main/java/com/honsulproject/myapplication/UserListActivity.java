package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

// 방 입장하면 보이는 UserListAct

public class UserListActivity extends AppCompatActivity {
    // 로그 확인
    private final String TAG = "UserListActivity";

    //    variable
    private ListView user_listview;
    private Button halfBTN, fullBTN;
    private String userId;
    private String roomId;
    private String v;
    private String check;
    private String name;

    // Adapter
//    public ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    public ArrayList<String> nameArrayList=new ArrayList<>();
    public ArrayList<String> arrayList=new ArrayList<>();
//    public HashMap<String,String> map=new HashMap<>();
    private ArrayAdapter<String> adapter;

    //    firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    public ValueEventListener findDB = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            // 임시로 고정해놓은 roomId 값
            roomId="170";
            while(child.hasNext()) {
                v = child.next().getKey();
                check = dataSnapshot.child(v).child("roomId").getValue().toString();
                if (check.equals(roomId)) {
                    name = dataSnapshot.child(v).child("userName").getValue().toString();
                    nameArrayList.add(name);
                    arrayList.add(v);
//                    map.put("name",name);
//                    map.put("id",v);
//                    arrayList.add(v);
                    adapter.add(v);
                }
            }

            adapter.notifyDataSetChanged();

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        // DB에서 값 불러옴
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        init();

        // Intent로 넘어온 userId, roomId 값
        Intent userINT=this.getIntent();
        userId=userINT.getStringExtra("userId");
        roomId=userINT.getStringExtra("roomId");

        // List 생성 및 관리 Adapter
//        adapter=new SimpleAdapter(this,arrayList,
//                android.R.layout.simple_list_item_2,
//                new String[]{"name","id"},
//                new int[]{android.R.id.text1,android.R.id.text2});

        // DB에서 userlist 불러옴
        databaseReference.addListenerForSingleValueEvent(findDB);

        Log.i(TAG,"불러온 후 값 확인============"+arrayList.size());

    }
    private void init(){
        user_listview=findViewById(R.id.user_listview);
        halfBTN=findViewById(R.id.halfBTN);
        fullBTN=findViewById(R.id.fullBTN);

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        // ListView에 List 설정
        user_listview.setAdapter(adapter);
    }
    public void onClick(View v){
        if(v.getId()==R.id.halfBTN){
//            선택한 유저
        }
        else if (v.getId()==R.id.fullBTN){

        }
    }
}