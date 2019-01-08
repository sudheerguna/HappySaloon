package com.product.happysaloon.menuslide;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.product.happysaloon.R;
import com.product.happysaloon.cview.TypefaceTextview;
import com.product.happysaloon.model.AppointmentAvailability;
import com.product.happysaloon.utils.Constant;

import java.util.ArrayList;

class AppointmentAvailabilityAdapter extends RecyclerView.Adapter<AppointmentAvailabilityAdapter.ViewHolder>{
    ArrayList<AppointmentAvailability> appointmentAvailabilityList;
    Context context;
    public AppointmentAvailabilityAdapter(Context context, ArrayList<AppointmentAvailability> appointmentAvailabilityList) {
        this.appointmentAvailabilityList = appointmentAvailabilityList;
        this.context = context;
    }
    @Override
    public AppointmentAvailabilityAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.appointmentlist, viewGroup, false);
        return new AppointmentAvailabilityAdapter.ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(AppointmentAvailabilityAdapter.ViewHolder holder, int position) {
        holder.txt_id.setVisibility(View.GONE);
        holder.txt_ServiceName.setVisibility(View.GONE);
        holder.txt_StartTime.setText("Start Time - "+appointmentAvailabilityList.get(position).getStartTime());
        holder.txt_EndTime.setText("End Time - "+appointmentAvailabilityList.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return appointmentAvailabilityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TypefaceTextview txt_id, txt_StartTime, txt_EndTime,txt_ServiceName;

        public ViewHolder(View v) {
            super(v);
            txt_id = (TypefaceTextview) v.findViewById(R.id.txt_id);
            txt_StartTime = (TypefaceTextview) v.findViewById(R.id.txt_StartTime);
            txt_EndTime = (TypefaceTextview) v.findViewById(R.id.txt_EndTime);
            txt_ServiceName = (TypefaceTextview) v.findViewById(R.id.txt_ServiceName);
        }
    }

}

