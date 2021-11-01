package com.student.tyro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.material.tabs.TabLayout;
import com.student.tyro.Adapter.ViewPagerAdapter;
import com.student.tyro.Model.Catgory;
import com.student.tyro.Model.SliderUtils;
import com.student.tyro.Model.StaySafeModel;
import com.student.tyro.Util.CustomVolleyRequest;

import java.util.ArrayList;
import java.util.List;

public class AcceptTermsPolicyActivity extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    AppCompatTextView agree_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_terms_policy);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        agree_btn = findViewById(R.id.agree_btn);

        ArrayList<StaySafeModel> arraylist = new ArrayList<>();
        arraylist.add(new StaySafeModel(R.drawable.mask_icon, "Mask Policy", "All students and instructors are required to wear a face cover or mask during lessons, even when vaccinated"));
        arraylist.add(new StaySafeModel(R.drawable.pay_icon, "Cancellation Policy", "In Order to prevent a $40 cancellation fee be sure to provide a 24 hours notice of cancellation prior to your scheduled lesson."));
        arraylist.add(new StaySafeModel(R.drawable.staysafe_icon, "Stay Safe", "Be aware of your surroundings at all times during your lesson. Ensure all distractions are reduced and your attention is directed on the road."));
        viewPagerAdapter = new ViewPagerAdapter(arraylist, getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setEnabled(false);
        viewPager.beginFakeDrag();

        tabLayout.clearOnTabSelectedListeners();
        for (View v : tabLayout.getTouchables()) {
            v.setEnabled(false);
        }

        agree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < arraylist.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, false);
                } else if (viewPager.getCurrentItem() == arraylist.size() - 1) {
                    Intent i = new Intent(getApplicationContext(), ConfirmBookingActivity.class);
                    i.putExtra("student_id", getIntent().getStringExtra("student_id"));
                    i.putExtra("instruct_id", getIntent().getStringExtra("instruct_id"));
                    i.putExtra("username", getIntent().getStringExtra("username"));
                    i.putExtra("location", getIntent().getStringExtra("location"));
                    i.putExtra("language", getIntent().getStringExtra("language"));
                    i.putExtra("rating", getIntent().getStringExtra("rating"));
                    i.putExtra("time_id", getIntent().getStringExtra("time_id"));
                    i.putExtra("strttme", getIntent().getStringExtra("strttme"));
                    i.putExtra("endtme", getIntent().getStringExtra("endtme"));
                    i.putExtra("total_hrs", getIntent().getStringExtra("total_hrs"));
                    i.putExtra("price", getIntent().getStringExtra("price"));
                    i.putExtra("User_id", getIntent().getStringExtra("User_id"));
                    i.putExtra("bookingdate", getIntent().getStringExtra("bookingdate"));
                    i.putExtra("image", getIntent().getStringExtra("image"));
                    i.putExtra("longitude", getIntent().getStringExtra("longitude"));
                    i.putExtra("lat", getIntent().getStringExtra("lat"));
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        ArrayList<StaySafeModel> arraylist;

        public ViewPagerAdapter(ArrayList<StaySafeModel> arraylist, Context context) {
            this.arraylist = arraylist;
            this.context = context;
        }

        @Override
        public int getCount() {
            return arraylist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.row_accept_policy, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView title = view.findViewById(R.id.title);
            TextView subtitle = view.findViewById(R.id.subtitle);

            imageView.setImageResource(arraylist.get(position).getImageView());
            title.setText(arraylist.get(position).getTitle());
            subtitle.setText(arraylist.get(position).getSubtitle());
            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);
        }

    }
}