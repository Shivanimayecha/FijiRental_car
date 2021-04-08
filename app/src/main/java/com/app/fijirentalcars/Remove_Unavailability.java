package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.Unavailability;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.HRDateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Remove_Unavailability extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back;
    TextView startDate,endDate,removeUnavail;
    Unavailability unavailability;
    APIService apiService;
    CarModel carModel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove__unavailability);

        iv_back=findViewById(R.id.iv_back);
        startDate=findViewById(R.id.start_date);
        endDate=findViewById(R.id.end_date);
        removeUnavail=findViewById(R.id.remove_unavail);

        iv_back.setOnClickListener(this);
        removeUnavail.setOnClickListener(this);

        carModel = Paper.book().read(FijiRentalUtils.carModel);

        List<Unavailability> unavailabilities = carModel.getUnavailability();


        unavailability = unavailabilities.get(HRDateUtil.UnavailPos);


        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

        try {
            Date t = ft.parse(unavailability.getToDateTime());
            Date t2 = ft.parse(unavailability.getFromDateTime());
            ft.applyPattern("dd MMM, hh:mm a");
            startDate.setText(ft.format(t));
            endDate.setText(ft.format(t2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_back){
            finish();
        }else if(v.getId()==R.id.remove_unavail){
            removeUnavailability();
        }
    }

    private void removeUnavailability() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiService = Config.getClient().create(APIService.class);
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("unavailability_id", unavailability.getId());



        body = reqBuilder
                .build();
        FijiRentalUtils.Logger("TAGS", "data load " + reqBuilder + " body " + body.toString());

        Call<ResponseBody> call = apiService.removeUnavailability(FijiRentalUtils.getAccessToken(this), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        FijiRentalUtils.isDateUpdate = true;
                        if (jsonobject.optString("code").matches("200")) {
                            updateCarModel();


                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, Remove_Unavailability.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, Remove_Unavailability.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());

                progressDialog.dismiss();
            }
        });
    }

    public void updateCarModel(){
//        apiService = Config.getClient().create(APIService.class);
        HashMap<String, String> data = new HashMap<>();
        data.put("item_id", carModel.getItemId());
        Call<ResponseBody> call = apiService.getCarDetails(FijiRentalUtils.getAccessToken(this), data);

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
                            JSONObject object = data.optJSONObject("data");
                            FijiRentalUtils.updateCarModel(object,Remove_Unavailability.this);
                            finish();
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, Remove_Unavailability.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, Remove_Unavailability.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }
}