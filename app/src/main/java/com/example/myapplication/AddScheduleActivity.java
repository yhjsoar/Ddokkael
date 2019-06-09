package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddScheduleActivity extends Activity {

    EditText schedule_name, schedule_memo;
    Button add, cancel;
    TextView schedule_date, schedule_start, schedule_end;
    String date, startTime, endTime;
    Boolean Setdate, SetStart, SetEnd;
    CheckBox checkBox;

    String name;

    int sTime=-1, sMin=-1, fTime=-1, fMin=-1;
    String dt="";
    int nowDay;

    DatabaseReference mPostReference;

    FirebaseSchedule addingSchedule;

    String[] day = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_schedule);

        final Calendar cal = Calendar.getInstance();

        //UI 객체생성
        schedule_name = (EditText)findViewById(R.id.edit_schedule_name);
        schedule_memo = (EditText)findViewById(R.id.edit_schedule_memo);
        schedule_date = (TextView)findViewById(R.id.setDateTextView);
        schedule_start = (TextView)findViewById(R.id.setStartTimeTextView);
        schedule_end = (TextView)findViewById(R.id.setEndTimeTextView);
        add = (Button)findViewById(R.id.btn_add);
        cancel = (Button)findViewById(R.id.btn_cancel);
        checkBox = (CheckBox)findViewById(R.id.isOpen);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        //데이터 가져오기
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String schedule = schedule_name.getText().toString();
                Log.d("정보", schedule+Integer.toString(sTime)+"~"+Integer.toString(fTime)+"/"+dt);
                if(schedule.length()==0 || sTime==-1 || fTime==-1 || dt.length()==0) return;
                String info = schedule_memo.getText().toString();
                int checked = checkBox.isChecked()? 1 : 0;

                addingSchedule = new FirebaseSchedule(schedule, info, sTime, sMin, fTime, fMin, checked);

                check();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_end();
            }
        });

        schedule_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog date_dialog = new DatePickerDialog(AddScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        nowDay = getDay(year, month, dayOfMonth);
                        Log.d("day", Integer.toString(nowDay));

                        date = String.format("%d.%d.%d",year,month+1,dayOfMonth);
                        schedule_date.setText(date);
                        String mt = Integer.toString(month+1);
                        String dt_ = Integer.toString(dayOfMonth);
                        if(mt.length()==1) mt = "0"+mt;
                        if(dt_.length()==1) dt_ = "0"+dt_;
                        dt = Integer.toString(year)+mt+dt_;
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                date_dialog.show();
            }
        });

        schedule_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog start_time_dialog = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTime = String.format("%d:%d",hourOfDay,minute);
                        schedule_start.setText(startTime);
                        sTime = hourOfDay;
                        sMin = minute;
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);

                start_time_dialog.show();
            }
        });


        schedule_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog end_time_dialog = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime = String.format("%d:%d",hourOfDay,minute);
                        schedule_end.setText(endTime);
                        fTime = hourOfDay;
                        fMin = minute;
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);

                end_time_dialog.show();
            }
        });
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            postValues = addingSchedule.toMap();
        }
        childUpdates.put("/list/Person_List/"+name+"/schedule_date/"+day[nowDay]+"/"+dt+"/"+addingSchedule.schedule, postValues);
        mPostReference.updateChildren(childUpdates);
        Toast.makeText(AddScheduleActivity.this, addingSchedule.schedule, Toast.LENGTH_SHORT).show();

        activity_end();
    }

    public void check(){
        mPostReference.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean cannot = false;
                int isFirst = 0;
                for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.person)).child(name).child(getString(R.string.schedule)).child(day[nowDay]).getChildren()){
                    FirebaseSchedule get = postSnapshot.getValue(FirebaseSchedule.class);
                    int st = get.start_time*60+get.start_min;
                    int ft = get.fin_time*60+get.fin_min;

                    int stO = sTime*60+sMin;
                    int ftO = fTime*60+fMin;

                    if ((st < ftO && ftO <= ft) || (st <= stO && stO < ft)) {
                        cannot = true;
                        break;
                    }
                    if(get.schedule.equals(addingSchedule.schedule)){
                        isFirst++;
                    }
                }
                for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.person)).child(name).child("schedule_date").child(day[nowDay]).child(dt).getChildren()){
                    FirebaseSchedule get = postSnapshot.getValue(FirebaseSchedule.class);
                    int st = get.start_time*60+get.start_min;
                    int ft = get.fin_time*60+get.fin_min;

                    int stO = sTime*60+sMin;
                    int ftO = fTime*60+fMin;

                    if ((st < ftO && ftO <= ft) || (st <= stO && stO < ft)) {
                        cannot = true;
                        break;
                    }
                    if(get.schedule.equals(addingSchedule.schedule)){
                        isFirst++;
                    }
                }
                Log.d("머야", "대체");
                if(cannot){
                    Toast.makeText(AddScheduleActivity.this, "같은 시간대에 이미 일정이 있습니다", Toast.LENGTH_SHORT).show();
                } else{
                    if(isFirst!=0) addingSchedule.schedule = addingSchedule.schedule + Integer.toString(isFirst);
                    postFirebaseDatabase(true);
                }
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

    public void activity_end(){
        Intent intent = new Intent();
        intent.putExtra("button",1);
        setResult(RESULT_OK, intent);

        finish();
    }
}