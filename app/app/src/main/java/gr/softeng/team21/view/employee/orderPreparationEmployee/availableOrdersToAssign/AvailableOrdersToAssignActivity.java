package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

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
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.firebasedao.OrderDAOFirebase;
import gr.softeng.team21.view.util.OrderAdapter;
import gr.softeng.team21.view.util.OrderAdapterType;

/**
 * Activity that displays a list of unassigned orders to Order Preparation Employees.
 * Provides the interface for employees to manually pick up orders from the list of newly submitted orders.
 * Implements the {@link AvailableOrdersToAssignView} interface and securely updates the UI
 * via runOnUiThread. Integrates Dependency Injection and Material Components.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableOrdersToAssignActivity extends AppCompatActivity implements AvailableOrdersToAssignView {

    private AvailableOrdersToAssignPresenter presenter;
    private OrderAdapter adapter;
    private RecyclerView recyclerView;

    private static final String EMP_ID_EXTRA = "ORDER_PREPARATION_EMPLOYEE_ID";

    /**
     * Configures the layout, injects DAOs into the Presenter, and initiates
     * the asynchronous loading of available orders.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_available_orders_to_assign);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.OrdPrepEmpAvailableOrdersToAssign), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.OrdPrepEmprecyclerViewNonAssignedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // DEPENDENCY INJECTION: Connect Presenter to Firebase DAOs
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        OrderDAO orderDAO = new OrderDAOFirebase();

        presenter = new AvailableOrdersToAssignPresenter(this, employeeDAO, orderDAO);

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);

        // Trigger asynchronous load
        presenter.loadAvailableOrders(employeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAvailableOrdersList(ArrayList<Order> orders) {
        runOnUiThread(() -> {
            if (orders != null) {
                adapter = new OrderAdapter(orders, OrderAdapterType.ASSIGN_ORDER_ADAPTER, order -> {
                    presenter.onOrderClicked(order);
                });
                recyclerView.setAdapter(adapter);
            }
        });
    }

    /**
     * {@inheritDoc}
     * Shows an informative {@link MaterialAlertDialogBuilder}.
     */
    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Ενημέρωση")
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
     * Shows an error {@link MaterialAlertDialogBuilder} with an alert icon.
     */
    @Override
    public void showError(String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Σφάλμα")
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
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
     * Removes the order from the current adapter to reflect its change in status.
     */
    @Override
    public void onOrderAssignedSuccess(Order order) {
        runOnUiThread(() -> {
            if (adapter != null) {
                adapter.removeOrder(order);
            }
        });
    }

    /**
     * {@inheritDoc}
     * Builds and displays a {@link MaterialAlertDialogBuilder} confirmation dialog
     * with "ΝΑΙ" and "ΟΧΙ" options about assigning the selected order.
     */
    @Override
    public void showConfirmationDialog(Order order, String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Επιβεβαίωση Ανάληψης παραγγελίας")
                    .setMessage(message)
                    .setPositiveButton("ΝΑΙ", (dialog, which) -> presenter.onOrderConfirmed(order))
                    .setNegativeButton("ΟΧΙ", null)
                    .show();
        });
    }
}