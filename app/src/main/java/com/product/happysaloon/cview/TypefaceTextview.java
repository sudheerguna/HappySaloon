package com.product.happysaloon.cview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypefaceTextview extends TextView {
    public TypefaceTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TypefaceTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TypefaceTextview(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/SF-Pro-Display-Regular.otf");
        setTypeface(tf);
    }
}
