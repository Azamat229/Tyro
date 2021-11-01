package com.student.tyro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.tyro.R;
import com.student.tyro.Model.TimeSlot;

import java.util.ArrayList;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.ViewHolder> {
private Context context;
        ArrayList<TimeSlot> timeSlotModels;

// creating a constructor class.
public SlotAdapter(Context context, ArrayList<TimeSlot> timeSlotModels) {
        this.context = context;
        this.timeSlotModels = timeSlotModels;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new SlotAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.time_slot_list, parent, false));

        }

@Override
public void onBindViewHolder(@NonNull SlotAdapter.ViewHolder holder, int position)
        {
        // setting data to our text views from our modal class.
        TimeSlot courses = timeSlotModels.get(position);
            Log.i("time slots",""+timeSlotModels.get(position));
         holder.time.setText(courses.getStrt_tme()+courses.getEnd_tme());


/*String s=courses.getSts();
if(s.equals("Busy"))
{
    holder.time.setBackgroundColor(context.getColor(R.color.colorGray));
}
else
{
    holder.time.setBackgroundColor(context.getColor(R.color.colorGreen));
}*/
        }

@Override
public int getItemCount() {
        return timeSlotModels.size();
        }

public static class ViewHolder extends RecyclerView.ViewHolder {
    // creating variables for our text views.

    private final Button time;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        // initializing our text views.

        time=itemView.findViewById(R.id.btn_tme);

    }
}
}

