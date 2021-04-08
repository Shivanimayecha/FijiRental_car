package com.app.fijirentalcars;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.DBModel;
import com.app.fijirentalcars.SQLDB.HRDatabase;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.app.fijirentalcars.util.HRDateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManualPricingActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_about;
    ImageView iv_back;
    EditText customPrice;
    RelativeLayout rlDiscount, customEditPrice;
    APIService apiService;
    TextView discountMsg;
    CarModel carModel;
    private com.app.fijirentalcars.SQLDB.HRDatabase HRDatabase;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateStatus();
                }
            });
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_pricing);
        Window window = this.getWindow();
        Log.e("ManualPricingActivity", "ManualPricingActivity");
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
//        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
//            carModel = (CarModel) getIntent().getSerializableExtra(FijiRentalUtils.CarDetailIntent);
//        }

        init();
    }

    public void init() {

        HRDatabase = new HRDatabase(this);

        ll_about = findViewById(R.id.ll_about);
        iv_back = findViewById(R.id.iv_back);
        customPrice = findViewById(R.id.custom_price);
        rlDiscount = findViewById(R.id.rl_discount);
        customEditPrice = findViewById(R.id.custom_editprice);
        discountMsg = findViewById(R.id.tv_discount_msg);
        rlDiscount.setOnClickListener(this);
        customEditPrice.setOnClickListener(this);

        customPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(runnable);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("$")) {
                    customPrice.setText("$");
                    Selection.setSelection(customPrice.getText(), customPrice.getText().length());
                    handler.postDelayed(runnable, 1000);

                }

                if (customPrice.getText().toString() != null && customPrice.getText().toString().length() > 0 && !customPrice.getText().toString().equals("") && !customPrice.getText().toString().equals("null") && !customPrice.getText().toString().equals("$")) {
                    handler.postDelayed(runnable, 1000);
                }

            }
        });


        ll_about.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carModel = Paper.book().read(FijiRentalUtils.carModel);
        if (carModel != null) {

            FijiRentalUtils.Logger("TAGS","val "+(carModel.getCarprice()!=null));

            if(carModel.getCarprice()!=null){
                if(carModel.getCarprice().equals("null") || TextUtils.isEmpty(carModel.getCarprice())){
                    customPrice.setText("$ "+HRDateUtil.DailyPrice);
                }else {
                    customPrice.setText("$" + String.valueOf((int)Double.parseDouble(carModel.getCarprice())));
                }
            }


            int day3Discount = 0, day7Discount = 0, day30Discount = 0;
            if (carModel.get3extraDayDiscount() != null) {
                day3Discount = ((int) Double.parseDouble(carModel.get3extraDayDiscount()));
            }
            if (carModel.get7extraDayDiscount() != null) {
                day7Discount = ((int) Double.parseDouble(carModel.get7extraDayDiscount()));
            }
            if (carModel.get30extraDayDiscount() != null) {
                day30Discount = ((int) Double.parseDouble(carModel.get30extraDayDiscount()));
            }

            discountMsg.setText("3+ day ("+day3Discount+"%), 7+ day ("+day7Discount+"%), 30+ day ("+day30Discount+"%)");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_about:
                Intent i =new Intent(this,Pricing_Details.class);
                startActivity(i);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_discount:
                Intent discountIntent = new Intent(this, DiscountActivity.class);
                discountIntent.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
                startActivity(discountIntent);
                break;
            case R.id.custom_editprice:
                Intent editPriceIntent = new Intent(this, ActivityVerticalCalendar.class);
                startActivity(editPriceIntent);
                break;
        }
    }

    private void updateStatus() {
        apiService = Config.getClient().create(APIService.class);
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        reqBuilder.addFormDataPart("carprice", customPrice.getText().toString().replace("$", ""));
        List<DBModel> model = new ArrayList<>();
        model.addAll(HRDatabase.getAllDate());

        FijiRentalUtils.Logger("TAGS","dara "+model.size());
        for(int i=0;i<model.size();i++){
            DBModel dbModel=model.get(i);
            reqBuilder.addFormDataPart("custom_price["+i+"][date]", dbModel.getDate());
            reqBuilder.addFormDataPart("custom_price["+i+"][price]", dbModel.getTitle());

        }


        body = reqBuilder
                .build();

        Call<ResponseBody> call = apiService.setCarPrice(FijiRentalUtils.getAccessToken(this), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);

                        if (jsonobject.optString("code").matches("200")) {
                            updateCarModel();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ManualPricingActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ManualPricingActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());


            }
        });
    }


        public void updateCarModel(){
            apiService = Config.getClient().create(APIService.class);
            HashMap<String, String> data = new HashMap<>();
            data.put("item_id", carModel.getItemId());
            Call<ResponseBody> call = apiService.getCarDetails(FijiRentalUtils.getAccessToken(this), data);

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
                                JSONObject data = jsonobject.optJSONObject("data");
                                JSONObject object = data.optJSONObject("data");
                                FijiRentalUtils.updateCarModel(object, ManualPricingActivity.this);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ManualPricingActivity.this, "0");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ManualPricingActivity.this, "0");
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {


                }
            });
        }

}