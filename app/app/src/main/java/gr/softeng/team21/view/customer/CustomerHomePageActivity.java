package gr.softeng.team21.view.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.view.MainActivity;
import gr.softeng.team21.view.user.User_EditData_activity;

public class CustomerHomePageActivity extends AppCompatActivity {

    // Δημιουργία του πελάτη (Hardcoded για τώρα)
    public static Customer customer = new Customer(
            "giannispap", "Giannis", "pass1234", "Papadopoulos",
            "697123456", new EmailAddress("giannis7@gmail.com"), "CUST-001", new Date());

    Button btnEditData, btnDeleteAccount, btnFindProduct, btnViewCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home_page);
<<<<<<< Updated upstream
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.delivererOrdersList), (v, insets) -> {
=======

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
>>>>>>> Stashed changes
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        User_EditData_activity.cus = customer;

        btnEditData = findViewById(R.id.btnCustomerHomePageEditData);
        btnEditData.setOnClickListener(v -> openEditDataScreen());

        btnDeleteAccount = findViewById(R.id.btnCustomerHomePageDeleteaccount);
        btnDeleteAccount.setOnClickListener(v -> showDeleteConfirmation());

        btnFindProduct = findViewById(R.id.btnCustomerHomePageFindProduct);

        btnFindProduct.setOnClickListener(v -> showFindProduct());

    }

    private void openEditDataScreen() {
        Intent intent = new Intent(CustomerHomePageActivity.this, User_EditData_activity.class);
        startActivity(intent);
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Διαγραφή Λογαριασμού")
                .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε τον λογαριασμό σας; Η ενέργεια δεν αναιρείται.")
                .setPositiveButton("Ναι", (dialog, which) -> DeleteAccount())
                .setNegativeButton("Όχι", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private void DeleteAccount() {
        try {
            if (customer != null) {
                customer.remove();
                Toast.makeText(this, "Ο λογαριασμός διαγράφηκε επιτυχώς.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Σφάλμα: Δεν βρέθηκε ο χρήστης.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Αποτυχία διαγραφής: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showFindProduct() {
        Intent intent = new Intent(CustomerHomePageActivity.this, Customer_FindProduct_Activity.class);
        startActivity(intent);
    }
}