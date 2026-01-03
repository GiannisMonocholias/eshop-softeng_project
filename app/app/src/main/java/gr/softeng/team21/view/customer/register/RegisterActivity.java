package gr.softeng.team21.view.customer.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.memorydao.CustomerDAOMemory;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private TextInputEditText edtRegisterUsername;
    private TextInputEditText edtRegisterPassword;
    private TextInputEditText edtRegisterName;
    private TextInputEditText edtRegisterSurname;
    private TextInputEditText edtRegisterEmail;
    private TextInputEditText edtRegisterPhone;

    private TextInputEditText edtRegisterStreet;
    private TextInputEditText edtRegisterNumber;
    private TextInputEditText edtRegisterCity;
    private TextInputEditText edtRegisterZip;

    private MaterialButton btnRegister;
    private TextView txtLoginLink;

    // Presenter
    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();

        presenter = new RegisterPresenter(this, CustomerDAOMemory.getInstance());

        btnRegister.setOnClickListener(v -> {
            String username = getTextFromField(edtRegisterUsername);
            String password = getTextFromField(edtRegisterPassword);
            String firstname = getTextFromField(edtRegisterName);
            String lastname = getTextFromField(edtRegisterSurname);
            String email = getTextFromField(edtRegisterEmail);
            String phone = getTextFromField(edtRegisterPhone);

            presenter.register(username, firstname, password, lastname, phone, email);
        });

        txtLoginLink.setOnClickListener(v -> {
            finish();
        });
    }

    private void initializeViews() {
        edtRegisterUsername = findViewById(R.id.edtRegisterUsername);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        edtRegisterName = findViewById(R.id.edtRegisterName);
        edtRegisterSurname = findViewById(R.id.edtRegisterSurname);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPhone = findViewById(R.id.edtRegisterPhone);

        edtRegisterStreet = findViewById(R.id.edtRegisterStreet);
        edtRegisterNumber = findViewById(R.id.edtRegisterNumber);
        edtRegisterCity = findViewById(R.id.edtRegisterCity);
        edtRegisterZip = findViewById(R.id.edtRegisterZip);

        btnRegister = findViewById(R.id.btnRegister);
        txtLoginLink = findViewById(R.id.txtLoginLink);
    }

    private String getTextFromField(EditText field) {
        if (field.getText() != null) {
            return field.getText().toString().trim();
        }
        return "";
    }


    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearInputFields() {
        edtRegisterUsername.setText("");
        edtRegisterPassword.setText("");
        edtRegisterName.setText("");
        edtRegisterSurname.setText("");
        edtRegisterEmail.setText("");
        edtRegisterPhone.setText("");

        edtRegisterStreet.setText("");
        edtRegisterNumber.setText("");
        edtRegisterCity.setText("");
        edtRegisterZip.setText("");

        edtRegisterUsername.requestFocus();
    }
}