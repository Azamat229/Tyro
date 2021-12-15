package com.student.tyro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.Fragment.HomeFragment;
import com.student.tyro.Fragment.MyBookings;
import com.student.tyro.Fragment.UsersFragment;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    LinearLayout linear_bottom, linear_hme, linear_contact, linear_user;
    RelativeLayout main;
    ImageView navigation;
    String userid, username, useremail, userimg;
    ImageView imghme, imghme1, imgcon, imgcon1, imgusr, imgusr1, myprofile;
    TextView tvheading;
    ImageView imglogo;
    public RelativeLayout mDrawerList;
    public DrawerLayout mDrawerLayout;
    public FrameLayout frameLayout;
    ImageView img_chat, ivRight_editprofile;
    NetworkConnection networkConnection;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = this.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("User_id", "");
        username = sharedPreferences.getString("login_firstname", "");
        useremail = sharedPreferences.getString("login_email", "");
        userimg = sharedPreferences.getString("User_pic", "");
        networkConnection = new NetworkConnection(MainActivity.this);
        initUI();
        fragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mDrawerLayout.closeDrawer(mDrawerList);
        linear_bottom.setVisibility(View.VISIBLE);

    }

    private void initUI() {

        //slider
        frameLayout = findViewById(R.id.frameLayout);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        //Bottom tab
        linear_bottom = findViewById(R.id.llMenu);
        linear_hme = findViewById(R.id.llTabHome);
        linear_contact = findViewById(R.id.llTabContact);
        linear_user = findViewById(R.id.llTabUser);

        //main
        main = findViewById(R.id.rlHeading);
        navigation = findViewById(R.id.ivNavigation);
        linear_hme.setOnClickListener(this);
        linear_contact.setOnClickListener(this);
        linear_user.setOnClickListener(this);
        navigation.setOnClickListener(this);
        imghme = (ImageView) findViewById(R.id.hme);
        imghme1 = (ImageView) findViewById(R.id.hme1);
        imgcon = (ImageView) findViewById(R.id.contact);
        imgcon1 = (ImageView) findViewById(R.id.contact1);
        imgusr = (ImageView) findViewById(R.id.user);
        imgusr1 = (ImageView) findViewById(R.id.user1);
        myprofile = (ImageView) findViewById(R.id.my_profile_icon);
        tvheading = (TextView) findViewById(R.id.tv_heading);
        imglogo = findViewById(R.id.img_logo);
        img_chat = findViewById(R.id.ivRight_chat);
        ivRight_editprofile = findViewById(R.id.ivRight_editprofile);
        TextView txtusername = findViewById(R.id.text_user);
        TextView txtmail = findViewById(R.id.text_mail);
        ImageView prfile_pic = findViewById(R.id.my_profile_icon);
        LinearLayout llNavHome = findViewById(R.id.llNavHome);
        LinearLayout llNavMyClasses = findViewById(R.id.llNavMyClasses);
        LinearLayout llNavChat = findViewById(R.id.llNavChat);
        LinearLayout llNavMyProfile = findViewById(R.id.llNavMyProfile);
        LinearLayout llNavCards = findViewById(R.id.llNavCards);
        LinearLayout llNavNotification = findViewById(R.id.llNavNotifications);
        LinearLayout llNavFaq = findViewById(R.id.llNavFaq);
        LinearLayout llNavLogout = findViewById(R.id.llNavLogout);
        LinearLayout llNavReferral = findViewById(R.id.llNavReferral);
        txtusername.setText(username);
        txtmail.setText(useremail);
        Picasso.get().load(Constants_Urls.pic_base_url + userimg)
                .placeholder(R.drawable.user)
                .into(prfile_pic);
        llNavHome.setOnClickListener(this);
        llNavMyClasses.setOnClickListener(this);
        llNavChat.setOnClickListener(this);
        llNavMyProfile.setOnClickListener(this);
        llNavCards.setOnClickListener(this);
        llNavNotification.setOnClickListener(this);
        llNavFaq.setOnClickListener(this);
        llNavLogout.setOnClickListener(this);
        img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConversationsActivity.class));
            }
        });
        llNavReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReferalActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivNavigation:

                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {

                    mDrawerLayout.openDrawer(mDrawerList);
                }
                break;
            case R.id.llNavHome:
                linear_bottom.setVisibility(View.VISIBLE);
                ivRight_editprofile.setVisibility(View.GONE);
                img_chat.setVisibility(View.VISIBLE);
                tvheading.setVisibility(View.GONE);
                imglogo.setVisibility(View.VISIBLE);
                // tvheading.setText("Home");
                fragment = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case R.id.llNavMyClasses:
                main.setVisibility(View.VISIBLE);
                ivRight_editprofile.setVisibility(View.GONE);
                img_chat.setVisibility(View.VISIBLE);
                linear_bottom.setVisibility(View.VISIBLE);
                tvheading.setVisibility(View.VISIBLE);
                tvheading.setText("My Bookings");
                imglogo.setVisibility(View.GONE);
                fragment = new MyBookings();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case R.id.llNavMyProfile:
                main.setVisibility(View.VISIBLE);
                tvheading.setVisibility(View.VISIBLE);
                ivRight_editprofile.setVisibility(View.VISIBLE);
                img_chat.setVisibility(View.GONE);
                imglogo.setVisibility(View.GONE);
                tvheading.setText("My Profile");
                imgusr.setVisibility(View.GONE);
                imgusr1.setVisibility(View.VISIBLE);
                imgcon1.setVisibility(View.GONE);
                imgcon.setVisibility(View.VISIBLE);
                imghme.setVisibility(View.GONE);
                imghme1.setVisibility(View.VISIBLE);
                fragment = new UsersFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case R.id.llNavCards:
                startActivity(new Intent(MainActivity.this, MyCards.class));
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case R.id.llNavNotifications:
                startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case R.id.llNavChat:
                startActivity(new Intent(MainActivity.this, ConversationsActivity.class));
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case R.id.llNavFaq:
                startActivity(new Intent(MainActivity.this, FAQActivity.class));
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case R.id.llNavLogout:

                final DialogInterface.OnClickListener dialogClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

