package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnoozedActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    ProgressDialog progressDialog;
    CarModel carModel;
    TextView snoozedDate;
    APIService apiService;
    Date selectedDate=new Date();
    String unListReason = "";
    ImageView backBtn;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    TextView saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snoozed);
        backBtn = findViewById(R.id.iv_back);

        snoozedDate = findViewById(R.id.textDay);
        saveBtn = findViewById(R.id.save_btn);

        snoozedDate.setOnClickListener(this);
        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
            carModel = (CarModel) getIntent().getSerializableExtra(FijiRentalUtils.CarDetailIntent);

            if (getIntent().hasExtra("UNLIST_REASON")) {
                unListReason = getIntent().getStringExtra("UNLIST_REASON");
            }
        }

//        Toast.makeText(this, "is null "+(getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)), Toast.LENGTH_SHORT).show();

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.textDay) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            DatePickerDialog dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        } else if (v.getId() == R.id.save_btn) {
            if(TextUtils.isEmpty(snoozedDate.getText().toString())){
                Toast.makeText(this, "Select a date", Toast.LENGTH_SHORT).show();
                return;
            }
            updateStatus();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        selectedDate=c.getTime();

        String dayOfTheWeek = (String) DateFormat.format("E", selectedDate);
        String day          = (String) DateFormat.format("dd",   selectedDate);
        String monthString  = (String) DateFormat.format("MMM",  selectedDate);

        Toast.makeText(this, "parde "+sdf.format(selectedDate), Toast.LENGTH_SHORT).show();

        snoozedDate.setText(dayOfTheWeek+", "+day+" "+monthString);
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
        if(TextUtils.isEmpty(unListReason)){
            reqBuilder.addFormDataPart("enabled", "2");
            reqBuilder.addFormDataPart("snooze_untill", sdf.format(selectedDate));
        }else {
            reqBuilder.addFormDataPart("enabled", "0");
            reqBuilder.addFormDataPart("snooze_untill", sdf.format(selectedDate));
            reqBuilder.addFormDataPart("unlis_reason", unListReason);

        }



        body = reqBuilder
                .build();

        Call<ResponseBody> call = apiService.updateCarListing(FijiRentalUtils.getAccessToken(this), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);

                        if (jsonobject.optString("code").matches("200")) {
                            FijiRentalUtils.Logger("TAGS", "vin res " + jstr);
                            JSONObject dataObject = jsonobject.optJSONObject("data");
                            JSONObject object = dataObject.optJSONObject("data");
                            FijiRentalUtils.updateCarModel(object, SnoozedActivity.this);
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result","success");
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SnoozedActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SnoozedActivity.this, "0");
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