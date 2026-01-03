package gr.softeng.team21.view.util;

import android.graphics.Color; // Για αλλαγή χρώματος αν θες
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

public class UpdateRequestsAdapter extends RecyclerView.Adapter<UpdateRequestsAdapter.ViewHolder> {

    public interface OnRequestClickListener {
        void onActionClick(CatalogueUpdateRequest request);
    }

    private final List<CatalogueUpdateRequest> requests;
    private final OnRequestClickListener listener;
    private final UpdateRequestAdapterTypes listType; // <--- ΝΕΟ: Αποθηκεύουμε τον τύπο της λίστας

    // --- ΤΡΟΠΟΠΟΙΗΜΕΝΟΣ CONSTRUCTOR ---
    public UpdateRequestsAdapter(List<CatalogueUpdateRequest> requests, UpdateRequestAdapterTypes listType, OnRequestClickListener listener) {
        this.requests = requests;
        this.listType = listType; // <--- Παίρνουμε τον τύπο από την Activity
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Χρησιμοποιούμε το ίδιο layout και για τα δύο
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assigned_catalogue_update_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CatalogueUpdateRequest request = requests.get(position);

        // ... (Τα setTexts για ID, Date, Product μένουν ίδια) ...
        holder.txtId.setText("REQ #" + request.getId());
        if (request.getSubmissionDate() != null) holder.txtDate.setText(request.getSubmissionDate().toString());
        if (request.getType() != null) holder.txtType.setText(request.getType().toString());
        if (request.getProduct() != null) {
            holder.txtProductName.setText(request.getProduct().getProductname());
            holder.txtProductCode.setText("Κωδικός: " + request.getProduct().getProductCode());
        }
        holder.txtDesc.setText(request.getUpdateDescription());

        // --- Η ΛΟΓΙΚΗ ΓΙΑ ΤΟ ΚΟΥΜΠΙ ---
        if (listType == UpdateRequestAdapterTypes.ASSIGN_REQUEST) {
            // Περίπτωση: Διαθέσιμα αιτήματα
            holder.btnExecute.setText("ΑΝΑΛΗΨΗ ΑΙΤΗΜΑΤΟΣ");
            // Προαιρετικά: Αλλάζεις και χρώμα για να ξεχωρίζει
            // holder.btnExecute.setBackgroundColor(Color.parseColor("#4CAF50")); // Πράσινο
        } else {
            // Περίπτωση: Δικά μου αιτήματα
            holder.btnExecute.setText("ΕΞΥΠΗΡΕΤΗΣΗ ΑΙΤΗΜΑΤΟΣ");
            // holder.btnExecute.setBackgroundColor(Color.parseColor("#2196F3")); // Μπλε
        }

        // Το Click Listener είναι ίδιο! Η Activity αποφασίζει τι θα κάνει.
        holder.btnExecute.setOnClickListener(v -> {
            if (listener != null) {
                listener.onActionClick(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtDate, txtType, txtProductName, txtProductCode, txtDesc;
        Button btnExecute;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtItemRequestId);
            txtDate = itemView.findViewById(R.id.txtItemRequestDate);
            txtType = itemView.findViewById(R.id.txtItemRequestType);
            txtProductName = itemView.findViewById(R.id.txtItemRequestProductName);
            txtProductCode = itemView.findViewById(R.id.txtItemRequestProductCode);
            txtDesc = itemView.findViewById(R.id.txtItemRequestDesc);

            // Κρατάμε το ίδιο ID στο XML, απλά αλλάζουμε το κείμενο με κώδικα
            btnExecute = itemView.findViewById(R.id.btnItemRequestExecute);
        }
    }
}