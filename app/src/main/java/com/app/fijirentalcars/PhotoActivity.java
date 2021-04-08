package com.app.fijirentalcars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Adapter.PhotoAdapter;
import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.listners.UploadListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.service.UploadService;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner, UploadListner {

    private static final int REQUEST_CODE_CAMERA = 1003;
    private static final int REQUEST_CODE_GALLERY = 1004;
    ImageView iv_plus;
    ImageView iv_back;
    CarModel carModel;
    CustomDialog customDialog;
    List carImages = new ArrayList();
    File file;
    TextView tv_next;
    TextView emptyView;
    Uri fileUri;
    PhotoAdapter adapter;
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    int STORAGE_CODE = 1002;
    List type;
    RecyclerView recyclerView;
    UploadService mBoundService;
    private boolean mShouldUnbind;
    boolean isCarListing = false;

    ProgressDialog progressDialog;
    APIService apiService;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Window window = this.getWindow();
        Log.e("PhotoActivity", "PhotoActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        type = new ArrayList();
        type.add("From Gallery");
        type.add("From Camera");
        customDialog = new CustomDialog(this, type, this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        apiService = Config.getClient().create(APIService.class);


        init();
    }

    public void init() {
        iv_plus = findViewById(R.id.iv_plus);
        iv_back = findViewById(R.id.iv_back);
        tv_next = findViewById(R.id.tv_next);
        emptyView = findViewById(R.id.emptyView);
        iv_plus.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_next.setOnClickListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewphotos);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        adapter = new PhotoAdapter(this, carImages);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
            carModel = (CarModel) getIntent().getSerializableExtra(FijiRentalUtils.CarDetailIntent);
            getCarPhotos();
        }

        if (getIntent().hasExtra(FijiRentalUtils.CAR_FLAW)) {
            ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
            carModel = new CarModel();
            carModel.setItemId(listingCarModel.getItemId());
            tv_next.setVisibility(View.VISIBLE);

            FijiRentalUtils.Logger("TAGS","id "+carModel.getItemId());

        } else {
            tv_next.setVisibility(View.GONE);
        }
    }

    private void getCarPhotos() {

        carImages.clear();
        carImages.addAll(carModel.getPhotos());
        adapter.notifyDataSetChanged();

        updateView();

//        adapter.UpdateList(carModel.getPhotos());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_plus:


                if (checkForPermission(permissions, STORAGE_CODE)) {
                    customDialog.show(getSupportFragmentManager(), "PICK_UP");
                }
                break;
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_next:

                if (carImages.size() < 3) {
                    FijiRentalUtils.ShowValidation("Please select atleast 3 image of car.", PhotoActivity.this, "");
                    return;
                }

                Intent i = new Intent(this, SafetyStandards.class);
                i.putExtra(FijiRentalUtils.CAR_FLAW, true);
                startActivity(i);
                break;
        }
    }

    private void updateView() {
        if (carImages.size() < 1) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
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
                    customDialog.show(getSupportFragmentManager(), "PICK_UP");


                } else {
                    Toast.makeText(this, "Need Permission For Upload Photo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Need Permission For Upload Photo", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onItemClick(Object val) {
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

            customDialog.dismiss();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            if (file.exists()) {
                carImages.add(Uri.fromFile(file).toString());
                adapter.notifyDataSetChanged();
                if (mBoundService != null) {
                    mBoundService.upLoadImage(file, carModel.getItemId(), String.valueOf(adapter.getItemCount()), false);
                }

//                upLoadImage(file);

            }

        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {


            if (new File(getRealPathFromUri(data.getData())).exists()) {
                carImages.add((data.getData()));
                adapter.notifyDataSetChanged();

                if (mBoundService != null) {
                    mBoundService.upLoadImage(new File(getRealPathFromUri(data.getData())), carModel.getItemId(), String.valueOf(adapter.getItemCount()), true);
                }
            }

        }
        updateView();

    }

    private void upLoadImage(File file) {

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("item_id", carModel.getItemId())
                .addFormDataPart("item_image_1", file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file))

                .build();


        progressDialog.show();
        apiService.uploadImage(FijiRentalUtils.getAccessToken(this), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;

                    jsonobject = new JSONObject(jstr);
                    if (jsonobject.optString("code").matches("200")) {

                        Toast.makeText(PhotoActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, PhotoActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(PhotoActivity.this, uri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column);
        cursor.close();
        return result;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((UploadService.LocalBinder) service).getService();
            mBoundService.setListner(PhotoActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mBoundService = null;
        }
    };

    void doBindService() {

        if (bindService(new Intent(this, UploadService.class),
                serviceConnection, Context.BIND_AUTO_CREATE)) {
            mShouldUnbind = true;
        } else {
            mShouldUnbind = false;
            FijiRentalUtils.Logger("TAGS", "Error: The requested service doesn't  exist, or this client isn't allowed access to it.");
        }
    }

    void doUnbindService() {
        if (mShouldUnbind) {
            // Release information about the service's state.
            unbindService(serviceConnection);
            mShouldUnbind = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        doBindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        doUnbindService();

    }

    @Override
    protected void onDestroy() {
//        unbindService(serviceConnection);
        super.onDestroy();
    }


    @Override
    public void onUploadFailed(String pos) {
        Toast.makeText(PhotoActivity.this, "removing " + pos, Toast.LENGTH_SHORT).show();
        if (Integer.parseInt(pos) > 1) {
            carImages.remove(Integer.parseInt(pos) - 1);
        }
        updateView();
        adapter.notifyDataSetChanged();

    }
}