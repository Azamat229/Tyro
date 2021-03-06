package com.student.tyro;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Adapter.InstructorsAdapter;
import com.student.tyro.Adapter.InstructorsFilterAdapter;
import com.student.tyro.Model.Instructor;
import com.student.tyro.Model.Instructor_Filter;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorsActivity extends AppCompatActivity {

    RecyclerView recycle_instruct;
    NetworkConnection networkConnection;
    ImageView img_back;
    ArrayList<Instructor> instructorslist;
    ArrayList<Instructor> instructors_list_filter;
    ArrayList<Instructor_Filter> instructorsfilterlist;

    SearchView edit_search;
    InstructorsAdapter instructorsAdapter;
    InstructorsFilterAdapter instructorsfilterAdapter;
    String lattude,     longtude;
    String Rating, Distance;
    ImageView filter;
    int Price_range = 0;
    String Price_rangeString = "";
    String Max_range = "";
    LinearLayout linearnoinstruct;
    TextView tvnoinstruct;
    GPSTracker gpsTracker;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructors);
        recycle_instruct = findViewById(R.id.rvinstruct);
        img_back = findViewById(R.id.ivBack);
        filter = findViewById(R.id.filter);
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        lattude = sharedPreferences.getString("lat", "");
        longtude = sharedPreferences.getString("long", "");

        gpsTracker = new GPSTracker(InstructorsActivity.this);
        gpsTracker.getLocation();

        if (lattude.equals("") || longtude.equals("")) {
            lattude = String.valueOf(gpsTracker.getLatitude());
            longtude = String.valueOf(gpsTracker.getLongitude());
            Log.e("Lattude: ", lattude + " logtude: "+longtude);

        }
        networkConnection = new NetworkConnection(InstructorsActivity.this);
        linearnoinstruct = findViewById(R.id.linear_noinstructors);
        tvnoinstruct = findViewById(R.id.tv_noinstructors);
        if (networkConnection.isConnectingToInternet()) {
            getInstructors();
        } else {
            Toast.makeText(InstructorsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(InstructorsActivity.this, R.style.FilterAlertDialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_filter);
                //  dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();
                CheckBox checkbox1 = dialog.findViewById(R.id.checkBox1);
                CheckBox checkbox2 = dialog.findViewById(R.id.checkBox2);
                CheckBox checkbox3 = dialog.findViewById(R.id.checkBox3);
                CheckBox checkbox4 = dialog.findViewById(R.id.checkBox4);
                CheckBox checkbox5 = dialog.findViewById(R.id.checkBox_manual);
                CheckBox checkbox6 = dialog.findViewById(R.id.checkBox_automatic);
                RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
                Button apply = dialog.findViewById(R.id.btnapply);
                Button reset = dialog.findViewById(R.id.btnReset);
                ArrayList<Integer> ratingtList = new ArrayList<Integer>();
                ArrayList<String> garetypeList = new ArrayList<String>();
                SeekBar price_seekbar = dialog.findViewById(R.id.price_seekbar);
                TextView txt_max_value = dialog.findViewById(R.id.txt_max_value);
                price_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                        txt_max_value.setText("$" + progress + "/hr");
                        txt_max_value.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                        Price_range = progress;
                        Price_rangeString = "" + progress;
                        Log.i("Price_range is", "" + Price_range);
                        Max_range = "300";
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                //First CheckBox
                checkbox1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkbox1.isChecked()) {
                            ratingtList.add(4);
                            ratingtList.add(5);
                        } else {
                            ratingtList.remove(4);
                            ratingtList.remove(5);
                        }

                    }
                });

                //Second CheckBox
                checkbox2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkbox2.isChecked()) {
                            ratingtList.add(3);
                            ratingtList.add(4);
                        } else {
                            ratingtList.remove(3);
                            ratingtList.remove(4);
                        }

                    }
                });

                //Third CheckBox

                checkbox3.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkbox3.isChecked()) {
                            ratingtList.add(2);
                            ratingtList.add(3);
                        } else {
                            ratingtList.remove(2);
                            ratingtList.remove(3);
                        }

                    }
                });
                checkbox4.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkbox4.isChecked()) {

                            ratingtList.add(1);
                            ratingtList.add(2);
                        } else {
                            ratingtList.remove(1);
                            ratingtList.remove(2);
                        }

                    }
                });
                checkbox5.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkbox5.isChecked()) {

                            garetypeList.add("Manual");
                        } else {

                            garetypeList.remove("Manual");
                        }

                    }
                });
                checkbox6.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkbox6.isChecked()) {
                            garetypeList.add("Automatic");
                        } else {
                            garetypeList.remove("Automatic");
                        }

                    }
                });
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Log.i("Price is",""+);
                        Log.i("the array list is", "" + ratingtList.toString());
                        Log.i("the array list is", "" + garetypeList.toString());
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton genderradioButton = dialog.findViewById(selectedId);
                        Log.i("Radiobutton text", "" + genderradioButton.getText().toString());


                        instructors_list_filter = new ArrayList<>();
                        for (Instructor i : instructorslist) {
                            int counter1 = 0, counter2 = 0;
                            float ratting = Float.parseFloat(i.getInstruct_rate());

                            if (ratingtList.size() > 0) {
                                counter1 += 1;
                                if (ratting > Collections.min(ratingtList) && ratting < Collections.max(ratingtList)) {
                                    counter2 += 1;
                                }
                            }
                            if (garetypeList.size() > 0 && !garetypeList.isEmpty()) {
                                counter1 += 1;
                                if (garetypeList.contains(i.getInstruct_gear_type())) {
                                    counter2 += 1;
                                }
                            }
                            float price = Float.parseFloat(i.getInstruct_price());

                            if (Price_range != 0) {
                                counter1 += 1;
                                if (price <= Price_range) {
                                    Log.i("PRICE_", String.valueOf(price));
                                    counter2 += 1;
                                }
                            }
                            Log.i("COUNTERS", "1 :" + counter1 + " 2 :" + counter2);

                            if (counter1 == counter2) {
                                instructors_list_filter.add(i);
                            }
                        }
                        dialog.dismiss();

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(InstructorsActivity.this, 1);
                        recycle_instruct.setLayoutManager(gridLayoutManager);

                        instructorsAdapter = new InstructorsAdapter(InstructorsActivity.this, instructors_list_filter);
                        recycle_instruct.setAdapter(instructorsAdapter);
                        recycle_instruct.setVisibility(View.VISIBLE);
                        linearnoinstruct.setVisibility(View.GONE);

                    }

                });
                ImageView cls = dialog.findViewById(R.id.img_cls);
                cls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkbox1.setChecked(false);
                        checkbox2.setChecked(false);
                        checkbox3.setChecked(false);
                        checkbox4.setChecked(false);
                        checkbox5.setChecked(false);
                        checkbox6.setChecked(false);
                        garetypeList.clear();
                        ratingtList.clear();
                        getInstructors();
                        dialog.dismiss();
                    }
                });
            }
        });
        edit_search = findViewById(R.id.etSearch);
        edit_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filters(newText);
                return false;
            }
        });
    }

    private void filters(String text) {
        if (instructorslist != null && instructorslist.size() > 0) {
            ArrayList<Instructor> filterlist = new ArrayList<>();
            for (Instructor mainItemModel : instructorslist) {
                if (mainItemModel.getInstruct_firstname().toLowerCase().contains(text.toLowerCase())) {
                    filterlist.add(mainItemModel);
                    Log.i("filterlist", "" + filterlist);
                }
            }

            // instructorsAdapter.filterlist(filterlist);
            instructorsAdapter = new InstructorsAdapter(InstructorsActivity.this, filterlist);
            recycle_instruct.setAdapter(instructorsAdapter);
        }
    }

    private void getInstructors() {
        //  RequestBody r_userid = RequestBody.create(MediaType.parse("multipart/form-data"), User_id);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_instructorList(lattude, longtude);
        Log.e("latitude and longitude", lattude + " " + longtude);

//        Call<JsonElement> call = apiClass.get_instructorList("43.65322", "-79.3831843"); // Azamat temporaly hard code
        final KProgressHUD hud = KProgressHUD.create(InstructorsActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hud.dismiss();

                if (response.isSuccessful()) {
                    Log.e("spprofileee", response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String data = jsonObject.optString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            System.out.println("array is" + jsonArray);
                            instructorslist = new ArrayList<>();
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(InstructorsActivity.this, 1);
                            recycle_instruct.setLayoutManager(gridLayoutManager);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String langu_age = jsonObject1.getString("language");
                                String inst_id = jsonObject1.getString("user_id");
                                String inst_first_name = jsonObject1.getString("firstname");
                                String inst_img = jsonObject1.getString("profile_pic");
                                String inst_rating = jsonObject1.getString("rating");
                                // String inst_last_name = jsonObject1.getString("lastname");
                                String inst_distance = jsonObject1.getString("distance");
                                String inst_gear_type = jsonObject1.getString("gear_type");
                                String inst_price = jsonObject1.getString("price");

                                //String inst_lat = jsonObject1.getString("latitude");
                                //String inst_long = jsonObject1.getString("longitude");
                                // Rating = Integer.toString((int)inst_rating);
                                //  Distance = Integer.toString((int)inst_distance);
                                instructorslist.add(new Instructor(langu_age, inst_id, inst_first_name, inst_img, inst_rating, inst_distance, inst_gear_type, inst_price));
                                Log.i("the catagory data is", "" + instructorslist);
                            }
                            //recycle_instruct.setAdapter(new InstructorsAdapter(InstructorsActivity.this,instructorslist));
                            instructorsAdapter = new InstructorsAdapter(InstructorsActivity.this, instructorslist);
                            recycle_instruct.setAdapter(instructorsAdapter);
                            hud.dismiss();

                            recycle_instruct.setVisibility(View.VISIBLE);
                            linearnoinstruct.setVisibility(View.GONE);
                        } else if (status == 0) {
                            recycle_instruct.setVisibility(View.GONE);
                            linearnoinstruct.setVisibility(View.VISIBLE);
                            tvnoinstruct.setText("No Instructors available");
                        }

                    } catch (Exception e) {
                        hud.dismiss();
                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });
    }
}



