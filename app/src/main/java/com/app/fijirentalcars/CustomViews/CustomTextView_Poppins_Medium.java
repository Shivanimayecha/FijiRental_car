package com.app.fijirentalcars.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextView_Poppins_Medium extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextView_Poppins_Medium(Context context) {
        super(context);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Montserrat-Medium.ttf");
        setTypeface(typeface, Typeface.NORMAL);

    }

    public CustomTextView_Poppins_Medium(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Montserrat-Medium.ttf");
        setTypeface(typeface, Typeface.NORMAL);
    }


}