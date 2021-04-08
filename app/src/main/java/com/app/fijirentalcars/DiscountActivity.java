package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class DiscountActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgBack;
    TextView saveBtn;
    EditText discount3, discount7, discount30;
    CarModel carModel;
    APIService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        imgBack = findViewById(R.id.iv_back);
        saveBtn = findViewById(R.id.tv_continue);
        discount3 = findViewById(R.id.discount3day);
        discount7 = findViewById(R.id.discount7day);
        discount30 = findViewById(R.id.discount30day);

        discount3.addTextChangedListener(textWatcher);
        discount7.addTextChangedListener(textWatcher);
        discount30.addTextChangedListener(textWatcher);

//        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
//            carModel = (CarModel) getIntent().getSerializableExtra(FijiRentalUtils.CarDetailIntent);
//
//            if (carModel.get3extraDayDiscount() != null) {
//                discount3.setText(String.valueOf((int) Double.parseDouble(carModel.get3extraDayDiscount())));
//            }
//            if (carModel.get7extraDayDiscount() != null) {
//                discount7.setText(String.valueOf((int) Double.parseDouble(carModel.get7extraDayDiscount())));
//            }
//            if (carModel.get30extraDayDiscount() != null) {
//                discount30.setText(String.valueOf((int) Double.parseDouble(carModel.get30extraDayDiscount())));
//            }
//        }


        imgBack.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        carModel = Paper.book().read(FijiRentalUtils.carModel);

        if (carModel.get3extraDayDiscount() != null) {
            discount3.setText(String.valueOf((int) Double.parseDouble(carModel.get3extraDayDiscount())));
        }
        if (carModel.get7extraDayDiscount() != null) {
            discount7.setText(String.valueOf((int) Double.parseDouble(carModel.get7extraDayDiscount())));
        }
        if (carModel.get30extraDayDiscount() != null) {
            discount30.setText(String.valueOf((int) Double.parseDouble(carModel.get30extraDayDiscount())));
        }

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkVlaue();
        }
    };

    private boolean checkVlaue() {
        boolean val = false;
        int day3Discount = 0, day7Discount = 0, day30Discount = 0;
        if (!TextUtils.isEmpty(discount3.getText().toString())) {
            day3Discount = Integer.parseInt(discount3.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(discount7.getText().toString())) {
            day7Discount = Integer.parseInt(discount7.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(discount30.getText().toString())) {
            day30Discount = Integer.parseInt(discount30.getText().toString().trim());
        }

        FijiRentalUtils.Logger("TAGS", "discoutn 7d " + day7Discount + " 3d " + day3Discount + " 30 " + day30Discount);

        if (day7Discount > 90) {
            discount7.setError("7+ day discount can't be greater than 90%.");
            val = true;
        } else {
            if (day7Discount > day30Discount) {
                discount7.setError("7+ day discount can't be greater than 30+ day discount.");
                val = true;
            }
            if (day7Discount < day3Discount) {
                discount7.setError("7+ day discount must be greater than or equal to 3+ day discount.");
                val = true;
            }
        }

        if (!val) {
            discount7.setError(null);
        }

        if (day3Discount > 90) {
            discount3.setError("3+ day discount can't be greater than 90%.");
            val = true;
        } else {
            if (day3Discount > day7Discount) {
                discount3.setError("3+ day discount can't be greater than 7+ day discount.");
                val = true;
            }
        }

        if (!val) {
            discount3.setError(null);
        }
        if (day30Discount > 90) {
            discount30.setError("30+ day discount can't be greater than 90%.");
        } else {
            if (day30Discount < day7Discount) {
                discount30.setError("30+ day discount must be greater than or equal to 7+ day discount.");
                val = true;
            }
        }

        if (!val) {
            discount30.setError(null);
        }


        return val;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.tv_continue) {

            if (checkVlaue()) {
                Toast.makeText(this, "Fix errors", Toast.LENGTH_SHORT).show();
            } else {

                if (carModel != null) {
                    updateStatus();
                }

            }

        }
    }

    private void updateStatus() {

        apiService = Config.getClient().create(APIService.class);

        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        reqBuilder.addFormDataPart("3extra_day_discount", discount3.getText().toString());
        reqBuilder.addFormDataPart("7extra_day_discount", discount7.getText().toString());
        reqBuilder.addFormDataPart("30extra_day_discount", discount30.getText().toString());


        body = reqBuilder
                .build();

        Call<ResponseBody> call = apiService.updateCarListing(FijiRentalUtils.getAccessToken(this), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);

                        if (jsonobject.optString("code").matches("200")) {
                            FijiRentalUtils.Logger("TAGS", "vin res " + jstr);
                            JSONObject dataObject = jsonobject.optJSONObject("data");
                            JSONObject object = dataObject.optJSONObject("data");
                           FijiRentalUtils.updateCarModel(object, DiscountActivity.this);

                            finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DiscountActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DiscountActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());

            }
        });
    }
}