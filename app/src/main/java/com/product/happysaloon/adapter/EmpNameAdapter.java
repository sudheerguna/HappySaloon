package com.product.happysaloon.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.product.happysaloon.R;
import com.product.happysaloon.model.EmpNames;
import com.product.happysaloon.model.Servicetypes;
import com.product.happysaloon.utils.Constant;

import java.util.ArrayList;

public class EmpNameAdapter implements SpinnerAdapter {
    Context context;
    ArrayList<EmpNames> empNameList;

    public EmpNameAdapter(Context context, ArrayList<EmpNames> empNameList) {
        this.context = context;
        this.empNameList = empNameList;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return empNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return empNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), 30, textView.getPaddingBottom());

        textView.setText(empNameList.get(position).getEmpname());
        textView.setTextColor(Color.parseColor("#000000"));

        return textView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ServiceTypeAdapter.Holder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_spinner, parent, false);
            holder = new ServiceTypeAdapter.Holder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            convertView.setTag(holder);
        } else {
            holder = (ServiceTypeAdapter.Holder) convertView.getTag();
        }
        holder.txtName.setText(empNameList.get(position).getEmpname());
        return convertView;
    }

    static class Holder {
        TextView txtName;
    }
}




