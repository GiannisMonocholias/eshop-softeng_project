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
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.StatusType;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OrderActionListener listener;
    private OrderAdapterTypes adapterType;

    // 1. Interface: Αυτό θα υλοποιήσουμε στο Activity
    public interface OrderActionListener {
        void onActionClick(Order order);
    }

    // Constructor
    public OrderAdapter(List<Order> orderList, OrderAdapterTypes adapterType, OrderActionListener listener) {
        this.orderList = orderList;
        this.adapterType = adapterType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        Customer customer = order.getShoppingCart().getCustomer();

        // Ενημέρωση των πεδίων με τα νέα IDs
        holder.txtOrderIdValue.setText("Order #" + order.getOrdercode());
        holder.txtDateValue.setText(order.getSubmissiondate().toString());
        if(order.getTotal_amount()!=null)
            holder.txtPriceValue.setText(order.getTotal_amount().toString() + " €");
        else
            holder.txtPriceValue.setText("0 €");

        holder.txtStatus.setText(order.getOrderstatus().toString());

        if (customer != null) {
            holder.txtCustomerNameValue.setText(customer.getFirstname() + " " + customer.getLastname());
        } else {
            holder.txtCustomerNameValue.setText("Άγνωστος Πελάτης");
        }

        // 1. Αρχικά το κρύβουμε και τα δύο κουμπιά
        holder.btnEmailNotification.setVisibility(View.GONE);
        holder.btnAssignOrder.setVisibility(View.GONE);
        if(adapterType == OrderAdapterTypes.NOTIFY_ORDER_ADAPTER) {
            holder.btnEmailNotification.setVisibility(View.VISIBLE);
        }
        else if(adapterType == OrderAdapterTypes.ASSIGN_ORDER_ADAPTER
        && order.getOrderstatus() == StatusType.NEW){
            holder.btnAssignOrder.setVisibility(View.VISIBLE);
        }



        // 2. Εμφάνιση ανάλογα με το status
        if (adapterType == OrderAdapterTypes.NOTIFY_ORDER_ADAPTER) {
            if (order.getOrderstatus() == StatusType.DELAYED) {
                holder.btnEmailNotification.setText("Ενημέρωση Καθυστέρησης");
            }
            else if (order.getOrderstatus() == StatusType.SHIPPED){
                holder.btnEmailNotification.setText("Ενημέρωση Ετοιμότητας");
            }
            else{
                holder.btnEmailNotification.setVisibility(View.GONE);
            }
        }


        if (adapterType == OrderAdapterTypes.NOTIFY_ORDER_ADAPTER) {
            // Εμφάνιση μόνο του Email Button
            holder.btnEmailNotification.setVisibility(View.VISIBLE);
            holder.btnAssignOrder.setVisibility(View.GONE);

            holder.btnEmailNotification.setOnClickListener(v -> {
                if (listener != null) listener.onActionClick(order);
            });

        } else if (adapterType == OrderAdapterTypes.ASSIGN_ORDER_ADAPTER) {
            // Εμφάνιση μόνο του Assign Button
            holder.btnAssignOrder.setVisibility(View.VISIBLE);
            holder.btnEmailNotification.setVisibility(View.GONE);

            holder.btnAssignOrder.setOnClickListener(v -> {
                // Εδώ υποθέτω ότι περνάς το order για να ξέρεις ποια να αναθέσεις
                if (listener != null) listener.onActionClick(order);
            });

        } else if (adapterType == OrderAdapterTypes.ASSIGNED_ORDERS_ADAPTER) {
            // --- Η ΠΕΡΙΠΤΩΣΗ ΠΟΥ ΜΑΣ ΕΝΔΙΑΦΕΡΕΙ ΤΩΡΑ ---

            // Απόκρυψη όλων των κουμπιών (θέλουμε κλικ σε όλο το πλαίσιο)
            holder.btnAssignOrder.setVisibility(View.GONE);
            holder.btnEmailNotification.setVisibility(View.GONE);

            // Κλικ σε ολόκληρη τη γραμμή (itemView) για πλοήγηση στις λεπτομέρειες
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onActionClick(order);
            });
        }


        // --- BONUS: Χρώμα Status ---
        if(order.getOrderstatus() == StatusType.DELAYED) {
            holder.txtStatus.setTextColor(android.graphics.Color.parseColor("#FF9800")); // Πορτοκαλί
        } else if (order.getOrderstatus() == StatusType.SHIPPED) {
            holder.txtStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50")); // Πράσινο
        } else {
            holder.txtStatus.setTextColor(android.graphics.Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderIdValue, txtDateValue, txtCustomerNameValue, txtStatus, txtPriceValue;
        Button btnEmailNotification,btnAssignOrder; // Το κουμπί σου

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Προσοχή: Εδώ συνδέουμε τα IDs που έβαλες στο XML σου
            txtOrderIdValue = itemView.findViewById(R.id.txtItemOrderIdValue);
            txtDateValue = itemView.findViewById(R.id.txtItemOrderSubmissionDateValue);
            txtCustomerNameValue = itemView.findViewById(R.id.txtItemOrderCustomerNameValue);
            txtStatus = itemView.findViewById(R.id.txtItemOrderStatus);
            txtPriceValue = itemView.findViewById(R.id.txtItemOrderPriceValue);

            // ΤΟ ΝΕΟ ID ΤΟΥ ΚΟΥΜΠΙΟΥ ΣΟΥ:
            btnEmailNotification = itemView.findViewById(R.id.btnItemOrderNotifyCustomer);
            btnAssignOrder = itemView.findViewById(R.id.btnItemOrderAssignOrder);
        }
    }
}