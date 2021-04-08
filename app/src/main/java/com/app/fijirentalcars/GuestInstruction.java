package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestInstruction extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back;
    TextView saveBtn;
    APIService apiService;
    CarModel carModel;
    EditText returnInstruction, welcomeMessage, carGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_instruction);

        iv_back = findViewById(R.id.iv_back);
        saveBtn = findViewById(R.id.save_btn);
        returnInstruction = findViewById(R.id.car_pickup_instruction);
        welcomeMessage = findViewById(R.id.welcome_message);
        carGuide = findViewById(R.id.car_guide);


        iv_back.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.save_btn) {
            updateCar();
//            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        carModel = Paper.book().read(FijiRentalUtils.carModel);
        if (carModel != null) {

            if (!TextUtils.isEmpty(carModel.getGuestPickupReturnInstructions())) {
                returnInstruction.setText(carModel.getGuestPickupReturnInstructions());
            }
            if (!TextUtils.isEmpty(carModel.getGuestWelcomeMessage())) {
                welcomeMessage.setText(carModel.getGuestWelcomeMessage());
            }
            if (!TextUtils.isEmpty(carModel.getGuestCarMessage())) {
                carGuide.setText(carModel.getGuestCarMessage());
            }

        }
    }

    private void updateCar() {


        apiService = Config.getClient().create(APIService.class);

        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());

        if (!TextUtils.isEmpty(returnInstruction.getText().toString())) {
            reqBuilder.addFormDataPart("guest_pickup_return_instructions", returnInstruction.getText().toString());
        }
        if (!TextUtils.isEmpty(welcomeMessage.getText().toString())) {
            reqBuilder.addFormDataPart("guest_welcome_message", welcomeMessage.getText().toString());
        }
        if (!TextUtils.isEmpty(carGuide.getText().toString())) {
            reqBuilder.addFormDataPart("guest_car_message", carGuide.getText().toString());
        }


        body = reqBuilder
                .build();

        Call<ResponseBody> call = apiService.updateCarListing(FijiRentalUtils.getAccessToken(this), body);
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
                            FijiRentalUtils.Logger("TAGS", "vin res " + jstr);
                            JSONObject dataObject = jsonobject.optJSONObject("data");
                            JSONObject object = dataObject.optJSONObject("data");
                            FijiRentalUtils.updateCarModel(object, GuestInstruction.this);
                            finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, GuestInstruction.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, GuestInstruction.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());

            }
        });

    }
}