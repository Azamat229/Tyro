package com.student.tyro.Model;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.Util.Retrofit_Class;
import com.student.tyro.R;
import com.student.tyro.Util.ApiCallInterface;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCardsAdapter extends RecyclerView.Adapter<MyCardsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SavedCardModel> cardDetailsList;
    int pos=-1;
    NetworkConnection networkConnection;
    // creating a constructor class.
    public MyCardsAdapter(Context context, ArrayList<SavedCardModel> cardlist) {
        this.context = context;
        this.cardDetailsList = cardlist;
        networkConnection = new NetworkConnection(context);
    }
    @NonNull
    @Override
    public MyCardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new MyCardsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.saved_card_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCardsAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        SavedCardModel savedcard = cardDetailsList.get(position);
        holder.selected_card.setColorFilter(context.getResources().getColor(R.color.colorBlack));
        String string = cardDetailsList.get(position).getCard_number();

        if (string.length() > 4) {
            string = string.substring(string.length() - 4);
        }
       holder.cardNumber.setText("****  ****  ****  " + string);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;
                final Dialog dialog = new Dialog(context, R.style.MyAlertDialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.delete_saved_card);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_background);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();
                Button yes=dialog.findViewById(R.id.btnyes);
                Button no=dialog.findViewById(R.id.btnno);
                ImageView imgclose=dialog.findViewById(R.id.cancel_image);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            {
                                if (networkConnection.isConnectingToInternet()) {
                                    ApiCallInterface apiClass = Retrofit_Class.getClient().create(ApiCallInterface.class);
                                    Call<JsonElement> call = apiClass.delecard(cardDetailsList.get(position).getId());
                                    final KProgressHUD hud = KProgressHUD.create(context)
                                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                            .setBackgroundColor(R.color.colorPrimary)
                                            .show();
                                    call.enqueue(new Callback<JsonElement>() {
                                        @Override
                                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                            if (response.isSuccessful()) {
                                                try {
                                                    Log.e("jvcnxcvb ", response.body().toString());
                                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                                    String status = jsonObject.getString("status");

                                                    if (status.equals("1"))
                                                    {
                                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        final int getAdapter_position  = holder.getAdapterPosition();
                                                        remove_item(getAdapter_position);
                                                        dialog.dismiss();
                                                    }   else if(status.equals("0"))
                                                    {
                                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                                else {
                                    Toast.makeText(context, context.getText(R.string.connecttointernet), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    imgclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                     }
            private void remove_item(int getAdapter_position) {
                cardDetailsList.remove(getAdapter_position);
                notifyItemRemoved(getAdapter_position);
                notifyItemRangeChanged(getAdapter_position,cardDetailsList.size());
                notifyDataSetChanged();
            }
        }); }

    @Override
    public int getItemCount() {
        return cardDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
       TextView cardNumber;
        ImageView selected_card;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
              cardNumber = itemView.findViewById(R.id.textCardNumber);
              selected_card = itemView.findViewById(R.id.arrow);
              relativeLayout=itemView.findViewById(R.id.rrl);
        }
    }
}


