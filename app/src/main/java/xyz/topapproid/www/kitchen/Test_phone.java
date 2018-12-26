package xyz.topapproid.www.kitchen;

import android.*;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Test_phone extends AppCompatActivity {
    EditText myphone;
    Button snd_code,check_code,chose;
    TextView a,b,c,d,f;
String code="";
String country_name="";
String country_code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_phone);
        myphone=findViewById(R.id.enter_phone);
        snd_code=findViewById(R.id.btn_snd_code);
        check_code=findViewById(R.id.btn_check_code);
        chose=findViewById(R.id.button7);
        a=findViewById(R.id.t1);
        b=findViewById(R.id.t2);
        c=findViewById(R.id.t3);
        d=findViewById(R.id.t4);
        f=findViewById(R.id.text_code);
        country_code=get_table_setting("code");
        country_name=get_table_setting("country");
        if(country_code.equals("")){f.setText(R.string.all);}
        f.setText(country_code);
        chose.setText(country_name);
        chose.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent go = new Intent(Test_phone.this, Country_codes.class);
        startActivity(go);
    }
});


        snd_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone=myphone.getText().toString();
                if(phone.isEmpty()) {
                    Toast.makeText(Test_phone.this, "يرجي إدخال رقم الهاتف بشكل صحيح", Toast.LENGTH_SHORT).show();
                }else if(country_code.equals("2")){
                    sms_result(phone);
                    if(code.equals("")) {
                        sendsms(phone);
                        }
                }else{
                    Intent go = new Intent(Test_phone.this, MainActivity.class);
                    go.putExtra("myphone", phone);
                    startActivity(go);
                }
            }
        });

        check_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone=myphone.getText().toString();

                if(phone.isEmpty()) {
                    Toast.makeText(Test_phone.this, "يرجي إدخال رقم الهاتف بشكل صحيح", Toast.LENGTH_SHORT).show();
                }else if(country_code.equals("2")){
                   sms_result(phone);
                }else{
                    Intent go = new Intent(Test_phone.this, MainActivity.class);
                    go.putExtra("myphone", phone);
                    startActivity(go);
                }



            }
        });


        verify();
    }



    public void sendsms(String phone)   {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED)
        {ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.SEND_SMS},1);
        }else {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, "2244", null, null);
            } catch (Exception ex) {
                String e = String.valueOf(ex);
            }
        }
    }

    public void sms_result(final String phone){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED)
        {ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.SEND_SMS},1);
        }else {

            try {
                Uri uri = Uri.parse("content://sms/inbox");
                String[] regcols = new String[]{"_id", "address", "body",};
                ContentResolver cr = getContentResolver();
                String phonenumber = "+"+country_code+ phone;
                Cursor cursor = cr.query(uri, regcols, "address=?", new String[]{phonenumber}, null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String strbody = cursor.getString(cursor.getColumnIndex("body"));
                        if (strbody.equals("2244")) {
                            code = "2244";
                            Toast.makeText(Test_phone.this, "تم تاكيد الهاتف", Toast.LENGTH_SHORT).show();
                            Intent go = new Intent(Test_phone.this, MainActivity.class);
                            go.putExtra("myphone", phone);
                            startActivity(go);
                            cursor.close();
                        }
                    }
                }


            } catch (Exception ex) {
                String none;
            }
        }

    }

    public void verify(){
        Sherifdb dbhandler=new Sherifdb(getBaseContext());
        SQLiteDatabase db=dbhandler.getWritableDatabase();
        Cursor allrwos=db.query("table3",null,"id=?",new String []{String.valueOf(1)},null,null,null);

        if(allrwos.getCount()>=0){
            allrwos.moveToNext();
            String session=allrwos.getString(allrwos.getColumnIndex("curent_id"));
            if(session.equals("")){
                Toast.makeText(Test_phone.this,"يرجي تسجيل الدخول للمتابعة", Toast.LENGTH_SHORT).show();
            }

            else{
                Intent profile=new Intent(Test_phone.this,MList.class);startActivity(profile);
            }
            allrwos.close();
        }


    }

    public String get_table_setting(String field){
        String value="";
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("settings", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex(field));
            cursor.close();
        }
        return value;
    }


}

