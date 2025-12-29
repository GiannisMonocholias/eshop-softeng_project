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
    public static  Customer customer = new Customer(
            "giannispap", "Giannis", "pass1234", "Papadopoulos",
            "697123456", new EmailAddress("giannis7@gmail.com"), "CUST-001", new Date());
Button btnEditData,btnDeleteAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         btnDeleteAccount = findViewById(R.id.btnCustomerHomePageDeleteaccount);
        btnDeleteAccount.setOnClickListener(v -> showDeleteConfirmation());
         btnEditData = findViewById(R.id.btnCustomerHomePageEditData);
        btnEditData.setOnClickListener(v -> openEditDataScreen());
    }

    private void openEditDataScreen() {
        Intent intent=new Intent(CustomerHomePageActivity.this,User_EditData_activity.class);
        startActivity(intent);
    }


    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Διαγραφή Λογαριασμού")
                .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε τον λογαριασμό σας; Η ενέργεια δεν αναιρείται.")
                .setPositiveButton("Ναι", (dialog, which) -> DeleteAccount())
                .setNegativeButton("Όχι", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void DeleteAccount() {
        try {
            if (customer != null) {
                customer.remove();
                Toast.makeText(this, "Ο λογαριασμός διαγράφηκε επιτυχώς.", Toast.LENGTH_LONG).show();
                // Αυτό μπαίνει μόλις επιβεβαιωθεί η διαγραφή και γυρίζει στην αρχική οθονη
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
}






