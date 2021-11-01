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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorsActivity extends AppCompatActivity {

    RecyclerView recycle_instruct;
    NetworkConnection networkConnection;
    ImageView img_back;
    ArrayList<Instructor> instructorslist;
    ArrayList<Instructor_Filter> instructorsfilterlist;
    SearchView edit_search;
    InstructorsAdapter instructorsAdapter;
    InstructorsFilterAdapter instructorsfilterAdapter;
    String lattude, longtude;
    String Rating, Distance;
    ImageView filter;
    String Price_range = "", Max_range = "";
    LinearLayout linearnoinstruct;
    TextView tvnoinstruct;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructors);
        recycle_instruct = findViewById(R.id.rvinstruct);
        img_back = findViewById(R.id.ivBack);
        filter = findViewById(R.id.filter);
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        lattude = sharedPreferences.getString("lat", "");
        longtude = sharedPreferences.getString("long", "");
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
                ArrayList<String> ratingtList = new ArrayList<String>();
                ArrayList<String> garetypeList = new ArrayList<String>();
                SeekBar price_seekbar = dialog.findViewById(R.id.price_seekbar);
                TextView txt_max_value = dialog.findViewById(R.id.txt_max_value);
                price_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                        txt_max_value.setText("$" + progress + "/hr");
                        txt_max_value.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                        Price_range = "" + progress;
                        Log.i("Price_range is", "" + Price_range);
                        Max_range = "120";
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
                            ratingtList.add("4");
                            ratingtList.add("5");
                        } else {
                            ratingtList.remove("4");
                            ratingtList.remove("5");
                        }

                    }
                });

                //Second CheckBox
                checkbox2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkbox2.isChecked()) {
                            ratingtList.add("3");
                            ratingtList.add("4");
                        } else {
                            ratingtList.remove("3");
                            ratingtList.remove("4");
                        }

                    }
                });

                //Third CheckBox

                checkbox3.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkbox3.isChecked()) {
                            ratingtList.add("2");
                            ratingtList.add("3");
                        } else {
                            ratingtList.remove("2");
                            ratingtList.remove("3");
                        }

                    }
                });
                checkbox4.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkbox4.isChecked()) {

                            ratingtList.add("1");
                            ratingtList.add("2");
                        } else {
                            ratingtList.remove("1");
                            ratingtList.remove("2");
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

                        StringBuilder sb = new StringBuilder(ratingtList.size());
                        ArrayList ratingvalues = new ArrayList();
                        if (ratingtList != null && ratingtList.size() > 0) {
                            for (int i = 0; i < ratingtList.size(); i++) {
                                ratingvalues.add(ratingtList.get(i));
                            }
                            System.out.println("post rating data " + ratingvalues);
                        }
                        if (networkConnection.isConnectingToInternet()) {
                            ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
                            Call<JsonElement> call = apiClass.get_filters
                                    (genderradioButton.getText().toString(),
                                            garetypeList, Price_range, ratingvalues, lattude, longtude);
                            final KProgressHUD hud = KProgressHUD.create(InstructorsActivity.this)
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
                                            int status = jsonObject.getInt("status");
                                            if (status == 1) {
                                                String data = jsonObject.optString("data");
                                                JSONArray jsonArray = new JSONArray(data);
                                                System.out.println("array is" + jsonArray);
                                                instructorsfilterlist = new ArrayList<>();
                                                GridLayoutManager gridLayoutManager = new GridLayoutManager(InstructorsActivity.this, 1);
                                                recycle_instruct.setLayoutManager(gridLayoutManager);
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    String langu_age = jsonObject1.getString("language");
                                                    String inst_id = jsonObject1.getString("user_id");
                                                    String inst_first_name = jsonObject1.getString("firstname");
                                                    String inst_img = jsonObject1.getString("profile_pic");
                                                    String inst_prce = jsonObject1.getString("price");
                                                    String gender = jsonObject1.getString("gender");
                                                    String rating = jsonObject1.getString("rating");
                                                    String inst_distance = jsonObject1.getString("distance");
                                                    //String inst_lat = jsonObject1.getString("latitude");
                                                    //String inst_long = jsonObject1.getString("longitude");
                                                    // Rating = Integer.toString((int)rating);
                                                    // Distance = Integer.toString((int)inst_distance);
                                                    instructorsfilterlist.add(new Instructor_Filter(langu_age, inst_id, inst_first_name, inst_img, inst_prce, gender, rating, inst_distance));
                                                    Log.i("the catagory data is", "" + instructorsfilterlist);
                                                }
                                                //recycle_instruct.setAdapter(new InstructorsAdapter(InstructorsActivity.this,instructorslist));
                                                instructorsfilterAdapter = new InstructorsFilterAdapter(InstructorsActivity.this, instructorsfilterlist);
                                                recycle_instruct.setAdapter(instructorsfilterAdapter);
                                                hud.dismiss();
                                                dialog.dismiss();
                                            }
                                        } catch (Exception e) {
                                            hud.dismiss();
                                            dialog.dismiss();
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
                        } else {
                            Toast.makeText(InstructorsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                        }
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
                                //String inst_lat = jsonObject1.getString("latitude");
                                //String inst_long = jsonObject1.getString("longitude");
                                // Rating = Integer.toString((int)inst_rating);
                                //  Distance = Integer.toString((int)inst_distance);
                                instructorslist.add(new Instructor(langu_age, inst_id, inst_first_name, inst_img, inst_rating, inst_distance));
                                Log.i("the catagory data is", "" + instructorslist);
                            }
                            //recycle_instruct.setAdapter(new InstructorsAdapter(InstructorsActivity.this,instructorslist));
                            instructorsAdapter = new InstructorsAdapter(InstructorsActivity.this, instructorslist);
                            recycle_instruct.setAdapter(instructorsAdapter);
                            hud.dismiss();

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



