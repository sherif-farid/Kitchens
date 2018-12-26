package xyz.topapproid.www.kitchen;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Calendar;

public class BackgroundService extends Service{
    public Context context=this;
    public Handler handler=null;
    public static Runnable runnable=null;
    Globals mGlobals;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    @Override
    public void onCreate(){
        handler=new Handler();
        mGlobals=new Globals(context);



        runnable=new Runnable() {
            @Override
            public void run() {

                int int_time= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int int_day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                 String time = String.valueOf(int_time);
                 String day = String.valueOf(int_day);
                 String start_day=mGlobals.get_value_db("settings","t2");
                String numberof_note_sent=mGlobals.get_value_db("settings","t3");


                if(!day.equals(start_day)){

                  if(time.equals("18")||time.equals("19")||time.equals("20")||time.equals("21")){
                      if(numberof_note_sent.equals("")) {
                          mGlobals.note("فريق خدمة عملاء مطبخي يرحب بكم .. في حالة وجود اي إستفسارات أو إقتراحات يرجي إرسالها عبر الإيميل التالي ");
                          mGlobals.update_value_id_1("settings","t2",day);
                      }else{
                          mGlobals.note("يوجد العديد من الوجبات حولك .. شاهدها الأن");
                          mGlobals.update_value_id_1("settings","t2",day);
                      }


                  }
              }
                handler.postDelayed(runnable,900000);
            }
        };
        handler.postDelayed(runnable,900000);
    }
    @Override
    public void onDestroy(){

    }
    @Override
    public void onStart(Intent intent,int startid){


    }

}