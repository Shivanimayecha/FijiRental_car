package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fijirentalcars.CustomViews.CustomButtonDialog;
import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.BodyType;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.FuelType;
import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.Model.PartnerDetail;
import com.app.fijirentalcars.Model.RatingModel;
import com.app.fijirentalcars.Model.TransmissionType;
import com.app.fijirentalcars.Model.VinModel;
import com.app.fijirentalcars.SQLDB.DBHandler_BodyType;
import com.app.fijirentalcars.SQLDB.DBHandler_Fueltypes;
import com.app.fijirentalcars.SQLDB.DBHandler_Transmission_type;
import com.app.fijirentalcars.listners.DialogButtonListner;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.fijirentalcars.util.FijiRentalUtils.setCustomTypeFace;

public class FillDetailsCarActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner, DialogButtonListner {

    TextView tv_next, tv_location, tv_carDetails, tv_entercar;
    ImageView iv_back;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
    List transmissionLsit = new ArrayList();
    List fuelTypeList = new ArrayList();
    List vehicleTypeList = new ArrayList();
    List odoList = new ArrayList();
    List carPriceList = new ArrayList();
    List luggageList = new ArrayList();
    LinearLayout subCarDetails;
    CustomDialog customDialog;
    EditText carName, carBondValue;
    private DBHandler_Transmission_type dbHandler_transmission_type;
    private DBHandler_BodyType dbHandler_bodyType;
    BodyType bodyType;
    AppCompatCheckBox checkBox1, checkBox2;
    TransmissionType transmissionType;
    FuelType fuelType;
    PlacesClient placesClient;
    private CustomButtonDialog btnDialog;
    AutoCompleteTextView car_seat, car_door, car_luggage, maxPassenger, tv_carTrans, tv_carOdo, tv_carType, tv_carPrice, tv_carfuel, driverMinAge;

    List doorCountList = Arrays.asList("None", "2", "3", "4", "5");
    List seatCountList = Arrays.asList("None", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    APIService apiService;
    ProgressDialog progressDialog;
    private List<String> ImageUrl = new ArrayList<>();
    private DBHandler_Fueltypes dbHandler_fueltypes;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_details_car);
        Window window = this.getWindow();
        Log.e("FillDetailsCarActivity", "FillDetailsCarActivity");
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        dbHandler_transmission_type = new DBHandler_Transmission_type(FillDetailsCarActivity.this);
        dbHandler_bodyType = new DBHandler_BodyType(FillDetailsCarActivity.this);
        dbHandler_fueltypes = new DBHandler_Fueltypes(FillDetailsCarActivity.this);

        luggageList.clear();
        for (int i = 0; i <= 100; i++) {
            luggageList.add(i);
        }

