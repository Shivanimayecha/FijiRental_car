package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.fragments.HostCarDetailFragment;
import com.app.fijirentalcars.fragments.VehiclesFragment;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.ViewHolder> {

    Context context;
    ArrayList<CarModel> modelList;

    // RecyclerView recyclerView;
    public VehiclesAdapter(Context context, ArrayList<CarModel> carModelArrayList) {
        this.context=context;
        this.modelList=carModelArrayList;

    }

    @Override
    public VehiclesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_layout_vehicles, parent, false);
        VehiclesAdapter.ViewHolder viewHolder = new VehiclesAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VehiclesAdapter.ViewHolder holder, int position) {

        CarModel carModel=modelList.get(position);

        Glide.with(context).load(FijiRentalUtils.BaseImageUrl+carModel.getItemImage1()).centerCrop().placeholder(R.drawable.car1).into(holder.imageView);

        holder.carModel.setText(carModel.getItemName());
        holder.carYear.setText(carModel.getModelYear());
        holder.carRating.setText(String.valueOf(carModel.getRatingModel().getAvg_rating()));
        holder.carNumber.setText(carModel.getCarNumber());

        holder.carLasttrip.setText("Last trip: "+carModel.getLast_trip());
        holder.carTripCount.setText(carModel.getTotal_trip()+" trip");
//        holder.listingStatus

        if(carModel.getEnabled().equals("0")){
            holder.listingStatus.setText("Unlisted");
        }else if(carModel.getEnabled().equals("1")){
            holder.listingStatus.setText("Listed");
        }else if(carModel.getEnabled().equals("2")){
            holder.listingStatus.setText("Snoozed");
        }




        holder.ll_mains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                HostCarDetailFragment hostCarDetailFragment=HostCarDetailFragment.newInstance("","");
                Bundle arguments = new Bundle();
                arguments.putSerializable( FijiRentalUtils.CarDetailIntent , carModel);
                hostCarDetailFragment.setArguments(arguments);

                FragmentTransaction transaction = ((FragmentActivity) holder.ll_mains.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, hostCarDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView,carYear,carModel,carRating,carNumber,carTripCount,carLasttrip,listingStatus;
        public LinearLayout ll_mains;

        public ViewHolder(View itemView) {
            super(itemView);

            this.ll_mains = itemView.findViewById(R.id.ll_mains);
            this.imageView = (ImageView) itemView.findViewById(R.id.roundedImageView);
            this.carYear = itemView.findViewById(R.id.car_year);
            this.carModel = itemView.findViewById(R.id.car_model);
            this.carRating = itemView.findViewById(R.id.car_rating);
            this.carNumber = itemView.findViewById(R.id.car_number);
            this.carTripCount = itemView.findViewById(R.id.trip_count);
            this.carLasttrip = itemView.findViewById(R.id.last_trip);
            listingStatus=itemView.findViewById(R.id.listing_status);
            /*this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);*/
        }
    }
}
