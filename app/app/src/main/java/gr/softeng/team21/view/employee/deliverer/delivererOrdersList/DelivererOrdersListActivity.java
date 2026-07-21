package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

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
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.view.util.DelivererOrderAdapter;

/**
 * Activity that displays a list of orders assigned to a Deliverer.
 * Uses a {@link RecyclerView} with a {@link DelivererOrderAdapter} to handle
 * user interaction for order's delivery confirmation.
 * Implements the {@link DelivererOrdersListView} interface and safely updates the UI
 * via runOnUiThread.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrdersListActivity extends AppCompatActivity implements DelivererOrdersListView {

    private RecyclerView recyclerView;
    private DelivererOrderAdapter adapter;
    private DelivererOrdersListPresenter presenter;

    private static final String EMP_ID_EXTRA = "DELIVERER_ID";

    /**
     * Sets up the UI, injects the required DAOs into the presenter, and initiates
     * the asynchronous loading of orders assigned to the current Deliverer.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deliverer_orders_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.delivererOrdersList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rvDelivererOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // DEPENDENCY INJECTION: EmployeeDAO via Firebase, OrderDAO via Firebase
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        OrderDAO orderDAO = OrderDAOMemory.getInstance();

        presenter = new DelivererOrdersListPresenter(this, orderDAO, employeeDAO);

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);

        // Trigger asynchronous load
        presenter.loadShippedOrders(employeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrdersList(ArrayList<Order> orders) {
        runOnUiThread(() -> {
            if (orders != null) {
                adapter = new DelivererOrderAdapter(orders, order ->
                        presenter.onOrderConfirmed(order)
                );
                recyclerView.setAdapter(adapter);
            }
        });
    }

    /**
     * {@inheritDoc}
     * Instructs the adapter to remove the specific order item from the display.
     */
    @Override
    public void removeOrderFromList(Order order) {
        runOnUiThread(() -> {
            if (adapter != null) {
                adapter.removeOrder(order);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * {@inheritDoc}
     * Displays a modern {@link MaterialAlertDialogBuilder} for error communication.
     */
    @Override
    public void showError(String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Σφάλμα")
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}