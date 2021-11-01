package com.student.tyro.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.student.tyro.Exampletimeslot;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.R;
import com.student.tyro.Model.Lessions;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Lessions> lessionsModalArrayList;
    NetworkConnection networkConnection;
    String studentid = " ";
    String instructid = " ";
    String id = " ";
    String reason;

    public UpcomingAdapter(Context applicationContext, ArrayList<Lessions> lessionslist) {
        this.context = applicationContext;
        this.lessionsModalArrayList = lessionslist;
        networkConnection = new NetworkConnection(context);
    }

    @Override
    public UpcomingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UpcomingAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_upcoming, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Lessions courses = lessionsModalArrayList.get(position);
        Log.i("array", "" + courses);
        holder.name.setText(courses.getUsername());
        holder.location.setText(courses.getLocation());
        holder.date.setText(courses.getBookdate());
        holder.tme.setText(courses.getStrt_tme());
        holder.hrs.setText(courses.getBookhrs()+"hr");
        holder.id.setText("#" + courses.getUniquid());
        holder.price.setText("$" + courses.getPrce());
        instructid = lessionsModalArrayList.get(position).getInstid();

        if (courses.getStatus() != null && courses.getStatus().equals("3")) {
            holder.cancel.setVisibility(View.GONE);
            holder.reschedule.setVisibility(View.GONE);
            holder.share_location.setVisibility(View.VISIBLE);
            holder.inprogress.setVisibility(View.VISIBLE);
//            holder.inprogress.setAnimation(AnimationUtils.loadAnimation(context,
//                    R.anim.blink));

        } else {
            holder.cancel.setVisibility(View.VISIBLE);
            holder.reschedule.setVisibility(View.VISIBLE);
            holder.share_location.setVisibility(View.GONE);
            holder.inprogress.setVisibility(View.GONE);
        }
        Picasso.get().load(Constants_Urls.pic_base_url + courses.getUser_image())
                .placeholder(R.drawable.user)
                .into(holder.img_icon);

      /*  if(courses.getSts().equals("4"))
        {
            holder.line_show.setVisibility(View.GONE);
            holder.line_cancel.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.line_show.setVisibility(View.VISIBLE);
            holder.line_cancel.setVisibility(View.GONE);
        }*/

        holder.share_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "https://maps.google.com/?q=" + courses.getUpdate_latitude() + "," + courses.getUpdate_longitude());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.activity_cancel_reason);
                dialog.show();
                EditText etbox = dialog.findViewById(R.id.et_reason);
                Button btnsubmit = dialog.findViewById(R.id.btn_reason_submit);
                ImageView close = dialog.findViewById(R.id.img_cls);
                id = lessionsModalArrayList.get(position).getId();
                studentid = lessionsModalArrayList.get(position).getSid();
                Log.i("cancel ****", "" + studentid);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //cancel upcoming classes
                btnsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reason = etbox.getText().toString();
                        if (!reason.isEmpty()) {

                            if (networkConnection.isConnectingToInternet()) {

                                ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
                                Call<JsonElement> call = apiClass.cancel_upcomingclass(studentid, id, reason, "1");
                                Log.i("cancel reason", "" + studentid + reason);
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
                                            Log.e("spprofileee", response.body().toString());
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                                int status = jsonObject.getInt("status");
                                                if (status == 1) {
                                                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                    //holder.line_show.setVisibility(View.GONE);
                                                    // holder.line_cancel.setVisibility(View.VISIBLE);
                                                  /*  lessionsModalArrayList.remove(position);
                                                    notifyDataSetChanged();

*/
                                                    final int getAdapter_position = holder.getAdapterPosition();
                                                    remove_item(getAdapter_position);

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
                        } else {
                            Toast.makeText(context, "Please enter reason", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        holder.reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Exampletimeslot.class);
                i.putExtra("id", courses.getId());
                i.putExtra("instruct_id", courses.getInstid());
                i.putExtra("booking_hrs", courses.getBookhrs());
                i.putExtra("payment_type", courses.getPaytype());
                context.startActivity(i);
            }
        });
    }

    private void remove_item(int getAdapter_position) {
        lessionsModalArrayList.remove(getAdapter_position);
        notifyItemRemoved(getAdapter_position);
        notifyItemRangeChanged(getAdapter_position, lessionsModalArrayList.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lessionsModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView name, location, date, tme, hrs, cancel, reschedule, price, id,
                share_location, inprogress;
        ImageView img_icon;
        LinearLayout line_cancel, line_show;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            name = itemView.findViewById(R.id.upcmg_username);
            location = itemView.findViewById(R.id.upcmg_location);
            date = itemView.findViewById(R.id.upcmg_date);
            tme = itemView.findViewById(R.id.upcmg_tme);
            hrs = itemView.findViewById(R.id.upcmg_tmehrs);
            img_icon = itemView.findViewById(R.id.upcmg_usr_icon);
            cancel = itemView.findViewById(R.id.tvCancel);
            reschedule = itemView.findViewById(R.id.tvReschedule);
            price = itemView.findViewById(R.id.upcmg_prce);
            id = itemView.findViewById(R.id.upcmg_uniqc_id);
            line_cancel = itemView.findViewById(R.id.linear_cancld);
            line_show = itemView.findViewById(R.id.linear_bth);
            share_location = itemView.findViewById(R.id.tvshare);
            inprogress = itemView.findViewById(R.id.tvinprogress);
        }
    }
}
