package com.student.tyro;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    //TextView text_location, text_currentlocation;
    ImageView pin_current_location;
    GoogleMap map;
    GPSTracker gpsTracker;
    Button bt_confirm_loc;
    String strAdd = "", strLatitude = "", strLongitude = "", city = "";
    String language = "";
    TextView search_location, text_location;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String User_id, flag;
    NetworkConnection networkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_map);
        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        User_id = sharedPreferences.getString("User_id", "");
        // Toast.makeText(getApplicationContext(),""+User_id,Toast.LENGTH_LONG).show();
        gpsTracker = new GPSTracker(LoginMapActivity.this);
        networkConnection = new NetworkConnection(LoginMapActivity.this);
      /*  preferenceUtils=new PreferenceUtils(LocationActivity.this);
        language=preferenceUtils.getLanguage();*/

        flag = getIntent().getStringExtra("flag");
        initilaizeUI();
        initializeValues();
    }

    private void initializeValues() {

        pin_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                getCompleteAddressString(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            }
        });

        bt_confirm_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkConnection.isConnectingToInternet()) {
//                                        Send_Location();
                    if (flag != null && flag.equals("1")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("location", search_location.getText().toString());
                        returnIntent.putExtra("strLatitude", strLatitude);
                        returnIntent.putExtra("strLongitude", strLongitude);
                        setResult(Activity.RESULT_OK, returnIntent);
                        LoginMapActivity.super.onBackPressed();
                    } else {
                        updatelocations();
                    }
                } else {
                    Toast.makeText(LoginMapActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }


            }
        });

        search_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initilizeValues();
            }
        });


    }

    private void initilaizeUI() {
        // image_back = (ImageView) findViewById(R.id.image_back);
        // text_location = (TextView) findViewById(R.id.text_location);
        pin_current_location = findViewById(R.id.pin_current_location);
        bt_confirm_loc = (Button) findViewById(R.id.bt_confirm_loc);
        search_location = findViewById(R.id.search_location);
        text_location = findViewById(R.id.text_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void initilizeValues() {

        Places.initialize(getApplicationContext(), "AIzaSyCziqe1Q-d4HMC3D9ZyYDFkBtx8ZHrzGzM");

        PlacesClient placesClient = Places.createClient(this);

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                // text_location.setText(place.getName());
                search_location.setText(place.getName());
                double lat = place.getLatLng().latitude;
                double lng = place.getLatLng().longitude;

                strLatitude = String.valueOf(lat);
                strLongitude = String.valueOf(lng);
                System.out.println("lattitude" + lat + "," + lng);


                Log.i("Tag", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("Tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        getCompleteAddressString(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        strLatitude = String.valueOf(gpsTracker.getLatitude());
        strLongitude = String.valueOf(gpsTracker.getLongitude());

        Log.e("safksdfds ", strLatitude);
        Log.e("safksdfds ", strLongitude);


        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.e("onCameraChange: ", "" + cameraPosition.target.latitude + " : " + cameraPosition.target.longitude);
                getCompleteAddressString(cameraPosition.target.latitude, cameraPosition.target.longitude);
                strLatitude = String.valueOf(cameraPosition.target.latitude);
                strLongitude = String.valueOf(cameraPosition.target.longitude);
            }
        });

    }


    private String getCompleteAddressString(double latitude, double longititude) {

        strAdd = "";
        search_location.setText("");
        Geocoder geocoder = new Geocoder(LoginMapActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longititude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                System.out.println("stradd" + strAdd);
               /* String     s_city = addresses.get(0).getLocality();
                Toast.makeText(Sp_location_activity.this,s_city,Toast.LENGTH_LONG).show();*/
                search_location.setText(strAdd);
                // search_location.setText(strAdd);
                if (addresses != null && addresses.size() > 0) {

                    city = addresses.get(0).getLocality();
                }

               /* Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);*/
                // city = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                // city = addresses.get(0).getLocality();
                if (strAdd.trim().isEmpty()) {
                    String address = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();

                    //     Toast.makeText(Sp_location_activity.this,"city:" + city,Toast.LENGTH_LONG).show();
                   /*  String state = addresses.get(0).getAdminArea();
                  String country = addresses.get(0).getCountryName();
                  String postalCode = addresses.get(0).getPostalCode();
                  String knownName = addresses.get(0).getFeatureName();*/

                    strAdd = address;
                    search_location.setText(strAdd);
                    System.out.println("Main_address" + strAdd);

                }
                Log.d("Current location add", "" + strReturnedAddress.toString());
            } else {
                Log.d("My Current location add",
                        "No Address returned!");
            }
        } catch (Exception e) {
            Log.e("getCompleteAddressSt: ", e.getMessage());
        }
        return strAdd;

    }

    private void Send_Location() {

        Log.i("User_id is", User_id);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.addAddress(User_id, search_location.getText().toString(), strLongitude, strLatitude);
        final KProgressHUD hud = KProgressHUD.create(LoginMapActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                hud.dismiss();

                if (response.isSuccessful()) {
                    try {
                        Log.e("jvcnxcvb ", response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {

                            String user_info = jsonObject.getString("Location_info");
                            JSONObject jsonObject1 = new JSONObject(user_info);
                            String user_id = jsonObject1.getString("user_id");
                            System.out.println("User_id" + user_id);
                            String firstname = jsonObject1.getString("firstname");
                            String lastname = jsonObject1.getString("lastname");
                            String email = jsonObject1.getString("email");
                            String phone = jsonObject1.getString("phone");
                            String password = jsonObject1.getString("password");
                            String unique_id = jsonObject1.getString("unique_id");
                            String auth_level = jsonObject1.getString("auth_level");
                            String profile_pic = jsonObject1.getString("profile_pic");
                            String device_name = jsonObject1.getString("device_name");
                            String device_token = jsonObject1.getString("device_token");
                            String role = jsonObject1.getString("role");
                            String type = jsonObject1.getString("type");
                            String user_status = jsonObject1.getString("user_status");
                            String location = jsonObject1.getString("location");
                            String latitude = jsonObject1.getString("latitude");
                            String longitude = jsonObject1.getString("longitude");
                            String reference_code = jsonObject1.getString("reference_code");
                            String licence = jsonObject1.getString("licence");
                            SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("lat", latitude);
                            editor.putString("long", longitude);
                            editor.putString("reference_code", reference_code);
                            editor.putString("location", location);
                            editor.putBoolean("loginstatus", true);
                            editor.commit();
                            editor.apply();
                            //  Toast.makeText(LoginMapActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(LoginMapActivity.this, UploadDocumentsActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        } else if (status.equals("0")) {
                            Toast.makeText(LoginMapActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        Log.e("shfdsfds", e.toString());
                    }
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Log.e("shdfdsf ", t.toString());

            }
        });

    }

    private void updatelocations() {

        Log.i("User_id is", User_id);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.Updatelocation_student(User_id, search_location.getText().toString(), strLongitude, strLatitude);
        final KProgressHUD hud = KProgressHUD.create(LoginMapActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                hud.dismiss();

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            Send_Location();
//                            ShowG2Dialog();

                        } else if (status.equals("0")) {

//                            final DialogInterface.OnClickListener dialogClickListener =
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            switch (which) {
//                                                case DialogInterface.BUTTON_POSITIVE:
//                                                    ShowG2Dialog();
//                                                    dialog.dismiss();
//                                                    break;
//                                            }
//                                        }
//                                    };
//                            AlertDialog dialog = new AlertDialog.Builder(LoginMapActivity.this)
//                                    .setPositiveButton("OK", dialogClickListener).create();
//                            dialog.setMessage(jsonObject.getString("message"));
//                            dialog.show();
                            Intent intent = new Intent(getApplicationContext(), InconvenienceActivity.class);
                            intent.putExtra("User_id", User_id);
                            startActivity(intent);

                            Toast.makeText(LoginMapActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        Log.e("shfdsfds", e.toString());
                    }
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Log.e("shdfdsf ", t.toString());

            }
        });

    }

    private void ShowG2Dialog() {
        final DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Send_Location();
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                ShowBDEDialog();
                                break;
                        }
                    }
                };
        AlertDialog dialog = new AlertDialog.Builder(LoginMapActivity.this)
                .setPositiveButton("Yes", dialogClickListener).
                        setNegativeButton("No", dialogClickListener).create();
        dialog.setMessage("Do you have g2?");
        dialog.show();
    }


    private void ShowBDEDialog() {
        final DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://demomaplebrains.com/tyro/"));
                                startActivity(browserIntent);
                                dialog.dismiss();
                                break;
                        }
                    }
                };
        AlertDialog dialog = new AlertDialog.Builder(LoginMapActivity.this)
                .setPositiveButton("Ok", dialogClickListener).create();
        dialog.setMessage("BDE Course");
        dialog.show();
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }
}