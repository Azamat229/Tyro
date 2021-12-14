package com.student.tyro.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.R;
import com.student.tyro.Model.ChatModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatModel> chatModels;
    String User_id;
    LocalBroadcastManager localBroadcastManager;
    private static final Pattern UNICODE_HEX_PATTERN = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
    private static final Pattern UNICODE_OCT_PATTERN = Pattern.compile("\\\\([0-7]{3})");

    public ChatHistoryAdapter(Context applicationContext,  ArrayList<ChatModel> chatModels,String User_id ) {
        this.context = applicationContext;
        this.chatModels=chatModels;
        this.User_id=User_id;
    }
    @NonNull
    @Override
    public ChatHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_item, parent, false);
        return new ChatHistoryAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatHistoryAdapter.ViewHolder holder, final int position)
    {

        ChatModel chatModel=chatModels.get(position);
        String fileName =chatModels.get(position).getMsg();
        int i = fileName.lastIndexOf('.');
        String fileExtension = fileName.substring(i+1);
        Log.v("FILE EXTENSION: ", fileExtension);
        if(chatModels.get(position).getSenderid().equalsIgnoreCase(User_id)) {

           if (fileExtension.equals("jpg")  || fileExtension.equals("jpeg") || fileExtension.equals("png"))
           {
               holder.receiver_rl.setVisibility(View.GONE);
               holder.sender_rl.setVisibility(View.VISIBLE);
               holder.sender_image_msg.setVisibility(View.VISIBLE);
               holder.sender_txt_msg.setVisibility(View.GONE);
               Picasso.get().load(Constants_Urls.pic_base_url + chatModels.get(position).getMsg())
                       .placeholder(R.drawable.loader)
                       .into(holder.sender_image_msg);
               Picasso.get().load(Constants_Urls.pic_base_url + chatModels.get(position).getUserimg())
                       .placeholder(R.drawable.user)
                       .into(holder.sender_img);
                holder.sender_time.setText(chatModels.get(position).getUdatedat());

           }
           else {
               holder.receiver_rl.setVisibility(View.GONE);
               holder.sender_rl.setVisibility(View.VISIBLE);
               holder.sender_image_msg.setVisibility(View.GONE);
               holder.sender_txt_msg.setVisibility(View.VISIBLE);
               holder.sender_txt_msg.setText(chatModels.get(position).getMsg());
               holder.sender_time.setText(chatModels.get(position).getUdatedat());
               Picasso.get().load(Constants_Urls.pic_base_url+chatModels.get(position).getUserimg())
                       .placeholder(R.drawable.user)
                       .into(holder.sender_img);

           }
           }

           else if(fileExtension.equals("jpg")  || fileExtension.equals("jpeg") || fileExtension.equals("png")){

            holder.receiver_rl.setVisibility(View.VISIBLE);
            holder.sender_rl.setVisibility(View.GONE);
            holder.receiver_txt_msg.setVisibility(View.GONE);
            holder.receiver_image_receive.setVisibility(View.VISIBLE);
            Picasso.get().load(Constants_Urls.pic_base_url + chatModels.get(position).getMsg())
                   .placeholder(R.drawable.loader)
                   .into(holder.receiver_image_receive);
           Log.i("*** the image path  is",""+chatModels.get(position).getMsg());
           Picasso.get().load(Constants_Urls.pic_base_url+chatModels.get(position).getUserimg())
                   .placeholder(R.drawable.user)
                   .into(holder.receiver_img);
          holder.reciver_time.setText(chatModels.get(position).getUdatedat());
        }
           else
       {
           holder.sender_rl.setVisibility(View.GONE);
           holder.receiver_rl.setVisibility(View.VISIBLE);
           holder.receiver_image_receive.setVisibility(View.GONE);
           holder.receiver_txt_msg.setVisibility(View.VISIBLE);
           holder.receiver_txt_msg.setText(chatModels.get(position).getMsg());
           Log.i("*** the text  is",""+chatModels.get(position).getMsg());
           Picasso.get().load(Constants_Urls.pic_base_url+chatModels.get(position).getUserimg())
                   .placeholder(R.drawable.user)
                   .into(holder.receiver_img);
          holder.reciver_time.setText(chatModels.get(position).getUdatedat());
       }
        /*holder.receiver_image_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagepreviewDialog(chatModels.get(position).getMsg());
            }
        });
        holder.sender_image_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagepreviewDialog(chatModels.get(position).getMsg());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout receiver_rl,sender_rl;
        TextView receiver_txt_msg,sender_txt_msg,reciver_time,sender_time;
        ImageView receiver_img,sender_img,sender_image_msg,receiver_image_receive;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            receiver_rl=itemView.findViewById(R.id.receiver_rl);
            sender_rl=itemView.findViewById(R.id.sender_rl);
            receiver_txt_msg=itemView.findViewById(R.id.receiver_txt_msg);
            sender_txt_msg=itemView.findViewById(R.id.sender_txt_msg);
            receiver_img=itemView.findViewById(R.id.receiver_img);
            sender_img=itemView.findViewById(R.id.sender_img);
            sender_image_msg=itemView.findViewById(R.id.sender_sent_img);
            receiver_image_receive=itemView.findViewById(R.id.receiver_received_img);
            reciver_time=itemView.findViewById(R.id.receiver_txt_time);
            sender_time=itemView.findViewById(R.id.sender_txt_time);
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

    private void imagepreviewDialog(String message) {
        LinearLayout linear;
        ImageView imagepreview, cancel_image;

        Dialog dialog = new Dialog(context, R.style.MyAlertDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.imagepreview_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_background);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        imagepreview = dialog.findViewById(R.id.imagepreview);
        linear = dialog.findViewById(R.id.linear);
        cancel_image = dialog.findViewById(R.id.cancel_image);

        linear.setVisibility(View.GONE);
        cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Picasso.get().load(Constants_Urls.pic_base_url.toString() + message)
                .placeholder(R.drawable.loader)
                .into(imagepreview);
    }

}
