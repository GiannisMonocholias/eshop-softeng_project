package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.OrderDAOMemory;
import gr.softeng.team21.view.util.StockProductAdapter;

/**
 * Android Activity providing the UI for reviewing order contents and
 * executing stock check.
 * Implements {@link OrderPreparationDetailsView} to receive updates from the presenter.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationDetailsActivity extends AppCompatActivity implements OrderPreparationDetailsView{
    private OrderPreparationDetailsPresenter presenter;
    private static final String EMP_ID_EXTRA = "ORDER_PREPARATION_EMPLOYEE_ID";
    private static final String ORD_CODE_EXTRA = "ORDER_CODE";


    /**
     * Configures the layout, binds UI components, and sets up the RecyclerView
     * to display the list of products in the order.
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

        presenter = new OrderPreparationDetailsPresenter(this, EmployeeDAOMemory.getInstance(), OrderDAOMemory.getInstance());

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        String ordercode = getIntent().getStringExtra(ORD_CODE_EXTRA);

        ArrayList<CartItem> cartItems = presenter.loadOrder(employeeId, ordercode);


        RecyclerView recyclerView = findViewById(R.id.rvOrderDetailsProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        StockProductAdapter adapter = new StockProductAdapter(cartItems);

        recyclerView.setAdapter(adapter);

        Button btnStockCheck = findViewById(R.id.btnOrderDetailsStockCheck);

        btnStockCheck.setOnClickListener(order -> {
            presenter.checkStockOrder();
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderDetails(String ordercode, String customerName, String submissionDate, String price, OrderStatusType status) {
        ((TextView) findViewById(R.id.txtOrderDetailsOrderIdValue)).setText(ordercode);
        ((TextView) findViewById(R.id.txtOrderDetailsOrderCustomerNameValue)).setText(customerName);
        ((TextView) findViewById(R.id.txtOrderDetailsOrderSubmissionDateValue)).setText(submissionDate);
        ((TextView) findViewById(R.id.txtOrderDetailsOrderPriceValue)).setText(price);
        ((TextView) findViewById(R.id.txtOrderDetailsOrderStatus)).setText(status.toString());
    }

    /**
     * {@inheritDoc}
     * Shows an Android error alert dialog with an alert icon.
     */
    @Override
    public void showErrorMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Σφάλμα")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", null)
                .show();
    };

    /**
     * {@inheritDoc}
     * Shows a success Android alert dialog. Upon confirmation, the activity finishes.
     */
    @Override
    public void showSuccessMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Επιτυχία")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finishActivity() {
        finish();
    }
}