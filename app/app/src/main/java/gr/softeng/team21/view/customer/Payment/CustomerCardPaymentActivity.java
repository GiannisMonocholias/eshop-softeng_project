package gr.softeng.team21.view.customer.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
 * Activity responsible for handling the customer's card payment process.
 * Manages UI inputs for card details and coordinates with the {@link CustomerCardPaymentPresenter}.
 * @author PAVLOS GRATSANIS
 */
public class CustomerCardPaymentActivity extends AppCompatActivity implements CustomerCardPaymentView {

    private EditText eTCardNumber;
    private Button btnPay;
    private CustomerCardPaymentPresenter presenter;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_card_payment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        customerId = getIntent().getStringExtra("CUSTOMER_ID");

        // Dependency Injection with Firebase DAOs
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        OrderDAO orderDAO = new OrderDAOFirebase();

        presenter = new CustomerCardPaymentPresenter(this, customerDAO, orderDAO);

        eTCardNumber = findViewById(R.id.edittxtCustomerCardPaymentActivityCardNumber);
        btnPay = findViewById(R.id.btnCustomerCardPaymentActivityPayment);

        btnPay.setOnClickListener(v -> CardPayment());

        // Load customer data asynchronously
        presenter.loadInitialData(customerId);
    }

    private void CardPayment() {
        String cardNumber = eTCardNumber.getText().toString().trim();
        presenter.CardPaymentClicked(cardNumber);
    }

    /** {@inheritDoc} */
    @Override
    public void showConfirmation(Money amount) {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση ή Ακύρωση")
                .setMessage("Η κάρτα σας θα χρεωθεί το ποσό: " + amount + "\nΝα καταχωρηθεί η παραγγελία σας;")
                .setCancelable(false)
                .setPositiveButton("Επιβεβαίωση Παραγγελίας", (dialog, which) -> presenter.ConfirmClicked())
                .setNegativeButton("Ακύρωση", (dialog, which) -> presenter.CancelClicked())
                .show());
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
            Intent intent = new Intent(CustomerCardPaymentActivity.this, CustomerHomePageActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}