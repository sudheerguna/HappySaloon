package com.product.happysaloon;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.product.happysaloon.authentication.LoginActivity;
import com.product.happysaloon.utils.Constant;
import com.product.happysaloon.utils.SharedPref;
import com.product.happysaloon.volley.AppController;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedPref mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    private void init(){
        mSharedPref = new SharedPref(HomeActivity.this);
        progressDialog = Constant.progresdialog(progressDialog, HomeActivity.this);

        getshopList();
    }

    private void getshopList(){
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                shop_response(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(HomeActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method", "getshopnameid");
//                jsonObject.put("UserName", et_phone.getText().toString());
//                jsonObject.put("Password", et_pwd.getText().toString());

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", HomeActivity.this);
    }

    private void shop_response(String response){

    }
}
