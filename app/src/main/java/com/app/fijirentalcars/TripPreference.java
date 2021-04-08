package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripPreference extends AppCompatActivity implements View.OnClickListener {
    CheckBox checkBox1, checkBox2, checkBox3, weekTripCheck;
    String[] guestLocationsVal = new String[]{"1 hour", "2 hours", "3 hours", "6 hours", "12 hours", "1 day", "2 days"};
    String[] guestLocationsTagVal = new String[]{"ONE_HOUR", "TWO_HOURS", "THREE_HOURS", "SIX_HOURS", "TWELVE_HOURS", "ONE_DAY", "TWO_DAYS"};
    String[] shortestVal = new String[]{"Any", "1 day", "2 days", "3 days", "5 days"};
    String[] longestVal = new String[]{"5 days", "1 week", "2 weeks", "1 month", "3 months", "Any"};
    AppCompatSpinner spinner, spinnerDelivery, spinnerGuest, spinnerCar1, spinnerDelivery1, spinnerGuest1, spinnerShortest, spinnerLongest;
    ImageView ivBack;
    TextView saveBtn;

    ProgressDialog progressDialog;
    CarModel carModel;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_preference);

        checkBox1 = findViewById(R.id.check_1);
        checkBox2 = findViewById(R.id.check_2);
        checkBox3 = findViewById(R.id.check_3);
        weekTripCheck = findViewById(R.id.check_week_trip);
        ivBack = findViewById(R.id.iv_back);
        spinner = findViewById(R.id.spinner1);
        saveBtn = findViewById(R.id.save_btn);
        spinnerDelivery = findViewById(R.id.spinner_delivery);
        spinnerGuest = findViewById(R.id.spinner_guest);
//        spinnerCar1 = findViewById(R.id.spinner_car1);
//        spinnerDelivery1 = findViewById(R.id.spinner_delivery1);
//        spinnerGuest1 = findViewById(R.id.spinner_guest1);
        spinnerShortest = findViewById(R.id.spinner_shortest);
        spinnerLongest = findViewById(R.id.spinner_longest);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_dropdown_item, guestLocationsVal);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        ArrayAdapter<String> adapterShortest = new ArrayAdapter<>(this,
                R.layout.custom_spinner_dropdown_item, shortestVal);
        adapterShortest.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        ArrayAdapter<String> adapterlongest = new ArrayAdapter<>(this,
                R.layout.custom_spinner_dropdown_item, longestVal);
        adapterlongest.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        spinnerShortest.setAdapter(adapterShortest);
        spinnerLongest.setAdapter(adapterlongest);

        spinner.setAdapter(adapter);
        spinnerDelivery.setAdapter(adapter);
        spinnerGuest.setAdapter(adapter);
