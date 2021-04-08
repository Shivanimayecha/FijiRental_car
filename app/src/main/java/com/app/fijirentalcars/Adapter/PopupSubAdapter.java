package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.fragments.EarningsFragment;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL;

public class PopupSubAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<CarModel> modelList;
    ItemSelect listner;
    public PopupSubAdapter(Context context, ArrayList<CarModel> carModelArrayList, ItemSelect listener) {
        this.context=context;
        this.modelList=carModelArrayList;
        this.listner=listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_sub_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder=(MyViewHolder)holder;

        CarModel model=modelList.get(position);

        if(model.isSelected()){
            myViewHolder.selectionImage.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.selectionImage.setVisibility(View.GONE);
        }

        if(model.getItemImage1()!=null){
            Glide.with(context).load(FijiRentalUtils.BaseImageUrl+model.getItemImage1()).centerCrop().into(myViewHolder.itemImage);
        }else {
            Glide.with(context).load(R.drawable.car).into(myViewHolder.itemImage);
        }
        myViewHolder.itemName.setText(model.getItemName());

        if(model.getItemSku()!=null) {
            myViewHolder.itemdesc.setText(model.getItemSku() + " \u2022 " + model.getModelName());
            FijiRentalUtils.Logger("TAGS", "data " + (model.getItemSku() + " \u2022 " + model.getModelName()));
        }else {
            myViewHolder.itemdesc.setVisibility(View.GONE);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!model.isSelected()){
                    deselectAll();
                    model.setSelected(true);
                    notifyDataSetChanged();
                    listner.onItemSelect(model);
                }

            }
        });

    }

    private void deselectAll() {
        Iterator<CarModel> iterator = modelList.iterator();
        while (iterator.hasNext()){
            iterator.next().setSelected(false);
        }
    }

    public interface ItemSelect{
        void onItemSelect(CarModel carModel);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,itemdesc;
        ImageView itemImage,selectionImage;
        public MyViewHolder(View view) {
            super(view);

            itemName=view.findViewById(R.id.item_name);
            itemImage=view.findViewById(R.id.image);
            selectionImage=view.findViewById(R.id.selection);
            itemdesc=view.findViewById(R.id.item_desc);


        }
    }
}
