package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Adapter.CarFeatureAdapter;
import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.SQLDB.DBHandler_Feature;
import com.app.fijirentalcars.util.FijiRentalUtils;

import java.util.ArrayList;
import java.util.List;

public class CarsFeatureList extends AppCompatActivity {

    private DBHandler_Feature dbHandler_feature;
    List<FutureModel> modelList=new ArrayList<>();
    CarFeatureAdapter carFeatureAdapter;
    RecyclerView recyclerView;
    ImageView ivBack;
    TextView tv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_feature_list);

        dbHandler_feature = new DBHandler_Feature(CarsFeatureList.this);
        recyclerView=findViewById(R.id.feature_list);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        modelList.clear();
        modelList.addAll(dbHandler_feature.getAllJobs());
        ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
        carFeatureAdapter=new CarFeatureAdapter(this,modelList,listingCarModel);
        recyclerView.setAdapter(carFeatureAdapter);

        tv_submit = findViewById(R.id.tv_continue);

        ivBack = findViewById(R.id.iv_back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
                    listingCarModel.setCarFeatures(carFeatureAdapter.getFeatured());
                    ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);

                    Intent advanceNotice=new Intent(CarsFeatureList.this,CarDescription.class);
                    startActivity(advanceNotice);


            }
        });

    }
}