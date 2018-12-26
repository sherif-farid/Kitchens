package xyz.topapproid.www.kitchen;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class Test_db extends Activity {
    Button map,register,share,feedback;
    String date="";
  //  private InterstitialAd mInterstitialAd;
    int btn_request=0;
   // private AdView mAdView;
    Globals mGlobals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);
        mGlobals=new Globals(this);
        startService(new Intent(this,BackgroundService.class));

        date= DateFormat.getDateTimeInstance().format(new Date());

        int int_day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String day = String.valueOf(int_day);

        if(get_value_db("settings","t2").equals("")){
       mGlobals.update_value_id_1("settings","t2",day);
        }


        map=findViewById(R.id.bmap);
        share=findViewById(R.id.btn_share1);
        register=findViewById(R.id.breg);
        feedback=findViewById(R.id.btn_feedback);


//        mAdView=findViewById(R.id.adView);
//        MobileAds.initialize(this,"ca-app-pub-2041092702369649~5014590282");
//        AdRequest adRequest=new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-2041092702369649/2077874092");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                if(btn_request==1) {
//                    Intent gomap = new Intent(Test_db.this, MapsActivity.class);startActivity(gomap);
//                }else if(btn_request==2){
//                    Intent gotest_phone=new Intent(Test_db.this,Test_phone.class);startActivity(gotest_phone);
//
//                }
//            }
//
//        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_request=1;
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }else{
                    Intent gomap = new Intent(Test_db.this, MapsActivity.class);startActivity(gomap);
             //   }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_request=2;
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }else{
                    Intent gotest_phone=new Intent(Test_db.this,Test_phone.class);startActivity(gotest_phone);
         //       }

            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String app_url="https://play.google.com/store/apps/details?id=xyz.topapproid.www.kitchen";
                Intent share_app=new Intent(Intent.ACTION_SEND);
                share_app.setType("text/plain");
                share_app.putExtra(Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(share_app,"مشاركة علي "));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Intent gomap=new Intent(Test_db.this,Myrecycler1.class);startActivity(gomap);

                Intent s=new Intent(Intent.ACTION_SEND);
                s.setData(Uri.parse("mail to:"));
                String [] to={"topapproid.com@gmail.com"};
                s.putExtra(Intent.EXTRA_EMAIL,to);
                s.putExtra(Intent.EXTRA_SUBJECT,"العنوان");
                s.putExtra(Intent.EXTRA_TEXT,"يرجي كتابة شرح مفصل للمشكلة أو الإقتراحات مع تدعيمها بلقطة الشاشة إن أمكن ونعدكم بمراجعتها سريعاً");
                s.setType("message/rfc822");
               // Intent choose=Intent.createChooser(s,"send Email");
                startActivity(s);

            }
        });


if(get_value_db("table3","curent_id").equals("")) {
    addtofirebase();
        }else{
    enter_firebase(get_value_db("table3","curent_id"),"last_time",date);
}


    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed(){
        finishAffinity();
    }

public void addtofirebase(){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference table_1 = database.getReference("table_1");
    //table setting field t1 = temp key
        if(get_value_db("settings","t1").equals("")) {
            String temp_randomkey = table_1.push().getKey();
            set_temp_key(temp_randomkey);
            set_data_structure(temp_randomkey);
        }else if(get_value_db("table3","curent_id").equals("")){
            data_structure_2(get_value_db("settings","t1"));
        }
}




    public String get_value_db(String table,String field){
        String value="";
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor_table3 = db.query(table, null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor_table3.getCount() > 0) {
            cursor_table3.moveToNext();
            value = cursor_table3.getString(cursor_table3.getColumnIndex(field));
            cursor_table3.close();
        }
        return value;
    }

    public void set_temp_key(String value){
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("settings", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues info = new ContentValues();
        info.put("t1",value);
        if (cursor.getCount() <= 0) {
            db.insert("settings", null, info);
        }else{
            db.update("settings", info, "id=?", new String[]{String.valueOf(1)});
        }


        cursor.close();
    }

    public void enter_firebase(String id, String field,String value){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference table_1=database.getReference("table_1");
        table_1.child(id).child(field).setValue(value);
    }

    public void set_data_structure(String id){
        String lat=String.valueOf(get_lat());
        String lon=String.valueOf(get_lon());
        enter_firebase(id,"_lat1",lat);
        enter_firebase(id,"_lon1",lon);
        enter_firebase(id,"img","");
        enter_firebase(id,"m_name","");
        enter_firebase(id,"m_des","");
        enter_firebase(id,"m_price","");
        enter_firebase(id,"m_num","");
        enter_firebase(id,"name","");
        enter_firebase(id,"on","");
        enter_firebase(id,"phone","");
        enter_firebase(id,"del","");
        enter_firebase(id,"first_time",date);
        enter_firebase(id,"last_time",date);
    }

    public void data_structure_2(String id){
        String lat=String.valueOf(get_lat());
        String lon=String.valueOf(get_lon());
        enter_firebase(id,"_lat1",lat);
        enter_firebase(id,"_lon1",lon);
        enter_firebase(id,"last_time",date);
        enter_firebase(id,"img","");
        enter_firebase(id,"m_name","");
        enter_firebase(id,"m_des","");
        enter_firebase(id,"m_price","");
        enter_firebase(id,"m_num","");
        enter_firebase(id,"name","");
        enter_firebase(id,"on","");
        enter_firebase(id,"phone","");
        enter_firebase(id,"del","");
        enter_firebase(id,"last_time",date);
    }
    public double get_lat() {
        double  cur_lat=0;
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor_table_id = db.query("table_id", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor_table_id.getCount() > 0) {
            cursor_table_id.moveToNext();
            cur_lat = cursor_table_id.getDouble(cursor_table_id.getColumnIndex("lat"));
        }
        cursor_table_id.close();
        return cur_lat;
    }

    public double get_lon() {
        double  cur_lon=0;
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor_table_id = db.query("table_id", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor_table_id.getCount() > 0) {
            cursor_table_id.moveToNext();
            cur_lon = cursor_table_id.getDouble(cursor_table_id.getColumnIndex("lon"));
        }
        cursor_table_id.close();
        return cur_lon;
    }



}



