package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.HostcarDetailsActivtiy;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.ColorModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder> {

    Context context;
    List<ColorModel> colorlist;
    String selectedColor;

    public ColorAdapter(Context context, List<ColorModel> colorList, String selectedColor) {
        this.context = context;
        this.colorlist = colorList;
        this.selectedColor=selectedColor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.color_picker_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ColorModel colorModel = colorlist.get(position);

//        Glide.with(context).load(colorModel.getColorCode()).into(holder.circleImageView);

        holder.circleImageView.setImageDrawable(new ColorDrawable(ContextCompat.getColor(context, colorModel.getColorCode())));
        if(selectedColor.equals(colorModel.getColorName())){
            colorModel.setSelected(true);
        }

        if (colorModel.isSelected()) {
            holder.selectionImage.setVisibility(View.VISIBLE);
        } else {
            holder.selectionImage.setVisibility(View.GONE);
        }



        if (isDark(ContextCompat.getColor(context, colorModel.getColorCode()))) {
            ImageViewCompat.setImageTintList(holder.selectionImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
        } else {
            ImageViewCompat.setImageTintList(holder.selectionImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)));
        }

//        FijiRentalUtils.Logger("TAGS", "colo name " + colorModel.getColorName() + " code " + isDark(ContextCompat.getColor(context, colorModel.getColorCode())));

        holder.colorName.setText(colorModel.getColorName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorModel.isSelected()) {
                    if(selectedColor.equals(colorModel.getColorName())){
                        selectedColor="";
                    }
                    colorModel.setSelected(false);
                } else {
                    deselectAll();
                    colorModel.setSelected(true);
                }
                notifyDataSetChanged();
            }
        });


    }

    boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.2;
    }

    private void deselectAll() {
        Iterator<ColorModel> iterator = colorlist.iterator();
        while (iterator.hasNext()) {
            iterator.next().setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return colorlist.size();
    }

    public String getSelectedColor() {
        for(ColorModel colorModel:colorlist){
            if(colorModel.isSelected()){
                return colorModel.getColorName();
            }
        }
        return "";
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        ImageView selectionImage;
        TextView colorName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.backGround);
            selectionImage = itemView.findViewById(R.id.selection);
            colorName = itemView.findViewById(R.id.colorName);
        }
    }
}
