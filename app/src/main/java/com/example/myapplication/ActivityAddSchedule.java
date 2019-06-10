package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ActivityAddSchedule extends Activity {

    EditText schedule_name, schedule_memo;
    Button add, cancel;
    TextView schedule_date, schedule_start, schedule_end, schedule_date_day;
    Switch sw;
    CheckBox checkBox;
    Boolean state = false;

    int sTime=-1, sMin=-1, fTime=-1, fMin=-1, nowDay;
    String dt="", name;
    String date, startTime, endTime, day="";
    String[] day_list = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    String dy="";

    DatabaseReference mPostReference;

    DataFirebaseSchedule addingSchedule;

    int fst = 0;

    DataClipBoard cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addschedule);

        final Calendar cal = Calendar.getInstance();

        //UI 객체생성
        schedule_name = (EditText)findViewById(R.id.edit_schedule_name);
        schedule_memo = (EditText)findViewById(R.id.edit_schedule_memo);
        schedule_date = (TextView)findViewById(R.id.setDateTextView);
        schedule_start = (TextView)findViewById(R.id.setStartTimeTextView);
        schedule_end = (TextView)findViewById(R.id.setEndTimeTextView);
        schedule_date_day = (TextView)findViewById(R.id.date_day);
        add = (Button)findViewById(R.id.btn_add);
        cancel = (Button)findViewById(R.id.btn_cancel);
        sw = (Switch)findViewById(R.id.switch1);
        checkBox = (CheckBox)findViewById(R.id.isOpen);

        checkBox.setChecked(true);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        //데이터 가져오기
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        cb = new DataClipBoard();
        try{
            cb.cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            cb.initialize();
            cb.searchDatafromClipBoard();
            if(cb.isExist){
                Intent intent2 = new Intent(ActivityAddSchedule.this, PopUpClipboard.class);
                startActivityForResult(intent2, 2);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckState();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String schedule = schedule_name.getText().toString();
                Log.d("정보", schedule+Integer.toString(sTime)+"~"+Integer.toString(fTime)+"/"+dt);
                if(state){
                    day = schedule_date.getText().toString();
                    Log.d("day", day);
                    if(day.equals("일요일"))   nowDay = 0;
                    else if(day.equals("월요일"))   nowDay = 1;
                    else if(day.equals("화요일"))   nowDay = 2;
                    else if(day.equals("수요일"))   nowDay = 3;
                    else if(day.equals("목요일"))   nowDay = 4;
                    else if(day.equals("금요일"))   nowDay = 5;
                    else nowDay = 6;
                    if(schedule.length()==0 || sTime == -1 || fTime == -1 || day.length()==0) return;
                }else {
                    if (schedule.length() == 0 || sTime == -1 || fTime == -1 || dt.length() == 0)
                        return;
                }
                int sstime = sTime*60 + sMin;
                int fftime = fTime*60+fMin;
                if(sstime >= fftime) {
                    Toast.makeText(ActivityAddSchedule.this, "종료 시간이 시작 시간보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String info = schedule_memo.getText().toString();
                int checked = checkBox.isChecked() ? 1 : 0;
                Log.d("info1", info);

                Random rnd = new Random();
                int c = rnd.nextInt(7);

                addingSchedule = new DataFirebaseSchedule(schedule, info, sTime, sMin, fTime, fMin, checked, c);
                Log.d("info2", addingSchedule.info);
                check();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_end(false);
            }
        });

        schedule_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(state){
                    Intent intent = new Intent(ActivityAddSchedule.this, PopUpDaySelect.class);
                    startActivityForResult(intent, 1);
                } else {
                    DatePickerDialog date_dialog = new DatePickerDialog(ActivityAddSchedule.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            nowDay = getDay(year, month, dayOfMonth);
                            Log.d("day", Integer.toString(nowDay));

                            date = String.format("%d.%d.%d", year, month + 1, dayOfMonth);
                            schedule_date.setText(date);
                            String mt = Integer.toString(month + 1);
                            String dt_ = Integer.toString(dayOfMonth);
                            if (mt.length() == 1) mt = "0" + mt;
                            if (dt_.length() == 1) dt_ = "0" + dt_;
                            dt = Integer.toString(year) + mt + dt_;
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                    date_dialog.show();
                }
            }
        });

        schedule_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog start_time_dialog = new TimePickerDialog(ActivityAddSchedule.this, new TimePickerDialog.OnTimeSetListener() {
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
                TimePickerDialog end_time_dialog = new TimePickerDialog(ActivityAddSchedule.this, new TimePickerDialog.OnTimeSetListener() {
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
        Log.d("info3", addingSchedule.info);
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        postValues = addingSchedule.toMap();
        Log.d("post", postValues.toString());
        if(state){
            if(fst!=0) childUpdates.put("/list/Person_List/"+name+"/"+getString(R.string.schedule)+"/"+day_list[nowDay]+"/"+addingSchedule.schedule+Integer.toString(fst), postValues);
            else childUpdates.put("/list/Person_List/"+name+"/"+getString(R.string.schedule)+"/"+day_list[nowDay]+"/"+addingSchedule.schedule, postValues);
        } else{
            if(fst!=0) childUpdates.put("/list/Person_List/"+name+"/schedule_date/"+day_list[nowDay]+"/"+dt+"/"+addingSchedule.schedule+Integer.toString(fst), postValues);
            else childUpdates.put("/list/Person_List/"+name+"/schedule_date/"+day_list[nowDay]+"/"+dt+"/"+addingSchedule.schedule, postValues);
        }
        mPostReference.updateChildren(childUpdates);
        Toast.makeText(ActivityAddSchedule.this, addingSchedule.schedule, Toast.LENGTH_SHORT).show();

        activity_end(true);
    }

    public void check(){
        mPostReference.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean cannot = false;
                int isFirst = 0;
                for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.person)).child(name).child(getString(R.string.schedule)).child(day_list[nowDay]).getChildren()){
                    DataFirebaseSchedule get = postSnapshot.getValue(DataFirebaseSchedule.class);

                    int st = get.start_time*60+get.start_min;
                    int ft = get.fin_time*60+get.fin_min;

                    int stO = sTime*60+sMin;
                    int ftO = fTime*60+fMin;

                    if ((st < ftO && ftO <= ft) || (st <= stO && stO < ft) || (st <= stO && ftO <= ft)) {
                        cannot = true;
                        break;
                    }
                    if(get.schedule.equals(addingSchedule.schedule)){
                        String key = postSnapshot.getKey();
                        if(key.equals(addingSchedule.schedule)) isFirst++;
                        else{
                            String split = key.substring(addingSchedule.schedule.length());
                            Log.d("split", split);
                            int size = Integer.parseInt(split);
                            if(size > isFirst) isFirst = size+1;
                        }
                    }
                }
                if(state){
                    for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.person)).child(name).child("schedule_date").child(day_list[nowDay]).getChildren()){
                        for(DataSnapshot post : postSnapshot.getChildren()){
                            DataFirebaseSchedule get = post.getValue(DataFirebaseSchedule.class);

                            int st = get.start_time*60+get.start_min;
                            int ft = get.fin_time*60+get.fin_min;

                            int stO = sTime*60+sMin;
                            int ftO = fTime*60+fMin;

                            Log.d("스케줄", get.schedule);
                            Log.d("시간s"+st, Integer.toString(stO));
                            Log.d("시간f"+ft, Integer.toString(ftO));

                            if ((st < ftO && ftO <= ft) || (st <= stO && stO < ft) || (st <= stO && ftO <= ft) || (stO <= st && ft <= ftO)) {
                                cannot = true;
                                break;
                            }
                            if(get.schedule.equals(addingSchedule.schedule)){
                                String key = postSnapshot.getKey();
                                if(key.equals(addingSchedule.schedule)) isFirst++;
                                else{
                                    String split = key.substring(addingSchedule.schedule.length());
                                    Log.d("split", split);
                                    int size = Integer.parseInt(split);
                                    if(size > isFirst) isFirst = size+1;
                                }
                            }
                        }
                    }
                } else{
                    for(DataSnapshot postSnapshot : dataSnapshot.child(getString(R.string.person)).child(name).child("schedule_date").child(day_list[nowDay]).child(dt).getChildren()){
                        DataFirebaseSchedule get = postSnapshot.getValue(DataFirebaseSchedule.class);
                        Log.d("왜 다안읽어", get.schedule+postSnapshot.getKey());
                        int st = get.start_time*60+get.start_min;
                        int ft = get.fin_time*60+get.fin_min;

                        int stO = sTime*60+sMin;
                        int ftO = fTime*60+fMin;

                        if ((st < ftO && ftO <= ft) || (st <= stO && stO < ft)) {
                            cannot = true;
                            break;
                        }
                        if(get.schedule.equals(addingSchedule.schedule)){
                            String key = postSnapshot.getKey();
                            if(key.equals(addingSchedule.schedule)) isFirst++;
                            else{
                                String split = key.substring(addingSchedule.schedule.length());
                                Log.d("split", split);
                                int size = Integer.parseInt(split);
                                if(size > isFirst) isFirst = size+1;
                            }
                        }
                    }
                }
                if(cannot){
                    Toast.makeText(ActivityAddSchedule.this, "같은 시간대에 이미 일정이 있습니다", Toast.LENGTH_SHORT).show();
                } else{
                    fst = isFirst;
                    postFirebaseDatabase(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckState(){
        schedule_date_day = (TextView)findViewById(R.id.date_day);
        schedule_date = (TextView)findViewById(R.id.setDateTextView);
        if(sw.isChecked()){
            schedule_date_day.setText("요일");
            schedule_date.setText("요일");
            state = true;
        }else{
            schedule_date_day.setText("날짜");
            schedule_date.setText("날짜");
            state = false;
        }
    }

    int getDay(int year, int month, int dayint){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, dayint);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        return day_of_week-1;
    }

    public void activity_end(boolean isYes){
        Intent intent = new Intent();
        intent.putExtra("button",1);
        if(isYes)   setResult(RESULT_OK, intent);
        else setResult(RESULT_CANCELED, intent);

        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                day = data.getExtras().getString("day");
                schedule_date = (TextView)findViewById(R.id.setDateTextView);
                schedule_date.setText(day);
            }
        }
        else if(requestCode == 2){
            if(resultCode==RESULT_OK){
                sTime = cb.hour;
                if(cb.apm==1) sTime = sTime + 12;
                sMin = cb.minute;
                schedule_name.setText(cb.schedule);
                startTime = String.format("%d:%d",sTime, sMin);
                schedule_start.setText(startTime);
            }
        }
    }
}