package com.product.happysaloon.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;

import com.product.happysaloon.R;

public class Constant {
    public static final String REST_URL_DOMAIN = "http://qualityapps.in/";
    public static final String FCMID = "FCMID";
    public static final String failed = "failed";
    public static final String success = "success";
    public static final String Login_success = "Login_success";
    public static final String Login_type = "Login_type";
    public static final String LoginUserType = "LoginUserType";

    public static final String CustomerId = "CustomerId";
    public static final String ShopId = "ShopId";
    public static final String UserName = "UserName";

    public static ProgressDialog progresdialog(ProgressDialog progressDialog, Context mContext) {
        progressDialog = new ProgressDialog(mContext, R.style.full_screen_dialog) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.progress_dialog_fullscreen);
                getWindow().setLayout(DrawerLayout.LayoutParams.FILL_PARENT,
                        DrawerLayout.LayoutParams.FILL_PARENT);
            }
        };
        return progressDialog;
    }

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim().length() <= 0);
    }

    public static void hidekeyboard(Context mContext) {
        ((Activity) mContext).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

}
