package com.student.tyro.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.student.tyro.Adapter.UpcomingAdapter;
import com.student.tyro.Model.Comleted_Booking;
import com.student.tyro.Model.ReportcardModel;
import com.student.tyro.Model.Reportcardsublist;
import com.student.tyro.PerformanceActivity;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.R;
import com.student.tyro.Model.Lessions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookings extends Fragment {

    LinearLayout linear_complted, linear_upcmg;
    View view_cmptd, view_upcmg;
    TextView tv_cmptd, tv_upcmg;
    RecyclerView recycle_cmplted, recycle_upcmg;
    NetworkConnection networkConnection;
    ArrayList<Lessions> lessionslist;
    ArrayList<Comleted_Booking> complete;
    String userid;
    LinearLayout linearnoclasses;
    TextView tvnoclasses;
    CompletedAdapter compltedadapter;

    @Override
    public void onResume() {
        super.onResume();
        if (view_cmptd.isSelected()) {
            completed_bookings();
        } else if (view_upcmg.isSelected()) {
            upcoming_bookings();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_contacts, container, false);
        networkConnection = new NetworkConnection(getActivity());
        linear_complted = rootView.findViewById(R.id.llTabCompleted);
        linear_upcmg = rootView.findViewById(R.id.llTabUpcoming);
        view_cmptd = rootView.findViewById(R.id.viewPayout);
        view_upcmg = rootView.findViewById(R.id.viewRemainingPayout);
        tv_cmptd = rootView.findViewById(R.id.tvPayout);
        tv_upcmg = rootView.findViewById(R.id.tvRemainingPayout);
        recycle_cmplted = rootView.findViewById(R.id.recycle_compled);
        recycle_cmplted.setVisibility(View.VISIBLE);
        linearnoclasses = rootView.findViewById(R.id.linear_noclasses);
        tvnoclasses = rootView.findViewById(R.id.tv_noclasses);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("User_id", "");
        if (networkConnection.isConnectingToInternet()) {
            completed_bookings();
        } else {
            Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
        }
        recycle_upcmg = rootView.findViewById(R.id.recycle_upcmg);
        view_cmptd.setSelected(true);
        view_upcmg.setSelected(false);
        tv_cmptd.setTextColor(getResources().getColor(R.color.colorYellow));
        tv_upcmg.setTextColor(getResources().getColor(R.color.colorWhite));
        linear_complted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_cmptd.setSelected(true);
                view_upcmg.setSelected(false);
                tv_cmptd.setTextColor(getResources().getColor(R.color.colorYellow));
                tv_upcmg.setTextColor(getResources().getColor(R.color.colorWhite));
                recycle_cmplted.setVisibility(View.VISIBLE);
                recycle_upcmg.setVisibility(View.GONE);
                linearnoclasses.setVisibility(View.GONE);
                if (networkConnection.isConnectingToInternet()) {
                    completed_bookings();
                } else {
                    Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        linear_upcmg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_upcmg.setSelected(true);
                view_cmptd.setSelected(false);
                tv_upcmg.setTextColor(getResources().getColor(R.color.colorYellow));
                tv_cmptd.setTextColor(getResources().getColor(R.color.colorWhite));
                recycle_upcmg.setVisibility(View.VISIBLE);
                recycle_cmplted.setVisibility(View.GONE);
                linearnoclasses.setVisibility(View.GONE);
                if (networkConnection.isConnectingToInternet()) {
                    upcoming_bookings();
                } else {
                    Toast.makeText(getActivity(), getResources().getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    //cmompleted bookings
    public void completed_bookings() {
        //RequestBody r_userid = RequestBody.create(MediaType.parse("multipart/form-data"), "211");
        RequestBody r_userid = RequestBody.create(MediaType.parse("multipart/form-data"), userid);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.Completed_classes(r_userid);
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
                            String data = jsonObject.optString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            System.out.println("array is" + jsonArray);
                            if (jsonArray.length() > 0) {
                                complete = new ArrayList<>();
                                // GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
                                //recycler_upcmg.setLayoutManager(gridLayoutManager);
                                // linerupcmg.setVisibility(View.VISIBLE);
                                linearnoclasses.setVisibility(View.GONE);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                                recycle_cmplted.setLayoutManager(gridLayoutManager);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String id = jsonObject1.getString("id");
                                    String sid = jsonObject1.getString("student_id");
                                    String insid = jsonObject1.getString("instructor_id");
                                    String instructor_name = jsonObject1.getString("instructor_name");
                                    String language = jsonObject1.getString("language");
                                    String profile_pic = jsonObject1.getString("profile_pic");
                                    String unique_id = jsonObject1.getString("unique_id");
                                    String location = jsonObject1.getString("location");
                                    String strtme = jsonObject1.getString("start_time");
                                    String endtme = jsonObject1.getString("end_time");
                                    String booking_date = jsonObject1.getString("booking_date");
                                    String booking_hours = jsonObject1.getString("booking_hours");
                                    String start_latitude = jsonObject1.getString("start_latitude");
                                    String start_longitude = jsonObject1.getString("start_longitude");
                                    String end_latitude = jsonObject1.getString("end_latitude");
                                    String end_longitude = jsonObject1.getString("end_longitude");
                                    String total_travelled = jsonObject1.getString("total_travelled");
                                    String speed = jsonObject1.getString("speed");
                                    String total_time = jsonObject1.getString("total_time");
                                    String avg_rating = jsonObject1.getString("avg_rating");
                                    String amount = jsonObject1.getString("amount");
                                    String sts = jsonObject1.getString("status");
                                    String ratingStatus = jsonObject1.getString("ratingStatus");
                                    complete.add(new Comleted_Booking(id, sid, insid, instructor_name, language, profile_pic, unique_id, location, strtme, endtme, booking_date, booking_hours, start_latitude,
                                            start_longitude, end_latitude, end_longitude, total_travelled, speed, total_time, avg_rating, amount, sts,
                                            ratingStatus));
                                    Log.i("the catagory data is", "" + complete);

                                }
                                //  recycle_cmplted.setAdapter(new CompletedAdapter(getActivity(),complete));
                                compltedadapter = new CompletedAdapter(getActivity(), complete);
                                //compltedadapter.notifyDataSetChanged();
                                recycle_cmplted.setAdapter(compltedadapter);
                            } else {
                                recycle_cmplted.setVisibility(View.GONE);
                                linearnoclasses.setVisibility(View.VISIBLE);
                                tvnoclasses.setText("No classes available");
                            }
                        } else if (status == 0) {
                            // linerupcmg.setVisibility(View.GONE);
                            //Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            recycle_cmplted.setVisibility(View.GONE);
                            linearnoclasses.setVisibility(View.VISIBLE);
                            tvnoclasses.setText("No classes available");
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
                hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });
    }

    //upcoming bookings
    private void upcoming_bookings() {
        RequestBody r_userid = RequestBody.create(MediaType.parse("multipart/form-data"), userid);
        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
        Call<JsonElement> call = apiClass.Upcoming_classes(r_userid);
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

                            String data = jsonObject.optString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            System.out.println("array is" + jsonArray);
                            if (jsonArray.length() > 0) {
                                lessionslist = new ArrayList<>();
                                // GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
                                //recycler_upcmg.setLayoutManager(gridLayoutManager);
                                // linerupcmg.setVisibility(View.VISIBLE);
                                linearnoclasses.setVisibility(View.GONE);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                                recycle_upcmg.setLayoutManager(gridLayoutManager);
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
                                    lessionslist.add(new Lessions(id, sid, insid, strtme, endtme, booking_date,
                                            booking_hours, username, profile_pic, location, price, uniqueid,
                                            sts, payment_type, cancel, status1, update_latitude, update_longitude));
                                    Log.i("the catagory data is", "" + lessionslist);

                                }
                                recycle_upcmg.setAdapter(new UpcomingAdapter(getActivity(), lessionslist));
                            } else {
                                recycle_upcmg.setVisibility(View.GONE);
                                linearnoclasses.setVisibility(View.VISIBLE);
                                tvnoclasses.setText("No classes available");
                            }
                        } else {
                            recycle_upcmg.setVisibility(View.GONE);
                            linearnoclasses.setVisibility(View.VISIBLE);
                            tvnoclasses.setText("No classes available");
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
                hud.dismiss();
                Log.e("sdfdsd ", t.toString());
            }
        });

    }

    public class CompletedAdapter extends RecyclerView.Adapter<CompletedAdapter.ViewHolder> {
        private Context context;
        private ArrayList<Comleted_Booking> lessionsModalArrayList;
        NetworkConnection networkConnection;
        String str_review, str_rating;
        String studentid, instid, bookingid;
        ArrayList<ReportcardModel> reportcardlist;
        ArrayList<Reportcardsublist> reportsubcardlist;
        private ArrayList<Comleted_Booking> initialData;

        public CompletedAdapter(Context applicationContext, ArrayList<Comleted_Booking> lessionslist) {
            this.context = applicationContext;
            this.lessionsModalArrayList = lessionslist;
            networkConnection = new NetworkConnection(context);
        }

        @Override
        public CompletedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CompletedAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_completed, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CompletedAdapter.ViewHolder holder, int position) {
            // setting data to our text views from our modal class.
            Comleted_Booking completed = lessionsModalArrayList.get(position);
            Log.i("array", "" + completed);
            holder.class_id.setText(completed.getUid());
            holder.name.setText(completed.getInstname());
            holder.location.setText(completed.getLocation());
            holder.date.setText(completed.getBookdate());
            holder.tme.setText(completed.getStrt_tme());
            holder.hrs.setText(completed.getBookhrs() + "hr");
            holder.amount.setText("$" + completed.getAmount());
            Picasso.get().load(Constants_Urls.pic_base_url + completed.getPic())
                    .placeholder(R.drawable.user)
                    .into(holder.img_icon);

            holder.ratng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (completed.getRatingStatus() != null && completed.getRatingStatus().equals("1")) {
//                        Toast.makeText(context, "Rating already given", Toast.LENGTH_SHORT).show();
                    } else {
                        final Dialog dialog = new Dialog(context, R.style.MyAlertDialogTheme);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.activity_rating);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_background);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        dialog.show();
                        ImageView rat_img = dialog.findViewById(R.id.rating_user_icon);
                        TextView rat_username = dialog.findViewById(R.id.rating_user_name);
                        TextView rat_date = dialog.findViewById(R.id.rating_user_date);
                        TextView rat_time = dialog.findViewById(R.id.rating_user_time);
                        TextView rat_hr = dialog.findViewById(R.id.rating_user_hr);
                        ImageView close = dialog.findViewById(R.id.ivClose);
                        RatingBar rating = dialog.findViewById(R.id.rating);
                        Button submit = dialog.findViewById(R.id.btnSubmit);
                        EditText edit_review = dialog.findViewById(R.id.edit_review);
                        str_review = edit_review.getText().toString();
                        Picasso.get().load(Constants_Urls.pic_base_url + completed.getPic())
                                .placeholder(R.drawable.user)
                                .into(rat_img);
                        rat_username.setText(completed.getInstname());
                        rat_date.setText(completed.getBookdate());
                        rat_time.setText(completed.getStrt_tme());
                        rat_hr.setText(completed.getBookhrs() + "hr");
                        studentid = completed.getSid();
                        instid = completed.getInstid();
                        bookingid = completed.getId();
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            public void onRatingChanged(RatingBar ratingBar, float rating,
                                                        boolean fromUser) {
                                str_rating = String.valueOf(rating);
                                //    txtRatingValue.setText(String.valueOf(rating));
                                // Toast.makeText(context,""+String.valueOf(rating), Toast.LENGTH_LONG).show();

                            }
                        });
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (networkConnection.isConnectingToInternet()) {
                                    ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
                                    Call<JsonElement> call = apiClass.Givereviewtoinstructor(studentid, instid, bookingid, str_rating, str_review);
                                    Log.e("review", studentid+" "+instid+" "+bookingid+" "+str_rating+" "+str_review);

                                    final KProgressHUD hud = KProgressHUD.create(context)
                                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                            .setBackgroundColor(R.color.colorPrimary)
                                            .show();

                                    call.enqueue(new Callback<JsonElement>() {
                                        @Override
                                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                            hud.dismiss();
                                            dialog.dismiss();

                                            if (response.isSuccessful()) {
                                                Log.e("my_bookings", response.body().toString());
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                                    int status = jsonObject.getInt("status");
                                                    if (status == 1) {
                                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        completed_bookings();
                                                    } else if (status == 0) {
                                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(context, context.getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            });

            holder.performance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PerformanceActivity.class);
                    i.putExtra("studentid", completed.getSid());
                    i.putExtra("instid", completed.getInstid());
                    i.putExtra("instname", completed.getInstname());
                    i.putExtra("instprofilepic", completed.getPic());
                    i.putExtra("instlang", completed.getLanguage());
                    i.putExtra("instrating", completed.getAvg_rate());
                    i.putExtra("instlocaton", completed.getLocation());
                    i.putExtra("insttime", completed.getStrt_tme());
                    i.putExtra("endtime", completed.getEnd_tme());
                    i.putExtra("instdate", completed.getBookdate());
                    i.putExtra("insthrs", completed.getBookhrs());
                    i.putExtra("strt_lat", completed.getStart_lat());
                    i.putExtra("strt_long", completed.getStart_long());
                    i.putExtra("end_lat", completed.getEnd_lat());
                    i.putExtra("end_long", completed.getEnd_long());
                    i.putExtra("instttltraveled", completed.getTotal_travel());
                    i.putExtra("speed", completed.getSpped());
                    i.putExtra("totaltime", completed.getTotal_tme());
                    i.putExtra("amount", completed.getAmount());
                    i.putExtra("completed_id", completed.getId());
                    context.startActivity(i);
                }
            });


        }

        @Override
        public int getItemCount() {
            return lessionsModalArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // creating variables for our text views.
            private final TextView name, location, date, tme, hrs, ratng, performance, amount, class_id;
            ImageView img_icon;
            CardView card_rating;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                // initializing our text views.
                name = itemView.findViewById(R.id.cmpltd_username);
                location = itemView.findViewById(R.id.cmpltd_location);
                date = itemView.findViewById(R.id.cmpltd_date);
                tme = itemView.findViewById(R.id.cmpltd_tme);
                hrs = itemView.findViewById(R.id.cmpltd_tmehrs);
                img_icon = itemView.findViewById(R.id.cmpltd_usr_icon);
                ratng = itemView.findViewById(R.id.tvRating);
                performance = itemView.findViewById(R.id.tvPerformance);
                card_rating = itemView.findViewById(R.id.cvRatings);
                amount = itemView.findViewById(R.id.tv_amt);
                class_id = itemView.findViewById(R.id.class_id);
            }
        }

    }
}
