package com.app.fijirentalcars;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.app.fijirentalcars.Adapter.CarFeatureAdapter;
import com.app.fijirentalcars.Adapter.CarListAdapter;
import com.app.fijirentalcars.Adapter.CarMapAdapter;
import com.app.fijirentalcars.Adapter.InfoWindowAdapter;
import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.BodyType;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.CarModelType;
import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.Model.MakeModel;
import com.app.fijirentalcars.Model.RatingModel;
import com.app.fijirentalcars.Model.TransmissionType;
import com.app.fijirentalcars.SQLDB.DBHandler_BodyType;
import com.app.fijirentalcars.SQLDB.DBHandler_Feature;
import com.app.fijirentalcars.SQLDB.DBHandler_Transmission_type;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.PaginationScrollListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.slider.RangeSlider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, DialogItemListner {

    private GoogleMap mMap;
    MapView mapView;
    LinearLayout ll_recycle;
    ArrayList<CarModel> carModelArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    ImageView iv_filterback;
    ArrayList ImageUrl = new ArrayList();
    CarMapAdapter adapter;
    LinearLayoutManager layoutManager;
    // Index from which pagination should start (0 is 1st page in our case)
    private static final int PAGE_START = 1;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 0;

    TextView filter_apply, filter_reset, emptyView, tv_range, tv_distanceInclude, tv_transmission, tv_vehicleType, tv_yearType, tv_seatNumber, tv_carmake, tv_modelType, tv_feature;
    LinearLayout bottom_sheet, tv_map, tv_filter;
    AppCompatSpinner priceSpinner;
    LinearLayout subFeatureVeiw;
    RecyclerView fetureListView;
    List makeList = new ArrayList<>();
    MakeModel makeModel;
    CarModelType carModelType;
    List distanceInclude = Arrays.asList("At least 100 mi/day", "At least 200 mi/day", "At least 300 mi/day", "At least 400 mi/day", "At least 500 mi/day", "Unlimited distance");
    List seatList = Arrays.asList("All seats", "2 or more", "3 or more", "4 or more", "5 or more", "6 or more", "7 or more", "8 or more", "9 or more", "10 or more");

    List modelList = new ArrayList<>();

    List transmissionLsit = new ArrayList();
    List fuelTypeList = new ArrayList();
    List yearList = new ArrayList<>();
    List vehicleTypeList = new ArrayList();

    private DBHandler_Transmission_type dbHandler_transmission_type;
    private DBHandler_BodyType dbHandler_bodyType;
    private DBHandler_Feature dbHandler_feature;
    CarFeatureAdapter carFeatureAdapter;
    List<FutureModel> futureModel = new ArrayList<>();
    CustomDialog customDialog;

    RangeSlider slider;
    BottomSheetBehavior sheetBehavior;
    private boolean isDataChange = false;
    AppCompatCheckBox bookInstant, deliverDirect, topRated;
    List<String> ITEMS = Arrays.asList("Relevance", "LowToHigh", "HighToLow");
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Window window = this.getWindow();
        Log.e("MapActivity", "MapActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));


        init(savedInstanceState);
        implementlistiner();

    }

    private void init(Bundle savedInstanceState) {

        bottom_sheet = findViewById(R.id.bottom_sheet);

        dbHandler_transmission_type = new DBHandler_Transmission_type(MapActivity.this);
        dbHandler_bodyType = new DBHandler_BodyType(MapActivity.this);
        dbHandler_feature = new DBHandler_Feature(MapActivity.this);

        transmissionLsit.clear();
        TransmissionType transmissionType = new TransmissionType();
        transmissionType.setTransmissionTypeId("0");
        transmissionType.setTransmissionTypeTitle("All transmissions");
        transmissionLsit.add(transmissionType);
        transmissionLsit.addAll(dbHandler_transmission_type.getAllJobs());

        vehicleTypeList.clear();
        BodyType bodyType = new BodyType();
        bodyType.setBodyTypeTitle("All vehicle");
        bodyType.setBody_type_id("0");
        vehicleTypeList.add(bodyType);
        vehicleTypeList.addAll(dbHandler_bodyType.getAllJobs());


        futureModel.clear();
        futureModel.addAll(dbHandler_feature.getAllJobs());

        yearList.clear();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearList.add("All years");

        for (int startyear = (year); startyear >= 1950; startyear--) {
            yearList.add(String.valueOf(startyear));
        }

        filter_reset = findViewById(R.id.reset_filter);
        emptyView = findViewById(R.id.empty_view);
        tv_filter = findViewById(R.id.tv_filter);
        tv_range = findViewById(R.id.tv_range);
        filter_reset.setOnClickListener(this);
        tv_filter.setOnClickListener(this);
        filter_apply = findViewById(R.id.tv_apply);
        filter_apply.setOnClickListener(this);


        priceSpinner = findViewById(R.id.spinner_sort);
        slider = findViewById(R.id.range_slider);
        bookInstant = findViewById(R.id.book_instant);
        deliverDirect = findViewById(R.id.direct_deliver);
        topRated = findViewById(R.id.top_rated);
        iv_filterback = findViewById(R.id.iv_filterback);
        iv_filterback.setOnClickListener(this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this,
                R.layout.view_spinner_item,
                ITEMS
        );


        tv_distanceInclude = findViewById(R.id.tv_distance);
        tv_transmission = findViewById(R.id.tv_transmission);
        tv_vehicleType = findViewById(R.id.vehicle_type);
        tv_yearType = findViewById(R.id.tv_year);
        tv_seatNumber = findViewById(R.id.tv_number_seat);
        tv_carmake = findViewById(R.id.car_make);
        tv_modelType = findViewById(R.id.tv_model_type);
        fetureListView = findViewById(R.id.feature_list);

        fetureListView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        ListingCarModel listingCarModel = new ListingCarModel();
        listingCarModel.setFilterModel(true);
        carFeatureAdapter = new CarFeatureAdapter(this, futureModel, listingCarModel);
        fetureListView.setAdapter(carFeatureAdapter);

        subFeatureVeiw = findViewById(R.id.sub_fetaure_view);
        tv_feature = findViewById(R.id.tv_feature);

        tv_distanceInclude.setOnClickListener(this);
        tv_transmission.setOnClickListener(this);
        tv_vehicleType.setOnClickListener(this);
        tv_yearType.setOnClickListener(this);
        tv_seatNumber.setOnClickListener(this);
        tv_carmake.setOnClickListener(this);
        tv_modelType.setOnClickListener(this);
        tv_feature.setOnClickListener(this);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(spinnerAdapter);

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        ViewGroup.LayoutParams layoutParams = bottom_sheet.getLayoutParams();
        layoutParams.height = height - (height / 3);
        bottom_sheet.setLayoutParams(layoutParams);
        // sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        // btnBottomSheet.setText("Close Sheet");
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

            }
        });

        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                if (slider.getValues().get(1).intValue() == 300) {
                    tv_range.setText("$ " + slider.getValues().get(0).intValue() + " - $ " + slider.getValues().get(1).intValue() + "+/day");
                } else {
                    tv_range.setText("$ " + slider.getValues().get(0).intValue() + " - $ " + slider.getValues().get(1).intValue() + "/day");
                }
            }
        });

        ll_recycle = findViewById(R.id.ll_recycle);
