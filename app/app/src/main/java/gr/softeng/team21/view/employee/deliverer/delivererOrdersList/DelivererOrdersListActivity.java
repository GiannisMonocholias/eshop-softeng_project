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

public class DelivererOrdersListActivity extends AppCompatActivity implements DelivererOrdersListView {

    private RecyclerView recyclerView;
    private DelivererOrderAdapter adapter;
    private DelivererOrdersListPresenter presenter;

    private static final String EMP_ID_EXTRA = "DELIVERER_ID";

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



    @Override
    public void removeOrderFromList(Order order) {
        if (adapter != null) {
            adapter.removeOrder(order);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Σφάλμα")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}