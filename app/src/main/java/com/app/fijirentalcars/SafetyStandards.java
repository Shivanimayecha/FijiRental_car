package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Model.ListingCarModel;

public class SafetyStandards extends AppCompatActivity {

    ImageView ivBack;
    TextView tv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_standards);

        tv_submit = findViewById(R.id.tv_next);

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

                    Intent advanceNotice=new Intent(SafetyStandards.this,SubmitListing.class);
                    startActivity(advanceNotice);


            }
        });

    }
}