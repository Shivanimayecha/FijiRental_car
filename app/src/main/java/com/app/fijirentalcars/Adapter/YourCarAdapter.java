package com.app.fijirentalcars.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.R;

public class YourCarAdapter extends RecyclerView.Adapter<YourCarAdapter.ViewHolder>{


    // RecyclerView recyclerView;
    public YourCarAdapter() {

    }
    @Override
    public YourCarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.your_car_item_layout, parent, false);
        YourCarAdapter.ViewHolder viewHolder = new YourCarAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(YourCarAdapter.ViewHolder holder, int position) {

        if (position==0){
            holder.tv_number.setText("1");
            holder.tv_door.setText("Driver side door");
        }else if(position == 1){
            holder.tv_number.setText("2");
            holder.tv_door.setText("Driver side dashboard");
        }else if(position == 2){
            holder.tv_number.setText("3");
            holder.tv_door.setText("Documentation");
        }

    }


    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tv_number,tv_door;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_door=itemView.findViewById(R.id.tv_door);
            this.tv_number=itemView.findViewById(R.id.tv_number);
            /*this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);*/
        }
    }
}
