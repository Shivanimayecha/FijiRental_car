package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.fijirentalcars.CarDetailsActivity;
import com.app.fijirentalcars.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImagePager  extends PagerAdapter {

    Context context;
    ArrayList imageUrl;

    public ImagePager(Context context, ArrayList imageUrl) {
        this.context=context;
        this.imageUrl=imageUrl;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view= LayoutInflater.from(context).inflate(R.layout.image_item,container,false);

        ImageView imageView=view.findViewById(R.id.image_view);

        Glide.with(context).load(imageUrl.get(position)).placeholder(R.drawable.drive).centerCrop().into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
