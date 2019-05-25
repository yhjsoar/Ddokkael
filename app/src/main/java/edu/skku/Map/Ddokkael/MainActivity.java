package edu.skku.Map.Ddokkael;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity {

    static final String[] LIST_MENU = {"스케쥴 추가", "친구 추가", "시간표 비교", "시간표 조회"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) findViewById(R.id.listView) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if(position==0){
                    intent = new Intent(MainActivity.this, addSchedule.class);
                } else if(position==1){
                    intent = new Intent(MainActivity.this, addFriend.class);
                } else if(position==2){
                    intent = new Intent(MainActivity.this, compareSchedule.class);
                } else{
                    intent = new Intent(MainActivity.this, getSchedule.class);
                }

                startActivity(intent);
            }
        });
    }
}
