package com.student.tyro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActvity extends AppCompatActivity {
    EditText edt_one, edt_two, edt_three, edt_four;
    Button button_verify;
    TextView resend_txt, tv_phone;
    String strOtp = "";
    NetworkConnection networkConnection;
    String User_id = "";
    String number = "";
    ImageView back;

    private String firebase_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        networkConnection = new NetworkConnection(OtpActvity.this);
        User_id = getIntent().getStringExtra("User_id");
        number = getIntent().getStringExtra("phonenumber");
        edt_one = findViewById(R.id.edt_one);
        edt_two = findViewById(R.id.edt_two);
        edt_three = findViewById(R.id.edt_three);
        edt_four = findViewById(R.id.edt_four);
        resend_txt = findViewById(R.id.resend_txt);
        button_verify = findViewById(R.id.button_verify);
        back = findViewById(R.id.imgbck);
        tv_phone = findViewById(R.id.tv_phone_number);
        tv_phone.setText(number);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", MODE_PRIVATE);
        firebase_token = sharedPreferences.getString("firebase_token", "");
        System.out.println("token  " + firebase_token);


        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (firebase_token.equals("")) {
                    FirebaseInstanceId.getInstance().getInstanceId().
                            addOnSuccessListener(OtpActvity.this, new OnSuccessListener<InstanceIdResult>() {
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


        button_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String etOne = edt_one.getText().toString();
                String etTwo = edt_two.getText().toString();
                String etThree = edt_three.getText().toString();
                String etFour = edt_four.getText().toString();

                if (!etOne.isEmpty() && !etTwo.isEmpty() && !etThree.isEmpty() && !etFour.isEmpty()) {
                    strOtp = etOne + etTwo + etThree + etFour;
                    Log.i("String otp is", "" + strOtp);
                    if (networkConnection.isConnectingToInternet()) {
                        /* loadApproveDialog();*/
                        verifyMobile(strOtp);
                    } else {
                        Toast.makeText(OtpActvity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast toast = Toast.makeText(OtpActvity.this, getResources().getText(R.string.pleaseenteryourotp), Toast.LENGTH_SHORT);
                    View view1 = toast.getView();
                    toast.show();

                }

            }
        });


        resend_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResendOTP();
            }
        });
        edt_one.addTextChangedListener(new GenericTextWatcher(edt_one));
        edt_two.addTextChangedListener(new GenericTextWatcher(edt_two));
        edt_three.addTextChangedListener(new GenericTextWatcher(edt_three));
        edt_four.addTextChangedListener(new GenericTextWatcher(edt_four));


    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.edt_one:
                    if (text.length() == 1)
                        edt_two.requestFocus();
                    edt_two.setSelection(edt_two.getText().length());
                    break;
                case R.id.edt_two:
                    if (text.length() == 1)
                        edt_three.requestFocus();
                    edt_three.setSelection(edt_three.getText().length());
                    break;
                case R.id.edt_three:
                    if (text.length() == 1)
                        edt_four.requestFocus();
                    edt_four.setSelection(edt_four.getText().length());
                    break;
                case R.id.edt_four:
                    if (text.length() == 1)
                        button_verify.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            String text = charSequence.toString();
            switch (view.getId()) {
                case R.id.edt_one:
                    if (text.length() == 1)
                        edt_two.requestFocus();
                    edt_two.setSelection(edt_two.getText().length());
                    break;
                case R.id.edt_two:
                    if (text.length() == 1) {
                        edt_three.requestFocus();
                        edt_three.setSelection(edt_three.getText().length());
                    } else if (text.length() == 0) {
                        edt_one.requestFocus();
                        edt_one.setSelection(edt_one.getText().length());
                    }
                    break;
                case R.id.edt_three:
                    if (text.length() == 1) {
                        edt_four.requestFocus();
                        edt_four.setSelection(edt_four.getText().length());
                    } else if (text.length() == 0) {
                        edt_two.requestFocus();
                        edt_two.setSelection(edt_two.getText().length());
                    }
                    break;
                case R.id.edt_four:
                    if (text.length() == 1) {
                        button_verify.requestFocus();
                    } else if (text.length() == 0) {
                        edt_three.requestFocus();
                        edt_three.setSelection(edt_three.getText().length());
                    }
                    break;
            }
        }
    }


    private void verifyMobile(String otp) {

        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.otpverification(User_id, otp, "android", firebase_token);
        Log.i("otp values is", "" + User_id + otp + "\t"
                + firebase_token);
        final KProgressHUD hud = KProgressHUD.create(OtpActvity.this)
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
                            String user_info = jsonObject.getString("User_info");
                            JSONObject jsonObject1 = new JSONObject(user_info);
                            String user_id = jsonObject1.getString("user_id");
                            String first_name = jsonObject1.getString("firstname");
                            String last_name = jsonObject1.getString("lastname");
                            String email = jsonObject1.getString("email");
                            String mobile_no = jsonObject1.getString("phone");
                            String user_image = jsonObject1.getString("profile_pic");
                            String auth_level = jsonObject1.getString("auth_level");
                            String otp_status = jsonObject1.getString("otp_status");
                            SharedPreferences sharedPreferences = getSharedPreferences("Login_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("User_id", user_id);
                            editor.putString("login_name", first_name + " " + last_name);
                            editor.putString("login_firstname", first_name);
                            editor.putString("login_email", email);
                            editor.putString("auth_level", auth_level);
                            editor.putString("login_number", mobile_no);
                            editor.putString("User_pic", user_image);
                            editor.commit();
                            editor.apply();
                            Toast.makeText(OtpActvity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(OtpActvity.this, G2LicenceActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        } else {
                            Toast.makeText(OtpActvity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    //resend otp
    private void ResendOTP() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.resend_otp(User_id);
        final KProgressHUD hud = KProgressHUD.create(OtpActvity.this)
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
                            Toast.makeText(OtpActvity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        } else if (status.equals("0")) {
                            Toast.makeText(OtpActvity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}