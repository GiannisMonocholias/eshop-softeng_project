package gr.softeng.team21.view.customer.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.OrderDAOFirebase;
import gr.softeng.team21.util.Money;
import gr.softeng.team21.view.customer.homePage.CustomerHomePageActivity;

/**
 * Activity responsible for handling the payment checkout process.
 * Manages UI controls for payment method selection, displays order summary & shipping details,
 * and delegates business operations to {@link CustomerPaymentPresenter}.
 * @author PAVLOS GRATSANIS
 */
public class CustomerPaymentActivity extends AppCompatActivity implements CustomerPaymentView {

    private TextView txtFinalAmount, txtShippingName, txtShippingAddress, txtShippingPhone;
    private Button btnPay;
    private RadioButton rbCash, rbCard;
    private CustomerPaymentPresenter presenter;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_payment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        customerId = getIntent().getStringExtra("CUSTOMER_ID");

        txtFinalAmount = findViewById(R.id.txtCustomerPaymentActivityFinalAmount);
        txtShippingName = findViewById(R.id.txtCustomerPaymentActivityShippingName);
        txtShippingAddress = findViewById(R.id.txtCustomerPaymentActivityShippingAddress);
        txtShippingPhone = findViewById(R.id.txtCustomerPaymentActivityShippingPhone);
        btnPay = findViewById(R.id.btnCustomerPaymentActivityPayment);
        rbCash = findViewById(R.id.rbCustomerPaymentActivityCash);
        rbCard = findViewById(R.id.rbCustomerPaymentActivityCard);

        // Dependency Injection with Firebase DAOs
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        OrderDAO orderDAO = new OrderDAOFirebase();
        presenter = new CustomerPaymentPresenter(this, customerDAO, orderDAO);

        btnPay.setOnClickListener(v -> presenter.paymentClicked(rbCash.isChecked()));

        // Asynchronously load initial data
        presenter.loadInitialData(customerId);
    }

    /** {@inheritDoc} */
    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    /** {@inheritDoc} */
    @Override
    public void goToCustomerHomePage() {
        runOnUiThread(() -> {
            Intent intent = new Intent(CustomerPaymentActivity.this, CustomerHomePageActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    /** {@inheritDoc} */
    @Override
    public void goToToCardPayment() {
        runOnUiThread(() -> {
            Intent intent = new Intent(CustomerPaymentActivity.this, CustomerCardPaymentActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId);
            startActivity(intent);
        });
    }

    /** {@inheritDoc} */
    @Override
    public void showConfirmation(Money amount) {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση ή Ακύρωση")
                .setMessage("Να καταχωρηθεί η παραγγελία σας αξίας: " + amount + ";")
                .setCancelable(false)
                .setPositiveButton("Επιβεβαίωση Παραγγελίας", (dialog, which) -> presenter.ConfirmClicked())
                .setNegativeButton("Ακύρωση", (dialog, which) -> presenter.CancelClicked())
                .show());
    }

    /** {@inheritDoc} */
    @Override
    public void showTotalAmount(String amount) {
        runOnUiThread(() -> txtFinalAmount.setText(amount));
    }

    /** {@inheritDoc} */
    @Override
    public void showShippingDetails(String name, String address, String phone) {
        runOnUiThread(() -> {
            txtShippingName.setText(name);
            txtShippingAddress.setText(address);
            txtShippingPhone.setText(phone);
        });
    }
}