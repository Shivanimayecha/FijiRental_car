package com.app.fijirentalcars;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.app.fijirentalcars.Adapter.CarFeatureAdapter;
import com.app.fijirentalcars.Adapter.CarListAdapter;
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
import com.app.fijirentalcars.util.HRDateUtil;
import com.app.fijirentalcars.util.PaginationScrollListener;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.slider.RangeSlider;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*import butterknife.BindView;
import butterknife.ButterKnife;*/

public class CarListActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner {

    TextView tv_heading, filter_apply, filter_reset, emptyView, tv_range, tv_distanceInclude, tv_transmission, tv_vehicleType, tv_yearType, tv_seatNumber, tv_carmake, tv_modelType, tv_feature;
    LinearLayout ll_main, bottom_sheet, tv_map, tv_filter;
    ImageView iv_dropdown, iv_filterback, iv_back;
    RelativeLayout pupUpview, dateSelection;
    LinearLayout subFeatureVeiw;
    RecyclerView fetureListView;
    PopupWindow popupWindow;
    List makeList = new ArrayList<>();
    MakeModel makeModel;
    CarModelType carModelType;
    CoordinatorLayout ll_layout;
    ArrayList<CarModel> carModelArrayList = new ArrayList<>();
    ArrayList ImageUrl = new ArrayList();
    RecyclerView recyclerView;
    List distanceInclude = Arrays.asList("At least 100 mi/day", "At least 200 mi/day", "At least 300 mi/day", "At least 400 mi/day", "At least 500 mi/day", "Unlimited distance");
    List seatList = Arrays.asList("All seats", "2 or more", "3 or more", "4 or more", "5 or more", "6 or more", "7 or more", "8 or more", "9 or more", "10 or more");
    CarListAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    AppCompatSpinner priceSpinner;
    RangeSlider slider;
    BottomSheetBehavior sheetBehavior;
    CustomDialog customDialog;
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
    PlacesClient placesClient;
    Place place;
    TextView placeName, searchBtn, tv_tourdate, tv_startdate, tv_enddate;
    LocalDate Startdate, EndDate;

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    // Set the fields to specify which types of place data to
    // return after the user has made a selection.
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

    AppCompatCheckBox bookInstant, deliverDirect, topRated;
    List<String> ITEMS = Arrays.asList("Relevance", "LowToHigh", "HighToLow");
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isDataChange = false;

    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    private String startTime = "10:00 AM";
    private String endTime = "10:00 AM";
    private int DATE_PICKER = 502;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        //     ButterKnife.bind(this);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        Log.e("CarListActivity", "CarListActivity");


