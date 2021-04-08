package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.Adapter.CarFeatureAdapter;
import com.app.fijirentalcars.Adapter.ColorAdapter;
import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.ColorModel;
import com.app.fijirentalcars.Model.FuelType;
import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.SQLDB.DBHandler_Feature;
import com.app.fijirentalcars.SQLDB.DBHandler_Fueltypes;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.fijirentalcars.util.FijiRentalUtils.setCustomTypeFace;

public class HostcarDetailsActivtiy extends AppCompatActivity implements View.OnClickListener, DialogItemListner {


    EditText carname, licenceNumber, cityMpg, hwyMpg, carDescription, carGuidelines, carBondValue;
    private DBHandler_Feature dbHandler_feature;
    private DBHandler_Fueltypes dbHandler_fueltypes;
    List<FutureModel> modelList = new ArrayList<>();

    List fuelTypeList = new ArrayList();
    List doorCountList = Arrays.asList("None", "2", "3", "4", "5");
    List seatCountList = Arrays.asList("None", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    TextView saveBtn;
    CustomDialog customDialog;
    CarFeatureAdapter carFeatureAdapter;
    RecyclerView recyclerView, colorListview;
    ColorAdapter colorAdapter;
    AutoCompleteTextView car_seat, car_door, car_fuel_type, car_luggage, maxPassenger, driverMinAge;
    List<ColorModel> colorModelList = new ArrayList<>();
    FuelType fuelType;
    ImageView backBtn;
    ProgressDialog progressDialog;
    CarModel carModel;
    APIService apiService;
    String selectedColor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostcar_details_activtiy);

        getCarColorList();

        carModel = Paper.book().read(FijiRentalUtils.carModel);


        dbHandler_feature = new DBHandler_Feature(this);
        dbHandler_fueltypes = new DBHandler_Fueltypes(this);
        recyclerView = findViewById(R.id.carList_feature);
        colorListview = findViewById(R.id.carColorList);
        backBtn = findViewById(R.id.iv_back);
        saveBtn = findViewById(R.id.save_btn);

        carname = findViewById(R.id.car_name);
        licenceNumber = findViewById(R.id.car_licenceNumber);
        cityMpg = findViewById(R.id.cityMpg);
        hwyMpg = findViewById(R.id.hwyMpg);
        carDescription = findViewById(R.id.car_desc);
        carGuidelines = findViewById(R.id.car_guideline);
        car_seat = findViewById(R.id.car_seat);
        car_door = findViewById(R.id.car_door);
        car_fuel_type = findViewById(R.id.car_fuelType);

        car_luggage = findViewById(R.id.car_luggage);
        maxPassenger = findViewById(R.id.car_passenger);
        carBondValue = findViewById(R.id.car_bondvalue);
        driverMinAge = findViewById(R.id.driver_min_age);

        carname.setTypeface(FijiRentalUtils.getRegularTypeFace(this));
        licenceNumber.setTypeface(FijiRentalUtils.getRegularTypeFace(this));
        cityMpg.setTypeface(FijiRentalUtils.getRegularTypeFace(this));
        hwyMpg.setTypeface(FijiRentalUtils.getRegularTypeFace(this));
        carDescription.setTypeface(FijiRentalUtils.getRegularTypeFace(this));
        carGuidelines.setTypeface(FijiRentalUtils.getRegularTypeFace(this));
        car_seat.setTypeface(FijiRentalUtils.getRegularTypeFace(this));
        car_door.setTypeface(FijiRentalUtils.getRegularTypeFace(this));
        car_fuel_type.setTypeface(FijiRentalUtils.getRegularTypeFace(this));

        car_fuel_type.setOnClickListener(this);
        car_seat.setOnClickListener(this);
        car_door.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        driverMinAge.setOnClickListener(this);
        car_luggage.setOnClickListener(this);
        maxPassenger.setOnClickListener(this);

        ListingCarModel listingCarModel = new ListingCarModel();

