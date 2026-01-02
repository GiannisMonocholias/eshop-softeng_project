package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.view.util.OrderAdapter;
import gr.softeng.team21.view.util.OrderAdapterType;

public class AvailableOrdersToAssignActivity extends AppCompatActivity implements AvailableOrdersToAssignView {

    AvailableOrdersToAssignPresenter presenter;
    OrderAdapter adapter;

    private static final String EMP_ID_EXTRA = "ORDER_PREPARATION_EMPLOYEE_ID";

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

        presenter = new AvailableOrdersToAssignPresenter(this, EmployeeDAOMemory.getInstance(), OrderDAOMemory.getInstance());


        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        ArrayList<Order> availableOrders = presenter.loadAvailableOrders(employeeId);


        RecyclerView recyclerView = findViewById(R.id.OrdPrepEmprecyclerViewNonAssignedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        adapter = new OrderAdapter(availableOrders, OrderAdapterType.ASSIGN_ORDER_ADAPTER, order -> {
            presenter.onOrderClicked(order);
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Ενημέρωση")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Σφάλμα")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", null)
                .show();
    }
    @Override
    public void updateList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onOrderAssignedSuccess(Order order) {
        if (adapter != null) {
            adapter.removeOrder(order);
        }
    }

    @Override
    public void showConfirmationDialog(Order order, String message) {
        new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση Ανάληψης παραγγελίας")
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