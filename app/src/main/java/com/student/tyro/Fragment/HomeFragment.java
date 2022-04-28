package com.student.tyro.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.InstructorsActivity;
import com.student.tyro.LoginActivity;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.R;
import com.student.tyro.Adapter.UpcmgLessonAdapter;
import com.student.tyro.Model.Lessions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    TextView username, tvupcoming, tvnoclasses;
    String user_name, user_id;
    RecyclerView recycler_upcmg;
    ArrayList<Lessions> lessionslist;
    Button book_cls;
    NetworkConnection networkConnection;
    LinearLayout linerupcmg;
    TextView tvamout, tvhrs, counter, credits_amount;
    ImageView imgvew_badge, imgvew_private, credits_icon;


    @Override
    public void onResume() {
        super.onResume();
        Upcoming_lessons();
        checkuser();
        getdriven_details();
        get_credits();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_dashboard, container, false);
        username = rootView.findViewById(R.id.tv_usrname);
        linerupcmg = rootView.findViewById(R.id.linear_upcmg);
        tvupcoming = rootView.findViewById(R.id.tv_upcmg);
        tvnoclasses = rootView.findViewById(R.id.txt_noclasses);
        tvamout = rootView.findViewById(R.id.tv_driven_amount);
        tvhrs = rootView.findViewById(R.id.tv_driven_hrs);
        imgvew_badge = rootView.findViewById(R.id.badge_for_studnt);
        imgvew_private = rootView.findViewById(R.id.private_for_studnt);
        credits_amount = rootView.findViewById(R.id.credits_amount);
        credits_icon = rootView.findViewById(R.id.credits_icon);



        networkConnection = new NetworkConnection(getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        user_name = sharedPreferences.getString("login_firstname", "");
        user_id = sharedPreferences.getString("User_id", "");
        //Toast.makeText(getActivity(),""+user_id,Toast.LENGTH_LONG).show();
        username.setText(user_name + "!");
        recycler_upcmg = rootView.findViewById(R.id.upcmg_lesson);
        book_cls = rootView.findViewById(R.id.btnBookClass);

        book_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), InstructorsActivity.class);
                startActivity(i);
            }
        });


        if (networkConnection.isConnectingToInternet()) {
            checkuser();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
        if (networkConnection.isConnectingToInternet()) {
            Upcoming_lessons();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
        if (networkConnection.isConnectingToInternet()) {
            getdriven_details();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }


    private void Upcoming_lessons() {
        // RequestBody r_userid = RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
        RequestBody r_userid = RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.Upcoming_classes(r_userid);
        Log.e("r_userid", r_userid.toString());


        final KProgressHUD hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hud.dismiss();

                if (response.isSuccessful()) {
                    Log.e("up_coming_classes", response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {

                            String data = jsonObject.optString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            System.out.println("array is" + jsonArray);
                            lessionslist = new ArrayList<>();
                            // GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
                            //recycler_upcmg.setLayoutManager(gridLayoutManager);
                            recycler_upcmg.setVisibility(View.VISIBLE);
                            tvupcoming.setVisibility(View.VISIBLE);
                            recycler_upcmg.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String sid = jsonObject1.getString("student_id");
                                String insid = jsonObject1.getString("instructor_id");
                                String strtme = jsonObject1.getString("start_time");
                                String endtme = jsonObject1.getString("end_time");
                                //String booking_time = jsonObject1.getString("booking_time");
                                String booking_date = jsonObject1.getString("booking_date");
                                String booking_hours = jsonObject1.getString("booking_hours");
                                //String created_on = jsonObject1.getString("created_on");
                                String username = jsonObject1.getString("instructor_name");
                                String profile_pic = jsonObject1.getString("profile_pic");
                                String location = jsonObject1.getString("location");
                                String price = jsonObject1.getString("price");
                                String uniqueid = jsonObject1.getString("unique_id");
                                String sts = jsonObject1.getString("status");
                                String payment_type = jsonObject1.getString("payment_type");
                                String cancel = jsonObject1.getString("cancel_reason");
                                String status1 = jsonObject1.getString("status");
                                String update_latitude = jsonObject1.getString("update_latitude");
                                String update_longitude = jsonObject1.getString("update_longitude");
                                lessionslist.add(new Lessions(id, sid, insid, strtme, endtme,
                                        booking_date, booking_hours, username, profile_pic, location,
                                        price, uniqueid, sts, payment_type, cancel
                                        , status1, update_latitude, update_longitude));
                                Log.i("the catagory data is", "" + lessionslist);

                            }

                            recycler_upcmg.setAdapter(new UpcmgLessonAdapter(getActivity(), lessionslist));
                        } else if (status == 0) {
                            recycler_upcmg.setVisibility(View.GONE);
                            tvupcoming.setVisibility(View.VISIBLE);
                            tvnoclasses.setVisibility(View.VISIBLE);
                            // Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        //hud.dismiss();
                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });
    }

    private void getdriven_details() {

        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.getdriven_details(user_id);
        /*final KProgressHUD hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();*/
        call.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //hud.dismiss();

                if (response.isSuccessful()) {
                    Log.e("spprofileee", response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        String bdestatus = jsonObject.getString("BDE");
                        if (status == 1) {

                            String get_timeslots = jsonObject.optString("data");
                            JSONArray jsonArray = new JSONArray(get_timeslots);
                            System.out.println("slot_data is" + jsonArray);

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("bde_status", bdestatus);
                            editor.commit();
                            editor.apply();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                String outofclasses = jsonObj.optString("total_classes");
                                String completedclasses = jsonObj.optString("completed_classes");
                                String kms = jsonObj.optString("kms");
                                String hours = jsonObj.optString("hours");
                                String badge = jsonObj.optString("badge");

                                tvhrs.setText(kms + "Km");

                                if (bdestatus != null && bdestatus.equals("1")) {
                                    imgvew_badge.setVisibility(View.VISIBLE);
                                    tvamout.setText(completedclasses + " out of 10");
                                } else {
                                    imgvew_private.setVisibility(View.VISIBLE);
                                    tvamout.setText(completedclasses + " out of " + outofclasses);
                                    tvamout.setText(completedclasses + " out of " + outofclasses);
                                }


                            }


                            //   hud.dismiss();

                        }

                    } catch (Exception e) {
                        //  hud.dismiss();
                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //   hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });

    }

    private void checkuser() {

        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.checkuser(user_id);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //hud.dismiss();

                if (response.isSuccessful()) {
                    Log.e("spprofileee", response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            String user = jsonObject.optString("user");
                            System.out.println("user is" + user);
                        } else {
                            SignOut();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("dskfsdf ", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //   hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });
    }

    private void get_credits() {
        RequestBody r_userid = RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_credits(r_userid);
        Log.e("r_userid", r_userid.toString());

        final KProgressHUD hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hud.dismiss();

                if (response.isSuccessful()) {
                    Log.e("get_credits_RESPONSE", response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int credits = jsonObject.getInt("credits");
                        Log.e("CREDITS", String.valueOf(credits));

                        if(credits > 0){
                            credits_amount.setVisibility(View.VISIBLE);
                            credits_icon.setVisibility(View.VISIBLE);
                            credits_amount.setText("x"+credits);
                        }

                    } catch (Exception e) {
                        //hud.dismiss();
                        e.printStackTrace();
                        Log.e("get_credits_catch ", e.toString());
                    }
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //hud.dismiss();
                Log.e("get_credits_failure ", t.toString());
            }
        });


    }

    private void SignOut() {
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.signout(user_id);
        final KProgressHUD hud = KProgressHUD.create(getActivity())
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

                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login_details", 0);
                            SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("student_details", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            editor.apply();
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.clear();
                            editor1.commit();
                            editor1.apply();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            getActivity().finish();
                        } else if (status == 0) {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
