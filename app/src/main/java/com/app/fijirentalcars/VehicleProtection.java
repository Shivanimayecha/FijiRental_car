package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.InsuranceModel;
import com.app.fijirentalcars.fragments.InsurancePlanSelect;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleProtection extends AppCompatActivity implements View.OnClickListener {

    TextView declineProtection,viewHost,changeInsurance,leagalInfo;
    ImageView ivBack;
    APIService apiService;
    ProgressDialog progressDialog;

    LinearLayout lossIncomeLayout,courtesyLayout;
    public static ArrayList<InsuranceModel> insuranceList=new ArrayList<>();
    CarModel carModel;
    public static InsuranceModel model;
    TextView planName,tripPrice,liability,damageCost,deductible,lossIncome,courtesyCar,postContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_protection);

        carModel = Paper.book().read(FijiRentalUtils.carModel);

        declineProtection=findViewById(R.id.decline_prot);
        ivBack=findViewById(R.id.iv_back);
        viewHost=findViewById(R.id.view_host);
        changeInsurance=findViewById(R.id.change_protection);

        lossIncomeLayout=findViewById(R.id.income_loss_lay);
        courtesyLayout=findViewById(R.id.courtesy_lay);

        planName=findViewById(R.id.plan_name);
        tripPrice=findViewById(R.id.take_trip_price);
        liability=findViewById(R.id.take_liability);
        damageCost=findViewById(R.id.take_dedctible_damage);
        deductible=findViewById(R.id.deductible);
        lossIncome=findViewById(R.id.income_loss);
        courtesyCar=findViewById(R.id.courtesy);
        postContent=findViewById(R.id.post_content);
        leagalInfo=findViewById(R.id.view_leagal_info);

        declineProtection.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        changeInsurance.setOnClickListener(this);
        viewHost.setOnClickListener(this);
        leagalInfo.setOnClickListener(this);
        getInsurancePlan();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.decline_prot){
            Intent i=new Intent(this,DeclineProtection.class);
            startActivity(i);
        }else  if(v.getId()==R.id.iv_back){
            finish();
        }else  if(v.getId()==R.id.view_host){
            String url = "https://fijirentalcars.siddhidevelopment.com/360001938527-detailed-explanation-of-host-protection-plans-uk/";

            if(model!=null){
                url=model.getLearnMore();
            }

            CustomTabsIntent tabsIntent1 = new CustomTabsIntent.Builder().build();
            tabsIntent1.intent.setPackage("com.android.chrome");
            tabsIntent1.launchUrl(this, Uri.parse(url));
        }else  if(v.getId()==R.id.view_leagal_info){
            String url = "https://fijirentalcars.siddhidevelopment.com/help/";

            if(model!=null){
                url=model.getMoreOptions();
            }

            CustomTabsIntent tabsIntent1 = new CustomTabsIntent.Builder().build();
            tabsIntent1.intent.setPackage("com.android.chrome");
            tabsIntent1.launchUrl(this, Uri.parse(url));
        }else if(v.getId()==R.id.change_protection){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new InsurancePlanSelect());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void getInsurancePlan() {
        apiService = Config.getClient().create(APIService.class);
        progressDialog = new ProgressDialog(VehicleProtection.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        progressDialog.show();
        Call<ResponseBody> call = apiService.getInsuranceplan(FijiRentalUtils.getAccessToken(VehicleProtection.this));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        if (jsonobject.optString("code").matches("200")) {
                            JSONObject data = jsonobject.optJSONObject("data");
                            JSONArray data_array = data.optJSONArray("data");
                            passdata(data_array);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, VehicleProtection.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, VehicleProtection.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    private void passdata(JSONArray jsonArray) {
        insuranceList.clear();

        for(int i=0;i<jsonArray.length();i++){
            JSONObject modelObject=jsonArray.optJSONObject(i);
            InsuranceModel insuranceModel=new InsuranceModel();
            insuranceModel.setId(modelObject.optInt("id"));
            insuranceModel.setPostContent(modelObject.optString("post_content"));
            insuranceModel.setPostTitle(modelObject.optString("post_title"));
            insuranceModel.setHostTake(modelObject.optString("host_take"));
            insuranceModel.setDeductible(modelObject.optString("deductible"));
            insuranceModel.setLossOfHostingIncome(modelObject.optString("loss_of_hosting_income"));
            insuranceModel.setLiabilityCoverage(modelObject.optString("liability_coverage"));
            insuranceModel.setCourtesyCar(modelObject.optString("courtesy_car"));
            insuranceModel.setLearnMore(modelObject.optString("learn_more"));
            insuranceModel.setMoreOptions(modelObject.optString("more_options"));
            insuranceList.add(insuranceModel);
        }

        updateView();

    }

    public void updateView(){
        carModel=Paper.book().read(FijiRentalUtils.carModel);
        if(carModel.getVehicle_protection().equalsIgnoreCase("0")){
            InsuranceModel insuranceModel=insuranceList.get(0);
            updatView(insuranceModel);
        }else {
            for(int i=0;i<insuranceList.size();i++){
                if(insuranceList.get(i).getId().toString().equalsIgnoreCase(carModel.getVehicle_protection())){
                    InsuranceModel insuranceModel=insuranceList.get(i);
                    updatView(insuranceModel);
                }
            }
        }
    }

    private void updatView(InsuranceModel insuranceModel) {
        model=insuranceModel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            postContent.setText(Html.fromHtml(insuranceModel.getPostContent(),Html.FROM_HTML_MODE_COMPACT));
        }else {
            postContent.setText(Html.fromHtml(insuranceModel.getPostContent()));
        }

        planName.setText(insuranceModel.getPostTitle());
    }
}