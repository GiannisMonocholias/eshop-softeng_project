package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.firebasedao.OrderDAOFirebase;
import gr.softeng.team21.view.util.StockProductAdapter;

/**
 * Android Activity providing the UI for reviewing order contents and
 * executing stock check.
 * Implements {@link OrderPreparationDetailsView} and securely updates the interface
 * using runOnUiThread. Integrates Material Components and Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationDetailsActivity extends AppCompatActivity implements OrderPreparationDetailsView {

    private OrderPreparationDetailsPresenter presenter;
    private RecyclerView recyclerView;
    private static final String EMP_ID_EXTRA = "ORDER_PREPARATION_EMPLOYEE_ID";
    private static final String ORD_CODE_EXTRA = "ORDER_CODE";

    /**
     * Configures the layout, binds UI components, injects DAOs, and triggers
     * the asynchronous loading of the order data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_preparation_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.order_preparation_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rvOrderDetailsProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // DEPENDENCY INJECTION: Connect Presenter to Firebase DAOs
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        OrderDAO orderDAO = new OrderDAOFirebase();

        presenter = new OrderPreparationDetailsPresenter(this, employeeDAO, orderDAO);

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        String ordercode = getIntent().getStringExtra(ORD_CODE_EXTRA);

        // Fetch data asynchronously
        presenter.loadOrder(employeeId, ordercode);

        Button btnStockCheck = findViewById(R.id.btnOrderDetailsStockCheck);
        btnStockCheck.setOnClickListener(v -> presenter.checkStockOrder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderDetails(String ordercode, String customerName, String submissionDate, String price, OrderStatusType status) {
        runOnUiThread(() -> {
            ((TextView) findViewById(R.id.txtOrderDetailsOrderIdValue)).setText(ordercode);
            ((TextView) findViewById(R.id.txtOrderDetailsOrderCustomerNameValue)).setText(customerName);
            ((TextView) findViewById(R.id.txtOrderDetailsOrderSubmissionDateValue)).setText(submissionDate);
            ((TextView) findViewById(R.id.txtOrderDetailsOrderPriceValue)).setText(price);
            ((TextView) findViewById(R.id.txtOrderDetailsOrderStatus)).setText(status.toString());
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCartItems(ArrayList<CartItem> items) {
        runOnUiThread(() -> {
            if (items != null) {
                StockProductAdapter adapter = new StockProductAdapter(items);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    /**
     * {@inheritDoc}
     * Shows a modern {@link MaterialAlertDialogBuilder} for errors.
     */
    @Override
    public void showErrorMessage(String message) {
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
     * Shows a success {@link MaterialAlertDialogBuilder}. Upon confirmation, the activity finishes.
     */
    @Override
    public void showSuccessMessage(String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Επιτυχία")
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("OK", (dialog, which) -> finishActivity())
                    .setCancelable(false)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finishActivity() {
        runOnUiThread(this::finish);
    }
}