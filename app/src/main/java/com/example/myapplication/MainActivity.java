package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // timetable_data.add(date_data)
    private ArrayList<ArrayList<ArrayList<String>>> timetable_data = null; // 시간표 (요일 정보를 담는 리스트)
    private ArrayList<ArrayList<String>> date_data = null; // 요일 정보 (일정을 담는 리스트)
    private ArrayList<String> data = null; // 일정 (일정 정보를 담는 리스트)
    ViewPager pager;
    Button btn[] = new Button[3];
    ImageView friendPlus;
    int v = 0, d = 1;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        name = intent.getStringExtra("ID");


        // Contents
        pager = (ViewPager)findViewById(R.id.viewPager);
        CustomAdapter adapter = new CustomAdapter(getLayoutInflater(), name, this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);


        // Menu Buttons
        btn[0] = (Button)findViewById(R.id.button1);
        btn[1] = (Button)findViewById(R.id.button2);
        btn[2] = (Button)findViewById(R.id.button3);

        Button add_schedule = (Button)findViewById(R.id.button4);

        btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
                Toast.makeText(MainActivity.this,"calendar", Toast.LENGTH_SHORT).show();
            }
        });

        btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
                Toast.makeText(MainActivity.this,"timetable", Toast.LENGTH_SHORT).show();
            }
        });

        btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);
                Toast.makeText(MainActivity.this,"friends", Toast.LENGTH_SHORT).show();
            }
        });

        add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddScheduleActivity.class);
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                int popup_add = data.getExtras().getInt("button");
                if(popup_add == 1){
                    Toast.makeText(MainActivity.this, "일정을 추가했습니다.", Toast.LENGTH_SHORT).show();
                    String schedule_name = data.getExtras().getString("name");
                    String schedule_info = data.getExtras().getString("info");
                }else{
                    Toast.makeText(MainActivity.this, "일정 추가를 취소했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(requestCode==2){

        }
    }
}