//        spinnerCar1.setAdapter(adapter);
//        spinnerDelivery1.setAdapter(adapter);
//        spinnerGuest1.setAdapter(adapter);

        checkBox1.setTypeface(FijiRentalUtils.getRegularTypeFace(this), Typeface.NORMAL);
        checkBox2.setTypeface(FijiRentalUtils.getRegularTypeFace(this), Typeface.NORMAL);
        checkBox3.setTypeface(FijiRentalUtils.getRegularTypeFace(this), Typeface.NORMAL);
        weekTripCheck.setTypeface(FijiRentalUtils.getRegularTypeFace(this), Typeface.NORMAL);

        ivBack.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carModel = Paper.book().read(FijiRentalUtils.carModel);
        if (carModel != null) {

            if (carModel.getBookInstatnlyCarLocation().equals("1")) {
                checkBox1.setChecked(true);
            } else {
                checkBox1.setChecked(false);
            }


            if (carModel.getGuestChoosenLocation().equalsIgnoreCase("1")) {
                if (carModel.getBookInstatnlyGuestsLocation().equals("1")) {
                    checkBox3.setChecked(true);
                } else {
                    checkBox3.setChecked(false);
                }
                checkBox3.setOnClickListener(null);
                spinnerGuest.setOnTouchListener(null);
                spinnerGuest.setSelection(getListValue(carModel.getAdvanceNoticeGuestsLocation()));
            } else {
                spinnerGuest.setSelection(0);
                spinnerGuest.setOnTouchListener(Spinner_OnTouch);
                checkBox3.setChecked(false);
                checkBox3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        checkBox3.setChecked(false);
                        showSnackBar();
                    }
                });
            }

            if(carModel.getAirportList().size()>0){
                if (carModel.getBookInstatnlyDiliveryLocations().equals("1")) {
                    checkBox2.setChecked(true);
                } else {
                    checkBox2.setChecked(false);
                }
                spinnerDelivery.setSelection(getListValue(carModel.getAdvanceNoticeDiliveryLocations()));
                checkBox2.setOnClickListener(null);
                spinnerDelivery.setOnTouchListener(null);
            }else {
                spinnerDelivery.setSelection(0);
                spinnerDelivery.setOnTouchListener(Spinner_OnTouch);
                checkBox2.setChecked(false);
                checkBox2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox2.setChecked(false);
                        showSnackBar();
                    }
                });

            }


            spinner.setSelection(getTagListValue(carModel.getAdvanceNoticeCarLocation()));


            spinnerLongest.setSelection(getLongestListValue(carModel.getLongestPossibleTrip()));
            spinnerShortest.setSelection(getShortestListValue(carModel.getShortestPossibleTrip()));

            if (carModel.getLongTermTrips().equals("1")) {
                weekTripCheck.setChecked(true);
            } else {
                weekTripCheck.setChecked(false);
            }
        }
    }

    private View.OnTouchListener Spinner_OnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showSnackBar();
            }
            return true;
        }
    };

    private void showSnackBar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.snack_view), "You don't currently offer delivery", Snackbar.LENGTH_LONG)
                .setAction("ADD", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(TripPreference.this, LocationDeliveryActivity.class);
                        startActivity(i);
                    }
                });

        Typeface typeface = Typeface.createFromAsset(getAssets(), "OpenSans-SemiBold.ttf");
        TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action);
        snackbarActionTextView.setTextSize(10);
        snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);
        snackbarActionTextView.setTextColor(ContextCompat.getColor(TripPreference.this,R.color.white));

        TextView snackbarTextView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarTextView.setTextSize(10);
        snackbarActionTextView.setTypeface(typeface, Typeface.NORMAL);
        snackbarTextView.setTypeface(typeface, Typeface.NORMAL);

        snackbar.show();
    }

    private int getLongestListValue(String value) {

        int val = 0;
        for (int i = 0; i < longestVal.length; i++) {
            if (longestVal[i].equalsIgnoreCase(value)) {
                val = i;
            }
        }

        return val;
    }

    private int getShortestListValue(String value) {

        int val = 0;
        for (int i = 0; i < shortestVal.length; i++) {
            if (shortestVal[i].equalsIgnoreCase(value)) {
                val = i;
            }
        }

        return val;
    }

    private int getListValue(String value) {

        int val = 0;
        for (int i = 0; i < guestLocationsVal.length; i++) {
            if (guestLocationsVal[i].equalsIgnoreCase(value)) {
                val = i;
            }
        }
        return val;
    }

    private int getTagListValue(String value) {
        int val = 0;
        for (int i = 0; i < guestLocationsTagVal.length; i++) {
            if (guestLocationsTagVal[i].equalsIgnoreCase(value)) {
                val = i;
            }
        }
        return val;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.save_btn) {
            updateStatus();
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

        if (checkBox1.isChecked()) {
            reqBuilder.addFormDataPart("book_instatnly_car_location", "1");
        } else {
            reqBuilder.addFormDataPart("book_instatnly_car_location", "0");
        }
        if (checkBox2.isChecked()) {
            reqBuilder.addFormDataPart("book_instatnly_dilivery_locations", "1");
        } else {
            reqBuilder.addFormDataPart("book_instatnly_dilivery_locations", "0");
        }
        if (checkBox3.isChecked()) {
            reqBuilder.addFormDataPart("book_instatnly_guests_location", "1");
        } else {
            reqBuilder.addFormDataPart("book_instatnly_guests_location", "0");
        }
        if (weekTripCheck.isChecked()) {
            reqBuilder.addFormDataPart("long_term_trips", "1");
        } else {
            reqBuilder.addFormDataPart("long_term_trips", "0");
        }

        reqBuilder.addFormDataPart("advance_notice_car_location", String.valueOf(guestLocationsTagVal[spinner.getSelectedItemPosition()]));
        reqBuilder.addFormDataPart("advance_notice_dilivery_locations", String.valueOf(spinnerDelivery.getSelectedItem()));
        reqBuilder.addFormDataPart("advance_notice_guests_location", String.valueOf(spinnerGuest.getSelectedItem()));
//        reqBuilder.addFormDataPart("trip_buffer_car_location", String.valueOf(spinnerCar1.getSelectedItem()));
//        reqBuilder.addFormDataPart("trip_buffer_dilivery_locations", String.valueOf(spinnerDelivery1.getSelectedItem()));
//        reqBuilder.addFormDataPart("trip_buffer_guests_location", String.valueOf(spinnerGuest1.getSelectedItem()));
        if (spinnerShortest.getSelectedItem().equals("Any")) {
            reqBuilder.addFormDataPart("shortest_possible_trip", "ANY");
        } else {
            reqBuilder.addFormDataPart("shortest_possible_trip", String.valueOf(spinnerShortest.getSelectedItem()));
        }
        if (spinnerLongest.getSelectedItem().equals("Any")) {
            reqBuilder.addFormDataPart("longest_possible_trip", "ANY");
        } else {
            reqBuilder.addFormDataPart("longest_possible_trip", String.valueOf(spinnerLongest.getSelectedItem()));
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
                            FijiRentalUtils.updateCarModel(object, TripPreference.this);
//                            FijiRentalUtils.isUpdateCarModel = true;
                            finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, TripPreference.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, TripPreference.this, "0");
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