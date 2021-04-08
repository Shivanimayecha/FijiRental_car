package com.app.fijirentalcars;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.AlterDialog;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner {

    TextView tv_continue, tv_login, businessLicense, transportLicense, tv_firstName, tv_lastName;
    EditText et_fname, et_lname, et_email, et_password, et_phone, CityName, SuburbName, HomeAddress;
    ImageView iv_back;
    String FirstName, LastName, Email, Password, PhoneNumber;
    LinearLayout googleBtn, facebookBtn, consumerAddress, ownerLicenseLayout, businessLicenseLayout, transportLicenseLayout;
    private FirebaseAuth mAuth;
    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private CallbackManager facebookCallbackManager;
    private LoginButton facebookLoginButton;
    String userType = "";
    private GoogleSignInClient mGoogleSignInClient;

    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    int STORAGE_CODE = 1002;
    CustomDialog customDialog;
    File file;
    boolean isBusinessLicence = false;
    Uri fileUri;
    private static final int REQUEST_CODE_CAMERA = 1003;
    private static final int REQUEST_CODE_GALLERY = 1004;
    LinearLayout selectedBusinessLicnse, selectedTransportLicnse;
    AppCompatImageView businessLicenseImage, closeBusinessLicenseImage, transportLicenseImage, closeTransportLicenseImage;
    File businessLicenseFile, transportLicenseFile;
    TextView businessLicenseName, transportLicenseName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Window window = this.getWindow();
        Log.e("SignUpActivity", "SignUpActivity");
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        inilization();
    }

    private void inilization() {
        facebookLoginButton = findViewById(R.id.facebookLoginButton);
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setPermissions(Arrays.asList("email", "public_profile"));
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.web_client_id)).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        bindcomponents();
        implemetlistioner();
        if (getIntent().hasExtra("USERTYPE")) {
            userType = getIntent().getStringExtra("USERTYPE");

            if (userType.equalsIgnoreCase(FijiRentalUtils.Consumer)) {
//                consumerAddress.setVisibility(View.VISIBLE);
//                ownerLicenseLayout.setVisibility(View.GONE);
                et_fname.setHint(getString(R.string.full_name));
                tv_firstName.setText(getString(R.string.full_name));
                tv_lastName.setVisibility(View.GONE);
                et_lname.setVisibility(View.GONE);

            } else {
//                consumerAddress.setVisibility(View.GONE);
//                ownerLicenseLayout.setVisibility(View.VISIBLE);
                et_fname.setHint(getString(R.string.company_name));
                tv_firstName.setText(getString(R.string.company_name));
                tv_lastName.setVisibility(View.GONE);
                et_lname.setVisibility(View.GONE);

            }
        }


    }

    private void bindcomponents() {
        et_fname = findViewById(R.id.et_fname);
        tv_firstName = findViewById(R.id.tv_firstname);
        tv_lastName = findViewById(R.id.tv_lname);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_phone = findViewById(R.id.et_phone);
        CityName = findViewById(R.id.city);
        SuburbName = findViewById(R.id.suburb);
        HomeAddress = findViewById(R.id.home_address);
        googleBtn = findViewById(R.id.google_btn);
        facebookBtn = findViewById(R.id.ll_facebook);
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

        consumerAddress = findViewById(R.id.address_layout);
        ownerLicenseLayout = findViewById(R.id.rental_license);

        consumerAddress.setVisibility(View.GONE);
        ownerLicenseLayout.setVisibility(View.GONE);

        ArrayList type = new ArrayList();
        type.add("From Gallery");
        type.add("From Camera");
        customDialog = new CustomDialog(this, type, this);


        googleBtn.setOnClickListener(this);
        facebookBtn.setOnClickListener(this);

        iv_back = findViewById(R.id.iv_back);
        tv_continue = findViewById(R.id.tv_continue);
        tv_login = findViewById(R.id.tv_login);

        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                // Application code
                                //   name = object.optString("name");
                                // email = object.optString("email");
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("FacebookLogin", exception.toString());
            }
        });

        updateView();

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d("FacebookLogin", "handleFacebookAccessToken:" + accessToken);
        Toast.makeText(SignUpActivity.this, R.string.profile_fetch, Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    getToken(user);
                } else {
                    if (task.getException() != null && task.getException().getMessage() != null) {
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // If sign in fails, display a message to the user.
                    Log.w("FacebookLogin", "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    private void implemetlistioner() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                if (checkForPermission(permissions, STORAGE_CODE)) {
                    customDialog.show(getSupportFragmentManager(), "PICK_UP");
                }
            }
        });

        transportLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBusinessLicence = false;
                if (checkForPermission(permissions, STORAGE_CODE)) {
                    customDialog.show(getSupportFragmentManager(), "PICK_UP");
                }
            }
        });


        /*tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, MapActivity.class);
                startActivity(i);
            }
        });*/

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FijiRentalUtils.hideKeyboard(SignUpActivity.this);
                Validation();
            }
        });
    }

    private void Validation() {
        FirstName = et_fname.getText().toString();
        LastName = et_lname.getText().toString();
        Email = et_email.getText().toString();
        Password = et_password.getText().toString();
        PhoneNumber = et_phone.getText().toString();

        if (TextUtils.isEmpty(FirstName)) {
            AlterDialog.ShowValidation(getResources().getString(R.string.error_firstname), SignUpActivity.this, "0");
        } else if (TextUtils.isEmpty(Email)) {
            AlterDialog.ShowValidation(getResources().getString(R.string.error_email), SignUpActivity.this, "0");
        } else if (!FijiRentalUtils.emailValidator(Email)) {
            AlterDialog.ShowValidation(getResources().getString(R.string.error_validemail), SignUpActivity.this, "0");
        } else if (TextUtils.isEmpty(Password)) {
            AlterDialog.ShowValidation(getResources().getString(R.string.error_pws), SignUpActivity.this, "0");
        } else if (Password.length() < 4) {
            AlterDialog.ShowValidation(getResources().getString(R.string.error_pws_length), SignUpActivity.this, "0");
        } else if (TextUtils.isEmpty(PhoneNumber)) {
            AlterDialog.ShowValidation(getResources().getString(R.string.error_phone), SignUpActivity.this, "0");
        } else if (PhoneNumber.length() < 4) {
            AlterDialog.ShowValidation(getResources().getString(R.string.error_phone_length), SignUpActivity.this, "0");
        } else {

//            if (userType.equalsIgnoreCase(FijiRentalUtils.Consumer)) {
////                if (TextUtils.isEmpty(CityName.getText().toString())) {
////                    AlterDialog.ShowValidation(getResources().getString(R.string.error_cityname), SignUpActivity.this, "0");
////                    return;
////                }
////                if (TextUtils.isEmpty(SuburbName.getText().toString())) {
////                    AlterDialog.ShowValidation(getResources().getString(R.string.error_Suburbname), SignUpActivity.this, "0");
////                    return;
////                }
//                if (TextUtils.isEmpty(LastName)) {
//                    AlterDialog.ShowValidation(getResources().getString(R.string.error_lastname), SignUpActivity.this, "0");
//                    return;
//                }
//
//            }
//
//            if (userType.equalsIgnoreCase(FijiRentalUtils.Owner)) {
//                if (businessLicenseFile == null) {
//                    AlterDialog.ShowValidation(getResources().getString(R.string.error_business_license), SignUpActivity.this, "0");
//                    return;
//                }
//                if (transportLicenseFile == null) {
//                    AlterDialog.ShowValidation(getResources().getString(R.string.error_transport_license), SignUpActivity.this, "0");
//                    return;
//                }
//            }

            if (FijiRentalUtils.isNetworkAvailable(SignUpActivity.this)) {
                registerUser(FijiRentalUtils.Device_id(SignUpActivity.this), "1",
                        FijiRentalUtils.FirebaseToken(), Email, Password, FirstName,
                        "", PhoneNumber);
            }

        }
    }

    private void registerUser(String deviceId, String deviceType, String deviceToken, String email,
                              String password, String fname, String lname, String phone) {

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> addressMap = new HashMap<>();
        addressMap.put("email", email);
        addressMap.put("password", password);
        addressMap.put("fname", fname);
        addressMap.put("mobile", phone);
        addressMap.put("role", userType);
//        if (userType.equalsIgnoreCase(FijiRentalUtils.Consumer)) {
//            addressMap.put("city", CityName.getText().toString().trim());
////            addressMap.put("city",SuburbName.getText().toString().trim());
//
//            if (!TextUtils.isEmpty(HomeAddress.getText().toString())) {
////                addressMap.put("",HomeAddress.getText().toString().trim());
//            }
//
//        }

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.register(deviceId, deviceType, deviceToken, addressMap);


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

                            FijiRentalUtils.savePreferences(FijiRentalUtils.accessToken, "" + data.optString("accessToken"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.roles, "" + data.optString("role"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.ID, "" + data.optString("ID"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_login, "" + data.optString("user_login"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_nicename, "" + data.optString("user_nicename"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_email, "" + data.optString("user_email"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_pic_url, "" + data.optString("user_pic_url"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_activation_key, "" + data.optString("user_activation_key"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_status, "" + data.optString("user_status"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.display_name, "" + data.optString("display_name"), SignUpActivity.this);

                            Intent intent = new Intent(SignUpActivity.this, ProfilePictureActivity.class); //MainActivity
                            intent.putExtra("USERTYPE", userType);
                            startActivity(intent);

                        } else {
                            Toast.makeText(SignUpActivity.this, " " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SignUpActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SignUpActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SignUpActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.google_btn) {
            startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_GOOGLE_SIGN_IN);
        } else if (v.getId() == R.id.ll_facebook) {
            facebookLoginButton.performClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("GoogleSignIn", "Google sign in failed", e);

            }

        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            if (file.exists()) {
                if (isBusinessLicence) {
                    businessLicenseFile = file;
                } else {
                    transportLicenseFile = file;
                }

            }

            updateView();

        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            if (new File(getRealPathFromUri(data.getData())).exists()) {
                if (isBusinessLicence) {
                    businessLicenseFile = new File(getRealPathFromUri(data.getData()));
                } else {
                    transportLicenseFile = new File(getRealPathFromUri(data.getData()));
                }
            }
            updateView();

        }
    }

    private void updateView() {
        if (businessLicenseFile != null) {
            Glide.with(SignUpActivity.this).load(businessLicenseFile).centerCrop().into(businessLicenseImage);
            businessLicenseName.setText(businessLicenseFile.getName());
            selectedBusinessLicnse.setVisibility(View.VISIBLE);
            businessLicense.setVisibility(View.GONE);

        } else {
            selectedBusinessLicnse.setVisibility(View.GONE);
            businessLicense.setVisibility(View.VISIBLE);
        }
        if (transportLicenseFile != null) {
            Glide.with(SignUpActivity.this).load(transportLicenseFile).centerCrop().into(transportLicenseImage);
            transportLicenseName.setText(transportLicenseFile.getName());
            selectedTransportLicnse.setVisibility(View.VISIBLE);
            transportLicense.setVisibility(View.GONE);
        } else {
            selectedTransportLicnse.setVisibility(View.GONE);
            transportLicense.setVisibility(View.VISIBLE);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(SignUpActivity.this, R.string.profile_fetch, Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    getToken(user);
                } else {
                    if (task.getException() != null && task.getException().getMessage() != null) {
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    private void getToken(FirebaseUser user) {
        user.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String idToken = getTokenResult.getToken();
                fb_Login(FijiRentalUtils.Device_id(SignUpActivity.this), "1",
                        FijiRentalUtils.FirebaseToken(), user.getEmail(), user.getUid(), user);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Token retrieve error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fb_Login(String deviceId, String deviceType, String deviceToken, String
            email, String facebook_id, FirebaseUser user) {

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call;
        boolean isFacebookuser = false;

        for (UserInfo loginuser : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (loginuser.getProviderId().equals("facebook.com")) {
                isFacebookuser = true;
            }
            if (loginuser.getProviderId().equals("google.com")) {
                isFacebookuser = false;
            }
        }


        if (isFacebookuser) {
            HashMap<String,String> data=new HashMap<>();
            data.put("email",email);
            data.put("facebookid",facebook_id);
            data.put("role",userType);
            data.put("fname",user.getDisplayName());
            Log.e("TAGS","data map "+data);
            call = apiService.fblogin(deviceId, deviceType, deviceToken, data);
        } else {

            HashMap<String,String> data=new HashMap<>();
            data.put("email",email);
            data.put("googleid",facebook_id);
            data.put("role",userType);
            data.put("fname",user.getDisplayName());
            Log.e("TAGS","data map "+data);
            call = apiService.googlelogin(deviceId, deviceType, deviceToken,data);
        }


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

                            FijiRentalUtils.savePreferences(FijiRentalUtils.accessToken, "" + data.optString("accessToken"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.roles, "" + data.optString("role"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.ID, "" + data.optString("ID"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_login, "" + data.optString("user_login"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_nicename, "" + data.optString("user_nicename"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_email, "" + data.optString("user_email"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_pic_url, "" + data.optString("user_pic_url"), SignUpActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.display_name, "" + data.optString("display_name"), SignUpActivity.this);

//                            Intent i2 = new Intent(SignUpActivity.this, MainActivity.class);
//                            startActivity(i2);

                            JSONObject object = data.optJSONObject("driver_details");

                            String licenceImage = object.optString("license_image");

                            String userRole=data.optString("role");


                            if (userRole.equalsIgnoreCase(FijiRentalUtils.Consumer)) {
                                if (TextUtils.isEmpty(licenceImage)) {
                                    Intent intent = new Intent(SignUpActivity.this, ProfilePictureActivity.class); //MainActivity
                                    intent.putExtra("USERTYPE", userRole);
                                    startActivity(intent);
                                } else {
                                    Intent i2 = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(i2);
                                }
                            } else {
                                String businessLicense = data.optString("business_license");

                                if (TextUtils.isEmpty(businessLicense)) {
                                    Intent intent = new Intent(SignUpActivity.this, ProfilePictureActivity.class); //MainActivity
                                    intent.putExtra("USERTYPE", userRole);
                                    startActivity(intent);
                                } else {
                                    Intent i2 = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(i2);
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SignUpActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SignUpActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, SignUpActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
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

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(SignUpActivity.this, uri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column);
        cursor.close();
        return result;
    }
}