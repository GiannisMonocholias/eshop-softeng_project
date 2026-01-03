package gr.softeng.team21.view.customer.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.view.customer.homePage.CustomerHomePageActivity;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;

public class CustomerCardPaymentActivity extends AppCompatActivity implements CustomerCardPaymentView {
    private EditText eTCardNumber;
   private Button btnPay;
   private CustomerCardPaymentPresenter presenter;
   private Customer customer;

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
        String customerId=getIntent().getStringExtra("CUSTOMER_ID");
        customer= CustomerDAOMemory.getInstance().getCustomer(customerId);
        presenter=new CustomerCardPaymentPresenter(this,customer);
        eTCardNumber = findViewById(R.id.edittxtCustomerCardPaymentActivityCardNumber);
        btnPay = findViewById(R.id.btnCustomerCardPaymentActivityPayment);
        btnPay.setOnClickListener(v -> CardPayment());
    }

    private void CardPayment() {
        String cardNumber = eTCardNumber.getText().toString().trim();
        presenter.CardPaymentClicked(cardNumber);

    }

    @Override
    public void showConfirmation(String amount) {
        new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση ή Ακύρωση")
                .setMessage("Η κάρτα σας θα χρεωθεί το ποσό: "+amount+"\nΝα καταχωρηθεί η παραγγελία σας;")
                .setCancelable(false)
                .setPositiveButton("Επιβεβαίωση Παραγγελίας", (dialog, which) -> presenter.ConfirmClicked())
                .setNegativeButton("Ακύρωση", (dialog, which) -> presenter.CancelClicked())
                .show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void goToCustomerHomePage() {
        Intent intent = new Intent(CustomerCardPaymentActivity.this, CustomerHomePageActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