        init();
    }


    public void init() {

        placesClient = Places.createClient(this);

        apiService = Config.getClient().create(APIService.class);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        tv_next = findViewById(R.id.tv_next);
        iv_back = findViewById(R.id.iv_back);
        tv_location = findViewById(R.id.et_location);
        tv_carDetails = findViewById(R.id.car_details);
        tv_entercar = findViewById(R.id.enter_car);
        tv_carfuel = findViewById(R.id.car_fuel);
        subCarDetails = findViewById(R.id.sub_car_details);
        carName = findViewById(R.id.car_name);
        car_seat = findViewById(R.id.car_seat);
        car_door = findViewById(R.id.car_door);
        car_luggage = findViewById(R.id.car_luggage);
        maxPassenger = findViewById(R.id.car_passenger);
        checkBox1 = findViewById(R.id.iv_yes);
        checkBox2 = findViewById(R.id.iv_yes2);

        tv_carTrans = findViewById(R.id.et_carTrans);
        tv_carOdo = findViewById(R.id.car_odoMeter);
        tv_carType = findViewById(R.id.car_type);
        tv_carPrice = findViewById(R.id.car_price);
        carBondValue = findViewById(R.id.car_bondvalue);
        driverMinAge = findViewById(R.id.driver_min_age);



        setCustomTypeFace(tv_carTrans,this);
        setCustomTypeFace(tv_carOdo,this);
        setCustomTypeFace(tv_carType,this);
        setCustomTypeFace(tv_carPrice,this);
        setCustomTypeFace(car_seat,this);
        setCustomTypeFace(car_door,this);
        setCustomTypeFace(car_luggage,this);
        setCustomTypeFace(maxPassenger,this);
        setCustomTypeFace(driverMinAge,this);
        setCustomTypeFace(tv_carfuel,this);

        transmissionLsit.clear();
        transmissionLsit = dbHandler_transmission_type.getAllJobs();

        vehicleTypeList.clear();
        vehicleTypeList = dbHandler_bodyType.getAllJobs();

        fuelTypeList.clear();
        fuelTypeList = dbHandler_fueltypes.getAllJobs();

        odoList.clear();
        odoList.add("0-80k kilometers");
        odoList.add("80-160k kilometers");
        odoList.add("160-200k kilometers");
        odoList.add("200k+ kilometers");

        carPriceList.clear();
        carPriceList.add("US$0 - US$5000");
        carPriceList.add("US$5,000 - US$10,000");
        carPriceList.add("US$10,000 - US$15,000");
        carPriceList.add("US$15,000 - US$20,000");


        tv_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_entercar.setOnClickListener(this);
        tv_carTrans.setOnClickListener(this);
        tv_carType.setOnClickListener(this);
        tv_carOdo.setOnClickListener(this);
        tv_carPrice.setOnClickListener(this);
        tv_carfuel.setOnClickListener(this);
        car_seat.setOnClickListener(this);
        car_door.setOnClickListener(this);
        car_luggage.setOnClickListener(this);
        maxPassenger.setOnClickListener(this);
        driverMinAge.setOnClickListener(this);

        carName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonbg();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    private void updateButtonbg() {
        ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

        if (listingCarModel == null) {
            tv_next.setEnabled(false);
            subCarDetails.setVisibility(View.GONE);
            tv_entercar.setVisibility(View.GONE);
            return;
        }

        if (!listingCarModel.isLocationSet()) {
            tv_next.setEnabled(false);
            subCarDetails.setVisibility(View.GONE);
            tv_entercar.setVisibility(View.GONE);
            return;
        }

        if (listingCarModel.getCountry() != null) {
            tv_location.setText(listingCarModel.getStreetAdrees() + " " + listingCarModel.getCity() + " " + listingCarModel.getState() + " " + listingCarModel.getCountryCode() + " " + listingCarModel.getZipCode());
        }

        if (FijiRentalUtils.isEmptyTextView(tv_location)) {
            tv_next.setEnabled(false);
            subCarDetails.setVisibility(View.GONE);
            tv_entercar.setVisibility(View.GONE);
            return;
        }

        if (listingCarModel.isCountryChange()) {
            subCarDetails.setVisibility(View.GONE);

            tv_entercar.setText("START");
        }

        tv_entercar.setVisibility(View.VISIBLE);

        VinModel vinModel = listingCarModel.getModel();

        if (vinModel == null) {
            subCarDetails.setVisibility(View.GONE);

            tv_entercar.setText("START");
            return;
        }

//        if (listingCarModel.getCountryCode().equalsIgnoreCase("US")) {
//
//            if (vinModel.getVinNumber() == null) {
//                subCarDetails.setVisibility(View.GONE);
//
//                tv_entercar.setText("START");
//                return;
//            }
//
//            if (TextUtils.isEmpty(vinModel.getVinNumber())) {
//                tv_entercar.setText("START");
//                return;
//            }
//            tv_carDetails.setText(vinModel.getMake() + " " + vinModel.getModel() + " " + vinModel.getYear() + "\n#" + vinModel.getVinNumber());
//            tv_entercar.setText("EDIT");
//        } else {

        if (vinModel.getMake() == null || TextUtils.isEmpty(vinModel.getMake())) {
            subCarDetails.setVisibility(View.GONE);

            tv_entercar.setText("START");
            return;
        }

        tv_carDetails.setText(vinModel.getMake() + " " + vinModel.getModel() + " " + vinModel.getYear());
        tv_entercar.setText("EDIT");
//        }


        if (FijiRentalUtils.isEmptyTextView(tv_carDetails)) {
            subCarDetails.setVisibility(View.GONE);
            tv_next.setEnabled(false);
            return;
        }
        subCarDetails.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(vinModel.getVehicleType())) {
            tv_carType.setVisibility(View.GONE);
        }

        if (FijiRentalUtils.isEmptyTextView(tv_carTrans) || FijiRentalUtils.isEmptyTextView(tv_carOdo) || FijiRentalUtils.isEmptyTextView(tv_carPrice) || FijiRentalUtils.isEmptyTextView(tv_carfuel)) {
            tv_next.setEnabled(false);
            return;
        }

        if (TextUtils.isEmpty(car_seat.getText().toString()) || TextUtils.isEmpty(car_door.getText().toString()) || TextUtils.isEmpty(car_luggage.getText().toString())|| TextUtils.isEmpty(maxPassenger.getText().toString())) {
            tv_next.setEnabled(false);
            return;
        }

        if (TextUtils.isEmpty(carName.getText().toString())) {
            tv_next.setEnabled(false);
            return;
        }


        tv_next.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateButtonbg();
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
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("TAGS", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_next:

                ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
                VinModel vinModel = listingCarModel.getModel();
                vinModel.setCarPriceRange(tv_carPrice.getText().toString());
                vinModel.setOdometer(tv_carOdo.getText().toString());
                listingCarModel.setTransmissionType(transmissionType);
                listingCarModel.setBodyType(bodyType);
                listingCarModel.setFuelType(fuelType);
                listingCarModel.setSeatCount(car_seat.getText().toString());
                listingCarModel.setDoorCount(car_door.getText().toString());
                listingCarModel.setModel(vinModel);

                ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);

                if (!checkBox1.isChecked()) {
//                    btnDialog = new CustomButtonDialog(this, " ", getString(R.string.unchec_title), "OK", this);
//                    btnDialog.show(getSupportFragmentManager(), "TITLE_SALVAGE");
                    FijiRentalUtils.ShowValidation(getString(R.string.unchec_title),FillDetailsCarActivity.this,"");
                    return;
                }


                if (listingCarModel.isUnderYear()) {
                    Intent i = new Intent(this, SpacialityCarActivity.class);
                    startActivity(i);
                } else {

                    if (Integer.parseInt(listingCarModel.getModel().getYear()) < FijiRentalUtils.MIN_CAR_YEAR) {
                        Intent i = new Intent(this, SpacialityCarActivity.class);
                        startActivity(i);
                    } else {

//                        Intent i = new Intent(this, CarEligible.class);
//                        startActivity(i);
                        ListCar();

                    }
                }


                break;
            case R.id.et_location:


                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setTypeFilter(TypeFilter.REGIONS)
                        .setTypeFilter(TypeFilter.GEOCODE)
                        .build(FillDetailsCarActivity.this);

                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);


