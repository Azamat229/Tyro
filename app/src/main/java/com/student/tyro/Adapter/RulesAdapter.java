package com.student.tyro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.ApiCallInterface;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.R;
import com.student.tyro.Model.Covid_Rules;
import com.student.tyro.Model.Covid_msgs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Covid_Rules> ruleslist;
    NetworkConnection networkConnection;
    ArrayList<Covid_msgs> msgs;
    boolean buttonOn1;
    public RulesAdapter(Context context, ArrayList<Covid_Rules> rules) {
        this.context = context;
        this.ruleslist = rules;
    }

    public RulesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new RulesAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.rules_list, parent, false));

    }
    @Override
    public void onBindViewHolder(@NonNull RulesAdapter.ViewHolder holder, int position)
    {
        // setting data to our text views from our modal class.
        Covid_Rules rule = ruleslist.get(position);
        holder.catque.setText(rule.getMsg());
        networkConnection = new NetworkConnection(context);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = rule.getId();
                if (!buttonOn1) {
                    buttonOn1 = true;
                    holder.img.setImageResource(0);
                    holder.img.setBackground(context.getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
                    holder.recycle.setVisibility(View.VISIBLE);
                    if (networkConnection.isConnectingToInternet()) {
                        ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
                        Call<JsonElement> call = apiClass.get_covid_list(id);
                        final KProgressHUD hud = KProgressHUD.create(context)
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
                                            String get_timeslots = jsonObject.optString("data");
                                            JSONArray jsonArray = new JSONArray(get_timeslots);
                                            System.out.println("slot_data is"+jsonArray);
                                            GridLayoutManager gridLayoutManager=new GridLayoutManager(context,1);
                                            holder.recycle.setLayoutManager(gridLayoutManager);
                                            msgs = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String id=jsonObject1.getString("id");
                                                String cat_id=jsonObject1.getString("cat_id");
                                                String message=jsonObject1.getString("message");
                                                String sts=jsonObject1.getString("status");
                                                String created=jsonObject1.getString("created_on");
                                                //holder.tv_message.setVisibility(View.VISIBLE);
                                                // holder.tv_message.setText(message);
                                                msgs.add(new Covid_msgs(id, cat_id, message, sts,created));
                                            }
                                            holder.recycle.setAdapter(new CovidMessageAdapter(context,msgs));
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
                    else {
                        Toast.makeText(context, context.getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    buttonOn1 = false;
                    holder.img.setImageResource(0);
                    holder.img.setBackground(context.getDrawable(R.drawable.ic_baseline_add_circle_outline_24));
                    holder.recycle.setVisibility(View.GONE);
                }
                // Toast.makeText(context,""+id,Toast.LENGTH_LONG).show();
               /* */
            }
        });
        }



    @Override
    public int getItemCount() {
        return ruleslist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.

        private final TextView catque;
        private final ImageView img;
        private final RecyclerView recycle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            catque=itemView.findViewById(R.id.tv_que);
            img=itemView.findViewById(R.id.img_open);
            recycle=itemView.findViewById(R.id.cvd_messages_recycle);
        }
    }
}
