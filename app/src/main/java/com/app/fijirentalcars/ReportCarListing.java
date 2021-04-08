package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RadioGroup;

public class ReportCarListing extends AppCompatActivity {

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_car_listing);
        radioGroup=findViewById(R.id.report_group);
        radioGroup.clearCheck();


    }
}