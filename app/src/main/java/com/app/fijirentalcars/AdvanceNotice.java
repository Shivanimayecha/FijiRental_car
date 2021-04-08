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

public class AdvanceNotice extends AppCompatActivity {

    RadioGroup noticeGroup;
    ImageView ivBack;
    TextView tv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_notice);

        noticeGroup = findViewById(R.id.advance_notice_group);
        tv_submit = findViewById(R.id.tv_continue);

        noticeGroup.clearCheck();
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
                if (noticeGroup.getCheckedRadioButtonId() == -1 ) {
                    Toast.makeText(AdvanceNotice.this, "Please select a response for each question to continue.", Toast.LENGTH_SHORT).show();
                } else {

                    RadioButton noticeRadio= (RadioButton)findViewById(noticeGroup.getCheckedRadioButtonId());


                    ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
                    listingCarModel.setAdvance_notice(noticeRadio.getTag().toString());
                    ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);

//                    Toast.makeText(AdvanceNotice.this, "" +noticeRadio.getTag().toString(), Toast.LENGTH_SHORT).show();

                    Intent advanceNotice=new Intent(AdvanceNotice.this,MinimumTripActivity.class);
                    startActivity(advanceNotice);

                }
            }
        });
    }

}