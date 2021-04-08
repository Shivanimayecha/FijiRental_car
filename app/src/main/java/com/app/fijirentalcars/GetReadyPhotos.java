package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.util.FijiRentalUtils;

public class GetReadyPhotos extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ready_photos);

        ivBack=findViewById(R.id.iv_back);
        nextBtn=findViewById(R.id.tv_next);

        ivBack.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_back){
            onBackPressed();
        }else if(v.getId()==R.id.tv_next){
            Intent i = new Intent(this, PhotoActivity.class);
            i.putExtra(FijiRentalUtils.CAR_FLAW,true);
            startActivity(i);
        }
    }
}