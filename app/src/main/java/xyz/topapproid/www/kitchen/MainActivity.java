package xyz.topapproid.www.kitchen;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    Button b2,accept_btn;
    EditText C;
    TextView A2;
    CheckBox loc_box ;
    String rundom_key="";
    ProgressBar progressBar;
    CountDownTimer mycountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loc_box=findViewById(R.id.checkBox);

        b2=findViewById(R.id.signup);

        A2=findViewById(R.id.phone_sign);

        C=findViewById(R.id.name_sign);
        progressBar=findViewById(R.id.progressBar4);
        accept_btn=findViewById(R.id.btn_accept22);
        accept_btn.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View view) {
        Intent profile = new Intent(MainActivity.this, Termsofuse.class);startActivity(profile);
                }
         });

        A2.setText(getIntent().getExtras().getString("myphone"));



        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean box = loc_box.isChecked();

                String phone = A2.getText().toString();

                String name = C.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(MainActivity.this, " يجب كتابة الإسم", Toast.LENGTH_SHORT).show();
                }else if(!box){
                    Toast.makeText(MainActivity.this, " يجب الموافقة علي تحديد الموقع", Toast.LENGTH_SHORT).show();
                }
                else  if (isnetworkavailable()) {
                    showdialog();
                    progressBar.setVisibility(View.VISIBLE);
                    query(phone, name);
                    timer();
                } else {
                    Toast.makeText(MainActivity.this, " تحقق من إتصال الانترنت", Toast.LENGTH_SHORT).show();
                }


            }
        });// end of button b2

        verify();
    }





    public void verify(){
        Sherifdb dbhandler=new Sherifdb(getBaseContext());
        SQLiteDatabase db=dbhandler.getWritableDatabase();
        Cursor allrwos=db.query("table3",null,"id=?",new String []{String.valueOf(1)},null,null,null);

        if(allrwos.getCount()>=0){
            allrwos.moveToNext();
            String session=allrwos.getString(allrwos.getColumnIndex("curent_id"));
            if(session.equals("")){
                Toast.makeText(MainActivity.this,"يرجي تسجيل الدخول للمتابعة", Toast.LENGTH_SHORT).show();
            }

            else{
                Intent profile=new Intent(MainActivity.this,MList.class);startActivity(profile);
            }
            allrwos.close();
        }


    }
    @Override
    public void onBackPressed(){
        Intent GO = new Intent(MainActivity.this, Test_db.class);
        startActivity(GO);
    }
    public void update_table3(String st){
        Sherifdb dbhandler=new Sherifdb(getBaseContext());
        SQLiteDatabase db=dbhandler.getWritableDatabase();
        ContentValues info=new ContentValues();
        info.put("curent_id",st);

        db.update("table3",info,"id=?",new String []{String.valueOf(1)});
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
    public void enter_firebase(String id, String field,String value){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference table_1=database.getReference("table_1");
        table_1.child(id).child(field).setValue(value);
    }
    private boolean isnetworkavailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activityNetworKInfo=connectivityManager.getActiveNetworkInfo();
        return activityNetworKInfo !=null&&activityNetworKInfo.isConnected();
    }
    public void query(final String phone, final String name){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_1 = database.getReference("table_1");
        final Query query = table_1.orderByChild("phone").equalTo(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot k1 : dataSnapshot.getChildren()) {
                        rundom_key = k1.getKey();
                        update_table3(rundom_key);
                        update_settings(name,phone);
                        enter_firebase(rundom_key, "name",name);
                        enter_firebase(rundom_key, "m_name","");
                        enter_firebase(rundom_key, "m_des","");
                        enter_firebase(rundom_key, "m_price","");
                        enter_firebase(rundom_key, "m_num","");
                        enter_firebase(rundom_key, "img","");
                        enter_firebase(rundom_key, "on","1");
                        String lat=String.valueOf(get_lat());
                        String lon=String.valueOf(get_lon());
                        enter_firebase(rundom_key, "_lat1",lat);
                        enter_firebase(rundom_key, "_lon1",lon);
                        if(!rundom_key.equals(get_value_db("settings", "t1"))){
                            enter_firebase(get_value_db("settings","t1"), "del","deleted");
                            set_temp_key(rundom_key);
                        }
                        Toast.makeText(MainActivity.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                        Intent profile = new Intent(MainActivity.this, MList.class);startActivity(profile);
                    }

                }else{
                    //table setting field t1 = temp key
                   String id= get_value_db("settings","t1");
                    enter_firebase(id, "name",name);
                    enter_firebase(id, "phone",phone);
                    enter_firebase(id, "on","1");
                    update_table3(id);
                    update_settings(name,phone);
                    Toast.makeText(MainActivity.this,"تم تسجيل الدخول بنجاح" , Toast.LENGTH_SHORT).show();
                    Intent profile = new Intent(MainActivity.this, MList.class);startActivity(profile);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void update_settings(String name,String phone){
        Sherifdb dbhandler=new Sherifdb(getBaseContext());
        SQLiteDatabase db=dbhandler.getWritableDatabase();
        ContentValues info=new ContentValues();
        info.put("name",name);
        info.put("phone",phone);
        info.put("online","1");
        db.update("settings",info,"id=?",new String []{String.valueOf(1)});
    }

    private void showdialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialogBuilder.setView(R.layout.progress_layout);
        }
        */
    }

    private void timer(){
    mycountdown = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
if(this.getClass().getSimpleName().equals("MainActivity.java")) {
    Toast.makeText(MainActivity.this, " تحقق من إتصال الانترنت", Toast.LENGTH_SHORT).show();
    Intent go = new Intent(MainActivity.this, Test_db.class);
    startActivity(go);
}
        }
    }.start();

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


}




