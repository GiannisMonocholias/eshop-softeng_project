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
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.util.Money;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.view.customer.homePage.CustomerHomePageActivity;

/**
 * Activity responsible for handling the payment process for the customer.
 * Implements {@link CustomerPaymentView} and manages UI elements,such as buttons and textView for payment method selection,
 * shipping details display and final confirmation.
 * @author PAVLOS GRATSANIS
 */
public class CustomerPaymentActivity extends AppCompatActivity implements CustomerPaymentView {

    private TextView txtFinalAmount,txtShippingName, txtShippingAddress, txtShippingPhone;
    private Button btnPay;
    private RadioButton rbCash, rbCard;
    private CustomerPaymentPresenter presenter;
    private Customer customer;

    /**
     * Initializes the activity, sets the UI layout, retrieves the customer ID,
     * initializes the presenter,customer and sets up UI components like text views for shipping info and buttons for payment.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
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
        String customerId=getIntent().getStringExtra("CUSTOMER_ID");
        customer= CustomerDAOMemory.getInstance().getCustomer(customerId);
        presenter = new CustomerPaymentPresenter(this, customer);
        txtFinalAmount = findViewById(R.id.txtCustomerPaymentActivityFinalAmount);
        txtShippingName = findViewById(R.id.txtCustomerPaymentActivityShippingName);
        txtShippingAddress = findViewById(R.id.txtCustomerPaymentActivityShippingAddress);
        txtShippingPhone = findViewById(R.id.txtCustomerPaymentActivityShippingPhone);
        btnPay = findViewById(R.id.btnCustomerPaymentActivityPayment);
        rbCash = findViewById(R.id.rbCustomerPaymentActivityCash);
        rbCard = findViewById(R.id.rbCustomerPaymentActivityCard);
        btnPay.setOnClickListener(v -> payment(rbCash.isChecked()));
        setdata();
    }

    /**
     * Retrieves and sets the initial data for the view (payment amount and shipping details).
     */
    private void setdata() {
        setpayamount();
        setShippingDetails();

    }

    /**
     *Calls the corresponding presenter method.
     */
    private void setShippingDetails() {
        presenter.loadShippingDetails();
    }

    /**
     * Retrieves the total cost from the cart and calls the corresponding presenter method.
     */
    private void setpayamount() {
        String amount = customer.getShoppingCart().getTotalCost().toString();
        presenter.setpaymentClicked(amount);

    }

    /**
     *Calls the corresponding presenter method
     * @param check True if Cash is selected, false otherwise.
     */
    private void payment(boolean check) {
        presenter.paymentClicked(check);

    }

    /**
     * {@inheritDoc}
     * Shows a short Toast message to the user.
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    /**
     * {@inheritDoc}
     * Navigates back to the HomePage, clearing the activity stack,via an Intent, passing the customer's ID as an extra.
     */
    @Override
    public void goToCustomerHomePage() {
        Intent intent = new Intent(CustomerPaymentActivity.this, CustomerHomePageActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * {@inheritDoc}
     * Starts the CustomerCardPaymentActivity for card transactions via an Intent, passing the customer's ID as an extra.
     */
    @Override
    public void goToToCardPayment() {
        Intent intent = new Intent(CustomerPaymentActivity.this, CustomerCardPaymentActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Displays an AlertDialog to confirm or cancel the order.
     */
    @Override
    public void showConfirmation(Money amount) {
        new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση ή Ακύρωση")
                .setMessage("Να καταχωρηθεί η παραγγελία σας αξίας: "+amount+";")
                .setCancelable(false)
                .setPositiveButton("Επιβεβαίωση Παραγγελίας", (dialog, which) -> presenter.ConfirmClicked())
                .setNegativeButton("Ακύρωση", (dialog, which) -> presenter.CancelClicked())
                .show();
    }

    /**
     * {@inheritDoc}
     * Updates the text view with the final total amount.
     */
    @Override
    public void showTotalAmount(String amount) {
        txtFinalAmount.setText(amount);
    }

    /**
     * {@inheritDoc}
     * Updates the UI with the shipping details (name, address, phone).
     */
    @Override
    public void showShippingDetails(String name, String address, String phone) {
        txtShippingName.setText(name);
        txtShippingAddress.setText(address);
        txtShippingPhone.setText(phone);
    }
}