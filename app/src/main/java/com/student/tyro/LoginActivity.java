package com.student.tyro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.HelperClass;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editusermail, editpassword;
    Button siginin;
    HelperClass helperClass;
    NetworkConnection networkConnection;
    TextView signup, forgot_pswd, tvcontent;
    private String firebase_token;
    Handler handler;
    Runnable update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editusermail = findViewById(R.id.edt_useremail);
        editpassword = findViewById(R.id.edt_pswd);
        siginin = findViewById(R.id.btnSignIn);
        signup = findViewById(R.id.txtSignUp);
        forgot_pswd = findViewById(R.id.tv_forgotpswd);
        tvcontent = findViewById(R.id.tv_signin_content);
        helperClass = new HelperClass(LoginActivity.this);
        networkConnection = new NetworkConnection(LoginActivity.this);
        //content getting
        if (networkConnection.isConnectingToInternet()) {
            getcontent();
        } else {
            Toast.makeText(LoginActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", MODE_PRIVATE);
        firebase_token = sharedPreferences.getString("firebase_token", "");
        System.out.println("token  " + firebase_token);


        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (firebase_token.equals("")) {
                    FirebaseInstanceId.getInstance().getInstanceId().
                            addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String newToken = instanceIdResult.getToken();
                                    SharedPreferences sharedPreferences1 =
                                            getSharedPreferences("Login_details", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                                    editor.putString("firebase_token", newToken + "");
                                    editor.apply();
                                    Log.d("token  1  ", newToken);
                                    firebase_token = newToken;
                                    //utils.print(TAG, "onTokenRefresh" + newToken);
                                }
                            });
                }
                Log.d("token  1  ", firebase_token);
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 0, 100);

        forgot_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this, R.style.MyAlertDialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.forgot_pswd_dialog);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_background);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();
                Button btn_submit = dialog.findViewById(R.id.btnforgot);
                ImageView cancel_image = dialog.findViewById(R.id.cancel_image);
                final EditText et_email = dialog.findViewById(R.id.et_email);
                cancel_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_email.getText().toString().isEmpty()) {
                            Toast.makeText(LoginActivity.this, getResources().getText(R.string.Enter_email), Toast.LENGTH_SHORT).show();
                        } else {

                            forgotPwdApi(dialog, et_email.getText().toString());
                        }
                    }
                });
            }
        });
        String text1 = " <font color=#F7BD01><u>" + "Sign Up" + "</u></font>";
        signup.setText(Html.fromHtml(text1));

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, G2LicenceActivity.class);
                startActivity(i);
            }
        });
        siginin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editusermail.getText().toString().isEmpty() && !editpassword.getText().toString().isEmpty()) {

                    if (networkConnection.isConnectingToInternet()) {

                        signIN();

                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getText(R.string.pleaseenterallfields), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void getcontent() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_signup_and_login_content();
        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this)
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
                            String guideline = jsonObject.getString("description");
                            JSONObject jsonObject1 = new JSONObject(guideline);
                            String content = jsonObject1.getString("student_login");
                            tvcontent.setText(content);
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
    }


    private void forgotPwdApi(final Dialog dialog, String email) {
        Log.e("password", email);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.Forgot_password(email);

        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e("jvcnxcvb ", response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        } else if (status.equals("0")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void signIN() {
        Log.e("password", editusermail.getText().toString() + editpassword.getText().toString() + "android"
                + firebase_token);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.api_login(editusermail.getText().toString(), editpassword.getText().toString(),
                "android", firebase_token);

        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this)
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
//                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            String user_info = jsonObject.getString("data");
                            JSONObject jsonObject1 = new JSONObject(user_info);
                            String user_id = jsonObject1.getString("user_id");
                            String first_name = jsonObject1.getString("firstname");
                            String last_name = jsonObject1.getString("lastname");
                            String email = jsonObject1.getString("email");
                            String mobile_no = jsonObject1.getString("phone");
                            String auth_level = jsonObject1.getString("auth_level");
                            String profile_pic = jsonObject1.getString("profile_pic");
                            String latitude = jsonObject1.getString("latitude");
                            String longitude = jsonObject1.getString("longitude");
                            String location = jsonObject1.getString("location");
                            String reference_code = jsonObject1.getString("reference_code");
                            System.out.println("User_id" + user_id);
                            SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("User_id", user_id);
                            editor.putString("login_name", first_name + " " + last_name);
                            editor.putString("login_firstname", first_name);
                            editor.putString("login_email", email);
                            editor.putString("auth_level", auth_level);
                            editor.putString("login_number", mobile_no);
                            editor.putString("reference_code", reference_code);
                            editor.putString("User_pic", profile_pic);
                            editor.putString("lat", latitude);
                            editor.putString("long", longitude);
                            editor.putString("location", location);
                            editor.putBoolean("loginstatus", true);
                            editor.commit();
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();


                        } else if (status.equals("0")) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    public void onBackPressed() {

//            super.onBackPressed();
        moveTaskToBack(true);

    }
}
