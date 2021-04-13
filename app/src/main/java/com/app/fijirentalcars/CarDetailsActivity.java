package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fijirentalcars.Adapter.ImagePager;
import com.app.fijirentalcars.Model.AirportLocationModel;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.DBModel;
import com.app.fijirentalcars.Model.DriverDetails;
import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.PartnerDetail;
import com.app.fijirentalcars.Model.RatingModel;
import com.app.fijirentalcars.Model.Unavailability;
import com.app.fijirentalcars.fragments.HostStart;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_dayExtra, tv_percntg, tv_totalSavePrice, tv_startDate, tv_endDate, tv_next, dateChange, modelCarLocation, modelName, modelYear, modelPrice, modelRating, carImages, unlimitedDistance, limitedDistance, carDoor, carSeat, carFuel, carDescription, hostName, hostJoine, carMpg;
    LinearLayout tripReview, carMpgLayout, ll_tripSaving;
    View v_tripsaving;
    ImageView iv_back, iv_fav, carFuelIcon;
    CircleImageView userProfile;
    ViewPager viewPager;
    TextView changeLocation;
    ArrayList ImageUrl = new ArrayList();
    ImagePager imagePager;
    APIService apiService;
    TextView report_listing, cancellation_policy;
    ProgressDialog progressDialog;
    LinearLayout insuranceView;
    CarModel carModel;
    private int DATE_PICKER = 502;
    LocalDate Startdate, EndDate;
    String startTime, endTime, _3dayDis = "", _7dayDis = "", _30dayDis = "";
    double price = 00.00;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        Window window = this.getWindow();
        Log.e("CarDetailsActivity", "CarDetailsActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        apiService = Config.getClient().create(APIService.class);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);


        // Log.e("TAG", "dateDiff " + FijiRentalUtils.getCountOfDays("09/04/2021", "12/04/2021"));
        init();
        getIntentData();

    }

    private void getIntentData() {

        if (getIntent().hasExtra(FijiRentalUtils.CarIntent)) {

            String carItemId = getIntent().getStringExtra(FijiRentalUtils.CarIntent);
            FijiRentalUtils.Logger("TAGS", "Car vale " + carItemId);

            getCarDetails(carItemId);
        }
    }

    private void getCarDetails(String carItemId) {

        HashMap<String, String> data = new HashMap<>();
        progressDialog.show();

        data.put("item_id", carItemId);
        Call<ResponseBody> call = apiService.getCarDetails(FijiRentalUtils.getAccessToken(this), data);

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
                            JSONObject data_array = data.optJSONObject("data");
                            passdata(data_array);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, CarDetailsActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, CarDetailsActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void passdata(JSONObject object) {
        carModel = new CarModel();
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
        carModel.setCityMileage(object.optString("city_mileage"));
        carModel.setItemColor(object.optString("item_color"));
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
        carModel.setModelCarGoalsEarning(object.optString("model_car_goals_earning"));
        carModel.setModelCarGoalsOwnerUtilization(object.optString("model_car_goals_owner_utilization"));
        carModel.setModelCarGoalsRenterUtilization(object.optString("model_car_goals_renter_utilization"));
        carModel.setModelCarAvailabilityAdvanceNotice(object.optString("model_car_availability_advanceNotice"));
        carModel.setModelCarDurationMinimumTripDuration(object.optString("model_car_duration_minimumTripDuration"));
        carModel.setModelCarDurationLongerWeekendTripPreferred(object.optString("model_car_duration_longerWeekendTripPreferred"));
        carModel.setModelCarDurationMaximumTripDuration(object.optString("model_car_duration_maximumTripDuration"));
        carModel.setModelCarLicensePlateNumber(object.optString("model_car_licensePlateNumber"));
        carModel.setModelCarLicensePlateRegion(object.optString("model_car_licensePlateRegion"));
//        carModel.setBookInstatnly(object.optString("book_instatnly"));
        carModel.setBookInstatnlyCarLocation(object.optString("book_instatnly_car_location"));
        carModel.setBookInstatnlyDiliveryLocations(object.optString("book_instatnly_dilivery_locations"));
        carModel.setBookInstatnlyGuestsLocation(object.optString("book_instatnly_guests_location"));
        carModel.setModelUnlimitedIncluded(object.optString("model_unlimited_included"));
        carModel.setModelDailyDistanceIncluded(object.optString("model_daily_distance_included"));
        carModel.setAdvanceNoticeCarLocation(object.optString("advance_notice_car_location"));
        carModel.setAdvanceNoticeDiliveryLocations(object.optString("advance_notice_dilivery_locations"));
        carModel.setAdvanceNoticeGuestsLocation(object.optString("advance_notice_guests_location"));
        carModel.setTripBufferCarLocation(object.optString("trip_buffer_car_location"));
        carModel.setTripBufferDiliveryLocations(object.optString("trip_buffer_dilivery_locations"));
        carModel.setTripBufferGuestsLocation(object.optString("trip_buffer_guests_location"));
        carModel.setShortestPossibleTrip(object.optString("shortest_possible_trip"));
        carModel.setLongestPossibleTrip(object.optString("longest_possible_trip"));
        carModel.setLongTermTrips(object.optString("long_term_trips"));
        carModel.setEnabled(object.optString("enabled"));
        carModel.setSnoozeUntill(object.optString("snooze_untill"));
        carModel.setGuestChoosenLocation(object.optString("guest_choosen_location"));
        carModel.setGuestChoosenLocationFee(object.optString("guest_choosen_location_fee"));
        carModel.setGuestChoosenUpToMiles(object.optString("guest_choosen_up_to_miles"));
        carModel.setDeliveryDiscountOnDays(object.optString("delivery_discount_on_days"));
        carModel.set3extraDayDiscount(object.optString("3extra_day_discount"));
        carModel.set7extraDayDiscount(object.optString("7extra_day_discount"));
        carModel.set30extraDayDiscount(object.optString("30extra_day_discount"));
        carModel.setCarprice(object.optString("carprice"));
        carModel.setUnlistReason(object.optString("unlist_reason"));
        carModel.setPickupDate(object.optString("pickup_date"));
        carModel.setReturnDate(object.optString("return_date"));

        JSONObject ratingObject = object.optJSONObject("ratings");

        RatingModel ratingModel = new RatingModel();
        ratingModel.setRatingCount(ratingObject.optString("rating_count"));
        ratingModel.setAvg_rating(ratingObject.optInt("average_rating"));
        carModel.setRatingModel(ratingModel);

        JSONArray selectedAirportArray = object.optJSONArray("selected_pickup_list");
        ArrayList<AirportLocationModel> airportList = new ArrayList<>();
        for (int ai = 0; ai < selectedAirportArray.length(); ai++) {
            JSONObject object1 = selectedAirportArray.optJSONObject(ai);
            AirportLocationModel airportLocationModel = new AirportLocationModel();
            airportLocationModel.setLocationName(object1.optString("location_name"));
            airportLocationModel.setPrice(object1.optString("price"));
            airportList.add(airportLocationModel);
        }
        carModel.setAirportList(airportList);

        JSONArray futuresArray = object.optJSONArray("futures");

        List<FutureModel> futureList = new ArrayList<>();

        for (int i = 0; i < futuresArray.length(); i++) {
            JSONObject futureObject = futuresArray.optJSONObject(i);

            FutureModel futureModel = new FutureModel();
            futureModel.setFeatureId(futureObject.optString("feature_id"));
            futureModel.setFeatureTitle(futureObject.optString("feature_title"));

            futureList.add(futureModel);
        }

        carModel.setFutures(futureList);

        JSONObject partnerObject = object.optJSONObject("partner_detail");

        PartnerDetail partnerDetail = new PartnerDetail();
        partnerDetail.setUserPicUrl(partnerObject.optString("user_pic_url"));
        partnerDetail.setFirstName(partnerObject.optString("first_name"));
        partnerDetail.setLastName(partnerObject.optString("last_name"));
        partnerDetail.setTotalTrip(partnerObject.optInt("total_trip"));
        partnerDetail.setJoined(partnerObject.optString("joined"));
        partnerDetail.setID(partnerObject.optInt("ID"));

        carModel.setPartnerDetail(partnerDetail);

        JSONArray dbDateList = object.optJSONArray("price_by_days");
        List<DBModel> dbModelList = new ArrayList<>();
        for (int ai = 0; ai < dbDateList.length(); ai++) {
            JSONObject object1 = dbDateList.optJSONObject(ai);
            DBModel dbModel = new DBModel();
            dbModel.setTitle(object1.optString("price"));
            dbModel.setDate(object1.optString("date"));
            dbModelList.add(dbModel);

        }
        carModel.setPriceByDays(dbModelList);

        List<Unavailability> unavailabilities = new ArrayList<>();
        JSONArray unavailArray = object.optJSONArray("unavailability");

        for (int ua = 0; ua < unavailArray.length(); ua++) {
            JSONObject unavailObject = unavailArray.optJSONObject(ua);
            Unavailability unavailability = new Unavailability();
            unavailability.setId(unavailObject.optString("id"));
            unavailability.setCarItemId(unavailObject.optString("car_item_id"));
            unavailability.setToDateTime(unavailObject.optString("to_date_time"));
            unavailability.setFromDateTime(unavailObject.optString("from_date_time"));
            unavailability.setIsrepeat(unavailObject.optString("isrepeat"));
            unavailability.setEverySunday(unavailObject.optString("every_sunday"));
            unavailability.setEveryMondy(unavailObject.optString("every_mondy"));
            unavailability.setEveryTuesday(unavailObject.optString("every_tuesday"));
            unavailability.setEveryWednesday(unavailObject.optString("every_wednesday"));
            unavailability.setEveryThursday(unavailObject.optString("every_thursday"));
            unavailability.setEveryFriday(unavailObject.optString("every_friday"));
            unavailability.setEverySaturday(unavailObject.optString("every_saturday"));
            unavailability.setUntilDateTime(unavailObject.optString("until_date_time"));
            unavailability.setCreatedAt(unavailObject.optString("created_at"));
            unavailabilities.add(unavailability);
        }

        carModel.setUnavailability(unavailabilities);
        carModel.setFuelTypeTitle(object.optString("fuel_type_title"));
        carModel.setTransmissionTypeTitle(object.optString("transmission_type_title"));
        carModel.setBodyTypeTitle(object.optString("body_type_title"));
        carModel.setManufacturerTitle(object.optString("manufacturer_title"));
        carModel.setCarNumber(object.optString("model_car_licensePlateNumber"));
        carModel.setGuidelines(object.optString("guidelines"));
        carModel.setAdditionalfeatures(object.optString("additionalfeatures"));
        carModel.setIs_favourites(object.optString("is_favourites"));
        ImageUrl.clear();
        JSONArray photos = object.optJSONArray("photos");


        for (int j = 0; j < photos.length(); j++) {
            ImageUrl.add(photos.optString(j));
        }
        carModel.setPhotos(ImageUrl);
        JSONObject licenceObject = object.optJSONObject("driver_details");
        DriverDetails driverDetails = new DriverDetails();
        driverDetails.setLicenseFirstname(licenceObject.optString("license_firstname"));
        driverDetails.setLicenseMiddlename(licenceObject.optString("license_middlename"));
        driverDetails.setLicenseLastname(licenceObject.optString("license_lastname"));
        driverDetails.setLicenseCountry(licenceObject.optString("license_country"));
        driverDetails.setLicenseState(licenceObject.optString("license_state"));
        driverDetails.setLicenseNumber(licenceObject.optString("license_number"));
        driverDetails.setLicenseExpirationdate(licenceObject.optString("license_expirationdate"));
        driverDetails.setLicenseBirthdate(licenceObject.optString("license_birthdate"));
        driverDetails.setDriverImage(licenceObject.optString("driver_image"));
        driverDetails.setLicenseImage(licenceObject.optString("license_image"));
        carModel.setDriverDetails(driverDetails);

        carModel.setGuestPickupReturnInstructions(object.optString("guest_pickup_return_instructions"));
        carModel.setGuestWelcomeMessage(object.optString("guest_welcome_message"));
        carModel.setGuestCarMessage(object.optString("guest_car_message"));

        Paper.book().write(FijiRentalUtils.carModel, carModel);

        updateView(carModel);

       /* String sdate = FijiRentalUtils.parseDateTodayddMM1(carModel.getPickupDate(), "yyyy-MM-dd HH:mm:ss", "EEE, dd MMM");
        String edate = FijiRentalUtils.parseDateTodayddMM1(carModel.getReturnDate(), "yyyy-MM-dd HH:mm:ss", "EEE, dd MMM");
        Log.e("TAG", "sdate--edate" + sdate + "--" + edate);

        price = Double.parseDouble(carModel.getPrice());
        Log.e("TAG", "price " + price);

        tripSavingCalculation(sdate, edate);*/
    }

    @Override
    protected void onDestroy() {
        Paper.book().delete(FijiRentalUtils.carModel);
        super.onDestroy();
    }

    private void updateView(CarModel carModel) {

        modelName.setText(carModel.getItemName());
        modelYear.setText(carModel.getModelYear());
        modelCarLocation.setText(carModel.getModelCarLocation());

        tv_startDate.setText(FijiRentalUtils.parseDateTodayddMM1(carModel.getPickupDate(), "yyyy-MM-dd HH:mm:ss", "EEE, dd MMM - HH:mm aa"));
        tv_endDate.setText(FijiRentalUtils.parseDateTodayddMM1(carModel.getReturnDate(), "yyyy-MM-dd HH:mm:ss", "EEE, dd MMM - HH:mm aa"));

        modelRating.setText(String.valueOf(carModel.getRatingModel().getAvg_rating()));
        if (!TextUtils.isEmpty(carModel.getPrice()) && !carModel.getPrice().equalsIgnoreCase("null")) {
            modelPrice.setText(String.valueOf((int) Double.parseDouble(carModel.getPrice())));
            price = Double.parseDouble(carModel.getPrice());
            Log.e("TAG", "price " + price);
        }

        _3dayDis = carModel.get3extraDayDiscount();
        _7dayDis = carModel.get7extraDayDiscount();
        _30dayDis = carModel.get30extraDayDiscount();
        //tripSavingCalculation("Fri, 09 Apr", "Mon, 12 Apr", price, carModel.get3extraDayDiscount());
        String sdate = FijiRentalUtils.parseDateTodayddMM1(carModel.getPickupDate(), "yyyy-MM-dd HH:mm:ss", "EEE, dd MMM");
        String edate = FijiRentalUtils.parseDateTodayddMM1(carModel.getReturnDate(), "yyyy-MM-dd HH:mm:ss", "EEE, dd MMM");
        Log.e("TAG", "sdate--edate" + sdate + "--" + edate);

//        price = Double.parseDouble(carModel.getPrice());
//        Log.e("TAG", "price " + price);

        tripSavingCalculation(sdate, edate);

        imagePager.notifyDataSetChanged();

        if (carModel.getIs_favourites().equals("0")) {
            iv_fav.setImageResource(R.drawable.ic_action_favrorite_unselected);
        } else {
            iv_fav.setImageResource(R.drawable.ic_action_favrorite_selected);
        }

        int carCityMpg = 0, hwyMpg = 0;

        if (!TextUtils.isEmpty(carModel.getCityMileage())) {
            carCityMpg = (int) Double.parseDouble(carModel.getCityMileage());
        }
        if (!TextUtils.isEmpty(carModel.getMileage())) {
            hwyMpg = Integer.parseInt(carModel.getMileage());
        }


        if (carCityMpg == 0 && hwyMpg == 0) {
            carMpgLayout.setVisibility(View.GONE);
        } else {
            carMpg.setText(Math.round((carCityMpg + hwyMpg) / 2) + " MPG");
            carMpgLayout.setVisibility(View.VISIBLE);
        }

        if (ImageUrl.size() > 0) {

            carImages.setText((1) + " to " + ImageUrl.size());
        } else {
            carImages.setText(String.valueOf(ImageUrl.size()));
        }

        if (carModel.getModelUnlimitedIncluded().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(carModel.getModelDailyDistanceIncluded())) {
                limitedDistance.setVisibility(View.GONE);
                unlimitedDistance.setVisibility(View.GONE);
            } else {

                unlimitedDistance.setText(carModel.getModelDailyDistanceIncluded() + " MI");
                limitedDistance.setText("$ 0.11 charge for each additional mile");

                limitedDistance.setVisibility(View.VISIBLE);
                unlimitedDistance.setVisibility(View.VISIBLE);
            }

        } else {
            unlimitedDistance.setText("Unlimited");
            limitedDistance.setVisibility(View.GONE);
            unlimitedDistance.setVisibility(View.VISIBLE);
        }

        carSeat.setText(carModel.getModelSeats() + " seats");
        carDoor.setText(carModel.getModelDoors() + " doors");
        if (carModel.getFuelTypeTitle().equalsIgnoreCase("hybrid")) {
            carFuelIcon.setImageResource(R.drawable.ic_action_green_tag);
        } else if (carModel.getFuelTypeTitle().equalsIgnoreCase("electric")) {
            carFuelIcon.setImageResource(R.drawable.ic_action_green_tag);
        } else {
            carFuelIcon.setImageResource(R.drawable.ic_action_diesel_petrol);
        }
        carFuel.setText(carModel.getFuelTypeTitle());

        if (TextUtils.isEmpty(carModel.getModelDescription())) {
            findViewById(R.id.desc_heading).setVisibility(View.GONE);
            carDescription.setVisibility(View.GONE);
        } else {
            carDescription.setText(carModel.getModelDescription());
            findViewById(R.id.desc_heading).setVisibility(View.VISIBLE);
            carDescription.setVisibility(View.VISIBLE);
        }

        Glide.with(this).load(carModel.getPartnerDetail().getUserPicUrl()).placeholder(R.drawable.ic_action_person).into(userProfile);

        hostName.setText(carModel.getPartnerDetail().getFirstName() + " " + carModel.getPartnerDetail().getLastName());
        hostJoine.setText(carModel.getPartnerDetail().getJoined());

    }

    private void tripSavingCalculation(String sdate, String edate) { // double price, String extraDayDiscount

        int daycount = FijiRentalUtils.getCountOfDays(sdate, edate, "EEE, dd MMM");

        Log.e("TAG", "daycount" + daycount);
        try {
            if (daycount < 3) {
                ll_tripSaving.setVisibility(View.GONE);
                v_tripsaving.setVisibility(View.GONE);
            } else {
                ll_tripSaving.setVisibility(View.VISIBLE);
                v_tripsaving.setVisibility(View.VISIBLE);
                double totalprice, discount, percantage;
                int savePrice;
                try {
                    if (daycount >= 3 && daycount < 7) {
                        totalprice = price * daycount;
                        discount = Double.parseDouble(_3dayDis);
                        percantage = (totalprice / 100.0f) * discount;
                        savePrice = (int) ((totalprice * percantage) / 100);
                        tv_dayExtra.setText("3+ day discount savings");
                        tv_percntg.setText("You save " + (int) percantage + " %");
                        tv_totalSavePrice.setText("$" + savePrice);

                        Log.e("TAG", "3day val " + totalprice + "--" + percantage);
                    } else if (daycount >= 7 && daycount < 30) {
                        totalprice = price * daycount;
                        discount = Double.parseDouble(_7dayDis);
                        percantage = (totalprice / 100.0f) * discount;
                        savePrice = (int) ((totalprice * percantage) / 100);
                        tv_dayExtra.setText("7+ day discount savings");
                        tv_percntg.setText("You save " + (int) percantage + " %");
                        tv_totalSavePrice.setText("$" + savePrice);

                        Log.e("TAG", "7day val " + totalprice + "--" + percantage);
                    } else {
                        totalprice = price * daycount;
                        discount = Double.parseDouble(_30dayDis);
                        percantage = (totalprice / 100.0f) * discount;
                        savePrice = (int) ((totalprice * percantage) / 100);
                        tv_dayExtra.setText("30+ day discount savings");
                        tv_percntg.setText("You save " + (int) percantage + " %");
                        tv_totalSavePrice.setText("$" + savePrice);
                        Log.e("TAG", "30day val " + totalprice + "--" + percantage);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DATE_PICKER) {
            if (resultCode == RESULT_OK) {
                Startdate = LocalDate.parse(data.getStringExtra("start_date"));
                EndDate = LocalDate.parse(data.getStringExtra("end_date"));
                startTime = data.getStringExtra("start_date_time");
                endTime = data.getStringExtra("end_date_time");

                Log.e("TAG", "startdate--enddate--starttime--endtime-- " + Startdate + "--" + EndDate + "--" + startTime + "--" + endTime);
                tv_startDate.setText(FijiRentalUtils.parseDateTodayddMM1(Startdate.toString(), "yyyy-MM-dd", "EEE, dd MMM") + " - " + startTime);
                tv_endDate.setText(FijiRentalUtils.parseDateTodayddMM1(EndDate.toString(), "yyyy-MM-dd", "EEE, dd MMM") + " - " + endTime);

                String sdate = FijiRentalUtils.parseDateTodayddMM1(Startdate.toString(), "yyyy-MM-dd", "EEE, dd MMM");
                String edate = FijiRentalUtils.parseDateTodayddMM1(EndDate.toString(), "yyyy-MM-dd", "EEE, dd MMM");

                tripSavingCalculation(sdate, edate);

            }
        }
    }

    public void init() {

        tv_next = findViewById(R.id.tv_next);
        iv_back = findViewById(R.id.iv_back);
        iv_fav = findViewById(R.id.iv_fav);
        viewPager = findViewById(R.id.vp_slider);
        changeLocation = findViewById(R.id.change);
        modelName = findViewById(R.id.model_name);
        modelPrice = findViewById(R.id.model_price);
        modelRating = findViewById(R.id.model_rating);
        modelYear = findViewById(R.id.model_year);
        carImages = findViewById(R.id.pager_count);
        tripReview = findViewById(R.id.reviews_link_label);
        carMpgLayout = findViewById(R.id.mpg_layout);
        report_listing = findViewById(R.id.report_listing);
        cancellation_policy = findViewById(R.id.cancel_policy);
        insuranceView = findViewById(R.id.insuranve_view);
        unlimitedDistance = findViewById(R.id.unlimited_distance);
        limitedDistance = findViewById(R.id.limited_distance);
        carSeat = findViewById(R.id.car_seat_num);
        carDoor = findViewById(R.id.car_door_num);
        carFuel = findViewById(R.id.car_fuel_num);
        carDescription = findViewById(R.id.car_description);
        hostName = findViewById(R.id.hostName);
        hostJoine = findViewById(R.id.hostJoin);
        userProfile = findViewById(R.id.userProfile);
        carFuelIcon = findViewById(R.id.fuel_icon);
        carMpg = findViewById(R.id.car_mpg);
        dateChange = findViewById(R.id.tv_changeDate);
        modelCarLocation = findViewById(R.id.tv_deliverylocation2);
        tv_startDate = findViewById(R.id.tv_startDate);
        tv_endDate = findViewById(R.id.tv_endDate);
        ll_tripSaving = findViewById(R.id.ll_tripSaving);
        v_tripsaving = findViewById(R.id.v_tripsaving);
        tv_dayExtra = findViewById(R.id.tv_dayExtra);
        tv_percntg = findViewById(R.id.tv_percntg);
        tv_totalSavePrice = findViewById(R.id.tv_totalSavePrice);

        tripReview.setOnClickListener(this);
        report_listing.setOnClickListener(this);
        cancellation_policy.setOnClickListener(this);
        insuranceView.setOnClickListener(this);
        dateChange.setOnClickListener(this);


        imagePager = new ImagePager(this, ImageUrl);
        viewPager.setAdapter(imagePager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                carImages.setText((position + 1) + " to " + ImageUrl.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        changeLocation.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_next:
                if (TextUtils.isEmpty(FijiRentalUtils.getAccessToken(this))) {
                    Intent i = new Intent(this, LetsStartActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, PaymentActivity.class);
                    startActivity(i);

                }

                break;
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_changeDate:
                String sdate = FijiRentalUtils.parseDateToddMMyyyy(carModel.getPickupDate());
                String edate = FijiRentalUtils.parseDateToddMMyyyy(carModel.getReturnDate());
                Intent dateSelection = new Intent(CarDetailsActivity.this, RouteDatePicker.class);
                dateSelection.putExtra("STARTDATE", sdate);
                dateSelection.putExtra("ENDDATE", edate);
                startActivityForResult(dateSelection, DATE_PICKER);
                break;

            case R.id.cancel_policy:
                String url = "https://fijirentalcars.siddhidevelopment.com/help/";


                CustomTabsIntent tabsIntent1 = new CustomTabsIntent.Builder().build();
                tabsIntent1.intent.setPackage("com.android.chrome");
                tabsIntent1.launchUrl(this, Uri.parse(url));
                break;
            case R.id.report_listing:
                Intent reportIntent = new Intent(this, ReportCarListing.class);
                startActivity(reportIntent);
                break;
            case R.id.change:
                Intent changeintent = new Intent(this, MapDescriptionActivity.class);
                startActivity(changeintent);
                break;
            case R.id.reviews_link_label:
                if (carModel != null) {
                    Intent tripintent = new Intent(this, TripReviewScreen.class);
                    tripintent.putExtra(FijiRentalUtils.CarIntent, carModel.getItemPageId());
                    tripintent.putExtra(FijiRentalUtils.CarReviewIntent, carModel.getRatingModel().getAvg_rating());
                    startActivity(tripintent);
                }
                break;

            case R.id.insuranve_view:
                BottomSheetDialog dialog = new BottomSheetDialog(this);
                View dialogView = LayoutInflater.from(this).inflate(R.layout.insurance_dialog, null);
                dialog.setContentView(dialogView);
                dialog.show();
                break;

        }

    }


}