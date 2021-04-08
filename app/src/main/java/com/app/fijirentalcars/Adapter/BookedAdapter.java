package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.BookedTripActivity;
import com.app.fijirentalcars.R;

public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.ViewHolder>{


    Context context;

    // RecyclerView recyclerView;
    public BookedAdapter(Context context) {
        this.context=context;
    }
    @Override
    public BookedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_layout_booked, parent, false);
        BookedAdapter.ViewHolder viewHolder = new BookedAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookedAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookedDetail=new Intent(context, BookedTripActivity.class);
                context.startActivity(bookedDetail);
            }
        });

    }


    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            /*this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);*/
        }
    }
}