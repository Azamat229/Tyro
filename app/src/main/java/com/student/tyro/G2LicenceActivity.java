package com.student.tyro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Retrofit_Class;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class G2LicenceActivity extends AppCompatActivity {
    ImageView no_btn, yes_btn;
    private String status, user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g2_licence);
        yes_btn = findViewById(R.id.yes_btn);
        no_btn = findViewById(R.id.no_btn);

        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("User_id", "");

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("2");
            }
        });

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog("2");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://demomaplebrains.com/tyro/"));
                startActivity(browserIntent);
            }
        });
    }

    private void showDialog(String tag) {
        final Dialog dialog = new Dialog(G2LicenceActivity.this, R.style.MyAlertDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bde_dialog);
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_background);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        TextView title = dialog.findViewById(R.id.title);
        TextView description = dialog.findViewById(R.id.description);
        LinearLayout submit_layout = dialog.findViewById(R.id.submit_layout);
        if (tag.equals("1")) {
            status = "1";
            title.setText("Yes");
            description.setText("Thank you for interested in BDE Course");
        } else if (tag.equals("2")) {
            status = "1";
            title.setText("Yes");
            description.setText("Not interested in BDE Course But would you like to book private lessons");
        }
        submit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceBDEstatus(status,dialog);
            }
        });
    }

    private void serviceBDEstatus(String status, Dialog dialog) {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.bde_status(user_id, status);
        final KProgressHUD hud = KProgressHUD.create(G2LicenceActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                hud.dismiss();

                if (response.isSuccessful()) {
                    try {
                        dialog.dismiss();
                        Log.e("jvcnxcvb ", response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            Intent mainIntent = new Intent(G2LicenceActivity.this, LoginMapActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        } else if (status.equals("0")) {
                            Toast.makeText(G2LicenceActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                dialog.dismiss();

                Log.e("shdfdsf ", t.toString());

            }
        });

    }

}
