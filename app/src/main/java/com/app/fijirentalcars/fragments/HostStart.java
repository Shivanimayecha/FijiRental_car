package com.app.fijirentalcars.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.fijirentalcars.FijiCarRentalApplication;
import com.app.fijirentalcars.FillDetailsCarActivity;
import com.app.fijirentalcars.Model.BodyType;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.Model.TransmissionType;
import com.app.fijirentalcars.Model.VinModel;
import com.app.fijirentalcars.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostStart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostStart extends Fragment implements View.OnClickListener {

    TextView StartListing;


    public HostStart() {
        // Required empty public constructor
    }


    public static HostStart newInstance(String param1, String param2) {
        HostStart fragment = new HostStart();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_getstarted_host, container, false);
        StartListing=view.findViewById(R.id.tv_listing);
        StartListing.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_listing:

                ListingCarModel carModel=((FijiCarRentalApplication) getActivity().getApplication()).getCarModel();
                carModel.setModel(new VinModel());
                carModel.setCountry("");
                carModel.setCountryCode("");
                carModel.setLocationSet(false);
                carModel.setTransmissionType(new TransmissionType());
                carModel.setBodyType(new BodyType());

                Intent carDetailsIntent=new Intent(getContext(), FillDetailsCarActivity.class);
                startActivity(carDetailsIntent);
                break;
        }
    }
}