package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Model.AirportLocationModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;

import java.util.ArrayList;

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.ViewHolder> {

    Context context;
    ArrayList<AirportLocationModel> modelList;
    private boolean onBind = false;

    // RecyclerView recyclerView;


    public AirportAdapter(Context context, ArrayList<AirportLocationModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public AirportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_layout_airport, parent, false);
        AirportAdapter.ViewHolder viewHolder = new AirportAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AirportAdapter.ViewHolder holder, int position) {
        onBind = true;
        AirportLocationModel locationModel = modelList.get(position);

        holder.airportName.setText(locationModel.getLocationName());

        if (locationModel.getSelected() == 0) {
            holder.locationCheckbox.setChecked(false);
        } else {
            holder.locationCheckbox.setChecked(true);
        }

        if (locationModel.getPrice() != null && !locationModel.getPrice().toString().equalsIgnoreCase("null") && !locationModel.getPrice().equals("0")) {
            holder.price.setText("$" + locationModel.getPrice());
            holder.priceView.setText(" delivery fee");
            holder.pricebar.setProgress( Integer.parseInt(locationModel.getPrice().toString()));
        } else {
            holder.price.setText("Free");
            holder.priceView.setText(" delivery");
            holder.pricebar.setProgress(0);
        }

        if (holder.locationCheckbox.isChecked()) {
            holder.pricebar.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.VISIBLE);
            holder.priceView.setVisibility(View.VISIBLE);
        } else {
            holder.pricebar.setVisibility(View.GONE);
            holder.price.setVisibility(View.GONE);
            holder.priceView.setVisibility(View.GONE);
        }

        FijiRentalUtils.Logger("TAGS","is selected "+locationModel.getSelected());

        holder.pricebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    holder.price.setText("Free");
                    holder.priceView.setText(" delivery");
                    locationModel.setPrice("null");
                } else {

                    int stepSize = 5;

                    progress = (progress/stepSize)*stepSize;
                    seekBar.setProgress(progress);

                    holder.price.setText("$" + progress);
                    holder.priceView.setText(" delivery fee");
                    locationModel.setPrice(progress);
                }
                notifyItemChanged(position,locationModel);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        holder.locationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    locationModel.setSelected(1);
                }else {
                    locationModel.setSelected(0);
                }
                if(!onBind) {
                    FijiRentalUtils.Logger("TAGS","data updated"+locationModel.getSelected()+" name "+locationModel.getLocationName());
                    notifyItemChanged(position,locationModel);
//                    notifyDataSetChanged();
                }
            }
        });
        onBind = false;

    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public ArrayList<AirportLocationModel> getAirportList() {
        ArrayList<AirportLocationModel> selectedList=new ArrayList<>();

        for (AirportLocationModel model:modelList){
            if(model.getSelected()==1){
                selectedList.add(model);
            }
        }
        return selectedList;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView, price, priceView, airportName;
        SeekBar pricebar;
        AppCompatCheckBox locationCheckbox;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            /*this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);*/

            locationCheckbox = itemView.findViewById(R.id.iv_toggle2);
            price = itemView.findViewById(R.id.tv_price);
            airportName = itemView.findViewById(R.id.tv_airportname);
            priceView = itemView.findViewById(R.id.tv_price1);
            pricebar = itemView.findViewById(R.id.seek_price);
        }


    }
}