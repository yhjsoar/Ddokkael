package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends PagerAdapter {

    LayoutInflater inflater;

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
            final ListView calendar_list = (ListView)view.findViewById(R.id.calendar_list);
            final ArrayList<String> schedule_list = new ArrayList<String>();

            // 오늘의 일정 정보(Default 값)
            schedule_list.add("9:00 A");
            schedule_list.add("10:30 B");
            schedule_list.add("11:00 모바일 앱 실습");
            schedule_list.add("13:30 자퇴");
            schedule_list.add("15:00 모바일 앱 실습");
            schedule_list.add("16:30 자퇴");

            final ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, schedule_list);
            calendar_list.setAdapter(adapter);
            String today = new SimpleDateFormat("yyyy/M/dd").format(new Date());
            calendar_text.setText(today);

            // 날자 선택
            calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    calendar_text.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                    // 해당 날짜의 data를 읽어와서 listView에 넣기
                    schedule_list.clear(); // schedule_list 초기화
                    schedule_list.add("11:30 휴학각");
                    schedule_list.add("12:30 휴학각");
                    schedule_list.add("13:30 휴학각");
                    schedule_list.add("14:30 휴학각");
                    schedule_list.add("15:30 휴학각");

                    calendar_list.setAdapter(adapter);
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
            final ArrayList<String> friends = new ArrayList<String>();

            // 오늘의 일정 정보(Default 값)
            friends.add("김두영");
            friends.add("윤혜진");
            friends.add("유태우");
            friends.add("바보");

            final ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, friends);
            friends_list.setAdapter(adapter);
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
}
