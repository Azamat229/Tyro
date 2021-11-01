package com.student.tyro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.student.tyro.R;
import com.student.tyro.Model.TimeSlot;

import java.util.ArrayList;

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.ViewHolder> {
    private Context context;
    ArrayList<TimeSlot> timeSlotModels;
    boolean selected = false;
    int row_index=-1;
    // creating a constructor class.
    public TimeSlotsAdapter(Context context, ArrayList<TimeSlot> timeSlotModels) {
        this.context = context;
        this.timeSlotModels = timeSlotModels;
    }

    @NonNull
    @Override
    public TimeSlotsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new TimeSlotsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_time_slot, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotsAdapter.ViewHolder holder, int position)
    {
        // setting data to our text views from our modal class.
        TimeSlot slots = timeSlotModels.get(position);
       // Log.i("array",""+slots.getBooking_dt());

        //holder.date.setText(slots.getBooking_dt());
        holder.time.setText(slots.getStrt_tme() + "-" +slots.getEnd_tme());
        holder.sts.setText(slots.getSts());


        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              //  Toast.makeText(context,""+row_index,Toast.LENGTH_LONG).show();

                //String buttonText =holder.time.getText().toString();
                Intent intent = new Intent("student-timeslot");
                intent.putExtra("instruct_id",slots.getInstruct_id());
                intent.putExtra("strt_time",slots.getStrt_tme());
                intent.putExtra("end_time",slots.getEnd_tme());
                intent.putExtra("total_hrs",slots.getTotal());
                intent.putExtra("Price",slots.getPrce());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                row_index=position;
                notifyDataSetChanged();
               /* holder.time.setBackgroundResource(R.drawable.bg_green);
                holder.time.setTextColor(Color.WHITE);*/
               /* if(selected){
                    //holder.time.setBackgroundResource(R.drawable.bg_green);
                    String buttonText =holder.time.getText().toString();
                    holder.time.setBackgroundResource(R.drawable.bg_white_green_border);
                }
                else {
                    String buttonText =holder.time.getText().toString();
                    holder.time.setBackgroundResource(R.drawable.bg_green);
                    selected = true;
                }*/
            }
        });
        if(row_index==position){
            holder.time.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.time.setBackgroundResource(R.drawable.bg_green);
        }
        else
        {
            holder.time.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.time.setBackgroundResource(R.drawable.bg_white_green_border);
        }
        /*if(row_index==position){
            holder.time.setSelected(true);
            holder.time.setBackgroundResource(R.drawable.bg_green);
            Toast.makeText(context,"selected"+row_index,Toast.LENGTH_LONG).show();
        }
        else
        {
            holder.time.setSelected(false);
            holder.time.setBackgroundResource(R.drawable.bg_white_green_border);
        }*/
       /* String st=courses.getSts();
        if(st.equals("Busy"))
        {
            holder.sts.setText(".Available");
            holder.date.setText(courses.getBooking_dt());
            holder.time.setText(courses.getStrt_tme() + courses.getEnd_tme());
        }
        else {

            holder.date.setVisibility(View.INVISIBLE);
            holder.time.setText(courses.getStrt_tme() + courses.getEnd_tme());
            holder.sts.setVisibility((View.INVISIBLE));
        }
*/

    }
    @Override
    public int getItemCount() {
        return timeSlotModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView date;
        private final TextView sts;
        private final Button time;
        LinearLayout line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            date = itemView.findViewById(R.id.date);
            sts=itemView.findViewById(R.id.txt_avail);
            time=itemView.findViewById(R.id.btn_tme);

        }
    }
}