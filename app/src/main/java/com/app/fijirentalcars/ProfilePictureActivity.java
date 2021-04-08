package com.app.fijirentalcars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.Manifest;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.listners.UploadListner;
import com.app.fijirentalcars.service.UploadService;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePictureActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner {

    TextView tv_continue;
    ImageView iv_back;
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    int STORAGE_CODE = 1002;
    TextView imagePick;
    CustomDialog customDialog;
    CircleImageView profileImage;
    private static final int REQUEST_CODE_CAMERA = 1003;
    private static final int REQUEST_CODE_GALLERY = 1004;
    UploadService mBoundService;
    private boolean mShouldUnbind;
    File file;
    boolean isImagePicked = false;
    Uri fileUri;
    List type;
    boolean isCarRegister = false;
    String userType = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        Log.e("ProfilePictureActivity", "ProfilePictureActivity");
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        type = new ArrayList();
        type.add("From Gallery");
        type.add("From Camera");
        customDialog = new CustomDialog(this, type, this);

        init();

    }

    public void init() {
        tv_continue = findViewById(R.id.tv_continue);
        iv_back = findViewById(R.id.iv_back);
        imagePick = findViewById(R.id.pick_image);
        profileImage = findViewById(R.id.profile_image);
        tv_continue.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        imagePick.setOnClickListener(this);

        if (getIntent().hasExtra(FijiRentalUtils.CAR_FLAW)) {
            isCarRegister = getIntent().getBooleanExtra(FijiRentalUtils.CAR_FLAW, false);
        }

        if (getIntent().hasExtra("USERTYPE")) {
            userType = getIntent().getStringExtra("USERTYPE");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isCarRegister){
            ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

            if(!TextUtils.isEmpty(carModel.getUserProfile())){
                Glide.with(ProfilePictureActivity.this).load(new File(carModel.getUserProfile())).placeholder(R.drawable.ic_action_person).into(profileImage);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_continue:

                if(file==null){
                    FijiRentalUtils.ShowValidation("Select Profile picture",ProfilePictureActivity.this,"");
                    return;
                }

                if (isCarRegister) {
                    ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
                    carModel.setUserProfile(file.getAbsolutePath());
                    ((FijiCarRentalApplication) getApplication()).setListCarModel(carModel);
                }

                if(!TextUtils.isEmpty(userType)){

                    File uploadFile = new File(FijiRentalUtils.compressImage(file.getAbsolutePath(),new File(getExternalCacheDir(),file.getName()) ));

                    if ((uploadFile.length() / 1024) > 50) {
                        Toast.makeText(this, FijiRentalUtils.largeImagefile, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ListingCarModel carModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
                    carModel.setUserProfile(uploadFile.getAbsolutePath());
                    ((FijiCarRentalApplication) getApplication()).setListCarModel(carModel);
                }

                Intent i = new Intent(this, DriverLicenseActivity.class);
                i.putExtra(FijiRentalUtils.CAR_FLAW, true);
                i.putExtra("USERTYPE",userType);
                startActivity(i);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.pick_image:
                if (checkForPermission(permissions, STORAGE_CODE)) {
                    customDialog.show(getSupportFragmentManager(), "PICK_UP");
                }
                break;
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
//                Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
//                Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
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

                Glide.with(ProfilePictureActivity.this).load(file).placeholder(R.drawable.ic_action_person).into(profileImage);

                isImagePicked = true;
//                upLoadImage(file);

            }

        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {

            file=new File(getRealPathFromUri(data.getData()));

            if (file.exists()) {
                isImagePicked = true;
                Glide.with(ProfilePictureActivity.this).load(file).placeholder(R.drawable.ic_action_person).into(profileImage);
            }
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(ProfilePictureActivity.this, uri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column);
        cursor.close();
        return result;
    }


}