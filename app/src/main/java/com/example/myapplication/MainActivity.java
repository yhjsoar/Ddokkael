package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    // timetable_data.add(date_data)
    private ArrayList<ArrayList<ArrayList<String>>> timetable_data = null; // 시간표 (요일 정보를 담는 리스트)
    private ArrayList<ArrayList<String>> date_data = null; // 요일 정보 (일정을 담는 리스트)
    private ArrayList<String> data = null; // 일정 (일정 정보를 담는 리스트)
    ViewPager pager;
    View header;
    Button btn[] = new Button[3];
    ImageView friendPlus;
    int v = 0, d = 1;
    String name;

    Toolbar myToolbar;

    double longitude;
    double latitude;

    DataWeatherItem item;
    LocationManager lm;

    LinearLayout layout;

    int today_year, today_month, today_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        name = intent.getStringExtra("ID");

        // Contents
        myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        pager = (ViewPager)findViewById(R.id.viewPager);
        AdapterCustom adapter = new AdapterCustom(getLayoutInflater(), name, this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        header = getLayoutInflater().inflate(R.layout.calendar, null, false);
        layout = (LinearLayout)header.findViewById(R.id.calender_back);

        setSupportActionBar(myToolbar);

        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        today_year = date.getYear()+1900;
        today_month = date.getMonth()+1;
        today_date = date.getDate();

        // Menu Buttons
        btn[0] = (Button)findViewById(R.id.button1);
        btn[1] = (Button)findViewById(R.id.button2);
        btn[2] = (Button)findViewById(R.id.button3);

        ImageView add_schedule = (ImageView) findViewById(R.id.button4);

        btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
                Toast.makeText(MainActivity.this,"calendar", Toast.LENGTH_SHORT).show();
            }
        });

        btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
                Toast.makeText(MainActivity.this,"timetable", Toast.LENGTH_SHORT).show();
            }
        });

        btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);
                Toast.makeText(MainActivity.this,"friends", Toast.LENGTH_SHORT).show();
            }
        });

        add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityAddSchedule.class);
                intent.putExtra("name", name);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                int popup_add = data.getExtras().getInt("button");
                if(popup_add == 1){
                    Toast.makeText(MainActivity.this, "일정을 추가했습니다.", Toast.LENGTH_SHORT).show();
                    String schedule_name = data.getExtras().getString("name");
                    String schedule_info = data.getExtras().getString("info");
                }else{
                    Toast.makeText(MainActivity.this, "일정 추가를 취소했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(requestCode==2){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.logout){
            Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
            intent.putExtra("logout", "yes");
            startActivity(intent);
            finish();
        }
        return true;
    }

    final LocationListener networkLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
    };

    public class MyAsyncTask extends AsyncTask<String, Void, DataWeatherItem> {
        OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected DataWeatherItem doInBackground(String... params) {
            HttpUrl.Builder urlBuilder= HttpUrl.parse("https://api.openweathermap.org/data/2.5/weather").newBuilder();
            urlBuilder.addQueryParameter("lat",Double.toString(latitude));
            urlBuilder.addQueryParameter("lon",Double.toString(longitude));
            urlBuilder.addQueryParameter("APPID","916262eccb4929ea1237b530a4b776b4");
            String requestUrl = urlBuilder.build().toString();
            Log.d("Result:", requestUrl);
            Request request = new Request.Builder().url(requestUrl).build();
            try{
                okhttp3.Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream()).getAsJsonObject();
                item = gson.fromJson(rootObject, DataWeatherItem.class);
                return item;
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(DataWeatherItem result) {
            super.onPostExecute(result);
        }
    }

    public DataWeatherItem getWeather(){
        item = null;
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    0 );
        }
        else{
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    10,
                    0,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    10,
                    0,
                    networkLocationListener);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location==null) location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location==null) return null;
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            MyAsyncTask mProcessTask = new MyAsyncTask();
            try{
                item = mProcessTask.execute().get();
                if(item!=null) {
                    Log.d("weather", item.weather.get(0).main);
                }
            } catch(Exception e){
                layout.setBackgroundResource(R.drawable.clear);
                e.printStackTrace();
            }
        }
        return item;
    }

}

