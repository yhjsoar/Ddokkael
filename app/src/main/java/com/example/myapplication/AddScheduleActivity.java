package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddScheduleActivity extends Activity {

    EditText schedule_name, schedule_memo;
    Button add, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_schedule);

        //UI 객체생성
        schedule_name = (EditText)findViewById(R.id.edit_schedule_name);
        schedule_memo = (EditText)findViewById(R.id.edit_schedule_memo);
        add = (Button)findViewById(R.id.btn_add);
        cancel = (Button)findViewById(R.id.btn_cancel);

        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("button",1);
                setResult(RESULT_OK, intent);

                finish();
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
}