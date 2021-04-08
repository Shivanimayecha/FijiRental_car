package com.app.fijirentalcars.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.app.fijirentalcars.MapActivity;
import com.app.fijirentalcars.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;
    public InfoWindowAdapter(Context context) {
        this.context=context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
            return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = View.inflate(context, R.layout.custom_info_window, null);
        TextView textView=v.findViewById(R.id.text_price);

//        textView.setText(marker.getTitle());

        return v;
    }
}
