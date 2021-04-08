package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.MapDescriptionActivity;
import com.app.fijirentalcars.Model.AirportLocationModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;

import java.util.ArrayList;

public class Delivery_location_Adapter extends RecyclerView.Adapter<Delivery_location_Adapter.MyViewHolder> {
    Context context;
    ArrayList<AirportLocationModel> modelArrayList;

    public Delivery_location_Adapter(Context context, ArrayList<AirportLocationModel> modelArrayList) {
        this.context=context;
        this.modelArrayList=modelArrayList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.delivery_location_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AirportLocationModel model=modelArrayList.get(position);

        holder.name.setText(model.getLocationName());
        holder.price.setText("$ "+model.getPrice());

        if(position==modelArrayList.size()-1){
            holder.divider.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView selectedLoaction;
        TextView name,price;
        View divider;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            selectedLoaction=itemView.findViewById(R.id.select_location);
            name=itemView.findViewById(R.id.tv_deliverylocation);
            price=itemView.findViewById(R.id.fair_price);
            divider=itemView.findViewById(R.id.view_divider);
        }
    }
}
