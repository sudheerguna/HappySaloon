package com.product.happysaloon.shopowner;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.product.happysaloon.R;
import com.product.happysaloon.adapter.AppointmentAdapter;
import com.product.happysaloon.adapter.EmpNameAdapter;
import com.product.happysaloon.adapter.OwnerAppointmentAdapter;
import com.product.happysaloon.authentication.LoginActivity;
import com.product.happysaloon.cview.NeoGramMEditText;
import com.product.happysaloon.model.AvailableAppointments;
import com.product.happysaloon.model.EmpNames;
import com.product.happysaloon.model.OwnerAppointments;
import com.product.happysaloon.utils.Constant;
import com.product.happysaloon.utils.SharedPref;
import com.product.happysaloon.volley.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShopOwnerFragment extends Fragment {
    ProgressDialog progressDialog;
    SharedPref mSharedPref;
    View rootView;
    NeoGramMEditText et_date;
    RecyclerView recycler_appointment;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ArrayList<OwnerAppointments> OwnerAppointmentsList;
    Spinner spin_emptype;
    ArrayList<EmpNames> EmpNameList;
    String EmpId,EmpName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.shopwwnerfragment, container, false);

        init(rootView);
        return rootView;
    }

    private void init(View rootView){
        mSharedPref = new SharedPref(getActivity());
        progressDialog = Constant.progresdialog(progressDialog, getActivity());
        et_date = (NeoGramMEditText) rootView.findViewById(R.id.et_date);
        recycler_appointment = (RecyclerView) rootView.findViewById(R.id.recycle_appointment);
        spin_emptype = (Spinner) rootView.findViewById(R.id.spin_emptype);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler_appointment.setLayoutManager(linearLayoutManager);

        OwnerAppointmentsList = new ArrayList<>();
        EmpNameList = new ArrayList<>();

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatepicker();
            }
        });

        rootView.findViewById(R.id.btn_sbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getappointmentlist(EmpId);
            }
        });

        rootView.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),DataUpdate.class));
            }
        });

        getcurrentdate();
        getEmplist();
    }

    private void getcurrentdate(){
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

                EmpNameList.add(new EmpNames(id, EmpName));
            }
            Log.e("EmpNameList","#"+EmpNameList.size());
            if(EmpNameList.size()>=1){
                do_EmpnameProcess(EmpNameList);

                rootView.findViewById(R.id.txt_1).setVisibility(View.GONE);
                rootView.findViewById(R.id.recycle_appointment).setVisibility(View.VISIBLE);
            }else{
                rootView.findViewById(R.id.txt_1).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.recycle_appointment).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("errorEmp","#"+e.getMessage());
        }
    }

    private void do_EmpnameProcess(ArrayList<EmpNames> empNameList){
        Log.e("empNameList","#"+empNameList.size());
        EmpNameAdapter adapter = new EmpNameAdapter(getActivity(), empNameList);
        spin_emptype.setAdapter(adapter);

        spin_emptype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EmpNames mEmpNames = (EmpNames) adapterView.getItemAtPosition(i);
                EmpId = mEmpNames.getId();
                EmpName = mEmpNames.getEmpname();

                getappointmentlist(EmpId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getappointmentlist(final String EmpId){
        progressDialog.show();
        progressDialog.setCancelable(false);

        String url = Constant.REST_URL_DOMAIN + "sampleget.php";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getresponse»»»>3", "#" + response);
                progressDialog.dismiss();
                appointmentresponse(response);
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
                jsonObject.put("method", "appointmentdetailsreporttoshop");
                jsonObject.put("ShopId", mSharedPref.getString(Constant.ShopId));
                jsonObject.put("EmpId", EmpId);
                jsonObject.put("ScheduleDate", et_date.getText().toString().trim());

                Log.e("jsonObject»»»>", "#" + jsonObject.toString());
                return jsonObject;
            }
        };
//        queue.add(sr);
        AppController.getInstance().addToRequestQueue(sr, "string_req", getActivity());
    }

    private void appointmentresponse(String response){
        try {
            OwnerAppointmentsList.clear();
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String ShopId = jsonObject.getString("ShopId");
                String Employee = jsonObject.getString("Employee");
                String StartTime = jsonObject.getString("StartTime");
                String EndTime = jsonObject.getString("EndTime");
                String CustomerName = jsonObject.getString("CustomerName");
                String CustomerImage = jsonObject.getString("CustomerImage");
                String EmpName = jsonObject.getString("EmpName");
                String ServiceName = jsonObject.getString("ServiceName");
                String TimeInMin = jsonObject.getString("TimeInMin");
                Log.e("id","#"+id);

                OwnerAppointmentsList.add(new OwnerAppointments(id, StartTime, EndTime, ServiceName,CustomerName));
            }

            if (OwnerAppointmentsList.size() >= 1) {
                rootView.findViewById(R.id.txt_1).setVisibility(View.GONE);
                rootView.findViewById(R.id.recycle_appointment).setVisibility(View.VISIBLE);
                doProcess(OwnerAppointmentsList);
            }else{
                rootView.findViewById(R.id.txt_1).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.recycle_appointment).setVisibility(View.GONE);
            }
        }catch (Exception e){
            rootView.findViewById(R.id.txt_1).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.recycle_appointment).setVisibility(View.GONE);
        }
    }

    private void doProcess(ArrayList<OwnerAppointments> ownerAppointmentsList){
        OwnerAppointmentAdapter adapter = new OwnerAppointmentAdapter(getActivity(), ownerAppointmentsList);
        recycler_appointment.setAdapter(adapter);
    }
}
