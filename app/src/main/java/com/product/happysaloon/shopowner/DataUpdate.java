package com.product.happysaloon.shopowner;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DataUpdate extends AppCompatActivity {
    SharedPref mSharedPref;
    ProgressDialog progressDialog;
    NeoGramMEditText et_regdate,et_totalemployees,et_timefrom,et_timeto;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_update);

        init();
    }

    private void init() {
        mSharedPref = new SharedPref(DataUpdate.this);
        progressDialog = Constant.progresdialog(progressDialog, DataUpdate.this);
        Toolbarinit();

        et_regdate = (NeoGramMEditText) findViewById(R.id.et_regdate);
        et_totalemployees = (NeoGramMEditText) findViewById(R.id.et_totalemployees);
        et_timefrom = (NeoGramMEditText) findViewById(R.id.et_timefrom);
        et_timeto = (NeoGramMEditText) findViewById(R.id.et_timeto);

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

        findViewById(R.id.btn_sbmt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                do_dailyupdate();
            }
        });
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
        ((TypefaceTextview) v.findViewById(R.id.txt_title)).setText("Data Update");
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

    private void do_dailyupdate(){
        if(Constant.isEmptyString(et_regdate.getText().toString().trim())){
            Toast.makeText(DataUpdate.this,"Enter Reg Date",Toast.LENGTH_LONG).show();
        }else if(Constant.isEmptyString(et_totalemployees.getText().toString().trim())){
            Toast.makeText(DataUpdate.this,"Enter Total No Of Employees",Toast.LENGTH_LONG).show();
        }else if(Constant.isEmptyString(et_timefrom.getText().toString().trim())){
            Toast.makeText(DataUpdate.this,"Enter Shop Open Time",Toast.LENGTH_LONG).show();
        }else if(Constant.isEmptyString(et_timeto.getText().toString().trim())){
            Toast.makeText(DataUpdate.this,"Enter Shop Close Time",Toast.LENGTH_LONG).show();
        }else{
            dailyshopdet();
        }
    }

    private void dailyshopdet() {
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(DataUpdate.this);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                doprocess(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(DataUpdate.this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method", "dailyshopdet");
                jsonObject.put("ShopId", mSharedPref.getString(Constant.ShopId));
                jsonObject.put("ScheduleDate", et_regdate.getText().toString().trim());
                jsonObject.put("TotEmployees", et_totalemployees.getText().toString().trim());
                jsonObject.put("ShopTimeFrom", et_timefrom.getText().toString().trim());
                jsonObject.put("ShopTimeTo", et_timeto.getText().toString().trim());
                jsonObject.put("EmpId", "1");


                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", DataUpdate.this);
    }

    private void doprocess(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            String respond = jsonObject.getString("respond");
            Log.e("respond","#"+respond);
            Toast.makeText(DataUpdate.this,jsonObject.getString("0"),Toast.LENGTH_LONG).show();
            if(respond.equals(Constant.success)){
                finish();
            }
        }catch (Exception e){

        }
    }
}
