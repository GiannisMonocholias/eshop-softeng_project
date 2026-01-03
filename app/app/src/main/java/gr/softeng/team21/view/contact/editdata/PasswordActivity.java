package gr.softeng.team21.view.contact.editdata;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.view.user.User_EditData_activity;

public class PasswordActivity extends AppCompatActivity {
    EditText etPass;
    Button btnSave;
    private Customer curcustomer = User_EditData_activity.cus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        etPass = findViewById(R.id.edittxtPasswordActivity);
        btnSave = findViewById(R.id.btnPasswordActivitySave);

        btnSave.setOnClickListener(v -> savePassword());
    }

    private void savePassword() {
        String pass = etPass.getText().toString().trim();
        if (pass.isEmpty()) {
            Toast.makeText(this, "Παρακαλώ εισάγετε νέο κωδικό", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            curcustomer.editData("2", pass, null, null);

            Toast.makeText(this, "Ο κωδικός ενημερώθηκε!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}