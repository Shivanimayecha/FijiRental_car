package com.app.fijirentalcars.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.AddressActivity;
import com.app.fijirentalcars.DistanceInclude;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.InsuranceModel;
import com.app.fijirentalcars.R;
import com.app.fijirentalcars.VehicleProtection;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.fijirentalcars.VehicleProtection.insuranceList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsurancePlanSelect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsurancePlanSelect extends Fragment {



    ToggleSwitch toggleSwitch;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView ivBack;
    CarModel carModel;

    TextView hostTake,deductible,lossIncome,liability,courtesy,hostTakecurrent,deductiblecurrent,lossIncomecurrent,liabilitycurrent,courtesycurrent,submitBtn,leagalinfo;

    public InsurancePlanSelect() {
        // Required empty public constructor
    }

    public static InsurancePlanSelect newInstance() {
        InsurancePlanSelect fragment = new InsurancePlanSelect();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_insurance_plan_select, container, false);
        carModel = Paper.book().read(FijiRentalUtils.carModel);

        toggleSwitch = view.findViewById(R.id.switch_toggle);

        hostTake=view.findViewById(R.id.host_take);
        hostTakecurrent=view.findViewById(R.id.host_take_current);
        deductible=view.findViewById(R.id.deductible);
        deductiblecurrent=view.findViewById(R.id.deductible_current);
        lossIncome=view.findViewById(R.id.income_loss);
        lossIncomecurrent=view.findViewById(R.id.income_loss_current);
        liability=view.findViewById(R.id.liability);
        liabilitycurrent=view.findViewById(R.id.liability_current);
        courtesy=view.findViewById(R.id.courtesy);
        courtesycurrent=view.findViewById(R.id.courtesy_current);
        ivBack=view.findViewById(R.id.iv_back);
        submitBtn=view.findViewById(R.id.tv_next);
        leagalinfo=view.findViewById(R.id.view_leagal_info);

        leagalinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://fijirentalcars.siddhidevelopment.com/help/";
                CustomTabsIntent tabsIntent1 = new CustomTabsIntent.Builder().build();
                tabsIntent1.intent.setPackage("com.android.chrome");
                tabsIntent1.launchUrl(getContext(), Uri.parse(url));
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VehicleProtection)getContext()).onBackPressed();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCar();
            }
        });

        ArrayList<String> labels = new ArrayList<>();
        if(insuranceList.size()>0){
            for (InsuranceModel insuranceModel:insuranceList){
                labels.add(insuranceModel.getHostTake());
            }

            toggleSwitch.setLabels(labels);
        }


        if(carModel.getVehicle_protection().equalsIgnoreCase("0")){
            toggleSwitch.setCheckedTogglePosition(0);
        }else {
            for(int i=0;i<insuranceList.size();i++){
                if(insuranceList.get(i).getId().toString().equalsIgnoreCase(carModel.getVehicle_protection())){
                    toggleSwitch.setCheckedTogglePosition(i);

                    InsuranceModel insuranceModel=insuranceList.get(i);

                    hostTake.setText(insuranceModel.getHostTake()+"%");
                    deductible.setText("$"+insuranceModel.getDeductible());
                    lossIncome.setText(insuranceModel.getLossOfHostingIncome());
                    liability.setText("$"+insuranceModel.getLiabilityCoverage());
                    courtesy.setText(insuranceModel.getCourtesyCar());
                }
            }
        }

        if(carModel.getVehicle_protection().equalsIgnoreCase("0")){
            InsuranceModel insuranceModel=insuranceList.get(0);
            hostTakecurrent.setText(insuranceModel.getHostTake()+"%");
            deductiblecurrent.setText("$"+insuranceModel.getDeductible());
            lossIncomecurrent.setText(insuranceModel.getLossOfHostingIncome());
            liabilitycurrent.setText("$"+insuranceModel.getLiabilityCoverage());
            courtesycurrent.setText(insuranceModel.getCourtesyCar());
        }else {
            for(int i=0;i<insuranceList.size();i++){
                if(insuranceList.get(i).getId().toString().equalsIgnoreCase(carModel.getVehicle_protection())){
                    InsuranceModel insuranceModel=insuranceList.get(i);
                    hostTakecurrent.setText(insuranceModel.getHostTake()+"%");
                    deductiblecurrent.setText("$"+insuranceModel.getDeductible());
                    lossIncomecurrent.setText(insuranceModel.getLossOfHostingIncome());
                    liabilitycurrent.setText("$"+insuranceModel.getLiabilityCoverage());
                    courtesycurrent.setText(insuranceModel.getCourtesyCar());
                }
            }
        }


        toggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {

                InsuranceModel insuranceModel=insuranceList.get(position);

                hostTake.setText(insuranceModel.getHostTake()+"%");
                deductible.setText("$"+insuranceModel.getDeductible());
                lossIncome.setText(insuranceModel.getLossOfHostingIncome());
                liability.setText("$"+insuranceModel.getLiabilityCoverage());
                courtesy.setText(insuranceModel.getCourtesyCar());


            }
        });
        return view;
    }

    private float dp2px(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    private void updateCar() {



        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        APIService apiService = Config.getClient().create(APIService.class);
        progressDialog.show();
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        InsuranceModel insuranceModel=insuranceList.get(toggleSwitch.getCheckedTogglePosition());
        reqBuilder.addFormDataPart("vehicle_protection", String.valueOf(insuranceModel.getId()));


        body = reqBuilder
                .build();

        Call<ResponseBody> call = apiService.updateCarListing(FijiRentalUtils.getAccessToken(getContext()), body);
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
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            FijiRentalUtils.Logger("TAGS", "vin res " + jstr);
                            JSONObject dataObject = jsonobject.optJSONObject("data");
                            JSONObject object = dataObject.optJSONObject("data");
                            FijiRentalUtils.updateCarModel(object, getContext());
                            ((VehicleProtection)getContext()).updateView();
                            ((VehicleProtection)getContext()).onBackPressed();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getContext(), "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, getContext(), "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.Logger("TAGS", "exception " + t.getMessage());
                progressDialog.dismiss();

            }
        });

    }
}