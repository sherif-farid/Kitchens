package xyz.topapproid.www.kitchen;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Sherifdb extends SQLiteOpenHelper {
    private final static String DB_NAME="SHDB";
    private final static int DB_VERSION=2;

    public Sherifdb(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE table_mael (id integer primary key autoincrement,m_name text,m_des text,m_price text,m_num text,m_photo text)");
        db.execSQL("CREATE TABLE table_id(id integer primary key autoincrement,lat double,lon double)");
        db.execSQL("CREATE TABLE table3(id integer primary key autoincrement,curent_id text)");
        db.execSQL("CREATE TABLE settings(id integer primary key autoincrement,name text,phone text,online text,code text,country text,t1 text" +
                ",t2 text,t3 text,t4 text,t5 text,t6 text,t7 text,t8 text,t9 text,t10 text)");
        db.execSQL("CREATE TABLE map(id integer primary key autoincrement,random_key text,lat text,lon text,m_name text)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion){

        if(oldversion==1&&newversion==2){
            db.execSQL("CREATE TABLE map(id integer primary key autoincrement,random_key text,lat text,lon text,m_name text)");

        }

    }


}

