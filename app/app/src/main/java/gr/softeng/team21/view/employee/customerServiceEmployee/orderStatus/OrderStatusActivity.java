package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.view.util.OrderAdapter;
import gr.softeng.team21.view.util.OrderAdapterType;

/**
 * Android Activity for notifying customers about their orders status.
 * Uses a RecyclerView to display orders assigned to the employee and provides
 * interaction via dialogs for sending automated updates.
 * Implements {@link OrderStatusView} to update the interface based on Presenter logic.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusActivity extends AppCompatActivity implements OrderStatusView {

    private OrderStatusPresenter presenter;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;

    /**
     * Configures the layout, initializes the presenter, and populates the
     * order list using a specialized RecyclerView adapter.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_status);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerViewOrders), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new OrderStatusPresenter(this, EmployeeDAOMemory.getInstance());

        String employeeId = getIntent().getStringExtra("CUSTOMER_SERVICE_EMPLOYEE_ID");
        ArrayList<Order> orders = presenter.loadOrders(employeeId);

        adapter = new OrderAdapter(orders, OrderAdapterType.NOTIFY_ORDER_ADAPTER , order -> {
            presenter.onOrderClicked(order);
        });

        recyclerView.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOrderSelected(Order order) {
        Toast.makeText(this, "Επιλέχθηκε η παραγγελία: " + order.getOrdercode(), Toast.LENGTH_SHORT).show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * {@inheritDoc}
     * Refreshes the RecyclerView data set to reflect the removal of processed orders.
     */
    @Override
    public void updateList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Displays an Android AlertDialog to confirm the intent to send
     * a customer notification.
     */
    @Override
    public void showConfirmationDialog(Order order, String message) {
        new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση Ενημέρωσης")
                .setMessage(message)
                .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onOrderConfirmed(order);
                    }
                })
                .setNegativeButton("ΟΧΙ", null)
                .show();
    }
}