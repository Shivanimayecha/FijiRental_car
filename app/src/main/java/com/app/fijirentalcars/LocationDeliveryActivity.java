package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.fijirentalcars.Adapter.SelectedAirPortAdapter;
import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.AirportLocationModel;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.SpinnerModel;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationDeliveryActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner {

    RelativeLayout tv_description, edit_guestLocation;
    ImageView iv_back;
    List itemList = new ArrayList();
    SpinnerModel selectedModel;
    CustomDialog customDialog;
    TextView editDeliverDiscount, deliveryDiscount, emptyView, homeLocation, editHome;
    ProgressDialog progressDialog;
    APIService apiService;
    AppCompatCheckBox gusestLocation;
    TextView guestPrice, guestDistance;
    RecyclerView selectedAirportList;
    CarModel carModel;
    String distanceName, distanceVal = "", price = "";
    ArrayList<AirportLocationModel> modelArrayList = new ArrayList<>();
    SelectedAirPortAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_delivery);
        Window window = this.getWindow();
        Log.e("LocationDeliveryActivit", "LocationDeliveryActivity");
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        init();

    }

    public void init() {
        tv_description = findViewById(R.id.tv_description);
        iv_back = findViewById(R.id.iv_back);
        editDeliverDiscount = findViewById(R.id.tv_edit_discount);
        deliveryDiscount = findViewById(R.id.tv_delivery_discount);
        edit_guestLocation = findViewById(R.id.guest_location);
        gusestLocation = findViewById(R.id.guest_locationCheckBox);
        homeLocation = findViewById(R.id.home_location);
        editHome = findViewById(R.id.edit_home);
        guestPrice = findViewById(R.id.tv_rate1);
        emptyView = findViewById(R.id.tv_discount);
        guestDistance = findViewById(R.id.guest_distance_val);
        selectedAirportList = findViewById(R.id.selected_airpot_list);
        selectedAirportList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectedAirPortAdapter(this, modelArrayList);

        selectedAirportList.setAdapter(adapter);

        getItemList();


        gusestLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                if(isChecked ){
//                    if(TextUtils.isEmpty(guestPrice.getText().toString())){
//                        Intent editGuest = new Intent(LocationDeliveryActivity.this, EditGuestActivity.class);
//                        if(carModel.getEnabled().equalsIgnoreCase("0")){
//                            carModel.setEnabled("1");
//                        }
//
//                        editGuest.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
//                        startActivityForResult(editGuest,FijiRentalUtils.UPDATE_LOCATION);
//                    }
//                }
                if (buttonView.isPressed()) {
                    updateCarModel();
                }


            }
        });

        tv_description.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        editDeliverDiscount.setOnClickListener(this);
        edit_guestLocation.setOnClickListener(this);
        editHome.setOnClickListener(this);
    }


    private void getItemList() {
        itemList.clear();

        SpinnerModel model1 = new SpinnerModel("", "Do not offer");
        SpinnerModel model2 = new SpinnerModel("2 days", "Trips of 2 days or longer");
        SpinnerModel model3 = new SpinnerModel("3 days", "Trips of 3 days or longer");
        SpinnerModel model4 = new SpinnerModel("4 days", "Trips of 4 days or longer");
        SpinnerModel model5 = new SpinnerModel("5 days", "Trips of 5 days or longer");
        SpinnerModel model6 = new SpinnerModel("6 days", "Trips of 6 days or longer");
        SpinnerModel model7 = new SpinnerModel("1 week", "Trips of 1 week or longer");
        SpinnerModel model8 = new SpinnerModel("2 weeks", "Trips of 2 weeks or longer");

        itemList.add(model1);
        itemList.add(model2);
        itemList.add(model3);
        itemList.add(model4);
        itemList.add(model5);
        itemList.add(model6);
        itemList.add(model7);
        itemList.add(model8);
    }


    private String getOfferVal(String name) {
        String offerName = "";

        FijiRentalUtils.Logger("TAGS", "val off " + name);

        if (name.equalsIgnoreCase("0.00") || name.equalsIgnoreCase("0.00")) {
            offerName = "Do not offer";
        } else {
            for (int i = 0; i < itemList.size(); i++) {
                SpinnerModel model = (SpinnerModel) itemList.get(i);
                if (model.getVlaue().equalsIgnoreCase(name)) {
                    offerName = model.getName();
                }
            }
        }


        return offerName;
    }

    @Override
    protected void onResume() {
        super.onResume();
        carModel = Paper.book().read(FijiRentalUtils.carModel);
        updateView(carModel);

        if (FijiRentalUtils.isUpdateAirportList) {
            updateCarModel();
            FijiRentalUtils.isUpdateAirportList = false;
        }
    }

    private void updateView(CarModel carModel) {
        if (carModel != null) {
            if (carModel.getGuestChoosenLocation().equalsIgnoreCase("1")) {
                gusestLocation.setChecked(true);

            } else {
                gusestLocation.setChecked(false);
            }
            if (carModel.getGuestChoosenLocationFee() != null) {
                if (((int) Double.parseDouble(carModel.getGuestChoosenLocationFee())) == 0) {
                    guestPrice.setText("Free");
                } else {
                    guestPrice.setText("$" + String.valueOf((int) Double.parseDouble(carModel.getGuestChoosenLocationFee())));
                }
            }

            if (carModel.getAirportList() == null || carModel.getAirportList().size() < 1) {
                selectedAirportList.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);

            } else {
                selectedAirportList.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                modelArrayList.clear();
                modelArrayList.addAll(carModel.getAirportList());
                adapter.notifyDataSetChanged();
            }

            homeLocation.setText(carModel.getModelCarLocation());


            guestDistance.setText(FijiRentalUtils.getDistanceNameFromVal(carModel.getGuestChoosenUpToMiles()));

            deliveryDiscount.setText(getOfferVal(carModel.getDeliveryDiscountOnDays()));

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_description:
                Intent i = new Intent(this, AirportActivity.class);
//                i.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
                startActivity(i);
                break;
            case R.id.guest_location:
                Intent editGuest = new Intent(this, EditGuestActivity.class);
                editGuest.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
                startActivityForResult(editGuest, FijiRentalUtils.UPDATE_LOCATION);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_edit_discount:
                customDialog = new CustomDialog(this, itemList, this);
                customDialog.show(getSupportFragmentManager(), "DELIVERY_DISCOUNT");
                break;
            case R.id.edit_home:
                Intent homeLoc=new Intent(this,HomeLocationChange.class);
                startActivity(homeLoc);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FijiRentalUtils.UPDATE_LOCATION) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");

                if (result.equals("0")) {
                    gusestLocation.setChecked(false);
                    guestPrice.setText("");
                    guestDistance.setText("");
                } else {

                    distanceName = data.getStringExtra("distance_name");
                    distanceVal = data.getStringExtra("distance");
                    price = data.getStringExtra("price");

                    gusestLocation.setChecked(true);
                    guestPrice.setText("$" + price);
                    guestDistance.setText(distanceName);
                }
                updateCarModel();
            }
        }
    }

    @Override
    public void onItemClick(Object val) {
        if (customDialog.getTag().equals("DELIVERY_DISCOUNT")) {
            selectedModel = (SpinnerModel) val;
            deliveryDiscount.setText(selectedModel.getName());
            updateCarModel();
        }
        customDialog.dismiss();
    }

    public void updateCarModel() {
        apiService = Config.getClient().create(APIService.class);
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());


        if (gusestLocation.isChecked()) {
            reqBuilder.addFormDataPart("guest_choosen_location", "1");
            if (TextUtils.isEmpty(price)) {
                reqBuilder.addFormDataPart("guest_choosen_location_fee", carModel.getGuestChoosenLocationFee());
            } else {
                reqBuilder.addFormDataPart("guest_choosen_location_fee", price);
            }

            if (TextUtils.isEmpty(distanceVal)) {
                reqBuilder.addFormDataPart("guest_choosen_up_to_miles", carModel.getGuestChoosenUpToMiles());
            } else {
                reqBuilder.addFormDataPart("guest_choosen_up_to_miles", distanceVal);
            }


            if (selectedModel != null) {
                reqBuilder.addFormDataPart("delivery_discount_on_days", selectedModel.getVlaue());
            }

        } else {
            reqBuilder.addFormDataPart("guest_choosen_location", "0");
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
                            JSONObject data = jsonobject.optJSONObject("data");
                            JSONObject object = data.optJSONObject("data");
                            FijiRentalUtils.updateCarModel(object, LocationDeliveryActivity.this);

                            carModel = Paper.book().read(FijiRentalUtils.carModel);
                            if (carModel.getAirportList() == null || carModel.getAirportList().size() < 1) {
                                selectedAirportList.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);

                            } else {
                                selectedAirportList.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                                modelArrayList.clear();
                                modelArrayList.addAll(carModel.getAirportList());
                                adapter.notifyDataSetChanged();
                            }
                            updateView(carModel);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, LocationDeliveryActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, LocationDeliveryActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }


}