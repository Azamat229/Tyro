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
import com.student.tyro.Model.ReportcardModel;
import com.student.tyro.Model.Reportcardsublist;

import java.util.ArrayList;

public class ReportcardAdapter extends RecyclerView.Adapter<ReportcardAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReportcardModel> cardcatgoryArrayList;
    private ArrayList<Reportcardsublist> cardsubcatgoryArrayList;

    // creating a constructor class.
    public ReportcardAdapter(Context context, ArrayList<ReportcardModel> reportcardlist, ArrayList<Reportcardsublist> reportsubcardlist) {
        this.context = context;
        this.cardcatgoryArrayList = reportcardlist;
        this.cardsubcatgoryArrayList = reportsubcardlist;
    }

    @NonNull
    @Override
    public ReportcardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ReportcardAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_cat_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ReportcardAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        ReportcardModel cards = cardcatgoryArrayList.get(position);
        Log.i("array", "" + cards);
        holder.catname.setText(cards.getCategoryname());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        holder.recycler.setLayoutManager(gridLayoutManager);
        holder.recycler.setAdapter(new ReportcardSublistAdapter(context, cards.getSublist()));
    }

    @Override
    public int getItemCount() {
        return cardcatgoryArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView catname;

        private final RecyclerView recycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            catname = itemView.findViewById(R.id.cat_name);

            recycler = itemView.findViewById(R.id.subdetails);
        }
    }
}

