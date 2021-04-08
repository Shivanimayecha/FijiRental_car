package com.app.fijirentalcars.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.CarsFeatureList;
import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CarFeatureAdapter extends RecyclerView.Adapter<CarFeatureAdapter.MyViewHolder> {

    Context context;
    List<FutureModel> modelList;
    String[] selectedItems;
    List<String> selectedItemList=new ArrayList<String>();

    public CarFeatureAdapter(Context context, List<FutureModel> modelList, ListingCarModel listingCarModel) {
        this.context=context;
        this.modelList=modelList;

        FijiRentalUtils.Logger("TAGS","features "+listingCarModel.getCarFeatures());

        if(listingCarModel!=null && listingCarModel.getCarFeatures()!=null && !TextUtils.isEmpty(listingCarModel.getCarFeatures())){
            selectedItems=listingCarModel.getCarFeatures().split(",");
            selectedItemList.clear();
            Collections.addAll(selectedItemList,selectedItems);
        }

        if(listingCarModel.isFilterModel()){
            selectedItems= FijiRentalUtils.featureCarList.split(",");
            selectedItemList.clear();
            Collections.addAll(selectedItemList,selectedItems);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.feature_model,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FutureModel futureModel=modelList.get(position);

        holder.featureName.setText(futureModel.getFeatureTitle());
        if(selectedItemList!=null){
            if(selectedItemList.contains(futureModel.getFeatureId())){
                futureModel.setSelected(true);
            }
        }

        Glide.with(context).load(futureModel.getFeatureIcon()).dontAnimate().into(holder.featureImage);

        if(futureModel.isSelected()){
            holder.featureImage.setSupportImageTintList(ContextCompat.getColorStateList(context, R.color.green));
            holder.featureName.setTextColor(ContextCompat.getColor(context,R.color.green));
            holder.bgLayout.setSelected(true);
        }else {
            holder.featureImage.setSupportImageTintList(ContextCompat.getColorStateList(context, R.color.black));
            holder.bgLayout.setSelected(false);
            holder.featureName.setTextColor(ContextCompat.getColor(context,R.color.black));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(futureModel.isSelected()) {
                    Iterator<String> iter = selectedItemList.iterator();
                    while (iter.hasNext()) {
                        String f = iter.next();
                        if (f.equals(futureModel.getFeatureId())) {
                            iter.remove();
                        }
                    }

                }
                futureModel.setSelected(!futureModel.isSelected());
                notifyDataSetChanged();
            }
        });

    }



    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public String getFeatured() {
        List featureString=new ArrayList();
        for(FutureModel model:modelList){
            if(model.isSelected()){
                featureString.add(model.getFeatureId());
            }
        }


        return TextUtils.join(",",featureString);
    }

    public void resetFilter() {
        selectedItems=null;
        for(FutureModel model:modelList){
            if(model.isSelected()){
                model.setSelected(false);
            }
        }

        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView featureImage;
        TextView featureName;
        LinearLayout bgLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            featureImage=itemView.findViewById(R.id.feture_image);
            featureName=itemView.findViewById(R.id.feture_name);
            bgLayout=itemView.findViewById(R.id.bg_layout);
        }
    }
}
