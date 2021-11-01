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

import com.student.tyro.Model.SavedCardModel;
import com.student.tyro.R;
import com.student.tyro.StripePayment;

import java.util.ArrayList;

public class SvedCardAdapter extends RecyclerView.Adapter<SvedCardAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SavedCardModel> cardDetailsList;
    int type = -1;
    int pos=-1;
    // creating a constructor class.
    public SvedCardAdapter(Context context, ArrayList<SavedCardModel> cardlist,int type) {
        this.context = context;
        this.cardDetailsList = cardlist;

        this.type = type;
    }
    @NonNull
    @Override
    public SvedCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new SvedCardAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.saved_card_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SvedCardAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        SavedCardModel savedcard = cardDetailsList.get(position);
        holder.selected_card.setColorFilter(context.getResources().getColor(R.color.colorBlack));
        String string = cardDetailsList.get(position).getCard_number();

        if (string.length() > 4) {
            string = string.substring(string.length() - 4);
        }
        holder.cardNumber.setText("****  ****  ****  " + string);
        if (pos==position)
        {
            holder.selected_card.setVisibility(View.VISIBLE);

            holder.selected_card.setBackgroundResource(R.drawable.successful_color);

            holder.relativeLayout.setBackgroundResource(R.drawable.line_line_background);
        }
        else {
            holder.selected_card.setVisibility(View.VISIBLE);

            holder.selected_card.setBackgroundResource(R.drawable.track_circle);

            holder.relativeLayout.setBackgroundResource(R.drawable.line_line_background);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;

                notifyDataSetChanged();

                ((StripePayment) context).showSaveCardDetails(cardDetailsList.get(position).getCard_number(),
                        cardDetailsList.get(position).getCard_expiry(), cardDetailsList.get(position).getCard_cvv());

            }
        });
    }

    @Override
    public int getItemCount() {
        return cardDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        TextView cardNumber;
        ImageView selected_card;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            cardNumber = itemView.findViewById(R.id.textCardNumber);
            selected_card = itemView.findViewById(R.id.arrow);
            relativeLayout=itemView.findViewById(R.id.rrl);
        }
    }
}








/*extends PagerAdapter {

    Context context;
    ArrayList<SavedCardModel> cardDetailsList;
    int type = -1;
    int pos=-1;

    public SvedCardAdapter(Context context, ArrayList<SavedCardModel> cardDetailsList, int type) {
        this.context = context;
        this.cardDetailsList = cardDetailsList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return cardDetailsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.saved_card_adapter, null);

        TextView cardNumber = view.findViewById(R.id.textCardNumber);
        final ImageView selected_card = view.findViewById(R.id.arrow);
        final RelativeLayout relativeLayout=view.findViewById(R.id.rrl);

        String string = cardDetailsList.get(position).getCard_number();

        if (string.length() > 4) {
            string = string.substring(string.length() - 4);

        }
        cardNumber.setText("****  ****  ****  " + string);


        if (pos==position)
        {
            selected_card.setVisibility(View.VISIBLE);

            selected_card.setBackgroundResource(R.drawable.successful_color);

            relativeLayout.setBackgroundResource(R.drawable.line_line_background);
        }
        else {
            selected_card.setVisibility(View.VISIBLE);

            selected_card.setBackgroundResource(R.drawable.track_circle);

            relativeLayout.setBackgroundResource(R.drawable.line_line_background);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos=position;

//                Toast.makeText(context, ""+pos, Toast.LENGTH_SHORT).show();

                notifyDataSetChanged();

                ((StripePayment) context).showSaveCardDetails(cardDetailsList.get(position).getCard_number(),
                        cardDetailsList.get(position).getCard_expiry(), cardDetailsList.get(position).getCard_cvv());



            }

        });


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}*/