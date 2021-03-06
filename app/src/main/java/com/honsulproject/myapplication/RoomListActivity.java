package com.honsulproject.myapplication;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.honsulproject.myapplication.Room.RoomData;
import com.honsulproject.myapplication.Room.RoomDataAdapter;

import java.util.ArrayList;

public class RoomListActivity extends AppCompatActivity {
    // 로그 확인
    private final String TAG = "UserListActivity";

    //    variable
    private ListView room_listview;

    // Adapter
    public ArrayList<RoomData> arrayList = new ArrayList<>();
    private RoomDataAdapter adapter;
    private String userId,clickroomId,pwdEDIT,okroomPwd,i;

    //    firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference_user=firebaseDatabase.getReference();

    // 방 리스트 불러오는
    public ValueEventListener findRoom = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String roomId = snapshot.getKey();
                String roomhost = dataSnapshot.child(roomId).child("userId").getValue().toString();
                String roomName = dataSnapshot.child(roomId).child("roomName").getValue().toString();

                arrayList.add(new RoomData(R.drawable.malll, roomName, roomhost, roomId));
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
                databaseReference.child(clickroomId).child("userCnt").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()) {
                            Log.e(TAG, "Error getting data", task.getException());
                        }
                        else {
                            //Log.d(TAG,"task - getResult : " + task.getResult().toString());
                            //Log.d(TAG,"Value : " + (long) task.getResult().getValue());
                            long Cnt = (long) task.getResult().getValue();
                            Cnt++;
                            //Log.d(TAG, "Cnt : " + Cnt);
                            databaseReference.child(clickroomId).child("userCnt").setValue(Cnt);
                            //Log.d(TAG,"after Cnt = : " + (long) task.getResult().getValue());
                        }
                    }
                });
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
        adapter = new RoomDataAdapter(RoomListActivity.this, R.layout.roomdata_layout,arrayList);

        // DB에서 userlist 불러옴
        databaseReference.addListenerForSingleValueEvent(findRoom);


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
                clickroomId= ((TextView)view.findViewById(R.id.Roomlist_id)).getText().toString();
                Log.i(TAG,"clickroomId : " + clickroomId);
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
    public void onClick(View v) {
        if (v.getId() == R.id.newBTN) {
            // 방 생성
            Intent movINT = new Intent(RoomListActivity.this, AddRoomActivity.class);
            movINT.putExtra("userId", userId);
            startActivity(movINT);
        }
    }
}