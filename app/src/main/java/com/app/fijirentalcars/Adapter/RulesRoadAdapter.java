package com.app.fijirentalcars.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.R;

public class RulesRoadAdapter extends RecyclerView.Adapter<RulesRoadAdapter.ViewHolder>{


    // RecyclerView recyclerView;
    public RulesRoadAdapter() {

    }

    @Override
    public RulesRoadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.road_item_layout, parent, false);
        RulesRoadAdapter.ViewHolder viewHolder = new RulesRoadAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RulesRoadAdapter.ViewHolder holder, int position) {


        if (position==0){

            holder.imageView.setImageResource(R.drawable.ic_action_return_on_time);
            holder.tv_name.setText("Return on time");

        }else if (position ==1){

            holder.imageView.setImageResource(R.drawable.ic_action_carry_your_licenece);
            holder.tv_name.setText("Carry your license during the trip");

        }else if(position == 2){

            holder.imageView.setImageResource(R.drawable.ic_action_no_smoking);
            holder.tv_name.setText("No smoking");

        }else if (position ==3){
            holder.imageView.setImageResource(R.drawable.ic_action_keep_the_car_tidy);
            holder.tv_name.setText("keep the car tidy");


        }else if (position ==4){

            holder.imageView.setImageResource(R.drawable.ic_action_100_miles_included);
            holder.tv_name.setText("100 miles included");

        }else if (position ==5){

            holder.imageView.setImageResource(R.drawable.ic_action_refuel_gas);
            holder.tv_name.setText("Refuel with premium gas");

        }



    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tv_name;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            /*relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);*/
        }
    }


    @Override
    public int getItemCount(){
        return 6;
    }
}