//                Intent addressIntent = new Intent(this, AddressActivity.class);
//                startActivity(addressIntent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.enter_car:

                ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

//                if (carModel.getCountryCode().equalsIgnoreCase("US")) {
//                    Intent carIntent = new Intent(this, YourCarActivity.class);
//                    startActivity(carIntent);
//                } else {

                Intent carIntent = new Intent(this, YourCarDetail.class);
                startActivity(carIntent);
//                }


                break;
            case R.id.et_carTrans:
                customDialog = new CustomDialog(this, transmissionLsit, this);

                customDialog.show(getSupportFragmentManager(), "TRANSMISSION");
                break;
            case R.id.car_type:
                customDialog = new CustomDialog(this, vehicleTypeList, this);

                customDialog.show(getSupportFragmentManager(), "VEHICLETYPE");
                break;
            case R.id.car_odoMeter:
                customDialog = new CustomDialog(this, odoList, this);

                customDialog.show(getSupportFragmentManager(), "ODOMETER");
                break;
            case R.id.car_price:
                customDialog = new CustomDialog(this, carPriceList, this);

                customDialog.show(getSupportFragmentManager(), "CARPRICE");
                break;
            case R.id.car_fuel:
                customDialog = new CustomDialog(this, fuelTypeList, this);

                customDialog.show(getSupportFragmentManager(), "CARFUEL");
                break;

            case R.id.car_seat:
                FijiRentalUtils.hideKeyboard(this);
                customDialog = new CustomDialog(this, seatCountList, this);

                customDialog.show(getSupportFragmentManager(), "CAR_SEAT");
                break;
            case R.id.car_door:
                FijiRentalUtils.hideKeyboard(this);
                customDialog = new CustomDialog(this, doorCountList, this);
                customDialog.show(getSupportFragmentManager(), "CAR_DOOR");
                break;
            case R.id.car_luggage:
                FijiRentalUtils.hideKeyboard(this);
                customDialog = new CustomDialog(this, FijiRentalUtils.getLugguageList(), this);
                customDialog.show(getSupportFragmentManager(), "CAR_LUGGAGE");
                break;
            case R.id.driver_min_age:
                FijiRentalUtils.hideKeyboard(this);
                customDialog = new CustomDialog(this, FijiRentalUtils.getDriverAgeList(), this);
                customDialog.show(getSupportFragmentManager(), "DRIVER_MIN");
                break;
            case R.id.car_passenger:
                FijiRentalUtils.hideKeyboard(this);
                ArrayList passengerList = new ArrayList();
                passengerList.addAll(seatCountList);
                passengerList.remove(0);
                customDialog = new CustomDialog(this, passengerList, this);
                customDialog.show(getSupportFragmentManager(), "MAX_PASSENGER");
                break;

        }
    }


    @Override
    public void onItemClick(Object val) {
        if (customDialog.getTag().equals("TRANSMISSION")) {

            transmissionType = (TransmissionType) val;
            tv_carTrans.setText(transmissionType.getTransmissionTypeTitle());
        }
        if (customDialog.getTag().equals("VEHICLETYPE")) {

            bodyType = (BodyType) val;
            tv_carType.setText(bodyType.getBodyTypeTitle());
        }

        if (customDialog.getTag().equals("ODOMETER")) {
            tv_carOdo.setText(String.valueOf(val));
        }
        if (customDialog.getTag().equals("CARPRICE")) {
            tv_carPrice.setText(String.valueOf(val));
        }
        if (customDialog.getTag().equals("CARFUEL")) {
            fuelType = (FuelType) val;
            tv_carfuel.setText(fuelType.getFuelTypeTitle());
        }
        if (customDialog.getTag().equals("CAR_SEAT")) {

            if (val.equals("None")) {
                car_seat.setText("");
            } else {
                car_seat.setText((String) val);
            }

        }
        if (customDialog.getTag().equals("CAR_DOOR")) {
            if (val.equals("None")) {
                car_door.setText("");
            } else {
                car_door.setText((String) val);
            }
        }

        if (customDialog.getTag().equalsIgnoreCase("CAR_LUGGAGE")) {
            car_luggage.setText(String.valueOf(val));
        }
        if (customDialog.getTag().equalsIgnoreCase("DRIVER_MIN")) {
            driverMinAge.setText(String.valueOf(val));
        }
        if (customDialog.getTag().equalsIgnoreCase("MAX_PASSENGER")) {
            maxPassenger.setText(String.valueOf(val));
        }
        updateButtonbg();

        customDialog.dismiss();

    }

    @Override
    public void onButtonClicked() {
        if (btnDialog.getTag().equalsIgnoreCase("TITLE_SALVAGE")) {

        }
        btnDialog.dismiss();

    }

    private void ListCar() {

        Log.e("TAGS", "check " + String.valueOf(checkBox2.isChecked()));

        ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();


        progressDialog.show();
        HashMap<String, String> data = new HashMap<>();
        data.put("partner_id", FijiRentalUtils.getId(this));
        data.put("manufacturer_id", carModel.getModel().getManufactureID());
        data.put("body_type_id", carModel.getBodyType().getBody_type_id());
        data.put("transmission_type_id", carModel.getTransmissionType().getTransmissionTypeId());
        data.put("fuel_type_id", fuelType.getFuelTypeId());
        data.put("item_mileage", "");

        data.put("item_doors", "");
        if(!TextUtils.isEmpty(driverMinAge.getText().toString())){
            data.put("min_driver_age", driverMinAge.getText().toString().trim());
        }
        data.put("car_make", carModel.getModel().getMake());
        data.put("car_model", carModel.getModel().getModel());
        data.put("model_year", carModel.getModel().getYear());
        data.put("model_name", carName.getText().toString());

        if (TextUtils.isEmpty(car_luggage.getText().toString().trim())) {
            data.put("max_luggage", "0");
        } else {
            data.put("max_luggage", car_luggage.getText().toString());
        }
        if (TextUtils.isEmpty(maxPassenger.getText().toString().trim())) {
            data.put("max_passengers", "");
        } else {
            data.put("max_passengers", maxPassenger.getText().toString());
        }

        if (TextUtils.isEmpty(carBondValue.getText().toString())) {
            data.put("fixed_deposit", "");
        } else {
            data.put("fixed_deposit", carBondValue.getText().toString());
        }

        if (carModel.getSeatCount().equalsIgnoreCase("none")) {
            data.put("item_model_seats", "");
        } else {
            data.put("item_model_seats", carModel.getSeatCount());
        }

        if (carModel.getDoorCount().equalsIgnoreCase("none")) {
            data.put("item_model_doors", "");
        } else {
            data.put("item_model_doors", carModel.getDoorCount());
        }

        data.put("item_model_latitude", String.valueOf(carModel.getLatLng().latitude));
        data.put("item_model_longitude", String.valueOf(carModel.getLatLng().longitude));
        data.put("item_model_description", "");
        data.put("location", carModel.getStreetAdrees());
        data.put("car_country", carModel.getCountryCode());
        data.put("car_city", carModel.getCity());
        data.put("car_state", carModel.getState());
        data.put("street", carModel.getStreetAdrees());
        data.put("postcode", carModel.getZipCode());
        data.put("fuel_type_id", carModel.getFuelType().getFuelTypeId());
        data.put("features[0]", "");
        data.put("features[1]", "");
        data.put("model_car_commercialHost", String.valueOf(checkBox2.isChecked()));
        data.put("model_car_isNotSalvaged", String.valueOf(checkBox1.isChecked()));
        data.put("vin", FijiRentalUtils.getMapValue(carModel.getModel().getVinNumber()));
        data.put("odometer", carModel.getModel().getOdometer());
        data.put("pricerange", carModel.getModel().getCarPriceRange());
        data.put("seatbelttype", FijiRentalUtils.getMapValue(carModel.getModel().getSeatbeltType()));
        Call<ResponseBody> call = apiService.listYourCar(FijiRentalUtils.getAccessToken(this), data);
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
                            FijiRentalUtils.Logger("TAGS", "vin res " + jstr);
                            JSONObject dataObject = jsonobject.optJSONObject("data");
                            JSONObject vinDataObject = dataObject.optJSONObject("data");
                            passData(vinDataObject);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, FillDetailsCarActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, FillDetailsCarActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());
                progressDialog.dismiss();

            }
        });

    }

    private void passData(JSONObject vinDataObject) {

        ListingCarModel carlistModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
        carlistModel.setItemId(vinDataObject.optString("item_id"));
        ((FijiCarRentalApplication) getApplication()).setListCarModel(carlistModel);

        CarModel carModel = new CarModel();
        carModel.setItemId(vinDataObject.optString("item_id"));
        carModel.setItemSku(vinDataObject.optString("item_sku"));
        carModel.setItemPageId(vinDataObject.optString("item_page_id"));
        carModel.setPartnerId(vinDataObject.optString("partner_id"));
        carModel.setManufacturerId(vinDataObject.optString("manufacturer_id"));
        carModel.setBodyTypeId(vinDataObject.optString("body_type_id"));
        carModel.setTransmissionTypeId(vinDataObject.optString("transmission_type_id"));
        carModel.setFuelTypeId(vinDataObject.optString("fuel_type_id"));
        carModel.setModelName(vinDataObject.optString("model_name"));
        carModel.setItemImage1(vinDataObject.optString("item_image_1"));
        carModel.setItemImage2(vinDataObject.optString("item_image_2"));
        carModel.setItemImage3(vinDataObject.optString("item_image_3"));
        carModel.setMileage(vinDataObject.optString("mileage"));
        carModel.setFuelConsumption(vinDataObject.optString("fuel_consumption"));
        carModel.setEngineCapacity(vinDataObject.optString("engine_capacity"));
        carModel.setMaxPassengers(vinDataObject.optString("max_passengers"));
        carModel.setMaxLuggage(vinDataObject.optString("max_luggage"));
        carModel.setItemDoors(vinDataObject.optString("item_doors"));
        carModel.setMinDriverAge(vinDataObject.optString("min_driver_age"));
        carModel.setPriceGroupId(vinDataObject.optString("price_group_id"));
        carModel.setFixedRentalDeposit(vinDataObject.optString("fixed_rental_deposit"));
        carModel.setUnitsInStock(vinDataObject.optString("units_in_stock"));
        carModel.setUnitsInStock(vinDataObject.optString("max_units_per_booking"));
        carModel.setOptionsMeasurementUnit(vinDataObject.optString("options_measurement_unit"));
        carModel.setBlogId(vinDataObject.optString("blog_id"));
        carModel.setModelYear(vinDataObject.optString("model_year"));
        carModel.setModelSeats(vinDataObject.optString("model_seats"));
        carModel.setModelDoors(vinDataObject.optString("model_doors"));
        carModel.setModelLatitude(vinDataObject.optString("model_latitude"));
        carModel.setModelLongitude(vinDataObject.optString("model_longitude"));
        carModel.setPrice(vinDataObject.optString("price"));
        carModel.setModelDescription(vinDataObject.optString("model_description"));
        carModel.setModelCarLocation(vinDataObject.optString("model_car_location"));
        carModel.setModelCarMake(vinDataObject.optString("model_car_make"));
        carModel.setModelCarModel(vinDataObject.optString("model_car_model"));
        carModel.setModelCarIsNotSalvaged(vinDataObject.optString("model_car_isNotSalvaged"));
        carModel.setModelCarCommercialHost(vinDataObject.optString("model_car_commercialHost"));
        carModel.setModelCarLicenseLicenseCountry(vinDataObject.optString("model_car_license_licenseCountry"));
        carModel.setModelCarLicenseLicenseState(vinDataObject.optString("model_car_license_licenseState"));
        carModel.setModelCarLicenseLicenseNumber(vinDataObject.optString("model_car_license_licenseNumber"));
        carModel.setModelCarLicenseFirstName(vinDataObject.optString("model_car_license_firstName"));
        carModel.setModelCarLicenseMiddleName(vinDataObject.optString("model_car_license_middleName"));
        carModel.setModelCarLicenseLastName(vinDataObject.optString("model_car_license_lastName"));
        carModel.setModelCarLicenseExpirationMonth(vinDataObject.optString("model_car_license_expirationMonth"));
        carModel.setModelCarLicenseExpirationDay(vinDataObject.optString("model_car_license_expirationDay"));
        carModel.setModelCarLicenseExpirationYear(vinDataObject.optString("model_car_license_expirationYear"));
        carModel.setModelCarLicenseBirthMonth(vinDataObject.optString("model_car_license_birthMonth"));
        carModel.setModelCarLicenseBirthYear(vinDataObject.optString("model_car_license_birthYear"));
        carModel.setModelCarLicenseBirthDay(vinDataObject.optString("model_car_license_birthDay"));
        carModel.setModelCarGoalsEarning(vinDataObject.optString("model_car_goals_earning"));
        carModel.setModelCarGoalsOwnerUtilization(vinDataObject.optString("model_car_goals_owner_utilization"));
        carModel.setModelCarGoalsRenterUtilization(vinDataObject.optString("model_car_goals_renter_utilization"));
        carModel.setModelCarAvailabilityAdvanceNotice(vinDataObject.optString("model_car_availability_advanceNotice"));
        carModel.setModelCarDurationMinimumTripDuration(vinDataObject.optString("model_car_duration_minimumTripDuration"));
        carModel.setModelCarDurationLongerWeekendTripPreferred(vinDataObject.optString("model_car_duration_longerWeekendTripPreferred"));
        carModel.setModelCarDurationMaximumTripDuration(vinDataObject.optString("model_car_duration_maximumTripDuration"));
        carModel.setModelCarLicensePlateNumber(vinDataObject.optString("model_car_licensePlateNumber"));
        carModel.setModelCarLicensePlateRegion(vinDataObject.optString("model_car_licensePlateRegion"));
        carModel.setBookInstatnly(vinDataObject.optString("book_instatnly"));
        carModel.setDeliveryDirectToYou(vinDataObject.optString("delivery_direct_to_you"));
        carModel.setPickupDate(vinDataObject.optString("pickup_date"));
        carModel.setReturnDate(vinDataObject.optString("return_date"));

        JSONObject ratingObject = vinDataObject.optJSONObject("ratings");

        RatingModel ratingModel = new RatingModel();
        ratingModel.setRatingCount(ratingObject.optString("rating_count"));
        ratingModel.setAvg_rating(ratingObject.optInt("average_rating"));

        carModel.setRatingModel(ratingModel);

        JSONArray futuresArray = vinDataObject.optJSONArray("futures");

        List<FutureModel> futureList = new ArrayList<>();

        for (int k = 0; k < futuresArray.length(); k++) {
            JSONObject futureObject = futuresArray.optJSONObject(k);
            FutureModel futureModel = new FutureModel();
            futureModel.setFeatureId(futureObject.optString("feature_id"));
            futureModel.setFeatureTitle(futureObject.optString("feature_title"));

            futureList.add(futureModel);
        }

        carModel.setFuelTypeTitle(vinDataObject.optString("fuel_type_title"));
        carModel.setTransmissionTypeTitle(vinDataObject.optString("transmission_type_title"));
        carModel.setBodyTypeTitle(vinDataObject.optString("body_type_title"));
        carModel.setManufacturerTitle(vinDataObject.optString("manufacturer_title"));

        JSONObject partnerObject = vinDataObject.optJSONObject("partner_detail");

        PartnerDetail partnerDetail = new PartnerDetail();
        partnerDetail.setUserPicUrl(partnerObject.optString("user_pic_url"));
        partnerDetail.setFirstName(partnerObject.optString("first_name"));
        partnerDetail.setLastName(partnerObject.optString("last_name"));
        partnerDetail.setTotalTrip(partnerObject.optInt("total_trip"));
        partnerDetail.setJoined(partnerObject.optString("joined"));
        partnerDetail.setID(partnerObject.optInt("ID"));
        carModel.setIs_favourites(vinDataObject.optString("is_favourites"));
        JSONArray photos = vinDataObject.optJSONArray("photos");

        ImageUrl.clear();
        for (int j = 0; j < photos.length(); j++) {
            ImageUrl.add(photos.optString(j));

        }
        carModel.setPhotos(ImageUrl);

        Intent i = new Intent(this, CarEligible.class);
        startActivity(i);
    }

}