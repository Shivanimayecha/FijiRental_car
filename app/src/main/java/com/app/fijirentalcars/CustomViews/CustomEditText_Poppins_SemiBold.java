package com.app.fijirentalcars.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditText_Poppins_SemiBold extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditText_Poppins_SemiBold(Context context) {
        super(context);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-SemiBold.ttf");
        setTypeface(typeface, Typeface.NORMAL);

    }

    public CustomEditText_Poppins_SemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-SemiBold.ttf");
        setTypeface(typeface, Typeface.NORMAL);
    }
}