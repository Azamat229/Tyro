package com.student.tyro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.student.tyro.ChatActivity;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.R;
import com.student.tyro.Model.Conversation;

import java.util.ArrayList;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {
    private Context context;

    ArrayList<Conversation> conlist;
    String userid;

    public ConversationsAdapter(Context context, ArrayList<Conversation> msgs) {
        this.context = context;
        this.conlist = msgs;
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login_details", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("User_id", "");
    }

    public ConversationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ConversationsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_conversations, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ConversationsAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Conversation msgs = conlist.get(position);
        holder.tv_username.setText(msgs.getInstruct_firstname());
        holder.message.setText(msgs.getMessage());
      /*  Picasso.get().load(Constants_Urls.pic_base_url+msgs.getUpic())
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .into(holder.img);*/
        Picasso.get().load(Constants_Urls.pic_base_url + msgs.getInstruct_img())
                .placeholder(R.drawable.user)
                .into(holder.img);

        if (msgs.getRead_status() != null && msgs.getRead_status().equals("0")) {
            holder.read_status.setVisibility(View.VISIBLE);
        } else {
            holder.read_status.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("sender_id", userid);
                i.putExtra("receiver_id", msgs.getInstruct_id());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.

        private final TextView tv_username, message;
        private final ImageView img;
        private AppCompatImageView read_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            tv_username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message);
            img = itemView.findViewById(R.id.ivProfile);
            read_status = itemView.findViewById(R.id.read_status);
        }
    }
}

