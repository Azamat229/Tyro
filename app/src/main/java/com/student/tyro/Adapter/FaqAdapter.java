package com.student.tyro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.student.tyro.R;
import com.student.tyro.Model.Catgory;
import com.student.tyro.Model.Faqcatgories;

import java.util.ArrayList;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Catgory> questionsModalArrayList;
    private ArrayList<Faqcatgories> questionsModalArrayList1;

    // creating a constructor class.
    public FaqAdapter(Context context, ArrayList<Catgory> questionsModalArrayList, ArrayList<Faqcatgories> faqcatgories1) {
        this.context = context;
        this.questionsModalArrayList = questionsModalArrayList;
        this.questionsModalArrayList1 = faqcatgories1;
    }

    @NonNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new FaqAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_faqlist, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull FaqAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Catgory courses = questionsModalArrayList.get(position);
        Log.i("array", "" + courses);
        holder.catname.setText(courses.getCategoryname());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        holder.recycler.setLayoutManager(gridLayoutManager);
        holder.recycler.setAdapter(new CategoryAdapter(context, courses.getFaqcatgories()));
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
        private final RecyclerView recycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            catname = itemView.findViewById(R.id.cat_name);
            catque = itemView.findViewById(R.id.tv_que);
            catanswer = itemView.findViewById(R.id.tv_answr);
            recycler = itemView.findViewById(R.id.details);
        }
    }
}
