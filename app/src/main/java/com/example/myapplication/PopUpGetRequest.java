package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PopUpGetRequest extends Activity {
    private DatabaseReference mPostReference;

    Button btn, btn2;
    TextView textView;

    String name;
    String friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_get_request);

        btn = (Button)findViewById(R.id.btn);
        btn2 = (Button)findViewById(R.id.plusfriend);
        textView = (TextView)findViewById(R.id.requestfriend);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        friend = intent.getStringExtra("friend");
        textView.setText(friend+"의 친구요청");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endPopUp();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Map<String, Object> postValues2 = null;
        if(add){
            DataFirebaseFriend me = new DataFirebaseFriend(friend);
            DataFirebaseFriend fr = new DataFirebaseFriend(name);
            postValues = me.toMap();
            postValues2 = fr.toMap();
        }
        childUpdates.put("/list/"+getString(R.string.person)+"/"+friend+"/"+getString(R.string.friend)+"/"+name, postValues2);
        childUpdates.put("/list/"+getString(R.string.person)+"/"+name+"/"+getString(R.string.friend)+"/"+friend, postValues);
        mPostReference.updateChildren(childUpdates);
        Toast.makeText(PopUpGetRequest.this, "친구 요청을 수락하였습니다.", Toast.LENGTH_SHORT).show();
    }

    public void check(){
        mPostReference.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.request)).child(name).child("from").getChildren()){
                    DataFirebaseFriend get = postSnapshot.getValue(DataFirebaseFriend.class);
                    if(get.name.equals(friend)){
                        postSnapshot.getRef().removeValue();
                        break;
                    }
                }
                for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.request)).child(friend).child("to").getChildren()){
                    DataFirebaseFriend get = postSnapshot.getValue(DataFirebaseFriend.class);
                    if(get.name.equals(name)){
                        postSnapshot.getRef().removeValue();
                        break;
                    }
                }
                postFirebaseDatabase(true);
                endPopUp();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void endPopUp(){
        Intent intent = new Intent();
        intent.putExtra("button",0);
        setResult(RESULT_OK, intent);

        finish();
    }
}
