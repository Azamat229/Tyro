package com.student.tyro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.R;
import com.student.tyro.Model.Lessions;

import java.util.ArrayList;

public class UpcmgLessonAdapter extends RecyclerView.Adapter<UpcmgLessonAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Lessions> lessionsModalArrayList;

    public UpcmgLessonAdapter(Context applicationContext, ArrayList<Lessions> lessionslist) {
        this.context = applicationContext;
        this.lessionsModalArrayList = lessionslist;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UpcmgLessonAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.up_cmng_lessonlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UpcmgLessonAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Lessions courses = lessionsModalArrayList.get(position);
        Log.i("array", "" + courses);
        holder.name.setText(courses.getUsername());
        holder.location.setText(courses.getLocation());
        holder.date.setText(courses.getBookdate());
        holder.tme.setText(courses.getStrt_tme());
        holder.hrs.setText(courses.getBookhrs() + "hr");
        Picasso.get().load(Constants_Urls.pic_base_url + courses.getUser_image())
                .placeholder(R.drawable.user)
                .into(holder.img_icon);
    }

    @Override
    public int getItemCount() {
        return lessionsModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView name, location, date, tme, hrs;
        ImageView img_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            name = itemView.findViewById(R.id.instruct_name);
            location = itemView.findViewById(R.id.instruct_location);
            date = itemView.findViewById(R.id.instruct_date);
            tme = itemView.findViewById(R.id.instruct_tme);
            hrs = itemView.findViewById(R.id.instruct_hrs);
            img_icon = itemView.findViewById(R.id.instruct_icon);
        }
    }
}
