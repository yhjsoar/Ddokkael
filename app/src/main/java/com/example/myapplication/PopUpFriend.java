package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PopUpFriend extends Activity {

    String name;
    String myname;

    TextView friend_schedule, friend_compare, friend_delete, cancel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_friend);

        //UI 객체생성
        friend_schedule = (TextView)findViewById(R.id.friend_schedule);
        friend_compare = (TextView)findViewById(R.id.friend_compare);
        friend_delete = (TextView)findViewById(R.id.friend_delete);
        cancel = (TextView)findViewById(R.id.friend_cancel);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        myname = intent.getStringExtra("myname");

        friend_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpFriend.this, FriendSchedule.class);
                intent.putExtra("mode", "schedule");
                intent.putExtra("name", name);
                startActivity(intent);

                finish();
            }
        });

        friend_compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpFriend.this, FriendSchedule.class);
                intent.putExtra("mode", "compare");
                intent.putExtra("name1", name);
                intent.putExtra("name2", myname);
                startActivity(intent);

                finish();
            }
        });

        friend_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 친구 삭제

                finish();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
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
}