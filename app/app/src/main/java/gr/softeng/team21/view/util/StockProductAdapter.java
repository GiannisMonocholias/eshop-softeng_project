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
import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

/**
 * Adapter for displaying products in the stock verification screen during order preparation.
 * It compares the requested quantity from the cart with the actual warehouse stock.
 * @author Γιάννης Μονοχολιάς
 */
public class StockProductAdapter extends RecyclerView.Adapter<StockProductAdapter.ViewHolder> {

    private final List<CartItem> cartItems;
    private final ProductsWareHouseDAO wareHouseDAO;

    /**
     * Constructor for StockProductAdapter.
     * @param cartItems The list of items contained in the order.
     */
    public StockProductAdapter(List<CartItem> cartItems, ProductsWareHouseDAO wareHouseDAO) {
        this.cartItems = cartItems;
        this.wareHouseDAO = wareHouseDAO;
    }

    /**
     * Inflates the layout for the stock product item.
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder instance.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_stock, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the product and stock data to the UI components.
     * Highlights the stock quantity in RED if it is less than the requested quantity.
     * @param holder The ViewHolder to update.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        ProductType product = item.getProductType();

        holder.txtCodeValue.setText(product.getProductCode());
        holder.txtNameValue.setText(product.getProductname());

        if (product.getPrice() != null) {
            holder.txtPriceValue.setText(product.getPrice().getAmount() + " €");
        } else {
            holder.txtPriceValue.setText("-");
        }

        int reqQty = item.getQuantity();
        holder.txtReqValue.setText(String.valueOf(reqQty));

        wareHouseDAO.getProductStock(product).thenAccept(currentStock -> {
            int stockQty = (currentStock != null) ? currentStock : 0;

            runOnMainThread(() -> {
                holder.txtStockValue.setText(String.valueOf(stockQty));

                if (reqQty > stockQty) {
                    holder.txtStockValue.setTextColor(android.graphics.Color.RED);
                } else {
                    holder.txtStockValue.setTextColor(android.graphics.Color.parseColor("#2E7D32"));
                }
            });
        });

    }

    /**
     * Returns the total number of items in the cart.
     * @return The size of the cart items list.
     */
    @Override
    public int getItemCount() {
        return (cartItems != null) ? cartItems.size() : 0;
    }

    /**
     * Helper method to safely execute UI updates on the main thread from within an Adapter.
     * Acts as a replacement for Activity's runOnUiThread().
     *
     * @param action The runnable task to execute on the main thread.
     */
    private void runOnMainThread(Runnable action) {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(action);
    }

    /**
     * ViewHolder class for caching the UI components of a stock product item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodeValue, txtNameValue, txtPriceValue, txtReqValue, txtStockValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCodeValue = itemView.findViewById(R.id.txtItemProductStockProductCodeValue);
            txtNameValue = itemView.findViewById(R.id.txtItemProductStockProductNameValue);
            txtPriceValue = itemView.findViewById(R.id.txtItemProductStockSubTotalValue);
            txtReqValue = itemView.findViewById(R.id.txtItemProductStockRequestedQuantityValue);
            txtStockValue = itemView.findViewById(R.id.txtItemProductStockStockQuantityValue);
        }
    }
}