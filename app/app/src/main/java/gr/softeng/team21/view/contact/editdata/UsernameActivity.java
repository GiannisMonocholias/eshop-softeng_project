package gr.softeng.team21.view.contact.editdata;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.view.user.User_EditData_activity;

public class UsernameActivity extends AppCompatActivity {
    EditText etUsername;
    Button btnsave;
    private Customer curcustomer = User_EditData_activity.cus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_username);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etUsername = findViewById(R.id.edittxtUsernameActivity);
        btnsave = findViewById(R.id.btnUsernameActivitySave);
        btnsave.setOnClickListener(v -> saveUsername());
    }

    private void saveUsername() {
        String name = etUsername.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Παρακαλώ συμπληρώστε τo πεδίo", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            curcustomer.editData("1", name, null, null);
            Toast.makeText(this, "To username ενημερώθηκε!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
