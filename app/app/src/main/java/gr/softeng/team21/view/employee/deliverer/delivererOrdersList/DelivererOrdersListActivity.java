package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.view.util.DelivererOrderAdapter;

/**
 * Activity that displays a list of orders assigned to a Deliverer.
 * Uses a {@link RecyclerView} with a {@link DelivererOrderAdapter} to handle
 * user interaction for order's delivery confirmation.
 * implements the {@link DelivererOrdersListView} inteface
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrdersListActivity extends AppCompatActivity implements DelivererOrdersListView {

    private RecyclerView recyclerView;
    private DelivererOrderAdapter adapter;
    private DelivererOrdersListPresenter presenter;

    private static final String EMP_ID_EXTRA = "DELIVERER_ID";

    /**
     * Sets up the UI, initializes the presenter, and populates the
     * RecyclerView with orders assigned to the current Deliverer.
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

        presenter = new DelivererOrdersListPresenter(this, OrderDAOMemory.getInstance(), EmployeeDAOMemory.getInstance());

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        ArrayList<Order> assignedOrders = presenter.loadShippedOrders(employeeId);

        adapter = new DelivererOrderAdapter(assignedOrders, order ->
                presenter.onOrderConfirmed(order)
        );

        recyclerView.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     * Instructs the adapter to remove the specific order item from the display.
     */
    @Override
    public void removeOrderFromList(Order order) {
        if (adapter != null) {
            adapter.removeOrder(order);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * {@inheritDoc}
     * Displays a native Android {@link AlertDialog} for error communication.
     */
    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Σφάλμα")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}