package com.student.tyro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.student.tyro.InstructorsDetailsActivity;
import com.student.tyro.Util.Constants_Urls;
import com.student.tyro.R;
import com.student.tyro.Model.Instructor_Filter;

import java.util.ArrayList;

public class InstructorsFilterAdapter extends RecyclerView.Adapter<InstructorsFilterAdapter.ViewHolder> {
    Context context;
    private ArrayList<Instructor_Filter> instructorModalArrayList;

    // Fragment fragment;
    // private ArrayList<Instructor_Filter> items;

    public InstructorsFilterAdapter(Context applicationContext, ArrayList<Instructor_Filter> instructorslist) {
        this.context = applicationContext;
        this.instructorModalArrayList = instructorslist;
    }


    @Override
    public InstructorsFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructorsFilterAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_instructors, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorsFilterAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Instructor_Filter filter = instructorModalArrayList.get(position);
        Log.i("array", "" + filter);
        holder.name.setText(filter.getInstruct_firstname());
        holder.lang.setText(filter.getInstruct_language());
        holder.rating.setText(filter.getInstruct_rate());
        holder.distance.setText(filter.getInstruct_distance() + "Km");
        // holder.date.setText(courses.getUsername());
        Picasso.get().load(Constants_Urls.pic_base_url + filter.getInstruct_img())
                .placeholder(R.drawable.user)
                .into(holder.img_icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, InstructorsDetailsActivity.class);
                i.putExtra("instruct_id", filter.getInstruct_id());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return instructorModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView name, lang, rating, distance;
        ImageView img_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            name = itemView.findViewById(R.id.tv_ins_name);
            lang = itemView.findViewById(R.id.tv_ins_lang);
            img_icon = itemView.findViewById(R.id.ivProfile);
            rating = itemView.findViewById(R.id.tv_rate);
            distance = itemView.findViewById(R.id.tv_ins_distance);
        }
    }
}
