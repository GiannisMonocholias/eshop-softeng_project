package gr.softeng.team21.view.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Specialized RecyclerView Adapter for the Deliverer's order list.
 * This adapter handles the complex logic of a delivery checklist, enabling the
 * "Confirm" button only when the order is marked as both paid and delivered.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrderAdapter extends RecyclerView.Adapter<DelivererOrderAdapter.DelivererViewHolder> {

    private List<Order> orderList;
    private OnOrderCompleteListener listener;

    /**
     * Interface definition for a callback to be invoked when an order
     * is successfully processed and confirmed by the deliverer.
     */
    public interface OnOrderCompleteListener {
        /**
         * Triggered when the deliverer clicks the confirm button after
         * completing the checklist.
         * @param order The completed order.
         */
        void onOrderCompleted(Order order);
    }

    /**
     * Initializes the adapter with the list of assigned orders and a completion listener.
     * @param orderList List of orders currently assigned to the deliverer.
     * @param listener The listener that handles the final confirmation logic.
     */
    public DelivererOrderAdapter(List<Order> orderList, OnOrderCompleteListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    /**
     * Creates and inflates the ViewHolder for individual order items.
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new DelivererViewHolder.
     */
    @NonNull
    @Override
    public DelivererViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deliverer_order, parent, false);
        return new DelivererViewHolder(view);
    }

    /**
     * Binds order data to the UI components and manages the state of the delivery checklist.
     * Handles dynamic logic for enabling/disabling the confirmation button based on CheckBox states.
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull DelivererViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Populate basic order info
        holder.txtId.setText("Order #" + order.getOrdercode());
        if(order.getSubmissiondate() != null)
            holder.txtDate.setText(order.getSubmissiondate().toString());

        // Format and display total amount
        if (order.getTotal_amount() != null) {
            holder.txtAmount.setText(order.getTotal_amount().toString() + " €");
        } else {
            holder.txtAmount.setText("0.0 €");
        }

        // Populate customer and address info
        if (order.getShoppingCart().getCustomer() != null) {
            holder.txtName.setText(order.getShoppingCart().getCustomer().getFirstname() + " " +
                    order.getShoppingCart().getCustomer().getLastname());

            if (order.getShoppingCart().getCustomer().getAddress() != null) {
                holder.txtAddress.setText(order.getShoppingCart().getCustomer().getAddress().toString());
            } else {
                holder.txtAddress.setText("-");
            }
        } else {
            holder.txtName.setText("Unknown");
            holder.txtAddress.setText("-");
        }

        // Reset listeners to prevent logic triggers during binding of recycled views
        holder.chkPaid.setOnCheckedChangeListener(null);
        holder.chkDelivered.setOnCheckedChangeListener(null);

        // Sync CheckBox states with the current domain model data
        holder.chkPaid.setChecked(order.getPaid());
        holder.chkDelivered.setChecked(order.getOrderstatus() == OrderStatusType.DELIVERED);

        // Initial update of the confirm button state
        updateButtonState(holder);

        // Shared listener for the checklist logic to update the domain model dynamically
        CompoundButton.OnCheckedChangeListener checkListener = (buttonView, isChecked) -> {
            int id = buttonView.getId();

            if (id == R.id.chkboxItem_deliverer_order_Paid) {
                order.setPaid(isChecked);
            } else if (id == R.id.chkboxItem_deliverer_order_Delivered) {
                if (isChecked) {
                    order.setOrderstatus(OrderStatusType.DELIVERED);
                } else {
                    order.setOrderstatus(OrderStatusType.SHIPPED);
                }
            }

            // Re-evaluate button state every time a checkbox is clicked
            updateButtonState(holder);
        };

        holder.chkPaid.setOnCheckedChangeListener(checkListener);
        holder.chkDelivered.setOnCheckedChangeListener(checkListener);

        // Notify the listener when the deliverer finalizes the delivery
        holder.btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderCompleted(order);
            }
        });
    }

    /**
     * Updates the visual and functional state of the "Confirm" button.
     * The button is enabled only when both 'Paid' and 'Delivered' are checked.
     * @param holder The ViewHolder containing the UI elements.
     */
    private void updateButtonState(DelivererViewHolder holder) {
        boolean isPaid = holder.chkPaid.isChecked();
        boolean isDelivered = holder.chkDelivered.isChecked();

        if (isPaid && isDelivered) {
            holder.btnConfirm.setEnabled(true);
            holder.btnConfirm.setAlpha(1.0f);
        } else {
            holder.btnConfirm.setEnabled(false);
            holder.btnConfirm.setAlpha(0.5f);
        }
    }

    /**
     * @return The total number of items in the delivery queue.
     */
    @Override
    public int getItemCount() {
        return orderList.size();
    }

    /**
     * Removes an order from the list with an animation.
     * Should be called after the delivery is successfully confirmed.
     * @param order The order object to be removed.
     */
    public void removeOrder(Order order) {
        int position = orderList.indexOf(order);
        if (position != -1) {
            orderList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, orderList.size());
        }
    }

    /**
     * ViewHolder class to optimize UI performance by caching view references.
     */
    public static class DelivererViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtDate, txtName, txtAddress, txtAmount;
        CheckBox chkPaid, chkDelivered;
        Button btnConfirm;

        public DelivererViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtItem_deliverer_orderId);
            txtDate = itemView.findViewById(R.id.txtItem_deliverer_orderDeliveryDate);
            txtName = itemView.findViewById(R.id.txtItem_deliverer_orderCurtomerNameValue);
            txtAddress = itemView.findViewById(R.id.txtItem_deliverer_orderCurtomerAddressValue);
            txtAmount = itemView.findViewById(R.id.txtItem_deliverer_orderTotalAmountValue);
            chkPaid = itemView.findViewById(R.id.chkboxItem_deliverer_order_Paid);
            chkDelivered = itemView.findViewById(R.id.chkboxItem_deliverer_order_Delivered);
            btnConfirm = itemView.findViewById(R.id.btnItem_deliverer_orderConfirmDelivery);
        }
    }
}