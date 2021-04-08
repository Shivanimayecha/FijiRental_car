package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifiactionSetting extends AppCompatActivity implements View.OnClickListener {


    private Typeface typeface;
    CheckBox textCheckbox, emailCheckbox;
    LinearLayout notificationSetting;
    ImageView backbtn;
    String getMailNot, getPushNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiaction_setting);
        typeface = Typeface.createFromAsset(getAssets(), "OpenSans-SemiBold.ttf");
        textCheckbox = findViewById(R.id.text_checkbox);
        emailCheckbox = findViewById(R.id.email_checkbox);
        notificationSetting = findViewById(R.id.notification_setting);
        backbtn = findViewById(R.id.iv_back);

        textCheckbox.setTypeface(typeface);
        emailCheckbox.setTypeface(typeface);
        notificationSetting.setOnClickListener(this);
        backbtn.setOnClickListener(this);


       updateView();

        emailCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RequestBody body;
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);


                builder.addFormDataPart("user_id", FijiRentalUtils.getId(NotifiactionSetting.this));
                builder.addFormDataPart("mail_notification", String.valueOf(!isChecked));
                builder.addFormDataPart("is_edit", "true");

                body = builder
                        .build();
                updateprofile(body);
            }
        });

        textCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RequestBody body;
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);


                builder.addFormDataPart("user_id", FijiRentalUtils.getId(NotifiactionSetting.this));
                builder.addFormDataPart("push_notification", String.valueOf(!isChecked));
                builder.addFormDataPart("is_edit", "true");

                body = builder
                        .build();
                updateprofile(body);

            }
        });


    }

    private void updateView() {

        getMailNot = FijiRentalUtils.getPrefVal(this, FijiRentalUtils.mail_notification);
        getPushNot = FijiRentalUtils.getPrefVal(this, FijiRentalUtils.push_notification);

        if (getMailNot.equalsIgnoreCase("true")) {
            emailCheckbox.setChecked(true);
        } else {
            emailCheckbox.setChecked(false);
        }
        if (getPushNot.equalsIgnoreCase("true")) {
            textCheckbox.setChecked(true);
        } else {
            textCheckbox.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.notification_setting) {

            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);

        } else if (v.getId() == R.id.iv_back) {
            finish();

        }
    }

    private void updateprofile(RequestBody body) {

        final ProgressDialog progressDialog = new ProgressDialog(NotifiactionSetting.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);


        Call<ResponseBody> call = apiService.updateprofile(FijiRentalUtils.getAccessToken(NotifiactionSetting.this), body);

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


                            FijiRentalUtils.savePreferences(FijiRentalUtils.push_notification, "" + data.optString("push_notification"),NotifiactionSetting.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.mail_notification, "" + data.optString("mail_notification"), NotifiactionSetting.this);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        updateView();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, NotifiactionSetting.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    updateView();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, NotifiactionSetting.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                updateView();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, NotifiactionSetting.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }
}