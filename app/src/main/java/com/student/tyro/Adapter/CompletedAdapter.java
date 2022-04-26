package com.student.tyro.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.student.tyro.Fragment.MyBookings;
import com.student.tyro.Model.Comleted_Booking;
import com.student.tyro.Model.ReportcardModel;
import com.student.tyro.Model.Reportcardsublist;
import com.student.tyro.StripePayment;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.R;
import com.student.tyro.PerformanceActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

//            holder.ratng.setOnClickListener(new View.OnClickListener() {
//            @Override
//              public void onClick(View v) {
//                Toast.makeText(context,"Rating was presed", Toast.LENGTH_SHORT).show();
//
//                if (completed.getRatingStatus() != null && completed.getRatingStatus().equals("1")) {
//                    Toast.makeText(context, "Rating already given", Toast.LENGTH_SHORT).show();
//                } else {
//                    final Dialog dialog = new Dialog(context, R.style.MyAlertDialogTheme);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.activity_rating);
//                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_background);
//                    dialog.setCanceledOnTouchOutside(true);
//                    dialog.setCancelable(true);
//                    dialog.show();
//                    ImageView rat_img = dialog.findViewById(R.id.rating_user_icon);
//                    TextView rat_username = dialog.findViewById(R.id.rating_user_name);
//                    TextView rat_date = dialog.findViewById(R.id.rating_user_date);
//                    TextView rat_time = dialog.findViewById(R.id.rating_user_time);
//                    TextView rat_hr = dialog.findViewById(R.id.rating_user_hr);
//                    ImageView close = dialog.findViewById(R.id.ivClose);
//                    RatingBar rating = dialog.findViewById(R.id.rating);
//                    Button submit = dialog.findViewById(R.id.btnSubmit);
//                    EditText edit_review = dialog.findViewById(R.id.edit_review);
//                    str_review = edit_review.getText().toString();
//                    Picasso.get().load(Constants_Urls.pic_base_url + completed.getPic())
//                            .placeholder(R.drawable.user)
//                            .into(rat_img);
//                    rat_username.setText(completed.getInstname());
//                    rat_date.setText(completed.getBookdate());
//                    rat_time.setText(completed.getStrt_tme());
//                    rat_hr.setText(completed.getBookhrs() + "hr");
//                    studentid = completed.getSid();
//                    instid = completed.getInstid();
//                    bookingid = completed.getId();
//                    close.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
//                    rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                        public void onRatingChanged(RatingBar ratingBar, float rating,
//                                                    boolean fromUser) {
//                            str_rating = String.valueOf(rating);
//                            //    txtRatingValue.setText(String.valueOf(rating));
//                            // Toast.makeText(context,""+String.valueOf(rating), Toast.LENGTH_LONG).show();
//
//
//                        }
//                    });
//                    submit.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(context, "sabmit was prested", Toast.LENGTH_SHORT).show();
//
//                            if (networkConnection.isConnectingToInternet()) {
//                                ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
//                                Call<JsonElement> call = apiClass.set_review_to_instructor(studentid, instid, bookingid, str_rating, str_review,"1");
//
//                                final KProgressHUD hud = KProgressHUD.create(context)
//                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                                        .setBackgroundColor(R.color.colorPrimary)
//                                        .show();
//                                call.enqueue(new Callback<JsonElement>() {
//                                    @Override
//                                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                                        hud.dismiss();
//                                        dialog.dismiss();// Azamat
//
//
//                                        if (response.isSuccessful()) {
//                                            Log.e("spprofileee", response.body().toString());
//                                            try {
//                                                JSONObject jsonObject = new JSONObject(response.body().toString());
//                                                int status = jsonObject.getInt("status");
//                                                if (status == 1) {
//                                                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                                    dialog.dismiss();
//                                                } else if (status == 0) {
//                                                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                                }
//
//                                            } catch (Exception e) {
//                                                hud.dismiss();
//                                                e.printStackTrace();
//                                                Log.e("dskfsdf ", e.toString());
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<JsonElement> call, Throwable t) {
//                                        hud.dismiss();
//                                        Log.e("sdfdsd ", t.toString());
//                                    }
//                                });
//
//                            } else {
//                                Toast.makeText(context, context.getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//                }
//            }
//        });

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
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