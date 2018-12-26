package xyz.topapproid.www.kitchen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class Note extends AppCompatActivity {
ImageView imageView;
TextView textView;
Globals mGlobals;
Button mainbtn;
   // private InterstitialAd mInterstitialAd;
   // private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);

//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-2041092702369649/2030985057");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                startActivity(new Intent(getBaseContext(),Test_db.class));
//
//            }
//
//        });
//
//        mAdView=findViewById(R.id.adView);
//        MobileAds.initialize(this,"ca-app-pub-2041092702369649~5014590282");
//        AdRequest adRequest=new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        imageView=findViewById(R.id.imagenote);
        textView=findViewById(R.id.textnote);
        mainbtn=findViewById(R.id.btn_main);
        mGlobals=new Globals(this);

        show_notifications();
        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(getBaseContext(), Test_db.class));


            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        }else {
            startActivity(new Intent(getBaseContext(), Test_db.class));
     //   }
    }



    public void show_notifications(){
        if(mGlobals.get_value_db("settings","t3").equals("")){
            imageView.setImageResource(R.drawable.cservice);
            textView.setText("لاي استفسارات او اقتراحات يرجي الذهاب للصفحة الرئيسية والضغط علي الإبلاغ عن مشكلة وارسال كل الملاحظات عبر الايميل وسوف يتم الرد عليها فوراً فقد أصبح لدينا العديد من موظفين خدمة العملاء للرد علي كل الإستفسارات في أسرع وقت ... ونتمني أن تحظي بتجربة ممتعة");
            mGlobals.update_value_id_1("settings","t3","1");
        }else if(mGlobals.get_value_db("settings","t3").equals("1")){
            imageView.setImageResource(R.drawable.meals);
            textView.setText("يومياً يتم إضافة وتحديث العديد من الوجبات من كل المستخدمين .. لمشاهده كل الوجبات حولك يرجي الذهاب الي الخريطة ومشاهدة كل الوجبات القريبة ");
            mGlobals.update_value_id_1("settings","t3","2");
        }
    }
}
