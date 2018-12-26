package xyz.topapproid.www.kitchen;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Globals{
    Context mContext;
    public Globals(Context context){
        this.mContext=context;
    }

    public void note(String des){
        final Intent emptyIntent= new Intent(mContext, Note.class);
      //  mContext.startActivity(emptyIntent);
        PendingIntent pendingIntent=PendingIntent.getActivity(mContext,1,
                emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri_sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder mBuilder=
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.notifications)
                        .setContentTitle("مطبخي")
                        .setContentText(des)
                        .setContentIntent(pendingIntent)
                        .setSound(uri_sound);
        try {

            NotificationManager notificationManager=(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(1,mBuilder.build());
            }

        }catch (Exception e){
            e.printStackTrace();
              }

    }

    public void update_value_id_1(String table,String field,String value){
        Sherifdb dbhandler = new Sherifdb(mContext);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query(table, null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues info = new ContentValues();
        info.put(field,value);
        db.update(table, info, "id=?", new String[]{String.valueOf(1)});
        cursor.close();
    }

    public String get_value_db(String table,String field){
        String value="";
        Sherifdb dbhandler = new Sherifdb(mContext);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query(table, null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex(field));
            cursor.close();
        }
        return value;
    }
}