package edu.skku.Map.Ddokkael;

import android.provider.ContactsContract;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class compareSchedule extends AppCompatActivity {
    int[][] result = new int[7][1441];

    private String name1;
    private String name2;

    private DatabaseReference mPostReference;

    EditText nameEt1;
    EditText nameEt2;
    Button btn;
    ListView listView;

    ArrayList<String> data = new ArrayList<String>();

    String[] date = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_schedule);

        nameEt1 = (EditText)findViewById(R.id.nameCom1Et);
        nameEt2 = (EditText)findViewById(R.id.nameCom2Et);
        btn = (Button)findViewById(R.id.buttonComp);
        listView = (ListView)findViewById(R.id.compList);

        arrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1 = nameEt1.getText().toString();
                name2 = nameEt2.getText().toString();

                if(name1.length()*name2.length()==0){
                    Toast.makeText(compareSchedule.this, "Data is missing", Toast.LENGTH_SHORT).show();
                } else{
                    if(mPostReference.equalTo(name1)!=null && mPostReference.equalTo(name2)!=null){
                        getFirebaseDatabase();
                        //clearET();
                    } else{
                        Toast.makeText(compareSchedule.this, "no such name", Toast.LENGTH_SHORT).show();
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
                for(int i=0;i<7;i++){
                    for(int j=0;j<=1440;j++){
                        result[i][j]=0;
                    }
                }
                boolean isFriend  = false;
                for(DataSnapshot postSnapshot : dataSnapshot.child(name1).child("Friend_List").getChildren()){
                    FirebaseName get = postSnapshot.getValue(FirebaseName.class);
                    if(get.name.equals(name2)) {
                        isFriend = true;
                        break;
                    }
                }
                if(!isFriend){
                    Toast.makeText(compareSchedule.this, "not friend", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DataSnapshot postSnapshot : dataSnapshot.child(name1).child("schedule_list").getChildren()) {
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    int st = get.start_time*60+get.start_min;
                    int ft = get.fin_time*60+get.fin_min;
                    int day = get.day;
                    for(int i=st;i<=ft;i++){
                        result[day][i] =  1;
                    }
                }
                for (DataSnapshot postSnapshot : dataSnapshot.child(name2).child("schedule_list").getChildren()) {
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    int st = get.start_time*60+get.start_min;
                    int ft = get.fin_time*60+get.fin_min;
                    int day = get.day;
                    for(int i=st;i<=ft;i++){
                        result[day][i] =  1;
                    }
                }
                int counting = 0;
                int st=0;
                int ft=0;
                for(int i=0;i<7;i++){
                    for(int j=0;j<=1440;j++){
                        if(counting == 0&&result[i][j]==0){
                            if(j!=0)    st = j-1;
                            else        st = j;
                            counting = 1;
                        } else if(counting == 1 && result[i][j]==0){
                            continue;
                        } else if(counting == 1 && result[i][j]==1){
                            ft = j;
                            data.add(timeToString(st, ft, i));
                            counting = 0;
                        } else if(counting == 0 && result[i][j]==1){
                            continue;
                        }
                    }
                    if(counting == 1){
                        ft = 1440;
                        counting = 0;
                        data.add(timeToString(st, ft, i));
                    }
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(data);
                arrayAdapter.notifyDataSetChanged();
                clearET();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("Person_List").addValueEventListener(postListener);
    }

    String timeToString(int st, int ft, int day){
        int startTime = st/60;
        int sm = st%60;
        int finishTime = ft/60;
        int fm = ft%60;
        String startT = Integer.toString(startTime);
        String startM;
        if(sm<10){
            startM = "0"+Integer.toString(sm);
        } else {startM = Integer.toString(sm);}
        String finalT = Integer.toString(finishTime);
        String finalM;
        if (fm < 10) {
            finalM = "0"+Integer.toString(fm);
        } else {finalM = Integer.toString(fm);}
        return (date[day]+" "+startT+":"+startM+"~"+finalT+":"+finalM);
    }

    public void clearET(){
        name1 = "";
        name2 = "";
        nameEt1.setText("");
        nameEt2.setText("");
    }

}
