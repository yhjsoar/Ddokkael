package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapter extends PagerAdapter {

    String name = "lezin";
    String friend_name = "lezin2";

    LayoutInflater inflater;
    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
    String person_list = "Person_List";
    String schedule = "schedule_list";
    String id_list = "id_list";
    String friend = "Friend_List";
    String[] date = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};


    public CustomAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
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

            // Default setting
            String today = new SimpleDateFormat("yyyy/M/dd").format(new Date());
            calendar_text.setText(today);

            // Calendar
            calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    calendar_text.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                    // 해당 요일의 data를 읽어와서 listView에 넣기
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

        // friends
        } else {
            view = inflater.inflate(R.layout.friends, null);
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

    public void getInfo(){
        mPostReference.child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<FirebaseSchedule> data = new ArrayList<FirebaseSchedule>(); // schedule list
                data.clear();
                for(int i=0;i<7;i++) {
                    for (DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name).child(schedule).child(date[i]).getChildren()) {
                        FirebaseSchedule get = postSnapshot.getValue(FirebaseSchedule.class);
                        data.add(get);
                    }
                }
                boolean isExist = false; // 친구 id 존재 여부
                for(DataSnapshot postSnapshot : dataSnapshot.child(id_list).getChildren()){
                    FirebaseID get = postSnapshot.getValue(FirebaseID.class);
                    if(get.id.equals(friend_name)){
                        isExist = true;
                        break;
                    }
                }
                if(isExist){
                    boolean isFriend = false; //이미 친구인지 확인
                    for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name).child(friend).getChildren()){
                        FirebaseFriend get = postSnapshot.getValue(FirebaseFriend.class);
                        if(get.name.equals(friend_name)){
                            isFriend = true;
                            break;
                        }
                    }
                    if(!isFriend){
                        Map<String, Object> childUpdates = new HashMap<>();
                        Map<String, Object> postValues = null;
                        Map<String, Object> postValues2 = null;
                        FirebaseFriend friend1 = new FirebaseFriend(name);
                        FirebaseFriend friend2 = new FirebaseFriend(friend_name);
                        postValues = friend1.toMap();
                        postValues2 = friend2.toMap();

                        childUpdates.put("/list/"+person_list+"/"+friend_name+"/"+friend+"/"+friend_name, postValues);
                        childUpdates.put("/list/"+person_list+"/"+name+"/"+friend+"/"+friend_name, postValues2);

                        mPostReference.updateChildren(childUpdates);
                    } //친구가 아닐때만 실행 , 친구 추가
                }

                ArrayList<String> friend_list = new ArrayList<String>(); // 친구리스트
                for(DataSnapshot postSnapshot : dataSnapshot.child(person_list).child(name).child(friend).getChildren()){
                    FirebaseFriend get = postSnapshot.getValue(FirebaseFriend.class);
                    friend_list.add(get.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
