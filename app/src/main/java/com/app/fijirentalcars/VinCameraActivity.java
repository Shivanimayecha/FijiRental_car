package com.app.fijirentalcars;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.fijirentalcars.CameraActivity.BarcodeGraphic;
import com.app.fijirentalcars.CameraActivity.BarcodeGraphicTracker;
import com.app.fijirentalcars.CameraActivity.BarcodeTrackerFactory;
import com.app.fijirentalcars.CameraActivity.CameraSource;
import com.app.fijirentalcars.CameraActivity.CameraSourcePreview;
import com.app.fijirentalcars.CameraActivity.GraphicOverlay;
import com.app.fijirentalcars.CustomViews.CustomButtonDialog;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.Model.Manufacturer;
import com.app.fijirentalcars.Model.VinModel;
import com.app.fijirentalcars.SQLDB.DBHandler_Manufacture;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VinCameraActivity extends AppCompatActivity implements View.OnClickListener, BarcodeGraphicTracker.BarcodeUpdateListener {

    TextView tv_submit;
    ImageView iv_back;

    TextView barcodeMsg;
    APIService apiService;
    boolean isChecking = false, isVinChecked = false;
    ProgressDialog progressDialog;
    DBHandler_Manufacture dbHandler_manufacture;
    ArrayList<Manufacturer> manList=new ArrayList<>();

    private static final int RC_HANDLE_GMS = 9001;

    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String BarcodeObject = "Barcode";

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vin_camera_layout);
        Window window = this.getWindow();
        Log.e("VinCameraActivity", "VinCameraActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        apiService = Config.getClient().create(APIService.class);

        dbHandler_manufacture=new DBHandler_Manufacture(this);
        manList.clear();
        manList.addAll(dbHandler_manufacture.getAllJobs());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        init();
    }


    public void init() {


        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) findViewById(R.id.graphicOverlay);

        barcodeMsg = findViewById(R.id.text_msg);

        tv_submit = findViewById(R.id.tv_submit);
        iv_back = findViewById(R.id.iv_back);
        tv_submit.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(true, false);
        } else {
            requestCameraPermission();
        }



    }

    private void requestCameraPermission() {


        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };
//
        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, this);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {


            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();

            }
        }

        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(
                    autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
        }

        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_submit:
                Intent i = new Intent(this, VinActivity.class);
                startActivity(i);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void checkVinNumber(Barcode barcode) {


        progressDialog.show();
        HashMap<String, String> data = new HashMap<>();
        data.put("vin", barcode.displayValue);

        FijiRentalUtils.Logger("TAGS", "checcking vin  " + barcode.displayValue);
        Call<ResponseBody> call = apiService.checkCarVIN(FijiRentalUtils.getAccessToken(this), data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;

                    jsonobject = new JSONObject(jstr);
                    message = jsonobject.optString("message");
                    if (jsonobject.optString("code").matches("200")) {
                        FijiRentalUtils.Logger("TAGS", "vin res " + jstr);
                        JSONObject dataObject = jsonobject.optJSONObject("data");
                        JSONObject vinDataObject = dataObject.optJSONObject("data");
                        passData(vinDataObject, barcode);
                        isChecking = false;

                    }
                    isChecking = false;
                } catch (Exception e) {
                    isChecking = false;
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, VinCameraActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isChecking = false;
                progressDialog.dismiss();

            }
        });

    }

    private void passData(JSONObject vinDataObject, Barcode barcode) {
        if (!vinDataObject.optString("error-code").equalsIgnoreCase("0")) {
            Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show();

            return;
        }

        VinModel vinModel = new VinModel();
        vinModel.setMake(vinDataObject.optString("make"));
        vinModel.setManufacturerName(vinDataObject.optString("manufacturer_name"));
        vinModel.setModel(vinDataObject.optString("model"));
        vinModel.setYear(vinDataObject.optString("year"));
        vinModel.setTrim(vinDataObject.optString("trim"));
        vinModel.setVehicleType(vinDataObject.optString("vehicle-type"));
        vinModel.setBodyclass(vinDataObject.optString("body-class"));
        vinModel.setDoor(vinDataObject.optString("doors"));
        vinModel.setGrossVehicleWeightRating(vinDataObject.optString("gross-vehicle-weight-rating"));
        vinModel.setDriveType(vinDataObject.optString("drive-type"));
        vinModel.setBreakeSystem(vinDataObject.optString("brake-system-type"));
        vinModel.setEngineCylinder(vinDataObject.optString("engine-number-of-cylinders"));
        vinModel.setFuelType(vinDataObject.optString("fuel-type-primary"));
        vinModel.setSeatbeltType(vinDataObject.optString("seat-belts-type"));
        vinModel.setManufactureID(FijiRentalUtils.getManFacID(manList,vinDataObject.optString("make")));
        vinModel.setVinNumber(barcode.displayValue);
        ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
        listingCarModel.setModel(vinModel);

        ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);
        isVinChecked = true;

        if (mPreview != null) {
            mPreview.stop();
        }

        Intent i = new Intent(this, FillDetailsCarActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }


    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus, false);
            boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }


        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fijirental Cars")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }


    private void startCameraSource() throws SecurityException {

        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    @Override
    public void onBarcodeDetected(Barcode barcode) {
        if (FijiRentalUtils.checkInternetConnection(this)) {

            if (!isChecking) {
                isChecking = true;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkVinNumber(barcode);
                    }
                });

            }
        }
    }
}

