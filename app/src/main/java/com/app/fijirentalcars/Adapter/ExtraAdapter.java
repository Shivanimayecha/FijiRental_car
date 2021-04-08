package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.AddExtras;
import com.app.fijirentalcars.Model.ExtraItemModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;

import java.util.List;


public class ExtraAdapter extends RecyclerView.Adapter<ExtraAdapter.MyviewHolder> {

    Context context;
    List<ExtraItemModel> modelList;
    UpdateExtra listner;

    public ExtraAdapter(Context context, List<ExtraItemModel> extraItemModelList,UpdateExtra listner) {
        this.context = context;
        this.modelList = extraItemModelList;
        this.listner=listner;

    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem = layoutInflater.inflate(R.layout.item_layout_car_extra, parent, false);

        return new MyviewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        ExtraItemModel itemModel=modelList.get(position);

        holder.extraName.setText(itemModel.getExtraName());
        if(itemModel.getStatus()==1){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }

        if(itemModel.getPriceType()==1){
            holder.extraPrice.setText("US$ "+itemModel.getPrice()+"/trip");
        }else {
            holder.extraPrice.setText("US$ "+itemModel.getPrice()+"/day");
        }


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listner.updateExtra(position,isChecked,false);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.updateExtra(position,false,true);
            }
        });

    }

    public interface UpdateExtra{
        void updateExtra(int position,boolean isCheked,boolean isUpdate);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView extraName,extraPrice;
        AppCompatCheckBox checkBox;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            extraName=itemView.findViewById(R.id.extraName);
            extraPrice=itemView.findViewById(R.id.extra_price);
            checkBox=itemView.findViewById(R.id.check_btn);
        }
    }
}
