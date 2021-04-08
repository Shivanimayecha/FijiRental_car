package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingStatus extends AppCompatActivity implements View.OnClickListener {
    RadioGroup statusGroup;
    ImageView backBtn;
    ProgressDialog progressDialog;
    TextView deleteListing;
    CarModel carModel;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_status);
        statusGroup = findViewById(R.id.status_group);
        backBtn=findViewById(R.id.iv_back);
        deleteListing=findViewById(R.id.delete_listing);

        backBtn.setOnClickListener(this);
        deleteListing.setOnClickListener(this);

        carModel= Paper.book().read(FijiRentalUtils.carModel);


        statusGroup.check(R.id.listed);

        if(carModel!=null){
            if(carModel.getEnabled().equals("0")){
                statusGroup.check(R.id.unlisted);
            }else if(carModel.getEnabled().equals("1")){
                statusGroup.check(R.id.listed);
            }else {
                statusGroup.check(R.id.snoozed);
            }
        }
        statusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton noticeRadio = (RadioButton) findViewById(checkedId);
                if (noticeRadio.getTag().equals("SNOOZED")) {
                    Intent i = new Intent(ListingStatus.this, SnoozedActivity.class);
                    i.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
                    startActivityForResult(i,FijiRentalUtils.UPDATE_LISTING_STATUS);
                    return;
                }
                if (noticeRadio.getTag().equals("UNLISTED")) {
                    Intent i = new Intent(ListingStatus.this, UnlistReason.class);
                    i.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
                    startActivityForResult(i,FijiRentalUtils.UPDATE_LISTING_STATUS);
                    return;
                }

                if(noticeRadio.getTag().equals("LISTED")){
                    if(!carModel.getEnabled().equals("1")){
                        updateStatus();
                    }
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FijiRentalUtils.UPDATE_LISTING_STATUS){
            if(resultCode==RESULT_OK){
                String result=data.getStringExtra("result");

                if(result.equals("success")){
                    finish();
                }
            }
        }

    }

    private void updateStatus() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        reqBuilder.addFormDataPart("enabled", "1");


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

                            JSONObject dataObject = jsonobject.optJSONObject("data");
                            JSONObject object = dataObject.optJSONObject("data");
                            FijiRentalUtils.updateCarModel(object, ListingStatus.this);
                            finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ListingStatus.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ListingStatus.this, "0");
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
    public void onClick(View v) {
        if(v.getId()==R.id.iv_back){
            finish();
        }else if(v.getId()==R.id.delete_listing){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.delete_listing_dialog, null);
            dialogBuilder.setView(dialogView);

            TextView yesBtn,noBtn;

            yesBtn =  dialogView.findViewById(R.id.yes_btn);
            noBtn =  dialogView.findViewById(R.id.no_btn);


            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    deleteCar();

                }
            });
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });


        }
    }

    private void deleteCar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_page_id", carModel.getItemPageId());



        body = reqBuilder
                .build();

        Call<ResponseBody> call = apiService.deleteCarListing(FijiRentalUtils.getAccessToken(this), body);
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

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result","delete");
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ListingStatus.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ListingStatus.this, "0");
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