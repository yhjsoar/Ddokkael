package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

public class ActivityFriendSchedule extends Activity {
    int[][] result = new int[7][1441];

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
            getscheduleForTable();
        } else{
            name1 = intent.getStringExtra("name1");
            name2 = intent.getStringExtra("name2");

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
            compareForTable();
            // name1과 name2를 비교하여 일정 추가
        }
    }

    public void getscheduleForTable(){
        mPostReference.child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<timeTable> timeArraySun = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayMon = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayTue = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayWed = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayThu = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayFri = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArraySat = new ArrayList<ActivityFriendSchedule.timeTable>();

                for(int i=0;i<7;i++){
                    for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name1).child(schedule).child(date[i]).getChildren()){
                        DataFirebaseSchedule get = postSnapshot.getValue(DataFirebaseSchedule.class);
                        if(get.isOpen == 0) continue;
                        Log.d("shedule", get.schedule);
                        int startTime = get.start_time*60 + get.start_min;
                        int finTime = get.fin_time*60+get.fin_min;
                        String sche = get.schedule;
                        String info = get.info;
                        if(i==0) timeArraySun.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==1) timeArrayMon.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==2) timeArrayTue.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==3) timeArrayWed.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==4) timeArrayThu.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==5) timeArrayFri.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else timeArraySat.add(new timeTable(startTime, finTime, sche, info, get.color));
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
                        DataFirebaseSchedule get = postSnapshot.getValue(DataFirebaseSchedule.class);
                        if(get.isOpen == 0) continue;
                        int startTime = get.start_time*60 + get.start_min;
                        int finTime = get.fin_time*60+get.fin_min;
                        String sche = get.schedule;
                        String info = get.info;
                        if(i==0) timeArraySun.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==1) timeArrayMon.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==2) timeArrayTue.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==3) timeArrayWed.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==4) timeArrayThu.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else if(i==5) timeArrayFri.add(new timeTable(startTime, finTime, sche, info, get.color));
                        else timeArraySat.add(new timeTable(startTime, finTime, sche, info, get.color));
                    }
                }
                for(int i=0;i<7;i++){
                    ActivityFriendSchedule.AscendingObj ascending = new ActivityFriendSchedule.AscendingObj();
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
                    ArrayList<ActivityFriendSchedule.timeTable> table;

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
                        if(table.get(j).finishTime<=9*60)   continue;
                        if(table.get(j).startTime>=21*60)   continue;
                        if(table.get(j).finishTime>9*60 && table.get(j).startTime<9*60) table.get(j).startTime = 9*60;
                        else if(table.get(j).finishTime>21*60 && table.get(j).startTime<21*60) table.get(j).finishTime = 21*60;
                        int dur = table.get(j).startTime-lasttime;
                        float time;
                        if(dur>0){
                            int first = lasttime;
                            int last = table.get(j).startTime;
                            int startLine = first/60+1;
                            int finishLine = (last-1)/60;
                            if(startLine > finishLine){
                                time = (float)dur / 30f;
                                sum_time += time;
                                Log.d("time", Float.toString(time));
                                LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                                scheduleParams1.weight = time;
                                ViewSchedule schedule2 = new ViewSchedule(ActivityFriendSchedule.this);
                                schedule2.schedule_name.setText("");
                                schedule2.schedule_info.setText("");
                                schedule2.schedule_layout.setBackgroundColor(0xFFFFFF);
                                schedule2.setLayoutParams(scheduleParams1);
                                layout[i].addView(schedule2);
                            }
                            else {
                                float[] weightArray = new float[2 * finishLine - 2 * startLine + 3];
                                float weightTmp = 60f * (float) startLine - (float) first;
                                weightTmp = weightTmp / 30f;
                                weightArray[0] = weightTmp;
                                weightTmp = (float) last - (float) finishLine * 60f;
                                weightTmp = weightTmp / 30f;
                                weightArray[2 * finishLine - 2 * startLine + 2] = weightTmp - 0.02f;
                                for (int tmp = 1; tmp < 2 * finishLine - 2 * startLine + 2; tmp++) {
                                    if (tmp % 2 == 1) weightArray[tmp] = 0.02f;
                                    else weightArray[tmp] = 1.98f;
                                }
                                for(int tmp = 0;tmp<weightArray.length;tmp++){
                                    Log.d("머지", Float.toString(weightArray[tmp]));
                                    sum_time += weightArray[tmp];
                                    LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                                    scheduleParams1.weight = weightArray[tmp];
                                    ViewSchedule schedule2 = new ViewSchedule(ActivityFriendSchedule.this);
                                    schedule2.schedule_name.setText("");
                                    schedule2.schedule_info.setText("");
                                    if(tmp%2==1) schedule2.schedule_layout.setBackgroundColor(0xFF888888);
                                    else schedule2.schedule_layout.setBackgroundColor(0xFFFFFF);
                                    schedule2.setLayoutParams(scheduleParams1);
                                    layout[i].addView(schedule2);
                                }
                            }
                        }
                        dur = table.get(j).finishTime-table.get(j).startTime;
                        time = (float)dur / 30f;
                        sum_time += time;
                        layout[i].addView(vs(table.get(j).name, table.get(j).info, time, table.get(j).color));

                        lasttime = table.get(j).finishTime;
                    }
                    int last = 21*60;
                    int dur = last-lasttime;
                    if(dur>0){
                        int first = lasttime;

                        int startLine = first/60+1;
                        int finishLine = (last-1)/60;
                        if(startLine > finishLine){
                            float time = (float)dur / 30f;
                            sum_time += time;
                            Log.d("time", Float.toString(time));
                            LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                            scheduleParams1.weight = time;
                            ViewSchedule schedule2 = new ViewSchedule(ActivityFriendSchedule.this);
                            schedule2.schedule_name.setText("");
                            schedule2.schedule_info.setText("");
                            schedule2.schedule_layout.setBackgroundColor(0xFFFFFF);
                            schedule2.setLayoutParams(scheduleParams1);
                            layout[i].addView(schedule2);
                        } else{
                            float[] weightArray = new float[2 * finishLine - 2 * startLine + 3];
                            float weightTmp = 60f * (float) startLine - (float) first;
                            weightTmp = weightTmp / 30f;
                            weightArray[0] = weightTmp;
                            weightTmp = (float) last - (float) finishLine * 60f;
                            weightTmp = weightTmp / 30f;
                            weightArray[2 * finishLine - 2 * startLine + 2] = weightTmp - 0.02f;
                            for (int tmp = 1; tmp < 2 * finishLine - 2 * startLine + 2; tmp++) {
                                if (tmp % 2 == 1) weightArray[tmp] = 0.02f;
                                else weightArray[tmp] = 1.98f;
                            }
                            for(int tmp = 0;tmp<weightArray.length;tmp++){
                                sum_time += weightArray[tmp];
                                sum_time += weightArray[tmp];
                                LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                                scheduleParams1.weight = weightArray[tmp];
                                ViewSchedule schedule2 = new ViewSchedule(ActivityFriendSchedule.this);
                                schedule2.schedule_name.setText("");
                                schedule2.schedule_info.setText("");
                                if(tmp%2==1) schedule2.schedule_layout.setBackgroundColor(0xFF888888);
                                else schedule2.schedule_layout.setBackgroundColor(0xFFFFFF);
                                schedule2.setLayoutParams(scheduleParams1);
                                layout[i].addView(schedule2);
                            }
                        }
                    }
                    Log.d("sum of time "+i, Float.toString(sum_time));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void compareForTable(){
        mPostReference.child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=0;i<7;i++){
                    for(int j=0;j<=1440;j++){
                        result[i][j] = 0;
                    }
                }
                for(int i=0;i<7;i++){
                    for(DataSnapshot post : dataSnapshot.child(person_list).child(name1).child(schedule).child(date[i]).getChildren()){
                        DataFirebaseSchedule get = post.getValue(DataFirebaseSchedule.class);
                        int st = get.start_time*60+get.start_min;
                        int ft = get.fin_time*60+get.fin_min;
                        for(int j=st;j<=ft;j++){
                            result[i][j] = 1;
                        }
                    }
                }
                for(int i=0;i<7;i++){
                    for(DataSnapshot post : dataSnapshot.child(getString(R.string.person)).child(name2).child(getString(R.string.schedule)).child(date[i]).getChildren()){
                        DataFirebaseSchedule get = post.getValue(DataFirebaseSchedule.class);
                        if(get.isOpen==0) continue;
                        int st = get.start_time*60+get.start_min;
                        int ft = get.fin_time*60+get.fin_min;
                        for(int j=st;j<=ft;j++){
                            result[i][j] = 1;
                        }
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
                    for(DataSnapshot post : dataSnapshot.child(person_list).child(name1).child("schedule_date").child(date[i]).child(week[i]).getChildren()){
                        DataFirebaseSchedule get = post.getValue(DataFirebaseSchedule.class);
                        int st = get.start_time*60+get.start_min;
                        int ft = get.fin_time*60+get.fin_min;
                        for(int j=st;j<=ft;j++){
                            result[i][j] = 1;
                        }
                    }
                }
                for(int i=0;i<7;i++){
                    for(DataSnapshot post : dataSnapshot.child(person_list).child(name2).child("schedule_date").child(date[i]).child(week[i]).getChildren()){
                        DataFirebaseSchedule get = post.getValue(DataFirebaseSchedule.class);
                        if(get.isOpen == 0) continue;
                        int st = get.start_time*60+get.start_min;
                        int ft = get.fin_time*60+get.fin_min;
                        for(int j=st;j<=ft;j++){
                            result[i][j] = 1;
                        }
                    }
                }
                // View view = inflater.inflate(R.layout.timetable, this);
                ArrayList<timeTable> timeArraySun = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayMon = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayTue = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayWed = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayThu = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArrayFri = new ArrayList<ActivityFriendSchedule.timeTable>();
                ArrayList<ActivityFriendSchedule.timeTable> timeArraySat = new ArrayList<ActivityFriendSchedule.timeTable>();

                int counting = 0;
                int st = 0;
                int ft = 0;
                String share = "CANNOT";
                for(int i=0;i<7;i++){
                    for(int j=0;j<=1440;j++){
                        if(counting ==0 && result[i][j]==1){
                            if(j!=0) st = j;
                            else st = j;
                            counting = 1;
                        } else if(counting == 1 && result[i][j]==1){
                            continue;
                        } else if(counting == 1 && result[i][j]==0){
                            ft = j-1;
                            int sTime = st/60;
                            int sMin = st%60;
                            int fTime = ft/60;
                            int fMin = ft%60;
                            String info = Integer.toString(sTime) + ":" + (sMin < 10 ? "0"+Integer.toString(sMin) : Integer.toString(sMin)) + "~" + Integer.toString(fTime) + ":" + (fMin < 10 ? "0"+Integer.toString(fMin) : Integer.toString(fMin));
                            if(i==0) timeArraySun.add(new timeTable(st, ft, share, info, 0));
                            else if(i==1) timeArrayMon.add(new timeTable(st, ft, share, info, 0));
                            else if(i==2) timeArrayTue.add(new timeTable(st, ft, share, info, 0));
                            else if(i==3) timeArrayWed.add(new timeTable(st, ft, share, info, 0));
                            else if(i==4) timeArrayThu.add(new timeTable(st, ft, share, info, 0));
                            else if(i==5) timeArrayFri.add(new timeTable(st, ft, share, info, 0));
                            else if(i==6) timeArraySat.add(new timeTable(st, ft, share, info, 0));
                            counting = 0;
                        } else if(counting == 0 && result[i][j]==0){
                            continue;
                        }
                    }
                    if(counting == 1){
                        ft = 1440;
                        counting = 0;
                        int sTime = st/60;
                        int sMin = st%60;
                        int fTime = ft/60;
                        int fMin = ft%60;
                        String info = Integer.toString(sTime) + ":" + (sMin < 10 ? "0"+Integer.toString(sMin) : Integer.toString(sMin)) + "~" + Integer.toString(fTime) + ":" + (fMin < 10 ? "0"+Integer.toString(fMin) : Integer.toString(fMin));
                        if(i==0) timeArraySun.add(new timeTable(st, ft, share, info, 0));
                        else if(i==1) timeArrayMon.add(new timeTable(st, ft, share, info, 0));
                        else if(i==2) timeArrayTue.add(new timeTable(st, ft, share, info, 0));
                        else if(i==3) timeArrayWed.add(new timeTable(st, ft, share, info, 0));
                        else if(i==4) timeArrayThu.add(new timeTable(st, ft, share, info, 0));
                        else if(i==5) timeArrayFri.add(new timeTable(st, ft, share, info, 0));
                        else if(i==6) timeArraySat.add(new timeTable(st, ft, share, info, 0));
                    }
                }

                for(int i=1;i<8;i++){
                    int dt = i-1;
                    ArrayList<ActivityFriendSchedule.timeTable> table;

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
                        if(table.get(j).finishTime<=9*60)   continue;
                        if(table.get(j).startTime>=21*60)   continue;
                        if(table.get(j).finishTime>9*60 && table.get(j).startTime<9*60) table.get(j).startTime = 9*60;
                        else if(table.get(j).finishTime>21*60 && table.get(j).startTime<21*60) table.get(j).finishTime = 21*60;
                        int dur = table.get(j).startTime-lasttime;
                        float time;
                        if(dur>0){
                            int first = lasttime;
                            int last = table.get(j).startTime;
                            int startLine = first/60+1;
                            int finishLine = (last-1)/60;
                            if(startLine > finishLine){
                                time = (float)dur / 30f;
                                sum_time += time;
                                Log.d("time", Float.toString(time));
                                LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                                scheduleParams1.weight = time;
                                ViewSchedule schedule2 = new ViewSchedule(ActivityFriendSchedule.this);
                                schedule2.schedule_name.setText("");
                                schedule2.schedule_info.setText("");
                                schedule2.schedule_layout.setBackgroundColor(0xFFFFFF);
                                schedule2.setLayoutParams(scheduleParams1);
                                layout[i].addView(schedule2);
                            }
                            else {
                                float[] weightArray = new float[2 * finishLine - 2 * startLine + 3];
                                float weightTmp = 60f * (float) startLine - (float) first;
                                weightTmp = weightTmp / 30f;
                                weightArray[0] = weightTmp;
                                weightTmp = (float) last - (float) finishLine * 60f;
                                weightTmp = weightTmp / 30f;
                                weightArray[2 * finishLine - 2 * startLine + 2] = weightTmp - 0.02f;
                                for (int tmp = 1; tmp < 2 * finishLine - 2 * startLine + 2; tmp++) {
                                    if (tmp % 2 == 1) weightArray[tmp] = 0.02f;
                                    else weightArray[tmp] = 1.98f;
                                }
                                for(int tmp = 0;tmp<weightArray.length;tmp++){
                                    Log.d("머지", Float.toString(weightArray[tmp]));
                                    sum_time += weightArray[tmp];
                                    LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                                    scheduleParams1.weight = weightArray[tmp];
                                    ViewSchedule schedule2 = new ViewSchedule(ActivityFriendSchedule.this);
                                    schedule2.schedule_name.setText("");
                                    schedule2.schedule_info.setText("");
                                    if(tmp%2==1) schedule2.schedule_layout.setBackgroundColor(0xFF888888);
                                    else schedule2.schedule_layout.setBackgroundColor(0xFFFFFF);
                                    schedule2.setLayoutParams(scheduleParams1);
                                    layout[i].addView(schedule2);
                                }
                            }
                        }
                        dur = table.get(j).finishTime-table.get(j).startTime;
                        time = (float)dur / 30f;
                        sum_time += time;
                        LinearLayout.LayoutParams scheduleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        scheduleParams.weight = time;   // 해당 일정의 시간 (30분 = 1 time)
                        ViewSchedule schedule = new ViewSchedule(ActivityFriendSchedule.this);
                        schedule.schedule_name.setText(table.get(j).name);
                        schedule.schedule_info.setText(table.get(j).info);
                        schedule.schedule_layout.setBackgroundColor(0xFF616D78);
                        schedule.setLayoutParams(scheduleParams);
                        layout[i].addView(schedule);

                        lasttime = table.get(j).finishTime;
                    }
                    int last = 21*60;
                    int dur = last-lasttime;
                    if(dur>0){
                        int first = lasttime;

                        int startLine = first/60+1;
                        int finishLine = (last-1)/60;
                        if(startLine > finishLine){
                            float time = (float)dur / 30f;
                            sum_time += time;
                            Log.d("time", Float.toString(time));
                            LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                            scheduleParams1.weight = time;
                            ViewSchedule schedule2 = new ViewSchedule(ActivityFriendSchedule.this);
                            schedule2.schedule_name.setText("");
                            schedule2.schedule_info.setText("");
                            schedule2.schedule_layout.setBackgroundColor(0xFFFFFF);
                            schedule2.setLayoutParams(scheduleParams1);
                            layout[i].addView(schedule2);
                        } else{
                            float[] weightArray = new float[2 * finishLine - 2 * startLine + 3];
                            float weightTmp = 60f * (float) startLine - (float) first;
                            weightTmp = weightTmp / 30f;
                            weightArray[0] = weightTmp;
                            weightTmp = (float) last - (float) finishLine * 60f;
                            weightTmp = weightTmp / 30f;
                            weightArray[2 * finishLine - 2 * startLine + 2] = weightTmp - 0.02f;
                            for (int tmp = 1; tmp < 2 * finishLine - 2 * startLine + 2; tmp++) {
                                if (tmp % 2 == 1) weightArray[tmp] = 0.02f;
                                else weightArray[tmp] = 1.98f;
                            }
                            for(int tmp = 0;tmp<weightArray.length;tmp++){
                                sum_time += weightArray[tmp];
                                Log.d("머지", Float.toString(weightArray[tmp]));
                                sum_time += weightArray[tmp];
                                LinearLayout.LayoutParams scheduleParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                                scheduleParams1.weight = weightArray[tmp];
                                ViewSchedule schedule2 = new ViewSchedule(ActivityFriendSchedule.this);
                                schedule2.schedule_name.setText("");
                                schedule2.schedule_info.setText("");
                                if(tmp%2==1) schedule2.schedule_layout.setBackgroundColor(0xFF888888);
                                else schedule2.schedule_layout.setBackgroundColor(0xFFFFFF);
                                schedule2.setLayoutParams(scheduleParams1);
                                layout[i].addView(schedule2);
                            }
                        }
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
        int color;

        public timeTable(){
            startTime = 0;
            finishTime = 0;
            name = "";
            info = "";
            color = 0;
        }

        public timeTable(int startTime, int finishTime, String name, String info, int color){
            this.startTime = startTime;
            this.finishTime = finishTime;
            this.name = name;
            this.info = info;
            this.color = color;
        }
    }
    class AscendingObj implements Comparator<timeTable> {
        @Override
        public int compare(ActivityFriendSchedule.timeTable o1, ActivityFriendSchedule.timeTable o2) {
            int rst;
            if(o1.startTime == o2.startTime) rst = 0;
            else if(o1.startTime > o2.startTime) rst = 1;
            else rst = -1;
            return rst;
        }
    }

    public ViewSchedule vs(String name, String info, Float weight, int color){
        LinearLayout.LayoutParams scheduleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        scheduleParams.weight = weight;
        ViewSchedule schedule = new ViewSchedule(ActivityFriendSchedule.this);
        schedule.schedule_name.setText(name);
        schedule.schedule_info.setText(info);

        switch(color){
            case 0:
                schedule.schedule_layout.setBackgroundColor(0xFF3FB8AF);
                break;
            case 1:
                schedule.schedule_layout.setBackgroundColor(0xFF7FC7AF);
                break;
            case 2:
                schedule.schedule_layout.setBackgroundColor(0xFFDAD8A7);
                break;
            case 3:
                schedule.schedule_layout.setBackgroundColor(0xFFFF9E9D);
                break;
            case 4:
                schedule.schedule_layout.setBackgroundColor(0xFFC78989);
                break;
            case 5:
                schedule.schedule_layout.setBackgroundColor(0xFFD6B8F3);
                break;
            default:
                schedule.schedule_layout.setBackgroundColor(0xFF445878);
                break;
        }
        schedule.setLayoutParams(scheduleParams);

        return schedule;
    }
}
