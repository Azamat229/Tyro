package com.student.tyro;

import android.app.Dialog;
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
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.HelperClass;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText editfname, editlname, editemail, editpswd, editphone;
    Button btnsignup;
    CheckBox check;
    String sp_agree = "";
    HelperClass helperClass;
    NetworkConnection networkConnection;
    TextView txt_terms, signin, tvcontent;
    private String firebase_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        helperClass = new HelperClass(SignUpActivity.this);
        editfname = findViewById(R.id.edit_fname);
        editlname = findViewById(R.id.edit_lname);
        editemail = findViewById(R.id.edit_email);
        editpswd = findViewById(R.id.edit_pswd);
        editphone = findViewById(R.id.edit_phno);
        btnsignup = findViewById(R.id.btnSignUp);
        check = findViewById(R.id.check);
        txt_terms = findViewById(R.id.txt_terms);
        signin = findViewById(R.id.tvSignIn);
        tvcontent = findViewById(R.id.tv_signup_content);
        String text1 = " <font color=#ffffff><u>" + getResources().getString(R.string.i_agree_the_terms_and_conditions) + "</u></font>";
        txt_terms.setText(Html.fromHtml(text1));

        String text2 = " <font color=#F7BD01><u>" + "Sign In" + "</u></font>";
        signin.setText(Html.fromHtml(text2));


        networkConnection = new NetworkConnection(SignUpActivity.this);
        if (check.isChecked()) {
            sp_agree = "1";
        } else {
            sp_agree = "0";
        }

        if (networkConnection.isConnectingToInternet()) {
            getcontent();
        } else {
            Toast.makeText(SignUpActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", MODE_PRIVATE);
        firebase_token = sharedPreferences.getString("firebase_token", "");
        System.out.println("token  " + firebase_token);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (firebase_token == null || firebase_token.equals("")) {
                    FirebaseInstanceId.getInstance().getInstanceId().
                            addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<InstanceIdResult>() {
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
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 100);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check.isChecked()) {
                    sp_agree = "1";
                } else {
                    sp_agree = "0";
                }
            }
        });

        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Dialog dialog = new Dialog(SignUpActivity.this, R.style.MyAlertDialogTheme);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.activity_terms_conditions);
//                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_background);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.setCancelable(true);
//                dialog.show();
//                TextView terms = dialog.findViewById(R.id.terms_conditions);
//                TextView terms_conditions_title = dialog.findViewById(R.id.terms_conditions_title);
//                terms_conditions_title.setVisibility(View.VISIBLE);
//                ImageView close = dialog.findViewById(R.id.img_cls);
//                close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
                if (networkConnection.isConnectingToInternet()) {
                    ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
                    Call<JsonElement> call = apiClass.get_termsconditions();
                    final KProgressHUD hud = KProgressHUD.create(SignUpActivity.this)
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
                    Toast.makeText(SignUpActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editfname.getText().toString().isEmpty()) {
                    showToast("Please enter firstname");
                } else if (editlname.getText().toString().isEmpty()) {
                    showToast("Please enter lastname");
                } else if (editphone.getText().toString().isEmpty()) {
                    showToast(getString(R.string.enter_number));
                } else if (editphone.getText().toString().length() != 10) {

                    Toast.makeText(SignUpActivity.this, getString(R.string.enter_number_length), Toast.LENGTH_SHORT).show();
                } else if (editemail.getText().toString().isEmpty()) {
                    showToast(getString(R.string.Enter_email));
                } else if (!helperClass.validateEmail(editemail.getText().toString().trim())) {
                    helperClass.showToast(SignUpActivity.this, getString(R.string.please_enter_your_valid_email));
                } else if (editpswd.getText().toString().isEmpty()) {
                    showToast(getString(R.string.enter_password));
                } else if (sp_agree.isEmpty() || sp_agree.equals("0")) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.agree_terms_cond), Toast.LENGTH_SHORT).show();
                } else if (networkConnection.isConnectingToInternet()) {
                    Registration();
                }


            }
        });
    }


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void Registration() {

        RequestBody r_first_name = RequestBody.create(MediaType.parse("multipart/form-data"), editfname.getText().toString());
        RequestBody r_last_name = RequestBody.create(MediaType.parse("multipart/form-data"), editlname.getText().toString());
        RequestBody r_mobile_no = RequestBody.create(MediaType.parse("multipart/form-data"), editphone.getText().toString());
        RequestBody r_email = RequestBody.create(MediaType.parse("multipart/form-data"), editemail.getText().toString());
        RequestBody r_password = RequestBody.create(MediaType.parse("multipart/form-data"), editpswd.getText().toString());
        RequestBody r_device_type = RequestBody.create(MediaType.parse("multipart/form-data"), "android");
        RequestBody r_device_token = RequestBody.create(MediaType.parse("multipart/form-data"),
                firebase_token);
        RequestBody r_type = RequestBody.create(MediaType.parse("multipart/form-data"), "2");
        Log.e("user data is ", "" + r_first_name + r_last_name + r_mobile_no + r_email + r_password + r_device_type + r_device_token + r_type);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.User_signup(r_first_name, r_last_name, r_mobile_no, r_email, r_password, r_device_type, r_device_token, r_type);
        final KProgressHUD hud = KProgressHUD.create(SignUpActivity.this)
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
                            String user_info = jsonObject.getString("user_info");
                            JSONObject jsonObject1 = new JSONObject(user_info);
                            String user_id = jsonObject1.getString("user_id");
                            String phonenumber = jsonObject1.getString("phone");
                            Toast.makeText(SignUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(SignUpActivity.this, OtpActvity.class);
                            mainIntent.putExtra("User_id", user_id);
                            mainIntent.putExtra("phonenumber", phonenumber);
                            startActivity(mainIntent);
                            /*SharedPreferences sharedPreferences=getSharedPreferences("Login_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("User_id",user_id);
                            editor.putString("username",user_name);
                            editor.putString("useremail",user_email);
                            editor.commit();
                            editor.apply();*/
                        } else if (status.equals("0")) {
                            Toast.makeText(SignUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void getcontent() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_signup_and_login_content();
        final KProgressHUD hud = KProgressHUD.create(SignUpActivity.this)
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
                            String content = jsonObject1.getString("student_signup");
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

}
