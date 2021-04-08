package com.app.fijirentalcars;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.fijirentalcars.Model.BodyType;
import com.app.fijirentalcars.Model.FuelType;
import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.Manufacturer;
import com.app.fijirentalcars.Model.TransmissionType;
import com.app.fijirentalcars.SQLDB.DBHandler_BodyType;
import com.app.fijirentalcars.SQLDB.DBHandler_Feature;
import com.app.fijirentalcars.SQLDB.DBHandler_Fueltypes;
import com.app.fijirentalcars.SQLDB.DBHandler_Manufacture;
import com.app.fijirentalcars.SQLDB.DBHandler_Transmission_type;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    String TAG = "SplashActivity";
    private DBHandler_Fueltypes dbHandler_fueltypes;
    private DBHandler_BodyType dbHandler_bodyType;
    private DBHandler_Transmission_type dbHandler_transmission_type;
    private DBHandler_Manufacture dbHandler_manufacture;
    private DBHandler_Feature dbHandler_feature;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window = this.getWindow();
        Log.e("SplashActivity", "SplashActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

//         printHashKey(SplashActivity.this);
        dbHandler_fueltypes = new DBHandler_Fueltypes(SplashActivity.this);
        dbHandler_bodyType = new DBHandler_BodyType(SplashActivity.this);
        dbHandler_transmission_type = new DBHandler_Transmission_type(SplashActivity.this);
        dbHandler_manufacture = new DBHandler_Manufacture(SplashActivity.this);
        dbHandler_feature = new DBHandler_Feature(SplashActivity.this);

        if (FijiRentalUtils.isNetworkAvailable(SplashActivity.this)) {
            getgeneraloptions();

        }



/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 3000);*/
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }


    private void getgeneraloptions() {

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.getgeneraloptions();


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            JSONObject data = jsonobject.optJSONObject("data");
                            JSONObject data_array = data.optJSONObject("data");
                            passdata_general(data_array);

                        } else {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SplashActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SplashActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SplashActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

    private void passdata_general(JSONObject object) {
        //for (int i = 0; i < data_array.length(); i++) {
        //  JSONObject object = data_array.optJSONObject(i);

        JSONArray fuel_types = object.optJSONArray("fuel_types");
        if (fuel_types != null) {
            passdata_fuel_types(fuel_types);
        }

        JSONArray body_types = object.optJSONArray("body_types");
        if (body_types != null) {
            passdata_body_types(body_types);
        }

        JSONArray transmission_types = object.optJSONArray("transmission_types");
        if (transmission_types != null) {
            passdata_transmission_types(transmission_types);
        }

        JSONArray manufacturers = object.optJSONArray("manufacturers");
        if (manufacturers != null) {
            passdata_manufacture(manufacturers);
        }

        JSONArray feature = object.optJSONArray("features");
        if (feature != null) {
            passdata_feature(feature);
        }

        //     }

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void passdata_feature(JSONArray feature) {
        for (int i = 0; i < feature.length(); i++) {
            JSONObject object = feature.optJSONObject(i);
            FutureModel futureModel = new FutureModel();
            futureModel.setFeatureId(object.optString("feature_id"));
            futureModel.setFeatureTitle(object.optString("feature_title"));
            futureModel.setFeatureIcon(object.optString("feature_icon"));
            dbHandler_feature.addJobs(futureModel);
        }
    }

    private void passdata_fuel_types(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            JSONObject object = data.optJSONObject(i);
            FuelType fuelType = new FuelType();
            fuelType.setFuelTypeId(object.optString("fuel_type_id"));
            fuelType.setFuelTypeTitle(object.optString("fuel_type_title"));
            dbHandler_fueltypes.addJobs(fuelType);
        }
    }

    private void passdata_body_types(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            JSONObject object = data.optJSONObject(i);
            BodyType bodyType = new BodyType();
            bodyType.setBody_type_id(object.optString("body_type_id"));
            bodyType.setBodyTypeTitle(object.optString("body_type_title"));
            dbHandler_bodyType.addJobs(bodyType);
        }
    }

    private void passdata_transmission_types(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            JSONObject object = data.optJSONObject(i);
            TransmissionType transmissionType = new TransmissionType();
            transmissionType.setTransmissionTypeId(object.optString("transmission_type_id"));
            transmissionType.setTransmissionTypeTitle(object.optString("transmission_type_title"));
            dbHandler_transmission_type.addJobs(transmissionType);
        }
    }

    private void passdata_manufacture(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            JSONObject object = data.optJSONObject(i);
            Manufacturer manufacturer = new Manufacturer();manufacturer.setManufacturerId(object.optString("manufacturer_id"));
            manufacturer.setManufacturerTitle(object.optString("manufacturer_title"));
            manufacturer.setManufacturerLogo(object.optString("manufacturer_logo"));
            dbHandler_manufacture.addJobs(manufacturer);
        }
    }



}
