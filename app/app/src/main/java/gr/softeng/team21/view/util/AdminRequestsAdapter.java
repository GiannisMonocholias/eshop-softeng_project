package gr.softeng.team21.view.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.view.admin.requests.requestDetails.RequestDetailsActivity;

/**
 * Adapter for rendering the list of catalogue update requests.
 * Handles user clicks to open the detailed view of a request.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class AdminRequestsAdapter extends RecyclerView.Adapter<AdminRequestsAdapter.ViewHolder> {

    private final List<CatalogueUpdateRequest> requests;

    public AdminRequestsAdapter(List<CatalogueUpdateRequest> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CatalogueUpdateRequest request = requests.get(position);
        Context context = holder.itemView.getContext();

        holder.txtItemReqType.setText(request.getType().toString());
        holder.txtItemReqDate.setText(request.getSubmissionDate().toString());

        String productName = request.getProduct() != null ? request.getProduct().getProductname() : "Άγνωστο Προϊόν";
        holder.txtItemReqProduct.setText(productName);

        holder.txtItemReqDesc.setText(request.getUpdateDescription());

        // Με κλικ ανοίγει η οθόνη λεπτομερειών
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RequestDetailsActivity.class);
            intent.putExtra("REQ_ID", String.valueOf(request.getId()));
            intent.putExtra("REQ_TYPE", request.getType().toString());
            intent.putExtra("REQ_DATE", request.getSubmissionDate().toString());
            intent.putExtra("REQ_PRODUCT", productName);
            intent.putExtra("REQ_DESC", request.getUpdateDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (requests != null) ? requests.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemReqType, txtItemReqDate, txtItemReqProduct, txtItemReqDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemReqType = itemView.findViewById(R.id.txtItemReqType);
            txtItemReqDate = itemView.findViewById(R.id.txtItemReqDate);
            txtItemReqProduct = itemView.findViewById(R.id.txtItemReqProduct);
            txtItemReqDesc = itemView.findViewById(R.id.txtItemReqDesc);
        }
    }
}