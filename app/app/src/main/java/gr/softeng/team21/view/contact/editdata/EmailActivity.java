package gr.softeng.team21.view.contact.editdata;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.view.customer.CustomerHomePageActivity;
import gr.softeng.team21.view.user.User_EditData_activity;

public class EmailActivity extends AppCompatActivity {
    EditText etEmail;
    Button btnSave;
    private Customer curcustomer = User_EditData_activity.cus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        etEmail = findViewById(R.id.edittxtEmailActivity);
        btnSave = findViewById(R.id.btnEmailActivitySave);

        btnSave.setOnClickListener(v -> saveEmail());
    }

    private void saveEmail() {
        String mailText = etEmail.getText().toString().trim();
        if (mailText.isEmpty()) {
            Toast.makeText(this, "Παρακαλώ εισάγετε email", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Δημιουργία αντικειμένου EmailAddress (όπως ορίσαμε στο domain)
            EmailAddress newEmail = new EmailAddress(mailText);

            // Choice "4" για Email -> περνάμε το αντικείμενο newEmail
            curcustomer.editData("4", null, null, newEmail);

            Toast.makeText(this, "Το email ενημερώθηκε!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}