package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Model.ListingCarModel;

public class MaximumTripActivity extends AppCompatActivity {

    RadioGroup maxTripgroup;
    ImageView ivBack;
    TextView tv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maximum_trip);

        maxTripgroup = findViewById(R.id.max_trip_group);
        tv_submit = findViewById(R.id.tv_continue);

        maxTripgroup.clearCheck();
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
                if (maxTripgroup.getCheckedRadioButtonId() == -1 ) {
                    Toast.makeText(MaximumTripActivity.this, "Please select a response for each question to continue.", Toast.LENGTH_SHORT).show();
                } else {

                    RadioButton noticeRadio= (RadioButton)findViewById(maxTripgroup.getCheckedRadioButtonId());


                    ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
                    listingCarModel.setMax_trip_duration(noticeRadio.getTag().toString());
                    ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);

                    Intent advanceNotice=new Intent(MaximumTripActivity.this,CarsFeatureList.class);
                    startActivity(advanceNotice);

                }
            }
        });
    }
}