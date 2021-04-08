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

public class YourGoals extends AppCompatActivity {

    RadioGroup shareRadioGroup,weekRadioGroup,monthRadioGroup;
    ImageView ivBack;
    TextView tv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_goals);

        shareRadioGroup=findViewById(R.id.share_group);
        weekRadioGroup=findViewById(R.id.week_group);
        monthRadioGroup=findViewById(R.id.month_group);
        tv_submit=findViewById(R.id.tv_continue);

        shareRadioGroup.clearCheck();
        weekRadioGroup.clearCheck();
        monthRadioGroup.clearCheck();
        ivBack=findViewById(R.id.iv_back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shareRadioGroup.getCheckedRadioButtonId()==-1 || weekRadioGroup.getCheckedRadioButtonId()==-1 || monthRadioGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(YourGoals.this, "Please select a response for each question to continue.", Toast.LENGTH_SHORT).show();
                }else {

                    RadioButton shareRadio= (RadioButton)findViewById(shareRadioGroup.getCheckedRadioButtonId());
                    RadioButton weekRadio= (RadioButton)findViewById(weekRadioGroup.getCheckedRadioButtonId());
                    RadioButton monthRadio= (RadioButton)findViewById(monthRadioGroup.getCheckedRadioButtonId());

                    ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
                    listingCarModel.setPrimary_goal_of_sharing(shareRadio.getTag().toString());
                    listingCarModel.setOften_use_of_car(weekRadio.getTag().toString());
                    listingCarModel.setOften_share_car(monthRadio.getTag().toString());

                    ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);

//                    Toast.makeText(YourGoals.this, "" +shareRadio.getText().toString(), Toast.LENGTH_SHORT).show();

                    Intent advanceNotice=new Intent(YourGoals.this,AdvanceNotice.class);
                    startActivity(advanceNotice);


                }
            }
        });


    }
}