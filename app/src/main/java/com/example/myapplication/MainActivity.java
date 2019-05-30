package com.example.myapplication;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // timetable_data.add(date_data)
    private ArrayList<ArrayList<ArrayList<String>>> timetable_data = null; // 시간표 (요일 정보를 담는 리스트)
    private ArrayList<ArrayList<String>> date_data = null; // 요일 정보 (일정을 담는 리스트)
    private ArrayList<String> data = null; // 일정 (일정 정보를 담는 리스트)
    ViewPager pager;
    Button btn[] = new Button[3];
    int v = 0, d = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        // data = <schedule, start h, start m, fin h, fin m>
        data.add("database_class");
        data.add("9");
        data.add("0");
        data.add("10");
        data.add("30");

        date_data.add(data);
        timetable_data.add(date_data);
        timetable_data.add(null);
        timetable_data.add(null);
        timetable_data.add(null);
        timetable_data.add(null);
        timetable_data.add(null);

        data = null;

        data.add("app");
        data.add("18");
        data.add("0");
        data.add("21");
        data.add("0");

        date_data.add(data);
        timetable_data.add(date_data);
        */

        // Contents
        pager = (ViewPager)findViewById(R.id.viewPager);
        CustomAdapter adapter = new CustomAdapter(getLayoutInflater());
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
                String result = data.getStringExtra("result");
                Toast.makeText(MainActivity.this,result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