        if (carModel != null) {
            carname.setText(carModel.getModelName());
            licenceNumber.setText(carModel.getModelCarLicensePlateNumber());
            carDescription.setText(carModel.getModelDescription());
            carGuidelines.setText(carModel.getGuidelines());

            if (carModel.getModelDoors().equals("0")) {
                car_door.setText("", false);
            } else {
                car_door.setText(carModel.getModelDoors(), false);
            }
            if (carModel.getModelSeats().equals("0")) {
                car_seat.setText("", false);
            } else {
                car_seat.setText(carModel.getModelSeats(), false);
            }

            car_fuel_type.setText(carModel.getFuelTypeTitle(), false);

            hwyMpg.setText(carModel.getMileage());

            if(!TextUtils.isEmpty(carModel.getCityMileage())){
                if(Double.parseDouble(carModel.getCityMileage())==0){
                    cityMpg.setText("");
                }else {
                    cityMpg.setText(String.valueOf((int)Double.parseDouble(carModel.getCityMileage())));
                }
            }else {
                cityMpg.setText("");
            }


            if (carModel.getMaxLuggage().equals("0")) {
                car_luggage.setText("", false);
            } else {
                car_luggage.setText(carModel.getMaxLuggage(), false);
            }
            if (carModel.getMaxPassengers().equals("0")) {
                maxPassenger.setText("", false);
            } else {
                maxPassenger.setText(carModel.getMaxPassengers(), false);
            }

            if (carModel.getMinDriverAge().equals("0")) {
                driverMinAge.setText("", false);
            } else {
                driverMinAge.setText(carModel.getMinDriverAge(), false);
            }

            if (carModel.getFixedRentalDeposit().equals("0") || TextUtils.isEmpty(carModel.getFixedRentalDeposit())) {
                carBondValue.setText("");
            } else {
                carBondValue.setText(carModel.getFixedRentalDeposit());
            }


            List featureString = new ArrayList();
            for (FutureModel model : carModel.getFutures()) {
                featureString.add(model.getFeatureId());
            }

            FijiRentalUtils.Logger("TAGS", "features li " + carModel.getFutures());


            listingCarModel.setCarFeatures(TextUtils.join(",", featureString));
            selectedColor = carModel.getItemColor();

        }

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        colorListview.setLayoutManager(new GridLayoutManager(this, 4));

        modelList.clear();
        modelList.addAll(dbHandler_feature.getAllJobs());
        fuelTypeList.clear();
        fuelTypeList.addAll(dbHandler_fueltypes.getAllJobs());


        carFeatureAdapter = new CarFeatureAdapter(this, modelList, listingCarModel);
        recyclerView.setAdapter(carFeatureAdapter);

        colorAdapter = new ColorAdapter(this, colorModelList, selectedColor);
        colorListview.setAdapter(colorAdapter);

        carFeatureAdapter.notifyDataSetChanged();

        backBtn.setOnClickListener(this);

