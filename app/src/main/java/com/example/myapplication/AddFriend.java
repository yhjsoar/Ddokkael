package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AddFriend extends Activity {
    private DatabaseReference mPostReference;

    EditText editText;
    Button button;
    TextView textView;
    ImageView imageView;

    String id;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friend);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        editText = (EditText)findViewById(R.id.findFriend);
        button = (Button)findViewById(R.id.findFriendBtn);
        textView = (TextView)findViewById(R.id.findFriendRst);
        imageView = (ImageView)findViewById(R.id.gB);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("button",0);
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = editText.getText().toString();
                if(id.length()==0){
                    Toast.makeText(AddFriend.this, "바르게 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                check();
            }
        });

    }

    public void check(){
        mPostReference.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isExist = false, isFriend = false;
                for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.id)).getChildren()){
                    FirebaseID get = postSnapshot.getValue(FirebaseID.class);
                    if(get.id.equals(id)){
                        isExist = true;
                        break;
                    }
                }
                if(isExist){
                    for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.person)).child(name).child(getString(R.string.friend)).getChildren()){
                        FirebaseFriend get = postSnapshot.getValue(FirebaseFriend.class);
                        if(get.name.equals(id)){
                            isFriend = true;
                            break;
                        }
                    }
                }
                if(!isExist){
                    textView.setText("아이디가 존재하지 않습니다.");
                } else if(isFriend){
                    textView.setText("이미 친구입니다.");
                } else{
                    textView.setText("친구 요청을 보내겠습니까?");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
}
