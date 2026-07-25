package gr.softeng.team21.view.admin.deleteEmp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.firebasedao.UserCredentialsDAOFirebase;

/**
 * Activity that displays the specific details of a selected employee and
 * mandates an explicit confirmation from the Admin before permanent deletion.
 * Implements MVP and uses Firebase DAOs via Dependency Injection.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class EmpInfoActivity extends AppCompatActivity implements EmpInfoView {

    private EmpInfoPresenter presenter;
    private Employee currentEmployee;

    /**
     * Initializes the layout, injects DAOs into the presenter, and initiates data loading.
     * @param savedInstanceState The previously saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emp_info);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Dependency Injection for Firebase DAOs
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UserCredentialsDAO credentialsDAO = new UserCredentialsDAOFirebase();

        presenter = new EmpInfoPresenter(this, employeeDAO, credentialsDAO);

        Intent intent = getIntent();
        String employeeUsername = intent.getStringExtra("EMPLOYEE_USERNAME");

        // Request data loading from Firebase
        if (employeeUsername != null) {
            presenter.loadEmployeeDetails(employeeUsername);
        }

        Button btnDeleteConfirm = findViewById(R.id.btnDeleteEmpConfirm);
        btnDeleteConfirm.setOnClickListener(v -> {
            if (currentEmployee != null) {
                showConfirmationDialog();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEmployeeDetails(Employee employee) {
        this.currentEmployee = employee;

        // UI updates must be executed on the Main Thread
        runOnUiThread(() -> {
            TextView txtDeleteFullName = findViewById(R.id.txtDeleteFullName);
            TextView txtDeletePhone = findViewById(R.id.txtDeletePhone);

            txtDeleteFullName.setText(employee.getFirstname() + " " + employee.getLastname());
            txtDeletePhone.setText(employee.getPhonenumber());
        });
    }

    /**
     * Displays a Material Design alert dialog forcing the Admin to confirm the destructive action.
     */
    private void showConfirmationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Οριστική Διαγραφή")
                .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε αυτόν τον υπάλληλο; Αυτή η ενέργεια δεν αναιρείται.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton("ΑΚΥΡΩΣΗ", null)
                .setPositiveButton("ΔΙΑΓΡΑΦΗ", (dialog, which) -> {
                    presenter.executeDeletion(currentEmployee);
                    Toast.makeText(this, "Ο υπάλληλος διαγράφηκε", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        runOnUiThread(() ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeScreen() {
        runOnUiThread(this::finish);
    }
}