package com.sp.locupdate;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

  private   SharedPreferences sharedPreferences;
   private SharedPreferences.Editor mEditor;
   LocationResult locationResult;

   EditText mName,mNumber;
   Button register,testLocationBtn;
   CardView  cardView;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



      Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Employee Registration");

        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if(am != null) {
            List<ActivityManager.AppTask> tasks = am.getAppTasks();
            if (tasks != null && tasks.size() > 0) {
                tasks.get(0).setExcludeFromRecents(true);
            }
        }

        final GoogleService googleService=new GoogleService();

        cardView=findViewById(R.id.cardView);
        mName=findViewById(R.id.nameEditTxt);
        mNumber=findViewById(R.id.numberEditTxt);
        register=findViewById(R.id.register_btn);




        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        mEditor=sharedPreferences.edit();
        locationPremission();
        gpsStatusCheck();
            checkSharedPreference();

//starting service if its not running
            if(!isMyServiceRunning(GoogleService.class)){
                if(!TextUtils.isEmpty(sharedPreferences.getString("name",""))){
                    Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                    startService(intent);

                }

            }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!TextUtils.isEmpty(mName.getText().toString())&&!TextUtils.isEmpty(mNumber.getText().toString())){
                    String name=mName.getText().toString();
                    mEditor.putString("name",name);
                    Log.d("", "onClick: "+name);
                    mEditor.commit();

                    String number=mNumber.getText().toString();
                    mEditor.putString("number",number);
                    Log.d("", "onClick: "+number);
                    mEditor.commit();

                    sendToServer();
                    checkSharedPreference();
                    Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                    startService(intent);
                }else {
                   if(TextUtils.isEmpty(mName.getText().toString())){
                       mName.setError("Empty");
                   }else if(TextUtils.isEmpty(mNumber.getText().toString())){
                       mNumber.setError("Empty");

                    }
                }




            }
        });



    }

    private  void  checkSharedPreference(){
        String name = sharedPreferences.getString("name","");
        String number = sharedPreferences.getString("number","");

        mName.setText(name);
        mNumber.setText(number);
        Log.d("", "checkSharedPreference: "+name+number);

        if(!TextUtils.isEmpty(name)){
//            PackageManager p = getPackageManager();
//
//            p.setComponentEnabledSetting(getComponentName(),PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
           cardView.setVisibility(View.INVISIBLE);
//            updateLocation();

        }


    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void gpsStatusCheck() {

//            startActivity( new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        }


    public void locationPremission(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }


    }




    private void sendToServer() {


        String url="your URL here";

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "error"+error, Toast.LENGTH_SHORT).show();

            }




        }){
            @Override
            protected Map<String,String> getParams(){
                String name = sharedPreferences.getString("name","");
                String number = sharedPreferences.getString("number","");

                Map<String,String> params = new HashMap<String, String>();
                params.put("username",name);
                params.put("mobile",number);
                params.put("latitude", "");
                params.put("longitude","");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }


        };

        Mysingleton.getMinstance(MainActivity.this).addToRequestQue(request);



    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}