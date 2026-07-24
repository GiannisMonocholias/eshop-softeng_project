package gr.softeng.team21.view.admin.createEmp.employeeRegistration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.firebasedao.UserCredentialsDAOFirebase;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * The dynamic Android Activity responsible for handling the user interface of
 * the employee registration process. It implements the {@link EmployeeRegistrationView}
 * and interacts strictly with its Presenter using DI and async UI updates.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class EmployeeRegistrationActivity extends AppCompatActivity implements EmployeeRegistrationView {

    private EmployeeRegistrationPresenter presenter;

    private TextView txtHeader;
    private TextInputLayout tilMaxQuantity;
    private TextInputEditText edtUsername, edtPassword, edtName, edtSurname, edtEmail, edtSalary, edtHours, edtMaxQuantity;
    private Button btnSubmit;
    private TextView btnChangePhoto;

    /**
     * Prepares the Android layout, injects DAOs, extracts the requested employee type
     * from the incoming intent, and orders the presenter to adapt the UI accordingly.
     * @param savedInstanceState Application state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_registration);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.employeeRegistrationActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();

        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UserCredentialsDAO userCredentialsDAO = new UserCredentialsDAOFirebase();

        presenter = new EmployeeRegistrationPresenter(this, employeeDAO, userCredentialsDAO);

        String employeeType = getIntent().getStringExtra("EMPLOYEE_TYPE");
        if (employeeType == null) employeeType = "CUSTOMER_SERVICE";

        presenter.setupUIForType(employeeType);

        btnChangePhoto.setOnClickListener(v -> {
        });

        btnSubmit.setOnClickListener(v -> presenter.onSubmitClicked());
    }

    /**
     * Binds the Java variables to their respective XML components.
     */
    private void initializeViews() {
        txtHeader = findViewById(R.id.txtRegisterHeader);
        tilMaxQuantity = findViewById(R.id.tilMaxQuantity);

        edtUsername = findViewById(R.id.edtRegUsername);
        edtPassword = findViewById(R.id.edtRegPassword);
        edtName = findViewById(R.id.edtRegName);
        edtSurname = findViewById(R.id.edtRegSurname);
        edtEmail = findViewById(R.id.edtRegEmail);
        edtSalary = findViewById(R.id.edtRegSalary);
        edtHours = findViewById(R.id.edtRegHours);
        edtMaxQuantity = findViewById(R.id.edtRegMaxQuantity);

        btnSubmit = findViewById(R.id.btnSubmitRegistration);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
    }

    /** {@inheritDoc} */
    @Override public void setHeaderTitle(String title) { runOnUiThread(() -> txtHeader.setText(title)); }

    /** {@inheritDoc} */
    @Override public void showDelivererSpecificFields() { runOnUiThread(() -> tilMaxQuantity.setVisibility(View.VISIBLE)); }

    /** {@inheritDoc} */
    @Override public String getUsername() { return edtUsername.getText() != null ? edtUsername.getText().toString() : ""; }

    /** {@inheritDoc} */
    @Override public String getPassword() { return edtPassword.getText() != null ? edtPassword.getText().toString() : ""; }

    /** {@inheritDoc} */
    @Override public String getFirstName() { return edtName.getText() != null ? edtName.getText().toString() : ""; }

    /** {@inheritDoc} */
    @Override public String getLastName() { return edtSurname.getText() != null ? edtSurname.getText().toString() : ""; }

    /** {@inheritDoc} */
    @Override public String getEmail() { return edtEmail.getText() != null ? edtEmail.getText().toString() : ""; }

    /** {@inheritDoc} */
    @Override public String getSalary() { return edtSalary.getText() != null ? edtSalary.getText().toString() : ""; }

    /** {@inheritDoc} */
    @Override public String getWorkingHours() { return edtHours.getText() != null ? edtHours.getText().toString() : ""; }

    /** {@inheritDoc} */
    @Override public String getMaxQuantity() { return edtMaxQuantity.getText() != null ? edtMaxQuantity.getText().toString() : ""; }

    /** {@inheritDoc} */
    @Override
    public void showConfirmDialog(Employee employee) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Επιβεβαίωση Στοιχείων")
                    .setMessage("Είστε βέβαιοι ότι θέλετε να προσθέσετε τον/την υπάλληλο " + employee.getFirstname() + " " + employee.getLastname() + ";")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("ΝΑΙ", (dialog, which) -> presenter.onRegistrationConfirmed(employee))
                    .setNegativeButton("ΑΚΥΡΟ", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    /** {@inheritDoc} */
    @Override
    public void showSuccessMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    /** {@inheritDoc} */
    @Override
    public void showErrorMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    /** {@inheritDoc} */
    @Override
    public void finishActivity() {
        finish();
    }
}