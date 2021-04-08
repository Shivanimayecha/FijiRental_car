package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.PhotoActivity;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{


    Context context;
    List imageList;

    // RecyclerView recyclerView;
    public PhotoAdapter(Context context, List carImages) {
        this.context=context;
        this.imageList=carImages;

    }
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.photo_item_layout, parent, false);
        PhotoAdapter.ViewHolder viewHolder = new PhotoAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.ViewHolder holder, int position) {

//        FijiRentalUtils.Logger("TAGS","car im "+imageList.get(position));

        Glide.with(context).load(imageList.get(position)).centerCrop().placeholder(R.drawable.car1).into(holder.imageView);


    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void UpdateList(List<String> photos) {
        imageList=photos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.car_image);
            /*this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);*/
        }
    }
}