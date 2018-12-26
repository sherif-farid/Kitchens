package xyz.topapproid.www.kitchen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class View_mael extends AppCompatActivity {
TextView a,b,c,d,my_dis;
ImageView imageView;
Button call;
String phone="";
  //  private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_mael);

//        mAdView=findViewById(R.id.adView);
//        MobileAds.initialize(this,"ca-app-pub-2041092702369649~5014590282");
//        AdRequest adRequest=new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        a=findViewById(R.id.t2);
        b=findViewById(R.id.t4);
        c=findViewById(R.id.t6);
        d=findViewById(R.id.t8);
        imageView=findViewById(R.id.imgV);
        call=findViewById(R.id.btn_call);
        my_dis=findViewById(R.id.my_distance);
        if(getIntent().getExtras().getString("distance")!=null) {
            String dist = getIntent().getExtras().getString("distance");
            String total_dist="المسافة"+dist+"متر";
            my_dis.setText(total_dist);
        }

       final String random_key=getIntent().getExtras().getString("key");
       Toast.makeText(View_mael.this,"جاري تحميل البيانات ...",Toast.LENGTH_LONG).show();
      //  final String random_key=get_cur_id();
try {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference table_1 = database.getReference("table_1");
    table_1.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                String m_name = dataSnapshot.child(random_key).child("m_name").getValue().toString();
                String m_des = dataSnapshot.child(random_key).child("m_des").getValue().toString();
                String m_price = dataSnapshot.child(random_key).child("m_price").getValue().toString();
                String m_num = dataSnapshot.child(random_key).child("m_num").getValue().toString();
                phone = dataSnapshot.child(random_key).child("phone").getValue().toString();
                String st_image = dataSnapshot.child(random_key).child("img").getValue().toString();
                byte[] image = Base64.decode(st_image, Base64.DEFAULT);
                if(m_name.equals("")){m_name="هذه الوجبة غير متاحة الآن ربما تكون قد تم بيعها ";}
                a.setText(m_name);
                b.setText(m_des);
                c.setText(m_price);
                d.setText(m_num);

                if (image.length > 4) {
                    Bitmap new_bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    imageView.setImageBitmap(new_bitmap);
                }
            } else {
                Toast.makeText(View_mael.this, " not found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}catch (Exception e){
    e.printStackTrace();
    Toast.makeText(View_mael.this,"هذه الوجبة غير متاحة الآن",Toast.LENGTH_LONG).show();

}

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call=new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:"+phone));
                startActivity(call);
            }
        });

    }


}
