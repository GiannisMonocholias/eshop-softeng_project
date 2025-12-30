package gr.softeng.team21.view.customer;

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
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.view.user.User_EditData_activity;

public class CustomerCardPaymentActivity extends AppCompatActivity {
    EditText eTCardNumber;
    Button btnPay;
    Customer cus = User_EditData_activity.cus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_card_payment);

        // Edge-to-Edge (Δουλεύει σωστά γιατί έβαλες id="main" στο XML)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.delivererOrdersList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Σύνδεση με τα δικά σου IDs από το XML
        eTCardNumber = findViewById(R.id.edittxtCustomerCardPaymentActivityCardNumber);
        btnPay = findViewById(R.id.btnCustomerCardPaymentActivityPayment);

        btnPay.setOnClickListener(v -> CardPayment());
    }

    private void CardPayment() {
        String cardNum = eTCardNumber.getText().toString().trim();

        if (cardNum.isEmpty()) {
            Toast.makeText(this, "Παρακαλώ εισάγετε τον αριθμό κάρτας!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Order newOrder = cus.Checkout();
            if (newOrder == null) {
                Toast.makeText(this, "Το καλάθι είναι άδειο!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Ορισμός Πληρωμής με ΚΑΡΤΑ
            cus.selectPaymentType(PaymentType.CARD, cardNum, newOrder);

            // 3. Εμφάνιση Dialog Επιβεβαίωσης
            new AlertDialog.Builder(this)
                    .setTitle("Επιβεβαίωση Πληρωμής")
                    .setMessage("Θα χρεωθεί η κάρτα: " + cardNum + "\nΘέλετε να προχωρήσετε;")
                    .setCancelable(false)
                    .setPositiveButton("Επιβεβαίωση", (dialog, which) -> Confirm(newOrder))
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
        Intent intent = new Intent(CustomerCardPaymentActivity.this, CustomerHomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}
