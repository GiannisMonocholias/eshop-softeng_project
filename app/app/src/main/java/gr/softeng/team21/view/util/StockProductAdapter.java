package gr.softeng.team21.view.util;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.ProductType;

public class StockProductAdapter extends RecyclerView.Adapter<StockProductAdapter.ViewHolder> {

    // Πλέον έχουμε μόνο μία λίστα από CartItems (που περιέχουν το προϊόν και την ποσότητα)
    private final List<CartItem> cartItems;

    // Constructor: Δέχεται μόνο τη λίστα των CartItems
    public StockProductAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Συνδέουμε με το XML layout της γραμμής
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Παίρνουμε το αντικείμενο CartItem της τρέχουσας θέσης
        CartItem item = cartItems.get(position);
        ProductType product = item.getProductType(); // Παίρνουμε το προϊόν από το item

        // 1. Ανάθεση Κωδικού & Ονόματος
        holder.txtCodeValue.setText(product.getProductCode());
        holder.txtNameValue.setText(product.getProductname());

        // 2. Ανάθεση Τιμής
        // Υποθέτουμε ότι η κλάση Money έχει getAmount(), αλλιώς χρησιμοποίησε .toString()
        if (product.getPrice() != null) {
            holder.txtPriceValue.setText(product.getPrice().getAmount() + " €");
        } else {
            holder.txtPriceValue.setText("-");
        }

        // 3. Ποσότητες
        int reqQty = item.getQuantity(); // Η ζητούμενη ποσότητα από το καλάθι
        int stockQty = 50; // <--- Η ΤΥΧΑΙΑ ΤΙΜΗ ΠΟΥ ΖΗΤΗΣΕΣ (Hardcoded)

        holder.txtReqValue.setText(String.valueOf(reqQty));
        holder.txtStockValue.setText(String.valueOf(stockQty));

        // 4. Λογική χρωμάτων: Κόκκινο αν το απόθεμα δεν επαρκεί
        if (reqQty > stockQty) {
            holder.txtStockValue.setTextColor(Color.RED);
        } else {
            // Πράσινο (#2E7D32)
            holder.txtStockValue.setTextColor(Color.parseColor("#2E7D32"));
        }
    }

    @Override
    public int getItemCount() {
        // Επιστρέφουμε το μέγεθος της λίστας, ή 0 αν είναι null
        return (cartItems != null) ? cartItems.size() : 0;
    }

    // Η κλάση ViewHolder κρατάει τα στοιχεία του UI για κάθε γραμμή
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodeValue, txtNameValue, txtPriceValue, txtReqValue, txtStockValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Σύνδεση με τα IDs από το XML σου (όπως τα έδωσες)
            txtCodeValue = itemView.findViewById(R.id.txtItemProductStockProductCodeValue);
            txtNameValue = itemView.findViewById(R.id.txtItemProductStockProductNameValue);
            txtPriceValue = itemView.findViewById(R.id.txtItemProductStockSubTotalValue);
            txtReqValue = itemView.findViewById(R.id.txtItemProductStockRequestedQuantityValue);
            txtStockValue = itemView.findViewById(R.id.txtItemProductStockStockQuantityValue);
        }
    }
}