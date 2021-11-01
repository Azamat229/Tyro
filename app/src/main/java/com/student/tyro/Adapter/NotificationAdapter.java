package com.student.tyro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.tyro.Model.NotificationModel;
import com.student.tyro.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    ArrayList<NotificationModel> notificationModels;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> notificationModels, Context context) {
        this.notificationModels = notificationModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notify_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tv_notication.setText(notificationModels.get(i).getMessage());
        viewHolder.txt_time.setText(notificationModels.get(i).getTime());

    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notication, txt_time,txt_date;
        RelativeLayout relative_head;
        ImageView red_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_notication = itemView.findViewById(R.id.notify_txt);
            txt_time = itemView.findViewById(R.id.notify_time);

        }
    }
}
