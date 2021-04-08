package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.fijirentalcars.Adapter.SpinnerAdapter;
import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.SpinnerModel;
import com.app.fijirentalcars.util.FijiRentalUtils;

public class EditGuestActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatSpinner distanceSpinner;
    SeekBar priceBar;
    AppCompatCheckBox guestCheckBox;
    public TextView price, priceView, saveBtn;
    ImageView backBtn;
    SpinnerModel spinnerModel;
    SpinnerAdapter adapter;
    CarModel carModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_guest);

        distanceSpinner = findViewById(R.id.spinner_delivery);
        priceBar = findViewById(R.id.price_bar);
        price = findViewById(R.id.tv_price);
        priceView = findViewById(R.id.tv_rate1);
        saveBtn = findViewById(R.id.tv_continue);
        backBtn = findViewById(R.id.iv_back);
        guestCheckBox = findViewById(R.id.iv_toggle1);

        guestCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    priceBar.setEnabled(true);
                    distanceSpinner.setEnabled(true);
                } else {

                    priceBar.setEnabled(false);
                    distanceSpinner.setEnabled(false);
                }
            }
        });


        adapter = new SpinnerAdapter(this,
                R.layout.custom_spinner_dropdown_item, FijiRentalUtils.modelList);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        distanceSpinner.setAdapter(adapter);

        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerModel = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        priceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    price.setText("Free");
                    priceView.setText(" delivery");

                } else {

                    int stepSize = 5;

                    progress = (progress / stepSize) * stepSize;
                    seekBar.setProgress(progress);

                    price.setText("$" + progress);
                    priceView.setText(" delivery fee");

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);


        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
            carModel = (CarModel) getIntent().getSerializableExtra(FijiRentalUtils.CarDetailIntent);

            if(carModel.getGuestChoosenLocation().equalsIgnoreCase("1")){
                guestCheckBox.setChecked(true);
                priceBar.setEnabled(true);
                if(carModel.getGuestChoosenLocationFee()!=null){
                    priceBar.setProgress((((int)Double.parseDouble(carModel.getGuestChoosenLocationFee()))));
                }

                for(int i=0;i<FijiRentalUtils.modelList.length;i++){
                    SpinnerModel demoModel=FijiRentalUtils.modelList[i];

                    if(demoModel.getVlaue().equalsIgnoreCase(carModel.getGuestChoosenUpToMiles())){
                        distanceSpinner.setSelection(i);
                    }
                }
                distanceSpinner.setEnabled(true);
            }else {
                guestCheckBox.setChecked(false);
                priceBar.setEnabled(false);
                distanceSpinner.setEnabled(false);
            }

        }

    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_continue) {

            Intent returnIntent = new Intent();
            if(guestCheckBox.isChecked()){
                returnIntent.putExtra("result","1");
                returnIntent.putExtra("distance",spinnerModel.getVlaue());
                returnIntent.putExtra("distance_name",spinnerModel.getName());
                if(priceBar.getProgress()==0){
                    returnIntent.putExtra("price","Free");
                }else {
                    returnIntent.putExtra("price",String.valueOf(priceBar.getProgress()));
                }

            }else {
                returnIntent.putExtra("result","0");
            }

            setResult(Activity.RESULT_OK,returnIntent);
            finish();


        } else if (v.getId() == R.id.iv_back) {
            finish();
        }
    }
}