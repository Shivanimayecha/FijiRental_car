package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.Adapter.GenericAdapter;
import com.app.fijirentalcars.Model.ExtraItem;
import com.app.fijirentalcars.Model.ExtraItemModel;
import com.app.fijirentalcars.listners.DialogItemListner;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtraListAdd extends AppCompatActivity implements View.OnClickListener, DialogItemListner {

    ImageView iv_back;
    ProgressDialog progressDialog;
    EditText extraName;
    List extraItemModelList = new ArrayList<>();
    RecyclerView extraItemListView;
    TextView itemHeading;
    GenericAdapter adapter;
    TextView emptyView;

    long delay = 1000;
    long last_text_edit = 0;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_list_add);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        iv_back = findViewById(R.id.iv_back);
        extraName = findViewById(R.id.extras_name);
        emptyView = findViewById(R.id.emptyView);
        extraItemListView = findViewById(R.id.extra_list);
        itemHeading = findViewById(R.id.heading);

        adapter = new GenericAdapter(this, extraItemModelList, this) {
            @Override
            public void selectProduct(int position) {

            }
        };

        extraItemListView.setLayoutManager(new LinearLayoutManager(this));
        extraItemListView.setAdapter(adapter);

        extraName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
//                }
            }
        });

        iv_back.setOnClickListener(this);
        getExtraList("");
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {

                getExtraList(extraName.getText().toString());
            }
        }
    };

    private void getExtraList(String s) {

        if (TextUtils.isEmpty(extraName.getText().toString())) {
            itemHeading.setVisibility(View.VISIBLE);
        } else {
            itemHeading.setVisibility(View.GONE);
        }

        APIService apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        HashMap<String, String> data = new HashMap<>();
        data.put("keyword", s);
        apiService.extraTypes(FijiRentalUtils.getAccessToken(this), data).enqueue(new Callback<ResponseBody>() {
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
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, ExtraListAdd.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void passdata(JSONArray data_array) {

        if (data_array.length() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            extraItemListView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            extraItemListView.setVisibility(View.VISIBLE);
        }
        extraItemModelList.clear();

        for (int i = 0; i < data_array.length(); i++) {
            JSONObject object = data_array.optJSONObject(i);
            ExtraItem extraItemModel = new ExtraItem();
            extraItemModel.setExtraName(object.optString("extra_name"));
            extraItemModel.setId(object.optString("id"));

            extraItemModelList.add(extraItemModel);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    @Override
    public void onItemClick(Object val) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(FijiRentalUtils.EXTRA_ITEAM, (ExtraItem) val);
        setResult(RESULT_OK, resultIntent);
        finish();

    }
}