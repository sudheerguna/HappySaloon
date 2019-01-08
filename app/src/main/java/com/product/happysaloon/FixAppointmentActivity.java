package com.product.happysaloon;

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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.product.happysaloon.adapter.EmpNameAdapter;
import com.product.happysaloon.adapter.ServiceTypeAdapter;
import com.product.happysaloon.authentication.RegistrationActivity;
import com.product.happysaloon.cview.NeoGramMEditText;
import com.product.happysaloon.cview.TypefaceTextview;
import com.product.happysaloon.model.AppointmentAvailability;
import com.product.happysaloon.model.AvailableAppointments;
import com.product.happysaloon.model.EmpNames;
import com.product.happysaloon.model.Servicetypes;
import com.product.happysaloon.utils.Constant;
import com.product.happysaloon.utils.SharedPref;
import com.product.happysaloon.volley.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FixAppointmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ProgressDialog progressDialog;
    SharedPref mSharedPref;
    Spinner spin_servicetype,spin_emp;
    ArrayList<Servicetypes> ServicetypeList;
    String serviceId,servicename;
    NeoGramMEditText et_date,et_starttime,et_endtime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ArrayList<EmpNames> EmpNameList;
    String EmpId,EmpName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_appointment);

        init();
    }

    private void init(){
        Toolbarinit();
        mSharedPref = new SharedPref(FixAppointmentActivity.this);
        progressDialog = Constant.progresdialog(progressDialog, FixAppointmentActivity.this);
        spin_servicetype = (Spinner) findViewById(R.id.spin_servicetype);
        spin_emp = (Spinner) findViewById(R.id.spin_emptype);
        et_date = (NeoGramMEditText) findViewById(R.id.et_date);
        et_starttime = (NeoGramMEditText) findViewById(R.id.et_starttime);
        et_endtime = (NeoGramMEditText) findViewById(R.id.et_endtime);

        spin_servicetype.setOnItemSelectedListener(this);
        spin_emp.setOnItemSelectedListener(this);
        ServicetypeList = new ArrayList<>();
        EmpNameList = new ArrayList<>();
        getServicetype();
        getEmpnames();

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatepicker();
            }
        });

        et_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimepicker(0);
            }
        });

        et_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimepicker(1);
            }
        });

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_data();
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
        ((TypefaceTextview) v.findViewById(R.id.txt_title)).setText("Fix Appointment");
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
                        et_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
                            et_starttime.setText(hour + ":" + min);
                        } else {
                            et_endtime.setText(hour + ":" + min);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void submit_data(){
        if(Constant.isEmptyString(et_date.getText().toString().trim())){
            Toast.makeText(FixAppointmentActivity.this,"Please enter date",Toast.LENGTH_LONG).show();
        }else if(Constant.isEmptyString(et_starttime.getText().toString().trim())){
            Toast.makeText(FixAppointmentActivity.this,"Please enter start time",Toast.LENGTH_LONG).show();
        }else if(Constant.isEmptyString(et_endtime.getText().toString().trim())){
            Toast.makeText(FixAppointmentActivity.this,"Please enter end time",Toast.LENGTH_LONG).show();
        }else if(Constant.isEmptyString(EmpName)){
            Toast.makeText(FixAppointmentActivity.this,"Please enter Emp Name",Toast.LENGTH_LONG).show();
        }else if(Constant.isEmptyString(servicename)){
            Toast.makeText(FixAppointmentActivity.this,"Please enter Servicetype",Toast.LENGTH_LONG).show();
        }else{
            submitdata();
        }
    }

    private void getEmpnames(){
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(FixAppointmentActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                empnames_response(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(FixAppointmentActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method", "getempnameid");
                jsonObject.put("ShopId", mSharedPref.getString(Constant.ShopId));

                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", FixAppointmentActivity.this);
    }

    private void empnames_response(String response){
        EmpNameList.clear();
        try {
            JSONArray jsonArray = new JSONArray(response);
//            Log.e("jsonArray","#"+jsonArray.toString());

            for (int i = 0; i <jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.getString("id");
                String EmpName = jsonObject.getString("EmpName");

                EmpNameList.add(new EmpNames(id, EmpName));
            }
            Log.e("EmpNameList","#"+EmpNameList.size());
            if(EmpNameList.size()>=1){
                Log.e("Enter","#");
                do_EmpnameProcess(EmpNameList);
            }

        } catch (Exception e) {
            Log.e("errorEmp","#"+e.getMessage());
        }
    }

    private void do_EmpnameProcess(ArrayList<EmpNames> empNameList){
        Log.e("empNameList","#"+empNameList.size());
        EmpNameAdapter adapter = new EmpNameAdapter(FixAppointmentActivity.this, empNameList);
        spin_emp.setAdapter(adapter);

        spin_emp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EmpNames mEmpNames = (EmpNames) adapterView.getItemAtPosition(i);
                EmpId = mEmpNames.getId();
                EmpName = mEmpNames.getEmpname();
                Log.e("EmpName","#"+EmpName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getServicetype(){
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(FixAppointmentActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                servicetype_response(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(FixAppointmentActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method","getservicetype");

                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", FixAppointmentActivity.this);

    }

    private void servicetype_response(String response){
        try{
            ServicetypeList.clear();
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.getString("id");
                String ServiceName = jsonObject.getString("ServiceName");
                String TimeInMin = jsonObject.getString("TimeInMin");

                ServicetypeList.add(new Servicetypes(id, ServiceName, TimeInMin));
            }
            if (ServicetypeList.size() >= 1) {
                do_Process(ServicetypeList);
            }
        }catch (Exception e){

        }
    }

    private void submitdata(){
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(FixAppointmentActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                data_response(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(FixAppointmentActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method","fixappointment");
                jsonObject.put("ShopId",mSharedPref.getString(Constant.ShopId));
                jsonObject.put("ScheduleDate",et_date.getText().toString());
                jsonObject.put("CustomerId",mSharedPref.getString(Constant.CustomerId));
                jsonObject.put("EmpID",EmpId);
                jsonObject.put("StartTime",et_starttime.getText().toString());
                jsonObject.put("EndTime",et_endtime.getText().toString());
                jsonObject.put("ServiceType",servicename);

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", FixAppointmentActivity.this);
    }

    private void data_response(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            String respond = jsonObject.getString("respond");
            Log.e("respond","#"+respond);
            Toast.makeText(FixAppointmentActivity.this,jsonObject.getString("0"),Toast.LENGTH_LONG).show();
            if(respond.equals(Constant.success)){
                finish();
            }


        }catch (Exception e){

        }

    }

    private void do_Process(ArrayList<Servicetypes> servicetypeList){
        ServiceTypeAdapter adapter = new ServiceTypeAdapter(FixAppointmentActivity.this, servicetypeList);
        spin_servicetype.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spin_servicetype:
                Servicetypes service = (Servicetypes) adapterView.getItemAtPosition(i);
                serviceId = service.getId();
                servicename = service.getServiceName();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
