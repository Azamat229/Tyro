package com.student.tyro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Model.Conversation;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsandConditionsActivity extends AppCompatActivity {
    TextView terms_conditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        terms_conditions = findViewById(R.id.terms_conditions);
        System.out.println(getIntent().getStringExtra("terms"));
        String content = getIntent().getStringExtra("terms");
        Spanned sp = Html.fromHtml(content);
        terms_conditions.setText(sp);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
