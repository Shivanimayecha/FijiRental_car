package com.app.fijirentalcars;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.fijirentalcars.util.FijiRentalUtils;

public class LetsStartActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_login, tv_signup, tv_signup_owner;
    ImageView iv_back;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letsstart);
        Window window = this.getWindow();
        Log.e("LetsStartActivity", "LetsStartActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        init();

    }

    public void init() {
        tv_login = findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        tv_signup = findViewById(R.id.tv_signup);
        tv_signup_owner = findViewById(R.id.tv_signup_owner);
        iv_back = findViewById(R.id.iv_back);
        tv_signup.setOnClickListener(this);
        tv_signup_owner.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_login:

                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);

                break;

            case R.id.tv_signup:

                Intent i2 = new Intent(this, SignUpActivity.class);
                i2.putExtra("USERTYPE", FijiRentalUtils.Consumer);
                startActivity(i2);

                break;
            case R.id.tv_signup_owner:

                Intent i3 = new Intent(this, SignUpActivity.class);
                i3.putExtra("USERTYPE",FijiRentalUtils.Owner);
                startActivity(i3);

                break;
            case R.id.iv_back:
                finish();
                break;


        }
    }
}