package com.app.fijirentalcars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.fijirentalcars.Adapter.Delivery_location_Adapter;
import com.app.fijirentalcars.Model.AirportLocationModel;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MapDescriptionActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    MapView mapView;
    TextView tv_next,tv_location,customPrice;
    BottomSheetBehavior sheetBehavior;
    LinearLayout bottom_sheet;
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
    private static int AUTOCOMPLETE_REQUEST_CODE = 1001;
    ImageView iv_back, addView;
    RelativeLayout deliverLocations, addLocation;
    CarModel carModel;
    RecyclerView deliveryLocationView;
    ArrayList<AirportLocationModel> modelArrayList = new ArrayList<>();
    Delivery_location_Adapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_description);
        Window window = this.getWindow();
        Log.e("MapDescriptionActivity", "MapDescriptionActivity");
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        mapView = (MapView) findViewById(R.id.map2);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carModel = Paper.book().read(FijiRentalUtils.carModel);
        if (carModel != null) {

            if (carModel.getAirportList().size() > 0) {
                modelArrayList.clear();
                modelArrayList.addAll(carModel.getAirportList());
                deliverLocations.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            } else {
                deliverLocations.setVisibility(View.GONE);
            }


            if (carModel.getGuestChoosenLocation().equalsIgnoreCase("1")) {
                addLocation.setVisibility(View.VISIBLE);
            } else {
                addLocation.setVisibility(View.GONE);
            }

            if(carModel.getGuestChoosenLocationFee()!=null && !carModel.getGuestChoosenLocationFee().equalsIgnoreCase("null")){
                customPrice.setText("$ "+String.valueOf((int)Double.parseDouble(carModel.getGuestChoosenLocationFee())));
            }
        }


        ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
        if (listingCarModel != null) {
            FijiRentalUtils.Logger("TAGS","is nul "+(listingCarModel.getStreetAdrees()==null ));
            if(listingCarModel.getStreetAdrees()==null ){
             tv_location.setText("Enter a delivery address");
            }else {
                tv_location.setText(listingCarModel.getStreetAdrees() + " " + listingCarModel.getCity() + " " + listingCarModel.getState() + " " + listingCarModel.getCountryCode() + " " + listingCarModel.getZipCode());
            }
        }
    }

    public void init() {
        tv_next = findViewById(R.id.tv_next);
        iv_back = findViewById(R.id.iv_back);
        addView = findViewById(R.id.addView);
        deliverLocations = findViewById(R.id.deliver_locations);
        tv_location=findViewById(R.id.tv_deliverylocation4);
        customPrice=findViewById(R.id.fair_price);
        tv_location.setSelected(true);
        addLocation = findViewById(R.id.rl_4);
        tv_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        addView.setOnClickListener(this);
        addLocation.setOnClickListener(this);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        deliveryLocationView = findViewById(R.id.delivery_location_view);
        deliveryLocationView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Delivery_location_Adapter(this, modelArrayList);
        deliveryLocationView.setAdapter(adapter);


        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//
//        ViewGroup.LayoutParams layoutParams = bottom_sheet.getLayoutParams();
//        layoutParams.height = height - (height / 3);
//        bottom_sheet.setLayoutParams(layoutParams);


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        addView.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        // btnBottomSheet.setText("Close Sheet");
                        addView.setVisibility(View.GONE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        // btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                switch (sheetBehavior.getState()) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        setMapPaddingBotttom(slideOffset);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(carModel.getModelLatitude()), Double.parseDouble(carModel.getModelLongitude()))));
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        setMapPaddingBotttom(slideOffset);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(carModel.getModelLatitude()), Double.parseDouble(carModel.getModelLongitude()))));
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        setMapPaddingBotttom(slideOffset);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(carModel.getModelLatitude()), Double.parseDouble(carModel.getModelLongitude()))));
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                }
            }
        });


    }

    private void setMapPaddingBotttom(float slideOffset) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Float maxMapPaddingBottom = Float.valueOf(Math.max(0, metrics.heightPixels - (int) bottom_sheet.getTop()));
        //left,top,right,bottom
        mMap.setPadding(0, 0, 0, Math.round(slideOffset * maxMapPaddingBottom));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(5);

        mMap.setMaxZoomPreference(18);
        mMap.getUiSettings().setCompassEnabled(false);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.places_ic_search);
        if (carModel != null) {
            LatLng hotel = new LatLng(Double.parseDouble(carModel.getModelLatitude()), Double.parseDouble(carModel.getModelLongitude()));
            mMap.addMarker(new
                    MarkerOptions().position(hotel).title("Hotel Ritz Inn")).setIcon(icon);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(hotel));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hotel, 10.0f));

        }

//        LatLng hotel2 = new LatLng(23.0561, 72.5962);
//        mMap.addMarker(new
//                MarkerOptions().position(hotel2).title("Hotel shahibaug")).setIcon(icon);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(hotel2));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_next:
                onBackPressed();
                break;

            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_4:
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(MapDescriptionActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;
            case R.id.addView:
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);


                ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

                if (!TextUtils.isEmpty(listingCarModel.getCountry())) {
                    listingCarModel.setCountryChange(true);
                    listingCarModel.setLocationSet(false);
                }

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String countryCode = addresses.get(0).getCountryCode();

                String[] addressName = address.split(",");

                listingCarModel.setCity(city);
                listingCarModel.setState(state);
                listingCarModel.setCountry(country);
                listingCarModel.setZipCode(postalCode);
                listingCarModel.setStreetAdrees(addressName[0] + addressName[1]);
                listingCarModel.setCountryCode(countryCode);
                listingCarModel.setLatLng(place.getLatLng());

//                String[] address=place.getAddress().split(",");
//
//                String[] state=address[address.length-2].split("\\s+");

//                listingCarModel.setCity(address[1]);
//                listingCarModel.setState(state[1]);
//                listingCarModel.setCountry(address[address.length-1]);
//                if(state.length>1){
//                    listingCarModel.setZipCode(state[2]);
//                }
//
//                listingCarModel.setStreetAdrees(place.getName());
//                listingCarModel.setCountryCode(countryModel.getCode());


//                LatLng latLng=new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);

//                listingCarModel.setLatLng(latLng);

                ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);


                Intent addressIntent = new Intent(this, AddressActivity.class);
                startActivity(addressIntent);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("TAGS", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}