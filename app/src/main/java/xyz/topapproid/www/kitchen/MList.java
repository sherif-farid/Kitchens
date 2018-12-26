package xyz.topapproid.www.kitchen;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button edit;
    TextView mm1,mm2,mm3,mm4;
    ImageView maelphoto;
   // private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlist);
        Toolbar toolbar =  findViewById(R.id.toolbar);

//        mAdView=findViewById(R.id.adView);
//        MobileAds.initialize(this,"ca-app-pub-2041092702369649~5014590282");
//        AdRequest adRequest=new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        setSupportActionBar(toolbar);
        mm1=findViewById(R.id.textView4);
        mm2=findViewById(R.id.textView5);
        mm3=findViewById(R.id.textView7);
        mm4=findViewById(R.id.textView10);
        maelphoto=findViewById(R.id.i_photo);
        String test= get_value("table_mael","m_name");
        if(test.length()>0){

             show_dailog_toshare();
          }

        mm1.setText(get_value("table_mael","m_name"));
        mm2.setText(get_value("table_mael","m_des"));
        mm3.setText(get_value("table_mael","m_price"));
        mm4.setText(get_value("table_mael","m_num"));

        if(get_value("table_mael","m_photo").length()>0&&get_value("table_mael","m_photo")!=null){
            maelphoto.setImageBitmap(get_bitmap(get_value("table_mael","m_photo")));
        }

        edit=findViewById(R.id.button4);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent open=new Intent(MList.this,Myprofile.class);startActivity(open);
            }
        });


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);







    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent GO = new Intent(MList.this, Test_db.class);startActivity(GO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            String curent_id= get_value("table3","curent_id");
            enter_firebase(curent_id,"on","");

            set_value("table_mael","m_name","");
            set_value("table_mael","m_des","");
            set_value("table_mael","m_price","");
            set_value("table_mael","m_num","");
            set_value("table_mael","m_photo","");
            set_value("table3","curent_id","");
            Intent go = new Intent(MList.this,Test_db .class);
            startActivity(go);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent open=new Intent(MList.this,Account_info.class);startActivity(open);
        }else if (id == R.id.nav_slideshow) {
            Intent open=new Intent(MList.this,About.class);startActivity(open);
        }else if(id==R.id.nav_share){
            String app_url="https://play.google.com/store/apps/details?id=xyz.topapproid.www.kitchen";
            Intent share_app=new Intent(Intent.ACTION_SEND);
            share_app.setType("text/plain");
            share_app.putExtra(Intent.EXTRA_TEXT,app_url);
            startActivity(Intent.createChooser(share_app,"مشاركة علي "));
        }else if(id==R.id.nav_advance){
            Intent open=new Intent(MList.this,Advance.class);startActivity(open);
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public Bitmap get_bitmap(String img_source){

        byte[] s1 = Base64.decode(img_source, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(s1, 0, s1.length);

    }

    public void enter_firebase(String id, String field,String value){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference table_1=database.getReference("table_1");
        table_1.child(id).child(field).setValue(value);
    }
    public String get_value( String table,String field){
        String value="";
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query(table, null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex(field));
            cursor.close();
        }
        return value;
    }
    public void set_value(String table,String field,String value){
        Sherifdb dbhandler=new Sherifdb(getBaseContext());
        SQLiteDatabase db=dbhandler.getWritableDatabase();
        ContentValues info=new ContentValues();
        info.put(field,value);
        db.update(table,info,"id=?",new String []{String.valueOf(1)});
    }

    private void show_dailog_toshare() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("مبروك لقد أكملت إعداد الوجبة بنجاح !!  لزيادة مبيعاتك يرجي مشاركة التطبيق لتحصل علي زبائن أكثر سريعاً")
                .setCancelable(false)
                .setPositiveButton("موافق",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                String app_url="https://play.google.com/store/apps/details?id=xyz.topapproid.www.kitchen";
                                Intent share_app=new Intent(Intent.ACTION_SEND);
                                share_app.setType("text/plain");
                                share_app.putExtra(Intent.EXTRA_TEXT,app_url);
                                startActivity(Intent.createChooser(share_app,"مشاركة علي "));
                            }
                        });
        alertDialogBuilder.setNegativeButton("إلغاء",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }


}



