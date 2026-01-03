package gr.softeng.team21.view.contact.editdata;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.view.user.User_EditData_activity;

public class PhoneActivity extends AppCompatActivity {
    EditText etPhone;
    Button btnSave;
    private Customer curcustomer = User_EditData_activity.cus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        etPhone = findViewById(R.id.edittxtPhoneActivity);
        btnSave = findViewById(R.id.btnPhoneActivitySave);

        btnSave.setOnClickListener(v -> savePhone());
    }

    private void savePhone() {
        String phone = etPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            Toast.makeText(this, "Παρακαλώ εισάγετε τηλέφωνο", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Choice "5" για Τηλέφωνο
            curcustomer.editData("5", phone, null, null);

            Toast.makeText(this, "Το τηλέφωνο ενημερώθηκε!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}