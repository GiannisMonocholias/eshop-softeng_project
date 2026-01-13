package gr.softeng.team21.view.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CartItem;

/**
 * Adapter responsible for displaying the list of items in the shopping cart within a RecyclerView.
 * It manages the binding of {@link CartItem} data to the UI views and handles user interactions
 * such as increasing, decreasing or deleting items via the {@link CartListener}.
 * @author PAVLOS GRATSANIS
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>{
    private List<CartItem> cartItems;
    private CartListener listener;

    /**
     * Interface to handle actions on cart items.
     * Defines callbacks for increasing quantity, decreasing quantity or deleting an item.
     */
    public interface CartListener {
        void plus(CartItem item);
        void minus(CartItem item);
        void delete(CartItem item);
    }

    /**
     * Initializes the adapter with the list of cart items and  listener.
     * @param cartItems The list of items to display in the cart.
     * @param listener The listener to handle click events on the items.
     */
    public ShoppingCartAdapter(List<CartItem> cartItems,CartListener listener) {
        this.cartItems = cartItems;
        this.listener=listener;
    }

    /**
     * {@inheritDoc}
     * Inflates the layout for individual cart items.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingcart_list_item, parent, false));
    }

    /**
     * {@inheritDoc}
     * Binds the data of a specific {@link CartItem} to the view elements.
     * Calculates the total price for the item, formats it with the correct currency.
     * It also sets up listeners for the increment, decrement, and delete buttons.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        BigDecimal unitPrice = item.getProductType().getPrice().getAmount();
        String currency = item.getProductType().getPrice().getCurrency();
        int quantity = item.getQuantity();
        BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(quantity));

        holder.txtName.setText(item.getProductType().getProductname());
        holder.txtSubTotalPrice.setText(String.format("%.2f %s", totalPrice, currency));
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> listener.plus(item));
        holder.btnMinus.setOnClickListener(v -> listener.minus(item));
        holder.btnDelete.setOnClickListener(v -> listener.delete(item));
    }


    /**
     * {@inheritDoc}
     * Returns the total number of items in the cart.
     */
    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    /**
     * ViewHolder class that holds references to the UI components of a cart item layout.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtQuantity,txtName,txtSubTotalPrice;
        public Button btnMinus,btnPlus;
        public ImageButton btnDelete;

        /**
         * Initializes the ViewHolder and binds the UI components of the cart item layout.
         * Retrieves references to the text views (name, price, quantity)
         * and the action buttons (plus, minus, delete) from the provided row view.
         * @param rowView The view representing a single row in the recycler view.
         */
        public ViewHolder(@NonNull View rowView) {
            super(rowView);
            txtName = rowView.findViewById(R.id.txtCustomerCartItemActivityName);
            txtSubTotalPrice = rowView.findViewById(R.id.txtCustomerCartItemActivitySubTotalPrice);
            txtQuantity = rowView.findViewById(R.id.txtCustomerCartItemActivityQuantity);

            btnPlus = rowView.findViewById(R.id.btnCustomerCartItemActivityPlus);
            btnMinus = rowView.findViewById(R.id.btnCustomerCartItemActivityMinus);
            btnDelete = rowView.findViewById(R.id.btnCustomerCartItemActivityDelete);
        }
    }
}