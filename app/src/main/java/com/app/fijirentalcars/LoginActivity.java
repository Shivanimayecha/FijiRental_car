package com.app.fijirentalcars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.AlterDialog;
import com.app.fijirentalcars.util.FijiRentalUtils;
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
import com.google.android.gms.common.SignInButton;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner {


    TextView tv_continue, tv_login, tv_forgot_password;
    ImageView iv_back;
    EditText et_email, et_password;
    String Email, Password;
    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager facebookCallbackManager;
    private LoginButton facebookLoginButton;
    private FirebaseAuth mAuth;
    private LinearLayout ll_facebook, ll_google;
    public SignInButton google_sign_in_button;
    CustomDialog customDialog;
    List type;
    String userType = "";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        Log.e("LoginActivity", "LoginActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));


        init();
    }

    public void init() {

        type = new ArrayList();
        type.add(getString(R.string.customer).toUpperCase());
        type.add(getString(R.string.rental_owner).toUpperCase());
        customDialog = new CustomDialog(this, type, this);
        customDialog.setCancelable(false);

        iv_back = findViewById(R.id.iv_back);
        tv_continue = findViewById(R.id.tv_continue);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_login = findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        iv_back.setOnClickListener(this);
//        tv_continue.setOnClickListener(this);

        ll_facebook = (LinearLayout) findViewById(R.id.ll_facebook);
        ll_google = findViewById(R.id.ll_google);
        ll_facebook.setOnClickListener(this);
        ll_google.setOnClickListener(this);
        google_sign_in_button = findViewById(R.id.google_sign_in_button);
        TextView gSignInButtonText = (TextView) google_sign_in_button.getChildAt(0);
        if (gSignInButtonText != null) {
            gSignInButtonText.setText("Google");
        }
        mAuth = FirebaseAuth.getInstance();
        facebookLoginButton = findViewById(R.id.facebookLoginButton);
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setPermissions(Arrays.asList("email", "public_profile"));
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
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.web_client_id)).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_GOOGLE_SIGN_IN);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_continue:
                Intent i = new Intent(this, ProfilePictureActivity.class);
                startActivity(i);
                break;

            case R.id.ll_facebook:
                facebookLoginButton.performClick();
                break;
            case R.id.ll_google:

                startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_GOOGLE_SIGN_IN);
