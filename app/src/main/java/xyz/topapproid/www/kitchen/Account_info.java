package xyz.topapproid.www.kitchen;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Account_info extends AppCompatActivity {
TextView my_name,my_phone;
Switch mSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);
        my_name=findViewById(R.id.myname);
        my_phone=findViewById(R.id.myphone);
        mSwitch=findViewById(R.id.switch1);

        my_name.setText(get_value("settings","name"));
        my_phone.setText(get_value("settings","phone"));
        String online=get_value("settings","online");
        if(online.length()>0) {
            mSwitch.setChecked(true);
        }else{
            mSwitch.setChecked(false);
        }
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSwitch.isChecked()){
                    Toast.makeText(Account_info.this,"on", Toast.LENGTH_SHORT).show();
                    enter_firebase(get_value("table3","curent_id"),"on","1");
                    set_value("settings","online","1");
                }else{
                    Toast.makeText(Account_info.this,"off", Toast.LENGTH_SHORT).show();
                    enter_firebase(get_value("table3","curent_id"),"on","");
                    set_value("settings","online","");
                }

            }
        });




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

    public void enter_firebase(String id, String field,String value){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference table_1=database.getReference("table_1");
        table_1.child(id).child(field).setValue(value);
    }

    public void set_value(String table,String field,String value){
        Sherifdb dbhandler=new Sherifdb(getBaseContext());
        SQLiteDatabase db=dbhandler.getWritableDatabase();
        ContentValues info=new ContentValues();
        info.put(field,value);
        db.update(table,info,"id=?",new String []{String.valueOf(1)});
    }
}
