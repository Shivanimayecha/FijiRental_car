package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.util.FijiRentalUtils;

public class CarDescription extends AppCompatActivity {

    LinearLayout cardDescBg;
    ImageView ivBack;
    TextView tv_submit,finishLater;
    EditText carDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_description);

        tv_submit = findViewById(R.id.tv_continue);
        carDesc=findViewById(R.id.car_desc);

        ivBack = findViewById(R.id.iv_back);
        finishLater=findViewById(R.id.finsh_btn);
        finishLater.setVisibility(View.GONE);
        finishLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] words = carDesc.getText().toString().split(" ");

                if(words.length<25){
                    Toast.makeText(CarDescription.this, "Minimum 25 words required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent(CarDescription.this, MainActivity.class);
                i.putExtra(FijiRentalUtils.CAR_FLAW,true);
                startActivity(i);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] words = carDesc.getText().toString().split(" ");

                if(words.length<25){
                    Toast.makeText(CarDescription.this, "Minimum 25 words required.", Toast.LENGTH_SHORT).show();
                    return;
                }




                ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
                listingCarModel.setCarDescription(carDesc.getText().toString());

                ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);

                Intent advanceNotice=new Intent(CarDescription.this,NumberPlate.class);
                startActivity(advanceNotice);


            }
        });
    }
}