package gr.softeng.team21.view.customer.register;

import android.os.Bundle;
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
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.UserCredentialsDAOFirebase;

/**
 * Activity providing the UI for new customer registration.
 * Manages form inputs, coordinates with {@link RegisterPresenter}, and securely
 * handles asynchronous UI updates via runOnUiThread.
 * @author Γιάννης Μονοχολιάς
 */
public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private TextInputEditText edtRegisterUsername, edtRegisterPassword, edtRegisterName;
    private TextInputEditText edtRegisterSurname, edtRegisterEmail, edtRegisterPhone;
    private TextInputEditText edtRegisterStreet, edtRegisterNumber, edtRegisterCity, edtRegisterZip;

    private MaterialButton btnRegister;
    private TextView txtLoginLink;

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

        // Dependency Injection with Firebase DAOs
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        UserCredentialsDAO credentialsDAO = new UserCredentialsDAOFirebase();

        presenter = new RegisterPresenter(this, customerDAO, credentialsDAO);

        btnRegister.setOnClickListener(v -> {
            String username = getTextFromField(edtRegisterUsername);
            String password = getTextFromField(edtRegisterPassword);
            String firstname = getTextFromField(edtRegisterName);
            String lastname = getTextFromField(edtRegisterSurname);
            String email = getTextFromField(edtRegisterEmail);
            String phone = getTextFromField(edtRegisterPhone);

            presenter.register(username, firstname, password, lastname, phone, email);
        });

        txtLoginLink.setOnClickListener(v -> finish());
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
        return (field.getText() != null) ? field.getText().toString().trim() : "";
    }

    /** {@inheritDoc} */
    @Override
    public void showSuccessMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    /** {@inheritDoc} */
    @Override
    public void showErrorMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    /** {@inheritDoc} */
    @Override
    public void clearInputFields() {
        runOnUiThread(() -> {
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
        });
    }
}