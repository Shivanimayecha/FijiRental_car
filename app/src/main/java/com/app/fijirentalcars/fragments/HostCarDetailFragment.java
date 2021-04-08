package com.app.fijirentalcars.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.fijirentalcars.ActivityVerticalCalendar;
import com.app.fijirentalcars.DistanceInclude;
import com.app.fijirentalcars.ExtrasList;
import com.app.fijirentalcars.GuestInstruction;
import com.app.fijirentalcars.HostcarDetailsActivtiy;
import com.app.fijirentalcars.ListingStatus;
import com.app.fijirentalcars.LocationDeliveryActivity;
import com.app.fijirentalcars.ManualPricingActivity;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.PhotoActivity;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.TripPreference;
import com.app.fijirentalcars.VehicleProtection;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class HostCarDetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CarModel carModel;
    TextView carYear, carModelName, totalTrip, carPlateNumber, editListing,listingStatus,statusMag,carStatusIndicator;
    RelativeLayout calenderScreen, carPricing, carLocation, carPhotos, guestInstruct, carExtras, distanceInclude, carPref, carProtection, carDetails;
    ImageView carImage, carBack;
    APIService apiService;
    ProgressDialog progressDialog;
    public HostCarDetailFragment() {
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
    public static HostCarDetailFragment newInstance(String param1, String param2) {
        HostCarDetailFragment fragment = new HostCarDetailFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            carModel = (CarModel) this.getArguments().getSerializable(FijiRentalUtils.CarDetailIntent);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.show();
            updateCarModel();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hostcardetail, container, false);
        calenderScreen = view.findViewById(R.id.iv_calender);
        carPricing = view.findViewById(R.id.rl_pricing);
        carLocation = view.findViewById(R.id.rl_location);
        carPhotos = view.findViewById(R.id.rl_photos);
        carExtras = view.findViewById(R.id.rl_extras);
        carImage = view.findViewById(R.id.car_image);
        statusMag=view.findViewById(R.id.status_msg);
        carBack = view.findViewById(R.id.iv_back);
        editListing = view.findViewById(R.id.edit_listing);
        carYear = view.findViewById(R.id.car_year);
        listingStatus = view.findViewById(R.id.listing_status);
        carDetails = view.findViewById(R.id.rl_carDetails);
        carModelName = view.findViewById(R.id.car_model);
        totalTrip = view.findViewById(R.id.car_trip);
        guestInstruct = view.findViewById(R.id.guest_instruct);
        carPlateNumber = view.findViewById(R.id.car_plate);
        distanceInclude = view.findViewById(R.id.rl_distance_include);
        carPref = view.findViewById(R.id.rl_trip_pref);
        carProtection = view.findViewById(R.id.rl_protection);
        carStatusIndicator = view.findViewById(R.id.car_status_icon);


        calenderScreen.setOnClickListener(this);
        carPricing.setOnClickListener(this);
        carLocation.setOnClickListener(this);
        carPhotos.setOnClickListener(this);
        carBack.setOnClickListener(this);
        guestInstruct.setOnClickListener(this);
        carExtras.setOnClickListener(this);
        distanceInclude.setOnClickListener(this);
        carDetails.setOnClickListener(this);
        editListing.setOnClickListener(this);
        carPref.setOnClickListener(this);
        carProtection.setOnClickListener(this);


        Log.e("HostCarDetailFragment", "HostCarDetailFragment");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (carModel != null) {

            Glide.with(getContext()).load(FijiRentalUtils.BaseImageUrl + carModel.getItemImage1()).placeholder(R.drawable.car1).into(carImage);
            carYear.setText(carModel.getModelYear());
            carModelName.setText(carModel.getItemName());
            totalTrip.setText(String.valueOf(carModel.getTotal_trip()));
            carPlateNumber.setText(carModel.getModelCarLicensePlateNumber());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
       carModel=Paper.book().read(FijiRentalUtils.carModel);
        updateView(carModel);


    }

    private void updateView(CarModel carModel) {
        if(carModel!=null){
            if(carModel.getEnabled().equals("0")){
                listingStatus.setText("Unlisted");
                carStatusIndicator.setTextColor(getResources().getColor(R.color.red));
                statusMag.setText("Your car won't appear in search results and can't be requested untill listed again.");
            }else if(carModel.getEnabled().equals("1")){
                listingStatus.setText("Listed");
                carStatusIndicator.setTextColor(getResources().getColor(R.color.green));
                statusMag.setText("Your car appears in search results and can be requested.");
            }else if(carModel.getEnabled().equals("2")){
                listingStatus.setText("Snoozed");
                carStatusIndicator.setTextColor(getResources().getColor(R.color.quantum_yellow));
                statusMag.setText("Your car won't appear in search results and can't be requested untill snooze ends.");
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_calender:
                Intent i = new Intent(getContext(), ActivityVerticalCalendar.class);
                getContext().startActivity(i);
                break;

            case R.id.rl_pricing:
                Intent pricingI = new Intent(getContext(), ManualPricingActivity.class);
                Objects.requireNonNull(getContext()).startActivity(pricingI);
                break;
            case R.id.guest_instruct:
                Intent guestI = new Intent(getContext(), GuestInstruction.class);
                Objects.requireNonNull(getContext()).startActivity(guestI);
                break;
            case R.id.iv_back:
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
                break;

            case R.id.rl_location:
                Intent location = new Intent(getContext(), LocationDeliveryActivity.class);
                Objects.requireNonNull(getContext()).startActivity(location);
                break;
            case R.id.rl_photos:
                Intent photos = new Intent(getContext(), PhotoActivity.class);
                photos.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
                Objects.requireNonNull(getContext()).startActivity(photos);
                break;
            case R.id.rl_extras:
                Intent extraIntent = new Intent(getContext(), ExtrasList.class);
                Objects.requireNonNull(getContext()).startActivity(extraIntent);
                break;
            case R.id.rl_distance_include:
                Intent disIntent = new Intent(getContext(), DistanceInclude.class);
                Objects.requireNonNull(getContext()).startActivity(disIntent);
                break;

            case R.id.rl_trip_pref:
                Intent carPref = new Intent(getContext(), TripPreference.class);
                Objects.requireNonNull(getContext()).startActivity(carPref);
                break;
            case R.id.rl_protection:
                Intent protectionIntent = new Intent(getContext(), VehicleProtection.class);
                startActivity(protectionIntent);
                break;
            case R.id.edit_listing:
                Intent editIntent = new Intent(getContext(), ListingStatus.class);
                getActivity().startActivityForResult(editIntent,FijiRentalUtils.UPDATE_LISTING_STATUS);
                break;
            case R.id.rl_carDetails:
                Intent detailIntent = new Intent(getContext(), HostcarDetailsActivtiy.class);
                startActivity(detailIntent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        Paper.book().delete(FijiRentalUtils.carModel);
        super.onDestroy();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FijiRentalUtils.UPDATE_LISTING_STATUS){
            if(resultCode==RESULT_OK){
                String result=data.getStringExtra("result");


                if(result.equals("delete")){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        }

    }

    public void updateCarModel(){
        apiService = Config.getClient().create(APIService.class);
        HashMap<String, String> data = new HashMap<>();
        data.put("item_id", carModel.getItemId());
        Call<ResponseBody> call = apiService.getCarDetails(FijiRentalUtils.getAccessToken(getContext()), data);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                String message = "";

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            JSONObject data = jsonobject.optJSONObject("data");
                            JSONObject object = data.optJSONObject("data");

                            FijiRentalUtils.updateCarModel(object, getContext());

//                            ArrayList ImageUrl = new ArrayList();
//                            CarModel carModel = new CarModel();
//                            carModel.setItemId(object.optString("item_id"));
//                            carModel.setItemName(object.optString("item_name"));
//                            carModel.setItemSku(object.optString("item_sku"));
//                            carModel.setItemPageId(object.optString("item_page_id"));
//                            carModel.setPartnerId(object.optString("partner_id"));
//                            carModel.setManufacturerId(object.optString("manufacturer_id"));
//                            carModel.setBodyTypeId(object.optString("body_type_id"));
//                            carModel.setTransmissionTypeId(object.optString("transmission_type_id"));
//                            carModel.setFuelTypeId(object.optString("fuel_type_id"));
//                            carModel.setModelName(object.optString("model_name"));
//                            carModel.setItemImage1(object.optString("item_image_1"));
//                            carModel.setItemImage2(object.optString("item_image_2"));
//                            carModel.setItemImage3(object.optString("item_image_3"));
//                            carModel.setMileage(object.optString("mileage"));
//                            carModel.setCityMileage(object.optString("city_mileage"));
//                            carModel.setItemColor(object.optString("item_color"));
//                            carModel.setFuelConsumption(object.optString("fuel_consumption"));
//                            carModel.setEngineCapacity(object.optString("engine_capacity"));
//                            carModel.setMaxPassengers(object.optString("max_passengers"));
//                            carModel.setMaxLuggage(object.optString("max_luggage"));
//                            carModel.setItemDoors(object.optString("item_doors"));
//                            carModel.setMinDriverAge(object.optString("min_driver_age"));
//                            carModel.setPriceGroupId(object.optString("price_group_id"));
//                            carModel.setFixedRentalDeposit(object.optString("fixed_rental_deposit"));
//                            carModel.setUnitsInStock(object.optString("units_in_stock"));
//                            carModel.setUnitsInStock(object.optString("max_units_per_booking"));
//                            carModel.setOptionsMeasurementUnit(object.optString("options_measurement_unit"));
//                            carModel.setBlogId(object.optString("blog_id"));
//                            carModel.setModelYear(object.optString("model_year"));
//                            carModel.setModelSeats(object.optString("model_seats"));
//                            carModel.setModelDoors(object.optString("model_doors"));
//                            carModel.setModelLatitude(object.optString("model_latitude"));
//                            carModel.setModelLongitude(object.optString("model_longitude"));
//                            carModel.setPrice(object.optString("price"));
//                            carModel.setModelDescription(object.optString("model_description"));
//                            carModel.setModelCarLocation(object.optString("model_car_location"));
//                            carModel.setModelCarMake(object.optString("model_car_make"));
//                            carModel.setModelCarModel(object.optString("model_car_model"));
//                            carModel.setModelCarIsNotSalvaged(object.optString("model_car_isNotSalvaged"));
//                            carModel.setModelCarCommercialHost(object.optString("model_car_commercialHost"));
//                            carModel.setModelCarGoalsEarning(object.optString("model_car_goals_earning"));
//                            carModel.setModelCarGoalsOwnerUtilization(object.optString("model_car_goals_owner_utilization"));
//                            carModel.setModelCarGoalsRenterUtilization(object.optString("model_car_goals_renter_utilization"));
//                            carModel.setModelCarAvailabilityAdvanceNotice(object.optString("model_car_availability_advanceNotice"));
//                            carModel.setModelCarDurationMinimumTripDuration(object.optString("model_car_duration_minimumTripDuration"));
//                            carModel.setModelCarDurationLongerWeekendTripPreferred(object.optString("model_car_duration_longerWeekendTripPreferred"));
//                            carModel.setModelCarDurationMaximumTripDuration(object.optString("model_car_duration_maximumTripDuration"));
//                            carModel.setModelCarLicensePlateNumber(object.optString("model_car_licensePlateNumber"));
//                            carModel.setModelCarLicensePlateRegion(object.optString("model_car_licensePlateRegion"));
//                            carModel.setBookInstatnly(object.optString("book_instatnly"));
//                            carModel.setBookInstatnlyCarLocation(object.optString("book_instatnly_car_location"));
//                            carModel.setBookInstatnlyDiliveryLocations(object.optString("book_instatnly_dilivery_locations"));
//                            carModel.setBookInstatnlyGuestsLocation(object.optString("book_instatnly_guests_location"));
//                            carModel.setModelUnlimitedIncluded(object.optString("model_unlimited_included"));
//                            carModel.setModelDailyDistanceIncluded(object.optString("model_daily_distance_included"));
//                            carModel.setAdvanceNoticeCarLocation(object.optString("advance_notice_car_location"));
//                            carModel.setAdvanceNoticeDiliveryLocations(object.optString("advance_notice_dilivery_locations"));
//                            carModel.setAdvanceNoticeGuestsLocation(object.optString("advance_notice_guests_location"));
//                            carModel.setTripBufferCarLocation(object.optString("trip_buffer_car_location"));
//                            carModel.setTripBufferDiliveryLocations(object.optString("trip_buffer_dilivery_locations"));
//                            carModel.setTripBufferGuestsLocation(object.optString("trip_buffer_guests_location"));
//                            carModel.setShortestPossibleTrip(object.optString("shortest_possible_trip"));
//                            carModel.setLongestPossibleTrip(object.optString("longest_possible_trip"));
//                            carModel.setLongTermTrips(object.optString("long_term_trips"));
//                            carModel.setEnabled(object.optString("enabled"));
//                            carModel.setSnoozeUntill(object.optString("snooze_untill"));
//                            carModel.setGuestChoosenLocation(object.optString("guest_choosen_location"));
//                            carModel.setGuestChoosenLocationFee(object.optString("guest_choosen_location_fee"));
//                            carModel.setGuestChoosenUpToMiles(object.optString("guest_choosen_up_to_miles"));
//                            carModel.setDeliveryDiscountOnDays(object.optString("delivery_discount_on_days"));
//                            carModel.set3extraDayDiscount(object.optString("3extra_day_discount"));
//                            carModel.set7extraDayDiscount(object.optString("7extra_day_discount"));
//                            carModel.set30extraDayDiscount(object.optString("30extra_day_discount"));
//                            carModel.setCarprice(object.optString("carprice"));
//                            carModel.setUnlistReason(object.optString("unlist_reason"));
//                            carModel.setPickupDate(object.optString("pickup_date"));
//                            carModel.setReturnDate(object.optString("return_date"));
//
//                            JSONObject ratingObject = object.optJSONObject("ratings");
//
//                            RatingModel ratingModel = new RatingModel();
//                            ratingModel.setRatingCount(ratingObject.optString("rating_count"));
//                            ratingModel.setAvg_rating(ratingObject.optInt("average_rating"));
//                            carModel.setRatingModel(ratingModel);
//
//                            JSONArray selectedAirportArray=object.optJSONArray("selected_pickup_list");
//                            ArrayList<AirportLocationModel> airportList=new ArrayList<>();
//                            for(int ai=0;ai<selectedAirportArray.length();ai++){
//                                JSONObject object1=selectedAirportArray.optJSONObject(ai);
//                                AirportLocationModel airportLocationModel=new AirportLocationModel();
//                                airportLocationModel.setLocationName(object1.optString("location_name"));
//                                airportLocationModel.setPrice(object1.optString("price"));
//                                airportList.add(airportLocationModel);
//                            }
//                            carModel.setAirportList(airportList);
//
//                            JSONArray futuresArray = object.optJSONArray("futures");
//
//                            List<FutureModel> futureList = new ArrayList<>();
//
//                            for (int i = 0; i < futuresArray.length(); i++) {
//                                JSONObject futureObject = futuresArray.optJSONObject(i);
//
//                                FutureModel futureModel = new FutureModel();
//                                futureModel.setFeatureId(futureObject.optString("feature_id"));
//                                futureModel.setFeatureTitle(futureObject.optString("feature_title"));
//
//                                futureList.add(futureModel);
//                            }
//
//                            carModel.setFutures(futureList);
//
//                            JSONObject partnerObject = object.optJSONObject("partner_detail");
//
//                            PartnerDetail partnerDetail = new PartnerDetail();
//                            partnerDetail.setUserPicUrl(partnerObject.optString("user_pic_url"));
//                            partnerDetail.setFirstName(partnerObject.optString("first_name"));
//                            partnerDetail.setLastName(partnerObject.optString("last_name"));
//                            partnerDetail.setTotalTrip(partnerObject.optInt("total_trip"));
//                            partnerDetail.setJoined(partnerObject.optString("joined"));
//                            partnerDetail.setID(partnerObject.optInt("ID"));
//
//                            carModel.setPartnerDetail(partnerDetail);
//
//                            carModel.setFuelTypeTitle(object.optString("fuel_type_title"));
//                            carModel.setTransmissionTypeTitle(object.optString("transmission_type_title"));
//                            carModel.setBodyTypeTitle(object.optString("body_type_title"));
//                            carModel.setManufacturerTitle(object.optString("manufacturer_title"));
//                            carModel.setCarNumber(object.optString("model_car_licensePlateNumber"));
//                            carModel.setGuidelines(object.optString("guidelines"));
//                            carModel.setAdditionalfeatures(object.optString("additionalfeatures"));
//                            carModel.setIs_favourites(object.optString("is_favourites"));
//                            ImageUrl = new ArrayList();
//                            JSONArray photos = object.optJSONArray("photos");
//
//
//                            for (int j = 0; j < photos.length(); j++) {
//                                ImageUrl.add(photos.optString(j));
//
//                            }
//                            carModel.setPhotos(ImageUrl);
//
//                            JSONObject licenceObject = object.optJSONObject("driver_details");
//
//                            DriverDetails driverDetails=new DriverDetails();
//                            driverDetails.setLicenseFirstname(licenceObject.optString("license_firstname"));
//                            driverDetails.setLicenseMiddlename(licenceObject.optString("license_middlename"));
//                            driverDetails.setLicenseLastname(licenceObject.optString("license_lastname"));
//                            driverDetails.setLicenseCountry(licenceObject.optString("license_country"));
//                            driverDetails.setLicenseState(licenceObject.optString("license_state"));
//                            driverDetails.setLicenseNumber(licenceObject.optString("license_number"));
//                            driverDetails.setLicenseExpirationdate(licenceObject.optString("license_expirationdate"));
//                            driverDetails.setLicenseBirthdate(licenceObject.optString("license_birthdate"));
//                            driverDetails.setDriverImage(licenceObject.optString("driver_image"));
//                            driverDetails.setLicenseImage(licenceObject.optString("license_image"));
//                            carModel.setDriverDetails(driverDetails);
//
//                            carModel.setGuestPickupReturnInstructions(object.optString("guest_pickup_return_instructions"));
//                            carModel.setGuestWelcomeMessage(object.optString("guest_welcome_message"));
//                            carModel.setGuestCarMessage(object.optString("guest_car_message"));
                            HostCarDetailFragment.this.carModel=Paper.book().read(FijiRentalUtils.carModel);
                            if(HostCarDetailFragment.this.carModel.getEnabled().equals("0")){
                                listingStatus.setText("Unlisted");
                                statusMag.setText("Your car won't appear in search results and can't be requested untill listed again.");
                            }else if(HostCarDetailFragment.this.carModel.getEnabled().equals("1")){
                                listingStatus.setText("Listed");
                                statusMag.setText("Your car appears in search results and can be requested.");
                            }else if(HostCarDetailFragment.this.carModel.getEnabled().equals("2")){
                                listingStatus.setText("Snoozed");
                                statusMag.setText("Your car won't appear in search results and can't be requested untill snooze ends.");
                            }



//                            Paper.book().write(FijiRentalUtils.carModel,carModel);
                            updateView(HostCarDetailFragment.this.carModel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getContext(), "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getContext(), "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

            }
        });
    }
}

