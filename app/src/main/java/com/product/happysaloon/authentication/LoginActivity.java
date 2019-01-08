package com.product.happysaloon.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.product.happysaloon.HomeActivity;
import com.product.happysaloon.Home_Activity;
import com.product.happysaloon.R;
import com.product.happysaloon.cview.NeoGramMEditText;
import com.product.happysaloon.model.LoginResponse;
import com.product.happysaloon.utils.Constant;
import com.product.happysaloon.utils.SharedPref;
import com.product.happysaloon.volley.AppController;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedPref mSharedPref;
    NeoGramMEditText et_phone,et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init(){
        mSharedPref = new SharedPref(LoginActivity.this);
        progressDialog = Constant.progresdialog(progressDialog, LoginActivity.this);
        et_phone = (NeoGramMEditText) findViewById(R.id.et_phone);
        et_pwd = (NeoGramMEditText) findViewById(R.id.etPassword);

        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void Login(){
        if(Constant.isEmptyString(et_phone.getText().toString())){
            Toast.makeText(LoginActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
        }else if(Constant.isEmptyString(et_pwd.getText().toString())){
            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
        }else{
            if(et_phone.getText().toString().length()<=9){
                Toast.makeText(LoginActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
            }else{
                doLogin();
            }
        }
    }

    private void doLogin(){
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                Login_response(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(LoginActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method", "login");
                jsonObject.put("UserName", et_phone.getText().toString());
                jsonObject.put("Password", et_pwd.getText().toString());

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", LoginActivity.this);
    }

    private void Login_response(String response){
        Gson gson = new Gson();
        LoginResponse sResult = gson.fromJson(response, LoginResponse.class);
        if (sResult != null) {
            if(sResult.getRespond().equals(Constant.success)){
                mSharedPref.putString(Constant.LoginUserType,sResult.getUserType());
                if(sResult.getUserType().equals("customer")){
                    mSharedPref.putString(Constant.Login_type,"1");
                    mSharedPref.putString(Constant.CustomerId,sResult.getCustomerLoginSuccessfull().get(0).getCusid());
                    mSharedPref.putString(Constant.ShopId,String.valueOf(sResult.getCustomerLoginSuccessfull().get(0).getShopId()));
                    mSharedPref.putString(Constant.UserName,sResult.getCustomerLoginSuccessfull().get(0).getUserName());
                }else if(sResult.getUserType().equals("owner")){
                    mSharedPref.putString(Constant.Login_type,"2");
                    mSharedPref.putString(Constant.ShopId,String.valueOf(sResult.getOwnerLoginSuccessfull().get(0).getShooId()));
                    mSharedPref.putString(Constant.UserName,sResult.getOwnerLoginSuccessfull().get(0).getUserName());
                }

                mSharedPref.putString(Constant.Login_success,"1");
                startActivity(new Intent(LoginActivity.this,Home_Activity.class));
                finish();
            }else{
                Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_LONG).show();
            }
        }
    }
}
