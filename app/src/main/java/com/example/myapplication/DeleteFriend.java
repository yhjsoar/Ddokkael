package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteFriend extends Activity {
    DatabaseReference databaseReference;

    String name; // 친구
    String myname; // 본인

    Button yes, no;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_delete_friend);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //UI 객체생성
        yes = (Button)findViewById(R.id.cancel_btn);
        no = (Button)findViewById(R.id.cancel_cancel_btn);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        myname = intent.getStringExtra("myname");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 레진군 친구 삭제하세요
                check();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    public void check(){
        databaseReference.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.child(getString(R.string.person)).child(myname).child(getString(R.string.friend)).getChildren()){
                    FirebaseFriend get = post.getValue(FirebaseFriend.class);
                    if(get.name.equals(name)){
                        post.getRef().removeValue();
                        break;
                    }
                }
                for(DataSnapshot post : dataSnapshot.child(getString(R.string.person)).child(name).child(getString(R.string.friend)).getChildren()){
                    FirebaseFriend get = post.getValue(FirebaseFriend.class);
                    if(get.name.equals(myname)){
                        post.getRef().removeValue();
                        break;
                    }
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
