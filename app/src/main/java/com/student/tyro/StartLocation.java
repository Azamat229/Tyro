/*
package com.student.tyro.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tyr.Models.UpcommingClassObj;
import com.example.tyr.R;
import com.example.tyr.sample.GMapV2Direction;
import com.example.tyr.sample.GMapV2DirectionAsyncTask;
import com.example.tyr.utils.ConstantFields;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

*/
/* created by prashanth valaboju 15-07-2021 *//*

public class StartLocation extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;
    private String user_address;

    //17.9693245,79.5908865  ..........
    private LatLng userLocation, sourcePosition;
    private Geocoder coder;
    private List<Address> address;

    TextView customer_name, customer_address, userlocation_txt, start_time, start_date, total_time_txt;
    CircleImageView user_profile;
    Button btnStartTrip;

    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    private LocationManager mLocManager;

    String starttrip_time;
    UpcommingClassObj coursedata;
    SharedPreferences sharedPreferences;

    KProgressHUD hud;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_location);
        getDirection = findViewById(R.id.btnGetDirection);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);

        hud = KProgressHUD.create(StartLocation.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();

        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapFragment.getMapAsync(this);


        //init widgets
        initwidgets();

        coursedata = (UpcommingClassObj) getIntent().getSerializableExtra("studentdata");
        final Calendar myCalendar = Calendar.getInstance();


        customer_name.setText(coursedata.getStudentName());
        customer_address.setText(coursedata.getLocation());
        userlocation_txt.setText(coursedata.getLocation());
        start_date.setText(coursedata.getBookingDate());
        start_time.setText(coursedata.getStartTime());
        total_time_txt.setText(coursedata.getBookingHours());

        user_address = coursedata.getLocation();

        Picasso.get().load(ConstantFields.pic_base_url + coursedata.getProfilePic())
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .into(user_profile);

        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starttrip_time = myCalendar.get(Calendar.HOUR_OF_DAY) + ":"
                        + myCalendar.get(Calendar.MINUTE);

                Intent intent = new Intent(getApplicationContext(), EndTripLocation.class);
                intent.putExtra("studentdata", coursedata);
                intent.putExtra("startlat", sourcePosition.latitude);
                intent.putExtra("startlong", sourcePosition.longitude);
                intent.putExtra("starttime", starttrip_time);
                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);


        if (user_address != null) {
            try {
                coder = new Geocoder(this);

                address = coder.getFromLocationName(coursedata.getLocation(), 1);

                if (address != null && address.size() > 0) {
                    Address location = address.get(0);
                    userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    System.out.println("userlocation" + location.getLatitude() + "," + location.getLongitude() + "\t"
                            + user_address);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        requestPermision();


        //27.658143,85.3199503
        //27.667491,85.3208583


    }

    private void initwidgets() {
        customer_name = findViewById(R.id.customer_name);
        customer_address = findViewById(R.id.customer_address);
        userlocation_txt = findViewById(R.id.userlocation);
        start_date = findViewById(R.id.start_date);
        start_time = findViewById(R.id.start_time);
        btnStartTrip = findViewById(R.id.btnStartTrip);
        user_profile = findViewById(R.id.user_profile);
        total_time_txt = findViewById(R.id.total_time);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        if (locationPermission) {
            getMyLocation();
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?"
                + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }


    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                }
                return;
            }
        }
    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                hud.dismiss();
                mMap.clear();
                sourcePosition = new LatLng(location.getLatitude(), location.getLongitude());
                System.out.println("current  " + userLocation + "\t" + sourcePosition);
                place1 = new MarkerOptions().position(sourcePosition)
                        .title(sharedPreferences.getString("firstname", "") + " " + sharedPreferences.getString("lastname", ""));

                place1.icon(BitmapDescriptorFactory.fromResource(R.drawable.navigation));
                place2 = new MarkerOptions().position(userLocation)
                        .title(customer_name.getText().toString());
                Log.d("mylog", "Added Markers");
                mMap.addMarker(place1);
                mMap.addMarker(place2);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place1.getPosition(), 16));

                String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
                new FetchURL(StartLocation.this).execute(url, "driving");
                route(sourcePosition, userLocation, GMapV2DirectionAsyncTask.MODE_DRIVING, mMap);

            }
        });
    }


    @SuppressLint("HandlerLeak")
    protected void route(LatLng sourcePosition, LatLng destPosition,
                         String mode, final GoogleMap mMap) {
        final Handler handler = new Handler() {
            @SuppressLint("UseCompatLoadingForDrawables")
            public void handleMessage(Message msg) {
                try {
                    Document doc = (Document) msg.obj;
                    GMapV2Direction md = new GMapV2Direction();
                    String duration = md.getDurationText(doc);
                    float distance = Float.parseFloat(md.getDistanceValue(doc) + "") / 1000;
                    System.out.println("start data distance  " + distance + "\t" + duration);

                    if (distance < 1) {
                        btnStartTrip.setEnabled(true);
                        btnStartTrip.setFocusable(true);
                        btnStartTrip.setBackground(getResources().getDrawable(R.drawable.drawable_yellow_cornered));
                    } else {
                        btnStartTrip.setEnabled(false);
                        btnStartTrip.setFocusable(false);
                        btnStartTrip.setBackground(getResources().getDrawable(R.drawable.drawable_grey_cornered));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, mode).execute();
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}*/
