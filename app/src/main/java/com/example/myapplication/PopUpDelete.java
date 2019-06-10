package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PopUpDelete extends Activity {
    DatabaseReference databaseReference;

    String data;
    String dat;

    Button yes, no;

    String deleteDate;
    String deleteSchedule;
    String name;
    int sTime, sMin, fTime, fMin, deleteDay;

    String[] date = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_delete);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        yes = (Button)findViewById(R.id.ysbtn);
        no = (Button)findViewById(R.id.nbtn);

        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        dat = intent.getStringExtra("date");
        name = intent.getStringExtra("name");

        String[] dt = dat.split("/");
        dt[0] = dt[0].substring(dt[0].lastIndexOf(' ')+1);
        deleteDate = dt[0] + (dt[1].length()==1? "0"+dt[1]: dt[1]) + (dt[2].length()==1? "0"+dt[2] : dt[2]);

        String stringStart = data.substring(0, data.indexOf('~'));
        String stringFinish = data.substring(data.indexOf('~')+1, data.indexOf(' '));
        String stringStartTime = stringStart.substring(0, stringStart.indexOf(':'));
        String stringStartMin = stringStart.substring(stringStart.indexOf(':')+1);
        String stringFinishTime = stringFinish.substring(0, stringFinish.indexOf(':'));
        String stringFinishMin = stringFinish.substring(stringFinish.indexOf(':')+1);
        deleteSchedule = data.substring(data.indexOf(' ')+1);

        sTime = Integer.parseInt(stringStartTime);
        sMin  = Integer.parseInt(stringStartMin);
        fTime = Integer.parseInt(stringFinishTime);
        fMin = Integer.parseInt(stringFinishMin);


        deleteDay = getDay(Integer.parseInt(dt[0]), Integer.parseInt(dt[1])-1, Integer.parseInt(dt[2]));

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end();
            }
        });
    }

    public void end(){
        Intent intent = new Intent();
        intent.putExtra("button",0);
        setResult(RESULT_OK, intent);

        finish();
    }

    int getDay(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        return day_of_week-1;
    }

    public void check(){
        databaseReference.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isDeleted = false;
                for(DataSnapshot post : dataSnapshot.child(getString(R.string.person)).child(name).child("schedule_date").child(date[deleteDay]).child(deleteDate).getChildren()){
                    FirebaseSchedule get = post.getValue(FirebaseSchedule.class);
                    if(get.schedule.equals(deleteSchedule) && get.start_min == sMin && get.start_time == sTime
                            && get.fin_min == fMin && get.fin_time == fTime){
                        post.getRef().removeValue();
                        isDeleted = true;
                        break;
                    }
                }
                if(!isDeleted){
                    for(DataSnapshot post : dataSnapshot.child(getString(R.string.person)).child(name).child(getString(R.string.schedule)).child(date[deleteDay]).getChildren()){
                        FirebaseSchedule get = post.getValue(FirebaseSchedule.class);
                        if(get.schedule.equals(deleteSchedule) && get.start_min == sMin && get.start_time == sTime
                                && get.fin_min == fMin && get.fin_time == fTime){
                            post.getRef().removeValue();
                            isDeleted = true;
                            break;
                        }
                    }
                }
                if(!isDeleted) Toast.makeText(PopUpDelete.this, "일정이 지워지지 않았습니다.", Toast.LENGTH_SHORT).show();
                end();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
