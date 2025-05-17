package com.example.idopontfoglalo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.idopontfoglalo.R;
import com.example.idopontfoglalo.models.Device;
import android.widget.ImageButton;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    public interface DeviceActionListener {
        void onEdit(Device device, int position);
        void onDelete(Device device, int position);
    }

    private List<Device> deviceList;
    private DeviceActionListener listener;

    public DeviceAdapter(List<Device> deviceList, DeviceActionListener listener) {
        this.deviceList = deviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.textBrand.setText(device.getBrand());
        holder.textModel.setText(device.getModel());
        holder.textType.setText(device.getType());

        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(device, position);
        });
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(device, position);
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView textBrand, textModel, textType;
        ImageButton buttonEdit, buttonDelete;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            textBrand = itemView.findViewById(R.id.textBrand);
            textModel = itemView.findViewById(R.id.textModel);
            textType = itemView.findViewById(R.id.textType);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}