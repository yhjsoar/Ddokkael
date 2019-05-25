package edu.skku.Map.Ddokkael;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addSchedule extends AppCompatActivity {

    private DatabaseReference mPostReference;
    String name="", schedule="";
    int startTime, startMin, finTime, finMin, day;
    String sort = "name";
    EditText nameEt, dayEt, scheEt, stEt, smEt, ftEt, fmEt;
    Button btn;
    ListView listView;

    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        FirebaseApp.initializeApp(getApplicationContext());

        data = new ArrayList<String>();
        nameEt = (EditText)findViewById(R.id.nameet);
        dayEt = (EditText)findViewById(R.id.dayet);
        scheEt = (EditText)findViewById(R.id.scheet);
        stEt = (EditText)findViewById(R.id.startTimeet);
        smEt = (EditText)findViewById(R.id.startMineet);
        ftEt = (EditText)findViewById(R.id.finishTimeet);
        fmEt = (EditText)findViewById(R.id.finishMineet);

        btn = (Button)findViewById(R.id.button);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString();
                String dayS = dayEt.getText().toString();
                schedule = scheEt.getText().toString();
                String stS = stEt.getText().toString();
                String smS = smEt.getText().toString();
                String ftS = ftEt.getText().toString();
                String fmS = fmEt.getText().toString();

                if((name.length()*dayS.length()*schedule.length()*stS.length()*ftS.length())==0){
                    Toast.makeText(addSchedule.this, "Data is missing", Toast.LENGTH_SHORT).show();
                }else{
                    if(smS.length()==0){
                        startMin=0;
                    } else{
                        startMin = Integer.parseInt(smS);
                    }
                    if(fmS.length()==0){
                        finMin=0;
                    }else{
                        finMin=Integer.parseInt(fmS);
                    }
                    day = dayToInt(dayS);
                    startTime = Integer.parseInt(stS);
                    finTime = Integer.parseInt(ftS);

                    postFirebaseDatabase(true);
                }
            }
        });
    }
    //    public void getFirebaseDatabase() {
//        final ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d("onDataChange", "Data is Updated");
//                data.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    String key = postSnapshot.getKey();
//                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
//                    String[] info = {get.name, get.day, get.schedule, get.time};
//                    String result = info[0] + " : " + info[1] + "(" + info[3] + ", " + info[2] + ")";
//                    data.add(result);
//                    Log.d("getFirebaseDatabase", "key: " + key);
//                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
//                }
//                arrayAdapter.clear();
//                arrayAdapter.addAll(data);
//                arrayAdapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        };
//        mPostReference.child("schedile_list").addValueEventListener(postListener);
//    }
    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebasePost post = new FirebasePost(schedule, startTime, startMin, finTime, finMin, day);
            postValues = post.toMap();
        }
        childUpdates.put("/Person_List/"+name+"/schedule_list/"+schedule, postValues);
        mPostReference.updateChildren(childUpdates);
        Toast.makeText(addSchedule.this, schedule, Toast.LENGTH_SHORT).show();
        clearET();
    }
    public void clearET () {
        nameEt.setText("");
        stEt.setText("");
        smEt.setText("");
        ftEt.setText("");
        fmEt.setText("");
        dayEt.setText("");
        scheEt.setText("");
        name="";
        day=0;
        startTime=0;
        startMin=0;
        finTime=0;
        finMin=0;
        schedule="";
    }
    public int dayToInt(String date){
        if(date.equals("MON")||date.equals("mon")||date.equals("Mon")||date.equals("MONDAY")||date.equals("Monday")||date.equals("monday")){
            return 0;
        } else if (date.equals("TUE")||date.equals("Tue")||date.equals("tue")||date.equals("tuesday")||date.equals("Tuesday")||date.equals("TUESDAY")) {
            return 1;
        } else if (date.equals("WED")||date.equals("Wed")||date.equals("wed")||date.equals("wednesday")||date.equals("Wednesday")||date.equals("WEDNESDAY")) {
            return 2;
        } else if (date.equals("THU")||date.equals("Thu")||date.equals("thu")||date.equals("thursday")||date.equals("Thursday")||date.equals("THURSDAY")) {
            return 3;
        } else if (date.equals("FRI")||date.equals("Fri")||date.equals("fri")||date.equals("friday")||date.equals("Friday")||date.equals("FRIDAY")) {
            return 4;
        } else if (date.equals("SAT")||date.equals("Sat")||date.equals("sat")||date.equals("saturday")||date.equals("Saturday")||date.equals("SATURDAY")){
            return 5;
        } else{
            return 6;
        }
    }
}
