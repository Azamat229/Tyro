package com.student.tyro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Retrofit_Class;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InconvenienceActivity extends AppCompatActivity {
    AppCompatTextView btnSubmit;
    AppCompatEditText edt_useremail;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inconvenience);
        btnSubmit = findViewById(R.id.btnSubmit);
        edt_useremail = findViewById(R.id.edt_useremail);

        user_id = getIntent().getStringExtra("User_id");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_useremail.getText().length() == 0) {
                    edt_useremail.setError("Enter email");
                    edt_useremail.requestFocus();
                } else {
                    serviceInconvience(user_id, edt_useremail.getText().toString());
                }
            }
        });
    }

    private void serviceInconvience(String user_id, String email) {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.inconvince(user_id, email);

        final KProgressHUD hud = KProgressHUD.create(InconvenienceActivity.this)
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
                            Intent mainIntent = new Intent(InconvenienceActivity.this, UploadDocumentsActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                            finish();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("inconvince"), Toast.LENGTH_SHORT).show();

                        } else if (status.equals("0")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("inconvince"), Toast.LENGTH_SHORT).show();
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
}