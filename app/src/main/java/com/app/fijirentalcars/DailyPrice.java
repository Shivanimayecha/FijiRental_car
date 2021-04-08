package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.DBModel;
import com.app.fijirentalcars.SQLDB.HRDatabase;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.HRDateUtil;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyPrice extends AppCompatActivity implements View.OnClickListener {

    ImageView ivback;
    CarModel carModel;
    String currentDate;
    EditText dailyPrice;
    private HRDatabase HRDatabase;
    TextView dateView, saveBtn;
    TextView selectDate;
    APIService apiService;
    ProgressDialog progressDialog;
    private int DATE_GET_PRICE = 2005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_price);
        ivback = findViewById(R.id.iv_back);
        dailyPrice = findViewById(R.id.price_daily);
        dateView = findViewById(R.id.date_view);
        saveBtn = findViewById(R.id.tv_continue);
        selectDate = findViewById(R.id.select_date);

        ivback.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        selectDate.setOnClickListener(this);
        HRDatabase = new HRDatabase(this);
        dailyPrice.setText("US$ 50");
        dailyPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("US$ ")) {
                    dailyPrice.setText("US$ ");
                    Selection.setSelection(dailyPrice.getText(), dailyPrice.getText().length());

                }

            }
        });


        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
            carModel = (CarModel) getIntent().getSerializableExtra(FijiRentalUtils.CarDetailIntent);

            currentDate = getIntent().getStringExtra("CURRENT_DATE");

            LocalDate localDate = LocalDate.parse(currentDate);
            dateView.setText(localDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate.getDayOfMonth() + " " + localDate.monthOfYear().getAsShortText(Locale.getDefault()));

            DBModel dbModel = HRDatabase.getSingleDate(currentDate);

            if (dbModel.getTitle() != null) {
                dailyPrice.setText("US$ " + dbModel.getTitle());
            } else {
                dailyPrice.setText("US$ " + HRDateUtil.DailyPrice);
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.tv_continue) {
            FijiRentalUtils.hideKeyboard(this);
            if (dailyPrice.getText().toString() != null && dailyPrice.getText().toString().length() > 0 && !dailyPrice.getText().toString().equals("") && !dailyPrice.getText().toString().equals("null") && !dailyPrice.getText().toString().equals("US$ ")) {

                if (currentDate.contains(HRDateUtil.DataSeprator)) {
                    String date1 = currentDate.split(HRDateUtil.DataSeprator)[0];
                    String date2 = currentDate.split(HRDateUtil.DataSeprator)[1];

                    LocalDate localDate = LocalDate.parse(date1);
                    LocalDate localDate2 = LocalDate.parse(date2);

                    for (LocalDate currentdate = localDate;
                         currentdate.isBefore(localDate2) || currentdate.isEqual(localDate2);
                         currentdate = currentdate.plusDays(1)) {
                        DBModel model = new DBModel(dailyPrice.getText().toString().replace("US$ ", ""), currentdate.toString());
                        if (!HRDateUtil.isUnavailableDate(currentdate)) {
                            HRDatabase.insertTitle(model);
                        }

                    }

                    updateStatus();

                } else {

                    DBModel model = new DBModel(dailyPrice.getText().toString().replace("US$ ", ""), currentDate);

                    if (!HRDateUtil.isUnavailableDate(LocalDate.parse(currentDate))) {
                        HRDatabase.insertTitle(model);
                    }
                    updateStatus();
                }

            } else {
                dailyPrice.setError("Enter Event");
            }
        } else if (v.getId() == R.id.select_date) {

            Intent getDate = new Intent(this, ActivityVerticalCalendar.class);
            getDate.putExtra("DATEGET", true);
            getDate.putExtra("DATEVALUE", currentDate);
            startActivityForResult(getDate, DATE_GET_PRICE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DATE_GET_PRICE) {
            if (resultCode == RESULT_OK) {

                currentDate = data.getStringExtra("DATEVALUE");

                if (currentDate.contains(HRDateUtil.DataSeprator)) {
                    String date1 = currentDate.split(HRDateUtil.DataSeprator)[0];
                    String date2 = currentDate.split(HRDateUtil.DataSeprator)[1];

                    LocalDate localDate = LocalDate.parse(date1);
                    LocalDate localDate2 = LocalDate.parse(date2);

                    FijiRentalUtils.Logger("TAGS", "dateq  " + date1 + " date2 " + date2);

                    if (localDate.monthOfYear().getAsShortText(Locale.getDefault()).toString().equalsIgnoreCase(localDate2.monthOfYear().getAsShortText(Locale.getDefault()).toString())) {
                        dateView.setText(localDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate.getDayOfMonth() + "-" + localDate2.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate2.getDayOfMonth() + " " + localDate.monthOfYear().getAsShortText(Locale.getDefault()));
                    } else {
                        dateView.setText(localDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate.getDayOfMonth() + " " + localDate.monthOfYear().getAsShortText(Locale.getDefault()) + "-" + localDate2.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate2.getDayOfMonth() + " " + localDate2.monthOfYear().getAsShortText(Locale.getDefault()));
                    }

                    DBModel dbModel = HRDatabase.getSingleDate(localDate.toString());
                    if (dbModel.getTitle() != null) {
                        dailyPrice.setText("US$ " + dbModel.getTitle());
                    } else {
                        dailyPrice.setText("US$ " + HRDateUtil.DailyPrice);
                    }

                } else {
                    LocalDate localDate = LocalDate.parse(currentDate);
                    dateView.setText(localDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate.getDayOfMonth() + " " + localDate.monthOfYear().getAsShortText(Locale.getDefault()));
                    DBModel dbModel = HRDatabase.getSingleDate(currentDate);
                    if (dbModel.getTitle() != null) {
                        dailyPrice.setText("US$ " + dbModel.getTitle());
                    } else {
                        dailyPrice.setText("US$ " + HRDateUtil.DailyPrice);
                    }
                }


            }
        }
    }


    private void updateStatus() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiService = Config.getClient().create(APIService.class);
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        reqBuilder.addFormDataPart("carprice", String.valueOf(HRDateUtil.DailyPrice));

        List<DBModel> model = new ArrayList<>();
        model.addAll(HRDatabase.getAllDate());

        FijiRentalUtils.Logger("TAGS","dara "+model.size());
        for(int i=0;i<model.size();i++){
            DBModel dbModel=model.get(i);
            reqBuilder.addFormDataPart("custom_price["+i+"][date]", dbModel.getDate());
            reqBuilder.addFormDataPart("custom_price["+i+"][price]", dbModel.getTitle());

        }



        body = reqBuilder
                .build();

        Call<ResponseBody> call = apiService.setCarPrice(FijiRentalUtils.getAccessToken(this), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


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
                        progressDialog.dismiss();
                        e.printStackTrace();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DailyPrice.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DailyPrice.this, "0");
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
                            FijiRentalUtils.updateCarModel(object,DailyPrice.this);
                            finish();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DailyPrice.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DailyPrice.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }
}