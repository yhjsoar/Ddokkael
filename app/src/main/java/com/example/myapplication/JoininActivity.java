package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class JoininActivity extends Activity {
    private DatabaseReference mPostReference;

    EditText edit_join_id, edit_join_pw;
    Button join_in, cancel;
    String id;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_joinin);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        //UI 객체생성
        edit_join_id = (EditText)findViewById(R.id.edit_join_id);
        edit_join_pw = (EditText)findViewById(R.id.edit_join_pw);
        join_in = (Button)findViewById(R.id.button_join);
        cancel = (Button)findViewById(R.id.button_join_cancel);

        join_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = edit_join_id.getText().toString();
                pw = edit_join_pw.getText().toString();

                // 정상적인 회원가입인지 확인
                if(id.length()*pw.length()==0){
                    Toast.makeText(JoininActivity.this, "바르게 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    check();
                }
//
//                Intent intent = new Intent();
//                intent.putExtra("button", 1);
//                intent.putExtra("ID",id);
//                intent.putExtra("PW", pw);
//                setResult(RESULT_OK, intent);
//
//                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("button",0);
                setResult(RESULT_OK, intent);

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
        mPostReference.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isExist = false;
                for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.id)).getChildren()){
                    FirebaseID get = postSnapshot.getValue(FirebaseID.class);
                    if(get.id.equals(id)){
                        isExist = true;
                        break;
                    }
                }
                if(isExist){
                    Toast.makeText(JoininActivity.this, "id already exists.", Toast.LENGTH_SHORT).show();
                } else{
                    postFirebaseDatabase(true);

                    Intent intent = new Intent();
                    intent.putExtra("button", 1);
                    intent.putExtra("ID",id);
                    intent.putExtra("PW", pw);
                    setResult(RESULT_OK, intent);

                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebaseID idinfo = new FirebaseID(id, pw);
            postValues = idinfo.toMap();
        }
        childUpdates.put("/list/"+getString(R.string.id)+"/"+id, postValues);
        mPostReference.updateChildren(childUpdates);
        Toast.makeText(JoininActivity.this, "sign up complete", Toast.LENGTH_SHORT).show();
    }
}
