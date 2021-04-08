package com.app.fijirentalcars;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fijirentalcars.Adapter.AirportAdapter;
import com.app.fijirentalcars.Model.AirportLocationModel;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.ExtraItemModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirportActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView addBtn, backBtn;
    TextView tv_done;
    ArrayList<AirportLocationModel> airportLocationModelArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    APIService apiService;
    CarModel carModel;
    LinearLayout emptyView;
    RecyclerView recyclerView;
    AirportAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport);
        Window window = this.getWindow();
        Log.e("AirportActivity", "AirportActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));


        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewairport);
        emptyView = findViewById(R.id.emptyView);

        adapter = new AirportAdapter(this, airportLocationModelArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
        carModel = Paper.book().read(FijiRentalUtils.carModel);
        getAirportList();

//        }


        tv_done = findViewById(R.id.tv_done);
        backBtn = findViewById(R.id.cancel_button);
        addBtn = findViewById(R.id.floating_action);

        tv_done.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.tv_done:

                FijiRentalUtils.Logger("TAGS", "data model " + adapter.getAirportList());
                updateAirportList();
//                finish();

                break;
            case R.id.cancel_button:
                finish();

                break;
            case R.id.floating_action:
                finish();

                break;
        }
    }

    private void updateAirportList() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        HashMap<String, String> data = new HashMap<>();
        data.put("item_id", carModel.getItemId());

        ArrayList<AirportLocationModel> selectedList = adapter.getAirportList();

        if (selectedList.size() > 0) {
            for (int i = 0; i < selectedList.size(); i++) {
                data.put("selected_ids[" + i + "]", String.valueOf(selectedList.get(i).getLocationId()));
                data.put("selected_price[" + i + "]", String.valueOf(selectedList.get(i).getPrice()));

            }
        }

        FijiRentalUtils.Logger("TAGS", "payload update Airport " + data);


        apiService.updateAirportList(FijiRentalUtils.getAccessToken(this), data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;

                    jsonobject = new JSONObject(jstr);
                    if (jsonobject.optString("code").matches("200")) {
                        JSONObject data = jsonobject.optJSONObject("data");
                        JSONArray data_array = data.optJSONArray("data");
                        FijiRentalUtils.isUpdateAirportList = true;
                        finish();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, AirportActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void getAirportList() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        HashMap<String, String> data = new HashMap<>();
        data.put("item_id", carModel.getItemId());
        apiService.getNearestAirportList(FijiRentalUtils.getAccessToken(this), data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;

                    jsonobject = new JSONObject(jstr);
                    if (jsonobject.optString("code").matches("200")) {
                        JSONObject data = jsonobject.optJSONObject("data");
                        JSONArray data_array = data.optJSONArray("data");
                        passdata(data_array);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, AirportActivity.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void passdata(JSONArray data_array) {
        airportLocationModelArrayList.clear();

        if (data_array.length() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < data_array.length(); i++) {
            JSONObject object = data_array.optJSONObject(i);
            AirportLocationModel extraItemModel = new AirportLocationModel();

            extraItemModel.setLocationId(object.optInt("location_id"));
            extraItemModel.setLocationName(object.optString("location_name"));
            extraItemModel.setSelected(object.optInt("selected"));
            extraItemModel.setPrice(object.optString("price"));
            airportLocationModelArrayList.add(extraItemModel);
        }

        adapter.notifyDataSetChanged();

    }
}