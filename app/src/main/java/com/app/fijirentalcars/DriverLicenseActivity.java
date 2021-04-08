package com.app.fijirentalcars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.CountryModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.Model.StateModel;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverLicenseActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner {

    TextView tv_continue;
    EditText userFirstName, userMiddelName, userLastName, licenceNumber, statename, cityName, streetName;
    TextView countryName, birthDate;
    private boolean isCarRegister = false;
    List<Object> countryList = new ArrayList<>();
    LinearLayout driverImage;
    List<Object> stateList = new ArrayList<>();
    CustomDialog customDialog;
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    int STORAGE_CODE = 1002;
    File file;
    private static final int REQUEST_CODE_CAMERA = 1003;
    Uri fileUri;
    ImageView ivBack;
    APIService apiService;
    CountryModel countryModel;
    ProgressDialog progressDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;


    boolean isBusinessLicence = false;
    boolean isDrivingLicence = false;
    LinearLayout ownerLicenseLayout, businessLicenseLayout, transportLicenseLayout;
    private static final int REQUEST_CODE_GALLERY = 1004;
    LinearLayout selectedBusinessLicnse, selectedTransportLicnse;
    AppCompatImageView businessLicenseImage, closeBusinessLicenseImage, transportLicenseImage, closeTransportLicenseImage;
    File businessLicenseFile, transportLicenseFile, driverLicense, profileFile;
    String userType = "";
    TextView businessLicenseName, transportLicenseName, businessLicense, transportLicense;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_license);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        Log.e("DriverLicenseActivity", "DriverLicenseActivity");

        apiService = Config.getClient().create(APIService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        tv_continue = findViewById(R.id.tv_continue);
        userFirstName = findViewById(R.id.et_fname);
        userMiddelName = findViewById(R.id.et_midlename);
        userLastName = findViewById(R.id.et_lname);
        ivBack = findViewById(R.id.iv_back);
        licenceNumber = findViewById(R.id.et_licensenum);
        countryName = findViewById(R.id.et_countryname);
        statename = findViewById(R.id.et_regionname);
        birthDate = findViewById(R.id.et_date);
        driverImage = findViewById(R.id.driver_image);
        cityName = findViewById(R.id.et_cityname);
        streetName = findViewById(R.id.et_streetname);

        ownerLicenseLayout = findViewById(R.id.rental_license);
        businessLicenseLayout = findViewById(R.id.business_license_lay);
        transportLicenseLayout = findViewById(R.id.transport_license_lay);
        businessLicense = findViewById(R.id.business_license);
        transportLicense = findViewById(R.id.transport_license);

        selectedBusinessLicnse = findViewById(R.id.selected_business_license);
        businessLicenseImage = findViewById(R.id.businessLicenscImage);
        closeBusinessLicenseImage = findViewById(R.id.close_business_lincse);
        businessLicenseName = findViewById(R.id.business_license_name);

        selectedTransportLicnse = findViewById(R.id.selected_transport_license);
        transportLicenseImage = findViewById(R.id.transportLicenscImage);
        closeTransportLicenseImage = findViewById(R.id.close_transport_license);
        transportLicenseName = findViewById(R.id.transport_license_name);

        tv_continue.setOnClickListener(this);
        countryName.setOnClickListener(this);
//        statename.setOnClickListener(this);
        birthDate.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        driverImage.setOnClickListener(this);

        userFirstName.addTextChangedListener(textWatcher);
        userMiddelName.addTextChangedListener(textWatcher);
        userLastName.addTextChangedListener(textWatcher);
        licenceNumber.addTextChangedListener(textWatcher);

        if (getIntent().hasExtra(FijiRentalUtils.CAR_FLAW)) {
            isCarRegister = getIntent().getBooleanExtra(FijiRentalUtils.CAR_FLAW, false);
        }

        if (getIntent().hasExtra("USERTYPE")) {
            userType = getIntent().getStringExtra("USERTYPE");

            if (userType.equalsIgnoreCase(FijiRentalUtils.Consumer)) {
                driverImage.setVisibility(View.VISIBLE);
                userFirstName.setVisibility(View.GONE);
                userLastName.setVisibility(View.GONE);
                userMiddelName.setVisibility(View.GONE);
                licenceNumber.setVisibility(View.VISIBLE);
                ownerLicenseLayout.setVisibility(View.GONE);
            } else {

                driverImage.setVisibility(View.GONE);
                userFirstName.setVisibility(View.GONE);
                userLastName.setVisibility(View.GONE);
                userMiddelName.setVisibility(View.GONE);
                licenceNumber.setVisibility(View.GONE);
                ownerLicenseLayout.setVisibility(View.VISIBLE);
            }
        }

        updateView();
        getCountryList();

        closeTransportLicenseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transportLicenseFile = null;
                updateView();
            }
        });

        closeBusinessLicenseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                businessLicenseFile = null;
                updateView();
            }
        });

        businessLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBusinessLicence = true;
                isDrivingLicence = false;
                if (checkForPermission(permissions, STORAGE_CODE)) {
                    ArrayList type = new ArrayList();
                    type.add("From Gallery");
                    type.add("From Camera");
                    customDialog = new CustomDialog(DriverLicenseActivity.this, type, DriverLicenseActivity.this);
                    customDialog.show(getSupportFragmentManager(), "PICK_UP");
                }
            }
        });

        transportLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBusinessLicence = false;
                isDrivingLicence = false;
                if (checkForPermission(permissions, STORAGE_CODE)) {
                    ArrayList type = new ArrayList();
                    type.add("From Gallery");
                    type.add("From Camera");
                    customDialog = new CustomDialog(DriverLicenseActivity.this, type, DriverLicenseActivity.this);
                    customDialog.show(getSupportFragmentManager(), "PICK_UP");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCarRegister) {
            ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

            if (!TextUtils.isEmpty(carModel.getLicenceFirstname())) {
                userFirstName.getText().toString();
            }
            if (!TextUtils.isEmpty(carModel.getLicenceMiddlename())) {
                userMiddelName.getText().toString();
            }
            if (!TextUtils.isEmpty(carModel.getLicenceLastname())) {
                userLastName.getText().toString();
            }
            if (!TextUtils.isEmpty(carModel.getLicenceNumber())) {
                licenceNumber.getText().toString();
            }
            if (!TextUtils.isEmpty(carModel.getLicenceCountryname())) {
                countryName.getText().toString();
            }
            if (!TextUtils.isEmpty(carModel.getLicenceDOb())) {
                birthDate.getText().toString();
            }
            if (!TextUtils.isEmpty(carModel.getLicenceStatename())) {
                statename.getText().toString();
            }
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_continue:
                if (isCarRegister) {
//                    ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
//                    carModel.setLicenceFirstname(userFirstName.getText().toString());
//                    carModel.setLicenceMiddlename(userMiddelName.getText().toString());
//                    carModel.setLicenceLastname(userLastName.getText().toString());
//                    carModel.setLicenceCountryname(countryModel.getCode());

                    RequestBody body;
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM);


                    builder.addFormDataPart("user_id", FijiRentalUtils.getId(DriverLicenseActivity.this));
                    builder.addFormDataPart("is_edit", "true");


                    ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

                    profileFile = new File(carModel.getUserProfile());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), profileFile);
                    builder.addFormDataPart("user_pic_url", profileFile.getName(), requestFile);

                    if (userType.equalsIgnoreCase(FijiRentalUtils.Consumer)) {
                        if (driverLicense == null) {
                            Toast.makeText(this, "Capture driver licence image", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        RequestBody requestFile2 = RequestBody.create(MediaType.parse("image/*"), driverLicense);
                        builder.addFormDataPart("license_image", driverLicense.getName(), requestFile2);
                        builder.addFormDataPart("country", countryModel.getCode());
                        builder.addFormDataPart("city", cityName.getText().toString());
                        builder.addFormDataPart("state", statename.getText().toString());
                        builder.addFormDataPart("dob", FijiRentalUtils.parseDate(birthDate.getText().toString()));
                        builder.addFormDataPart("address", streetName.getText().toString());
                        builder.addFormDataPart("license_number", licenceNumber.getText().toString());
                        builder.addFormDataPart("driver_image", profileFile.getName(), requestFile);


                    } else {
                        if (businessLicenseFile == null) {
                            Toast.makeText(this, "Select business licence image", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (transportLicenseFile == null) {
                            Toast.makeText(this, "Select transport licence image", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        builder.addFormDataPart("country", countryModel.getCode());
                        builder.addFormDataPart("city", cityName.getText().toString());
                        builder.addFormDataPart("state", statename.getText().toString());
                        builder.addFormDataPart("dob", FijiRentalUtils.parseDate(birthDate.getText().toString()));
                        builder.addFormDataPart("address", streetName.getText().toString());

                        RequestBody requestBusinessFile = RequestBody.create(MediaType.parse("image/*"), businessLicenseFile);
                        builder.addFormDataPart("business_license", businessLicenseFile.getName(), requestBusinessFile);

                        RequestBody requestTransportFile = RequestBody.create(MediaType.parse("image/*"), transportLicenseFile);
                        builder.addFormDataPart("transport_license", transportLicenseFile.getName(), requestTransportFile);
                    }

                    body = builder
                            .build();

                    updateprofile(body);
//
//                    if (TextUtils.isEmpty(carModel.getDriverLicenseImage())) {
//                        Toast.makeText(this, "Capture driver licence image", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//
//                    if (statename.getVisibility() == View.VISIBLE) {
//                        carModel.setLicenceStatename(statename.getText().toString());
//                    }
//                    carModel.setLicenceNumber(licenceNumber.getText().toString());
//                    carModel.setLicenceDOb(birthDate.getText().toString());
//                    ((FijiCarRentalApplication) getApplication()).setListCarModel(carModel);
//
//                    ListCar();
                } else {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                break;

            case R.id.et_countryname:
                customDialog = new CustomDialog(this, countryList, this);

                customDialog.show(getSupportFragmentManager(), "Country");
                break;
            case R.id.et_regionname:
                customDialog = new CustomDialog(this, stateList, this);

                customDialog.show(getSupportFragmentManager(), "State");
                break;
            case R.id.et_date:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (!TextUtils.isEmpty(birthDate.getText().toString())) {
                    String[] date = birthDate.getText().toString().split("/");
                    mDay = Integer.parseInt(date[2]);
                    mMonth = Integer.parseInt(date[1]);
                    mYear = Integer.parseInt(date[0]);
                }


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String month, day;

                                if (dayOfMonth <= 9) {
                                    day = "0" + dayOfMonth;
                                } else {
                                    day = String.valueOf(dayOfMonth);
                                }

                                if (monthOfYear < 9) {
                                    month = "0" + (monthOfYear + 1);
                                } else {
                                    month = String.valueOf((monthOfYear + 1));
                                }

                                birthDate.setText(year + "/" + month + "/" + day);
                                updateView();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

            case R.id.iv_back:
                finish();
                break;

            case R.id.driver_image:
                isDrivingLicence = true;
                if (checkForPermission(permissions, STORAGE_CODE)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file = new File(getExternalCacheDir(),
                            String.valueOf(System.currentTimeMillis()) + ".jpg");
                    fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }
                break;
        }
    }

    private void updateView() {
        tv_continue.setEnabled(false);

        if (businessLicenseFile != null) {
            Glide.with(DriverLicenseActivity.this).load(businessLicenseFile).centerCrop().into(businessLicenseImage);
            businessLicenseName.setText(businessLicenseFile.getName());
            selectedBusinessLicnse.setVisibility(View.VISIBLE);
            businessLicense.setVisibility(View.GONE);

        } else {
            selectedBusinessLicnse.setVisibility(View.GONE);
            businessLicense.setVisibility(View.VISIBLE);
        }
        if (transportLicenseFile != null) {
            Glide.with(DriverLicenseActivity.this).load(transportLicenseFile).centerCrop().into(transportLicenseImage);
            transportLicenseName.setText(transportLicenseFile.getName());
            selectedTransportLicnse.setVisibility(View.VISIBLE);
            transportLicense.setVisibility(View.GONE);
        } else {
            selectedTransportLicnse.setVisibility(View.GONE);
            transportLicense.setVisibility(View.VISIBLE);
        }

        if (userType.equalsIgnoreCase(FijiRentalUtils.Consumer)) {
//            if (TextUtils.isEmpty(userFirstName.getText().toString().trim()) || TextUtils.isEmpty(userLastName.getText().toString().trim())  //|| TextUtils.isEmpty(userMiddelName.getText().toString().trim())
            if (TextUtils.isEmpty(licenceNumber.getText().toString().trim())
            ) {
                tv_continue.setEnabled(false);
                return;
            }
        }


        if (userType.equalsIgnoreCase(FijiRentalUtils.Owner)) {
            if (TextUtils.isEmpty(statename.getText().toString().trim())) {
                tv_continue.setEnabled(false);
                return;
            }

        }


        if (FijiRentalUtils.isEmptyTextView(countryName) || FijiRentalUtils.isEmptyTextView(birthDate)) {
            tv_continue.setEnabled(false);
            return;
        }

        if (statename.getVisibility() == View.VISIBLE && FijiRentalUtils.isEmptyTextView(statename)) {
            tv_continue.setEnabled(false);
            return;
        }
        tv_continue.setEnabled(true);

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
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DriverLicenseActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DriverLicenseActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });


    }

    private void passdata(JSONArray data_array) {

        countryList.clear();
        for (int i = 0; i < data_array.length(); i++) {
            JSONObject object = data_array.optJSONObject(i);
            CountryModel countryModel = new CountryModel();
            countryModel.setCode(object.optString("code"));
            countryModel.setName(object.optString("name"));
            countryModel.setDial(object.optString("dial"));
            countryModel.setImage(object.optString("image"));
            countryList.add(countryModel);
        }


    }

    @Override
    public void onItemClick(Object val) {
        if (customDialog.getTag().equalsIgnoreCase("country")) {
            countryModel = (CountryModel) val;
            countryName.setText(countryModel.getName());
//            getStateList(countryModel.getCode());
            updateView();
        }

        if (customDialog.getTag().equalsIgnoreCase("state")) {
            StateModel stateModel = (StateModel) val;
            statename.setText(stateModel.getValue());
            updateView();
        }

        if (customDialog.getTag().equals("PICK_UP")) {

            if (val.toString().toLowerCase().contains("gallery")) {
                Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File(getExternalCacheDir(),
                        String.valueOf(System.currentTimeMillis()) + ".jpg");
                fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }

        }

        customDialog.dismiss();
    }

    private void getStateList(String code) {
        progressDialog.show();
        Call<ResponseBody> call = apiService.getStateList(FijiRentalUtils.getAccessToken(this), code);
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
                            passStatedata(data_array);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DriverLicenseActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DriverLicenseActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }

    private void passStatedata(JSONArray data_array) {

        if (data_array.length() == 0) {
            statename.setVisibility(View.GONE);
            return;
        } else {
            statename.setVisibility(View.VISIBLE);
        }

        stateList.clear();
        for (int i = 0; i < data_array.length(); i++) {
            JSONObject object = data_array.optJSONObject(i);
            StateModel stateModel = new StateModel();
            stateModel.setId(object.optString("id"));
            stateModel.setValue(object.optString("value"));
            stateList.add(stateModel);
        }

    }


    private void ListCar() {

        ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

        File uploadFile = new File(FijiRentalUtils.compressImage(carModel.getUserProfile(), new File(getExternalCacheDir(),
                new File(carModel.getUserProfile()).getName())));
        File licenceFile = new File(FijiRentalUtils.compressImage(carModel.getDriverLicenseImage(), new File(getExternalCacheDir(),
                new File(carModel.getDriverLicenseImage()).getName())));


        progressDialog.show();

        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), uploadFile);
        RequestBody requestFile2 = RequestBody.create(MediaType.parse("image/*"), licenceFile);

        reqBuilder.addFormDataPart("driver_image", uploadFile.getName(), requestFile);
        reqBuilder.addFormDataPart("license_image", licenceFile.getName(), requestFile2);
        reqBuilder.addFormDataPart("license_firstname", carModel.getLicenceFirstname());
        reqBuilder.addFormDataPart("license_middlename", carModel.getLicenceMiddlename());
        reqBuilder.addFormDataPart("license_lastname", carModel.getLicenceLastname());
        reqBuilder.addFormDataPart("license_country", carModel.getLicenceCountryname());
        if (carModel.getLicenceStatename() != null) {
            reqBuilder.addFormDataPart("license_state", carModel.getLicenceStatename());
        }

        reqBuilder.addFormDataPart("license_number", carModel.getLicenceNumber());
        reqBuilder.addFormDataPart("license_birthdate", carModel.getLicenceDOb());


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
                            JSONObject vinDataObject = dataObject.optJSONObject("data");
//                            passData(vinDataObject);
                            Intent i = new Intent(DriverLicenseActivity.this, PersonalizeActivity.class);
                            startActivity(i);
                            finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DriverLicenseActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DriverLicenseActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());
                progressDialog.dismiss();

            }
        });

    }

    public boolean checkForPermission(final String[] permissions, final int permRequestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(permissions[i])) {

                    permissionsNeeded.add(perm);

                } else {
                    // add the request.
                    permissionsNeeded.add(perm);
                }
            }
        }

        if (permissionsNeeded.size() > 0) {
            // go ahead and request permissions
            requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), permRequestCode);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_CODE) {
            if (grantResults.length > 0) {
                boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalFile = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (cameraPermission && readExternalFile) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file = new File(getExternalCacheDir(),
                            String.valueOf(System.currentTimeMillis()) + ".jpg");
                    fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);


                } else {
                    Toast.makeText(this, "Need Permission For Upload Photo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Need Permission For Upload Photo", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            if (file.exists()) {
                if (isDrivingLicence) {
                    File uploadFile = new File(FijiRentalUtils.compressImage(file.getAbsolutePath(), new File(getExternalCacheDir(), file.getName())));

                    if ((uploadFile.length() / 1024) > 50) {
                        Toast.makeText(this, FijiRentalUtils.largeImagefile, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    driverLicense = uploadFile;
                    Log.e("TAGS", "file " + (driverLicense == null));
                } else {
                    if (isBusinessLicence) {
                        File uploadFile = new File(FijiRentalUtils.compressImage(file.getAbsolutePath(), new File(getExternalCacheDir(), file.getName())));
                        if ((uploadFile.length() / 1024) > 50) {
                            Toast.makeText(this, FijiRentalUtils.largeImagefile, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        businessLicenseFile = uploadFile;
                    } else {
                        File uploadFile = new File(FijiRentalUtils.compressImage(file.getAbsolutePath(), new File(getExternalCacheDir(), file.getName())));

                        if ((uploadFile.length() / 1024) > 50) {
                            Toast.makeText(this, FijiRentalUtils.largeImagefile, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        transportLicenseFile = uploadFile;
                    }
                }
//                ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
//                carModel.setDriverLicenseImage(file.getAbsolutePath());
//                ((FijiCarRentalApplication) getApplication()).setListCarModel(carModel);


            }

        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            if (new File(getRealPathFromUri(data.getData())).exists()) {
                if (isBusinessLicence) {
                    File uploadFile = new File(FijiRentalUtils.compressImage(new File(getRealPathFromUri(data.getData())).getAbsolutePath(), new File(getExternalCacheDir(), new File(getRealPathFromUri(data.getData())).getName())));

                    if ((uploadFile.length() / 1024) > 50) {
                        Toast.makeText(this, FijiRentalUtils.largeImagefile, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    businessLicenseFile = uploadFile;
                } else {
                    File uploadFile = new File(FijiRentalUtils.compressImage(new File(getRealPathFromUri(data.getData())).getAbsolutePath(), new File(getExternalCacheDir(), new File(getRealPathFromUri(data.getData())).getName())));

                    if ((uploadFile.length() / 1024) > 50) {
                        Toast.makeText(this, FijiRentalUtils.largeImagefile, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    transportLicenseFile = uploadFile;
                }
            }

        }
        updateView();
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(DriverLicenseActivity.this, uri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column);
        cursor.close();
        return result;
    }


    private void updateprofile(RequestBody body) {

        final ProgressDialog progressDialog = new ProgressDialog(DriverLicenseActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);


        Call<ResponseBody> call = apiService.updateprofile(FijiRentalUtils.getAccessToken(DriverLicenseActivity.this), body);

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

                        JSONObject data = jsonobject.optJSONObject("data");
                        message = jsonobject.optString("message");
                        if (data.optString("status").matches("200")) {
                            if (businessLicenseFile != null) {
                                if (businessLicenseFile.exists()) {
                                    businessLicenseFile.delete();
                                }
                            }

                            if (transportLicenseFile != null) {
                                if (transportLicenseFile.exists()) {
                                    transportLicenseFile.delete();
                                }
                            }

                            if (driverLicense != null) {
                                if (driverLicense.exists()) {
                                    driverLicense.delete();
                                }
                            }
                            if (profileFile != null) {
                                if (profileFile.exists()) {
                                    profileFile.delete();
                                }
                            }

                            Intent newIntent = new Intent(DriverLicenseActivity.this, MainActivity.class);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(newIntent);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        updateView();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DriverLicenseActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    updateView();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DriverLicenseActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                updateView();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, DriverLicenseActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

}
