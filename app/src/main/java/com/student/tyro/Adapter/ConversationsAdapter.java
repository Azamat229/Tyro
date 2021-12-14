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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {
    private Context context;

    ArrayList<Conversation> conlist;
    String userid;
    private static final Pattern UNICODE_HEX_PATTERN = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
    private static final Pattern UNICODE_OCT_PATTERN = Pattern.compile("\\\\([0-7]{3})");

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

        if (msgs.getMessage() != null) {
            String fileName = msgs.getMessage();
            int i = fileName.lastIndexOf('.');
            String fileExtension = fileName.substring(i + 1);

            if (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png")) {
//                Picasso.get().load(Constants_Urls.pic_base_url + msgs.getMessage())
//                        .placeholder(R.drawable.loader)
//                        .into(holder.image);
                holder.image.setVisibility(View.VISIBLE);
                holder.message.setVisibility(View.VISIBLE);
                holder.message.setText("Photo");
            } else {
                holder.message.setText(msgs.getMessage());
                holder.image.setVisibility(View.GONE);
                holder.message.setVisibility(View.VISIBLE);
            }
        }

        if (msgs.getMessage().equals("null")) {
            holder.message.setVisibility(View.GONE);
        }

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
        private final ImageView img, image;
        private AppCompatImageView read_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            tv_username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message);
            img = itemView.findViewById(R.id.ivProfile);
            read_status = itemView.findViewById(R.id.read_status);
            image = itemView.findViewById(R.id.image);
        }
    }

    public static String decodeFromNonLossyAscii(String original) {
        Matcher matcher = UNICODE_HEX_PATTERN.matcher(original);
        StringBuffer charBuffer = new StringBuffer(original.length());
        while (matcher.find()) {
            String match = matcher.group(1);
            char unicodeChar = (char) Integer.parseInt(match, 16);
            matcher.appendReplacement(charBuffer, Character.toString(unicodeChar));
        }
        matcher.appendTail(charBuffer);
        String parsedUnicode = charBuffer.toString();

        matcher = UNICODE_OCT_PATTERN.matcher(parsedUnicode);
        charBuffer = new StringBuffer(parsedUnicode.length());
        while (matcher.find()) {
            String match = matcher.group(1);
            char unicodeChar = (char) Integer.parseInt(match, 8);
            matcher.appendReplacement(charBuffer, Character.toString(unicodeChar));
        }
        matcher.appendTail(charBuffer);
        return charBuffer.toString();
    }


    public static String decodeEmoji(String message) {
        String myString = null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

}

