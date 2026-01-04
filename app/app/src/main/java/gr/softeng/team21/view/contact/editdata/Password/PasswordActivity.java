package gr.softeng.team21.view.contact.editdata.Password;

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
import gr.softeng.team21.view.contact.editdata.Email.EmailPresenter;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;

public class PasswordActivity extends AppCompatActivity implements PasswordView {
    private EditText etPass;
   private Button btnSave;
   private PasswordPresenter presenter;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String customerId=getIntent().getStringExtra("CUSTOMER_ID");
        customer= CustomerDAOMemory.getInstance().getCustomer(customerId);
        presenter=new PasswordPresenter(this,customer);
        etPass = findViewById(R.id.edittxtPasswordActivity);
        btnSave = findViewById(R.id.btnPasswordActivitySave);

        btnSave.setOnClickListener(v -> savePassword());
    }

    private void savePassword() {
        String password = etPass.getText().toString().trim();
        presenter.savePasswordClicked(password);

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