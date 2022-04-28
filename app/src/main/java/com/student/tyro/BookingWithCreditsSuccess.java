package com.student.tyro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Retrofit_Class;

import org.json.JSONObject;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingWithCreditsSuccess extends AppCompatActivity {

    AppCompatImageView ivBack;
    AppCompatButton btn_proceed;
    String User_id;
    String instructor_id, name, lang, rating, loc, date, start_time, end_time, hr, img, longt, lat, studentid, type;
    int credits;
    private String pickup_location, student_lat, student_lng;
    String bde_status;
    TextView credits_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_with_credits_success);

        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        User_id = sharedPreferences.getString("User_id", "");

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            studentid = (String) b.get("student_id");
            instructor_id = (String) b.get("instruct_id");
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
            type = (String) b.get("Type");
            pickup_location = (String) b.get("pickup_location");
            student_lat = (String) b.get("student_lat");
            student_lng = (String) b.get("student_lng");
            credits = (Integer) b.get("student_credits");
        }

        System.out.println("LOCATION " + pickup_location + "\t" + student_lat + "\t" + student_lng);
        System.out.println("INTENT RESULT" + b.toString());


        ivBack = findViewById(R.id.ivBack);
        btn_proceed = findViewById(R.id.btn_proceed);
        credits_amount = findViewById(R.id.credits_amount);
        credits_amount.setText(credits + "x");

        // Konfetti animation run place
        final KonfettiView konfettiView = findViewById(R.id.viewKonfetti);

        konfettiView.build()
                .addColors(Color.parseColor("#C66190"), Color.parseColor("#A651C4"), Color.parseColor("#B93467"))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = BookingWithCreditsSuccess.this.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
                bde_status = sharedPreferences.getString("bde_status", "");
                ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
                Call<JsonElement> call = apiClass.add_booking_class_by_credits(studentid, instructor_id, start_time, end_time, date, hr, lat, longt, pickup_location, student_lat, student_lng, bde_status);

                Log.e("DBE_status", bde_status);

                final KProgressHUD hud = KProgressHUD.create(BookingWithCreditsSuccess.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setBackgroundColor(R.color.colorPrimary)
                        .show();

                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        hud.dismiss();
                        if (response.isSuccessful()) {
                            Log.e("BOOKING_BY_CREDITS_RES", response.body().toString());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                int status = jsonObject.getInt("status");
                                if (status == 1) {
                                    Toast.makeText(BookingWithCreditsSuccess.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(BookingWithCreditsSuccess.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(BookingWithCreditsSuccess.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    hud.dismiss();
                                }

                                hud.dismiss();

                            } catch (Exception e) {

                                e.printStackTrace();
                                Log.e("BOOKING_BY_CREDIT_EXCE ", e.toString());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {

                        Log.e("sdfdsd ", t.toString());
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BookingWithCreditsSuccess.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}