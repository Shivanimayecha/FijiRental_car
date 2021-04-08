package com.app.fijirentalcars;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.Adapter.AddphotosAdapter;
import com.app.fijirentalcars.util.SpacesItemDecoration;

public class AddPhotosActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_next;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);
        Window window = this.getWindow();
        Log.e("AddPhotosActivity","AddPhotosActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.my_statusbar_color));

        ImageView iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        AddphotosAdapter adapter = new AddphotosAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(adapter);

        tv_next=findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_next:
                Intent i =new Intent(this,MileageActivity.class);
                startActivity(i);

                break;

        }
    }
}
