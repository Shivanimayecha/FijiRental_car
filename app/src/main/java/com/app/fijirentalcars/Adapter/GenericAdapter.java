package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Model.BodyType;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.CarModelType;
import com.app.fijirentalcars.Model.CountryModel;
import com.app.fijirentalcars.Model.ExtraItem;
import com.app.fijirentalcars.Model.FuelType;
import com.app.fijirentalcars.Model.MakeModel;
import com.app.fijirentalcars.Model.SpinnerModel;
import com.app.fijirentalcars.Model.StateModel;
import com.app.fijirentalcars.Model.TransmissionType;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.listners.DialogItemListner;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Object> mArrayList;
    DialogItemListner listner;


    public GenericAdapter(Context context, List<Object> arrayList, DialogItemListner listner) {
        this.mContext = context;
        this.mArrayList = arrayList;
        this.listner = listner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item_view, parent, false);
        return new MyViewHolder(view);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemname;

        public MyViewHolder(View view) {
            super(view);
            itemname = view.findViewById(R.id.item_name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        Object object = mArrayList.get(position);

        if (object instanceof TransmissionType) {
            myViewHolder.itemname.setText(((TransmissionType) object).getTransmissionTypeTitle());
        } else  if (object instanceof CountryModel) {
            myViewHolder.itemname.setText(((CountryModel) object).getName());
        }else  if (object instanceof MakeModel) {
            myViewHolder.itemname.setText(((MakeModel) object).getMakeDisplay());
        }else  if (object instanceof CarModelType) {
            myViewHolder.itemname.setText(((CarModelType) object).getModelName());
        }else  if (object instanceof BodyType) {
            myViewHolder.itemname.setText(((BodyType) object).getBodyTypeTitle());
        }else  if (object instanceof StateModel) {
            myViewHolder.itemname.setText(((StateModel) object).getValue());
        }else  if (object instanceof FuelType) {
            myViewHolder.itemname.setText(((FuelType) object).getFuelTypeTitle());
        }else  if (object instanceof ExtraItem) {
            myViewHolder.itemname.setText(((ExtraItem) object).getExtraName());
        }else  if (object instanceof SpinnerModel) {
            myViewHolder.itemname.setText(((SpinnerModel) object).getName());
        }else {
            myViewHolder.itemname.setText(String.valueOf(mArrayList.get(position)));
        }


        myViewHolder.itemname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(mArrayList.get(position));
            }
        });


    }

    public abstract void selectProduct(int position);


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
