package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RoomListActivity extends AppCompatActivity {
    // 로그 확인
    private final String TAG = "UserListActivity";

    //    variable
    private ListView room_listview;

    // Adapter
    public ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    public HashMap<String,String> map=new HashMap<>();
    private SimpleAdapter adapter;
    private String roomId,roomName,userId,clickroomId;

    //    firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public ValueEventListener findRoom = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                HashMap<String,String> map=new HashMap<>();
                roomId = snapshot.getKey();
                roomName = dataSnapshot.child(roomId).child("roomName").getValue().toString();
                map.put("roomName",roomName);
                map.put("roomId",roomId);
                arrayList.add(map);
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
        setContentView(R.layout.activity_room_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Room"); // DB에서 값 불러옴
        init();

        // Intent로 넘어온 userId, roomId 값
        Intent userINT=this.getIntent();
        userId=userINT.getStringExtra("userId");

//         List 생성 및 관리 Adapter
        adapter=new SimpleAdapter(this,arrayList,
                android.R.layout.simple_list_item_2,
                new String[]{"roomName","roomId"},
                new int[]{android.R.id.text1,android.R.id.text2});

        // DB에서 userlist 불러옴
        databaseReference.addListenerForSingleValueEvent(findRoom);

        room_listview.setAdapter(adapter);

        Log.i(TAG,"클릭하기전 리스트뷰 뜸");

        room_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"클릭 "+((TextView)view.findViewById(android.R.id.text2)).getText());
                clickroomId= String.valueOf(((TextView)view.findViewById(android.R.id.text2)).getText());
            }
        });
    }
    private void init(){
        room_listview=findViewById(R.id.room_listview);
    }
}