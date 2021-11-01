package com.student.tyro.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.student.tyro.MainActivity;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.R;
import com.student.tyro.EditProfileActivity;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragment extends Fragment {
    String User_id;
    ImageView edit, userimage;
    TextView name, location, email, phone, password, about;
    String firstname, lastname, userlocation, usermail, userpswd, userphone, user_image, aboutus, hours, kms, ttlclasses;
    NetworkConnection networkConnection;
    String user_id;
    TextView tvttlcls, ttlhrs, ttldrvntme;

    public UsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_users, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        User_id = sharedPreferences.getString("User_id", "");
        Log.i("the user id", "" + User_id);
        //Toast.makeText(getActivity(),""+User_id,Toast.LENGTH_LONG).show();
        name = rootView.findViewById(R.id.tv_name);
        location = rootView.findViewById(R.id.tv_location);
        email = rootView.findViewById(R.id.tv_email);
        phone = rootView.findViewById(R.id.tv_call);
        password = rootView.findViewById(R.id.tv_pswd);
        userimage = rootView.findViewById(R.id.userimage);
        edit = getActivity().findViewById(R.id.ivRight_editprofile);
        about = rootView.findViewById(R.id.tv_abt);
        tvttlcls = rootView.findViewById(R.id.std_ttl_cls);
        ttlhrs = rootView.findViewById(R.id.std_ttl_drvn);
        ttldrvntme = rootView.findViewById(R.id.std_ttl_tme);
        networkConnection = new NetworkConnection(getActivity());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                i.putExtra("user_id", user_id);
                i.putExtra("firstname", firstname);
                i.putExtra("lastname", lastname);
                i.putExtra("usermail", usermail);
                i.putExtra("userphone", userphone);
                i.putExtra("userpswd", userpswd);
                i.putExtra("userlocation", userlocation);
                i.putExtra("user_image", user_image);
                i.putExtra("aboutus", aboutus);
                startActivity(i);
                getActivity().finish();
            }
        });
        if (networkConnection.isConnectingToInternet()) {
            getProfile();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void getProfile() {
        // RequestBody r_userid = RequestBody.create(MediaType.parse("multipart/form-data"), );
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.get_profile(User_id);
        final KProgressHUD hud = KProgressHUD.create(getActivity())
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

                            JSONObject dataobject = jsonObject.getJSONObject("userInfo");
                            user_id = dataobject.optString("user_id");
                            firstname = dataobject.optString("firstname");
                            lastname = dataobject.optString("lastname");
                            usermail = dataobject.optString("email");
                            userphone = dataobject.optString("phone");
                            userpswd = dataobject.optString("password");
                            userlocation = dataobject.optString("location");
                            user_image = dataobject.optString("profile_pic");
                            aboutus = dataobject.optString("about_us");
                            hours = dataobject.optString("hours");
                            kms = dataobject.optString("kms");
                            ttlclasses = dataobject.optString("total_classes");
                            Picasso.get().load(Constants_Urls.pic_base_url + user_image)
                                    .placeholder(R.drawable.user)
                                    .into(userimage);
                            Log.i("the image path is", "" + Constants_Urls.pic_base_url + user_image);
                            name.setText(firstname);
                            email.setText(usermail);
                            location.setText(userlocation);
                            //password.setText(userpswd);
                            phone.setText(userphone);
                            about.setText(aboutus);
                            ttlhrs.setText(kms + "Km");
                            ttldrvntme.setText(hours + "Hr");
                            tvttlcls.setText(ttlclasses);
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("User_pic", user_image);
                            editor.putString("usermail", usermail);
                            editor.commit();
                            editor.apply();

                            // System.out.println("Display_pic"+display_pic);

                            hud.dismiss();

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
