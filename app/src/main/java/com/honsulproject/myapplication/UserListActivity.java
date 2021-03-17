package com.honsulproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button halfBTN, fullBTN,onceBTN;
    private String userId;
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
                    }
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
        databaseReference = FirebaseDatabase.getInstance().getReference("User"); // DB에서 값 불러옴

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

        user_listview.setAdapter(adapter);

        Log.i(TAG,"클릭하기전 리스트뷰 뜸");

        user_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"클릭 "+((TextView)view.findViewById(android.R.id.text2)).getText());
                clickid= String.valueOf(((TextView)view.findViewById(android.R.id.text2)).getText());
            }
        });

    }
    private void init(){
        user_listview=findViewById(R.id.user_listview);
        halfBTN=findViewById(R.id.halfBTN);
        fullBTN=findViewById(R.id.onceBTN);
        onceBTN=findViewById(R.id.onceBTN);
    }
    public void onClick(View v){
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
        else if (v.getId()==R.id.fullBTN){
            if (clickid!="nonclick"){
                databaseReference.child(clickid).child("value").setValue("F");
                Toast.makeText(this,"풀잔",Toast.LENGTH_SHORT).show();
                clickid="nonclick";
                return;
            }
        }
        else if (v.getId()==R.id.onceBTN){
            if (clickid!="nonclick"){
                databaseReference.child(clickid).child("value").setValue("O");
                Toast.makeText(this,"한잔",Toast.LENGTH_SHORT).show();
                clickid="nonclick";
                return;
            }
        }
        Toast.makeText(this,"상대 유저를 선택해주세요!",Toast.LENGTH_SHORT).show();
    }
}