        setCustomTypeFace(car_luggage, this);
        setCustomTypeFace(maxPassenger, this);
        setCustomTypeFace(driverMinAge, this);
        setCustomTypeFace(car_seat, this);
        setCustomTypeFace(car_door, this);
        setCustomTypeFace(car_fuel_type, this);
        setCustomTypeFace(carname, this);
        setCustomTypeFace(licenceNumber, this);
        setCustomTypeFace(carBondValue, this);
        setCustomTypeFace(cityMpg, this);
        setCustomTypeFace(hwyMpg, this);
    }


    private void getCarColorList() {
        colorModelList.clear();

        ColorModel colorModel = new ColorModel();
        colorModel.setColorCode(R.color.quantum_googredA700);
        colorModel.setColorName("Red");
        colorModelList.add(colorModel);

        ColorModel colorModel1 = new ColorModel();
        colorModel1.setColorCode(R.color.quantum_yellow);
        colorModel1.setColorName("Yellow");
        colorModelList.add(colorModel1);

        ColorModel colorModel2 = new ColorModel();
        colorModel2.setColorCode(R.color.quantum_googgreen800);
        colorModel2.setColorName("Green");
        colorModelList.add(colorModel2);

        ColorModel colorModel3 = new ColorModel();
        colorModel3.setColorCode(R.color.quantum_googblue);
        colorModel3.setColorName("Blue");
        colorModelList.add(colorModel3);

        ColorModel colorModel4 = new ColorModel();
        colorModel4.setColorCode(R.color.quantum_orange);
        colorModel4.setColorName("Gold");
        colorModelList.add(colorModel4);

        ColorModel colorModel5 = new ColorModel();
        colorModel5.setColorCode(R.color.white);
        colorModel5.setColorName("White");
        colorModelList.add(colorModel5);

        ColorModel colorModel6 = new ColorModel();
        colorModel6.setColorCode(R.color.black);
        colorModel6.setColorName("Black");
        colorModelList.add(colorModel6);

        ColorModel colorModel7 = new ColorModel();
        colorModel7.setColorCode(R.color.grey_700);
        colorModel7.setColorName("Grey");
        colorModelList.add(colorModel7);

        ColorModel colorModel8 = new ColorModel();
        colorModel8.setColorCode(R.color.grey_200);
        colorModel8.setColorName("Silver");
        colorModelList.add(colorModel8);

        ColorModel colorModel9 = new ColorModel();
        colorModel9.setColorCode(R.color.quantum_purple);
        colorModel9.setColorName("Other");
        colorModelList.add(colorModel9);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.car_fuelType) {
            FijiRentalUtils.hideKeyboard(this);
            customDialog = new CustomDialog(this, fuelTypeList, this);

            customDialog.show(getSupportFragmentManager(), "CAR_FUEL");
        } else if (v.getId() == R.id.car_seat) {
            FijiRentalUtils.hideKeyboard(this);
            customDialog = new CustomDialog(this, seatCountList, this);

            customDialog.show(getSupportFragmentManager(), "CAR_SEAT");
        } else if (v.getId() == R.id.car_door) {
            FijiRentalUtils.hideKeyboard(this);
            customDialog = new CustomDialog(this, doorCountList, this);
            customDialog.show(getSupportFragmentManager(), "CAR_DOOR");
        } else if (v.getId() == R.id.save_btn) {
            updateDate();
        } else if (v.getId() == R.id.car_luggage) {
            FijiRentalUtils.hideKeyboard(this);
            customDialog = new CustomDialog(this, FijiRentalUtils.getLugguageList(), this);
            customDialog.show(getSupportFragmentManager(), "CAR_LUGGAGE");
        } else if (v.getId() == R.id.driver_min_age) {
            FijiRentalUtils.hideKeyboard(this);
            customDialog = new CustomDialog(this, FijiRentalUtils.getDriverAgeList(), this);
            customDialog.show(getSupportFragmentManager(), "DRIVER_MIN");
        } else if (v.getId() == R.id.car_passenger) {
            FijiRentalUtils.hideKeyboard(this);
            ArrayList passengerList = new ArrayList();
            passengerList.addAll(seatCountList);
            passengerList.remove(0);
            customDialog = new CustomDialog(this, passengerList, this);
            customDialog.show(getSupportFragmentManager(), "MAX_PASSENGER");
        }
    }

    private void updateDate() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        reqBuilder.addFormDataPart("model_name", carname.getText().toString());
        reqBuilder.addFormDataPart("license_number", licenceNumber.getText().toString());
        reqBuilder.addFormDataPart("item_model_seats", car_seat.getText().toString());
        reqBuilder.addFormDataPart("item_model_doors", car_door.getText().toString());
        if (fuelType != null) {
            reqBuilder.addFormDataPart("fuel_type_id", fuelType.getFuelTypeId());
        }
        reqBuilder.addFormDataPart("mileage", hwyMpg.getText().toString());
        reqBuilder.addFormDataPart("city_mileage", cityMpg.getText().toString());
        reqBuilder.addFormDataPart("item_model_description", carDescription.getText().toString());
        reqBuilder.addFormDataPart("guidelines", carGuidelines.getText().toString());
        reqBuilder.addFormDataPart("item_color", colorAdapter.getSelectedColor());

        if (TextUtils.isEmpty(car_luggage.getText().toString().trim())) {
            reqBuilder.addFormDataPart("max_luggage", "0");
        } else {
            reqBuilder.addFormDataPart("max_luggage", car_luggage.getText().toString());
        }
        if (TextUtils.isEmpty(maxPassenger.getText().toString().trim())) {
            reqBuilder.addFormDataPart("max_passengers", "");
        } else {
            reqBuilder.addFormDataPart("max_passengers", maxPassenger.getText().toString());
        }

        if (TextUtils.isEmpty(carBondValue.getText().toString())) {
            reqBuilder.addFormDataPart("fixed_deposit", "");
        } else {
            reqBuilder.addFormDataPart("fixed_deposit", carBondValue.getText().toString());
        }

        if (car_seat.getText().toString().equalsIgnoreCase("none")) {
            reqBuilder.addFormDataPart("item_model_seats", "");
        } else {
            reqBuilder.addFormDataPart("item_model_seats", car_seat.getText().toString());
        }

        if (car_door.getText().toString().equalsIgnoreCase("none")) {
            reqBuilder.addFormDataPart("item_model_doors", "");
        } else {
            reqBuilder.addFormDataPart("item_model_doors", car_door.getText().toString());
        }
//        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        String[] features = carFeatureAdapter.getFeatured().split(",");

        for (int i = 0; i < features.length; i++) {
            reqBuilder.addFormDataPart("features[" + (i) + "]", features[i]);
        }


        body = reqBuilder
                .build();

        Call<ResponseBody> call = apiService.updateCarListing(FijiRentalUtils.getAccessToken(this), body);
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
                            JSONObject object = dataObject.optJSONObject("data");
                            FijiRentalUtils.updateCarModel(object, HostcarDetailsActivtiy.this);
                            finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, HostcarDetailsActivtiy.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, HostcarDetailsActivtiy.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());
                progressDialog.dismiss();

            }
        });

    }

    @Override
    public void onItemClick(Object val) {
        if (customDialog.getTag().equals("CAR_FUEL")) {

            fuelType = (FuelType) val;
            car_fuel_type.setText(fuelType.getFuelTypeTitle());
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
        customDialog.dismiss();
    }
}