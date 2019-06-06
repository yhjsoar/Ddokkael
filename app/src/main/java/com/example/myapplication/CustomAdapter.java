package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends PagerAdapter {

    String name = "lezin";
    String person_list = "Person_List";
    String schedule = "schedule_list";
    String id_list = "id_list";
    String friend = "Friend_List";
    String[] date = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    LayoutInflater inflater;
    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> schedule_list;
    ArrayList<String> friends;
    ArrayAdapter adapterCal;
    ArrayAdapter adapterFriend;

    MainActivity mainActivity;

    String dt;

    int year_, month_, date_, day_;

    public CustomAdapter(LayoutInflater inflater, String name, MainActivity mainActivity) {
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

            // 오늘의 일정 정보(Default 값)

            long now = System.currentTimeMillis();
            final Date date = new Date(now);
            year_ = date.getYear();
            month_ = date.getMonth();
            date_ = date.getDate(); // 날짜
            day_ = date.getDay(); // 요일
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

                    calendar_text.setText("   "+year+"/"+(month+1)+"/"+dayOfMonth);

                    getschedule();
                }
            });

        // Time table
        } else if (position == 1) {
            view = inflater.inflate(R.layout.timetable, null);
            LinearLayout[] layout = new LinearLayout[8];

            layout[0] = (LinearLayout)view.findViewById(R.id.layout1);
            layout[1] = (LinearLayout)view.findViewById(R.id.layout2);
            layout[2] = (LinearLayout)view.findViewById(R.id.layout3);
            layout[3] = (LinearLayout)view.findViewById(R.id.layout4);
            layout[4] = (LinearLayout)view.findViewById(R.id.layout5);
            layout[5] = (LinearLayout)view.findViewById(R.id.layout6);
            layout[6] = (LinearLayout)view.findViewById(R.id.layout7);
            layout[7] = (LinearLayout)view.findViewById(R.id.layout8);

            LinearLayout.LayoutParams linearLayoutParams_date = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            for(int i = 0; i<8; i++){
                TextView textView_date = new TextView(view.getContext());
                textView_date.setBackgroundResource(R.drawable.timetable_time_edge);
                textView_date.setLayoutParams(linearLayoutParams_date);
                String date = null;
                switch(i){
                    case 0:
                        date = " ";
                        break;
                    case 1:
                        date = "일";
                        break;
                    case 2:
                        date = "월";
                        break;
                    case 3:
                        date = "화";
                        break;
                    case 4:
                        date = "수";
                        break;
                    case 5:
                        date = "목";
                        break;
                    case 6:
                        date = "금";
                        break;
                    case 7:
                        date = "토";
                        break;
                    default:
                        break;
                }

                textView_date.setText(date);
                textView_date.setGravity(Gravity.CENTER);
                layout[i].addView(textView_date);
            }

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

            // 일정 추가
            for(int i = 1; i<8; i++){ // 일 ~ 토요일

                // 일정 집어 넣기 (해당 요일의 weight 총합이 = 24가 되어야함)
                int time = 1;
                LinearLayout.LayoutParams scheduleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                scheduleParams.weight = time;   // 해당 일정의 시간 (30분 = 1 time)
                ScheduleView schedule = new ScheduleView(view.getContext());
                schedule.schedule_name.setText("name");
                schedule.schedule_info.setText("info");
                schedule.schedule_layout.setBackgroundColor(0xff000000+0xff0000);
                schedule.setLayoutParams(scheduleParams);
                layout[i].addView(schedule);
            }

        // friends
        } else {
            view = inflater.inflate(R.layout.friends, null);

            final ListView friends_list = (ListView)view.findViewById(R.id.friends_list);
            friends = new ArrayList<String>();

            adapterFriend = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, friends);
            friends_list.setAdapter(adapterFriend);

            getFriend();

            ImageView friendPlus = (ImageView)view.findViewById(R.id.fp);
            friendPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mainActivity, AddFriend.class);
                    intent.putExtra("name", name);
                    mainActivity.startActivityForResult(intent, 2);
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
                for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name).child(schedule).child(date[day_]).getChildren()){
                    FirebaseSchedule get = postSnapshot.getValue(FirebaseSchedule.class);
                    String data = Integer.toString(get.start_time) + ":";
                    data += get.start_min<10? "0"+Integer.toString(get.start_min): Integer.toString(get.start_min);
                    data += "~"+Integer.toString(get.fin_time) + ":";
                    data += get.fin_min<10? "0"+Integer.toString(get.fin_min) : Integer.toString(get.fin_min);
                    data += " " + get.schedule;
                    schedule_list.add(data);
                }
                Log.d("dt", dt);
                for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name).child("schedule_date").child(dt).getChildren()){
                    FirebaseSchedule get = postSnapshot.getValue(FirebaseSchedule.class);
                    String data = Integer.toString(get.start_time) + ":";
                    data += get.start_min<10? "0"+Integer.toString(get.start_min): Integer.toString(get.start_min);
                    data += "~"+Integer.toString(get.fin_time) + ":";
                    data += get.fin_min<10? "0"+Integer.toString(get.fin_min) : Integer.toString(get.fin_min);
                    data += " " + get.schedule;
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
                    FirebaseFriend get = post.getValue(FirebaseFriend.class);
                    friends.add(get.name);
                }
                adapterFriend.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