//        mapView = (MapView) findViewById(R.id.map2);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CarMapAdapter(carModelArrayList, MapActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                //Increment page index to load the next one
                currentPage += 1;
                getcarlist();

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int pos = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findFirstVisibleItemPosition();

                changeMarkerView(pos);

            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return (TOTAL_PAGES==currentPage);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        if (FijiRentalUtils.isNetworkAvailable(MapActivity.this)) {
            currentPage=PAGE_START;
            getcarlist();
            getMakeList();
        }

    }

    private void changeMarkerView(int pos) {
        LatLng latLng = new LatLng(Double.parseDouble(carModelArrayList.get(pos).getModelLatitude()), Double.parseDouble(carModelArrayList.get(pos).getModelLongitude()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void implementlistiner() {
        ll_recycle.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMinZoomPreference(5);
//        mMap.setMaxZoomPreference(12);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car);
        // Add a marker in Sydney and move the camera
//        LatLng hotel2 = new LatLng(23.0561, 72.5962);
////        mMap.addMarker(new
////                MarkerOptions().position(hotel2).title("")).setIcon(icon);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(hotel2));
//        mMap.addMarker(new MarkerOptions().position(hotel2).title("Marker"));
//        mMap.setInfoWindowAdapter(new InfoWindowAdapter(this));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                int pos = (int) marker.getTag();
                recyclerView.scrollToPosition(pos);

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mapView.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_recycle) {
//            Intent i2 = new Intent(MapActivity.this, CarListActivity.class);
//            startActivity(i2);
            finish();
        } else if (v.getId() == R.id.tv_apply) {
            isDataChange = true;
            FijiRentalUtils.isApply = true;
            FijiRentalUtils.sortType = priceSpinner.getSelectedItem().toString().toLowerCase();
            FijiRentalUtils.minSort = String.valueOf((int) slider.getValues().get(0).intValue());
            FijiRentalUtils.maxSort = String.valueOf((int) slider.getValues().get(1).intValue());

            if (bookInstant.isChecked()) {
                FijiRentalUtils.isBookInstant = 1;
            } else {
                FijiRentalUtils.isBookInstant = 0;
            }

            if (deliverDirect.isChecked()) {
                FijiRentalUtils.isDeliverDirect = 1;
            } else {
                FijiRentalUtils.isDeliverDirect = 0;
            }

            if (topRated.isChecked()) {
                FijiRentalUtils.isTopRated = 1;
            } else {
                FijiRentalUtils.isTopRated = 0;
            }

            FijiRentalUtils.featureCarList = carFeatureAdapter.getFeatured();

            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            currentPage = 1;
            getcarlist();
        } else if (v.getId() == R.id.reset_filter) {
            FijiRentalUtils.isApply = false;
            FijiRentalUtils.minSort = "";
            FijiRentalUtils.maxSort = "";
            FijiRentalUtils.sortType = "";
            FijiRentalUtils.isTopRated = 0;
            FijiRentalUtils.isBookInstant = 0;
            FijiRentalUtils.isDeliverDirect = 0;
            FijiRentalUtils.distanceIncludePos = 0;
            FijiRentalUtils.carYearPos = 0;
            FijiRentalUtils.seatPos = 0;
            FijiRentalUtils.vehicleTypePos = 0;
            FijiRentalUtils.makeTypePos = 0;
            FijiRentalUtils.modelTypePos = 0;
            FijiRentalUtils.transmissionPos = 0;
            FijiRentalUtils.featureCarList = "";
            tv_modelType.setText("All models");
            carFeatureAdapter.resetFilter();

            isDataChange = true;
            currentPage = 1;
            getcarlist();
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (v.getId() == R.id.tv_filter) {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {


                tv_distanceInclude.setText((String) distanceInclude.get(FijiRentalUtils.distanceIncludePos));
                tv_yearType.setText((String) yearList.get(FijiRentalUtils.carYearPos));
                tv_seatNumber.setText((String) seatList.get(FijiRentalUtils.seatPos));

                if (vehicleTypeList != null) {
                    if (vehicleTypeList.size() > 0) {
                        tv_vehicleType.setText(((BodyType) vehicleTypeList.get(FijiRentalUtils.vehicleTypePos)).getBodyTypeTitle());
                    }
                }

                if (makeList != null) {
                    if (makeList.size() > 0) {
                        tv_carmake.setText(((MakeModel) makeList.get(FijiRentalUtils.makeTypePos)).getMakeDisplay());
                    }
                }

                if (modelList != null) {
                    if (modelList.size() > 0) {
                        tv_modelType.setText(((CarModelType) modelList.get(FijiRentalUtils.modelTypePos)).getModelName());

                    }
                }

                if (transmissionLsit != null) {
                    if (transmissionLsit.size() > 0) {
                        tv_transmission.setText(((TransmissionType) transmissionLsit.get(FijiRentalUtils.transmissionPos)).getTransmissionTypeTitle());
                    }
                }


                if (FijiRentalUtils.isApply) {


                    if (FijiRentalUtils.isBookInstant == 1) {
                        bookInstant.setChecked(true);
                    } else {
                        bookInstant.setChecked(false);
                    }
                    if (FijiRentalUtils.isDeliverDirect == 1) {
                        deliverDirect.setChecked(true);
                    } else {
                        deliverDirect.setChecked(false);
                    }
                    if (FijiRentalUtils.isTopRated == 1) {
                        topRated.setChecked(true);
                    } else {
                        topRated.setChecked(false);
                    }

                    priceSpinner.setSelection(getIndex(priceSpinner, FijiRentalUtils.sortType));

                    slider.setValues(Float.parseFloat(FijiRentalUtils.minSort), Float.parseFloat(FijiRentalUtils.maxSort));

                } else {
                    slider.setValues(10f, 300f);
                    deliverDirect.setChecked(false);
                    bookInstant.setChecked(false);
                    tv_distanceInclude.setText((String) distanceInclude.get(FijiRentalUtils.distanceIncludePos));
                    topRated.setChecked(false);
                }

                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        } else if (v.getId() == R.id.iv_filterback) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (v.getId() == R.id.tv_distance) {
            customDialog = new CustomDialog(this, distanceInclude, this);

            customDialog.show(getSupportFragmentManager(), "DISTANCE_INCLUDE");
        } else if (v.getId() == R.id.tv_transmission) {
            customDialog = new CustomDialog(this, transmissionLsit, this);

            customDialog.show(getSupportFragmentManager(), "TRANSMISSION");
        } else if (v.getId() == R.id.vehicle_type) {
            customDialog = new CustomDialog(this, vehicleTypeList, this);

            customDialog.show(getSupportFragmentManager(), "VEHICLE_LIST");
        } else if (v.getId() == R.id.tv_year) {
            customDialog = new CustomDialog(this, yearList, this);

            customDialog.show(getSupportFragmentManager(), "CAR_YEAR");
        } else if (v.getId() == R.id.tv_number_seat) {
            customDialog = new CustomDialog(this, seatList, this);

            customDialog.show(getSupportFragmentManager(), "SEAT_TYPE");
        } else if (v.getId() == R.id.tv_feature) {
            if (subFeatureVeiw.getVisibility() == View.VISIBLE) {
                subFeatureVeiw.setVisibility(View.GONE);
            } else {
                subFeatureVeiw.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.car_make) {
            customDialog = new CustomDialog(this, makeList, this);

            customDialog.show(getSupportFragmentManager(), "CAR_MAKE");
        } else if (v.getId() == R.id.tv_model_type) {
            if (modelList.size() == 0) {
                Toast.makeText(this, "Select make first", Toast.LENGTH_SHORT).show();
                return;
            }
            customDialog = new CustomDialog(this, modelList, this);

            customDialog.show(getSupportFragmentManager(), "CAR_MODEL");
        }

    }

    private int getIndex(AppCompatSpinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private String getStringSeat() {
        String[] seatPos = ((String) seatList.get(FijiRentalUtils.seatPos)).split("\\s+");

        return seatPos[0];

    }

    private void getcarlist() {


        final ProgressDialog progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        HashMap<String, String> data = new HashMap<>();
        if (FijiRentalUtils.isApply) {
            data.put("min_price_range", FijiRentalUtils.minSort);
            data.put("max_price_range", FijiRentalUtils.maxSort);
            data.put("book_instatnly", String.valueOf(FijiRentalUtils.isBookInstant));
            data.put("delivery_direct_to_you", String.valueOf(FijiRentalUtils.isDeliverDirect));
            data.put("toprated", String.valueOf(FijiRentalUtils.isTopRated));
            data.put("sort_price", String.valueOf(FijiRentalUtils.sortType));

            String[] selectedItems = FijiRentalUtils.featureCarList.split(",");
            if (selectedItems != null) {
                for (int i = 0; i < selectedItems.length; i++) {
                    data.put("feature_ids[" + i + "]", selectedItems[i]);
                }
            }

            if (FijiRentalUtils.vehicleTypePos != 0) {
                if (vehicleTypeList != null) {
                    if (vehicleTypeList.size() > 0) {
                        data.put("vehicle_type", ((BodyType) vehicleTypeList.get(FijiRentalUtils.vehicleTypePos)).getBody_type_id());
                    }
                }
            }

            if (FijiRentalUtils.makeTypePos != 0) {
                if (makeList != null) {
                    if (makeList.size() > 0) {
                        data.put("make", ((MakeModel) makeList.get(FijiRentalUtils.makeTypePos)).getMakeId());
                    }
                }
            }

            if (FijiRentalUtils.modelTypePos != 0) {
                if (modelList != null) {
                    if (modelList.size() > 0) {
                        data.put("model", ((CarModelType) modelList.get(FijiRentalUtils.modelTypePos)).getModelMakeId());
                    }
                }
            }

            if (FijiRentalUtils.transmissionPos != 0) {
                if (transmissionLsit != null) {
                    if (transmissionLsit.size() > 0) {
                        data.put("transmission_type_id", ((TransmissionType) transmissionLsit.get(FijiRentalUtils.transmissionPos)).getTransmissionTypeId());
                    }
                }
            }

            if (FijiRentalUtils.carYearPos != 0) {
                data.put("year", (String) yearList.get(FijiRentalUtils.carYearPos));
            }

            if (FijiRentalUtils.seatPos != 0) {
                data.put("number_of_seat", getStringSeat());
            }

            if(FijiRentalUtils.distanceIncludePos==distanceInclude.size()-1){
                data.put("model_daily_distance_included","UNLIMITED");
            }else {
                String[] dinclude=distanceInclude.get(FijiRentalUtils.distanceIncludePos).toString().split("\\s+");
                data.put("model_daily_distance_included",dinclude[2]);
            }
        }

        data.put("page", String.valueOf(currentPage));

        Call<ResponseBody> call = apiService.getcarslists(FijiRentalUtils.getAccessToken(MapActivity.this),
                String.valueOf(currentPage), "10", FijiRentalUtils.getId(MapActivity.this), data);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";
                isLoading=false;
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            JSONObject data = jsonobject.optJSONObject("data");
                            TOTAL_PAGES = Integer.parseInt(data.optString("max_num_pages"));
                            JSONArray data_array = data.optJSONArray("data");
                            passdata(data_array);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        isDataChange = false;
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MapActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isDataChange = false;
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MapActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                isDataChange = false;
                isLoading = false;
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MapActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

    private void passdata(JSONArray data_array) {

        if (isDataChange) {
            carModelArrayList = new ArrayList<>();
        }

        if (data_array.length() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            mMap.clear();
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < data_array.length(); i++) {
            JSONObject object = data_array.optJSONObject(i);
            CarModel carModel = new CarModel();
            carModel.setItemId(object.optString("item_id"));
            carModel.setItemSku(object.optString("item_sku"));
            carModel.setItemName(object.optString("item_name"));
            carModel.setItemPageId(object.optString("item_page_id"));
            carModel.setPartnerId(object.optString("partner_id"));
            carModel.setManufacturerId(object.optString("manufacturer_id"));
            carModel.setBodyTypeId(object.optString("body_type_id"));
            carModel.setTransmissionTypeId(object.optString("transmission_type_id"));
            carModel.setFuelTypeId(object.optString("fuel_type_id"));
            carModel.setModelName(object.optString("model_name"));
            carModel.setItemImage1(object.optString("item_image_1"));
            carModel.setItemImage2(object.optString("item_image_2"));
            carModel.setItemImage3(object.optString("item_image_3"));
            carModel.setMileage(object.optString("mileage"));
            carModel.setFuelConsumption(object.optString("fuel_consumption"));
            carModel.setEngineCapacity(object.optString("engine_capacity"));
            carModel.setMaxPassengers(object.optString("max_passengers"));
            carModel.setMaxLuggage(object.optString("max_luggage"));
            carModel.setItemDoors(object.optString("item_doors"));
            carModel.setMinDriverAge(object.optString("min_driver_age"));
            carModel.setPriceGroupId(object.optString("price_group_id"));
            carModel.setFixedRentalDeposit(object.optString("fixed_rental_deposit"));
            carModel.setUnitsInStock(object.optString("units_in_stock"));
            carModel.setUnitsInStock(object.optString("max_units_per_booking"));
            carModel.setOptionsMeasurementUnit(object.optString("options_measurement_unit"));
            carModel.setBlogId(object.optString("blog_id"));
            carModel.setModelYear(object.optString("model_year"));
            carModel.setModelSeats(object.optString("model_seats"));
            carModel.setModelDoors(object.optString("model_doors"));
            carModel.setModelLatitude(object.optString("model_latitude"));
            carModel.setModelLongitude(object.optString("model_longitude"));
            carModel.setPrice(object.optString("price"));
            carModel.setModelDescription(object.optString("model_description"));
            carModel.setModelCarLocation(object.optString("model_car_location"));
            carModel.setModelCarMake(object.optString("model_car_make"));
            carModel.setModelCarModel(object.optString("model_car_model"));
            carModel.setModelCarIsNotSalvaged(object.optString("model_car_isNotSalvaged"));
            carModel.setModelCarCommercialHost(object.optString("model_car_commercialHost"));
            carModel.setModelCarLicenseLicenseCountry(object.optString("model_car_license_licenseCountry"));
            carModel.setModelCarLicenseLicenseState(object.optString("model_car_license_licenseState"));
            carModel.setModelCarLicenseLicenseNumber(object.optString("model_car_license_licenseNumber"));
            carModel.setModelCarLicenseFirstName(object.optString("model_car_license_firstName"));
            carModel.setModelCarLicenseMiddleName(object.optString("model_car_license_middleName"));
            carModel.setModelCarLicenseLastName(object.optString("model_car_license_lastName"));
            carModel.setModelCarLicenseExpirationMonth(object.optString("model_car_license_expirationMonth"));
            carModel.setModelCarLicenseExpirationDay(object.optString("model_car_license_expirationDay"));
            carModel.setModelCarLicenseExpirationYear(object.optString("model_car_license_expirationYear"));
            carModel.setModelCarLicenseBirthMonth(object.optString("model_car_license_birthMonth"));
            carModel.setModelCarLicenseBirthYear(object.optString("model_car_license_birthYear"));
            carModel.setModelCarLicenseBirthDay(object.optString("model_car_license_birthDay"));
            carModel.setModelCarGoalsEarning(object.optString("model_car_goals_earning"));
            carModel.setModelCarGoalsOwnerUtilization(object.optString("model_car_goals_owner_utilization"));
            carModel.setModelCarGoalsRenterUtilization(object.optString("model_car_goals_renter_utilization"));
            carModel.setModelCarAvailabilityAdvanceNotice(object.optString("model_car_availability_advanceNotice"));
            carModel.setModelCarDurationMinimumTripDuration(object.optString("model_car_duration_minimumTripDuration"));
            carModel.setModelCarDurationLongerWeekendTripPreferred(object.optString("model_car_duration_longerWeekendTripPreferred"));
            carModel.setModelCarDurationMaximumTripDuration(object.optString("model_car_duration_maximumTripDuration"));
            carModel.setModelCarLicensePlateNumber(object.optString("model_car_licensePlateNumber"));
            carModel.setModelCarLicensePlateRegion(object.optString("model_car_licensePlateRegion"));
            carModel.setBookInstatnly(object.optString("book_instatnly"));
            carModel.setDeliveryDirectToYou(object.optString("delivery_direct_to_you"));
            carModel.setPickupDate(object.optString("pickup_date"));
            carModel.setReturnDate(object.optString("return_date"));

            JSONObject ratingObject = object.optJSONObject("ratings");

            RatingModel ratingModel = new RatingModel();
            ratingModel.setRatingCount(ratingObject.optString("rating_count"));
            ratingModel.setAvg_rating(ratingObject.optInt("average_rating"));

            carModel.setRatingModel(ratingModel);

//            JSONArray futuresArray = object.optJSONArray("futures");
//
//            List<FutureModel> futureList = new ArrayList<>();
//
//            for (int k = 0; k < futuresArray.length(); k++) {
//                JSONObject futureObject = futuresArray.optJSONObject(k);
//
//                FutureModel futureModel = new FutureModel();
//                futureModel.setFeatureId(futureObject.optString("feature_id"));
//                futureModel.setFeatureTitle(futureObject.optString("feature_title"));
//
//                futureList.add(futureModel);
//            }

            carModel.setFuelTypeTitle(object.optString("fuel_type_title"));
            carModel.setTransmissionTypeTitle(object.optString("transmission_type_title"));
            carModel.setBodyTypeTitle(object.optString("body_type_title"));
            carModel.setManufacturerTitle(object.optString("manufacturer_title"));

//            JSONObject partnerObject = object.optJSONObject("partner_detail");
//
//            PartnerDetail partnerDetail = new PartnerDetail();
//            partnerDetail.setUserPicUrl(partnerObject.optString("user_pic_url"));
//            partnerDetail.setFirstName(partnerObject.optString("first_name"));
//            partnerDetail.setLastName(partnerObject.optString("last_name"));
//            partnerDetail.setTotalTrip(partnerObject.optInt("total_trip"));
//            partnerDetail.setJoined(partnerObject.optString("joined"));
//            partnerDetail.setID(partnerObject.optInt("ID"));
            carModel.setIs_favourites(object.optString("is_favourites"));
            JSONArray photos = object.optJSONArray("photos");

            ImageUrl.clear();
            for (int j = 0; j < photos.length(); j++) {
                ImageUrl.add(photos.optString(j));

            }
            carModel.setPhotos(ImageUrl);
//            carModel.setPartnerDetail(partnerDetail);

            carModelArrayList.add(carModel);
        }

        if (isDataChange) {
            isDataChange = false;
            adapter.updateDate(carModelArrayList);
        } else {
            adapter.notifyDataSetChanged();
        }

        addMarkers();

    }


    private void addMarkers() {
        for (int i = 0; i < carModelArrayList.size(); i++) {
            CarModel carModel = carModelArrayList.get(i);
            Double latitude = Double.valueOf(carModel.getModelLatitude());
            Double longitude = Double.valueOf(carModel.getModelLongitude());
            LatLng destination = new LatLng(latitude, longitude);

//            Bitmap bitmap = GetBitmapMarker(getApplicationContext(), R.drawable.ic_action_chat_selected, carModel.getPrice());


            MarkerOptions markerOptions = new MarkerOptions().position(destination);
            if (i == 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
            }

            Marker marker = mMap.addMarker(createMarker(this, destination, (carModel.getPrice())));
            marker.setTag(i);
            marker.showInfoWindow();
//            marker.setTitle(carModel.getPrice());
        }
    }

    public static MarkerOptions createMarker(Context context, LatLng point, String bedroomCount) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(point);
        int px = context.getResources().getDimensionPixelSize(R.dimen._50sdp);
        View markerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_info_window, null);
        markerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        markerView.layout(0, 0, px, px);
        markerView.buildDrawingCache();
        TextView bedNumberTextView = (TextView) markerView.findViewById(R.id.text_price);
        Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
        bedNumberTextView.setText("$ " + bedroomCount);
        markerView.draw(canvas);
        marker.icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap));
        return marker;
    }


    @Override
    public void onItemClick(Object val) {
        if (customDialog.getTag().equals("DISTANCE_INCLUDE")) {
            tv_distanceInclude.setText((String) val);
            FijiRentalUtils.distanceIncludePos = distanceInclude.indexOf(val);
        }
        if (customDialog.getTag().equals("TRANSMISSION")) {
            TransmissionType transmissionModel = (TransmissionType) val;

            FijiRentalUtils.transmissionPos = transmissionLsit.indexOf(val);
            tv_transmission.setText(transmissionModel.getTransmissionTypeTitle());
        }
        if (customDialog.getTag().equals("VEHICLE_LIST")) {
            BodyType transmissionModel = (BodyType) val;
            FijiRentalUtils.vehicleTypePos = vehicleTypeList.indexOf(val);
            tv_vehicleType.setText(transmissionModel.getBodyTypeTitle());
        }
        if (customDialog.getTag().equals("CAR_YEAR")) {

            FijiRentalUtils.carYearPos = yearList.indexOf(val);

            tv_yearType.setText((String) val);
        }
        if (customDialog.getTag().equals("SEAT_TYPE")) {

            FijiRentalUtils.seatPos = seatList.indexOf(val);

            tv_seatNumber.setText((String) val);
        }
        if (customDialog.getTag().equals("CAR_MAKE")) {
            makeModel = (MakeModel) val;
            tv_carmake.setText(makeModel.getMakeDisplay());

            FijiRentalUtils.makeTypePos = makeList.indexOf(val);

            tv_modelType.setText("All models");
            carModelType = null;

            if (makeModel.getMakeId().equals("0")) {
                FijiRentalUtils.modelTypePos = 0;
                modelList.clear();
            } else {
                getModelList(makeModel);
            }
        }
        if (customDialog.getTag().equals("CAR_MODEL")) {
            carModelType = (CarModelType) val;
            FijiRentalUtils.modelTypePos = modelList.indexOf(val);
            tv_modelType.setText(carModelType.getModelName());
        }
        customDialog.dismiss();
    }

    private void getModelList(MakeModel makeModel) {
        final ProgressDialog progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        HashMap<String, String> data = new HashMap<>();
        data.put("make", makeModel.getMakeId());

        Call<ResponseBody> call = apiService.getModelList(FijiRentalUtils.getAccessToken(this), data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            JSONObject data = jsonobject.optJSONObject("data");
                            JSONArray data_array = data.optJSONArray("data");
                            modelList.clear();
                            CarModelType modelType = new CarModelType();
                            modelType.setModelMakeId("0");
                            modelType.setModelName("All models");
                            modelList.add(modelType);
                            for (int i = 0; i < data_array.length(); i++) {
                                JSONObject object = data_array.optJSONObject(i);
                                CarModelType carModelType = new CarModelType();
                                carModelType.setModelName(object.optString("model_name"));
                                carModelType.setModelMakeId(object.optString("model_make_id"));
                                modelList.add(carModelType);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MapActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MapActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    private void getMakeList() {
        final ProgressDialog progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        HashMap<String, String> data = new HashMap<>();
        data.put("year", "");

        Call<ResponseBody> call = apiService.getMakeList(FijiRentalUtils.getAccessToken(this), data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            JSONObject data = jsonobject.optJSONObject("data");
                            JSONArray data_array = data.optJSONArray("data");
                            makeList.clear();
                            MakeModel model = new MakeModel();
                            model.setMakeId("0");
                            model.setMakeDisplay("All makes");
                            makeList.add(model);

                            for (int i = 0; i < data_array.length(); i++) {
                                JSONObject object = data_array.optJSONObject(i);
                                MakeModel makeModel = new MakeModel();
                                makeModel.setMakeId(object.optString("make_id"));
                                makeModel.setMakeDisplay(object.optString("make_display"));
                                makeModel.setMakeIsCommon(object.optString("make_is_common"));
                                makeModel.setMakeCountry(object.optString("make_country"));


                                makeList.add(makeModel);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MapActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MapActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }
}