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

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails.OrderPreparationDetailsActivity;
import gr.softeng.team21.view.util.OrderAdapter;
import gr.softeng.team21.view.util.OrderAdapterType;

/**
 * Activity that displays a list of orders currently assigned to an Order Preparation Employee.
 * It uses a {@link RecyclerView} to display the assigned orders of the employee and refreshes the data
 * during the onResume lifecycle event.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedOrdersToPrepareActivity extends AppCompatActivity implements AssignedOrdersToPrepareView {

    private AssignedOrdersToPreparePresenter presenter;
    private static final String EMP_ID_EXTRA = "ORDER_PREPARATION_EMPLOYEE_ID";
    private static final String ORD_CODE_EXTRA = "ORDER_CODE";
    private RecyclerView recyclerView;


    /**
     * Sets up the UI layout and initializes the presenter.
     * Configuration of the RecyclerView's LayoutManager is performed here.
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

        presenter = new AssignedOrdersToPreparePresenter(this, EmployeeDAOMemory.getInstance());

        recyclerView = findViewById(R.id.OrdPrepEmprecyclerViewAssignedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * Refreshes the assigned orders list every time the Activity becomes visible.
     * This ensures that any processed or newly assigned orders are updated in the UI.
     */
    @Override
    public void onResume() {
        super.onResume();


        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);

        ArrayList<Order> assignedOrders = presenter.loadAssignedOrders(employeeId);

        OrderAdapter adapter = new OrderAdapter(assignedOrders, OrderAdapterType.ASSIGNED_ORDERS_ADAPTER, order -> {
            presenter.onClickOrder(order);
        });

        recyclerView.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     * Starts the {@link OrderPreparationDetailsActivity} using Intent extras
     * for employee and order identification.
     *
     */
    @Override
    public void navigateToOrderPreparationDetails(String employeeId, String orderId) {
        Intent intent = new Intent(AssignedOrdersToPrepareActivity.this, OrderPreparationDetailsActivity.class);
        intent.putExtra(EMP_ID_EXTRA, employeeId);
        intent.putExtra(ORD_CODE_EXTRA, orderId);
        startActivity(intent);
    }
}