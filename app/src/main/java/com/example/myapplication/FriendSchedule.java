package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class FriendSchedule extends Activity {

    String name1 = "lezin";
    String mode, name2;
    String person_list = "Person_List";
    String schedule = "schedule_list";
    String request = "request_list";
    String id_list = "id_list";
    String friend = "Friend_List";
    String[] date = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    int year_, month_, date_, day_;
    int today_year, today_month, today_date, today_day;

    LayoutInflater inflater;
    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
    LinearLayout[] layout = new LinearLayout[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        if(mode.equals("schedule")){
            name1 = intent.getStringExtra("name");
            long now = System.currentTimeMillis();
            final Date date = new Date(now);
            year_ = date.getYear();
            month_ = date.getMonth();
            date_ = date.getDate(); // 날짜
            day_ = date.getDay(); // 요일

            today_year = year_+1900;
            today_month = month_+1;
            today_date = date_;
            today_day =day_;

            layout[0] = (LinearLayout)findViewById(R.id.layout1);
            layout[1] = (LinearLayout)findViewById(R.id.layout2);
            layout[2] = (LinearLayout)findViewById(R.id.layout3);
            layout[3] = (LinearLayout)findViewById(R.id.layout4);
            layout[4] = (LinearLayout)findViewById(R.id.layout5);
            layout[5] = (LinearLayout)findViewById(R.id.layout6);
            layout[6] = (LinearLayout)findViewById(R.id.layout7);
            layout[7] = (LinearLayout)findViewById(R.id.layout8);

            LinearLayout.LayoutParams linearLayoutParams_date = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            for(int i = 0; i<8; i++){
                TextView textView_date = new TextView(this);
                textView_date.setBackgroundResource(R.drawable.timetable_time_edge);
                textView_date.setLayoutParams(linearLayoutParams_date);
                String timetable_date = null;
                switch(i){
                    case 0:
                        timetable_date = " ";
                        break;
                    case 1:
                        timetable_date = "일";
                        break;
                    case 2:
                        timetable_date = "월";
                        break;
                    case 3:
                        timetable_date = "화";
                        break;
                    case 4:
                        timetable_date = "수";
                        break;
                    case 5:
                        timetable_date = "목";
                        break;
                    case 6:
                        timetable_date = "금";
                        break;
                    case 7:
                        timetable_date = "토";
                        break;
                    default:
                        break;
                }

                textView_date.setText(timetable_date);
                textView_date.setGravity(Gravity.CENTER);
                layout[i].addView(textView_date);
            }

            LinearLayout.LayoutParams linearLayoutParams_time = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            linearLayoutParams_time.weight = 1;

            for(int i = 0; i<12; i++){
                TextView textView_time = new TextView(this);
                textView_time.setBackgroundResource(R.drawable.timetable_time_edge);
                textView_time.setLayoutParams(linearLayoutParams_time);
                textView_time.setGravity(Gravity.RIGHT);
                textView_time.setText(Integer.toString(9+i) + " ");
                layout[0].addView(textView_time);
            }
        } else{
            name1 = intent.getStringExtra("name1");
            name2 = intent.getStringExtra("name2");

            // name1과 name2를 비교하여 일정 추가
        }

        getscheduleForTable();
    }

    public void getscheduleForTable(){
        mPostReference.child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<timeTable> timeArraySun = new ArrayList<FriendSchedule.timeTable>();
                ArrayList<FriendSchedule.timeTable> timeArrayMon = new ArrayList<FriendSchedule.timeTable>();
                ArrayList<FriendSchedule.timeTable> timeArrayTue = new ArrayList<FriendSchedule.timeTable>();
                ArrayList<FriendSchedule.timeTable> timeArrayWed = new ArrayList<FriendSchedule.timeTable>();
                ArrayList<FriendSchedule.timeTable> timeArrayThu = new ArrayList<FriendSchedule.timeTable>();
                ArrayList<FriendSchedule.timeTable> timeArrayFri = new ArrayList<FriendSchedule.timeTable>();
                ArrayList<FriendSchedule.timeTable> timeArraySat = new ArrayList<FriendSchedule.timeTable>();

                for(int i=0;i<7;i++){
                    for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name1).child(schedule).child(date[i]).getChildren()){
                        FirebaseSchedule get = postSnapshot.getValue(FirebaseSchedule.class);
                        if(get.isOpen == 0) continue;
                        Log.d("shedule", get.schedule);
                        int startTime = get.start_time*60 + get.start_min;
                        int finTime = get.fin_time*60+get.fin_min;
                        String sche = get.schedule;
                        String info = get.schedule;
                        if(i==0) timeArraySun.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==1) timeArrayMon.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==2) timeArrayTue.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==3) timeArrayWed.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==4) timeArrayThu.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==5) timeArrayFri.add(new timeTable(startTime, finTime, sche, info));
                        else timeArraySat.add(new timeTable(startTime, finTime, sche, info));
                    }
                }

                String week[] = new String[7];
                int[] mt_dt = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                int[] mt_dt2 = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

                //date : 날짜, day : 요일

                if(today_day+1>today_date){
                    int first_day = today_day + 1 - today_date;
                    for(int i=first_day;i<7;i++){
                        String mt = Integer.toString(today_month);
                        String dt = Integer.toString(today_date);
                        String yr = Integer.toString(today_year);
                        if(mt.length()==1) mt = "0"+mt;
                        if(dt.length()==1) dt = "0"+dt;
                        week[i] = yr+mt+dt;
                    }
                    for(int i=0;i<first_day;i++){
                        String yr = Integer.toString(today_year);
                        String mt = Integer.toString(today_month-1);
                        String dt;
                        if(today_month==1){
                            yr = Integer.toString(today_year-1);
                            mt = "12";
                            dt = Integer.toString(32-first_day+i);
                        } else if(today_year%4==0 && today_year%100!=0 && today_year%400==0){
                            dt = Integer.toString(mt_dt2[today_month-2]-(first_day-i-1));
                        } else{
                            dt = Integer.toString(mt_dt[today_month-2]-(first_day-i-1));
                        }
                        week[i] = yr+mt+dt;
                    }
                } else{
                    int first_date = today_date - today_day;
                    for (int i=first_date;i<first_date+7;i++){
                        String mt = Integer.toString(today_month);
                        String dt = Integer.toString(i);
                        String yr = Integer.toString(today_year);
                        if(i>mt_dt[today_month-1]){
                            if(today_month==2 && today_year%4==0 && today_year%100!=0 && today_year%400==0){
                                if(i>mt_dt2[today_month-1]){
                                    mt = Integer.toString(today_month+1);
                                    dt = Integer.toString(i-mt_dt2[today_month-1]);
                                }
                            } else {
                                if (today_month == 12) {
                                    mt = "1";
                                    yr = Integer.toString(today_year + 1);
                                }
                                mt = Integer.toString(today_month+1);
                                dt = Integer.toString(i - mt_dt[today_month-1]);
                            }
                        }
                        if(mt.length()==1){
                            mt = "0"+mt;
                        }
                        if(dt.length()==1){
                            dt = "0"+dt;
                        }
                        week[i-first_date] = yr+mt+dt;
                    }
                }
                for(int i=0;i<7;i++){
                    for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name1).child("schedule_date").child(date[i]).child(week[i]).getChildren()){
                        FirebaseSchedule get = postSnapshot.getValue(FirebaseSchedule.class);
                        if(get.isOpen == 0) continue;
                        int startTime = get.start_time*60 + get.start_min;
                        int finTime = get.fin_time*60+get.fin_min;
                        String sche = get.schedule;
                        String info = get.info;
                        if(i==0) timeArraySun.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==1) timeArrayMon.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==2) timeArrayTue.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==3) timeArrayWed.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==4) timeArrayThu.add(new timeTable(startTime, finTime, sche, info));
                        else if(i==5) timeArrayFri.add(new timeTable(startTime, finTime, sche, info));
                        else timeArraySat.add(new timeTable(startTime, finTime, sche, info));
                    }
                }
                for(int i=0;i<7;i++){
                    FriendSchedule.AscendingObj ascending = new FriendSchedule.AscendingObj();
                    if(i==0) Collections.sort(timeArraySun, ascending);
                    else if(i==1) Collections.sort(timeArrayMon, ascending);
                    else if(i==2) Collections.sort(timeArrayTue, ascending);
                    else if(i==3) Collections.sort(timeArrayWed, ascending);
                    else if(i==4) Collections.sort(timeArrayThu, ascending);
                    else if(i==5) Collections.sort(timeArrayFri, ascending);
                    else Collections.sort(timeArraySat, ascending);
                }

                // View view = inflater.inflate(R.layout.timetable, this);

                for(int i=1;i<8;i++){
                    int dt = i-1;
                    ArrayList<FriendSchedule.timeTable> table;

                    if(i==1) table =timeArraySun;
                    else if(i==2) table =timeArrayMon;
                    else if(i==3) table =timeArrayTue;
                    else if(i==4) table =timeArrayWed;
                    else if(i==5) table =timeArrayThu;
                    else if(i==6) table =timeArrayFri;
                    else table =timeArraySat;

                    int lasttime = 9*60;

                    float sum_time = 0;

                    for(int j=0;j<table.size();j++){
                        int dur = table.get(j).startTime-lasttime;
                        float time;
                        if(dur>0){
                            time = (float)dur / 30f;
                            sum_time += time;
                            Log.d("time", Float.toString(time));
                            LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
                            scheduleParams1.weight = time;
                            ScheduleView schedule2 = new ScheduleView(FriendSchedule.this);
                            schedule2.schedule_name.setText("");
                            schedule2.schedule_info.setText("");
                            schedule2.schedule_layout.setBackgroundColor(android.R.color.white);
                            schedule2.setLayoutParams(scheduleParams1);
                            layout[i].addView(schedule2);
                        }
                        dur = table.get(j).finishTime-table.get(j).startTime;
                        time = (float)dur / 30f;
                        sum_time += time;
                        LinearLayout.LayoutParams scheduleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        scheduleParams.weight = time;   // 해당 일정의 시간 (30분 = 1 time)
                        ScheduleView schedule = new ScheduleView(FriendSchedule.this);
                        schedule.schedule_name.setText(table.get(j).name);
                        schedule.schedule_info.setText(table.get(j).info);
                        schedule.schedule_layout.setBackgroundColor(R.color.colorPrimaryDark);
                        schedule.setLayoutParams(scheduleParams);
                        layout[i].addView(schedule);

                        lasttime = table.get(j).finishTime;
                    }
                    int last = 21*60;
                    int dur = last-lasttime;
                    if(dur>0){
                        float time = (float)dur / 30f;
                        sum_time += time;
                        Log.d("time", Float.toString(time));
                        LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        scheduleParams1.weight = time;
                        ScheduleView schedule2 = new ScheduleView(FriendSchedule.this);
                        schedule2.schedule_name.setText("");
                        schedule2.schedule_info.setText("");
                        schedule2.schedule_layout.setBackgroundColor(android.R.color.white);
                        schedule2.setLayoutParams(scheduleParams1);
                        layout[i].addView(schedule2);
                    }
                    Log.d("sum of time "+i, Float.toString(sum_time));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class timeTable{
        int startTime;
        int finishTime;
        String name;
        String info;

        public timeTable(){
            startTime = 0;
            finishTime = 0;
            name = "";
        }

        public timeTable(int startTime, int finishTime, String name, String info){
            this.startTime = startTime;
            this.finishTime = finishTime;
            this.name = name;
            this.info = info;
        }
    }
    class AscendingObj implements Comparator<timeTable> {
        @Override
        public int compare(FriendSchedule.timeTable o1, FriendSchedule.timeTable o2) {
            int rst;
            if(o1.startTime == o2.startTime) rst = 0;
            else if(o1.startTime > o2.startTime) rst = 1;
            else rst = -1;
            return rst;
        }
    }
}
