package com.student.tyro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Adapter.RulesAdapter;
import com.student.tyro.Adapter.ViewPagerAdapter;
import com.student.tyro.Model.Covid_Rules;
import com.student.tyro.Model.SliderUtils;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmBookingActivity extends AppCompatActivity {
    NetworkConnection networkConnection;
    ArrayList<Covid_Rules> rules;
    TextView txtname, txtlanguage, txtrating, txtlocation, txtdt, txttime, txthr, student_pickup_location;
    ImageView img_instruct;
    String id, name, lang, rating, loc, date, start_time, end_time, hr, img, longt, lat, studentid, price, student_lat, student_lng,
            pi_location;
    List<SliderUtils> sliderImg;
    private int dotscount;
    private ImageView[] dots;
    private static int currentPage = 0;
    ViewPagerAdapter viewPagerAdapter;
    public ViewPager viewPager;
    public static LinearLayout sliderDotspanel;
    CheckBox check;
    String sp_agree = "";
    Button confrm_booking;
    TextView tvprice;
    AppCompatTextView txt_terms;
    ImageView back, edit_location;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        txtname = findViewById(R.id.instruct_name);
        txtlanguage = findViewById(R.id.instruct_lang);
        txtrating = findViewById(R.id.instruct_rate);
        txtlocation = findViewById(R.id.instruct_location);
        txtdt = findViewById(R.id.instruct_date);
        txttime = findViewById(R.id.instruct_time);
        txthr = findViewById(R.id.instruct_hr);
        img_instruct = findViewById(R.id.instruct_icon);
        viewPager = findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        confrm_booking = findViewById(R.id.btnConfirmBooking);
        tvprice = findViewById(R.id.instruct_price);
        check = findViewById(R.id.check);
        back = findViewById(R.id.ivBack);
        edit_location = findViewById(R.id.edit_location);
        tabLayout = findViewById(R.id.tabLayout);
        txt_terms = findViewById(R.id.txt_terms);
        student_pickup_location = findViewById(R.id.student_pickup_location);
        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        student_lat = sharedPreferences.getString("lat", "");
        student_lng = sharedPreferences.getString("long", "");
        pi_location = sharedPreferences.getString("location", "");
        student_pickup_location.setText(sharedPreferences.getString("location", ""));

        String text1 = " <font color=#ffffff><u>" + getResources().getString(R.string.i_agree_the_terms_and_conditions) + "</u></font>";
        txt_terms.setText(Html.fromHtml(text1));

        viewPager.setClipToPadding(false);
        tabLayout.setupWithViewPager(viewPager, true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginMapActivity.class);
                intent.putExtra("flag", "1");
                startActivityForResult(intent, 1);
            }
        });

        networkConnection = new NetworkConnection(ConfirmBookingActivity.this);


        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkConnection.isConnectingToInternet()) {
                    ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
                    Call<JsonElement> call = apiClass.get_termsconditions();
                    final KProgressHUD hud = KProgressHUD.create(ConfirmBookingActivity.this)
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
                                        String guideline = jsonObject.getString("terms");
                                        JSONObject jsonObject1 = new JSONObject(guideline);
                                        String content = jsonObject1.getString("description");
                                        Spanned sp = Html.fromHtml(content);
//                                        terms.setText(sp);
                                        Intent intent = new Intent(getApplicationContext(), TermsandConditionsActivity.class);
                                        intent.putExtra("terms", content);
                                        startActivity(intent);
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
                } else {
                    Toast.makeText(ConfirmBookingActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (check.isChecked()) {
            sp_agree = "1";
        } else {
            sp_agree = "0";
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check.isChecked()) {
                    sp_agree = "1";
                    confrm_booking.setBackgroundResource(R.drawable.bg_yellow_rounded);
                } else {
                    sp_agree = "0";
                    confrm_booking.setBackgroundResource(R.drawable.bg_gray_rounded);
                }
            }
        });

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            studentid = (String) b.get("student_id");
            id = (String) b.get("instruct_id");
            name = (String) b.get("username");
            lang = (String) b.get("language");
            rating = (String) b.get("rating");
            loc = (String) b.get("location");
            date = (String) b.get("bookingdate");
            start_time = (String) b.get("strttme");
            end_time = (String) b.get("endtme");
            hr = (String) b.get("total_hrs");
            img = (String) b.get("image");
            longt = (String) b.get("longitude");
            lat = (String) b.get("lat");
            price = (String) b.get("price");
            tvprice.setText(price + "/hr");
            txtname.setText(name);
            txtlanguage.setText(lang);
            System.out.println("rating test " + rating + "\t" + price);
            txtrating.setText(rating);
            txtlocation.setText(loc);
            txtdt.setText(date);
            txttime.setText(start_time + "-" + end_time);
            txthr.setText(hr + "hr");

            Picasso.get().load(Constants_Urls.pic_base_url + img)
                    .placeholder(R.drawable.user)
                    .into(img_instruct);
            // Toast.makeText(getApplicationContext(),""+price,Toast.LENGTH_LONG).show();
        }
        confrm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_agree.isEmpty() || sp_agree.equals("0")) {
                    Toast.makeText(ConfirmBookingActivity.this, getResources().getString(R.string.agree_terms_cond), Toast.LENGTH_SHORT).show();


                } else {
                    if (Float.parseFloat(price) > 0) {
                        //Booking_confirm();
                        Intent i = new Intent(ConfirmBookingActivity.this, StripePayment.class);
                        i.putExtra("student_id", studentid);
                        i.putExtra("instruct_id", id);
                        i.putExtra("username", name);
                        i.putExtra("location", loc);
                        i.putExtra("language", lang);
                        i.putExtra("rating", rating);
                        i.putExtra("strttme", start_time);
                        i.putExtra("endtme", end_time);
                        i.putExtra("bookingdate", date);
                        i.putExtra("longitude", longt);
                        i.putExtra("lat", lat);
                        i.putExtra("Type", "1");
                        i.putExtra("price", price);
                        i.putExtra("total_hrs", hr);
                        i.putExtra("pickup_location", student_pickup_location.getText().toString());
                        i.putExtra("student_lat", student_lat);
                        i.putExtra("student_lng", student_lng);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Amount has not added by admin", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        sliderImg = new ArrayList<>();
        sliderImg.clear();
        if (networkConnection.isConnectingToInternet()) {
            get_slider_Details();
        } else {
            Toast.makeText(ConfirmBookingActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

        final Dialog dialog = new Dialog(ConfirmBookingActivity.this, R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.actvity_rules);
        /*  dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent_bg);*/
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
//        dialog.show();
        RecyclerView recycler_rules = dialog.findViewById(R.id.recyclr_cvd_rules);
        ImageView close = dialog.findViewById(R.id.img_cls);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (networkConnection.isConnectingToInternet()) {
            ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
            Call<JsonElement> call = apiClass.get_covid_category();
            final KProgressHUD hud = KProgressHUD.create(ConfirmBookingActivity.this)
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
                                String data = jsonObject.optString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                System.out.println("array list1" + jsonArray);
                                rules = new ArrayList<>();
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(ConfirmBookingActivity.this, 1);
                                recycler_rules.setLayoutManager(gridLayoutManager);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String catgryid = jsonObject1.getString("id");
                                    Log.i("the catagory data is", "" + catgryid);
                                    String msg = jsonObject1.getString("message");
                                    Log.i("the faq data is", "" + msg);
                                    String sts = jsonObject1.getString("status");
                                    String created = jsonObject1.getString("created_on");

                                    rules.add(new Covid_Rules(catgryid, msg, sts, created));
                                }
                                recycler_rules.setAdapter(new RulesAdapter(ConfirmBookingActivity.this, rules));
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
        } else {
            Toast.makeText(ConfirmBookingActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                pi_location = data.getStringExtra("location");
                student_lat = data.getStringExtra("strLatitude");
                student_lng = data.getStringExtra("strLongitude");
                student_pickup_location.setText(pi_location);
            }
        }
    }


    private void get_slider_Details() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.terms_instructions();


        KProgressHUD hud = KProgressHUD.create(ConfirmBookingActivity.this)
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
                            String offersbanner = jsonObject.optString("data");
                            JSONArray jsonArray = new JSONArray(offersbanner);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String description = jsonObject1.getString("description");
                                String image = jsonObject1.getString("image");
                                String stus = jsonObject1.getString("status");
                                String created = jsonObject1.getString("created_on");
                                String img_path = jsonObject.optString("image_path");
                                Log.i("image path", "" + img_path);
                                sliderImg.add(new SliderUtils(id, description, image, stus, created, img_path));
                                viewPagerAdapter = new ViewPagerAdapter(sliderImg,
                                        ConfirmBookingActivity.this);
                                viewPager.setAdapter(viewPagerAdapter);
                             /*   viewPager.setPadding(50,0,50,0);
                                viewPager.setPageMargin(-1);*/

                            }

                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {

                                    for (int i = 0; i < dotscount; i++) {
                                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                                    }

                                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });

                            dotscount = viewPagerAdapter.getCount();
                            dots = new ImageView[dotscount];

                            System.out.println("dots " + dotscount);
                            for (int i = 0; i < dotscount; i++) {

                                dots[i] = new ImageView(ConfirmBookingActivity.this);
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                        R.drawable.nonactive_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                        (LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);

                                params.setMargins(8, 0, 8, 0);

                                sliderDotspanel.addView(dots[i], params);

                            }

                            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                    R.drawable.active_dot));
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == sliderImg.size()) {
                                        currentPage = 0;
                                    }
                                    viewPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 5000, 5000);

                        } else {

                            /*hud.dismiss();*/

                        }


                    } catch (Exception e) {

                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

                Log.e("sdfdsd ", t.toString());
            }
        });
    }


}
