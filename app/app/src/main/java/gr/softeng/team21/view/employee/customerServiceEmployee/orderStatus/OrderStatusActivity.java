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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.view.util.OrderAdapter;
import gr.softeng.team21.view.util.OrderAdapterType;

/**
 * Android Activity for notifying customers about their orders status.
 * Uses a RecyclerView to display orders assigned to the employee and provides
 * interaction via dialogs for sending automated updates.
 * Implements {@link OrderStatusView} and uses runOnUiThread to safely update the UI
 * based on asynchronous Presenter logic. Incorporates Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusActivity extends AppCompatActivity implements OrderStatusView {

    private OrderStatusPresenter presenter;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;

    /**
     * Configures the layout, injects the Firebase DAO into the presenter,
     * and initiates the asynchronous loading of the order list.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
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

        // DEPENDENCY INJECTION: Use Firebase DAO for production
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        presenter = new OrderStatusPresenter(this, employeeDAO);

        String employeeId = getIntent().getStringExtra("CUSTOMER_SERVICE_EMPLOYEE_ID");

        // Trigger the asynchronous order loading
        presenter.loadOrders(employeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrders(ArrayList<Order> orders) {
        runOnUiThread(() -> {
            if (orders != null) {
                adapter = new OrderAdapter(orders, OrderAdapterType.NOTIFY_ORDER_ADAPTER, order -> {
                    presenter.onOrderClicked(order);
                });
                recyclerView.setAdapter(adapter);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOrderSelected(Order order) {
        runOnUiThread(() -> {
            Toast.makeText(this, "Επιλέχθηκε η παραγγελία: " + order.getOrdercode(), Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }

    /**
     * {@inheritDoc}
     * Refreshes the RecyclerView data set to reflect the removal of processed orders.
     */
    @Override
    public void updateList() {
        runOnUiThread(() -> {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * {@inheritDoc}
     * Displays an Android AlertDialog to confirm the intent to send
     * a customer notification.
     */
    @Override
    public void showConfirmationDialog(Order order, String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Επιβεβαίωση Ενημέρωσης")
                    .setMessage(message)
                    .setPositiveButton("ΝΑΙ", (dialog, which) -> presenter.onOrderConfirmed(order))
                    .setNegativeButton("ΟΧΙ", null)
                    .show();
        });
    }
}