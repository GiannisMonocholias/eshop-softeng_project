package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

import android.content.Intent;
import android.os.Bundle;

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
import gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails.OrderPreparationDetailsActivity;
import gr.softeng.team21.view.util.OrderAdapter;
import gr.softeng.team21.view.util.OrderAdapterType;

/**
 * Activity that displays a list of orders currently assigned to an Order Preparation Employee.
 * It uses a {@link RecyclerView} to display the assigned orders of the employee and fetches data
 * asynchronously during the onResume lifecycle event.
 * Implements Dependency Injection and uses runOnUiThread for safe UI rendering.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedOrdersToPrepareActivity extends AppCompatActivity implements AssignedOrdersToPrepareView {

    private AssignedOrdersToPreparePresenter presenter;
    private static final String EMP_ID_EXTRA = "ORDER_PREPARATION_EMPLOYEE_ID";
    private static final String ORD_CODE_EXTRA = "ORDER_CODE";
    private RecyclerView recyclerView;

    /**
     * Sets up the UI layout, initializes the presenter with Firebase DAO injection,
     * and configures the RecyclerView's LayoutManager.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assigned_orders_to_prepare);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.OrdPrepEmpAssignedOrders), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // DEPENDENCY INJECTION: Connect Presenter to Firebase
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        presenter = new AssignedOrdersToPreparePresenter(this, employeeDAO);

        recyclerView = findViewById(R.id.OrdPrepEmprecyclerViewAssignedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Triggers an asynchronous refresh of the assigned orders list every time the Activity
     * becomes visible, ensuring processed or newly assigned orders are updated.
     */
    @Override
    public void onResume() {
        super.onResume();
        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        presenter.loadAssignedOrders(employeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAssignedOrdersList(ArrayList<Order> orders) {
        runOnUiThread(() -> {
            if (orders != null) {
                OrderAdapter adapter = new OrderAdapter(orders, OrderAdapterType.ASSIGNED_ORDERS_ADAPTER, order -> {
                    presenter.onClickOrder(order);
                });
                recyclerView.setAdapter(adapter);
            }
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
                    .setPositiveButton("ΟΚ", null)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
     * Starts the {@link OrderPreparationDetailsActivity} using Intent extras
     * for employee and order identification.
     */
    @Override
    public void navigateToOrderPreparationDetails(String employeeId, String orderId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(AssignedOrdersToPrepareActivity.this, OrderPreparationDetailsActivity.class);
            intent.putExtra(EMP_ID_EXTRA, employeeId);
            intent.putExtra(ORD_CODE_EXTRA, orderId);
            startActivity(intent);
        });
    }
}