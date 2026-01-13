package gr.softeng.team21.view.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Adapter class for managing and displaying a list of {@link Order} objects within a RecyclerView.
 * This adapter supports multiple display modes defined by {@link OrderAdapterType},
 * allowing different button labels and actions depending on whether the user is
 * assigning, notifying, or preparing an order.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OnOrderClickListener listener;
    private OrderAdapterType type;

    /**
     * Interface definition for a callback to be invoked when an order action button is clicked.
     */
    public interface OnOrderClickListener {
        /**
         * Called when the action button of a specific order item is clicked.
         * @param order The {@link Order} object associated with the clicked item.
         */
        void onAssignClick(Order order);
    }

    /**
     * Constructs a new OrderAdapter.
     * @param orders   The list of orders to display.
     * @param type     The functional type of the adapter (Assign, Notify, or Assigned).
     * @param listener The listener for handling button click events.
     */
    public OrderAdapter(List<Order> orders, OrderAdapterType type, OnOrderClickListener listener) {
        this.orders = (orders != null) ? orders : new java.util.ArrayList<>();
        this.listener = listener;
        this.type = type;
    }

    /**
     * Called when RecyclerView needs a new {@link OrderViewHolder} of the given type to represent an item.
     * @param parent   The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new OrderViewHolder that holds a View for an order item.
     */
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method updates the contents of the {@link OrderViewHolder#itemView} to reflect the
     * order information and configures the action button based on the {@link OrderAdapterType}.
     * @param holder   The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Populate basic order details
        holder.txtOrderIdValue.setText("#" + order.getOrdercode());
        holder.txtCustomerNameValue.setText(order.getShoppingCart().getCustomer().getLastname() + " " +
                order.getShoppingCart().getCustomer().getFirstname());
        holder.txtPriceValue.setText(order.getTotal_amount().toString());
        holder.txtDateValue.setText(order.getSubmissiondate().toString());
        holder.txtStatus.setText(order.getOrderstatus().toString());

        // Configure the action button text based on the adapter's functional context
        switch(type){
            case ASSIGN_ORDER_ADAPTER:
                holder.btnItemOrder.setText("Ανάληψη παραγγελίας");
                break;
            case NOTIFY_ORDER_ADAPTER:
                if(order.getOrderstatus() == OrderStatusType.DELAYED)
                    holder.btnItemOrder.setText("Ενημέρωση καθυστέρησης");
                else if (order.getOrderstatus() == OrderStatusType.SHIPPED)
                    holder.btnItemOrder.setText("Ενημέρωση ετοιμότητας");
                break;
            case ASSIGNED_ORDERS_ADAPTER:
                holder.btnItemOrder.setText("Ετοιμασία παραγγελίας");
                break;
        }

        // Set the click listener for the action button
        holder.btnItemOrder.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAssignClick(order);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The size of the orders list.
     */
    @Override
    public int getItemCount() {
        return orders.size();
    }

    /**
     * Removes a specific order from the list and notifies the adapter to refresh the UI.
     * @param order The {@link Order} object to be removed.
     */
    public void removeOrder(Order order) {
        int position = orders.indexOf(order);
        if (position != -1) {
            orders.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * ViewHolder class that provides a reference to the views for each order item.
     */
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderIdValue, txtCustomerNameValue, txtDateValue, txtPriceValue, txtStatus;
        Button btnItemOrder;

        /**
         * Constructor for the ViewHolder, initializing UI components from the inflated layout.
         * @param itemView The inflated view for a single list item.
         */
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderIdValue = itemView.findViewById(R.id.txtItemOrderIdValue);
            txtCustomerNameValue = itemView.findViewById(R.id.txtItemOrderCustomerNameValue);
            txtDateValue = itemView.findViewById(R.id.txtItemOrderSubmissionDateValue);
            txtPriceValue = itemView.findViewById(R.id.txtItemOrderPriceValue);
            txtStatus = itemView.findViewById(R.id.txtItemOrderStatus);
            btnItemOrder = itemView.findViewById(R.id.btnItemOrder);
        }
    }
}