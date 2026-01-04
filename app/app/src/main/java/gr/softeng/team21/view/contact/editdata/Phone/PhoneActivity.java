package gr.softeng.team21.view.contact.editdata.Phone;

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

public class PhoneActivity extends AppCompatActivity implements PhoneView {

    private EditText etPhone;
    private Button btnSave;
    private PhonePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phone);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Λήψη του user_id
        String userId = getIntent().getStringExtra("user_id");

        etPhone = findViewById(R.id.edittxtPhoneActivity);
        btnSave = findViewById(R.id.btnPhoneActivitySave);

        // Δημιουργία του Presenter με userId
        presenter = new PhonePresenter(this, userId);

        btnSave.setOnClickListener(v -> savePhone());
    }

    private void savePhone() {
        String phone = etPhone.getText().toString().trim();
        presenter.savePhoneClicked(phone);
    }

    @Override
    public void setPhone(String phone) {
        etPhone.setText(phone);
        if (phone != null) {
            etPhone.setSelection(phone.length());
        }
    }

    @Override
    public void SaveSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishView() {
        finish();
    }
}