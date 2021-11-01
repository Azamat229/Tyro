package com.student.tyro.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.tyro.R;
import com.student.tyro.Model.Covid_msgs;

import java.util.ArrayList;

public class CovidMessageAdapter extends RecyclerView.Adapter<CovidMessageAdapter.ViewHolder>{
    private Context context;

    ArrayList<Covid_msgs> msgslist;
    public CovidMessageAdapter(Context context, ArrayList<Covid_msgs> msgs) {
        this.context = context;
        this.msgslist = msgs;
    }

    public CovidMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new CovidMessageAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.covid_msgslist, parent, false));

    }
    @Override
    public void onBindViewHolder(@NonNull CovidMessageAdapter.ViewHolder holder, int position)
    {
        // setting data to our text views from our modal class.
        Covid_msgs msgs = msgslist.get(position);
        holder.msg.setText(Html.fromHtml(msgs.getMsgs()));

    }

    @Override
    public int getItemCount() {
        return msgslist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.

        private final TextView msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            msg=itemView.findViewById(R.id.tv_msg);
        }
    }
}