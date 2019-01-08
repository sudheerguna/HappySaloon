package com.product.happysaloon.cview;

/**
 * Created by aspire on 7/8/2015.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class NeoGramMEditText extends EditText {
    public NeoGramMEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public NeoGramMEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public NeoGramMEditText(Context context) {
        super(context);
        init();
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/SF-Pro-Display-Regular.otf");
        setTypeface(tf);
    }
}