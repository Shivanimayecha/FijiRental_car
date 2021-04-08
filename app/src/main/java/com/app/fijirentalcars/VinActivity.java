package com.app.fijirentalcars;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.CustomViews.CustomButtonDialog;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.Model.Manufacturer;
import com.app.fijirentalcars.Model.VinModel;
import com.app.fijirentalcars.SQLDB.DBHandler_Manufacture;
import com.app.fijirentalcars.listners.DialogButtonListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VinActivity extends AppCompatActivity implements View.OnClickListener, DialogButtonListner {

    TextView tv_submit, tv_vinNumber;
    ImageView iv_back;
    APIService apiService;
    ProgressDialog progressDialog;
    CustomButtonDialog btnDialog;
    AppCompatCheckBox olderCar;
    DBHandler_Manufacture dbHandler_manufacture;
    ArrayList<Manufacturer> manList=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vin);
        Window window = this.getWindow();
        Log.e("VinActivity", "VinActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        apiService = Config.getClient().create(APIService.class);
        dbHandler_manufacture=new DBHandler_Manufacture(this);
        manList.clear();
        manList.addAll(dbHandler_manufacture.getAllJobs());


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        init();
    }

    public void init() {

        tv_submit = findViewById(R.id.tv_submit);
        iv_back = findViewById(R.id.iv_back);
        tv_vinNumber = findViewById(R.id.et_vinnumber);
        olderCar = findViewById(R.id.car_older);
        olderCar.setChecked(true);
        tv_submit.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_vinNumber.addTextChangedListener(textWatcher);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_submit:

                if (FijiRentalUtils.checkInternetConnection(this)) {

                    checkVinNumber();
                }


//                Intent i = new Intent(this, SpacialityCarActivity.class);
//                startActivity(i);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    private void checkVinNumber() {


        String vinNumber = tv_vinNumber.getText().toString().trim().toUpperCase();

        if (vinNumber.length() != 17) {
            btnDialog = new CustomButtonDialog(this, getString(R.string.vin_not_recognized), getString(R.string.invalid_vin_length), "YES", this);
            btnDialog.show(getSupportFragmentManager(), "VIN_VALIDATION");
//            showAlertDialog();
            return;
        }

        progressDialog.show();
        HashMap<String, String> data = new HashMap<>();
        data.put("vin", vinNumber);
        Call<ResponseBody> call = apiService.checkCarVIN(FijiRentalUtils.getAccessToken(this), data);
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
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, VinActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, VinActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }

    private void passData(JSONObject vinDataObject) {
        if (!vinDataObject.optString("error-code").equalsIgnoreCase("0")) {

            if (olderCar.isChecked()) {
                btnDialog = new CustomButtonDialog(this, getString(R.string.vin_not_recognized), getString(R.string.double_check_vin), "YES", this);
                btnDialog.show(getSupportFragmentManager(), "VIN_VALIDATION_FAILED");

            } else {
                btnDialog = new CustomButtonDialog(this, getString(R.string.vehicle_not_identified), getString(R.string.could_not_find), "NEXT", this);
                btnDialog.show(getSupportFragmentManager(), "VIN_FAILED");
            }

            return;
        }
        VinModel vinModel = new VinModel();
        vinModel.setMake(vinDataObject.optString("make"));
        vinModel.setManufacturerName(vinDataObject.optString("manufacturer_name"));
        vinModel.setModel(vinDataObject.optString("model"));
        vinModel.setYear(vinDataObject.optString("year"));
        vinModel.setTrim(vinDataObject.optString("trim"));
        vinModel.setVehicleType(vinDataObject.optString("vehicle-type"));
        vinModel.setBodyclass(vinDataObject.optString("body-class"));
        vinModel.setDoor(vinDataObject.optString("doors"));
        vinModel.setGrossVehicleWeightRating(vinDataObject.optString("gross-vehicle-weight-rating"));
        vinModel.setDriveType(vinDataObject.optString("drive-type"));
        vinModel.setBreakeSystem(vinDataObject.optString("brake-system-type"));
        vinModel.setEngineCylinder(vinDataObject.optString("engine-number-of-cylinders"));
        vinModel.setFuelType(vinDataObject.optString("fuel-type-primary"));
        vinModel.setSeatbeltType(vinDataObject.optString("seat-belts-type"));
        vinModel.setVinNumber(tv_vinNumber.getText().toString());
        vinModel.setManufactureID(FijiRentalUtils.getManFacID(manList,vinDataObject.optString("make")));
        ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
        listingCarModel.setModel(vinModel);

        listingCarModel.setUnderYear(!olderCar.isChecked());


        ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);


        Intent i = new Intent(this, FillDetailsCarActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
//        finish();

    }




    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateView();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateView() {
        if (FijiRentalUtils.isEmptyTextView(tv_vinNumber)) {
            tv_submit.setEnabled(false);
            return;
        }
        tv_submit.setEnabled(true);
    }

    @Override
    public void onButtonClicked() {

        if (btnDialog.getTag().equals("VIN_FAILED")) {


            ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
            VinModel vinModel = listingCarModel.getModel();
            vinModel.setVinNumber(tv_vinNumber.getText().toString());
            listingCarModel.setModel(vinModel);

            ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);

            Intent i = new Intent(VinActivity.this, YourCarDetail.class);
            startActivity(i);
        }

        if (btnDialog.getTag().equals("VIN_VALIDATION_FAILED")) {
            tv_vinNumber.setText("");
        }

        btnDialog.dismiss();
    }
}
