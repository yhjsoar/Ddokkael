package edu.skku.map.ddokkael;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    ClipBoard cb;
    TextView text2;

    double longitude;
    double latitude;

    String result = "no data";

    WeatherItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cb = new ClipBoard();
        text2 = (TextView)findViewById(R.id.text2);
        final LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                try{
                    cb.cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    cb.initialize();
                    cb.searchDatafromClipBoard();
                    TextView textView = (TextView)findViewById(R.id.text);
                    if(cb.isExist){
                        textView.setText(cb.parseToString());
                    } else{
                        textView.setText("no data");
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(),
                                android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( MainActivity.this, new String[]
                                    { android.Manifest.permission.ACCESS_FINE_LOCATION },
                            0 );
                }
                else{
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            10,
                            0,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            10,
                            0,
                            networkLocationListener);
                    MyAsyncTask mProcessTask = new MyAsyncTask();
                    try{
                        item = mProcessTask.execute().get();
                        setString();
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
                text2.setText(result);
            }
        });
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

    public class MyAsyncTask extends AsyncTask<String, Void, WeatherItem> {
        OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected WeatherItem doInBackground(String... params) {
            HttpUrl.Builder urlBuilder= HttpUrl.parse("https://api.openweathermap.org/data/2.5/weather").newBuilder();
            urlBuilder.addQueryParameter("lat",Double.toString(latitude));
            urlBuilder.addQueryParameter("lon",Double.toString(longitude));
            urlBuilder.addQueryParameter("APPID","916262eccb4929ea1237b530a4b776b4");
            String requestUrl = urlBuilder.build().toString();
            Log.d("Result:", requestUrl);
            Request request = new Request.Builder().url(requestUrl).build();
            try{
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream()).getAsJsonObject();
                item = gson.fromJson(rootObject, WeatherItem.class);
                return item;
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(WeatherItem result) {
            super.onPostExecute(result);
        }
    }

    public void setString(){
        result = "City: "+item.name + ", "+item.sys.country;
        result += "\ntemp, humidity: "+Double.toString(item.main.temp)+", "+Double.toString(item.main.humidity);
        result += "\nwind: "+Double.toString(item.wind.speed);
        result += "\nweather: "+item.weather.get(0).main + ", " + item.weather.get(0).description+", "+item.weather.get(0).icon;
    }
}
