package com.app.fijirentalcars;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.CarModelType;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.Model.MakeModel;
import com.app.fijirentalcars.Model.Manufacturer;
import com.app.fijirentalcars.Model.VinModel;
import com.app.fijirentalcars.SQLDB.DBHandler_Manufacture;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourCarDetail extends AppCompatActivity implements View.OnClickListener, DialogItemListner {

    List yearList=new ArrayList<>();
    List makeList=new ArrayList<>();
    List modelList=new ArrayList<>();

    ImageView ivBack;
    TextView submit, tv_year, tv_make, tv_model;
    CustomDialog customDialog;
    MakeModel makeModel;
    DBHandler_Manufacture dbHandler_manufacture;
    ArrayList<Manufacturer> manList=new ArrayList<>();
    APIService apiService;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_your_car);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));



        dbHandler_manufacture=new DBHandler_Manufacture(this);
        manList.clear();
        manList.addAll(dbHandler_manufacture.getAllJobs());



        ivBack = findViewById(R.id.iv_back);
        submit = findViewById(R.id.tv_submit);
        tv_year = findViewById(R.id.et_year);
        tv_make = findViewById(R.id.et_make);
        tv_model = findViewById(R.id.et_model);

        getArrayList();

        ivBack.setOnClickListener(this);
        submit.setOnClickListener(this);
        tv_year.setOnClickListener(this);
        tv_make.setOnClickListener(this);
        tv_model.setOnClickListener(this);

    }

    private void getArrayList() {

        yearList.clear();

        int year= Calendar.getInstance().get(Calendar.YEAR);

        for(int startyear=(year+1);startyear>=1900;startyear--){
            yearList.add(String.valueOf(startyear));
        }



//        FijiRentalUtils.Logger("TAGS","year "+year);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {
        if (FijiRentalUtils.isEmptyTextView(tv_year)) {
            tv_make.setEnabled(false);
            tv_model.setEnabled(false);
            submit.setEnabled(false);
            return;
        }
        tv_make.setEnabled(true);

        if (FijiRentalUtils.isEmptyTextView(tv_make)) {
            return;
        }
        tv_model.setEnabled(true);
        if (FijiRentalUtils.isEmptyTextView(tv_model)) {
            return;
        }
        submit.setEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_submit:

                VinModel vinModel=((FijiCarRentalApplication)getApplication()).getCarModel().getModel();
                vinModel.setYear(tv_year.getText().toString().trim());
                vinModel.setMake(tv_make.getText().toString().trim());
                vinModel.setModel(tv_model.getText().toString().trim());
                vinModel.setManufacturerName(makeModel.getMakeId());
                vinModel.setManufactureID(FijiRentalUtils.getManFacID(manList,makeModel.getMakeId()));

                ListingCarModel listingCarModel=((FijiCarRentalApplication)getApplication()).getCarModel();
                listingCarModel.setModel(vinModel);

                ((FijiCarRentalApplication)getApplication()).setListCarModel(listingCarModel);

                finish();
                break;
            case R.id.et_year:



                customDialog = new CustomDialog(this,yearList,this);

                customDialog.show(getSupportFragmentManager(),"YEAR" );

                break;
            case R.id.et_make:
                customDialog = new CustomDialog(this,makeList,this);

                customDialog.show(getSupportFragmentManager(),"MAKE" );
                break;
            case R.id.et_model:
                customDialog = new CustomDialog(this,modelList,this);

                customDialog.show(getSupportFragmentManager(),"MODEL" );
                break;
        }
    }

    @Override
    public void onItemClick(Object val) {
        if(customDialog.getTag().equals("YEAR")){
            tv_year.setText((CharSequence) val);
            updateView();
            getMakeList();
        }
        if(customDialog.getTag().equals("MAKE")){


            makeModel = (MakeModel)val;

            tv_make.setText(makeModel.getMakeDisplay());
            getModelList(makeModel);
            updateView();
        }

        if(customDialog.getTag().equals("MODEL")){

            CarModelType makeModel=(CarModelType) val;

            tv_model.setText(makeModel.getModelName());
            updateView();
        }

        customDialog.dismiss();


    }

    private void getModelList(MakeModel makeModel) {
        apiService = Config.getClient().create(APIService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String,String> data=new HashMap<>();
        data.put("make",makeModel.getMakeId());

        Call<ResponseBody> call = apiService.getModelList(FijiRentalUtils.getAccessToken(this),data);
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
                            JSONArray data_array = data.optJSONArray("data");
                            passdata(data_array,"MODEL");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, YourCarDetail.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, YourCarDetail.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    private void getMakeList() {
        apiService = Config.getClient().create(APIService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String,String> data=new HashMap<>();
        data.put("year",tv_year.getText().toString());

        Call<ResponseBody> call = apiService.getMakeList(FijiRentalUtils.getAccessToken(this),data);
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
                            JSONArray data_array = data.optJSONArray("data");
                            passdata(data_array, "MAKE");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, YourCarDetail.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, YourCarDetail.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    private void passdata(JSONArray data_array, String make) {

        if(make.equals("MAKE")) {

            makeList.clear();
            for (int i = 0; i < data_array.length(); i++) {
                JSONObject object = data_array.optJSONObject(i);
                MakeModel makeModel = new MakeModel();
                makeModel.setMakeId(object.optString("make_id"));
                makeModel.setMakeDisplay(object.optString("make_display"));
                makeModel.setMakeIsCommon(object.optString("make_is_common"));
                makeModel.setMakeCountry(object.optString("make_country"));


                makeList.add(makeModel);
            }
        }else {
            modelList.clear();
            for (int i = 0; i < data_array.length(); i++) {
                JSONObject object = data_array.optJSONObject(i);
                CarModelType carModelType = new CarModelType();
                carModelType.setModelName(object.optString("model_name"));
                carModelType.setModelMakeId(object.optString("model_make_id"));
                modelList.add(carModelType);
            }
        }
    }
}