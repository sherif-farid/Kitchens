package xyz.topapproid.www.kitchen;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;


public class Start extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{


    private FirebaseAuth mAuth;
    double lat=0;
    double lon=0;
    //  CountDownTimer mycountdown;
    //  int counter=4;
    //  TextView text_countd;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        getSupportActionBar().hide();
      //  text_countd=findViewById(R.id.text_count);
        mAuth=FirebaseAuth.getInstance();
        mAuth.signInAnonymously();

        check_for_gps();
        createLocationRequest();
        createApi();
        get_location_and_enter();
        firsttime_table_map();
       // this.deleteDatabase("SHDB");

    }

    @Override
    protected void onStart(){
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        get_location_and_enter();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        get_location_and_enter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    @Override
    protected void onResume(){
        super.onResume();
        mGoogleApiClient.connect();
        get_location_and_enter();
    }



    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("يرجي تفعيل GPS أولاً")
                .setCancelable(false)
                .setPositiveButton("تفعيل الآن",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("إلغاء",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }
    public void insert_table_id(double lat,double lon) {
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("table_id", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues info = new ContentValues();
        info.put("lat", lat);
        info.put("lon", lon);
        if (cursor.getCount() <= 0) {
            db.insert("table_id", null, info);
        } else {
            db.update("table_id", info, "id=?", new String[]{String.valueOf(1)});
        }
        cursor.close();
    }
    public void insert_table3(){

        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor_table3 = db.query("table3", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        Cursor cursor_settings = db.query("settings", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues info = new ContentValues();
        ContentValues info2 = new ContentValues();
        info.put("curent_id", "");
        info2.put("name", "");
        info2.put("phone", "");
        info2.put("online", "1");
        info2.put("code", "2");
        info2.put("country", "مصر");
        //t1=temp key
        info2.put("t1", "");
        //t2=start day
        info2.put("t2", "");
        //t3= number of notifications has been sent
        info2.put("t3", "");
        info2.put("t4", "");
        info2.put("t5", "");
        info2.put("t6", "");
        info2.put("t7", "");
        info2.put("t8", "");
        info2.put("t9", "");
        info2.put("t10", "");
        if (cursor_table3.getCount() <= 0) {
            db.insert("table3", null, info);
        }

        if (cursor_settings.getCount() <= 0) {
            db.insert("settings", null, info2);
        }
        cursor_table3.close();
        cursor_settings.close();
    }
    protected void createLocationRequest() {
        mLocationRequest =  LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void check_for_gps(){
        LocationManager  locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&
               ! locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            showGPSDisabledAlertToUser();
        }
    }
    public void createApi() {
        if ( mGoogleApiClient ==null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    protected void startLocationUpdates() {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }else {
            mCurrentLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mCurrentLocation==null) {
                 LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }


    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);

    }
    private void get_location_and_enter() {
        insert_table3();
        if (null != mCurrentLocation) {
            lat = mCurrentLocation.getLatitude();
            lon = mCurrentLocation.getLongitude();
            insert_table_id(lat, lon);
            Intent go = new Intent(Start.this, Test_db.class);startActivity(go);
            /*
            mycountdown = new CountDownTimer(2000, 500) {
                @Override
                public void onTick(long l) {
                    counter-=1;
                    String s_counter=String.valueOf(counter);
                    text_countd.setText(s_counter);
                }

                @Override
                public void onFinish() {

                }
            }.start();
*/

        }
    }

    public void insert_table_map(String random_key,String lat,String lon,String m_name) {
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("map", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues r1 = new ContentValues();

        r1.put("random_key", random_key);
        r1.put("lat",lat);
        r1.put("lon", lon);
        r1.put("m_name", m_name);
        db.insert("map", null, r1);
        cursor.close();
    }
    public void firsttime_table_map(){
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("map", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if(cursor.getCount()<=0){
            insert_table_map("-L8Z2uRlg7Vu3u3Vk3lk","31.2569132","29.9999342","فطير مشلتت");
            insert_table_map("-L8n5PjEn9e5hy3Mha2l","29.9499136","30.9369838","قراقيش بعجوة");
            insert_table_map("-L9QGbmdD-f_MeYoVFuE","30.0875125","31.2806701","قرع عسلى بالكريم");
            insert_table_map("-L9VufvVq82M7LjB36Od","29.9728765","31.2608815","مكرونه باشميل");
            insert_table_map("-L9XxaOiQwP5i54bs6oh","29.9531099","31.0453551","بيتزا ");
            insert_table_map("-L9ZIMhN9U6zwb8vaUCY","30.7914017","30.9793722","محشي مشكل مع ملوخية و فراخ محمرة");
            insert_table_map("-L9_kkVmCVsPhmBXrUa9","30.082808","31.2589625","كبسة");
            insert_table_map("-LCMuYXQnabo1CXmgSLA","30.2475157","31.4900639","بيتزا");

        }
        cursor.close();
    }

}