//                                        SharedPreferences sharedPreferences = getSharedPreferences("Login_details", 0);
//                                        SharedPreferences sharedPreferences1 = getSharedPreferences("student_details", 0);
//                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                                        editor.clear();
//                                        editor.commit();
//                                        editor.apply();
//                                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//                                        editor1.clear();
//                                        editor1.commit();
//                                        editor1.apply();
//                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(i);
//                                        finish();
                                        SignOut();

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        };
                dialog = new AlertDialog.Builder(MainActivity.this).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).create();
                dialog.setMessage("Do you want to logout?");
                dialog.show();

                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            //bottom
            case R.id.llTabHome:
                main.setVisibility(View.VISIBLE);
                tvheading.setVisibility(View.GONE);
                ivRight_editprofile.setVisibility(View.GONE);
                img_chat.setVisibility(View.VISIBLE);
                imgusr.setVisibility(View.VISIBLE);
                imgusr1.setVisibility(View.GONE);
                imgcon1.setVisibility(View.GONE);
                imgcon.setVisibility(View.VISIBLE);
                imghme.setVisibility(View.VISIBLE);
                imghme1.setVisibility(View.GONE);
                imglogo.setVisibility(View.VISIBLE);
                fragment = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case R.id.llTabContact:
                main.setVisibility(View.VISIBLE);
                tvheading.setVisibility(View.VISIBLE);
                ivRight_editprofile.setVisibility(View.GONE);
                img_chat.setVisibility(View.VISIBLE);
                tvheading.setText("My Bookings");
                imglogo.setVisibility(View.GONE);
                imgcon.setVisibility(View.GONE);
                imgcon1.setVisibility(View.VISIBLE);
                imgusr1.setVisibility(View.GONE);
                imgusr.setVisibility(View.VISIBLE);
                imghme.setVisibility(View.GONE);
                imghme1.setVisibility(View.VISIBLE);
                fragment = new MyBookings();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case R.id.llTabUser:
                main.setVisibility(View.VISIBLE);
                tvheading.setVisibility(View.VISIBLE);
                ivRight_editprofile.setVisibility(View.VISIBLE);
                img_chat.setVisibility(View.GONE);
                tvheading.setText("My Profile");
                imglogo.setVisibility(View.GONE);
                imgusr.setVisibility(View.GONE);
                imgusr1.setVisibility(View.VISIBLE);
                imgcon1.setVisibility(View.GONE);
                imgcon.setVisibility(View.VISIBLE);
                imghme.setVisibility(View.GONE);
                imghme1.setVisibility(View.VISIBLE);

                fragment = new UsersFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;

        }
    }

    private void SignOut() {

        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.signout(userid);
        final KProgressHUD hud = KProgressHUD.create(MainActivity.this)
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
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("Login_details", 0);
                            SharedPreferences sharedPreferences1 = getSharedPreferences("student_details", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            editor.apply();
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                            editor1.clear();
                            editor1.commit();
                            editor1.apply();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        } else if (status == 0) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    public void onBackPressed() {
        moveTaskToBack(true);
    }
}