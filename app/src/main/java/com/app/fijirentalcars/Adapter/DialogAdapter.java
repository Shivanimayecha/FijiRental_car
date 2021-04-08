package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.R;
import com.app.fijirentalcars.listners.DialogItemListner;

import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.MyViewHolder> {

    Context context;
    List<String> ItemsList;
    DialogItemListner listner;

    public DialogAdapter(Context context, List<String> itemList,DialogItemListner listner) {
        this.context=context;
        this.ItemsList=itemList;
        this.listner=listner;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_item_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.itemname.setText(ItemsList.get(position));
        holder.itemname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(ItemsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemname;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname=itemView.findViewById(R.id.item_name);
        }
    }
}
