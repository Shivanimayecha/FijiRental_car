package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Model.CountryModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumberPlate extends AppCompatActivity {

    ImageView ivBack;
    EditText licensePlate;
    TextView tv_submit;
    APIService apiService;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_plate);

        apiService = Config.getClient().create(APIService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        ivBack = findViewById(R.id.iv_back);
        licensePlate = findViewById(R.id.license_number);
        tv_submit = findViewById(R.id.tv_continue);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(licensePlate.getText().toString().trim())) {
                    FijiRentalUtils.ShowValidation("Please enter a valid license plate number to continue.", NumberPlate.this, "");
                } else {

                    ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
                    listingCarModel.setLicensePlateNumber(licensePlate.getText().toString());
                    ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);


//                    Intent advanceNotice=new Intent(NumberPlate.this,LastPhotos.class);
//                    startActivity(advanceNotice);

                    ListCar();

                }
            }
        });

    }

    private void ListCar() {

        ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();


        progressDialog.show();

        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        reqBuilder.addFormDataPart("EARNING_GOALS", carModel.getPrimary_goal_of_sharing());
        reqBuilder.addFormDataPart("OWNER_UTILIZATION", carModel.getOften_use_of_car());
        reqBuilder.addFormDataPart("RENTER_UTILIZATION_PREFERENCE", carModel.getOften_share_car());
        reqBuilder.addFormDataPart("model_car_availability_advanceNotice", carModel.getAdvance_notice());
        reqBuilder.addFormDataPart("model_car_duration_minimumTripDuration", carModel.getMin_trip_duration());
        reqBuilder.addFormDataPart("model_car_duration_maximumTripDuration", carModel.getMax_trip_duration());
        String[] features = carModel.getCarFeatures().split(",");

        for (int i = 0; i < features.length; i++) {
            reqBuilder.addFormDataPart("features[" + (i) + "]", features[i]);
        }

        reqBuilder.addFormDataPart("item_model_description", carModel.getCarDescription());
        reqBuilder.addFormDataPart("model_car_licensePlateNumber", carModel.getLicensePlateNumber());


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

                            Intent i = new Intent(NumberPlate.this, LastPhotos.class);
                            startActivity(i);
                            finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, NumberPlate.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, NumberPlate.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());
                progressDialog.dismiss();

            }
        });

    }
}