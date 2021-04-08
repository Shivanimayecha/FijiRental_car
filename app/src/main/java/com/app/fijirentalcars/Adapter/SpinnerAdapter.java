package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.fijirentalcars.Model.SpinnerModel;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<SpinnerModel> {
    private SpinnerModel[] mArrayList;
    Context context;
    public SpinnerAdapter(@NonNull Context context, int resource,SpinnerModel[] mArrayList) {
        super(context, resource,mArrayList);

        this.context=context;
        this.mArrayList=mArrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.length;
    }

    @Nullable
    @Override
    public SpinnerModel getItem(int position) {
        return mArrayList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(mArrayList[position].getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(mArrayList[position].getName());

        return label;
    }
}
