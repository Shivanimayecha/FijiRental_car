package com.app.fijirentalcars.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import com.app.fijirentalcars.Model.AirportLocationModel;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.DBModel;
import com.app.fijirentalcars.Model.DriverDetails;
import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.Manufacturer;
import com.app.fijirentalcars.Model.PartnerDetail;
import com.app.fijirentalcars.Model.RatingModel;
import com.app.fijirentalcars.Model.SpinnerModel;
import com.app.fijirentalcars.Model.Unavailability;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.SQLDB.HRDatabase;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class FijiRentalUtils {

    public static final int MIN_CAR_YEAR = 1981;
    public static final String CAR_FLAW = "CAR_LISTING";
    public static final String EXTRA_ITEAM = "Extra_item";
    public static final String EXTRA_MODEL = "Extra_model";
    public static final int UPDATE_LISTING_STATUS = 2025;
    public static final int UPDATE_LOCATION = 2026;
    private static final String TAG = "FijiRentalUtils";
    private static final boolean isDebug = true;
    public static String errorMessage = "Something went wrong.";
    public static final String PREFS_NAME = "FijiRental";
    public static final String skip = "skip";
    public static final String accessToken = "accessToken";
    public static final String roles = "roles";
    public static final String about_desc = "about_desc";
    public static final String work = "work";
    public static final String school = "school";
    public static final String language = "languages";
    public static final String ID = "ID";
    public static final String user_login = "user_login";
    public static final String user_nicename = "user_nicename";
    public static final String user_email = "user_email";
    public static final String user_pic_url = "user_pic_url";
    public static final String user_activation_key = "user_activation_key";
    public static final String user_status = "user_status";
    public static final String display_name = "display_name";
    public static final String user_pass = "user_password";

    public static boolean isApply = false;
    public static String sortType = "";
    public static String minSort = "";
    public static String maxSort = "";
    public static int isBookInstant = 0, isDeliverDirect = 0, isTopRated = 0, distanceIncludePos = 0, carYearPos = 0, vehicleTypePos = 0, makeTypePos = 0, modelTypePos = 0, seatPos = 0, transmissionPos = 0;
    public static String CarIntent = "CAR_DETAILS";
    public static String CarReviewIntent = "CAR_REVIEW";
    public static String BaseImageUrl = "https://fijirentalcars.siddhidevelopment.com/wp-content/uploads/CarRentalGallery/";
    public static String CarDetailIntent = "HOST_CAR_DETAILS";
    public static boolean isProfileUpdate = false;
    public static String mail_notification = "MAIL_NOTIFICATION";
    public static String is_car_listed = "CAR_LISTED_CO";
    public static String push_notification = "PUSH_NOTIFICATION";
    public static String largeImagefile = "Select file under 50 kb";
    public static String performanceUtil = "1 Apr 2020 - 31 Mar 2021 (Current)";
    public static String EarningFilter = "2021";
    public static String ReviewDay = "Last 365 days";
    public static String featureCarList = "";
    public static boolean isUpdateCarModel = false;
    public static boolean isUpdateAirportList = false;
    public static SpinnerModel[] modelList;
    public static boolean isDateUpdate = false;
    public static String carModel = "CarModelDetails";
    public static String Consumer = "subscriber";
    public static String Owner = "fleet_management_partner";
    public static String User_Selection="USER_SELECTION";


    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9.-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();


    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            imm.hideSoftInputFromWindow(
//                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showProgressDialog(Context context) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            if (!progressDialog.isShowing()) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Logger(String tags, String s) {
        Log.e(tags, s);
    }

    public static boolean isEmptyTextView(TextView tv_location) {

        if (TextUtils.isEmpty(tv_location.getText().toString().trim())) {
            return true;
        }

        return false;
    }

    public static String getMapValue(String seatbeltType) {
        if (seatbeltType != null) {
            return seatbeltType;
        }
        return " ";
    }

    public static String getManFacID(ArrayList<Manufacturer> manList, String make) {

        for (int i = 0; i < manList.size(); i++) {
            Manufacturer manufacturer = manList.get(i);

            if (manufacturer.getManufacturerTitle().equalsIgnoreCase(make)) {
                make = manufacturer.getManufacturerId();
            }
        }

        Logger("TAGS", "man list " + manList + " make " + make);

        return make;

    }

    public static Typeface getRegularTypeFace(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");

        return typeface;
    }

    public static Typeface getSemiBoldTypeFace(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-SemiBold.ttf");
        return typeface;
    }

    public static void updateCarModel(JSONObject object, Context context) {
        HRDatabase hrDatabase = new HRDatabase(context);
        hrDatabase.deleteAllNotes();
        ArrayList ImageUrl = new ArrayList();
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
        carModel.setVehicle_protection(object.optString("vehicle_protection"));
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
        ImageUrl = new ArrayList();
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
        hrDatabase.upDateDatabase(carModel.getPriceByDays());

        Paper.book().write(FijiRentalUtils.carModel, carModel);
    }

    public static String chagneDateFormate(LocalDate startdate, String startTime) {

        String time = "";
        String date = startdate.toString() + " " + startTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        try {
            Date d = sdf.parse(date);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            time = sdf.format(d);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        return time;

    }

    public static ArrayList<Object> getDriverAgeList() {
        ArrayList ageList = new ArrayList();
        for (int i = 18; i <= 29; i++) {
            ageList.add(i);
        }
        return ageList;
    }

    public static ArrayList<Object> getLugguageList() {
        ArrayList ageList = new ArrayList();
        for (int i = 0; i <= 100; i++) {
            ageList.add(i);
        }
        return ageList;
    }
    public static void setCustomTypeFace(View textView,Context context) {
        if(textView instanceof TextInputEditText){
            ((EditText)textView).setTypeface(FijiRentalUtils.getSemiBoldTypeFace(context));
        } if(textView instanceof AutoCompleteTextView){
            ((AutoCompleteTextView)textView).setTypeface(FijiRentalUtils.getSemiBoldTypeFace(context));
        }

        ((TextInputLayout) textView.getParent().getParent()).setTypeface(FijiRentalUtils.getSemiBoldTypeFace(context));
    }


    protected void hideProgressDialog(Context context) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void v(String value) {
        // for log detail set isDebug true
        if (isDebug) Log.v(TAG, value);
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidLength(String pass) {
        if (!pass.isEmpty() && pass.length() > 5) {
            return true;
        }

        return false;
    }

    public static boolean checkInternetConnection(Activity activity) {
        if (activity != null) {
            if (!isNetworkAvailable(activity)) {
                String errorMessage = "No internet connection!";
                ShowValidation(errorMessage, activity, "0");
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static void ShowValidation(String s, Context context, final String pos) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        View rv = inflater.inflate(R.layout.validation_dialog, null);
        dialogBuilder.setView(rv);
        final Dialog b = dialogBuilder.create();
        TextView tvMessage = rv.findViewById(R.id.tvMessage);
        TextView btnOK = rv.findViewById(R.id.btnOK);
        tvMessage.setText(s);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        dialogBuilder.setTitle("");
        dialogBuilder.setMessage("");
        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        b.show();
    }


    public static String Appversion(Context context) {
        String appVersion = null;
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    public static String Device_id(Context context) {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static String FirebaseToken() {
        String Token = null;
        Token = FirebaseInstanceId.getInstance().getToken();
        return Token;
    }


    public static void savePreferences(String item, String value, Context context) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        if (item.equalsIgnoreCase(accessToken)) {
            editor.putString(accessToken, value);
        } else if (item.equalsIgnoreCase(roles)) {
            editor.putString(roles, value);
        } else if (item.equalsIgnoreCase(ID)) {
            editor.putString(ID, value);
        } else if (item.equalsIgnoreCase(user_login)) {
            editor.putString(user_login, value);
        } else if (item.equalsIgnoreCase(user_nicename)) {
            editor.putString(user_nicename, value);
        } else if (item.equalsIgnoreCase(user_email)) {
            editor.putString(user_email, value);
        } else if (item.equalsIgnoreCase(user_pic_url)) {
            editor.putString(user_pic_url, value);
        } else if (item.equalsIgnoreCase(user_activation_key)) {
            editor.putString(user_activation_key, value);
        } else if (item.equalsIgnoreCase(user_status)) {
            editor.putString(user_status, value);
        } else if (item.equalsIgnoreCase(display_name)) {
            editor.putString(display_name, value);
        } else if (item.equalsIgnoreCase(about_desc)) {
            editor.putString(about_desc, value);
        } else if (item.equalsIgnoreCase(work)) {
            editor.putString(work, value);
        } else if (item.equalsIgnoreCase(school)) {
            editor.putString(school, value);
        } else if (item.equalsIgnoreCase(language)) {
            editor.putString(language, value);
        } else if (item.equalsIgnoreCase(user_pass)) {
            editor.putString(user_pass, value);
        } else if (item.equalsIgnoreCase(mail_notification)) {
            editor.putString(mail_notification, value);
        } else if (item.equalsIgnoreCase(push_notification)) {
            editor.putString(push_notification, value);
        } else if (item.equalsIgnoreCase(is_car_listed)) {
            editor.putString(is_car_listed, value);
        }
        editor.apply();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(accessToken, null);
    }

    public static String getCarListedVal(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(is_car_listed, "0");
    }

    public static String getRoles(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(roles, null);
    }

    public static String getPrefVal(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(key, null);
    }


    public static String getId(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(ID, null);
    }

    public static String getOldPass(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(user_pass, null);
    }


    public static String getUser_login(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(user_login, null);
    }

    public static String getUser_nicename(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(user_nicename, null);
    }

    public static String getUser_email(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(user_email, null);
    }

    public static String getUser_activation_key(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(user_activation_key, null);
    }

    public static String getDisplay_name(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(display_name, null);
    }

    public static String getAbout_desc(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(about_desc, "");
    }

    public static String getWork(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(work, "");
    }

    public static String getSchool(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(school, "");
    }

    public static String getLanguage(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(language, "");
    }

    public static String getUser_status(Context context) {
        SharedPreferences preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preference.getString(user_status, null);
    }

    public static InputFilter ignoreFirstWhiteSpace() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        if (dstart == 0)
                            return "";
                    }
                }
                return null;
            }
        };
    }

    public static String compressImage(String imageUri, File outputFile) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = outputFile.getAbsolutePath();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static void getSpinnerValue() {

        modelList = new SpinnerModel[11];

        modelList[0] = new SpinnerModel();
        modelList[0].setName("Up to 1 mile");
        modelList[0].setVlaue("1");

        modelList[1] = new SpinnerModel();
        modelList[1].setName("Up to 2 miles");
        modelList[1].setVlaue("2");

        modelList[2] = new SpinnerModel();
        modelList[2].setName("Up to 3 miles");
        modelList[2].setVlaue("3");

        modelList[3] = new SpinnerModel();
        modelList[3].setName("Up to 4 miles");
        modelList[3].setVlaue("4");

        modelList[4] = new SpinnerModel();
        modelList[4].setName("Up to 5 miles");
        modelList[4].setVlaue("5");

        modelList[5] = new SpinnerModel();
        modelList[5].setName("Up to 10 miles");
        modelList[5].setVlaue("10");

        modelList[6] = new SpinnerModel();
        modelList[6].setName("Up to 15 miles");
        modelList[6].setVlaue("15");

        modelList[7] = new SpinnerModel();
        modelList[7].setName("Up to 20 miles");
        modelList[7].setVlaue("20");

        modelList[8] = new SpinnerModel();
        modelList[8].setName("Up to 25 miles");
        modelList[8].setVlaue("25");

        modelList[9] = new SpinnerModel();
        modelList[9].setName("Up to 50 miles");
        modelList[9].setVlaue("50");

        modelList[10] = new SpinnerModel();
        modelList[10].setName("Any distance");
        modelList[10].setVlaue("");
    }

    public static String getDistanceNameFromVal(String val) {
        String distanceName = "";

        if (val.equalsIgnoreCase("0")) {
            distanceName = "Any distance";
        } else {
            for (int i = 0; i < FijiRentalUtils.modelList.length; i++) {
                if (FijiRentalUtils.modelList[i].getVlaue().equalsIgnoreCase(val)) {
                    distanceName = FijiRentalUtils.modelList[i].getName();
                }
            }
        }
        return distanceName;
    }


    public static String parseDate(String val){

            String datetime=null;
            DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat d= new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date convertedDate = inputFormat.parse(val);
                datetime = d.format(convertedDate);

            }catch (ParseException e)
            {

            }
            return  datetime;


        }

}
