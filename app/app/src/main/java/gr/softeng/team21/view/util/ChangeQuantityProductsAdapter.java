package gr.softeng.team21.view.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

/**
 * RecyclerView Adapter responsible for rendering the list of products and handling
 * user interactions to modify warehouse stock quantities dynamically.
 * Features a modern, distinct dual-button (+/-) UI pattern.
 *
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public class ChangeQuantityProductsAdapter extends RecyclerView.Adapter<ChangeQuantityProductsAdapter.ViewHolder> {

    private final List<ProductType> products;

    /**
     * Constructs the adapter with a provided list of products.
     *
     * @param products The dataset of ProductType objects to display.
     */
    public ChangeQuantityProductsAdapter(List<ProductType> products) {
        this.products = products;
    }

    /**
     * Inflates the custom XML layout for individual list items.
     *
     * @param parent   The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new instance of {@link ViewHolder}.
     */
    @NonNull
    @Override
    public ChangeQuantityProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_quantity, parent, false);
        return new ChangeQuantityProductsAdapter.ViewHolder(view);
    }

    /**
     * Binds data to the views inside the ViewHolder and sets up the event listeners
     * for adjusting product stock using the memory DAO.
     *
     * @param holder   The ViewHolder to update.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ChangeQuantityProductsAdapter.ViewHolder holder, int position) {
        ProductType product = products.get(position);

        holder.txtProductName.setText(product.getProductname());
        holder.txtProductCode.setText("Κωδικός: " + product.getProductCode());

        // 1. Display Current Stock
        Integer currentStock = ProductsWareHouseDAOMemory.getInstance().getProductStock(product);
        holder.txtCurrentStock.setText(currentStock != null ? currentStock + " τεμ." : "0 τεμ.");

        // 2. Remove Stock Button Logic (-)
        holder.btnApplyRemove.setOnClickListener(v -> {
            int changeAmt = getAmountFromInput(holder.edtChangeAmount);
            boolean success = ProductsWareHouseDAOMemory.getInstance().decreaseProductStock(product, changeAmt);

            if (success) {
                // Update UI instantly
                holder.txtCurrentStock.setText(ProductsWareHouseDAOMemory.getInstance().getProductStock(product) + " τεμ.");
                holder.edtChangeAmount.setText("1"); // Reset input to 1
            } else {
                Toast.makeText(v.getContext(), "Μη επαρκές απόθεμα για αφαίρεση!", Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Add Stock Button Logic (+)
        holder.btnApplyAdd.setOnClickListener(v -> {
            int changeAmt = getAmountFromInput(holder.edtChangeAmount);
            boolean success = ProductsWareHouseDAOMemory.getInstance().increaseProductStock(product, changeAmt);

            if (success) {
                // Update UI instantly
                holder.txtCurrentStock.setText(ProductsWareHouseDAOMemory.getInstance().getProductStock(product) + " τεμ.");
                holder.edtChangeAmount.setText("1"); // Reset input to 1
            }
        });
    }

    /**
     * @return The total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return (products != null) ? products.size() : 0;
    }

    /**
     * Helper method to safely parse the input from the EditText.
     * Prevents crashes by defaulting to 1 if the input is empty or invalid.
     *
     * @param edt The EditText instance to read from.
     * @return The parsed positive integer, defaulting to 1.
     */
    private int getAmountFromInput(EditText edt) {
        String input = edt.getText().toString().trim();
        if (input.isEmpty()) {
            return 1;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    /**
     * ViewHolder pattern for caching view references and optimizing list rendering.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductCode, txtCurrentStock;
        EditText edtChangeAmount;
        MaterialButton btnApplyRemove, btnApplyAdd;

        /**
         * Binds the XML components to their respective Java object references.
         * @param itemView The View containing the item layout.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductCode = itemView.findViewById(R.id.txtProductCode);
            txtCurrentStock = itemView.findViewById(R.id.txtCurrentStock);
            edtChangeAmount = itemView.findViewById(R.id.edtChangeAmount);

            btnApplyRemove = itemView.findViewById(R.id.btnApplyRemove);
            btnApplyAdd = itemView.findViewById(R.id.btnApplyAdd);
        }
    }
}