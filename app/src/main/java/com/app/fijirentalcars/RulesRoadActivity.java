package com.app.fijirentalcars;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class RulesRoadActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_gotit;
    ImageView iv_back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_road);
        Log.e("RulesRoadActivity", "RulesRoadActivity");
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.bg));

        /*RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RulesRoadAdapter adapter = new RulesRoadAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);*/

        init();
    }

    public void init() {
        tv_gotit = findViewById(R.id.tv_gotit);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_gotit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_gotit:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }
}