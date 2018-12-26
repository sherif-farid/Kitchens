package xyz.topapproid.www.kitchen;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Country_codes extends AppCompatActivity {
Button a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20;
String country="";
String code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_codes);

        a1=findViewById(R.id.mauritania);
        a2=findViewById(R.id.morocco);
        a3=findViewById(R.id.algeria);
        a4=findViewById(R.id.tunisia);
        a5=findViewById(R.id.libya);
        a6=findViewById(R.id.egypt);
        a7=findViewById(R.id.sudan);
        a8=findViewById(R.id.saudi);
        a9=findViewById(R.id.yemen);
        a10=findViewById(R.id.oman);
        a11=findViewById(R.id.emarates);
        a12=findViewById(R.id.qatar);
        a13=findViewById(R.id.kuwait);
        a14=findViewById(R.id.bahrin);
        a15=findViewById(R.id.iraq);
        a16=findViewById(R.id.syria);
        a17=findViewById(R.id.palestine);
        a18=findViewById(R.id.jordon);
        a19=findViewById(R.id.lebanon);
        a20=findViewById(R.id.other);

    }

    public void code(View v){
switch (v.getId()) {
    case R.id.mauritania:
    country="موريتانيا";
    code="222";
    break;
    case R.id.morocco:
        country="المغرب";
        code="212";
        break;
    case R.id.algeria:
        country="الجزائر";
        code="213";
        break;
    case R.id.tunisia:
        country="تونس";
        code="216";
        break;
    case R.id.libya:
        country="ليبيا";
        code="218";
        break;
    case R.id.egypt:
        country="مصر";
        code="2";
        break;
    case R.id.sudan:
        country="السودان";
        code="249";
        break;
    case R.id.saudi:
        country="السعودية";
        code="966";
        break;
    case R.id.yemen:
        country="اليمن";
        code="967";
        break;
    case R.id.oman:
        country="عمان";
        code="968";
        break;
    case R.id.emarates:
        country="الإمارات ";
        code="971";
        break;
    case R.id.qatar:
        country="قطر";
        code="974";
        break;
    case R.id.kuwait:
        country="الكويت";
        code="965";
        break;
    case R.id.bahrin:
        country="البحرين";
        code="973";
        break;
    case R.id.iraq:
        country="العراق";
        code="964";
        break;
    case R.id.syria:
        country="سوريا";
        code="963";
        break;
    case R.id.palestine:
        country="فلسطين";
        code="970";
        break;
    case R.id.jordon:
        country="الأردن";
        code="962";
        break;
    case R.id.lebanon:
        country="لبنان";
        code="961";
        break;
    case R.id.other:
        country="دولة أخرى";
        code="";
        break;
             }
        set_table_settings("code",code);
        set_table_settings("country",country);
        Intent go = new Intent(Country_codes.this, Test_phone.class);
        startActivity(go);
    }

    public void set_table_settings(String field,String value) {
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("settings", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues info = new ContentValues();
        info.put(field, value);
        if (cursor.getCount() <= 0) {
            db.insert("settings", null, info);
        } else {
            db.update("settings", info, "id=?", new String[]{String.valueOf(1)});
        }
        cursor.close();
    }


}
