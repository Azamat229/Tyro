package com.student.tyro;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.student.tyro.R;

public class RatingActivity extends AppCompatActivity {
RatingBar rating;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        //rating=(RatingBar)findViewById(R.id.rating);

       /* rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

            //    txtRatingValue.setText(String.valueOf(rating));
                Toast.makeText(getApplicationContext(),""+String.valueOf(String.valueOf(rating)),Toast.LENGTH_LONG).show();

            }
        });*/
    }

}
