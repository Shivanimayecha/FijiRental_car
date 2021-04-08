package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;

import java.util.List;

public class PopUpAdapter extends RecyclerView.Adapter {
    Context context;
    List itemList;
    OnSelectListner listner;
    String val;

    public PopUpAdapter(Context context, List itemList, OnSelectListner listner, String val) {
        this.context=context;
        this.itemList=itemList;
        this.listner=listner;
        this.val=val;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        if(val.equals(itemList.get(position))){
            myViewHolder.selectedImage.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.selectedImage.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onSelect(String.valueOf(itemList.get(position)));
            }
        });

        myViewHolder.itemName.setText(String.valueOf(itemList.get(position)));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnSelectListner{
         void onSelect(String val);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView selectedImage;
        public MyViewHolder(View view) {
            super(view);
            itemName=view.findViewById(R.id.item_name);
            selectedImage=view.findViewById(R.id.selection);
        }
    }
}
