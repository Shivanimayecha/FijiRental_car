package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.CountryModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner {

    TextView tv_next, tv_Country, tv_street, tv_city, tv_region, tv_postal;
    ImageView iv_back;
    //    List<Object> itemList=new ArrayList<>();
    List<Object> countryList = new ArrayList<>();
    CustomDialog customDialog;
    APIService apiService;
    CountryModel countryModel;
    ProgressDialog progressDialog;
    int CountryPos = 0;
    Geocoder geocoder;
    private int LOCATION_SUCCESS=102;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Window window = this.getWindow();
        Log.e("AddressActivity", "AddressActivity");
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        apiService = Config.getClient().create(APIService.class);

        geocoder = new Geocoder(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        getCountryList();

        init();

    }



    private void getCountryList() {

        progressDialog.show();
        Call<ResponseBody> call = apiService.getCountryList(FijiRentalUtils.getAccessToken(this));
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
                            JSONArray data_array = data.optJSONArray("countries");
                            passdata(data_array);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, AddressActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, AddressActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });


    }

    private void passdata(JSONArray data_array) {

        ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

        countryList.clear();
        for (int i = 0; i < data_array.length(); i++) {
            JSONObject object = data_array.optJSONObject(i);
            CountryModel countryModel = new CountryModel();
            countryModel.setCode(object.optString("code"));
            countryModel.setName(object.optString("name"));
            countryModel.setDial(object.optString("dial"));
            countryModel.setImage(object.optString("image"));

            if (listingCarModel.getCountryCode().equalsIgnoreCase(object.optString("code"))) {
                this.countryModel = countryModel;
                CountryPos = i;
            }

            countryList.add(countryModel);
        }

        tv_Country.setText(listingCarModel.getCountry());
        tv_street.setText(listingCarModel.getStreetAdrees());
        tv_city.setText(listingCarModel.getCity());
        tv_region.setText(listingCarModel.getState());
        if (!TextUtils.isEmpty(listingCarModel.getZipCode())) {
            tv_postal.setText(listingCarModel.getZipCode());
        }


    }

    public void init() {
        tv_next = findViewById(R.id.tv_next);
        iv_back = findViewById(R.id.iv_back);
        tv_Country = findViewById(R.id.et_country);
        tv_street = findViewById(R.id.et_streetaddress);
        tv_city = findViewById(R.id.et_city);
        tv_region = findViewById(R.id.et_region);
        tv_postal = findViewById(R.id.et_zippostal);
        tv_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_Country.setOnClickListener(this);

        tv_city.addTextChangedListener(textWatcher);
        tv_postal.addTextChangedListener(textWatcher);
        tv_street.addTextChangedListener(textWatcher);
        updateView();

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

        if (FijiRentalUtils.isEmptyTextView(tv_city) || FijiRentalUtils.isEmptyTextView(tv_street) || FijiRentalUtils.isEmptyTextView(tv_postal)) {
            tv_next.setEnabled(false);
            return;
        }

        tv_next.setEnabled(true);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_next:

                ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();


                listingCarModel.setCity(tv_city.getText().toString().trim());
                listingCarModel.setState(tv_region.getText().toString().trim());
                listingCarModel.setCountry(tv_Country.getText().toString().trim());
                listingCarModel.setZipCode(tv_postal.getText().toString().trim());
                listingCarModel.setStreetAdrees(tv_street.getText().toString().trim());
                listingCarModel.setCountryCode(countryModel.getCode());

                String address = tv_street.getText().toString().trim() + "," + tv_city.getText().toString().trim() + "," + tv_region.getText().toString().trim() + "," +
                        tv_Country.getText().toString().trim() + " " + tv_postal.getText().toString();

                if (TextUtils.isEmpty(listingCarModel.getLatLng().toString().trim())) {

                    List<Address> latAddress = new ArrayList<>();

                    try {
                        latAddress = geocoder.getFromLocationName(address, 100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.e("TAG", "addrwss " + latAddress + " po " + address);

                    if (latAddress.size() == 0) {
                        try {
                            latAddress = geocoder.getFromLocationName(tv_Country.getText().toString(), 100);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    LatLng latLng = new LatLng(latAddress.get(0).getLatitude(), latAddress.get(0).getLongitude());

                    listingCarModel.setLatLng(latLng);
                }
                ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);

                Intent i = new Intent(this, CarLocationActivity.class);
                startActivityForResult(i,LOCATION_SUCCESS);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_country:
                showCountryDialog();
                break;
        }

    }

    private void showCountryDialog() {


        customDialog = new CustomDialog(this, countryList, this);

        customDialog.show(getSupportFragmentManager(), "Country");
    }

    @Override
    public void onItemClick(Object val) {
        customDialog.dismiss();

        if (customDialog.getTag().equals("Country")) {

            countryModel = (CountryModel) val;
            tv_Country.setText(countryModel.getName());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LOCATION_SUCCESS){
            if(resultCode==RESULT_OK){
                String result=data.getStringExtra("result");

                if(result.equals("success")){
                    finish();
                }
            }
        }
    }
}
