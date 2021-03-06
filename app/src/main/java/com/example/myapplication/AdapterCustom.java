package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

public class AdapterCustom extends PagerAdapter {

    String name = "lezin";
    String person_list = "Person_List";
    String schedule = "schedule_list";
    String request = "request_list";
    String id_list = "id_list";
    String friend = "Friend_List";
    String[] date = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    float view_height;

    LayoutInflater inflater;
    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> schedule_list;
    ArrayList<String> friends;
    ArrayAdapter adapterCal;
    ArrayAdapter adapterFriend;
    ArrayList<String> toRequest;
    ArrayList<String> fromRequest;
    ArrayAdapter adapterTo;
    ArrayAdapter adapterFrom;

    MainActivity mainActivity;

    TextView fromText, toText, listText;

    AppCompatImageView weatherImage;

    LinearLayout linearLayout;

    String dt;

    int year_, month_, date_, day_;
    int today_year, today_month, today_date, today_day;

    DataWeatherItem item;

    LinearLayout[] layout = new LinearLayout[8];


    public AdapterCustom(LayoutInflater inflater, String name, MainActivity mainActivity) {
        this.inflater = inflater;
        this.name = name;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;

        // calendar
        if (position == 0) {
            view = inflater.inflate(R.layout.calendar, null);
            CalendarView calendar = (CalendarView)view.findViewById(R.id.calendarView);
            final TextView calendar_text = (TextView)view.findViewById(R.id.calendar_text);
            final ListView calendar_list = (ListView)view.findViewById(R.id.calendar_list);
            schedule_list = new ArrayList<String>();
            linearLayout = (LinearLayout)view.findViewById(R.id.calender_back);
            weatherImage = (AppCompatImageView) view.findViewById(R.id.weather);

            // 오늘의 일정 정보(Default 값)

            long now = System.currentTimeMillis();
            final Date date = new Date(now);
            year_ = date.getYear();
            month_ = date.getMonth();
            date_ = date.getDate(); // 날짜
            day_ = date.getDay(); // 요일

            item = mainActivity.getWeather();
            if(item!=null){
                Log.d("weather", item.weather.get(0).main);
                weatherImage.setVisibility(View.VISIBLE);
                if(item.weather.get(0).main.equals("Thunderstorm")){
                    linearLayout.setBackgroundResource(R.drawable.thunderstorm);
                    weatherImage.setImageResource(R.drawable.ic_thunderstorm);
                } else if (item.weather.get(0).main.equals("Drizzle")) {
                    linearLayout.setBackgroundResource(R.drawable.drizzle);
                    weatherImage.setImageResource(R.drawable.ic_drizzle);
                } else if (item.weather.get(0).main.equals("Rain")) {
                    linearLayout.setBackgroundResource(R.drawable.rain);
                    weatherImage.setImageResource(R.drawable.ic_rain);
                } else if (item.weather.get(0).main.equals("Snow")) {
                    linearLayout.setBackgroundResource(R.drawable.snow);
                    weatherImage.setImageResource(R.drawable.ic_snow);
                } else if (item.weather.get(0).main.equals("Mist") || item.weather.get(0).main.equals("Smoke") || item.weather.get(0).main.equals("Haze") || item.weather.get(0).main.equals("Fog")) {
                    linearLayout.setBackgroundResource(R.drawable.haze);
                    weatherImage.setImageResource(R.drawable.ic_fog);
                } else if (item.weather.get(0).main.equals("Dust")) {
                    linearLayout.setBackgroundResource(R.drawable.dust);
                    weatherImage.setImageResource(R.drawable.ic_dust);
                } else if (item.weather.get(0).main.equals("Sand")) {
                    weatherImage.setImageResource(R.drawable.ic_sand);
                    linearLayout.setBackgroundResource(R.drawable.sand);
                } else if (item.weather.get(0).main.equals("Ash")) {
                    linearLayout.setBackgroundResource(R.drawable.ash);
                    weatherImage.setImageResource(R.drawable.ic_ash);
                } else if (item.weather.get(0).main.equals("Squall")) {
                    linearLayout.setBackgroundResource(R.drawable.squall);
                    weatherImage.setImageResource(R.drawable.ic_squall);
                } else if (item.weather.get(0).main.equals("Tornado")) {
                    linearLayout.setBackgroundResource(R.drawable.tornado);
                    weatherImage.setImageResource(R.drawable.ic_tornado);
                } else if (item.weather.get(0).main.equals("Clouds")) {
                    linearLayout.setBackgroundResource(R.drawable.clouds);
                    weatherImage.setImageResource(R.drawable.ic_clouds);
                } else{
                    linearLayout.setBackgroundResource(R.drawable.clear);
                    weatherImage.setImageResource(R.drawable.ic_clear);
                }
            } else{
                weatherImage.setVisibility(View.GONE);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());
            Date dat = new Date();
            dt = dateFormat.format(dat);

            adapterCal = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, schedule_list);
            calendar_list.setAdapter(adapterCal);
            String today = new SimpleDateFormat("yyyy/M/dd").format(new Date());
            today = "   "+today;
            calendar_text.setText(today);

            getschedule();

            // 날자 선택
            calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    year_ = year;
                    month_ = month+1;
                    date_ = dayOfMonth; // 날짜
                    day_ = getDay(year, month, dayOfMonth);
                    dt = getStringdate();


                    if(year_==today_year && month_ == today_month && date_==today_date){
                        weatherImage.setVisibility(View.VISIBLE);
                    } else {
                        weatherImage.setVisibility(View.GONE);
                    }
                    calendar_text.setText("   "+year+"/"+(month+1)+"/"+dayOfMonth);

                    getschedule();
                }
            });

            calendar_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), PopUpDelete.class);
                    intent.putExtra("data", (String)parent.getAdapter().getItem(position));
                    intent.putExtra("date", calendar_text.getText().toString());
                    intent.putExtra("name", name);
                    mainActivity.startActivity(intent);
                    return false;
                }
            });

        // Time table
        } else if (position == 1) {
            view = inflater.inflate(R.layout.timetable, null);

            View layoutview = (View)view.findViewById(R.id.timetable_layout);
            view_height = layoutview.getHeight();

            layout[0] = (LinearLayout)view.findViewById(R.id.layout1);
            layout[1] = (LinearLayout)view.findViewById(R.id.layout2);
            layout[2] = (LinearLayout)view.findViewById(R.id.layout3);
            layout[3] = (LinearLayout)view.findViewById(R.id.layout4);
            layout[4] = (LinearLayout)view.findViewById(R.id.layout5);
            layout[5] = (LinearLayout)view.findViewById(R.id.layout6);
            layout[6] = (LinearLayout)view.findViewById(R.id.layout7);
            layout[7] = (LinearLayout)view.findViewById(R.id.layout8);

            LinearLayout.LayoutParams linearLayoutParams_time = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayoutParams_time.weight = 1;

            for(int i = 0; i<12; i++){
                TextView textView_time = new TextView(view.getContext());
                textView_time.setBackgroundResource(R.drawable.timetable_time_edge);
                textView_time.setLayoutParams(linearLayoutParams_time);
                textView_time.setGravity(Gravity.RIGHT);
                textView_time.setText(Integer.toString(9+i) + " ");
                layout[0].addView(textView_time);
            }
            getscheduleForTable();
        // friends
        } else {
            view = inflater.inflate(R.layout.friends, null);

            listText = (TextView)view.findViewById(R.id.listText);
            fromText = (TextView)view.findViewById(R.id.fromText);
            toText = (TextView)view.findViewById(R.id.toText);

            final ListView friends_list = (ListView)view.findViewById(R.id.friends_list);
            final ListView friend_from = (ListView)view.findViewById(R.id.friends_request_list);
            final ListView friend_to = (ListView)view.findViewById(R.id.friends_request_list2);
            friends = new ArrayList<String>();
            toRequest = new ArrayList<String>();
            fromRequest = new ArrayList<String>();

            adapterFriend = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, friends);
            adapterTo = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, toRequest);
            adapterFrom = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, fromRequest);

            friends_list.setAdapter(adapterFriend);
            friend_from.setAdapter(adapterFrom);
            friend_to.setAdapter(adapterTo);

            getFriend();

            ImageView friendPlus = (ImageView)view.findViewById(R.id.fp);
            friendPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mainActivity, ActivityAddFriend.class);
                    intent.putExtra("name", name);
                    mainActivity.startActivityForResult(intent, 2);
                }
            });

            friend_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String friend =(String)parent.getAdapter().getItem(position);
                    Intent intent = new Intent(mainActivity, PopUpGetRequest.class);
                    intent.putExtra("name", name);
                    intent.putExtra("friend", friend);
                    mainActivity.startActivityForResult(intent, 3);
                }
            });

            friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), PopUpFriend.class);
                    intent.putExtra("name", (String)parent.getAdapter().getItem(position));
                    intent.putExtra("myname", name);
                    mainActivity.startActivity(intent);
                }
            });

            friends_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), PopUpDeleteFriend.class);
                    intent.putExtra("name", (String)parent.getAdapter().getItem(position));
                    intent.putExtra("myname", name);
                    mainActivity.startActivity(intent);
                    return true;
                }
            });

            friend_to.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), PopUpCancelFriendRequest.class);
                    intent.putExtra("name", (String)parent.getAdapter().getItem(position));
                    intent.putExtra("myname", name);
                    mainActivity.startActivity(intent);
                    return true;
                }
            });
        }

        container.addView(view);

        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    public void getschedule(){
        mPostReference.child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                schedule_list.clear();
                if(!dataSnapshot.child(person_list).child(name).exists()){
                    return;
                }
                AscendingObj ascending = new AscendingObj();
                ArrayList<timeTable> calTable = new ArrayList<timeTable>();
                for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name).child(schedule).child(date[day_]).getChildren()){
                    DataFirebaseSchedule get = postSnapshot.getValue(DataFirebaseSchedule.class);
                    calTable.add(new timeTable(get.start_time, get.start_min, get.fin_time, get.fin_min, get.schedule, get.info, get.color));
                }
                Log.d("dat_", date[day_]+dt);

                for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name).child("schedule_date").child(date[day_]).child(dt).getChildren()){
                    DataFirebaseSchedule get = postSnapshot.getValue(DataFirebaseSchedule.class);
                    calTable.add(new timeTable(get.start_time, get.start_min, get.fin_time, get.fin_min, get.schedule, get.info, get.color));
                }
                Collections.sort(calTable, ascending);
                for(timeTable get : calTable){
                    String data = Integer.toString(get.sTime) + ":";
                    data += get.sMin<10? "0"+Integer.toString(get.sMin): Integer.toString(get.sMin);
                    data += "~"+Integer.toString(get.fTime) + ":";
                    data += get.fMin<10? "0"+Integer.toString(get.fMin) : Integer.toString(get.fMin);
                    data += " " + get.name;
                    schedule_list.add(data);
                }
                adapterCal.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    int getDay(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        return day_of_week-1;
    }

    String getStringdate(){
        String yr = Integer.toString(year_);
        String mt = Integer.toString(month_);
        String dy = Integer.toString(date_);
        if(month_<10) mt = "0"+mt;
        if(date_<10) dy = "0"+dy;
        return yr+mt+dy;
    }

    public void getFriend(){
        mPostReference.child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friends.clear();
                for(DataSnapshot post : dataSnapshot.child(person_list).child(name).child(friend).getChildren()){
                    DataFirebaseFriend get = post.getValue(DataFirebaseFriend.class);
                    friends.add(get.name);
                }
                if(friends.size()==0){
                    listText.setVisibility(View.VISIBLE);
                } else listText.setVisibility(View.GONE);
                adapterFriend.notifyDataSetChanged();

                fromRequest.clear();
                for(DataSnapshot post : dataSnapshot.child(request).child(name).child("from").getChildren()){
                    DataFirebaseFriend get = post.getValue(DataFirebaseFriend.class);
                    fromRequest.add(get.name);
                }
                if(fromRequest.size()==0){
                    fromText.setVisibility(View.VISIBLE);
                } else{
                    fromText.setVisibility(View.GONE);
                }
                adapterFrom.notifyDataSetChanged();

                toRequest.clear();
                for(DataSnapshot post : dataSnapshot.child(request).child(name).child("to").getChildren()){
                    DataFirebaseFriend get = post.getValue(DataFirebaseFriend.class);
                    toRequest.add(get.name);
                }
                if(toRequest.size()==0){
                    toText.setVisibility(View.VISIBLE);
                } else{
                    toText.setVisibility(View.GONE);
                }
                adapterTo.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getscheduleForTable(){
        mPostReference.child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=1;i<8;i++)     layout[i].removeAllViews();
                ArrayList<timeTable> timeArraySun = new ArrayList<timeTable>();
                ArrayList<timeTable> timeArrayMon = new ArrayList<timeTable>();
                ArrayList<timeTable> timeArrayTue = new ArrayList<timeTable>();
                ArrayList<timeTable> timeArrayWed = new ArrayList<timeTable>();
                ArrayList<timeTable> timeArrayThu = new ArrayList<timeTable>();
                ArrayList<timeTable> timeArrayFri = new ArrayList<timeTable>();
                ArrayList<timeTable> timeArraySat = new ArrayList<timeTable>();

                for(int i=0;i<7;i++){
                    for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name).child(schedule).child(date[i]).getChildren()){
                        DataFirebaseSchedule get = postSnapshot.getValue(DataFirebaseSchedule.class);
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
                long now = System.currentTimeMillis();
                Date dd  = new Date(now);
                today_year = dd.getYear()+1900;
                today_month = dd.getMonth()+1;
                today_date = dd.getDate();
                today_day =dd.getDay();

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
                    for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name).child("schedule_date").child(date[i]).child(week[i]).getChildren()){
                        DataFirebaseSchedule get = postSnapshot.getValue(DataFirebaseSchedule.class);
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
                    AscendingObj ascending = new AscendingObj();
                    if(i==0) Collections.sort(timeArraySun, ascending);
                    else if(i==1) Collections.sort(timeArrayMon, ascending);
                    else if(i==2) Collections.sort(timeArrayTue, ascending);
                    else if(i==3) Collections.sort(timeArrayWed, ascending);
                    else if(i==4) Collections.sort(timeArrayThu, ascending);
                    else if(i==5) Collections.sort(timeArrayFri, ascending);
                    else Collections.sort(timeArraySat, ascending);
                }
                View view = inflater.inflate(R.layout.timetable, null);
                for(int i=1;i<8;i++){
                    int dt = i-1;
                    ArrayList<timeTable> table;

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
                                ViewSchedule schedule2 = new ViewSchedule(view.getContext());
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
                                    ViewSchedule schedule2 = new ViewSchedule(view.getContext());
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
                        layout[i].addView(vs(view, table.get(j).name, table.get(j).info, time, table.get(j).color));

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
                            ViewSchedule schedule2 = new ViewSchedule(view.getContext());
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
                                ViewSchedule schedule2 = new ViewSchedule(view.getContext());
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
        int sTime, sMin;
        int fTime, fMin;
        int color;

        public timeTable(){
            startTime = 0;
            finishTime = 0;
            name = "";
            sTime = 0;
            sMin = 0;
            fTime = 0;
            fMin = 0;
            color = 0;
        }

        public timeTable(int sTime, int sMin, int fTime, int fMin, String name, String info, int color){
            this.sTime = sTime;
            this.sMin = sMin;
            this.fTime = fTime;
            this.fMin = fMin;
            this.name = name;
            this.info = info;
            this.color = color;
            startTime = sTime*60+sMin;
            finishTime = fTime*60+fMin;
        }

        public timeTable(int startTime, int finishTime, String name, String info, int color){
            this.startTime = startTime;
            this.finishTime = finishTime;
            this.name = name;
            this.info = info;
            this.color = color;
            sTime = 0;
            sMin = 0;
            fTime = 0;
            fMin = 0;
        }
    }
    class AscendingObj implements Comparator<timeTable> {
        @Override
        public int compare(timeTable o1, timeTable o2) {
            int rst;
            if(o1.startTime == o2.startTime) rst = 0;
            else if(o1.startTime > o2.startTime) rst = 1;
            else rst = -1;
            return rst;
        }

    }

    public ViewSchedule vs(View view, String name, String info, Float weight, int color){
        LinearLayout.LayoutParams scheduleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        scheduleParams.weight = weight;
        ViewSchedule schedule = new ViewSchedule(view.getContext());
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
