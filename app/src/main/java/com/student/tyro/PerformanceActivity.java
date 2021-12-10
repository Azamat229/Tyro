
package com.student.tyro;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.student.tyro.Adapter.ReportcardAdapter;
import com.student.tyro.Model.ReportcardModel;
import com.student.tyro.Model.Reportcardsublist;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.Util.MySingleton;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerformanceActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    private static final int DURATION = 5000;
    String lattude, longtude;
    TextView tv_class_details, tv_report_card;
    LinearLayout linear_class_details, liner_report_card;
    View view_class, view_report;
    LinearLayout linear_tab_report, linear_note;
    NetworkConnection networkConnection;
    ArrayList<ReportcardModel> reportcardlist;
    ArrayList<Reportcardsublist> reportsubcardlist;
    RecyclerView recycler_report;
    String instruct_id, student_id;
    RelativeLayout relatve_class_details;
    String name, pic, language, rating, location, time, date, hrs, startlat, startlong, endlat, endlong, traveld, speed, ttltme, amount;
    TextView tv_name, tv_location, tv_lang, tv_amt, tv_date, tv_tme, tv_hrs, tv_ttltravel, tv_speed, tv_ttltme, tv_rtng, tv_price;
    private static final int POLYLINE_WIDTH = 10;
    private Polyline polyline1;
    private Polyline polyline2;
    private Timer timer, timerDriver;
    private static final float ANCHOR_VALUE = 0.5f;
    private static final String TAG = PerformanceActivity.class.getSimpleName();

    private Marker driverMarker;
    private Marker pickupMarker;

    ArrayList markerPoints = new ArrayList();
    int rat;
    ImageView user_icon;
    RelativeLayout reltve_root;
    ImageView back;
    String cmpltd_id;
    AppCompatTextView note, taught_txt, done_well_txt, worked_on_txt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        networkConnection = new NetworkConnection(PerformanceActivity.this);
        view_class = findViewById(R.id.viewClassDetails);
        view_report = findViewById(R.id.viewReportCard);
        recycler_report = findViewById(R.id.report_cat_details);
        linear_class_details = findViewById(R.id.llTabClassDetails);
        liner_report_card = findViewById(R.id.llTabReportCard);
        tv_class_details = findViewById(R.id.tvClassDetails);
        tv_report_card = findViewById(R.id.tvReportCard);
        linear_tab_report = findViewById(R.id.lineartab_report);
        linear_note = findViewById(R.id.linear_note);
        relatve_class_details = findViewById(R.id.rlClassDetails);
        tv_name = findViewById(R.id.tv_instruct_name);
        tv_location = findViewById(R.id.tv_instruct_location);
        tv_lang = findViewById(R.id.tv_instruct_language);
        // tv_amt=findViewById(R.id.tv_instruct_language);
        tv_date = findViewById(R.id.tv_instruct_date);
        tv_tme = findViewById(R.id.tv_instruct_strtme);
        tv_hrs = findViewById(R.id.tv_instruct_hour);
        tv_ttltravel = findViewById(R.id.tv_ttl_time);
        tv_speed = findViewById(R.id.tv_ttl_speed);
        tv_ttltme = findViewById(R.id.tv_ttl_hrs);
        tv_rtng = findViewById(R.id.tv_instruct_rate);
        user_icon = findViewById(R.id.ivProfile);
        reltve_root = findViewById(R.id.rlRoot);
        back = findViewById(R.id.ivBack);
        tv_price = findViewById(R.id.instruct_price);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.show_map);
        note = findViewById(R.id.note);
        worked_on_txt = findViewById(R.id.worked_on_txt);
        taught_txt = findViewById(R.id.taught_txt);
        done_well_txt = findViewById(R.id.done_well_txt);


        SharedPreferences sharedPreferences = this.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        lattude = sharedPreferences.getString("lat", "");
        longtude = sharedPreferences.getString("long", "");
        System.out.println("sfdsfsf" + lattude + "\t" + longtude);

        student_id = getIntent().getStringExtra("studentid");
        instruct_id = getIntent().getStringExtra("instid");
        name = getIntent().getStringExtra("instname");
        pic = getIntent().getStringExtra("instprofilepic");
        language = getIntent().getStringExtra("instlang");
        rating = getIntent().getStringExtra("instrating");
        location = getIntent().getStringExtra("instlocaton");
        time = getIntent().getStringExtra("insttime") + "-" + getIntent().getStringExtra("endtime");
        date = getIntent().getStringExtra("instdate");
        hrs = getIntent().getStringExtra("insthrs");
        startlat = getIntent().getStringExtra("strt_lat");
        startlong = getIntent().getStringExtra("strt_long");
        endlat = getIntent().getStringExtra("end_lat");
        endlong = getIntent().getStringExtra("end_long");
        cmpltd_id = getIntent().getStringExtra("completed_id");
      /*  startlat="17.689345963037187";
        startlong="83.16824834793806";
        endlat="17.9693245";
        endlong="79.5908865";*/
        traveld = getIntent().getStringExtra("instttltraveled");
        speed = getIntent().getStringExtra("speed");
        ttltme = getIntent().getStringExtra("totaltime");
        amount = getIntent().getStringExtra("amount");
        System.out.println("latt" + startlat + "\t" + startlong + "\t" + endlat + "\t" + endlong);
        relatve_class_details.setVisibility(View.VISIBLE);
        tv_name.setText(name);
        tv_location.setText(location);
        tv_lang.setText(language);
        // tv_amt.setText(amount);
        tv_date.setText(date);
        tv_tme.setText(time);
        tv_hrs.setText(hrs + "hr");
        tv_ttltravel.setText(traveld);
        tv_speed.setText(speed + "km/hr");
        tv_ttltme.setText(ttltme + "hr");
        Picasso.get().load(Constants_Urls.pic_base_url + pic)
                .placeholder(R.drawable.user)
                .into(user_icon);
        // rat=Integer.parseInt(rating);
        //  rat= new Double(rating).intValue();
        //  Log.i("rating",""+rat);
        // String newValue = Integer.toString((int)rat);
        float f = Float.parseFloat(rating);
        System.out.println("float" + f);
        String mytext = Float.toString(f);
        System.out.println("after float" + mytext);
        tv_rtng.setText(mytext);
        tv_price.setText("$" + amount);
        view_class.setSelected(true);
        view_report.setSelected(false);
        tv_class_details.setTextColor(getResources().getColor(R.color.colorYellow));
        tv_report_card.setTextColor(getResources().getColor(R.color.colorWhite));
        Log.i("ids is", "" + student_id + instruct_id + cmpltd_id);
        linear_class_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_class.setSelected(true);
                view_report.setSelected(false);
                tv_class_details.setTextColor(getResources().getColor(R.color.colorYellow));
                tv_report_card.setTextColor(getResources().getColor(R.color.colorWhite));
                linear_tab_report.setVisibility(View.GONE);
                relatve_class_details.setVisibility(View.VISIBLE);

            }
        });

        liner_report_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_class.setSelected(false);
                view_report.setSelected(true);
                tv_class_details.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_report_card.setTextColor(getResources().getColor(R.color.colorYellow));
                linear_tab_report.setVisibility(View.VISIBLE);
                relatve_class_details.setVisibility(View.GONE);
                reltve_root.setBackgroundResource(R.drawable.bg_blue_black);
                if (networkConnection.isConnectingToInternet()) {
                    gerReportcard();
                } else {
                    Toast.makeText(PerformanceActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mapFragment.getMapAsync(this);
        setData();
    }

    private void setData() {

        timerDriver = new Timer();
        timerDriver.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                final LatLng pickupLatLng;
                pickupLatLng = new LatLng(Double.valueOf(startlat), Double.valueOf(startlong));
                LatLng driverLatLng = new LatLng(Double.valueOf(endlat), Double.valueOf(endlong));
               /* if (i == 0) {
                    i = 1;
                    driverLatLng = new LatLng(lat + 0.0002, lng + 0.0002);
                } else {
                    i = 0;
                    driverLatLng = new LatLng(lat - 0.0002, lng - 0.0002);
                }*/

                final LatLng finalDriverLatLng = driverLatLng;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   Toast.makeText(MainActivity.this, ""+lat, Toast.LENGTH_SHORT).show();
                        locUpdateServiceRequest();
                        if (pickupMarker == null) {
                            setPickup(pickupLatLng);
                            setDriver(finalDriverLatLng);
                        } else {
                            animateMarker(driverMarker, finalDriverLatLng, false);
                        }
                        setPath(pickupMarker, 1);
                    }
                });
            }

        }, 0, DURATION);


    }

    /**
     * set polyline path
     */
    private void setPath(final Marker marker, final int type) {
        LatLng origin = new LatLng(driverMarker.getPosition().latitude, driverMarker.getPosition().longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 13.5f));
        LatLng dest = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask(type);
        downloadTask.execute(url);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int mType;

        DownloadTask(final int type) {
            mType = type;
        }

        @Override
        protected String doInBackground(final String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask(mType);
            parserTask.execute(result);
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Projection proj = mMap.getProjection();
        final Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 3000;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                final long elapsed = SystemClock.uptimeMillis() - start;
                final float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                final double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                final double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;

                final LatLng driverPoint = new LatLng(lat, lng);

                Location startLocation = new Location("Start Location");
                startLocation.setLatitude(startLatLng.latitude);
                startLocation.setLongitude(startLatLng.longitude);

                Location endLocation = new Location("End Location");
                endLocation.setLatitude(toPosition.latitude);
                endLocation.setLongitude(toPosition.longitude);
                /* rotate the marker to the new latlng position */
                float bearing = startLocation.bearingTo(endLocation);

                /* Change the position of the marker instead of adding new marker
                 * in order to make a smooth marker movement */
                marker.setPosition(driverPoint);
                marker.setRotation(bearing);
                marker.setAnchor(ANCHOR_VALUE, ANCHOR_VALUE);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition.Builder().target(driverPoint).zoom(15.5f).build()));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 6);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }


    public void locUpdateServiceRequest() {
//        Log.e("locUpdateServ: ", strLat + "------" + strLong + "driver_id" + userInfo.getKeyID() + " ");
//        Log.e("locUpdategps ", gpsTracker.getLatitude() + "------" + gpsTracker.getLongitude() + "driver_id" + userInfo.getKeyID() + " ");


        //   Toast.makeText(getApplicationContext(), "gps_tracker_values"+ gpsTracker.getLatitude() + "------" + gpsTracker.getLongitude(),Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(), "gps_tracker_values"+ gpsTracker.getLatitude(),Toast.LENGTH_LONG).show();

        String tag_string_req = "req_location";
        // gpsTracker = new GPSTracker(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "https://sample.com.sa/api/UpdateLatLang", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Location Response: " + response.toString());
                String searchResponse = response.toString();
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    final int status = jsonObj.getInt("status");
                    String msg = jsonObj.getString("message");
                    if (status == 1) {
                        //  Toast.makeText(PerformanceActivity.this, "" + lat, Toast.LENGTH_SHORT).show();
                        Log.e("onResponse: ", searchResponse);
                        //  locUpdateServiceRequest();
                    } else {
                        Log.e("onResponse: ", searchResponse);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    //    toast("Json error: " + e.getMessage());
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                //     toast("Unknown Error occurred");
                //    progressDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();


                params.put("user_lat", String.valueOf(startlat));
                params.put("user_lng", String.valueOf(startlong));
                params.put("driver_id", "33");


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put("lang", LangCode);

                Log.d(TAG, "onCreateView:ffff " + LangCode);*/
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        //     AndroidLoginController.getInstance().addToRequestQueue(strReq, tag_string_req);
        MySingleton.getMinstance(this).addRequestQueuex(strReq);
    }

    private void setPickup(final LatLng mLatLng) {

        pickupMarker = mMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_endpoint_elipse)));
    }


    private void setDriver(final LatLng mLatLng) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        driverMarker = mMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_elipse))
        );


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        //  LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        double latitude = Double.parseDouble(startlat);
        double longitude = Double.parseDouble(startlong);
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        if (markerPoints.size() > 1) {
            markerPoints.clear();
            mMap.clear();
        }

        // Adding new item to the ArrayList
        markerPoints.add(sydney);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(sydney);

        if (markerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else if (markerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }

        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);
        Log.i("addMarker date", "" + markerPoints.size() + "/t");
        // Checks, whether start and end locations are captured

        double latitude1 = Double.parseDouble(endlat);
        double longitude1 = Double.parseDouble(endlong);

        LatLng sourcePosition = new LatLng(latitude, longitude);
        LatLng desti = new LatLng(latitude1, longitude1);
        Log.i("sourcePosition" + sourcePosition, "" + desti);
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(sourcePosition, desti);
        /*    DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);*/


    }

    private String getDirectionsUrl(final LatLng origin, final LatLng dest) {

        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "key=" + getString(R.string.google_maps_api);
        // Building the parameters to the web service
        String parameters = strOrigin + "&" + strDest + "&" + sensor + "&" + mode + "&" + key;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.e("skdj", url);
        return url;
    }


    private String downloadUrl(final String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove location updates to save battery.
        // stopLocationUpdates();
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        private int mType;

        ParserTask(final int type) {
            mType = type;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(final String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(final List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            if (result == null) {
                return;
            }
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);

                }
                List<PatternItem> pattern = Arrays.<PatternItem>asList(
                        new Gap(10), new Dash(20), new Gap(10));
                lineOptions.addAll(points);
                lineOptions.width(POLYLINE_WIDTH);
                lineOptions.color(Color.RED);
                // lineOptions.pattern(pattern);
                lineOptions.geodesic(true);
            }
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                switch (mType) {
                    case 1:
                        if (polyline1 != null) {
                            polyline1.remove();
                        }
                        polyline1 = mMap.addPolyline(lineOptions);
                        break;

                    case 2:
                        if (polyline2 != null) {
                            polyline2.remove();
                        }
                        polyline2 = mMap.addPolyline(lineOptions);
                        break;

                    default:
                }
            }
        }
    }

    private void gerReportcard() {

        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_rating_reportcard_student(student_id, instruct_id, cmpltd_id);
        final KProgressHUD hud = KProgressHUD.create(PerformanceActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hud.dismiss();
                if (response.isSuccessful()) {
                    Log.e("response is", response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String data = jsonObject.optString("category");
                            JSONArray jsonArray = new JSONArray(data);
                            System.out.println("array list1" + jsonArray);
                            reportcardlist = new ArrayList<>();
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(PerformanceActivity.this, 1);
                            recycler_report.setLayoutManager(gridLayoutManager);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String catgry = jsonObject1.getString("category");
                                Log.i("the catagory data is", "" + catgry);
                                String sub = jsonObject1.getString("subList");
                                Log.i("the subdatalist data is", "" + sub);
                                JSONArray jsonArray1 = new JSONArray(sub);
                                System.out.println("faq list" + jsonArray1);
                                if (jsonArray1 != null && jsonArray1.length() > 0) {
                                    linear_note.setVisibility(View.VISIBLE);
                                    note.setText(jsonArray1.getJSONObject(0).getString("note"));
                                    taught_txt.setText(jsonArray1.getJSONObject(0).getString("qns_1"));
                                    done_well_txt.setText(jsonArray1.getJSONObject(0).getString("qns_2"));
                                    worked_on_txt.setText(jsonArray1.getJSONObject(0).getString("qns_3"));
                                }
                                reportsubcardlist = new ArrayList<>();
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                    String repord_id = jsonObject2.getString("id");
                                    String repord_catid = jsonObject2.getString("cat_id");
                                    String report_name = jsonObject2.getString("report_name");
                                    String report_sts = jsonObject2.getString("status");
                                    String report_creatd = jsonObject2.getString("created_on");
                                    String report_rating = jsonObject2.getString("rating");
                                    reportsubcardlist.add(new Reportcardsublist(repord_id, repord_catid, report_name, report_sts, report_creatd, report_rating));
                                    //  Log.i("the  data is", "" + faqcatgories);

                                }
                                reportcardlist.add(new ReportcardModel(catgry, reportsubcardlist));
                            }
                            recycler_report.setAdapter(new ReportcardAdapter(PerformanceActivity.this, reportcardlist, reportsubcardlist));
                        }
                    } catch (Exception e) {
                        //hud.dismiss();
                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });
    }

}