package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.app.fijirentalcars.Add_Unavailability;
import com.app.fijirentalcars.R;

public class CustomSpinnerAdapter extends BaseAdapter {

    Context context;
    String[] days;

    public CustomSpinnerAdapter(Context context, String[] days) {
        this.context=context;
        this.days=days;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.custom_spinner_items, null);
        AppCompatCheckBox checkBox=convertView.findViewById(R.id.check_box);
        TextView textView=convertView.findViewById(R.id.textName);
        textView.setText(days[position]);

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkBox.setChecked(!checkBox.isChecked());
//            }
//        });
        return convertView;
    }
}
