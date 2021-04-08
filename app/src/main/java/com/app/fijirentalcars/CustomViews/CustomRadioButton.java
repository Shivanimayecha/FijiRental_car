package com.app.fijirentalcars.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomRadioButton extends com.google.android.material.radiobutton.MaterialRadioButton {
    public CustomRadioButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomRadioButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRadioButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-SemiBold.ttf");
        setTypeface(typeface, Typeface.NORMAL);
    }
}
