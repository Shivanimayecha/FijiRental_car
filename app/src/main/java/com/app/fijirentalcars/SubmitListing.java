package com.app.fijirentalcars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fijirentalcars.util.FijiRentalUtils;

public class SubmitListing extends AppCompatActivity implements View.OnClickListener {


    ImageView ivBack;
    TextView termService, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_listing);
        ivBack = findViewById(R.id.iv_back);
        termService = findViewById(R.id.txt_service);
        nextBtn = findViewById(R.id.tv_next);

        ivBack.setOnClickListener(this);
        String s1="By submitting your listing, uou agree to the ";
        String s2="Turo term of service.";
        SpannableString ss = new SpannableString(s1+s2);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = "https://fijirentalcars.siddhidevelopment.com/help/";


                CustomTabsIntent tabsIntent1 = new CustomTabsIntent.Builder().build();
                tabsIntent1.intent.setPackage("com.android.chrome");
                tabsIntent1.launchUrl(SubmitListing.this, Uri.parse(url));

            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, s1.length(), s1.length()+s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termService.setText(ss);
        termService.setMovementMethod(LinkMovementMethod.getInstance());
        termService.setHighlightColor(Color.TRANSPARENT);
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
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(FijiRentalUtils.CAR_FLAW,true);
            startActivity(i);
        }
    }
}