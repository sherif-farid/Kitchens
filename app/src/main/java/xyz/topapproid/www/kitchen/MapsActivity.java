package xyz.topapproid.www.kitchen;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.webkit.WebIconDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double  my_lat=0;
    double  my_lon=0;
    int km=1000;
  //  private AdView mAdView;
    String lat="";
    String lon="";
    String m_name="";
    String on="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        mAdView=findViewById(R.id.adView);
//        MobileAds.initialize(this,"ca-app-pub-2041092702369649~5014590282");
//        AdRequest adRequest=new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker from sqilte to my location
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor_table_id = db.query("table_id", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor_table_id.getCount() > 0) {
            cursor_table_id.moveToNext();
              my_lat = cursor_table_id.getDouble(cursor_table_id.getColumnIndex("lat"));
              my_lon = cursor_table_id.getDouble(cursor_table_id.getColumnIndex("lon"));

            LatLng  my_location=new LatLng(my_lat,my_lon);
           Marker my_marker= mMap.addMarker(new MarkerOptions().position(my_location).title("موقعك الحالي"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_location,15));

         //   mMap.addCircle(new CircleOptions().center(my_location).radius(km).strokeColor(Color.GREEN));

            cursor_table_id.close();
           my_marker.setTag("my_location");

        }
//add marker from sqilte

        Cursor cursor_map = db.query("map", null, null, null, null, null, null);

        for (int i=1;cursor_map.moveToNext();i++){

            String randomkey=get_table_map(i,"random_key");
            String lat=get_table_map(i,"lat");
            String lon=get_table_map(i,"lon");
            String m_name=get_table_map(i,"m_name");
            double other_lat = Double.valueOf(lat);
            double other_lon = Double.valueOf(lon);
            LatLng other_location = new LatLng(other_lat, other_lon);
            Marker other = mMap.addMarker(new MarkerOptions()
                    .position(other_location)
                    .title(m_name)
                    .snippet("لمزيد من التفاصيل إضفط هنا")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            other.setTag(randomkey);
            //  float[] distance=new float[2];Location.distanceBetween(my_lat,my_lon,other_lat,other_lon,distance);
        }
cursor_map.close();


        //update sqlite from firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_1 = database.getReference("table_1");

    table_1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot sh : dataSnapshot.getChildren()) {
                try{
                    if(sh.child("_lat1").getValue()!=null&&sh.child("_lon1").getValue()!=null
                            &&sh.child("m_name").getValue()!=null&&sh.child("on").getValue()!=null)
                    {
                        lat = sh.child("_lat1").getValue().toString();
                        lon = sh.child("_lon1").getValue().toString();
                        m_name = sh.child("m_name").getValue().toString();
                        on = sh.child("on").getValue().toString();
                    }

                // you can put this in if statement &&distance[0]<km
                if (lat.length() > 0 && lon.length() > 0 && m_name.length() > 0 && on.length() > 0) {
                        String random_key = sh.getKey();
                        update_table_map(random_key,lat,lon,m_name);

                        lat="";lon="";m_name="";on="";
                }

            }catch (Exception e){
        e.printStackTrace();

    }

            }



        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });






mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
    @Override
    public void onInfoWindowClick(Marker all_marker) {
        try {
            float[] distance = new float[2];
            Location.distanceBetween(my_lat, my_lon, all_marker.getPosition().latitude, all_marker.getPosition().longitude, distance);
            int mdistance = (int) distance[0];
            String my_distance = String.valueOf(mdistance);
            String marker_key = all_marker.getTag().toString();
            if (marker_key.equals("my_location")) {
                Toast.makeText(getBaseContext(), "موقعك الحالي", Toast.LENGTH_LONG).show();
            } else {
                Intent open = new Intent(MapsActivity.this, View_mael.class);
                open.putExtra("key", marker_key);
                open.putExtra("distance", my_distance);
                startActivity(open);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
});



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker all_marker) {
            all_marker.showInfoWindow();
                return true;
            }
        });



    }

    public String get_table_map(int id,String field){
        String value="";
        Sherifdb dbhandler = new Sherifdb(getBaseContext());
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("map", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex(field));
            cursor.close();
        }
        return value;
    }

    public void update_table_map(String random_key,String lat,String lon,String m_name){
    Sherifdb dbhandler = new Sherifdb(getBaseContext());
    SQLiteDatabase db = dbhandler.getWritableDatabase();
    Cursor cursor = db.query("map", null, "random_key=?", new String[]{random_key}, null, null, null);
    ContentValues r1 = new ContentValues();

    r1.put("random_key", random_key);
    r1.put("lat",lat);
    r1.put("lon", lon);
    r1.put("m_name", m_name);

    if(cursor.getCount()>0){
        db.update("map", r1, "random_key=?", new String[]{random_key});
    }else{
        db.insert("map", null, r1);
    }
    cursor.close();
}
}
