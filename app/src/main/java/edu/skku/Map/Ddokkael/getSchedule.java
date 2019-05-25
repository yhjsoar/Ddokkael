package edu.skku.Map.Ddokkael;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class getSchedule extends AppCompatActivity {
    private DatabaseReference mPostReference;

    private String name;

    EditText nameEt;
    Button btn;
    ListView listView;

    ArrayList<String> data = new ArrayList<String>();

    String[] date = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_schedule);

        nameEt = (EditText) findViewById(R.id.nameForSchedule);
        btn = (Button) findViewById(R.id.buttonGet);
        listView = (ListView)findViewById(R.id.scheduleList);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString();

                if (name.length() == 0) {
                    Toast.makeText(getSchedule.this, "Data is missing", Toast.LENGTH_SHORT).show();
                } else {
                    if (mPostReference.equalTo(name) != null) {
                        getFirebaseDatabase();
                    }
                }
            }
        });
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                data.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.child(name).child("schedule_list").getChildren()) {
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    String sched = get.schedule;
                    int day = get.day;
                    String result = sched + " : " + date[day] + " (" + timeToString(get.start_time, get.start_min, get.fin_time, get.fin_min) + ")";
                    data.add(result);
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(data);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("Person_List").addValueEventListener(postListener);
    }

    String timeToString(int sT, int sM, int fT, int fM){
        String startT = Integer.toString(sT);
        String startM;
        if(sM<10){
            startM = "0"+Integer.toString(sM);
        } else { startM = Integer.toString(sM);}
        String finalT = Integer.toString(fT);
        String finalM;
        if (fM < 10) {
            finalM = "0"+Integer.toString(fM);
        } else { finalM = Integer.toString(fM);}
        return (startT+":"+startM+"~"+finalT+":"+finalM);
    }
}


