package com.product.happysaloon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.product.happysaloon.R;
import com.product.happysaloon.cview.TypefaceTextview;
import com.product.happysaloon.model.AvailableAppointments;

import java.util.ArrayList;

import static com.product.happysaloon.menuslide.UserHomeFragment.showpopup;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>{
    Context context;
    ArrayList<AvailableAppointments> availableAppointmentsList;
    public AppointmentAdapter(Context context, ArrayList<AvailableAppointments> availableAppointmentsList) {
        this.availableAppointmentsList = availableAppointmentsList;
        this.context = context;
    }

    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.appointmentlist, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.ViewHolder holder, final int position) {
        holder.txt_id.setText("Id - "+availableAppointmentsList.get(position).getId());
        holder.txt_StartTime.setText("Start Time - "+availableAppointmentsList.get(position).getStartTime());
        holder.txt_EndTime.setText("End Time - "+availableAppointmentsList.get(position).getEndTime());
        holder.txt_ServiceName.setText("Service Name - "+availableAppointmentsList.get(position).getServiceName());
        holder.img_cancel.setVisibility(View.VISIBLE);
        holder.img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup(availableAppointmentsList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return availableAppointmentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TypefaceTextview txt_id, txt_StartTime, txt_EndTime,txt_ServiceName;
        ImageView img_cancel;

        public ViewHolder(View v) {
            super(v);
            txt_id = (TypefaceTextview) v.findViewById(R.id.txt_id);
            txt_StartTime = (TypefaceTextview) v.findViewById(R.id.txt_StartTime);
            txt_EndTime = (TypefaceTextview) v.findViewById(R.id.txt_EndTime);
            txt_ServiceName = (TypefaceTextview) v.findViewById(R.id.txt_ServiceName);
            img_cancel = (ImageView) v.findViewById(R.id.img_cancel);
        }
    }
}
