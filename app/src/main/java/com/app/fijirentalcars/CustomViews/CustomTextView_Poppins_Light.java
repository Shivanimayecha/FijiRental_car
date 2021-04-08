package com.app.fijirentalcars.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextView_Poppins_Light extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextView_Poppins_Light(Context context) {
        super(context);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        setTypeface(typeface, Typeface.NORMAL);

    }

    public CustomTextView_Poppins_Light(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        setTypeface(typeface, Typeface.NORMAL);
    }


}