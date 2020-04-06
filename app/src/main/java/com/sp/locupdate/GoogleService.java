package com.sp.locupdate;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.LocationResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static android.content.ContentValues.TAG;
import static android.os.SystemClock.elapsedRealtime;

/**
 * Created by deepshikha on 24/11/16.
 */

public class GoogleService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    LocationListener locationListener;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 300000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmService.set(ELAPSED_REALTIME, elapsedRealtime() + 1000,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);


        Log.d(TAG, "onTaskRemoved: removed service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    public GoogleService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startMyOwnForeground();
        mTimer = new Timer();
        startTimer();

//        fn_getlocation();
    }

        private void startMyOwnForeground(){
            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
            String channelName = "My Background Service";
            NotificationChannel chan = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
                chan.setLightColor(Color.BLUE);
                chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert manager != null;
                manager.createNotificationChannel(chan);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
                Notification notification = notificationBuilder.setOngoing(true)
                        .setContentTitle("App is running in background")
                        .setPriority(NotificationManager.IMPORTANCE_MIN)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .build();
                startForeground(2, notification);
            }else {
                Notification notification = new Notification.Builder(this)
                        .setContentTitle("Android")
                        .setContentText("")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .build();

                startForeground(1, notification);
            }

        }



    public  void startTimer(){
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isGPSEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                if (locationManager!=null){
                    locationListener =new LocationListener() {
                        @Override
                        public void onLocationChanged(Location loc) {
                            Toast.makeText(GoogleService.this, ""+loc.getLatitude(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };
                  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        Log.e("latitude",location.getLatitude()+"");
                        Log.e("longitude",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        try {
                            DateFormat df = new SimpleDateFormat("HH:mm");
                            String realTime = df.format(Calendar.getInstance().getTime());
                            String myTime="8:00";
                            String myTime2="20:00";


                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                            Date mytime = sdf.parse(myTime);
                            Date mytime2 = sdf.parse(myTime2);
                            Date realtime = sdf.parse(realTime);
                            if(realtime.after(mytime)&&realtime.before(mytime2)){
                                fn_update(location);
                                Log.d(TAG, "fn_getlocation: gps");
                            }else{
                                Log.d(TAG, "fn_getlocation: timeeeeeee");
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }




                    }
                }
            }else {
                if (isNetworkEnable) {
                    location = null;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    if (locationManager != null) {

                        locationListener =new LocationListener() {
                            @Override
                            public void onLocationChanged(Location loc) {
                                Toast.makeText(GoogleService.this, ""+loc.getLatitude(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        };
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {

                      Log.e("latitude", location.getLatitude() + "");
                       Log.e("longitude", location.getLongitude() + "");

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            try {
                                DateFormat df = new SimpleDateFormat("HH:mm");
                                String realTime = df.format(Calendar.getInstance().getTime());
                                String myTime="8:00";
                                String myTime2="20:00";


                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                                Date mytime = sdf.parse(myTime);
                                Date mytime2 = sdf.parse(myTime2);
                                Date realtime = sdf.parse(realTime);
                                if(realtime.after(mytime)&&realtime.before(mytime2)){
                                    fn_update(location);
                                    Log.d(TAG, "fn_getlocation: network");
                                }else{
                                    Log.d(TAG, "fn_getlocation: timeeeeeeeeeeeeee");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }






        }

    }

    private class TimerTaskToGetLocation extends TimerTask{
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(final Location location){
//        Log.d(TAG, "fn_update: "+location.getLatitude()+location.getLongitude());
        final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String name = sharedPreferences.getString("name","");
        final String number = sharedPreferences.getString("number","");
//        Log.d(TAG, "fn_update: "+name+number);


        String url="http://13.233.166.82:7777/user";

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse: "+error);

            }




        }){
            @Override
            protected Map<String,String> getParams(){


                Map<String,String> params = new HashMap<String, String>();
                params.put("username",name);
                params.put("mobile",number);
                params.put("latitude", String.valueOf(location.getLatitude()));
                params.put("longitude", String.valueOf(location.getLongitude()));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }


        };

        Mysingleton.getMinstance(getApplicationContext()).addToRequestQue(request);
//        Log.d(TAG, "fn_update: request "+request);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: service distroyied");
    }
}
