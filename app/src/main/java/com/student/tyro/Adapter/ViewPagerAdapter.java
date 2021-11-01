package com.student.tyro.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;
import com.student.tyro.Util.CustomVolleyRequest;
import com.student.tyro.R;
import com.student.tyro.Model.SliderUtils;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderUtils> sliderImg;
    private ImageLoader imageLoader;


    public ViewPagerAdapter(List sliderImg, Context context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);

        final SliderUtils utils = sliderImg.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView offer_text = view.findViewById(R.id.offer_text);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        String completepath = utils.getImagepath() + utils.getImage();
        imageLoader.get(completepath, ImageLoader.getImageListener(imageView, android.R.drawable.progress_indeterminate_horizontal, android.R.drawable.progress_indeterminate_horizontal));
        Spanned sp = Html.fromHtml(utils.getDescript());
        offer_text.setText(sp);
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
