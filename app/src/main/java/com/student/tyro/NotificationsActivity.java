package com.student.tyro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Model.NotificationModel;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Adapter.NotificationAdapter;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity
{
    NetworkConnection networkConnection;
    String userid;
    ImageView back;
    RecyclerView recycler_notify;
    ArrayList<NotificationModel> notificationModels;
    LinearLayout linearnonotify;
    TextView tvnotifications;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        SharedPreferences sharedPreferences=this.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        userid=sharedPreferences.getString("User_id","");
        networkConnection = new NetworkConnection(NotificationsActivity.this);
        recycler_notify=findViewById(R.id.recycler_notify);
        linearnonotify=findViewById(R.id.linear_nonotifications);
        tvnotifications=findViewById(R.id.tv_nonotifications);
        back=findViewById(R.id.ivBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (networkConnection.isConnectingToInternet())
        {
            getNotifications();
        }
        else
        {
            Toast.makeText(NotificationsActivity.this, getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
    }

    private void getNotifications() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.getnotifications(userid);
        final KProgressHUD hud = KProgressHUD.create(NotificationsActivity.this)
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
                            JSONArray jsonArray=jsonObject.optJSONArray("notifications");
                            notificationModels = new ArrayList<>();
                            if(jsonArray != null) {
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.optJSONObject(i);
                                        String id = object.getString("id");
                                        String uid = object.getString("user_id");
                                        String appointm = object.getString("appointment_id");
                                        String message = object.getString("message");
                                        String finalamount = object.getString("final_amount");
                                        String createdtme = object.getString("notification_created");
                                        String usertype = object.getString("user_type");
                                        String statu = object.getString("status");
                                        String cancel = object.getString("cancel_status");
                                        String type = object.getString("type");
                                        String orderid = object.getString("order_id");
                                        NotificationModel notificationModel =new NotificationModel(id,uid,message,createdtme);
                                        notificationModels.add(notificationModel);
                                    }
                                }
                            }
                            if (notificationModels != null) {
                                if (notificationModels.isEmpty()) {
                                   // Toast.makeText(NotificationsActivity.this, "No Items!", Toast.LENGTH_SHORT).show();
                                    recycler_notify.setAdapter(null);
                                } else {
                                    recycler_notify.setHasFixedSize(true);
                                    recycler_notify.setLayoutManager(new GridLayoutManager(NotificationsActivity.this, 1));
                                    recycler_notify.setAdapter(new NotificationAdapter(notificationModels, NotificationsActivity.this));
                                }
                            }
                            hud.dismiss();
                        }
                        else
                        {
                            recycler_notify.setVisibility(View.GONE);
                            linearnonotify.setVisibility(View.VISIBLE);
                            tvnotifications.setText("No Data Found");
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
    }

