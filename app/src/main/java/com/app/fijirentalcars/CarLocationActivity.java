package com.app.fijirentalcars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarLocationActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    MapView mapView;
    TextView confirm_location, mapMsg, adjustPin, tvAddress;
    ImageView iv_hide;
    RelativeLayout descMsg;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_location);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        Log.e("CarLocationActivity", "CarLocationActivity");
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm_location = findViewById(R.id.tv_next);
        descMsg = findViewById(R.id.tv_grab);
        iv_hide = findViewById(R.id.iv_hide);
        mapMsg = findViewById(R.id.map_msg);
        adjustPin = findViewById(R.id.adjust_pin);
        tvAddress = findViewById(R.id.tv_address);

        ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
        tvAddress.setText(listingCarModel.getStreetAdrees() + " " + listingCarModel.getCity() + " " + listingCarModel.getState() + " " + listingCarModel.getCountryCode() + " " + listingCarModel.getZipCode());

        mapView = (MapView) findViewById(R.id.map2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mapMsg.setText(getString(R.string.adjustpin_location));
        descMsg.setVisibility(View.VISIBLE);

        confirm_location.setOnClickListener(this);
        iv_hide.setOnClickListener(this);
        adjustPin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
//        mMap.setMinZoomPreference(5);
//
//        mMap.setMaxZoomPreference(12);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.places_ic_search);
        // Add a marker in Sydney and move the camera
//        LatLng hotel = new LatLng(23.0757, 72.5090);
//        mMap.addMarker(new
//                MarkerOptions().position(hotel).title("Hotel Ritz Inn")).setIcon(icon);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(hotel));
//
        ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();


        LatLng hotel2 = listingCarModel.getLatLng();
//        mMap.addMarker(new
//                MarkerOptions().position(hotel2).title("Hotel shahibaug")).setIcon(icon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hotel2, 18));


    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", this.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:

                LatLng latLng = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();

                if (mMap.getUiSettings().isScrollGesturesEnabled()) {
                    Geocoder geocoder = new Geocoder(this);
                    List<Address> addressList = new ArrayList<>();
                    try {

                        addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

                    if (addressList.size() > 0) {
                        listingCarModel.setZipCode(addressList.get(0).getPostalCode());
                        listingCarModel.setCountry(addressList.get(0).getCountryName());
                        listingCarModel.setState(addressList.get(0).getAdminArea());
                        listingCarModel.setCity(addressList.get(0).getLocality());
                        listingCarModel.setStreetAdrees(addressList.get(0).getSubAdminArea());
                        listingCarModel.setRegion(" ");
                        listingCarModel.setCountryCode(addressList.get(0).getCountryCode());

                    }

                    FijiRentalUtils.Logger("TAGS", "Lat lng " + latLng + "data " + addressList.get(0).getCountryCode() + " city " + addressList.get(0).getLocality() + " s " + addressList.get(0).getSubAdminArea() + " loc " + addressList.get(0).getSubLocality());

                    listingCarModel.setLocationSet(true);
                    listingCarModel.setLatLng(latLng);
                    ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);
                }else {
                    ListingCarModel listingCarModel=((FijiCarRentalApplication) getApplication()).getCarModel();
                    listingCarModel.setLocationSet(true);
                    ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);
                }


                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","success");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

                break;
            case R.id.iv_hide:
                descMsg.setVisibility(View.GONE);
                break;
            case R.id.adjust_pin:
                adjustPin.setVisibility(View.GONE);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mapMsg.setText(getString(R.string.drag_map));
                if (descMsg.getVisibility() != View.VISIBLE) {
                    descMsg.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}