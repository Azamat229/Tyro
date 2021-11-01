package com.student.tyro.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.tyro.R;
import com.student.tyro.Model.Faqcatgories;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Faqcatgories> questionsModalArrayList;
    boolean buttonOn1;
    // creating a constructor class.
    public CategoryAdapter(Context context, ArrayList<Faqcatgories> questionsModalArrayList) {
        this.context = context;
        this.questionsModalArrayList = questionsModalArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.faqlist, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        // setting data to our text views from our modal class.
        Faqcatgories courses = questionsModalArrayList.get(position);
        Log.i("array",""+courses.getTitle());
        System.out.println("data_values"+courses.getTitle());
        // holder.catname.setText(courses.getCatgory());
        holder.catque.setText(courses.getTitle());


holder.img.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
        if (!buttonOn1) {
            buttonOn1 = true;
            holder.img.setImageResource(0);
            holder.img.setBackground(context.getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
            holder.catanswer.setVisibility(View.VISIBLE);
            holder.catanswer.setText(Html.fromHtml(courses.getDescription()));
        }
        else
        {
            buttonOn1 = false;
            holder.img.setImageResource(0);
            holder.img.setBackground(context.getDrawable(R.drawable.ic_baseline_add_circle_outline_24));
            holder.catanswer.setVisibility(View.GONE);

        }
    }
});
    }

    @Override
    public int getItemCount() {
        return questionsModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView catname;
        private final TextView catque;
        private final TextView catanswer;
        private final ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            catname = itemView.findViewById(R.id.cat_name);
            catque=itemView.findViewById(R.id.tv_que);
            catanswer=itemView.findViewById(R.id.tv_answr);
            img=itemView.findViewById(R.id.img_plus);
        }
    }
}