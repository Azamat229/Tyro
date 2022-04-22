package com.student.tyro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.student.tyro.Adapter.StudentReviewAdapter;
import com.student.tyro.Model.StudentReview;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorsDetailsActivity extends AppCompatActivity {
    String insid;
    ImageView instruct_img;
    TextView tv_name, tv_loc;
    NetworkConnection networkConnection;
    String id, fname, lname, loc, image, email, phone, password, unique_id, auth_level, device_name, device_token, role, type, user_status, latitude, longitude, licence, insurance, void_check, about_us;
    String language, facebook, instagram, twitter, linkedin, created_on, carname, carmodel, caryear;
    //int total_reviews,rating_1,rating_2,rating_3,rating_4,rating_5,avg_ratings;
    String total_reviews, rating_1, rating_2, rating_3, rating_4, rating_5, avg_ratings;
    Button buk_cls;
    String newValue;
    RoundCornerProgressBar progress1, progress2, progress3, progress4, progress5;
    TextView tvrate, ttlrating, tvrating, tvcartype, tvkms, tvlanguage, tvamount, tvabout;
    ImageView back;
    LinearLayout linearchat;
    String userid, bde_status;
    ImageView imgbadge;
    TextView tvratingcount5, tvratingcount4, tvratingcount3, tvratingcount2, tvratingcount1, counter_detail_inst;
    private static final Pattern UNICODE_HEX_PATTERN = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
    private static final Pattern UNICODE_OCT_PATTERN = Pattern.compile("\\\\([0-7]{3})");

    // Student Review
    RecyclerView recycle_student_review;
    ArrayList<StudentReview> studentReviewsList;
    StudentReviewAdapter studentReviewAdapter;

    public interface OnItemUpdateListener{
        static void onUpdateTotal(int total) {


        }
    }
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructors_details);
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("User_id", "");
        bde_status = sharedPreferences.getString("bde_status", "");
        instruct_img = findViewById(R.id.img_detail_ins_img);
        tv_name = findViewById(R.id.tv_detail_ins_name);
        tv_loc = findViewById(R.id.tv_detail_ins_loc);
        tvcartype = findViewById(R.id.tvcartype);
        tvkms = findViewById(R.id.txtkms);
        tvlanguage = findViewById(R.id.txtlanguage);
        tvamount = findViewById(R.id.txtamount);
        buk_cls = findViewById(R.id.btnBookAClass);
        progress1 = findViewById(R.id.progress_1);
        progress2 = findViewById(R.id.progress_2);
        progress3 = findViewById(R.id.progress_3);
        progress4 = findViewById(R.id.progress_4);
        progress5 = findViewById(R.id.progress_5);
        tvrate = findViewById(R.id.tvrate);
        ttlrating = findViewById(R.id.ttl_ratings);
        tvrating = findViewById(R.id.tvrating);
        tvabout = findViewById(R.id.tv_detail_ins_about);
        back = findViewById(R.id.ivBack);
        linearchat = findViewById(R.id.llChat);
        imgbadge = findViewById(R.id.img_badge);
        tvratingcount1 = findViewById(R.id.star1_count);
        tvratingcount2 = findViewById(R.id.star2_count);
        tvratingcount3 = findViewById(R.id.star3_count);
        tvratingcount4 = findViewById(R.id.star4_count);
        tvratingcount5 = findViewById(R.id.star5_count);
        counter_detail_inst = findViewById(R.id.counter_detail_inst);

        recycle_student_review = findViewById(R.id.rvstudentreviews);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            insid = (String) b.get("instruct_id");
            Toast.makeText(getApplicationContext(), "" + insid, Toast.LENGTH_LONG).show();
        }

        buk_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InstructorsDetailsActivity.this, Exampletimeslot.class);
                i.putExtra("instruct_id", insid);
                i.putExtra("fname", fname);
                i.putExtra("lname", lname);
                i.putExtra("location", loc);
                i.putExtra("language", language);
                i.putExtra("rating", newValue);
                i.putExtra("image", image);
                i.putExtra("longitude", longitude);
                i.putExtra("latitude", latitude);
                i.putExtra("payment_type", "0");
                startActivity(i);
            }
        });
        linearchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InstructorsDetailsActivity.this, ChatActivity.class);
                i.putExtra("sender_id", userid);
                i.putExtra("receiver_id", insid);
                startActivity(i);
            }
        });

        /*tv_loc.setText(insloc);
        Picasso.get().load(Constants_Urls.pic_base_url+insimg)
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .into(instruct_img);*/
        networkConnection = new NetworkConnection(InstructorsDetailsActivity.this);
        if (networkConnection.isConnectingToInternet()) {
            getInstructordetils();


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recycle_student_review.setLayoutManager(linearLayoutManager);
//            recycle_student_review.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            studentReviewAdapter = new StudentReviewAdapter(InstructorsDetailsActivity.this, studentReviewsList);
            getStudentReviews();

        } else {
            Toast.makeText(InstructorsDetailsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
    }

    private void getStudentReviews() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<List<StudentReview>> call = apiClass.student_review(insid);

        final KProgressHUD hud = KProgressHUD.create(InstructorsDetailsActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<List<StudentReview>>() {
            @Override
            public void onResponse(Call<List<StudentReview>> call, Response<List<StudentReview>> response) {
                hud.dismiss();
                if (response.isSuccessful()) {
                    Log.e("StudentReviews", response.body().toString());
                    try {
                        hud.dismiss();
                        List<StudentReview> userResponses = response.body();
                        studentReviewAdapter.setData(userResponses );
                        recycle_student_review.setAdapter(studentReviewAdapter);

//                        JSONArray jsonArray = new JSONArray(response.body());
//                        studentReviewsList = new ArrayList<>();
//                        GridLayoutManager gridLayoutManager = new GridLayoutManager(InstructorsDetailsActivity.this, 1);
//                        recycle_student_review.setLayoutManager(gridLayoutManager);
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            StudentReview jsonObject1 = userResponses.get(i);
//                            Log.e("Loop in run", "" + jsonObject1);
//                            String student_name = jsonObject1.getStudent_name();
//                            String rating = jsonObject1.getRating();
//                            String review = jsonObject1.getReview();
//                            String time = jsonObject1.getTime();
//                            String base_path = jsonObject1.getBase_path();
//                            String picture = jsonObject1.getPicture();
//
//                            studentReviewsList.add(new StudentReview(base_path,picture,rating,  review,  student_name,  time));
//                            Log.i("the catagory data is", "" + studentReviewsList);
//                        }
//                        studentReviewAdapter = new StudentReviewAdapter(InstructorsDetailsActivity.this, studentReviewsList);
//                        recycle_student_review.setAdapter(studentReviewAdapter);
//                        recycle_student_review.setVisibility(View.VISIBLE);
//                            linearnoinstruct.setVisibility(View.GONE); // thing about it
//                        } else if (status == 0) {
//                            recycle_instruct.setVisibility(View.GONE);
//                            linearnoinstruct.setVisibility(View.VISIBLE);
//                            tvnoinstruct.setText("No Instructors available");
//
                    } catch (Exception e) {
                        hud.dismiss();
                        e.printStackTrace();
                        Log.e("EXCEPTION ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<StudentReview>> call, Throwable t) {

            }


        });


    }


    private void getInstructordetils() {

        //  RequestBody r_instructid = RequestBody.create(MediaType.parse("multipart/form-data"), insid);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.instructor_profile_view(insid);
        final KProgressHUD hud = KProgressHUD.create(InstructorsDetailsActivity.this)
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
                            JSONObject dataobject = jsonObject.getJSONObject("userInfo");
                            Log.e("dataobjectdataobject", "" + dataobject);

                            id = dataobject.optString("user_id");
                            fname = dataobject.optString("firstname");
                            lname = dataobject.optString("lastname");
                            email = dataobject.optString("email");
                            phone = dataobject.optString("phone");
                            password = dataobject.optString("password");
                            unique_id = dataobject.optString("unique_id");
                            auth_level = dataobject.optString("auth_level");
                            image = dataobject.optString("profile_pic");
                            device_name = dataobject.optString("device_name");
                            device_token = dataobject.optString("device_token");
                            role = dataobject.optString("role");
                            type = dataobject.optString("type");
                            user_status = dataobject.optString("user_status");
                            loc = dataobject.optString("location");
                            latitude = dataobject.optString("latitude");
                            longitude = dataobject.optString("longitude");
                            licence = dataobject.optString("licence");
                            insurance = dataobject.optString("insurance");
                            void_check = dataobject.optString("void_check");
                            about_us = dataobject.optString("about_us");
                            language = dataobject.optString("language");
                            carname = dataobject.optString("car_name");
                            carmodel = dataobject.optString("car_model");
                            caryear = dataobject.optString("car_year");
                          /*  facebook = dataobject.optString("facebook");
                            instagram = dataobject.optString("instagram");
                            twitter = dataobject.optString("twitter");
                            linkedin = dataobject.optString("linkedin");*/
                            created_on = dataobject.optString("created_on");
                            String price = dataobject.optString("price");
                            if (bde_status != null && bde_status.equals("1")) {
                                price = "50";
                            } else {
                                price = price;
                            }
                            String badge = dataobject.optString("badge");
                            total_reviews = jsonObject.optString("total_reviews");
                            JSONObject dataobject1 = jsonObject.getJSONObject("rating_values");
                            rating_1 = dataobject1.optString("rating_1");
                            rating_2 = dataobject1.optString("rating_2");
                            rating_3 = dataobject1.optString("rating_3");
                            rating_4 = dataobject1.optString("rating_4");
                            rating_5 = dataobject1.optString("rating_5");
                            avg_ratings = jsonObject.optString("avg_ratings");
                          /*  String data = jsonObject.getString("travelled");
                            Log.i("the faq data is", "" + data);*/
                            JSONObject jsonObject1 = jsonObject.getJSONObject("travelled");
                            Log.i("the faq data is", "" + jsonObject1);
                            String total_classes = jsonObject1.optString("total_classes");
                            String completed_classes = jsonObject1.optString("completed_classes");
                            String kms = jsonObject1.optString("kms");
                            Log.i("the kms data is", "" + kms);
                            String hours = jsonObject1.optString("hours");
                            // newValue = Integer.toString((int)avg_ratings);
                            Picasso.get().load(Constants_Urls.pic_base_url + image)
                                    .placeholder(R.drawable.user)
                                    .into(instruct_img);
                            Log.i("the image path is", "" + Constants_Urls.pic_base_url + image);
                            tv_name.setText(fname);
                            tv_loc.setText(loc);
                            if (!dataobject.optString("car_name").equals("")) {
                                carname = dataobject.optString("car_name");
                            } else {
                                carname = "-";
                            }
                            if (!dataobject.optString("car_model").equals("")) {
                                carmodel = dataobject.optString("car_model");
                            } else {
                                carmodel = "";
                            }
                            if (!dataobject.optString("car_year").equals("")) {
                                caryear = dataobject.optString("car_year");
                            } else {
                                caryear = "";
                            }

                            tvcartype.setText(carname + "\n" + carmodel + "\n" + caryear);

                            String langtxt = null;
                            if (!language.equals("")) {
                                String[] lang = language.split(",");
                                for (String lang1 : lang) {
                                    if (langtxt == null) {
                                        langtxt = lang1;
                                    } else {
                                        langtxt = langtxt + "\n" + lang1;
                                    }
                                }
                            } else {
                                langtxt = "-";
                            }

                            tvlanguage.setText(langtxt);
                            tvkms.setText(total_classes);
                            tvamount.setText("$" + price);
                            tvabout.setText(about_us);
                            // System.out.println("Display_pic"+display_pic);
                            progress1.setProgressColor(Color.parseColor("#FFBE00"));
                            progress1.setProgressBackgroundColor(Color.parseColor("#D3D3D3"));
                            progress1.setMax(100);
                            float rating1 = Float.parseFloat(rating_1);
                            progress1.setProgress(rating1);

                            progress2.setProgressColor(Color.parseColor("#FFBE00"));
                            progress2.setProgressBackgroundColor(Color.parseColor("#D3D3D3"));
                            progress2.setMax(100);
                            float rating2 = Float.parseFloat(rating_2);
                            progress2.setProgress(rating2);

                            progress3.setProgressColor(Color.parseColor("#FFBE00"));
                            progress3.setProgressBackgroundColor(Color.parseColor("#D3D3D3"));
                            progress3.setMax(100);
                            float rating3 = Float.parseFloat(rating_3);
                            progress3.setProgress(rating3);

                            progress4.setProgressColor(Color.parseColor("#FFBE00"));
                            progress4.setProgressBackgroundColor(Color.parseColor("#D3D3D3"));
                            progress4.setMax(100);
                            float rating4 = Float.parseFloat(rating_4);
                            progress4.setProgress(rating4);

                            progress5.setProgressColor(Color.parseColor("#FFBE00"));
                            progress5.setProgressBackgroundColor(Color.parseColor("#D3D3D3"));
                            progress5.setMax(100);
                            float rating5 = Float.parseFloat(rating_5);
                            progress5.setProgress(rating5);
                            float f = Float.parseFloat(avg_ratings);
                            String myrating = Float.toString(f);
                            newValue = myrating;
                            tvrate.setText(myrating);
                            tvrating.setText(myrating);
                            ttlrating.setText(total_reviews + " " + "Ratings");
                            /*if(badge.equals("1"))
                            {
                                imgbadge.setVisibility(View.VISIBLE);
                            }*/
//                            String count1, count2, count3, count4, count5;
//                            count1 = Integer.toString((int) Integer.parseInt(rating_1));
//                            tvratingcount1.setText(count1);
//                            count2 = Integer.toString((int) Integer.parseInt(rating_2));
//                            tvratingcount2.setText(count2);
//                            count3 = Integer.toString((int) Integer.parseInt(rating_3));
//                            tvratingcount3.setText(count3);
//                            count4 = Integer.toString((int) Integer.parseInt(rating_4));
//                            tvratingcount4.setText(count4);
//                            count5 = String.valueOf(Integer.parseInt(rating_5));
                            tvratingcount1.setText(String.format("%.0f", Float.parseFloat(rating_1)));
                            tvratingcount2.setText(String.format("%.0f", Float.parseFloat(rating_2)));
                            tvratingcount3.setText(String.format("%.0f", Float.parseFloat(rating_3)));
                            tvratingcount4.setText(String.format("%.0f", Float.parseFloat(rating_4)));
                            tvratingcount5.setText(String.format("%.0f", Float.parseFloat(rating_5)));
                            hud.dismiss();
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

    public static String decodeFromNonLossyAscii(String original) {
        Matcher matcher = UNICODE_HEX_PATTERN.matcher(original);
        StringBuffer charBuffer = new StringBuffer(original.length());
        while (matcher.find()) {
            String match = matcher.group(1);
            char unicodeChar = (char) Integer.parseInt(match, 16);
            matcher.appendReplacement(charBuffer, Character.toString(unicodeChar));
        }
        matcher.appendTail(charBuffer);
        String parsedUnicode = charBuffer.toString();

        matcher = UNICODE_OCT_PATTERN.matcher(parsedUnicode);
        charBuffer = new StringBuffer(parsedUnicode.length());
        while (matcher.find()) {
            String match = matcher.group(1);
            char unicodeChar = (char) Integer.parseInt(match, 8);
            matcher.appendReplacement(charBuffer, Character.toString(unicodeChar));
        }
        matcher.appendTail(charBuffer);
        return charBuffer.toString();
    }

    public static String decodeEmoji(String message) {
        String myString = null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

}

