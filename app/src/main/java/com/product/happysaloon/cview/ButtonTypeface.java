package com.product.happysaloon.cview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by guna on 26-09-2016.
 */
public class ButtonTypeface extends Button {
    public ButtonTypeface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ButtonTypeface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonTypeface(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/SF-Pro-Display-Regular.otf");
        setTypeface(tf);
    }
}


