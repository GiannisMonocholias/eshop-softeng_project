package gr.softeng.team21.view.util;

import android.graphics.Color;
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

/**
 * RecyclerView Adapter for displaying Catalogue Update Requests.
 * This adapter supports two modes: assigning a new request to an employee
 * and executing/serving an already assigned request.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateRequestsAdapter extends RecyclerView.Adapter<UpdateRequestsAdapter.ViewHolder> {

    /**
     * Interface definition for a callback to be invoked when the action button
     * on a request item is clicked.
     */
    public interface OnRequestClickListener {
        /**
         * Triggered when the user clicks the main action button (Assign or Execute).
         * @param request The CatalogueUpdateRequest associated with the clicked item.
         */
        void onActionClick(CatalogueUpdateRequest request);
    }

    private final List<CatalogueUpdateRequest> requests;
    private final OnRequestClickListener listener;
    private final UpdateRequestAdapterTypes listType;

    /**
     * Initializes the adapter with the request list, the specific list mode, and a listener.
     * @param requests The list of update requests to display.
     * @param listType The mode of the adapter (ASSIGN_REQUEST or EXECUTE_REQUEST).
     * @param listener The callback for button click events.
     */
    public UpdateRequestsAdapter(List<CatalogueUpdateRequest> requests, UpdateRequestAdapterTypes listType, OnRequestClickListener listener) {
        this.requests = requests;
        this.listType = listType;
        this.listener = listener;
    }

    /**
     * Inflates the layout for individual catalogue update request items.
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assigned_catalogue_update_request, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds request data to the UI components.
     * Dynamically sets button text based on the {@link UpdateRequestAdapterTypes} provided.
     * @param holder   The ViewHolder to update.
     * @param position The position of the item within the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CatalogueUpdateRequest request = requests.get(position);

        holder.txtId.setText("REQ #" + request.getId());
        if (request.getSubmissionDate() != null) holder.txtDate.setText(request.getSubmissionDate().toString());
        if (request.getType() != null) holder.txtType.setText(request.getType().toString());

        if (request.getProduct() != null) {
            holder.txtProductName.setText(request.getProduct().getProductname());
            holder.txtProductCode.setText("Code: " + request.getProduct().getProductCode());
        }

        holder.txtDesc.setText(request.getUpdateDescription());

        // Context-aware button text
        if (listType == UpdateRequestAdapterTypes.ASSIGN_REQUEST) {
            holder.btnExecute.setText("ASSIGN REQUEST");
        } else {
            holder.btnExecute.setText("SERVE REQUEST");
        }

        holder.btnExecute.setOnClickListener(v -> {
            if (listener != null) {
                listener.onActionClick(request);
            }
        });
    }

    /**
     * @return The total number of requests in the adapter's data set.
     */
    @Override
    public int getItemCount() {
        return requests.size();
    }

    /**
     * Removes a request from the list and notifies the RecyclerView of the removal.
     * @param request The request object to be removed from the UI.
     */
    public void removeRequest(CatalogueUpdateRequest request) {
        int position = requests.indexOf(request);
        if (position != -1) {
            requests.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * ViewHolder class for caching UI component references of a catalogue update request item.
     */
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

            btnExecute = itemView.findViewById(R.id.btnItemRequestExecute);
        }
    }
}