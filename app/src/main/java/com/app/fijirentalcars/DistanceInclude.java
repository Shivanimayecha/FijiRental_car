package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class DistanceInclude extends AppCompatActivity implements View.OnClickListener {

    LinearLayout checkOn, checkOff;
    ImageView backBtn;
    TextView tv_save,learnMore;
    TextView checkStat;
    CheckBox checkBox;
    AutoCompleteTextView dailyDistance;
    ProgressDialog progressDialog;
    CarModel carModel;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_include);

        checkOn = findViewById(R.id.check_on);
        checkOff = findViewById(R.id.check_off);
        tv_save = findViewById(R.id.tv_next);
        checkStat = findViewById(R.id.check_stat);
        dailyDistance = findViewById(R.id.filled_exposed_dropdown);
        checkBox = findViewById(R.id.check_btn);
        backBtn = findViewById(R.id.iv_back);
        learnMore = findViewById(R.id.learn_more);

        String[] type = new String[]{"200", "250", "300", "350", "400", "450", "500"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        type);

        AutoCompleteTextView editTextFilledExposedDropdown =
                findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

        dailyDistance.setText(type[0],false);

//        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
//            carModel = (CarModel) getIntent().getSerializableExtra(FijiRentalUtils.CarDetailIntent);
//
//            if(carModel.getModelUnlimitedIncluded().equals("1")){
//                checkBox.setChecked(true);
//            }else {
//                checkBox.setChecked(false);
//                dailyDistance.setText(carModel.getModelDailyDistanceIncluded(),false);
//            }
//
//            FijiRentalUtils.Logger("TAGS","data dis "+carModel.getModelDailyDistanceIncluded());
//        }

        if (checkBox.isChecked()) {
            checkStat.setText("On");
            checkOn.setVisibility(View.VISIBLE);
            checkOff.setVisibility(View.GONE);
        } else {
            checkStat.setText("Off");
            checkOn.setVisibility(View.GONE);
            checkOff.setVisibility(View.VISIBLE);
        }


        backBtn.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        learnMore.setOnClickListener(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    checkStat.setText("On");
                    checkOn.setVisibility(View.VISIBLE);
                    checkOff.setVisibility(View.GONE);
                } else {
                    checkStat.setText("Off");
                    checkOn.setVisibility(View.GONE);
                    checkOff.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carModel= Paper.book().read(FijiRentalUtils.carModel);
        if(carModel.getModelUnlimitedIncluded().equals("1")){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
            dailyDistance.setText(carModel.getModelDailyDistanceIncluded(),false);
        }

        FijiRentalUtils.Logger("TAGS","data dis "+carModel.getModelDailyDistanceIncluded());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.tv_next) {

            if(!checkBox.isChecked()) {
                if (TextUtils.isEmpty(dailyDistance.getText().toString())) {
                    FijiRentalUtils.ShowValidation("Select value", this, "");
                    return;
                }
            }

            updateCar();

        }else if (v.getId() == R.id.learn_more) {
            String url = "https://fijirentalcars.siddhidevelopment.com/help/";


            CustomTabsIntent tabsIntent1 = new CustomTabsIntent.Builder().build();
            tabsIntent1.intent.setPackage("com.android.chrome");
            tabsIntent1.launchUrl(this, Uri.parse(url));
        }
    }

    private void updateCar() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());


        if (checkBox.isChecked()) {
            reqBuilder.addFormDataPart("model_unlimited_included", "1");

        } else {
            reqBuilder.addFormDataPart("model_unlimited_included", "0");
            reqBuilder.addFormDataPart("model_daily_distance_included", dailyDistance.getText().toString());
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
                            FijiRentalUtils.updateCarModel(object, DistanceInclude.this);
                            finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DistanceInclude.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DistanceInclude.this, "0");
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