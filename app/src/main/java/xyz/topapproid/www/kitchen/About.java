package xyz.topapproid.www.kitchen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends AppCompatActivity {
    TextView txtV;
   // private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

//        mAdView=findViewById(R.id.adView);
//        MobileAds.initialize(this,"ca-app-pub-2041092702369649~5014590282");
//        AdRequest adRequest=new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        txtV=findViewById(R.id.textV13);

        txtV.setText(Html.fromHtml(getString(R.string.htm)));
        txtV.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
