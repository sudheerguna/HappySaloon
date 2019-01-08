package com.product.happysaloon.menuslide;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.product.happysaloon.FixAppointmentActivity;
import com.product.happysaloon.R;
import com.product.happysaloon.adapter.AppointmentAdapter;
import com.product.happysaloon.adapter.EmpNameAdapter;
import com.product.happysaloon.cview.NeoGramMEditText;
import com.product.happysaloon.model.AppointmentAvailability;
import com.product.happysaloon.model.AvailableAppointments;
import com.product.happysaloon.model.EmpNames;
import com.product.happysaloon.utils.Constant;
import com.product.happysaloon.utils.SharedPref;
import com.product.happysaloon.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserHomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    ProgressDialog progressDialog;
    SharedPref mSharedPref;
    View rootView;
    RecyclerView recycle_appointmentlist, recycle_appointment_available;
    ArrayList<AvailableAppointments> AvailableAppointmentsList;
    ArrayList<AppointmentAvailability> AppointmentAvailabilityList;
    static Context context;
    NeoGramMEditText et_date;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Spinner spin_emplist;
    ArrayList<EmpNames> EmpNameList;
    String EmpId,EmpName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.userhomefragment, container, false);

        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        context = getActivity();
//        Toast.makeText(getActivity(), "User", Toast.LENGTH_LONG).show();
        mSharedPref = new SharedPref(getActivity());
        progressDialog = Constant.progresdialog(progressDialog, getActivity());
        recycle_appointmentlist = (RecyclerView) rootView.findViewById(R.id.recycle_appointmentlist);
        recycle_appointment_available = (RecyclerView) rootView.findViewById(R.id.recycle_appointment_available);
        et_date = (NeoGramMEditText) rootView.findViewById(R.id.et_date);
        spin_emplist = (Spinner) rootView.findViewById(R.id.spin_emplist);

        AvailableAppointmentsList = new ArrayList<>();
        AppointmentAvailabilityList = new ArrayList<>();
        EmpNameList = new ArrayList<>();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycle_appointmentlist.setLayoutManager(linearLayoutManager);

        final LinearLayoutManager linear_LayoutManager = new LinearLayoutManager(getActivity());
        recycle_appointment_available.setLayoutManager(linear_LayoutManager);

        getCurrentDate();

        rootView.findViewById(R.id.btn_fixappointment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FixAppointmentActivity.class));
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatepicker();
            }
        });

        getEmplist();
        getAppointmentList();

        rootView.findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppintmentAvailability(EmpId);
            }
        });
    }

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        et_date.setText(formattedDate);
    }

    private void openDatepicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        et_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getEmplist() {
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getEmplist»»»>3", "#" + response);
                progressDialog.dismiss();
                Emplist(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method", "getempnameid");
                jsonObject.put("ShopId", mSharedPref.getString(Constant.ShopId));

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", getActivity());
    }

    private void Emplist(String response) {
        EmpNameList.clear();
        try {
            JSONArray jsonArray = new JSONArray(response);
//            Log.e("jsonArray","#"+jsonArray.toString());

            for (int i = 0; i <jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.getString("id");
                String EmpName = jsonObject.getString("EmpName");

                Log.e("id","#"+id);
                Log.e("EmpName","#"+EmpName);
                EmpNameList.add(new EmpNames(id, EmpName));
            }
            Log.e("EmpNameList","#"+EmpNameList.size());
            if(EmpNameList.size()>=1){
                Log.e("Enter","#");
                do_EmpnameProcess(EmpNameList);
                rootView.findViewById(R.id.txt_2).setVisibility(View.GONE);
                rootView.findViewById(R.id.recycle_appointment_available).setVisibility(View.VISIBLE);
            }else{
                rootView.findViewById(R.id.txt_2).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.recycle_appointment_available).setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Log.e("errorEmp","#"+e.getMessage());
        }
    }

    private void do_EmpnameProcess(ArrayList<EmpNames> empNameList){
        Log.e("empNameList","#"+empNameList.size());
        EmpNameAdapter adapter = new EmpNameAdapter(getActivity(), empNameList);
        spin_emplist.setAdapter(adapter);

        spin_emplist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EmpNames mEmpNames = (EmpNames) adapterView.getItemAtPosition(i);
                EmpId = mEmpNames.getId();
                EmpName = mEmpNames.getEmpname();

                AppintmentAvailability(EmpId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void AppintmentAvailability(final String EmpId) {
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Available»»»>3", "#" + response);
                progressDialog.dismiss();
                AppointmentAvailable_response(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method", "getavailableappointmenttimings");
                jsonObject.put("ShopId", mSharedPref.getString(Constant.ShopId));
                jsonObject.put("EmpId", EmpId);
                jsonObject.put("ScheduleDate", et_date.getText().toString());

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", getActivity());
    }

    private void AppointmentAvailable_response(String response) {
        AppointmentAvailabilityList.clear();
        try {
            if(!Constant.isEmptyString(response)){
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String StartTime = jsonObject.getString("StartTime");
                    String EndTime = jsonObject.getString("EndTime");

                    AppointmentAvailabilityList.add(new AppointmentAvailability(StartTime, EndTime));
                }

                if (AppointmentAvailabilityList.size() >= 1) {
                    rootView.findViewById(R.id.txt_2).setVisibility(View.GONE);
                    rootView.findViewById(R.id.recycle_appointment_available).setVisibility(View.VISIBLE);
                    do_Process(AppointmentAvailabilityList);
                } else {
                    rootView.findViewById(R.id.txt_2).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.recycle_appointment_available).setVisibility(View.GONE);
                }
            }else{
                rootView.findViewById(R.id.txt_2).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.recycle_appointment_available).setVisibility(View.GONE);
            }

        } catch (Exception e) {
            rootView.findViewById(R.id.txt_2).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.recycle_appointment_available).setVisibility(View.GONE);
        }
    }

    private void do_Process(ArrayList<AppointmentAvailability> appointmentAvailabilityList) {
        AppointmentAvailabilityAdapter adapter = new AppointmentAvailabilityAdapter(getActivity(), appointmentAvailabilityList);
        recycle_appointment_available.setAdapter(adapter);
    }

    private void getAppointmentList() {
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                appointmentlist_response(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method", "cusappointmentdetails");
                jsonObject.put("CusId", mSharedPref.getString(Constant.CustomerId));

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", getActivity());
    }

    private void appointmentlist_response(String response) {
        AvailableAppointmentsList.clear();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String ShopId = jsonObject.getString("ShopId");
                String Employee = jsonObject.getString("Employee");
                String StartTime = jsonObject.getString("StartTime");
                String EndTime = jsonObject.getString("EndTime");
                String ServiceName = jsonObject.getString("ServiceName");
                String TimeInMin = jsonObject.getString("TimeInMin");

                AvailableAppointmentsList.add(new AvailableAppointments(id, StartTime, EndTime, ServiceName, TimeInMin));
            }

            if (AvailableAppointmentsList.size() >= 1) {
                rootView.findViewById(R.id.txt_1).setVisibility(View.GONE);
                rootView.findViewById(R.id.recycle_appointmentlist).setVisibility(View.VISIBLE);
                doProcess(AvailableAppointmentsList);
            } else {
                rootView.findViewById(R.id.txt_1).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.recycle_appointmentlist).setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            rootView.findViewById(R.id.txt_1).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.recycle_appointmentlist).setVisibility(View.GONE);
        }
    }

    private void doProcess(ArrayList<AvailableAppointments> availableAppointmentsList) {
        AppointmentAdapter adapter = new AppointmentAdapter(getActivity(), availableAppointmentsList);
        recycle_appointmentlist.setAdapter(adapter);
    }

    public static void showpopup(final String position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Happy Saloon");
        alertDialog.setMessage("Cancel Appointment");
        alertDialog.setButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                CancelAppointment(position);
                alertDialog.cancel();
            }
        });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        alertDialog.show();
    }

    private static void CancelAppointment(final String position) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                cancelprocess(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.e(" response error is", "#" + response);
                Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> jsonObject = new HashMap<String, String>();
                jsonObject.put("method", "cancelappointment");
                jsonObject.put("id", position);

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", context);
    }

    private static void cancelprocess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String respond = jsonObject.getString("respond");
            Log.e("respond", "#" + respond);

            Toast.makeText(context, jsonObject.getString("0"), Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spin_emplist:
                EmpNames mEmpNames = (EmpNames) adapterView.getItemAtPosition(i);
                EmpId = mEmpNames.getId();
                EmpName = mEmpNames.getEmpname();
                Log.e("EmpNameadapter2","#"+EmpName);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
