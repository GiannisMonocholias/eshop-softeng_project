package gr.softeng.team21.view.customer;

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
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.view.user.User_EditData_activity;

public class CustomerPaymentActivity extends AppCompatActivity {

    TextView txtFinalAmount;
    Button btnPay;
    RadioButton rbCash, rbCard;

    Customer cus = User_EditData_activity.cus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_payment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.delivererOrdersList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtFinalAmount = findViewById(R.id.txtCustomerPaymentActivityFinalAmount);
        btnPay = findViewById(R.id.btnCustomerPaymentActivityPayment);
        rbCash = findViewById(R.id.rbCustomerPaymentActivityCash);
        rbCard = findViewById(R.id.rbCustomerPaymentActivityCard);

        setpayamount();

        // Κουμπί Πληρωμής
        btnPay.setOnClickListener(v -> Payment());
    }

    private void setpayamount() {
        if (cus != null && cus.getShoppingCart() != null) {
            String amount = cus.getShoppingCart().getTotalCost().toString();
            txtFinalAmount.setText("Σύνολο: " + amount);
        }
    }

    private void Payment() {
        if (rbCash.isChecked()) {
            CashPayment();
        } else {
            Intent intent = new Intent(CustomerPaymentActivity.this, CustomerCardPaymentActivity.class);
            startActivity(intent);
        }
    }

    private void CashPayment() {
        try {
            // 1. Δημιουργία Παραγγελίας
            Order newOrder = cus.Checkout();
            if (newOrder == null) {
                Toast.makeText(this, "Το καλάθι είναι άδειο!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Ορισμός Τρόπου Πληρωμής
            cus.selectPaymentType(PaymentType.CASH, "", newOrder);

            // 3. Εμφάνιση Dialog
            new AlertDialog.Builder(this)
                    .setTitle("Επιβεβαίωση ή Ακύρωση")
                    .setMessage("Να καταχωρηθεί η παραγγελία σας;")
                    .setCancelable(false)
                    .setPositiveButton("Επιβεβαίωση Παραγγελίας", (dialog, which) -> Confirm(newOrder))
                    .setNegativeButton("Ακύρωση", (dialog, which) -> Cancel(newOrder))
                    .show();


        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Confirm(Order order) {
        try {
            cus.Confirm("CONFIRM", order);
            Toast.makeText(this, "Η παραγγελία σας καταχωρήθυκε.", Toast.LENGTH_SHORT).show();
           goCustomerHomePage();
        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Cancel(Order order) {
        try {
            cus.Confirm("CANCEL", order);
            Toast.makeText(this, "Η παραγγελία σας ακυρώθηκε.", Toast.LENGTH_SHORT).show();
            goCustomerHomePage();
        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void goCustomerHomePage() {
        Intent intent = new Intent(CustomerPaymentActivity.this, CustomerHomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}