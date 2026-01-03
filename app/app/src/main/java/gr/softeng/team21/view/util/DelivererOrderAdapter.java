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

public class DelivererOrderAdapter extends RecyclerView.Adapter<DelivererOrderAdapter.DelivererViewHolder> {

    private List<Order> orderList;
    private OnOrderCompleteListener listener;

    // Interface για να επικοινωνούμε με το Activity όταν πατηθεί το "ΟΛΟΚΛΗΡΩΣΗ"
    public interface OnOrderCompleteListener {
        void onOrderCompleted(Order order, int position);
    }

    public DelivererOrderAdapter(List<Order> orderList, OnOrderCompleteListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DelivererViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Εδώ φορτώνουμε το XML που έστειλες
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deliverer_order, parent, false);
        return new DelivererViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DelivererViewHolder holder, int position) {
        Order order = orderList.get(position);

        // --- 1. ΑΝΤΙΣΤΟΙΧΙΣΗ ΔΕΔΟΜΕΝΩΝ ---
        holder.txtId.setText("Order #" + order.getOrdercode());
        holder.txtDate.setText(order.getSubmissiondate().toString());

        // Έλεγχος για null τιμές στο ποσό
        if (order.getTotal_amount() != null) {
            holder.txtAmount.setText(order.getTotal_amount().toString() + " €");
        } else {
            holder.txtAmount.setText("0.0 €");
        }

        // Στοιχεία Πελάτη
        if (order.getShoppingCart().getCustomer() != null) {
            holder.txtName.setText(order.getShoppingCart().getCustomer().getFirstname() + " " +
                    order.getShoppingCart().getCustomer().getLastname());

            // Προσοχή: Βεβαιώσου ότι υπάρχει getAddress() στον Customer ή πάρε το από αλλού
            holder.txtAddress.setText(order.getShoppingCart().getCustomer().getAddress().toString());
        } else {
            holder.txtName.setText("Άγνωστος");
            holder.txtAddress.setText("-");
        }


        // --- 2. LOGIC ΓΙΑ CHECKBOXES & BUTTON ---

        // Αφαιρούμε προσωρινά τους listeners για να μην έχουμε conflicts κατά το scroll
        holder.chkPaid.setOnCheckedChangeListener(null);
        holder.chkDelivered.setOnCheckedChangeListener(null);

        // Reset στα widgets (σημαντικό για το Recycling του RecyclerView)
        holder.chkPaid.setChecked(false);
        holder.chkDelivered.setChecked(false);
        holder.btnConfirm.setEnabled(false);
        holder.btnConfirm.setAlpha(0.5f); // Γίνεται μισο-διάφανο

        // Ο listener που ελέγχει αν και τα δύο είναι τικαρισμένα
        CompoundButton.OnCheckedChangeListener checkListener = (buttonView, isChecked) -> {
            boolean isPaid = holder.chkPaid.isChecked();
            boolean isDelivered = holder.chkDelivered.isChecked();

            if (isPaid && isDelivered) {
                holder.btnConfirm.setEnabled(true);
                holder.btnConfirm.setAlpha(1.0f); // Πλήρως ορατό
            } else {
                holder.btnConfirm.setEnabled(false);
                holder.btnConfirm.setAlpha(0.5f); // Μισο-διάφανο
            }
        };

        // Συνδέουμε τον listener
        holder.chkPaid.setOnCheckedChangeListener(checkListener);
        holder.chkDelivered.setOnCheckedChangeListener(checkListener);

        // Κλικ στο κουμπί Ολοκλήρωσης
        holder.btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderCompleted(order, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // --- VIEWHOLDER ---
    public static class DelivererViewHolder extends RecyclerView.ViewHolder {
        // Δηλώνουμε τα widgets με βάση τα IDs του XML σου
        TextView txtId, txtDate, txtName, txtAddress, txtAmount;
        CheckBox chkPaid, chkDelivered;
        Button btnConfirm;

        public DelivererViewHolder(@NonNull View itemView) {
            super(itemView);

            // Σύνδεση με τα IDs του XML που έστειλες
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