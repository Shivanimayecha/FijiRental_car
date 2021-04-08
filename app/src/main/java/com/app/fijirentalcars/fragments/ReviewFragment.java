package com.app.fijirentalcars.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fijirentalcars.Adapter.PopUpAdapter;
import com.app.fijirentalcars.Adapter.PopupSubAdapter;
import com.app.fijirentalcars.Adapter.ReviewAdapter;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.RatingModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment implements PopUpAdapter.OnSelectListner, PopupSubAdapter.ItemSelect, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    ProgressDialog progressDialog;
    private String mParam2;
    LinearLayout dayDialog,vehicleDialog;

    private static final int PAGE_START = 1;
    APIService apiService;

    ArrayList<CarModel> carModelArrayList = new ArrayList<>();
    PopupSubAdapter popupSubAdapter;

    List itemList= Arrays.asList("Last 30 days","Last 90 days","Last 180 days","Last 365 days","All time");
    BottomSheetDialog dialog;


    private boolean isLoading = false;

    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;

    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 0;

    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;

    public ReviewFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        popupSubAdapter = new PopupSubAdapter(getContext(),carModelArrayList,this);
        getVehicleData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reviews, container, false);
        Log.e("ReviewFragment","ReviewFragment");


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewreview);
        dayDialog=view.findViewById(R.id.iv_days);
        vehicleDialog=view.findViewById(R.id.tv_allvehicles);



        dayDialog.setOnClickListener(this);
        vehicleDialog.setOnClickListener(this);

        ReviewAdapter adapter = new ReviewAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getReviewList();

    }

    private void getReviewList() {


//        progressDialog.show();


        HashMap<String ,String > data=new HashMap<>();
        Call<ResponseBody> call = apiService.getcarslists(FijiRentalUtils.getAccessToken(getActivity()),
                String.valueOf(currentPage), "10", FijiRentalUtils.getId(getActivity()),data);
    }

    private void getVehicleData() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        FijiRentalUtils.Logger("YAGS","Review frag");
        Call<ResponseBody> call = apiService.listMyCar(FijiRentalUtils.getAccessToken(getActivity()),
                String.valueOf(currentPage));


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String message = "";
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;

                    jsonobject = new JSONObject(jstr);
                    message = jsonobject.optString("message");
                    if (jsonobject.optString("code").matches("200")) {
                        JSONObject data = jsonobject.optJSONObject("data");
                        TOTAL_PAGES = Integer.parseInt(data.optString("max_num_pages"));
                        JSONArray data_array = data.optJSONArray("data");
                        passdata(data_array);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getActivity(), "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoading = false;
                progressDialog.dismiss();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getActivity(), "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

    private void passdata(JSONArray data_array) {

        carModelArrayList.clear();
        CarModel demoModel=new CarModel();
        demoModel.setItemName("All");
        demoModel.setSelected(true);

        carModelArrayList.add(demoModel);

        for (int i = 0; i < data_array.length(); i++) {
            JSONObject object = data_array.optJSONObject(i);
            CarModel carModel = new CarModel();
            carModel.setItemId(object.optString("item_id"));
            carModel.setItemName(object.optString("item_name"));
            carModel.setItemSku(object.optString("item_sku"));
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
            carModel.setLast_trip(object.optString("last_trip"));
            carModel.setTotal_trip(object.optInt("total_trip"));
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
            carModel.setFuelTypeTitle(object.optString("fuel_type_title"));
            carModel.setTransmissionTypeTitle(object.optString("transmission_type_title"));
            carModel.setBodyTypeTitle(object.optString("body_type_title"));
            carModel.setManufacturerTitle(object.optString("manufacturer_title"));
            carModel.setCarNumber(object.optString("model_car_licensePlateNumber"));
            carModel.setIs_favourites(object.optString("is_favourites"));

            JSONArray photos = object.optJSONArray("photos");

            carModelArrayList.add(carModel);
        }

        popupSubAdapter.notifyDataSetChanged();





    }

    @Override
    public void onItemSelect(CarModel carModel) {
        dialog.dismiss();
    }

    @Override
    public void onSelect(String val) {
        FijiRentalUtils.ReviewDay=val;
        dialog.dismiss();

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_days){
            dialog = new BottomSheetDialog(getContext());
            View dialogView= LayoutInflater.from(getContext()).inflate(R.layout.performance_period,null);

            RecyclerView recyclerView,subListView;
            recyclerView = dialogView.findViewById(R.id.item_list);
            subListView = dialogView.findViewById(R.id.car_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            subListView.setLayoutManager(new LinearLayoutManager(getContext()));
            TextView resetBtn,popupHeading;
            LinearLayout sublayout=dialogView.findViewById(R.id.subLayout);
            sublayout.setVisibility(View.GONE);
            resetBtn=dialogView.findViewById(R.id.reset_btn);
            popupHeading=dialogView.findViewById(R.id.popup_heading);
            popupHeading.setText("All");
            resetBtn.setVisibility(View.VISIBLE);

            resetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            LinearLayout searchClose=dialogView.findViewById(R.id.search_close_btn);
            searchClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });



            subListView.setAdapter(popupSubAdapter);

            PopUpAdapter popUpAdapter=new PopUpAdapter(getContext(),itemList,this, FijiRentalUtils.ReviewDay);
            recyclerView.setAdapter(popUpAdapter);
            dialog.setContentView(dialogView);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                    FrameLayout bottomSheet = (FrameLayout)
                            bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    layoutParams.height = height-(height/3);
                    bottomSheet.setLayoutParams(layoutParams);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
            dialog.show();

        }else if(v.getId()==R.id.tv_allvehicles){
            dialog = new BottomSheetDialog(getContext());
            View dialogView= LayoutInflater.from(getContext()).inflate(R.layout.performance_period,null);

            RecyclerView recyclerView,subListView;
            recyclerView = dialogView.findViewById(R.id.item_list);
            subListView = dialogView.findViewById(R.id.car_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            subListView.setLayoutManager(new LinearLayoutManager(getContext()));
            TextView resetBtn,popupHeading;
            LinearLayout sublayout=dialogView.findViewById(R.id.subLayout);
            sublayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            resetBtn=dialogView.findViewById(R.id.reset_btn);
            popupHeading=dialogView.findViewById(R.id.popup_heading);
            popupHeading.setText("All");
            resetBtn.setVisibility(View.VISIBLE);

            resetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            LinearLayout searchClose=dialogView.findViewById(R.id.search_close_btn);
            searchClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });



            subListView.setAdapter(popupSubAdapter);
            dialog.setContentView(dialogView);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                    FrameLayout bottomSheet = (FrameLayout)
                            bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    layoutParams.height = height-(height/3);
                    bottomSheet.setLayoutParams(layoutParams);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
            dialog.show();

        }
    }
}
