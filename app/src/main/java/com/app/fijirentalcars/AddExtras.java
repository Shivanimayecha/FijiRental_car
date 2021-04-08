package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.CustomViews.CustomButtonDialog;
import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.ExtraItem;
import com.app.fijirentalcars.Model.ExtraItemModel;
import com.app.fijirentalcars.Model.TransmissionType;
import com.app.fijirentalcars.listners.DialogItemListner;
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

public class AddExtras extends AppCompatActivity implements View.OnClickListener, DialogItemListner {
    private static final int EXTRA_SELECTION = 200;
    TextView saveBtn, selectExtra;
    private CustomDialog customDialog;
    CarModel carModel;
    ExtraItemModel itemModel;
    EditText extraPrice, extraQuantity, priceCircle, extraInstruction;
    ProgressDialog progressDialog;
    boolean isEdit = false;
    List val;
    ExtraItem item;
    List QuantityVal = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extras);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        val = new ArrayList();
        val.add("Price per day");
        val.add("Price per trip");

        saveBtn = findViewById(R.id.save_btn);
        selectExtra = findViewById(R.id.select_extra);
        extraQuantity = findViewById(R.id.extra_quantity);
        extraInstruction = findViewById(R.id.extra_instruction);
        extraPrice = findViewById(R.id.extra_price);
        priceCircle = findViewById(R.id.price_circle);
        saveBtn.setOnClickListener(this);
        selectExtra.setOnClickListener(this);
        priceCircle.setOnClickListener(this);
        extraQuantity.setOnClickListener(this);
        QuantityVal.clear();

        carModel = Paper.book().read(FijiRentalUtils.carModel);


        updateView();
//        Selection.setSelection(extraPrice.getText(), extraPrice.getText().length());


        extraPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("US$ ")) {
                    extraPrice.setText("US$ " + s);
                    Selection.setSelection(extraPrice.getText(), extraPrice.getText().length());

                }

            }
        });

        for (int i = 0; i < 10; i++) {
            QuantityVal.add(String.valueOf(i + 1));
        }

        if (getIntent().hasExtra("ISEDIT")) {
            isEdit = getIntent().getBooleanExtra("ISEDIT", false);
            itemModel = (ExtraItemModel) getIntent().getSerializableExtra(FijiRentalUtils.EXTRA_MODEL);


            UpdateDate();
        }
    }

    private void UpdateDate() {
        selectExtra.setText(itemModel.getExtraName());
        extraQuantity.setText(String.valueOf(itemModel.getUnitsInStock()));
        extraPrice.setText("US$ " + (int) Double.parseDouble(itemModel.getPrice()));
        priceCircle.setText((String) val.get(itemModel.getPriceType()));
        extraInstruction.setText(itemModel.getDescription());
        extraQuantity.setEnabled(true);
        selectExtra.setEnabled(false);
    }

    private void updateView() {
        if (TextUtils.isEmpty(selectExtra.getText().toString())) {
            extraQuantity.setEnabled(false);
            return;
        }
        extraQuantity.setText(QuantityVal.get(0).toString());
        extraQuantity.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save_btn) {
            if (TextUtils.isEmpty(selectExtra.getText().toString())) {
                selectExtra.requestFocus();
                FijiRentalUtils.ShowValidation("Invalid type", this, "");
                return;
            }

            if (TextUtils.isEmpty(extraPrice.getText().toString())) {
                extraPrice.requestFocus();
                extraPrice.setError("Invalid Value");
                return;
            }

            if (TextUtils.isEmpty(extraPrice.getText().toString().replace("US$ ", ""))) {
                extraPrice.requestFocus();
                extraPrice.setError("Invalid Value");
                return;
            }

            addExtra();
        } else if (v.getId() == R.id.extra_quantity) {
            customDialog = new CustomDialog(this, QuantityVal, this);

            customDialog.show(getSupportFragmentManager(), "QUANTITY");
        } else if (v.getId() == R.id.price_circle) {


            customDialog = new CustomDialog(this, val, this);

            customDialog.show(getSupportFragmentManager(), "PRICECAL");
        } else if (v.getId() == R.id.select_extra) {
            Intent guestI = new Intent(this, ExtraListAdd.class);
            startActivityForResult(guestI, EXTRA_SELECTION);
        }
    }

    private void addExtra() {
        APIService apiService = Config.getClient().create(APIService.class);
        progressDialog.show();


        HashMap<String, String> data = new HashMap<>();
        data.put("item_id", carModel.getItemId());
        data.put("partner_id", carModel.getPartnerId());
        data.put("quantity", String.valueOf(extraQuantity.getText().toString()));
        data.put("price", extraPrice.getText().toString().replace("US$ ", ""));
        if (isEdit) {
            data.put("id", String.valueOf(itemModel.getExtraMainId()));
        } else {
            data.put("id", String.valueOf(item.getId()));
        }
        data.put("price_type", getPricePostion());
        data.put("description", extraInstruction.getText().toString());

        data.put("status", "1");
        if (isEdit) {
            data.put("extra_id", String.valueOf(itemModel.getExtraId()));
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
                        ExtrasList.isDataChanged = true;

                        finish();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, AddExtras.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private String getPricePostion() {
        String pos = "0";
        for (int i = 0; i < val.size(); i++) {
            if (priceCircle.getText().toString().equals(val.get(i))) {
                pos = String.valueOf(i);
            }
        }
        return pos;
    }

    @Override
    public void onItemClick(Object val) {

        if (customDialog.getTag().equals("QUANTITY")) {

            extraQuantity.setText(String.valueOf(val));
        }

        if (customDialog.getTag().equals("PRICECAL")) {
            priceCircle.setText(String.valueOf(val));
        }

        customDialog.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXTRA_SELECTION && resultCode == RESULT_OK) {

            item = (ExtraItem) data.getSerializableExtra(FijiRentalUtils.EXTRA_ITEAM);
            FijiRentalUtils.Logger("TAGS", "data " + item.toString());
//            Toast.makeText(this, "name "+item.getExtraName(), Toast.LENGTH_SHORT).show();

            selectExtra.setText(item.getExtraName());
            priceCircle.setText("Price per trip");
            updateView();
        }
    }
}