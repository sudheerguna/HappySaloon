package com.product.happysaloon.authentication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.product.happysaloon.R;
import com.product.happysaloon.cview.NeoGramMEditText;
import com.product.happysaloon.cview.TypefaceTextview;
import com.product.happysaloon.utils.Constant;
import com.product.happysaloon.utils.SharedPref;
import com.product.happysaloon.volley.AppController;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedPref mSharedPref;
    private RadioGroup radioGroup;
    String selecttype = "User",currentDateandTime;
    NeoGramMEditText name, email, password, mobile, et_cnfm_pwd, et_shopid, et_regdate, et_timefrom, et_timeto, et_totalemployees;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
    }

    private void init() {
        Toolbarinit();
        mSharedPref = new SharedPref(RegistrationActivity.this);
        progressDialog = Constant.progresdialog(progressDialog, RegistrationActivity.this);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        name = (NeoGramMEditText) findViewById(R.id.et_Name);
        email = (NeoGramMEditText) findViewById(R.id.et_Email);
        password = (NeoGramMEditText) findViewById(R.id.et_pwd);
        mobile = (NeoGramMEditText) findViewById(R.id.et_mobile);
        et_cnfm_pwd = (NeoGramMEditText) findViewById(R.id.et_cnfm_pwd);

        et_shopid = (NeoGramMEditText) findViewById(R.id.et_shopid);
        et_regdate = (NeoGramMEditText) findViewById(R.id.et_regdate);
        et_timefrom = (NeoGramMEditText) findViewById(R.id.et_timefrom);
        et_timeto = (NeoGramMEditText) findViewById(R.id.et_timeto);
        et_totalemployees = (NeoGramMEditText) findViewById(R.id.et_totalemployees);

        radioGroup.check(R.id.rb_user);
        findViewById(R.id.layout_merchant).setVisibility(View.GONE);
        getcurrentdate();

        et_regdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatepicker();
            }
        });

        et_timefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimepicker(0);
            }
        });

        et_timeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimepicker(1);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
//                    Toast.makeText(RegistrationActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    if (rb.getText().equals("User")) {
                        selecttype = "User";
                        findViewById(R.id.layout_merchant).setVisibility(View.GONE);
                    } else {
                        selecttype = "Merchant";
                        findViewById(R.id.layout_merchant).setVisibility(View.GONE);
                    }
                }
            }
        });

        findViewById(R.id.btn_create_Account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doregister();
            }
        });

        Constant.hidekeyboard(RegistrationActivity.this);
    }

    private void Toolbarinit() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);
        ((TypefaceTextview) v.findViewById(R.id.txt_title)).setText("SignUp");
        getSupportActionBar().setCustomView(v);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getcurrentdate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        currentDateandTime = sdf.format(new Date());
        Log.e("currentDateandTime","#"+currentDateandTime);
    }

    private void openDatepicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
//                        et_regdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + );

                        et_regdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void openTimepicker(final int pos) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String min, hour;
                        if (String.valueOf(minute).length() == 1) {
                            min = "0" + minute;
                        } else {
                            min = String.valueOf(minute);
                        }

                        if (String.valueOf(hourOfDay).length() == 1) {
                            hour = "0" + hourOfDay;
                        } else {
                            hour = String.valueOf(hourOfDay);
                        }

                        if (pos == 0) {
                            et_timefrom.setText(hour + ":" + min);
                        } else {
                            et_timeto.setText(hour + ":" + min);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void doregister() {
        if (selecttype.equals("User")) {
            if (Constant.isEmptyString(name.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(email.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(password.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(et_cnfm_pwd.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(mobile.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter mobile", Toast.LENGTH_SHORT).show();
            } else {
                if (password.getText().toString().trim().equals(et_cnfm_pwd.getText().toString().trim())) {
                    if (mobile.getText().toString().trim().length() <= 9) {
                        Toast.makeText(RegistrationActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                    } else {
                        SendDatatoserver();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "password and confirm password should be same", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (Constant.isEmptyString(name.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(email.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(password.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(et_cnfm_pwd.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(mobile.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter mobile", Toast.LENGTH_SHORT).show();
            }
           /* else if (Constant.isEmptyString(et_regdate.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter registered date", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(et_timefrom.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter shop open time", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(et_timeto.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter shop close time", Toast.LENGTH_SHORT).show();
            } else if (Constant.isEmptyString(et_totalemployees.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter no of employees", Toast.LENGTH_SHORT).show();
            } */
            else {
                if (password.getText().toString().trim().equals(et_cnfm_pwd.getText().toString().trim())) {
                    if (mobile.getText().toString().trim().length() <= 9) {
                        Toast.makeText(RegistrationActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                    } else {
                        SendDatatoserver();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "password and confirm password should be same", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void SendDatatoserver() {
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = null;
        if (selecttype.equals("User")) {
            url = Constant.REST_URL_DOMAIN + "sample.php";
        } else {
            url = Constant.REST_URL_DOMAIN + "sample.php";
        }

        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                Register_response(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(RegistrationActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                if (selecttype.equals("User")) {
                    jsonObject.put("method", "custins");
                    jsonObject.put("UserName", mobile.getText().toString().trim());
                    jsonObject.put("Password", password.getText().toString().trim());
                    jsonObject.put("CustomerName", name.getText().toString().trim());
                    jsonObject.put("FirebaseId", mSharedPref.getString(Constant.FCMID));
                } else {
                    jsonObject.put("method", "shopins");
                    jsonObject.put("UserName", mobile.getText().toString().trim());
                    jsonObject.put("Password", password.getText().toString().trim());
//                    jsonObject.put("otp", "");
//                    jsonObject.put("Verified", "");
                    jsonObject.put("OwnerName", name.getText().toString().trim());
//                    jsonObject.put("Reg_date", et_regdate.getText().toString().trim());
                    jsonObject.put("FirebaseId", mSharedPref.getString(Constant.FCMID));
//                    jsonObject.put("ShopTimeFrom",currentDateandTime +" "+ et_timefrom.getText().toString().trim());
//                    jsonObject.put("ShopTimeTo", currentDateandTime +" "+et_timeto.getText().toString().trim());
//                    jsonObject.put("TotEmployees", et_totalemployees.getText().toString().trim());
                }

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", RegistrationActivity.this);
    }

    private void Register_response(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String jsonObject1 = jsonObject.getString("respond");
            String Data = jsonObject.getString("Data Inserted");

            if (Data.equals("true")) {
                Toast.makeText(RegistrationActivity.this, "Registration Success", Toast.LENGTH_LONG).show();
                gotoOtp();
            } else {
                Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.e("error", "#" + e.toString());
        }
    }

    private void gotoOtp() {
        startActivity(new Intent(RegistrationActivity.this, OtpActivity.class).putExtra("selecttype", selecttype).putExtra("mobile",mobile.getText().toString().trim()));
    }
}
