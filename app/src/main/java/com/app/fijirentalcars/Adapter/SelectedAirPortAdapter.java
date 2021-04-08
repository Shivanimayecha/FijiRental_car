package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Model.AirportLocationModel;
import com.app.fijirentalcars.R;

import java.util.ArrayList;

public class SelectedAirPortAdapter extends RecyclerView.Adapter<SelectedAirPortAdapter.MyViewHolder> {

    Context context;
    ArrayList<AirportLocationModel> modelList;
    private boolean onBind = false;

    // RecyclerView recyclerView;


    public SelectedAirPortAdapter(Context context, ArrayList<AirportLocationModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_layout_selected_airport, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AirportLocationModel model=modelList.get(position);

        holder.airPortName.setText(model.getLocationName());
        holder.airPortPrice.setText((String)model.getPrice());
        if (model.getPrice() != null && !model.getPrice().toString().equalsIgnoreCase("null") && !model.getPrice().equals("0")) {
            holder.airPortPrice.setText("$" + model.getPrice());
        } else {
            holder.airPortPrice.setText("Free");

        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView airPortName,airPortPrice;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            airPortName=itemView.findViewById(R.id.tv_airportname);
            airPortPrice=itemView.findViewById(R.id.tv_airportprice);
        }
    }
}
