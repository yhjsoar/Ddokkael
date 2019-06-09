package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class PopUpDaySelect extends Activity {

    String day;

    TextView textSun, textMon, textThu, textWed, textThur, textFri, textSat, textCancel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_day);

        //UI 객체생성
        textSun = (TextView)findViewById(R.id.textSun);
        textMon = (TextView)findViewById(R.id.textMon);
        textThu = (TextView)findViewById(R.id.textThu);
        textWed = (TextView)findViewById(R.id.textWed);
        textThur = (TextView)findViewById(R.id.textThur);
        textFri = (TextView)findViewById(R.id.textFri);
        textSat = (TextView)findViewById(R.id.textSat);

        textCancel = (TextView)findViewById(R.id.textCancel);

        textSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpDaySelect.this, AddScheduleActivity.class);
                intent.putExtra("day", "일요일");
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        textMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpDaySelect.this, AddScheduleActivity.class);
                intent.putExtra("day", "월요일");
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        textThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpDaySelect.this, AddScheduleActivity.class);
                intent.putExtra("day", "화요일");
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        textWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpDaySelect.this, AddScheduleActivity.class);
                intent.putExtra("day", "수요일");
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        textThur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpDaySelect.this, AddScheduleActivity.class);
                intent.putExtra("day", "목요일");
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        textFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpDaySelect.this, AddScheduleActivity.class);
                intent.putExtra("day", "금요일");
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        textSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUpDaySelect.this, AddScheduleActivity.class);
                intent.putExtra("day", "토요일");
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        textCancel.setOnClickListener(new View.OnClickListener() {
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