//                google_sign_in_button.performClick();
                break;
            case R.id.tv_forgot_password:
                Intent f = new Intent(this, ForgotPasswordActivity.class);
                startActivity(f);
                break;
            case R.id.tv_login:
                Email = et_email.getText().toString();
                Password = et_password.getText().toString();
                FijiRentalUtils.hideKeyboard(this);

                if (TextUtils.isEmpty(Email)) {
                    AlterDialog.ShowValidation(getResources().getString(R.string.error_email), LoginActivity.this, "0");
                } else if (!FijiRentalUtils.emailValidator(Email)) {
                    AlterDialog.ShowValidation(getResources().getString(R.string.error_validemail), LoginActivity.this, "0");
                } else if (TextUtils.isEmpty(Password)) {
                    AlterDialog.ShowValidation(getResources().getString(R.string.error_pws), LoginActivity.this, "0");
                } else if (Password.length() < 4) {
                    AlterDialog.ShowValidation(getResources().getString(R.string.error_pws_length), LoginActivity.this, "0");
                } else {
                    Login(FijiRentalUtils.Device_id(LoginActivity.this), "1",
                            FijiRentalUtils.FirebaseToken(), Email, Password);
                }
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    private void Login(String deviceId, String deviceType, String deviceToken, String email, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.app_login(deviceId, deviceType, deviceToken, email, password);


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

                            FijiRentalUtils.savePreferences(FijiRentalUtils.accessToken, "" + data.optString("accessToken"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.roles, "" + data.optString("role"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.ID, "" + data.optString("ID"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_login, "" + data.optString("user_login"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_nicename, "" + data.optString("user_nicename"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_email, "" + data.optString("user_email"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_pic_url, "" + data.optString("user_pic_url"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.display_name, "" + data.optString("display_name"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_pass, "" + password, LoginActivity.this);

//                            Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(i2);
                            JSONObject object = data.optJSONObject("driver_details");

                            String licenceImage = object.optString("license_image");

                            String userRole = data.optString("role");


                            if (userRole.equalsIgnoreCase(FijiRentalUtils.Consumer)) {
                                if (TextUtils.isEmpty(licenceImage)) {
                                    Intent intent = new Intent(LoginActivity.this, ProfilePictureActivity.class); //MainActivity
                                    intent.putExtra("USERTYPE", userRole);
                                    startActivity(intent);
                                } else {
                                    Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i2);
                                }
                            } else {
                                String businessLicense = data.optString("business_license");

                                if (TextUtils.isEmpty(businessLicense)) {
                                    Intent intent = new Intent(LoginActivity.this, ProfilePictureActivity.class); //MainActivity
                                    intent.putExtra("USERTYPE", userRole);
                                    startActivity(intent);
                                } else {
                                    Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i2);
                                }
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, LoginActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, LoginActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, LoginActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d("FacebookLogin", "handleFacebookAccessToken:" + accessToken);
        Toast.makeText(LoginActivity.this, R.string.profile_fetch, Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                    if (isNew) {
                        customDialog.show(getSupportFragmentManager(), FijiRentalUtils.User_Selection);

                    } else {
                        FirebaseUser user = mAuth.getCurrentUser();
                        getToken(user);
                    }
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    getToken(user);
                } else {
                    if (task.getException() != null && task.getException().getMessage() != null) {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // If sign in fails, display a message to the user.
                    Log.w("FacebookLogin", "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    private void getToken(FirebaseUser user) {
        user.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String idToken = getTokenResult.getToken();
                Log.d("Authorization", "Bearer " + idToken+" email "+user.getEmail());
                fb_Login(FijiRentalUtils.Device_id(LoginActivity.this), "1",
                        FijiRentalUtils.FirebaseToken(), user.getEmail(), user.getUid(), user);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Token retrieve error", Toast.LENGTH_LONG).show();
            }
        });
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

                // ...
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(LoginActivity.this, R.string.profile_fetch, Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();

                    if (isNew) {
                        customDialog.show(getSupportFragmentManager(), FijiRentalUtils.User_Selection);

                    } else {
                        FirebaseUser user = mAuth.getCurrentUser();
                        getToken(user);
                    }
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    getToken(user);
                } else {
                    if (task.getException() != null && task.getException().getMessage() != null) {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // If sign in fails, display a message to the user.
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    private void fb_Login(String deviceId, String deviceType, String deviceToken, String
            email, String facebook_id, FirebaseUser user) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call;
//        call = apiService.fblogin(deviceId, deviceType, deviceToken, email, facebook_id,"");

        boolean isFacebookuser = false;

        for (UserInfo loginuser : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (loginuser.getProviderId().equals("facebook.com")) {
                Log.e("TAGS", "User is signed in with Facebook");
                isFacebookuser = true;
            }
            if (loginuser.getProviderId().equals("google.com")) {
                Log.e("TAGS", "User is signed in with Google");
                isFacebookuser = false;
            }
        }
        Log.e("TAGS","role "+userType);

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
            call = apiService.googlelogin(deviceId, deviceType, deviceToken, data);
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

                            FijiRentalUtils.savePreferences(FijiRentalUtils.accessToken, "" + data.optString("accessToken"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.roles, "" + data.optString("role"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.ID, "" + data.optString("ID"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_login, "" + data.optString("user_login"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_nicename, "" + data.optString("user_nicename"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_email, "" + data.optString("user_email"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_pic_url, "" + data.optString("user_pic_url"), LoginActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.display_name, "" + data.optString("display_name"), LoginActivity.this);


//                            Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(i2);

                            JSONObject object = data.optJSONObject("driver_details");

                            String licenceImage = object.optString("license_image");

                            String userRole = data.optString("role");


                            if (userRole.equalsIgnoreCase(FijiRentalUtils.Consumer)) {
                                if (TextUtils.isEmpty(licenceImage)) {
                                    Intent intent = new Intent(LoginActivity.this, ProfilePictureActivity.class); //MainActivity
                                    intent.putExtra("USERTYPE", userRole);
                                    startActivity(intent);
                                } else {
                                    Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i2);
                                }
                            } else {
                                String businessLicense = data.optString("business_license");

                                if (TextUtils.isEmpty(businessLicense)) {
                                    Intent intent = new Intent(LoginActivity.this, ProfilePictureActivity.class); //MainActivity
                                    intent.putExtra("USERTYPE", userRole);
                                    startActivity(intent);
                                } else {
                                    Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i2);
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, LoginActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, LoginActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, LoginActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(Object val) {
        if (customDialog.getTag().equalsIgnoreCase(FijiRentalUtils.User_Selection)) {
            String value = String.valueOf(val);

            if (value.equalsIgnoreCase(getString(R.string.customer))) {
                userType = FijiRentalUtils.Consumer;
            } else {
                userType = FijiRentalUtils.Owner;
            }

            Log.e("TAGS","value "+userType+" "+value);
            FirebaseUser user = mAuth.getCurrentUser();
            getToken(user);
        }

        customDialog.dismiss();

    }
}