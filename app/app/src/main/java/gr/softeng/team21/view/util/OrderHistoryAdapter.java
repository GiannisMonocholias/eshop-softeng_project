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
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.util.Money;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private final OnOrderClickListener listener;


    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderHistoryAdapter(List<Order> orderList, OnOrderClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderCode.setText(order.getOrderCode() != null ? order.getOrderCode() : "-");

        if (order.getSubmissionDate() != null) {
            holder.txtOrderDate.setText("Ημερομηνία: " + order.getSubmissionDate().toString());
        } else {
            holder.txtOrderDate.setText("Ημερομηνία: -");
        }

        OrderStatusType status = order.getOrderStatus();
        if (status != null) {
            holder.txtOrderStatus.setText(status.name());
            if (status == OrderStatusType.SHIPPED) {
                holder.txtOrderStatus.setBackgroundColor(Color.parseColor("#E0F2F1"));
                holder.txtOrderStatus.setTextColor(Color.parseColor("#00796B"));
            } else if (status == OrderStatusType.DELAYED) {
                holder.txtOrderStatus.setBackgroundColor(Color.parseColor("#FFEBEE"));
                holder.txtOrderStatus.setTextColor(Color.parseColor("#C62828"));
            } else {
                holder.txtOrderStatus.setBackgroundColor(Color.parseColor("#FFF8E1"));
                holder.txtOrderStatus.setTextColor(Color.parseColor("#F57F17"));
            }
        }


        PaymentType payment = order.getPaymentMethod();
        String paymentText = (payment == PaymentType.CARD) ? "Κάρτα" : "Μετρητά";
        String paidStatus = order.isPaid() ? " (Εξοφλημένη)" : " (Εκκρεμεί πληρωμή)";
        holder.txtOrderPayment.setText("Πληρωμή: " + paymentText + paidStatus);

        Money total = order.getTotal_amount();
        if (total == null && order.getShoppingCart() != null) {
            total = order.getShoppingCart().getTotalCost();
        }
        if (total != null) {
            holder.txtOrderTotal.setText(total.toString());
        } else {
            holder.txtOrderTotal.setText("0.00 €");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderCode, txtOrderStatus, txtOrderDate, txtOrderPayment, txtOrderTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderCode = itemView.findViewById(R.id.txtOrderCodeItem);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatusItem);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDateItem);
            txtOrderPayment = itemView.findViewById(R.id.txtOrderPaymentMethod);
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotalItem);
        }
    }
}