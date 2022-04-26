package com.student.tyro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.student.tyro.Fragment.HomeFragment;
import com.student.tyro.InstructorsDetailsActivity;
import com.student.tyro.Model.StudentReview;
import com.student.tyro.R;
import com.student.tyro.Util.Constants_Urls;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.JvmField;

public class StudentReviewAdapter extends RecyclerView.Adapter<StudentReviewAdapter.ViewHolder> {

    Context context;
    private List<StudentReview> studentReviewsArrayList;
    private MutableLiveData<String> positionOfSlider;

    public StudentReviewAdapter(Context applicationContext, ArrayList<StudentReview> studentReviewsList) {
        this.context = applicationContext;
        this.studentReviewsArrayList = studentReviewsList;
    }
    private final MutableLiveData<String> addressInput = new MutableLiveData();


    public void setData(List<StudentReview> studentReviewsArrayList) {
        this.studentReviewsArrayList = studentReviewsArrayList;
//        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentReviewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_student_reviews, parent, false));
    }

    // Create a LiveData with a String
    private MutableLiveData<String> currentName;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //setting data from models to views
        StudentReview review = studentReviewsArrayList.get(position);
        Log.e("review", review.getStudent_name());

        InstructorsDetailsActivity.OnItemUpdateListener.onUpdateTotal(position);



        holder.counter.setText(String.format("%d/%d", position+1, studentReviewsArrayList.size()));
        holder.name.setText(review.getStudent_name());
        holder.comment.setText(review.getReview());
        holder.comment.setText(review.getReview());
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(review.getTime());
            //2022-04-14 07:30:03
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = inputFormatter.parse(review.getTime());

            DateFormat outputFormatter = new SimpleDateFormat("MMMM dd, yyyy");
            String output = outputFormatter.format(date); // Output : 01/20/2012

            holder.data.setText(output);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.stu_rating.setRating(Float.parseFloat(review.getRating()));



        Picasso.get().load(Constants_Urls.pic_base_url + review.getPicture())
                .placeholder(R.drawable.user)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return studentReviewsArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        ImageView image;
        private final TextView comment;
        private final TextView data;
        private final RatingBar stu_rating;
        private final TextView counter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_stu_name);
            image = itemView.findViewById(R.id.student_review_usr_icon);
            comment = itemView.findViewById(R.id.tv_review_comment);
            data = itemView.findViewById(R.id.tv_lesson_data);
            stu_rating = itemView.findViewById(R.id.stu_rating);
            counter = itemView.findViewById(R.id.tv_reviews_counter);
        }
    }
    public MutableLiveData<String> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<String>();
        }
        return currentName;
    }

}
