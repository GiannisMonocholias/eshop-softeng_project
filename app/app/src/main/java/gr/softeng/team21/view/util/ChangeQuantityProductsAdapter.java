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
import android.os.Handler;
import android.os.Looper;
import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.domain.ProductType;

/**
 * A sophisticated RecyclerView Adapter responsible for rendering the inventory list
 * and handling dynamic quantity adjustments (add/remove) for each product.
 *
 * It interacts directly with the {@link ProductsWareHouseDAO} to fetch current stock
 * levels asynchronously and applies user-requested modifications in real-time.
 *
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public class ChangeQuantityProductsAdapter extends RecyclerView.Adapter<ChangeQuantityProductsAdapter.ViewHolder> {

    private final List<ProductType> products;
    private final ProductsWareHouseDAO wareHouseDAO;

    /**
     * Constructs the adapter with a predefined list of products and a Data Access Object.
     *
     * @param products     The collection of {@link ProductType} objects to be displayed.
     * @param wareHouseDAO The DAO interface handling the asynchronous warehouse stock operations.
     */
    public ChangeQuantityProductsAdapter(List<ProductType> products, ProductsWareHouseDAO wareHouseDAO) {
        this.products = products;
        this.wareHouseDAO = wareHouseDAO;
    }

    /**
     * Called when the RecyclerView needs a new {@link ViewHolder} of the given type to represent an item.
     * Inflates the custom XML layout specifically designed for the product quantity UI.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A newly created {@link ViewHolder} that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ChangeQuantityProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_quantity, parent, false);
        return new ChangeQuantityProductsAdapter.ViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display the data at the specified position.
     * This method updates the contents of the {@link ViewHolder#itemView} to reflect the item at the given position.
     * It also attaches the necessary click listeners for the add and remove stock operations.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ChangeQuantityProductsAdapter.ViewHolder holder, int position) {
        ProductType product = products.get(position);

        // Bind static product information
        holder.txtProductName.setText(product.getProductName());
        holder.txtProductCode.setText("Κωδικός: " + product.getProductCode());

        // Fetch and display the initial current stock from the database asynchronously
        wareHouseDAO.getProductStock(product).thenAccept(currentStock -> {
            int stock = (currentStock != null) ? currentStock : 0;
            runOnMainThread(() -> holder.txtCurrentStock.setText(stock + " τεμ."));
        });

        // Setup interaction logic for the "Remove Stock" (-) button
        holder.btnApplyRemove.setOnClickListener(v -> {
            int changeAmt = getAmountFromInput(holder.edtChangeAmount);

            wareHouseDAO.decreaseProductStock(product, changeAmt).thenAccept(success -> {
                if (success) {
                    // Re-fetch the updated stock to ensure UI synchronization with the backend
                    wareHouseDAO.getProductStock(product).thenAccept(newStock -> {
                        runOnMainThread(() -> {
                            holder.txtCurrentStock.setText(newStock + " τεμ.");
                            holder.edtChangeAmount.setText("1"); // Reset input field to default
                        });
                    });
                } else {
                    // Display error if stock goes below zero
                    runOnMainThread(() -> Toast.makeText(holder.itemView.getContext(), "Μη επαρκές απόθεμα για αφαίρεση!", Toast.LENGTH_SHORT).show());
                }
            });
        });

        // Setup interaction logic for the "Add Stock" (+) button
        holder.btnApplyAdd.setOnClickListener(v -> {
            int changeAmt = getAmountFromInput(holder.edtChangeAmount);

            wareHouseDAO.increaseProductStock(product, changeAmt).thenAccept(success -> {
                if (success) {
                    // Re-fetch the updated stock to ensure UI synchronization with the backend
                    wareHouseDAO.getProductStock(product).thenAccept(newStock -> {
                        runOnMainThread(() -> {
                            holder.txtCurrentStock.setText(newStock + " τεμ.");
                            holder.edtChangeAmount.setText("1"); // Reset input field to default
                        });
                    });
                }
            });
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The size of the products list, or 0 if the list is null.
     */
    @Override
    public int getItemCount() {
        return (products != null) ? products.size() : 0;
    }

    /**
     * A thread-safe utility method designed to execute UI-related operations on the main thread.
     * Crucial for updating Android UI components from within asynchronous DAO callbacks.
     *
     * @param action The specific task or UI update to be executed.
     */
    private void runOnMainThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

    /**
     * Safely extracts and parses the user input from the quantity text field.
     * In the event of an empty field or non-numeric input, it gracefully falls back to a default value of 1.
     *
     * @param edt The {@link EditText} component containing the user's requested quantity change.
     * @return The parsed positive integer value, defaulting to 1 if invalid.
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
     * The ViewHolder class which caches the views associated with a single item in the RecyclerView.
     * This pattern improves scrolling performance by avoiding repeated runtime findViewById() calls.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /** TextView displaying the product's name. */
        TextView txtProductName;

        /** TextView displaying the product's unique code. */
        TextView txtProductCode;

        /** TextView displaying the currently available stock quantity. */
        TextView txtCurrentStock;

        /** EditText allowing the user to specify the amount to add or remove. */
        EditText edtChangeAmount;

        /** Button triggering the stock decrement operation. */
        MaterialButton btnApplyRemove;

        /** Button triggering the stock increment operation. */
        MaterialButton btnApplyAdd;

        /**
         * Initializes the ViewHolder components by mapping them to their XML layout IDs.
         *
         * @param itemView The instantiated View of the XML layout for a single item.
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