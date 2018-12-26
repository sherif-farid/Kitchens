package xyz.topapproid.www.kitchen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class Advance extends AppCompatActivity {
TextView a,b,c,d;
Button sharebtn;
  //  private AdView mAdView;
    //  private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advance);
//        mAdView=findViewById(R.id.adView);
//        MobileAds.initialize(this,"ca-app-pub-2041092702369649~5014590282");
//        AdRequest adRequest=new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        a=findViewById(R.id.a1);
        b=findViewById(R.id.a2);
        c=findViewById(R.id.a3);
        d=findViewById(R.id.a4);
        sharebtn=findViewById(R.id.btn_share);

        String aa1="نصائح هامة لزيادة مبيعاتك";
        String aa2="يمكنك زيادة المبيعات عن طريق أضافة وجبة جديدة يومياً مع مراعاه أن تكون صورة الوجبة جذابة وتظهر تفاصيل الوجبة بشكل جيد ";
        String aa3="لن تتم المبعيات حتي تقوم بمشاركة التطبيق حتي يعلم من حولك بأنك تستخدم التطبيق وتظهر وجباتك لمن حولك ";
        String aa4="يمكنك مشاركة التطبيق عن طريق الفيسبوك أو واتساب وسوف تجد فرق ملحوظ في معدل المبيعات والتفاعل علي الوجبة   ";
   a.setText(aa1);
   b.setText(aa2);
   c.setText(aa3);
   d.setText(aa4);

   sharebtn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           String app_url="https://play.google.com/store/apps/details?id=xyz.topapproid.www.kitchen";
           Intent share_app=new Intent(Intent.ACTION_SEND);
           share_app.setType("text/plain");
           share_app.putExtra(Intent.EXTRA_TEXT,app_url);
           startActivity(Intent.createChooser(share_app,"مشاركة علي "));
       }
   });

    }
}
