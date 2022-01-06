package com.student.tyro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Retrofit_Class;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferalActivity extends AppCompatActivity {
    AppCompatTextView btnSubmit;
    String user_id, referral;
    TextView referal_txt,click_here;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referal);
        btnSubmit = findViewById(R.id.btnSubmit);
        referal_txt = findViewById(R.id.referal_txt);
        click_here = findViewById(R.id.click_here);

        user_id = getIntent().getStringExtra("User_id");
        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        referral = sharedPreferences.getString("reference_code", "");

        referal_txt.setText(referral);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        click_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://demomaplebrains.com/tyro/"));
                startActivity(browserIntent);
            }
        });

      btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
//                        "Hey, Have you hear about Tyro? Use this code when signing up to help me win a prize! " + referral);
                        "Hey, I'm using Tyro Driving to acquire my G2 and you should too! Use this code when\n" +
                                "purchasing the e-course and we both receive an entry into their monthly raffles! " + referral);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

}