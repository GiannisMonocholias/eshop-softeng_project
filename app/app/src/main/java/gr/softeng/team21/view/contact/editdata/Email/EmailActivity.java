package gr.softeng.team21.view.contact.editdata.Email;

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
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;

public class EmailActivity extends AppCompatActivity implements EmailView {
    private EditText etEmail;
    private Button btnSave;
    private Customer customer;
    private EmailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String customerId=getIntent().getStringExtra("CUSTOMER_ID");
        customer= CustomerDAOMemory.getInstance().getCustomer(customerId);
        presenter=new EmailPresenter(this,customer);
        etEmail = findViewById(R.id.edittxtEmailActivity);
        btnSave = findViewById(R.id.btnEmailActivitySave);

        btnSave.setOnClickListener(v -> saveEmail());
    }

    private void saveEmail() {
        String mailtxt = etEmail.getText().toString().trim();
        presenter.saveEmailClicked(mailtxt);

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
}