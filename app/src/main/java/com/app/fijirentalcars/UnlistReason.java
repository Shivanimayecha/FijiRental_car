package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.util.FijiRentalUtils;

public class UnlistReason extends AppCompatActivity implements View.OnClickListener {

    TextView saveBtn;
    EditText unlistReason;
    CarModel carModel;
    RadioGroup radioGroup;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlist_reason);
        saveBtn=findViewById(R.id.save_btn);

        backBtn=findViewById(R.id.iv_back);
        radioGroup=findViewById(R.id.status_group);
        unlistReason=findViewById(R.id.unlist_reason);

        backBtn.setOnClickListener(this);

        if (getIntent().hasExtra(FijiRentalUtils.CarDetailIntent)) {
            carModel = (CarModel) getIntent().getSerializableExtra(FijiRentalUtils.CarDetailIntent);

        }

        saveBtn.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton noticeRadio = (RadioButton) findViewById(checkedId);

                if(noticeRadio.getText().equals("I have other reasons")){
                    unlistReason.setVisibility(View.VISIBLE);
                }else {
                    unlistReason.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.save_btn){
            if(radioGroup.getCheckedRadioButtonId()==-1){
                Toast.makeText(this, "Select a reason", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton noticeRadio= (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
            if(noticeRadio.getText().equals("I have other reasons")){
                if(!TextUtils.isEmpty(unlistReason.getText().toString())){
                    Intent i=new Intent(this,SnoozedActivity.class);
                    i.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
                    i.putExtra("UNLIST_REASON", unlistReason.getText().toString());
                    startActivityForResult(i,FijiRentalUtils.UPDATE_LISTING_STATUS);
                    return;
                }

            }

            Intent i=new Intent(this,SnoozedActivity.class);
            i.putExtra("UNLIST_REASON",  noticeRadio.getText().toString());
            i.putExtra(FijiRentalUtils.CarDetailIntent, carModel);
            startActivityForResult(i,FijiRentalUtils.UPDATE_LISTING_STATUS);
        }else if(v.getId()==R.id.iv_back){
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FijiRentalUtils.UPDATE_LISTING_STATUS){
            if(resultCode==RESULT_OK){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","success");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }

    }

}