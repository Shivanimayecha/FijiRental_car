package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Adapter.ExtraAdapter;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.ExtraItemModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtrasList extends AppCompatActivity implements View.OnClickListener, ExtraAdapter.UpdateExtra {
    ImageView iv_back, addBtn;
    ExtraAdapter adapter;
    RecyclerView extraListView;
    APIService apiService;
    ProgressDialog progressDialog;
    LinearLayout emptyView;
    CarModel carModel;
    static boolean isDataChanged = false;
    private static final int PAGE_START = 1;
    List<ExtraItemModel> extraItemModelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extras_list);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        iv_back = findViewById(R.id.iv_back);
        addBtn = findViewById(R.id.save_btn);
        extraListView = findViewById(R.id.extralist);
        emptyView = findViewById(R.id.emptyView);

        extraListView.setLayoutManager(new LinearLayoutManager(this));
        carModel = Paper.book().read(FijiRentalUtils.carModel);
        getExtraList();

        iv_back.setOnClickListener(this);
        addBtn.setOnClickListener(this);

        adapter = new ExtraAdapter(this, extraItemModelList, this);
        extraListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isDataChanged) {
            isDataChanged = false;
            getExtraList();
        }
    }

    private void getExtraList() {
        apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        HashMap<String, String> data = new HashMap<>();
        data.put("item_id", carModel.getItemId());
        apiService.listExtras(FijiRentalUtils.getAccessToken(this), data).enqueue(new Callback<ResponseBody>() {
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
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ExtrasList.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void passdata(JSONArray data_array) {
        extraItemModelList.clear();

        if (data_array.length() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            extraListView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            extraListView.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < data_array.length(); i++) {
            JSONObject object = data_array.optJSONObject(i);
            ExtraItemModel extraItemModel = new ExtraItemModel();

            extraItemModel.setExtraId(object.optInt("extra_id"));
            extraItemModel.setExtraSku(object.optString("extra_sku"));
            extraItemModel.setPartnerId(object.optInt("partner_id"));
            extraItemModel.setItemId(object.optInt("item_id"));
            extraItemModel.setExtraName(object.optString("extra_name"));
            extraItemModel.setPrice(object.optString("price"));
            extraItemModel.setPriceType(object.optInt("price_type"));
            extraItemModel.setFixedRentalDeposit(object.optString("fixed_rental_deposit"));
            extraItemModel.setUnitsInStock(object.optInt("units_in_stock"));
            extraItemModel.setMaxUnitsPerBooking(object.optInt("max_units_per_booking"));
            extraItemModel.setOptionsDisplayMode(object.optString("options_display_mode"));
            extraItemModel.setOptionsMeasurementUnit(object.optString("options_measurement_unit"));
            extraItemModel.setBlogId(object.optInt("blog_id"));
            extraItemModel.setExtraMainId(object.optInt("extra_main_id"));
            extraItemModel.setStatus(object.optInt("status"));
            extraItemModel.setDescription(object.optString("description"));

            extraItemModelList.add(extraItemModel);
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.save_btn) {
            Intent guestI = new Intent(this, AddExtras.class);
            startActivity(guestI);
        }
    }


    @Override
    public void updateExtra(int position, boolean isCheked, boolean isUpdate) {

        if (isUpdate) {
            Intent extraIntent = new Intent(this, AddExtras.class);
            extraIntent.putExtra(FijiRentalUtils.EXTRA_MODEL, extraItemModelList.get(position));
            extraIntent.putExtra("ISEDIT", true);
            extraIntent.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
            startActivity(extraIntent);
            return;
        }

        apiService = Config.getClient().create(APIService.class);
        progressDialog.show();

        ExtraItemModel extraItemModel = extraItemModelList.get(position);

        HashMap<String, String> data = new HashMap<>();
        data.put("extra_id", String.valueOf(extraItemModel.getExtraId()));
        data.put("item_id", carModel.getItemId());
        data.put("partner_id", carModel.getPartnerId());
        data.put("quantity", String.valueOf(extraItemModel.getUnitsInStock()));
        data.put("price", extraItemModel.getPrice());
        data.put("id", String.valueOf(extraItemModel.getExtraMainId()));
        if (isCheked) {
            data.put("status", "1");
        } else {
            data.put("status", "0");
        }


        apiService.addExtras(FijiRentalUtils.getAccessToken(this), data).enqueue(new Callback<ResponseBody>() {
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
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ExtrasList.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


    }

    private void updateData(JSONArray data_array, int position) {

//        for (int i = 0; i < data_array.length(); i++) {
        JSONObject object = data_array.optJSONObject(0);
        ExtraItemModel extraItemModel = new ExtraItemModel();

        extraItemModel.setExtraId(object.optInt("extra_id"));
        extraItemModel.setExtraSku(object.optString("extra_sku"));
        extraItemModel.setPartnerId(object.optInt("partner_id"));
        extraItemModel.setItemId(object.optInt("item_id"));
        extraItemModel.setExtraName(object.optString("extra_name"));
        extraItemModel.setPrice(object.optString("price"));
        extraItemModel.setPriceType(object.optInt("price_type"));
        extraItemModel.setFixedRentalDeposit(object.optString("fixed_rental_deposit"));
        extraItemModel.setUnitsInStock(object.optInt("units_in_stock"));
        extraItemModel.setMaxUnitsPerBooking(object.optInt("max_units_per_booking"));
        extraItemModel.setOptionsDisplayMode(object.optString("options_display_mode"));
        extraItemModel.setOptionsMeasurementUnit(object.optString("options_measurement_unit"));
        extraItemModel.setBlogId(object.optInt("blog_id"));
        extraItemModel.setExtraMainId(object.optInt("extra_main_id"));
        extraItemModel.setStatus(object.optInt("status"));
        extraItemModel.setDescription(object.optString("description"));
//        }

        extraItemModelList.set(position, extraItemModel);
        adapter.notifyDataSetChanged();


    }
}