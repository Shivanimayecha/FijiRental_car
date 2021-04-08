package com.app.fijirentalcars.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.CarDetailsActivity;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CarMapAdapter extends  RecyclerView.Adapter<CarMapAdapter.ViewHolder>{
//   private MyListData[] listdata;

    Activity activity;
    ArrayList<CarModel> carModelArrayList = new ArrayList<>();
    Context mcontext;

    // RecyclerView recyclerView;
    public CarMapAdapter(ArrayList<CarModel> carModelArrayList, Context context) {
        this.mcontext=context;
        this.carModelArrayList = carModelArrayList;
    }
    @Override
    public CarMapAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.car_description_item_layout, parent, false);
        ViewGroup.LayoutParams layoutParams = listItem.getLayoutParams();
        layoutParams.width = (int) (parent.getWidth() * 0.8);
        listItem.setLayoutParams(layoutParams);
        CarMapAdapter.ViewHolder viewHolder = new CarMapAdapter.ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CarMapAdapter.ViewHolder holder, int position) {

        CarModel carModel=carModelArrayList.get(position);

        Glide.with(mcontext)
                .load(FijiRentalUtils.BaseImageUrl+carModel.getItemImage1())
                .centerCrop()
                .placeholder(R.drawable.car1)
                .into(holder.car_image);

        holder.tv_car_model_year.setText(carModel.getModelYear());
        holder.tv_carname.setText(carModelArrayList.get(position).getItemName());
        holder.tv_rate.setText(String.valueOf(carModel.getRatingModel().getAvg_rating()));
        holder.tv_car_rent.setText("From $ " + carModelArrayList.get(position).getFixedRentalDeposit() + " per day");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent carDetailsIntent=new Intent(mcontext, CarDetailsActivity.class);
                carDetailsIntent.putExtra(FijiRentalUtils.CarIntent,carModelArrayList.get(position).getItemId());
                mcontext.startActivity(carDetailsIntent);
            }
        });


       /* final MyListData myListData = listdata[position];
        holder.textView.setText(listdata[position].getDescription());
        holder.imageView.setImageResource(listdata[position].getImgId());*/
     /*   holder.ll_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





            }
        });*/
    }


    @Override
    public int getItemCount() {
        return carModelArrayList.size();
    }

    public void updateDate(ArrayList<CarModel> carModelArrayList) {
        this.carModelArrayList.clear();
        this.carModelArrayList=carModelArrayList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView car_image;
        public TextView tv_car_model_year, tv_carname, tv_rate, tv_car_rent, tv_price;
        public LinearLayout ll_hotel;
        public ViewHolder(View itemView) {
            super(itemView);
            car_image=itemView.findViewById(R.id.roundedImageView);
            tv_car_model_year=itemView.findViewById(R.id.car_year);

            tv_carname=itemView.findViewById(R.id.car_model_name);
            tv_rate=itemView.findViewById(R.id.car_rating);
            tv_car_rent=itemView.findViewById(R.id.car_rent);

           // this.ll_hotel = (LinearLayout) itemView.findViewById(R.id.ll_hotel);
            // this.textView = (TextView) itemView.findViewById(R.id.textView);
            // relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}