        init();

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */

    }


    public void init() {

        place = Paper.book().read("PLACE");

        placesClient = Places.createClient(this);

        dbHandler_transmission_type = new DBHandler_Transmission_type(CarListActivity.this);
        dbHandler_bodyType = new DBHandler_BodyType(CarListActivity.this);
        dbHandler_feature = new DBHandler_Feature(CarListActivity.this);

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

        tv_heading = findViewById(R.id.tv_heading);
        tv_tourdate = findViewById(R.id.tour_date);


        Startdate = HRDateUtil.getTodaysDate().plusDays(8 - HRDateUtil.getTodaysDate().getDayOfWeek());
        EndDate = Startdate.plusDays(3);
        tv_tourdate.setText(Startdate.getDayOfMonth() + " " + Startdate.monthOfYear().getAsShortText(Locale.getDefault()) + ", " + startTime + " - " + EndDate.getDayOfMonth() + " " + EndDate.monthOfYear().getAsShortText(Locale.getDefault()) + ", " + endTime);
//        tv_heading.setOnClickListener(this);
        tv_map = findViewById(R.id.tv_map);
        tv_map.setOnClickListener(this);
        tv_filter = findViewById(R.id.tv_filter);
        tv_range = findViewById(R.id.tv_range);
        filter_reset = findViewById(R.id.reset_filter);
        emptyView = findViewById(R.id.empty_view);
        tv_filter.setOnClickListener(this);
        filter_reset.setOnClickListener(this);

        filter_apply = findViewById(R.id.tv_apply);
        filter_apply.setOnClickListener(this);

        ll_layout = findViewById(R.id.ll_layout);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        iv_dropdown = findViewById(R.id.iv_dropdown);
//        iv_dropdown.setOnClickListener(this);
        pupUpview = findViewById(R.id.popUp);
        pupUpview.setOnClickListener(this);

        priceSpinner = findViewById(R.id.spinner_sort);
        slider = findViewById(R.id.range_slider);
        bookInstant = findViewById(R.id.book_instant);
        deliverDirect = findViewById(R.id.direct_deliver);
        topRated = findViewById(R.id.top_rated);
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

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this,
                R.layout.view_spinner_item,
                ITEMS
        );
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

        iv_filterback = findViewById(R.id.iv_filterback);
        iv_filterback.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CarListAdapter(carModelArrayList, CarListActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {


                isLoading = true;
                //Increment page index to load the next one
                currentPage += 1;
                getcarlist();


            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return (TOTAL_PAGES == currentPage);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        if (place != null) {
            tv_heading.setText(place.getName());
        }

        if (FijiRentalUtils.isNetworkAvailable(CarListActivity.this)) {
            isLoading = true;
            currentPage = 1;
            getcarlist();
            getMakeList();
        }


    }

    @Override
    public void onBackPressed() {

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

//            case R.id.tv_heading:
//                Intent i = new Intent(this, FillDetailCar2Activity.class);
//                startActivity(i);
//                break;

            case R.id.tv_map:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_apply:
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
                break;
            case R.id.tv_filter:
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
                        topRated.setChecked(false);
                        tv_distanceInclude.setText((String) distanceInclude.get(FijiRentalUtils.distanceIncludePos));
                    }

                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.reset_filter:
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

                break;

            case R.id.popUp:
                LayoutInflater layoutInflater = (LayoutInflater) CarListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup_search_layout, null);
                ImageView iv_back;


                placeName = customView.findViewById(R.id.placeName);
                dateSelection = customView.findViewById(R.id.date_selection);
                tv_startdate = customView.findViewById(R.id.tv_startdate);
                tv_enddate = customView.findViewById(R.id.endDate);
                searchBtn = customView.findViewById(R.id.searchBtn);

                tv_startdate.setText(Startdate.getDayOfMonth() + " " + Startdate.monthOfYear().getAsShortText(Locale.getDefault()) + ", " + startTime);
                tv_enddate.setText(EndDate.getDayOfMonth() + " " + EndDate.monthOfYear().getAsShortText(Locale.getDefault()) + ", " + endTime);

                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                dateSelection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("TAG", "STARTDATE:-- " + Startdate.toString());
                        Log.e("TAG", "ENDDATE:-- " + EndDate.toString());
                        Intent dateSelection = new Intent(CarListActivity.this, RouteDatePicker.class);
                        dateSelection.putExtra("STARTDATE", Startdate.toString());
                        dateSelection.putExtra("ENDDATE", EndDate.toString());
                        startActivityForResult(dateSelection, DATE_PICKER);

                    }
                });

                if (place != null) {
                    placeName.setText(place.getName());
                }
                RelativeLayout placeSearch;
                iv_back = (ImageView) customView.findViewById(R.id.iv_back);
                placeSearch = customView.findViewById(R.id.place_search);
                //instantiate popup window
                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                //display the popup window
                popupWindow.showAtLocation(ll_layout, Gravity.TOP, 0, 0);
                //close the popup window on button click
                iv_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                placeSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                                .build(CarListActivity.this);
                        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                    }
                });

                break;
            case R.id.iv_filterback:
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_distance:
                customDialog = new CustomDialog(this, distanceInclude, this);

                customDialog.show(getSupportFragmentManager(), "DISTANCE_INCLUDE");
                break;
            case R.id.tv_transmission:
                customDialog = new CustomDialog(this, transmissionLsit, this);

                customDialog.show(getSupportFragmentManager(), "TRANSMISSION");
                break;
            case R.id.vehicle_type:
                customDialog = new CustomDialog(this, vehicleTypeList, this);

                customDialog.show(getSupportFragmentManager(), "VEHICLE_LIST");
                break;
            case R.id.tv_year:
                customDialog = new CustomDialog(this, yearList, this);

                customDialog.show(getSupportFragmentManager(), "CAR_YEAR");
                break;
            case R.id.tv_number_seat:
                customDialog = new CustomDialog(this, seatList, this);

                customDialog.show(getSupportFragmentManager(), "SEAT_TYPE");
                break;
            case R.id.tv_feature:
                if (subFeatureVeiw.getVisibility() == View.VISIBLE) {
                    subFeatureVeiw.setVisibility(View.GONE);
                } else {
                    subFeatureVeiw.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.car_make:
                customDialog = new CustomDialog(this, makeList, this);

                customDialog.show(getSupportFragmentManager(), "CAR_MAKE");
                break;
            case R.id.tv_model_type:
                if (modelList.size() == 0) {
                    Toast.makeText(this, "Select make first", Toast.LENGTH_SHORT).show();
                    return;
                }
                customDialog = new CustomDialog(this, modelList, this);

                customDialog.show(getSupportFragmentManager(), "CAR_MODEL");
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                place = Autocomplete.getPlaceFromIntent(data);
                Paper.book().write("PLACE", place);
                tv_heading.setText(place.getName());
                if (placeName != null) {
                    placeName.setText(place.getName());
                }

                getcarlist();

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

        if (requestCode == DATE_PICKER) {
            if (resultCode == RESULT_OK) {
                Startdate = LocalDate.parse(data.getStringExtra("start_date"));
                EndDate = LocalDate.parse(data.getStringExtra("end_date"));
                startTime = data.getStringExtra("start_date_time");
                endTime = data.getStringExtra("end_date_time");

                tv_tourdate.setText(Startdate.getDayOfMonth() + " " + Startdate.monthOfYear().getAsShortText(Locale.getDefault()) + ", " + startTime + " - " + EndDate.getDayOfMonth() + " " + EndDate.monthOfYear().getAsShortText(Locale.getDefault()) + ", " + endTime);
                if (tv_startdate != null) {
                    tv_startdate.setText(Startdate.getDayOfMonth() + " " + Startdate.monthOfYear().getAsShortText(Locale.getDefault()) + ", " + startTime);
                    tv_enddate.setText(EndDate.getDayOfMonth() + " " + EndDate.monthOfYear().getAsShortText(Locale.getDefault()) + ", " + endTime);
                }
            }
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

    private void getMakeList() {
        final ProgressDialog progressDialog = new ProgressDialog(CarListActivity.this);
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
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, CarListActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, CarListActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    private void getcarlist() {


        final ProgressDialog progressDialog = new ProgressDialog(CarListActivity.this);
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

            if (place != null) {
                data.put("latitude", String.valueOf(place.getLatLng().latitude));
                data.put("longitude", String.valueOf(place.getLatLng().longitude));
            }

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

            if (FijiRentalUtils.distanceIncludePos == distanceInclude.size() - 1) {
                data.put("model_daily_distance_included", "UNLIMITED");
            } else {
                String[] dinclude = distanceInclude.get(FijiRentalUtils.distanceIncludePos).toString().split("\\s+");
                data.put("model_daily_distance_included", dinclude[2]);
            }


        }
        data.put("page", String.valueOf(currentPage));
        data.put("to", FijiRentalUtils.chagneDateFormate(Startdate, startTime));
        data.put("from", FijiRentalUtils.chagneDateFormate(EndDate, endTime));


        Call<ResponseBody> call = apiService.getcarslists(FijiRentalUtils.getAccessToken(CarListActivity.this),
                String.valueOf(currentPage), "10", FijiRentalUtils.getId(CarListActivity.this), data);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";
                progressDialog.dismiss();
                isLoading = false;


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
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, CarListActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isDataChange = false;
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, CarListActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                isDataChange = false;
                isLoading = false;
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, CarListActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

    private String getStringSeat() {
        String[] seatPos = ((String) seatList.get(FijiRentalUtils.seatPos)).split("\\s+");

        return seatPos[0];

    }

    private void passdata(JSONArray data_array) {

        if (isDataChange) {
            carModelArrayList = new ArrayList<>();
        }

        if (data_array.length() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < data_array.length(); i++) {
            JSONObject object = data_array.optJSONObject(i);
            CarModel carModel = new CarModel();
            carModel.setItemId(object.optString("item_id"));
            carModel.setItemSku(object.optString("item_sku"));
            carModel.setItemPageId(object.optString("item_page_id"));
            carModel.setPartnerId(object.optString("partner_id"));
            carModel.setManufacturerId(object.optString("manufacturer_id"));
            carModel.setBodyTypeId(object.optString("body_type_id"));
            carModel.setTransmissionTypeId(object.optString("transmission_type_id"));
            carModel.setFuelTypeId(object.optString("fuel_type_id"));
            carModel.setModelName(object.optString("model_name"));
            carModel.setItemName(object.optString("item_name"));
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
        final ProgressDialog progressDialog = new ProgressDialog(CarListActivity.this);
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
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, CarListActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, CarListActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }
}