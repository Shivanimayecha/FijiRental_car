package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Adapter.CustomSpinnerAdapter;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.DBModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.HRDateUtil;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class Add_Unavailability extends AppCompatActivity implements View.OnClickListener {

    ImageView ivback;
    String[] time_val = new String[]{"midnight", "12:30 AM", "1:00 AM", "1:30 AM", "2:00 AM", "2:30 AM", "3:00 AM", "3:30 AM", "4:00 AM", "4:30 AM", "5:00 AM", "5:30 AM", "6:00 AM", "6:30 AM", "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "Noon 12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM", "9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM"};
    TextView saveBtn, spinnerStart, spinnerEnd, txtUntill, txtDay;
    String[] date;
    CheckBox isRepeat;
    String currentDate;
    APIService apiService;
    ProgressDialog progressDialog;
    CarModel carModel;
    LocalDate startDate, endDate, untilDate;
    private int DATE_GET_PRICE = 2005;
    private int SELECTE_DATE = 2006;
    LinearLayout isRepeatLayout;
    String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    AppCompatSpinner endSpinner, startSpinner;
    CustomSpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__unavailability);

        ivback = findViewById(R.id.iv_back);
        saveBtn = findViewById(R.id.save_btn);
        startSpinner = findViewById(R.id.spinner_start_time);
        endSpinner = findViewById(R.id.spinner_end_time);
        spinnerStart = findViewById(R.id.spinner_start);
        spinnerEnd = findViewById(R.id.spinner_end);
        txtUntill = findViewById(R.id.txtUntill);
        txtDay = findViewById(R.id.textDay);

        isRepeat = findViewById(R.id.isrepeat);
        isRepeatLayout = findViewById(R.id.repeat_lay);

        if (isRepeat.isChecked()) {
            isRepeatLayout.setVisibility(View.VISIBLE);
        } else {
            isRepeatLayout.setVisibility(View.GONE);
        }

        carModel = Paper.book().read(FijiRentalUtils.carModel);

        isRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    untilDate=endDate.plusMonths(3);
                    txtUntill.setText(untilDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + untilDate.getDayOfMonth() + " " + untilDate.monthOfYear().getAsShortText(Locale.getDefault()));
                    isRepeatLayout.setVisibility(View.VISIBLE);
                } else {
                    isRepeatLayout.setVisibility(View.GONE);
                }
            }
        });

        if (getIntent().hasExtra("CURRENT_DATE")) {
            currentDate = getIntent().getStringExtra("CURRENT_DATE");
            startDate = LocalDate.parse(currentDate);
            endDate = LocalDate.parse(currentDate);

            spinnerStart.setText(startDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + startDate.getDayOfMonth() + " " + startDate.monthOfYear().getAsShortText(Locale.getDefault()));
            spinnerEnd.setText(endDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + endDate.getDayOfMonth() + " " + endDate.monthOfYear().getAsShortText(Locale.getDefault()));
        }

        date = new String[1];
        date[0] = giveDate();

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_dropdown_item, date);
        dateAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        ivback.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        spinnerStart.setOnClickListener(this);
        spinnerEnd.setOnClickListener(this);
        txtUntill.setOnClickListener(this);
        txtDay.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_dropdown_item, time_val);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        startSpinner.setAdapter(adapter);
        endSpinner.setAdapter(adapter);
    }

    public String giveDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d");
        return sdf.format(cal.getTime());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.save_btn) {

            if (startDate.toString().equals(endDate.toString())) {
                FijiRentalUtils.ShowValidation("Start Date and End Date should be different", this, null);
            } else {
                addUnavilability();
            }

        } else if (v.getId() == R.id.spinner_start) {
            Intent getDate = new Intent(this, ActivityVerticalCalendar.class);
            getDate.putExtra("DATEGET", true);
            getDate.putExtra("DATEVALUE", currentDate);
            startActivityForResult(getDate, DATE_GET_PRICE);
        } else if (v.getId() == R.id.spinner_end) {
            Intent getDate = new Intent(this, ActivityVerticalCalendar.class);
            getDate.putExtra("DATEGET", true);
            getDate.putExtra("DATEVALUE", currentDate);
            startActivityForResult(getDate, DATE_GET_PRICE);
        } else if (v.getId() == R.id.txtUntill) {
            Intent getDate = new Intent(this, ActivityVerticalCalendar.class);
            getDate.putExtra("DATEVALUE", endDate.plusMonths(3).toString());
            startActivityForResult(getDate, SELECTE_DATE);
        } else if (v.getId() == R.id.textDay) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.checkbox_dialog, null);
            dialogBuilder.setView(dialogView);

            CheckBox sunBox, monBox, tueBox, wedBox, thuBox, friBox, satBox;

            sunBox = dialogView.findViewById(R.id.sunday);
            monBox = dialogView.findViewById(R.id.monday);
            tueBox = dialogView.findViewById(R.id.tuesday);
            wedBox = dialogView.findViewById(R.id.wednesday);
            thuBox = dialogView.findViewById(R.id.thursday);
            friBox = dialogView.findViewById(R.id.friday);
            satBox = dialogView.findViewById(R.id.saturday);

            String dayName = txtDay.getText().toString();

            if (dayName.contains("Sun")) {
                sunBox.setChecked(true);
            }
            if (dayName.contains("Mon")) {
                monBox.setChecked(true);
            }
            if (dayName.contains("Tue")) {
                tueBox.setChecked(true);
            }
            if (dayName.contains("Wed")) {
                wedBox.setChecked(true);
            }
            if (dayName.contains("Thu")) {
                thuBox.setChecked(true);
            }
            if (dayName.contains("Fri")) {
                friBox.setChecked(true);
            }
            if (dayName.contains("Sat")) {
                satBox.setChecked(true);
            }

            TextView okBtn = dialogView.findViewById(R.id.ok_btn);

            AlertDialog alertDialog = dialogBuilder.create();

            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedDay = "";

                    if (sunBox.isChecked()) {
                        selectedDay = selectedDay + "Sun ";
                    }

                    if (monBox.isChecked()) {
                        selectedDay = selectedDay + "Mon ";
                    }
                    if (tueBox.isChecked()) {
                        selectedDay = selectedDay + "Tue ";
                    }
                    if (wedBox.isChecked()) {
                        selectedDay = selectedDay + "Wed ";
                    }
                    if (thuBox.isChecked()) {
                        selectedDay = selectedDay + "Thu ";
                    }
                    if (friBox.isChecked()) {
                        selectedDay = selectedDay + "Fri ";
                    }
                    if (satBox.isChecked()) {
                        selectedDay = selectedDay + "Sat ";
                    }

                    txtDay.setText(selectedDay.toString());
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();

        }
    }

    private void addUnavilability() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiService = Config.getClient().create(APIService.class);
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("car_item_id", carModel.getItemId());
        reqBuilder.addFormDataPart("to_date_time", get24Hour(((String)startSpinner.getSelectedItem()),startDate.toString()));
        reqBuilder.addFormDataPart("from_date_time", get24Hour(((String)endSpinner.getSelectedItem()),endDate.toString()));


        if (isRepeat.isChecked()) {
            reqBuilder.addFormDataPart("repeat", "1");

            String dayName = txtDay.getText().toString();

            if (dayName.contains("Sun")) {
                reqBuilder.addFormDataPart("every_sunday", "1");
            } else {
                reqBuilder.addFormDataPart("every_sunday", "0");
            }
            if (dayName.contains("Mon")) {
                reqBuilder.addFormDataPart("every_mondy", "1");
            } else {
                reqBuilder.addFormDataPart("every_mondy", "0");
            }
            if (dayName.contains("Tue")) {
                reqBuilder.addFormDataPart("every_tuesday", "1");
            } else {
                reqBuilder.addFormDataPart("every_tuesday", "0");
            }
            if (dayName.contains("Wed")) {
                reqBuilder.addFormDataPart("every_wednesday", "1");
            } else {
                reqBuilder.addFormDataPart("every_wednesday", "0");
            }
            if (dayName.contains("Thu")) {
                reqBuilder.addFormDataPart("every_thursday", "1");
            } else {
                reqBuilder.addFormDataPart("every_thursday", "0");
            }
            if (dayName.contains("Fri")) {
                reqBuilder.addFormDataPart("every_friday", "1");
            } else {
                reqBuilder.addFormDataPart("every_friday", "0");
            }
            if (dayName.contains("Sat")) {
                reqBuilder.addFormDataPart("every_saturday", "1");
            } else {
                reqBuilder.addFormDataPart("every_saturday", "0");
            }

            reqBuilder.addFormDataPart("until_date_time", get24Hour(((String)endSpinner.getSelectedItem()),untilDate.toString()));
        } else {
            reqBuilder.addFormDataPart("repeat", "0");
            reqBuilder.addFormDataPart("every_sunday", "0");
            reqBuilder.addFormDataPart("every_mondy", "0");
            reqBuilder.addFormDataPart("every_tuesday", "0");
            reqBuilder.addFormDataPart("every_wednesday", "0");
            reqBuilder.addFormDataPart("every_thursday", "0");
            reqBuilder.addFormDataPart("every_friday", "0");
            reqBuilder.addFormDataPart("every_saturday", "0");
            reqBuilder.addFormDataPart("until_date_time", "0");

        }


        body = reqBuilder
                .build();
        FijiRentalUtils.Logger("TAGS", "data load " + reqBuilder + " body " + body.toString());

        Call<ResponseBody> call = apiService.addUnavailability(FijiRentalUtils.getAccessToken(this), body);
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
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, Add_Unavailability.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, Add_Unavailability.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());

                progressDialog.dismiss();
            }
        });
    }

    private View.OnTouchListener Spinner_OnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Toast.makeText(Add_Unavailability.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };
    private View.OnKeyListener Spinner_OnKey = new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                Toast.makeText(Add_Unavailability.this, "Cleick", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DATE_GET_PRICE) {
            if (resultCode == RESULT_OK) {

                currentDate = data.getStringExtra("DATEVALUE");

                if (currentDate.contains(HRDateUtil.DataSeprator)) {
                    String date1 = currentDate.split(HRDateUtil.DataSeprator)[0];
                    String date2 = currentDate.split(HRDateUtil.DataSeprator)[1];
                    startDate = LocalDate.parse(date1);
                    endDate = LocalDate.parse(date2);
                    spinnerStart.setText(startDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + startDate.getDayOfMonth() + " " + startDate.monthOfYear().getAsShortText(Locale.getDefault()));
                    spinnerEnd.setText(endDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + endDate.getDayOfMonth() + " " + endDate.monthOfYear().getAsShortText(Locale.getDefault()));
                } else {
                    startDate = LocalDate.parse(currentDate);
                    endDate = LocalDate.parse(currentDate);
                    spinnerStart.setText(startDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + startDate.getDayOfMonth() + " " + startDate.monthOfYear().getAsShortText(Locale.getDefault()));
                    spinnerEnd.setText(endDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + endDate.getDayOfMonth() + " " + endDate.monthOfYear().getAsShortText(Locale.getDefault()));
                }


            }
        } else if (requestCode == SELECTE_DATE) {
            if (resultCode == RESULT_OK) {

                String selectedDate = data.getStringExtra("SELECTEDDATE");

                if (selectedDate.contains(HRDateUtil.DataSeprator)) {
                    String date1 = selectedDate.split(HRDateUtil.DataSeprator)[0];
                    String date2 = selectedDate.split(HRDateUtil.DataSeprator)[1];
                    LocalDate localDate = LocalDate.parse(date1);
                    LocalDate localDate2 = LocalDate.parse(date2);
                    spinnerStart.setText(localDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate.getDayOfMonth() + " " + localDate.monthOfYear().getAsShortText(Locale.getDefault()));
                    spinnerEnd.setText(localDate2.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + localDate2.getDayOfMonth() + " " + localDate2.monthOfYear().getAsShortText(Locale.getDefault()));
                } else {
                    untilDate = LocalDate.parse(selectedDate);
                    txtUntill.setText(untilDate.dayOfWeek().getAsShortText(Locale.getDefault()) + ", " + untilDate.getDayOfMonth() + " " + untilDate.monthOfYear().getAsShortText(Locale.getDefault()));
                }


            }
        }
    }

    public String get24Hour(String vale,String date) {
        String value = "";
        if (vale.equalsIgnoreCase("midnight")) {
            vale = "12:00 AM";
        } if (vale.equalsIgnoreCase("Noon 12:30 PM")) {
            vale = "12:30 PM";
        }

        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        try {
            Date _24HourDt = _12HourSDF.parse(vale);
            value=_24HourSDF.format(_24HourDt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FijiRentalUtils.Logger("TAGS","value return "+(date+" "+value));

        return date+" "+value;
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
                            FijiRentalUtils.updateCarModel(object,Add_Unavailability.this);
                            finish();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, Add_Unavailability.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, Add_Unavailability.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }
}