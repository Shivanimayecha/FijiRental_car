package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.util.FijiRentalUtils;

public class CarEligible extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView finishLater,nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_eligible);
        ivBack=findViewById(R.id.iv_back);
        finishLater=findViewById(R.id.finsh_btn);
        nextBtn=findViewById(R.id.tv_next);

        ivBack.setOnClickListener(this);
        finishLater.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_back){
            onBackPressed();
        }else if(v.getId()==R.id.finsh_btn){
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(FijiRentalUtils.CAR_FLAW,true);
            startActivity(i);
        }else if(v.getId()==R.id.tv_next){
            Intent i = new Intent(this, YourGoals.class);
            i.putExtra(FijiRentalUtils.CAR_FLAW,true);
            startActivity(i);
        }
    }
}