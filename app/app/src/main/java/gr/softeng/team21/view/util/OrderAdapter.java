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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OnOrderClickListener listener;
    private OrderAdapterType type;

    public interface OnOrderClickListener {
        void onAssignClick(Order order);
    }

    public OrderAdapter(List<Order> orders, OrderAdapterType type, OnOrderClickListener listener) {
        this.orders = (orders != null) ? orders : new java.util.ArrayList<>();
        this.listener = listener;
        this.type = type;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.txtOrderIdValue.setText("#" + order.getOrdercode());
        holder.txtCustomerNameValue.setText(order.getShoppingCart().getCustomer().getLastname() + " " +
                order.getShoppingCart().getCustomer().getFirstname());
        holder.txtPriceValue.setText(order.getTotal_amount().toString());
        holder.txtDateValue.setText(order.getSubmissiondate().toString());
        holder.txtStatus.setText(order.getOrderstatus().toString());

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

        holder.btnItemOrder.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAssignClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void removeOrder(Order order) {
        int position = orders.indexOf(order);
        if (position != -1) {
            orders.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderIdValue, txtCustomerNameValue, txtDateValue, txtPriceValue, txtStatus;
        Button btnItemOrder;

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