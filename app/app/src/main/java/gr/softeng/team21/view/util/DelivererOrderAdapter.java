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

public class DelivererOrderAdapter extends RecyclerView.Adapter<DelivererOrderAdapter.DelivererViewHolder> {

    private List<Order> orderList;
    private OnOrderCompleteListener listener;

    public interface OnOrderCompleteListener {
        void onOrderCompleted(Order order);
    }

    public DelivererOrderAdapter(List<Order> orderList, OnOrderCompleteListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DelivererViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deliverer_order, parent, false);
        return new DelivererViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DelivererViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtId.setText("Order #" + order.getOrdercode());
        if(order.getSubmissiondate() != null)
            holder.txtDate.setText(order.getSubmissiondate().toString());

        if (order.getTotal_amount() != null) {
            holder.txtAmount.setText(order.getTotal_amount().toString() + " €");
        } else {
            holder.txtAmount.setText("0.0 €");
        }

        if (order.getShoppingCart().getCustomer() != null) {
            holder.txtName.setText(order.getShoppingCart().getCustomer().getFirstname() + " " +
                    order.getShoppingCart().getCustomer().getLastname());

            if (order.getShoppingCart().getCustomer().getAddress() != null) {
                holder.txtAddress.setText(order.getShoppingCart().getCustomer().getAddress().toString());
            } else {
                holder.txtAddress.setText("-");
            }
        } else {
            holder.txtName.setText("Άγνωστος");
            holder.txtAddress.setText("-");
        }


        holder.chkPaid.setOnCheckedChangeListener(null);
        holder.chkDelivered.setOnCheckedChangeListener(null);

        holder.chkPaid.setChecked(order.getPaid());
        holder.chkDelivered.setChecked(order.getOrderstatus() == OrderStatusType.DELIVERED);

        updateButtonState(holder);

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

            updateButtonState(holder);
        };

        holder.chkPaid.setOnCheckedChangeListener(checkListener);
        holder.chkDelivered.setOnCheckedChangeListener(checkListener);

        holder.btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderCompleted(order);
            }
        });
    }

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

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void removeOrder(Order order) {
        int position = orderList.indexOf(order); // Βρίσκουμε το index του αντικειμένου
        if (position != -1) {
            orderList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, orderList.size());
        }
    }

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