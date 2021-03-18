package com.honsulproject.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
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
    private String roomId,roomName,userId,clickroomId,pwdEDIT,okroomPwd,i;

    //    firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference_user=firebaseDatabase.getReference();

    // 방 리스트 불러오는
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
                room_listview.setAdapter(adapter);
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    // 방 비번 찾는, 맞으면 입장
    public ValueEventListener findPwd = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            // 클릭한 방의 비번 => okroomPwd
            okroomPwd = String.valueOf(dataSnapshot.child(clickroomId).child("roomPwd").getValue());
            // 비번 맞는지 아닌지 확인
            if(pwdEDIT.equals(okroomPwd)){
                Toast.makeText(getApplicationContext(), "방 입장", Toast.LENGTH_SHORT).show();
                Intent movINT = new Intent(RoomListActivity.this, UserListActivity.class);
                movINT.putExtra("userId",userId);
                movINT.putExtra("roomId",clickroomId);
                startActivity(movINT);
                // user의 roomId 정보 바꿈
                databaseReference_user.child(userId).child("roomId").setValue(clickroomId);
                return;
            }
            else{
                Toast.makeText(getApplicationContext(),"비밀번호가 틀렸습니다!",Toast.LENGTH_SHORT).show();
                return;
            }
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
        databaseReference_user = FirebaseDatabase.getInstance().getReference("User"); // DB에서 값 불러옴
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

//        room_listview.setAdapter(adapter);

        // Roomlist 갱신하기 위해서 필요
        FirebaseDatabase.getInstance().getReference("Room").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("RoomListActivity", "ChildEventListener - onChildAdded : " + dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("RoomListActivity", "ChildEventListener - onChildChanged : " + s);
                arrayList.clear();
                databaseReference.addListenerForSingleValueEvent(findRoom);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("RoomListActivity", "ChildEventListener - onChildRemoved : " + dataSnapshot.getKey());
                arrayList.clear();
                databaseReference.addListenerForSingleValueEvent(findRoom);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("RoomListActivity", "ChildEventListener - onChildMoved" + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MainActivity", "ChildEventListener - onCancelled" + databaseError.getMessage());
            }
        });

        room_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"클릭 "+((TextView)view.findViewById(android.R.id.text2)).getText());
                clickroomId= String.valueOf(((TextView)view.findViewById(android.R.id.text2)).getText());
                dialog();
            }
        });
    }
    private void init(){
        room_listview=findViewById(R.id.room_listview);
    }
    public void dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 입력").setMessage("입장하려는 방의 비밀번호를 입력하세요!");
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(et);

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            { //취소 버튼
            }
        });

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            { //비밀번호 확인 버튼
                // DB에서 pwd 확
                pwdEDIT=String.valueOf(et.getText());
                databaseReference.addListenerForSingleValueEvent(findPwd);
                //비밀번호가 선택한 방의 아이디에 있는 비번과 같다면~
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}