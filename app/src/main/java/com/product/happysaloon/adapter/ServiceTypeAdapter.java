package com.product.happysaloon.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.product.happysaloon.FixAppointmentActivity;
import com.product.happysaloon.R;
import com.product.happysaloon.model.Servicetypes;

import java.util.ArrayList;
import java.util.List;

public class ServiceTypeAdapter implements SpinnerAdapter {
    Context context;
    int flag;
    ArrayList<Servicetypes> servicetypeList;

    public ServiceTypeAdapter(Context context, ArrayList<Servicetypes> servicetypeList) {
        this.context = context;
        this.servicetypeList = servicetypeList;
        this.flag = flag;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return servicetypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return servicetypeList.get(position);
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

        textView.setText(servicetypeList.get(position).getServiceName());
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
        Holder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_spinner, parent, false);
            holder = new Holder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.txtName.setText(servicetypeList.get(position).getServiceName());
        return convertView;
    }

    static class Holder {
        TextView txtName;
    }
}



