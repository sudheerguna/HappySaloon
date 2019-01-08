
package com.product.happysaloon.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.product.happysaloon.R;
import com.product.happysaloon.cview.NeoGramMEditText;
import com.product.happysaloon.utils.Constant;
import com.product.happysaloon.volley.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    String selecttype,mobile;
    NeoGramMEditText et_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        init();
    }

    private void init() {
        progressDialog = Constant.progresdialog(progressDialog, OtpActivity.this);
        et_otp = (NeoGramMEditText) findViewById(R.id.et_otp);
        selecttype = getIntent().getStringExtra("selecttype");
        mobile = getIntent().getStringExtra("mobile");
        Log.e("selecttype", "#" + selecttype);

        findViewById(R.id.btn_verify_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constant.isEmptyString(et_otp.getText().toString())){
                    Toast.makeText(OtpActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                }else{
                    verifyotp();
                }
            }
        });
    }

    private void verifyotp() {
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(OtpActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                otp_response(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(OtpActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                if (selecttype.equals("User")) {
                    jsonObject.put("method", "verifycustotp");
                } else {
                    jsonObject.put("method", "verifyshopotp");
                }

                jsonObject.put("UserName", mobile);
                jsonObject.put("otp", et_otp.getText().toString().trim());

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", OtpActivity.this);
    }

    private void otp_response(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String jsonObject1 = jsonObject.getString("respond");
            if(jsonObject1.equals(Constant.success)){
                Toast.makeText(OtpActivity.this,"OTP verified successfully",Toast.LENGTH_LONG).show();
                gotoLogin();
            }else{
                Toast.makeText(OtpActivity.this,"Please try again",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }
    }

    private void gotoLogin(){
        Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
