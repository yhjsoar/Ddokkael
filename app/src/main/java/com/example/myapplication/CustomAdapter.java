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

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
