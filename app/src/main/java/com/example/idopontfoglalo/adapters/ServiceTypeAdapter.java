package com.example.idopontfoglalo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.idopontfoglalo.R;
import com.example.idopontfoglalo.models.ServiceType;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ServiceTypeAdapter extends RecyclerView.Adapter<ServiceTypeAdapter.ServiceTypeViewHolder> {
    private List<ServiceType> serviceTypes;
    private List<String> ids;
    private Context context;
    private OnEditListener onEditListener;

    public interface OnEditListener {
        void onEdit(ServiceType serviceType, String id);
    }

    public ServiceTypeAdapter(List<ServiceType> serviceTypes, List<String> ids, Context context, OnEditListener onEditListener) {
        this.serviceTypes = serviceTypes;
        this.ids = ids;
        this.context = context;
        this.onEditListener = onEditListener;
    }

    @NonNull
    @Override
    public ServiceTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicetype, parent, false);
        return new ServiceTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceTypeViewHolder holder, int position) {
        ServiceType st = serviceTypes.get(position);
        String id = ids.get(position);
        holder.textName.setText(st.getName());
        holder.textDuration.setText(st.getDuration() + " perc");
        holder.textPrice.setText(st.getPrice() + " Ft");

        holder.buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Törlés")
                    .setMessage("Biztosan törlöd?")
                    .setPositiveButton("Igen", (dialog, which) -> {
                        FirebaseFirestore.getInstance().collection("serviceTypes").document(id).delete();
                    })
                    .setNegativeButton("Mégse", null)
                    .show();
        });

        holder.buttonEdit.setOnClickListener(v -> {
            if (onEditListener != null) onEditListener.onEdit(st, id);
        });
    }

    @Override
    public int getItemCount() {
        return serviceTypes.size();
    }

    public static class ServiceTypeViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDuration, textPrice;
        ImageButton buttonEdit, buttonDelete;
        public ServiceTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDuration = itemView.findViewById(R.id.textDuration);
            textPrice = itemView.findViewById(R.id.textPrice);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}