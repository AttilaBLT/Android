package com.example.idopontfoglalo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.idopontfoglalo.R;
import com.example.idopontfoglalo.models.Appointment;
import com.example.idopontfoglalo.models.AppointmentDisplay;
import com.google.firebase.Timestamp;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private List<AppointmentDisplay> appointmentList;

    public AppointmentAdapter(List<AppointmentDisplay> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentDisplay appointment = appointmentList.get(position);
        holder.textViewDate.setText(appointment.date);
        holder.textViewStatus.setText(appointment.status);
        holder.textViewDevice.setText(appointment.deviceName);
        holder.textViewService.setText(appointment.serviceName);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewStatus, textViewDevice, textViewService;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewDevice = itemView.findViewById(R.id.textViewDevice);
            textViewService = itemView.findViewById(R.id.textViewService);
        }
    }
}