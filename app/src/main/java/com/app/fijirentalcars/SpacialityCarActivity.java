package com.app.fijirentalcars;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.fijirentalcars.CustomViews.CustomDialog;
import com.app.fijirentalcars.listners.DialogItemListner;

import java.util.ArrayList;
import java.util.List;

public class SpacialityCarActivity extends AppCompatActivity implements View.OnClickListener, DialogItemListner {

    TextView tv_next, etMechCondition, etHaveSeatBelt, etSeatType;
    ImageView iv_back;
    EditText etPrice, etAnythingElse;
    private CustomDialog customDialog;
    List MechCondList=new ArrayList();
    List haveSeatbelt=new ArrayList();
    List seatBeltType=new ArrayList();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacialitycar);
        Window window = this.getWindow();
        Log.e("SpacialityCarActivity", "SpacialityCarActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        MechCondList.clear();
        MechCondList.add("Excellent");
        MechCondList.add("Good");
        MechCondList.add("Fair");
        MechCondList.add("Not working");

        haveSeatbelt.clear();
        haveSeatbelt.add("Yes");
        haveSeatbelt.add("No");

        seatBeltType.clear();
        seatBeltType.add("Only shoulder belts");
        seatBeltType.add("Only lap belts");
        seatBeltType.add("Only lap and shoulder belts");
        init();
    }

    public void init() {
        tv_next = findViewById(R.id.tv_next);
        iv_back = findViewById(R.id.iv_back);
        etPrice = findViewById(R.id.et_carprice);
        etMechCondition = findViewById(R.id.et_mech_condition);
        etHaveSeatBelt = findViewById(R.id.et_have_seat);
        etSeatType = findViewById(R.id.et_seatbelttype);
        etAnythingElse = findViewById(R.id.et_anythingelse);
        tv_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        etMechCondition.setOnClickListener(this);
        etHaveSeatBelt.setOnClickListener(this);
        etSeatType.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                Intent i = new Intent(this, UserProfilePicture.class);
                startActivity(i);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_mech_condition:

                customDialog = new CustomDialog(this, MechCondList, this);

                customDialog.show(getSupportFragmentManager(), "MECH_COND");
                break;
            case R.id.et_have_seat:
                customDialog = new CustomDialog(this, haveSeatbelt, this);

                customDialog.show(getSupportFragmentManager(), "HAVE_BELT");
                break;
            case R.id.et_seatbelttype:
                customDialog = new CustomDialog(this, seatBeltType, this);

                customDialog.show(getSupportFragmentManager(), "BELT_TYPE");
                break;
        }
    }

    @Override
    public void onItemClick(Object val) {
        if(customDialog.getTag().equals("MECH_COND")){
            etMechCondition.setText(String.valueOf(val));
        }

        if(customDialog.getTag().equals("HAVE_BELT")){
            etHaveSeatBelt.setText(String.valueOf(val));

            if(String.valueOf(val).equalsIgnoreCase("yes")){
                etSeatType.setVisibility(View.VISIBLE);
            }else {
                etSeatType.setVisibility(View.GONE);
            }

        }

        if(customDialog.getTag().equals("BELT_TYPE")){
            etSeatType.setText(String.valueOf(val));
        }

        customDialog.dismiss();
    }
}