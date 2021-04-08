package com.app.fijirentalcars;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.AlterDialog;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_submit;
    ImageView iv_back;
    EditText et_email;
    String Email;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_layout);
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
        iv_back = findViewById(R.id.iv_back);
        et_email = findViewById(R.id.et_email);
        tv_submit = findViewById(R.id.tv_submit);
        iv_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_submit:
                Email = et_email.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    AlterDialog.ShowValidation(getResources().getString(R.string.error_email), ForgotPasswordActivity.this, "0");
                } else if (!FijiRentalUtils.emailValidator(Email)) {
                    AlterDialog.ShowValidation(getResources().getString(R.string.error_validemail), ForgotPasswordActivity.this, "0");
                } else {
                    Forgot_Password(Email);
                }
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    private void Forgot_Password(String email) {

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.forgotpassword(email);


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

                            ShowValidation(message, ForgotPasswordActivity.this, "0");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ForgotPasswordActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ForgotPasswordActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ForgotPasswordActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

    public void ShowValidation(String s, Context context, final String pos) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        View rv = inflater.inflate(R.layout.validation_dialog, null);
        dialogBuilder.setView(rv);
        final Dialog b = dialogBuilder.create();
        TextView tvMessage = rv.findViewById(R.id.tvMessage);
        TextView btnOK = rv.findViewById(R.id.btnOK);
        tvMessage.setText(s);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        dialogBuilder.setTitle("");
        dialogBuilder.setMessage("");
        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        b.show();
    }


}