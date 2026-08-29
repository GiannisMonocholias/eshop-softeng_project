package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

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
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.firebasedao.EmailDAOFirebase;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.firebasedao.OrderDAOFirebase;
import gr.softeng.team21.view.util.OrderAdapter;
import gr.softeng.team21.view.util.OrderAdapterType;

/**
 * Android Activity for notifying customers about their orders status.
 * Integrates Dependency Injection for Employee, Order, and Email DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusActivity extends AppCompatActivity implements OrderStatusView {
    private OrderStatusPresenter presenter;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;

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

        // DEPENDENCY INJECTION
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        EmailDAO emailDAO = new EmailDAOFirebase();
        OrderDAO orderDAO = new OrderDAOFirebase();

        presenter = new OrderStatusPresenter(this, employeeDAO, orderDAO, emailDAO);

        String employeeId = getIntent().getStringExtra("CUSTOMER_SERVICE_EMPLOYEE_ID");
        presenter.loadOrders(employeeId);
    }

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

    @Override
    public void showError(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onOrderSelected(Order order) {
        runOnUiThread(() -> Toast.makeText(this, "Επιλέχθηκε η παραγγελία: " + order.getOrdercode(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    public void updateList() {
        runOnUiThread(() -> {
            if (adapter != null) adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void showConfirmationDialog(Order order, String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this).setTitle("Επιβεβαίωση Ενημέρωσης")
                    .setMessage(message).setPositiveButton("ΝΑΙ", (dialog, which) -> presenter.onOrderConfirmed(order))
                    .setNegativeButton("ΟΧΙ", null).show();
        });
    }
}