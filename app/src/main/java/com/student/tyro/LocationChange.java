package com.student.tyro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocationChange extends AppCompatActivity implements OnMapReadyCallback {

    AutocompleteSupportFragment autocompleteFragment;
    LinearLayout ll_auto_fragment;
    ImageView img_search,img_curent_loc;
    GoogleMap googlemap;
    GPSTracker gpsTracker;
    TextView txt_address;
    public static double Flc_longitude, Flc_latitude;
    Button bt_confirm_loc;
    SupportMapFragment mapFragment;

    ImageView back_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationchange_map_actvity);


        ll_auto_fragment=findViewById(R.id.ll_auto_fragment);
        img_search=findViewById(R.id.img_search);
        txt_address=findViewById(R.id.txt_address);
        img_curent_loc=findViewById(R.id.img_curent_loc);
        bt_confirm_loc=findViewById(R.id.btn_confirm_loc);
        back_map=findViewById(R.id.back_icon);



        gpsTracker=new GPSTracker(LocationChange.this);

        Flc_latitude = gpsTracker.getLatitude();
        Flc_longitude = gpsTracker.getLongitude();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(LocationChange.this);


        bt_confirm_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_auto_fragment.setVisibility(View.VISIBLE);
                img_search.setVisibility(View.GONE);

                autocompleteFragment=(AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

                // Initialize Places.
                Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_api));
                // Create a new Places client instance.
                PlacesClient placesClient = Places.createClient(LocationChange.this);

                // Specify the types of place data to return.
                autocompleteFragment.setHint(getResources().getString(R.string.search_address_here));
                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

                // Set up a PlaceSelectionListener to handle the response.
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {

                        // TODO: Get info about the selected place.
                        Log.i("sdfg", "Place: " + place.getName() + ", " +  place.getLatLng().latitude);

                        Flc_latitude=place.getLatLng().latitude;
                        Flc_longitude=place.getLatLng().longitude;

                        String from_address= getCompleteAddressString(place.getLatLng().latitude,place.getLatLng().longitude);

                        googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16));

                        ll_auto_fragment.setVisibility(View.GONE);
                        img_search.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i("agfd", "An error occurred: " + status);
                    }
                });
            }
        });

        img_curent_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 16));

                    Flc_latitude = gpsTracker.getLatitude();
                    Flc_longitude = gpsTracker.getLongitude();

                    getCompleteAddressString(Flc_latitude, Flc_longitude);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap = googleMap;

        if (ActivityCompat.checkSelfPermission(LocationChange.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationChange.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googlemap.setMyLocationEnabled(false);
        googlemap.getUiSettings().setZoomControlsEnabled(false);
        googlemap.getUiSettings().setZoomGesturesEnabled(true);
        googlemap.getUiSettings().setCompassEnabled(false);
        googlemap.getUiSettings().setRotateGesturesEnabled(true);

        googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 16));


        googlemap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Flc_latitude = cameraPosition.target.latitude;
                Flc_longitude = cameraPosition.target.longitude;
                // googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Flc_latitude, Flc_longitude), 16));
                getCompleteAddressString(cameraPosition.target.latitude, cameraPosition.target.longitude);
            }
        });
    }

    public String getCompleteAddressString(Double LATITUDE, Double LONGITUDE) {
        String strAdd = "";
        txt_address.setText("");
        Geocoder geocoder = new Geocoder(LocationChange.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                txt_address.setText(strAdd);
                if (strAdd.trim().isEmpty()) {
                    String address = addresses.get(0).getAddressLine(0);
                    strAdd = address;
                    txt_address.setText(strAdd);
                }
                Log.d("Current location add", "" + strReturnedAddress.toString());
            } else {
                Log.d("My Current location add", "No Address returned!");
            }
        } catch (Exception e) {
            Log.e("getCompleteAddressSt: ", e.getMessage());
        }
        return strAdd;
    }

}