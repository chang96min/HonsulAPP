package com.honsulproject.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

// 방 입장하면 보이는 UserListAct

public class UserListActivity extends AppCompatActivity {
    // 로그 확인
    private final String TAG = "UserListActivity";

    //    variable
    private ListView user_listview;
    private Button halfBTN, fullBTN,onceBTN,exitroomBTN,delroomBTN;
    private String userId,curRoomuserID;
    private String roomId;
    private String v;
    private String check;
    private String name;
    private String clickid="nonclick";

    // Adapter
    public ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    public HashMap<String,String> map=new HashMap<>();
    private SimpleAdapter adapter;

    //    firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference_room=firebaseDatabase.getReference();

    databaseReference.child(userID).addChildEventListener(new ChildEventListener(){

    });
    public ValueEventListener findDB = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            //roomId="87"; // 임시로 고정해놓은 roomId 값
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                HashMap<String,String> map=new HashMap<>();
                v = snapshot.getKey();
                check = dataSnapshot.child(v).child("roomId").getValue().toString();
                if (userId.equals(v)){
                    continue;
                }
                // 자신이 입장한 방 userlist에 자신은 리스트에 보여지지 않음!
                else{
                    if (check.equals(roomId)) {
                        name = dataSnapshot.child(v).child("userName").getValue().toString();
                        map.put("name",name);
                        map.put("id",v);
                        arrayList.add(map);
                        user_listview.setAdapter(adapter);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    //    현재 들어온 room의 userId 찾기
    public ValueEventListener finduserId = new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
        curRoomuserID= String.valueOf(dataSnapshot.child(roomId).child("userId").getValue());
        Log.i(TAG,"gg"+curRoomuserID);
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("User"); // DB에서 값 불러옴
        databaseReference_room = FirebaseDatabase.getInstance().getReference("Room"); // DB에서 값 불러옴

        init();

        // Intent로 넘어온 userId, roomId 값
        Intent userINT=this.getIntent();
        userId=userINT.getStringExtra("userId");
        roomId=userINT.getStringExtra("roomId");

//         List 생성 및 관리 Adapter
        adapter=new SimpleAdapter(this,arrayList,
                android.R.layout.simple_list_item_2,
                new String[]{"name","id"},
                new int[]{android.R.id.text1,android.R.id.text2});

        // DB에서 userlist 불러옴
            databaseReference.addListenerForSingleValueEvent(findDB);


//        adapter.notifyDataSetChanged();

        user_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickid= String.valueOf(((TextView)view.findViewById(android.R.id.text2)).getText());
            }
        });

    }
    private void init(){
        user_listview=findViewById(R.id.user_listview);
        halfBTN=findViewById(R.id.halfBTN);
        fullBTN=findViewById(R.id.onceBTN);
        onceBTN=findViewById(R.id.onceBTN);
        delroomBTN=findViewById(R.id.delroomBTN);
        exitroomBTN=findViewById(R.id.exitroomBTN);
    }
    public void onClick(View v){

        //       유저를 선택하고, 술 전달

        if(v.getId()==R.id.halfBTN){
            if (clickid!="nonclick"){
                //            선택한 유저
                databaseReference.child(clickid).child("value").setValue("H");
                Toast.makeText(this,"반잔",Toast.LENGTH_SHORT).show();
                clickid="nonclick";
                return;
            }
            Toast.makeText(this,"풀잔",Toast.LENGTH_SHORT).show();
        }
        if (v.getId()==R.id.fullBTN){
            if (clickid!="nonclick"){
                databaseReference.child(clickid).child("value").setValue("F");
                Toast.makeText(this,"풀잔",Toast.LENGTH_SHORT).show();
                clickid="nonclick";
                return;
            }
        }
        if (v.getId()==R.id.onceBTN){
            if (clickid!="nonclick"){
                databaseReference.child(clickid).child("value").setValue("O");
                Toast.makeText(this,"한잔",Toast.LENGTH_SHORT).show();
                clickid="nonclick";
                return;
            }
        }
        Toast.makeText(this,"상대 유저를 선택해주세요!",Toast.LENGTH_SHORT).show();

        //       유저가 방 삭제, 방 나가기 버튼을 눌렀을 때

        if (v.getId()==R.id.delroomBTN){
            // 현재 방의 userId와 intent로 전달받은 userId의 값이 같으면 
            // dialog로 찐으로 방 삭제할건지 물어보고 방삭제
            databaseReference.addListenerForSingleValueEvent(finduserId);
            Log.i(TAG,"gg"+curRoomuserID);
//            if (curRoomuserID.equals(userId)){
//                dialog_delroom();
//            }

        }
        if (v.getId()==R.id.exitroomBTN){
            // 현재 방의 userId와 intent로 전달받은 userId의 값이 다르면 (같으면 방 삭제를 눌러달라고 토스트ㄱㄱ)
            // dialog로 찐으로 방 나갈건지 물어보고, 방 나가고 해당 userId의 roomId를 "" 로 바꿈
            dialog_exitroom();
        }
    }
    public void dialog_delroom(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("방 삭제 확인").setMessage("방을 삭제하시겠습니까?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                //삭제 코드
                databaseReference_room.child(roomId).setValue(null);
                // 유저의 roomId가 현재 roomId와 같은 사람의 roomId는 "" 로 만듬
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            { //취소 버튼
                
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void dialog_exitroom(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("방 나가기").setMessage("방을 나가시겠습니까?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                //해당 userId의 roomId를 "" 로 바꿈
                databaseReference.child(userId).child("roomId").setValue("");
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            { //취소 버튼
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}