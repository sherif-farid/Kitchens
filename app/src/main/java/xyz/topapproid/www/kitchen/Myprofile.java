package xyz.topapproid.www.kitchen;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Myprofile extends AppCompatActivity {
    Button save_onfirebase,b_mael;
    EditText mael_name,mael_des,mael_price,mael_numofperson;
    TextView dir;
CheckBox Free;
    ImageView im_mael;

 //   private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

//        mAdView=findViewById(R.id.adView);
//
//        MobileAds.initialize(this,"ca-app-pub-2041092702369649~5014590282");
//        AdRequest adRequest=new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        mael_name=findViewById(R.id.meal_name);
        mael_des=findViewById(R.id.meal_des);
        mael_price=findViewById(R.id.meal_price);
        mael_numofperson=findViewById(R.id.maele_numoppersons);
        save_onfirebase=findViewById(R.id.bshow);
        dir=findViewById(R.id.dir_uri);
        im_mael=findViewById(R.id.im_maels);
        b_mael=findViewById(R.id.button5);
        Free=findViewById(R.id.free);

        save_onfirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m_name=mael_name.getText().toString();
                String m_des=mael_des.getText().toString();
                String m_price=mael_price.getText().toString();
                String m_num=mael_numofperson.getText().toString();
                if(m_name.isEmpty()||m_des.isEmpty()||m_price.isEmpty()||m_num.isEmpty()){
                    Toast.makeText(Myprofile.this, "يجب مليء كافة الحقول", Toast.LENGTH_SHORT).show();
                }else if(im_mael.getDrawable()==null){
                    Toast.makeText(Myprofile.this, "يجب إختيار صورة الوجبة", Toast.LENGTH_SHORT).show();

                } else{
                    String m_photo=get_table_mael("m_photo");
                    //add to sqilte
                    set_table_mael("m_name",m_name);
                    set_table_mael("m_des",m_des);
                    set_table_mael("m_price",m_price);
                    set_table_mael("m_num",m_num);

                    //add to fire base
                    String key=get_cur_id();

                    enter_firebase(key, "m_name",m_name);
                    enter_firebase(key, "m_des",m_des);
                    enter_firebase(key, "m_price",m_price);
                    enter_firebase(key, "m_num",m_num);
                    enter_firebase(key, "img",m_photo);

                    Intent open=new Intent(Myprofile.this,MList.class);

                    startActivity(open);
                }






            }
        });//end of button


Free.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(Free.isChecked()){
            mael_price.setEnabled(false);
            mael_price.setText("0");
        }else{
            mael_price.setEnabled(true);
            mael_price.setText("");
        }
    }
});

        b_mael.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intfoto_mael=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intfoto_mael,1);
            }
        });


        mael_name.setText(get_table_mael("m_name"));
        mael_des.setText(get_table_mael("m_des"));
        mael_price.setText(get_table_mael("m_price"));
        mael_numofperson.setText(get_table_mael("m_num"));
        if(get_table_mael("m_photo").length()>0){
            im_mael.setImageBitmap(get_bitmap(get_table_mael("m_photo")));

        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            Uri ri=data.getData();
            if(ri!=null) {

                try {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(ri);
                        Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);
                        float bitmap_height= bitmap1.getHeight();
                        float bitmap_width=bitmap1.getWidth();

                        float percent=bitmap_height/bitmap_width;


                        int uses_width;
                        if(bitmap_width>400){
                            uses_width=400;
                        }else{
                            uses_width=(int) bitmap_width;
                        }
                        double D=uses_width*percent;
                        int uses_height=(int)D;
                        Bitmap bitmap2=Bitmap.createScaledBitmap(bitmap1,uses_width,uses_height,true);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] img = bos.toByteArray();
                        if(img.length<1000000) {


                            String  st_img= Base64.encodeToString(img,Base64.DEFAULT);
                            set_table_mael("m_photo",st_img);

                            byte[] s1=Base64.decode(st_img,Base64.DEFAULT);
                            Bitmap new_bitmap = BitmapFactory.decodeByteArray(s1, 0, s1.length);
                            im_mael.setImageBitmap(new_bitmap);

                        }else{  Toast.makeText(Myprofile.this, "صورة كبيرة لا يمكن تحميلها" , Toast.LENGTH_SHORT).show();
                            int img_linght = img.length;
                            String st_byte = String.valueOf(img_linght);
                            dir.setText(st_byte);
                        }
                    } catch (FileNotFoundException e) {
                        String ex = String.valueOf(e);
                        Toast.makeText(Myprofile.this, "Exception /" + ex, Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    String ex = String.valueOf(e);
                    Toast.makeText(Myprofile.this, "Exception /" + ex, Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public String get_cur_id(){
        String cur_id="";
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor_table3 = db.query("table3", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor_table3.getCount() > 0) {
            cursor_table3.moveToNext();
            cur_id = cursor_table3.getString(cursor_table3.getColumnIndex("curent_id"));
            cursor_table3.close();
        }
        return cur_id;
    }

    public void set_table_mael(String field,String value){
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("table_mael", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues info = new ContentValues();
        info.put(field,value);
        if (cursor.getCount() <= 0) {
            db.insert("table_mael", null, info);
        }else{
            db.update("table_mael", info, "id=?", new String[]{String.valueOf(1)});
        }


        cursor.close();
    }
    public String get_table_mael(String field){
        String value="";
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("table_mael", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex(field));
            cursor.close();
        }
        return value;
    }

    public Bitmap get_bitmap(String img_source){

        byte[] s1 = Base64.decode(img_source, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(s1, 0, s1.length);

    }

    public void enter_firebase(String id, String field,String value){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference table_1=database.getReference("table_1");
        table_1.child(id).child(field).setValue(value);
    }



}


