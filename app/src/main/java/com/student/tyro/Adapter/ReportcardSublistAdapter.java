package com.student.tyro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.tyro.Model.Reportcardsublist;
import com.student.tyro.Util.NetworkConnection;
import com.student.tyro.R;

import java.util.ArrayList;

public class ReportcardSublistAdapter extends RecyclerView.Adapter<ReportcardSublistAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Reportcardsublist> reportcardsublistArrayList;
    NetworkConnection networkConnection;

    // creating a constructor class.
    public ReportcardSublistAdapter(Context context, ArrayList<Reportcardsublist> sublist) {
        this.context = context;
        this.reportcardsublistArrayList = sublist;
        networkConnection = new NetworkConnection(context);
    }

    @NonNull
    @Override
    public ReportcardSublistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ReportcardSublistAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Reportcardsublist sublist = reportcardsublistArrayList.get(position);
        Log.i("array", "" + sublist.getReportname());
        System.out.println("data_values" + sublist.getReportname());
        // holder.catname.setText(courses.getCatgory());
        holder.catque.setText(sublist.getReportname());
        holder.ratingbar.setRating(Float.parseFloat(sublist.getRating()));

    }
        @Override
        public int getItemCount() {
            return reportcardsublistArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            // creating variables for our text views.
            private final TextView catque;
            RatingBar ratingbar;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                // initializing our text views.
                catque=itemView.findViewById(R.id.tv_sub);
                ratingbar=itemView.findViewById(R.id.rating);
            }
        }
    }


