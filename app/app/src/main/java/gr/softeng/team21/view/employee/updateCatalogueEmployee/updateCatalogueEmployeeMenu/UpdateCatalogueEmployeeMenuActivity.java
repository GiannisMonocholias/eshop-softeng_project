package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

import android.content.Intent;
import android.os.Bundle;
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
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;
import gr.softeng.team21.view.user.login.LoginActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute.AssignedRequestsToExecuteActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign.AvailableRequestsToAssignActivity;

/**
 * Main dashboard Activity for Update Catalogue Employees.
 * Provides the interface for navigating to assigned or available requests
 * and managing account settings. Secures UI updates using runOnUiThread
 * and incorporates Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateCatalogueEmployeeMenuActivity extends AppCompatActivity implements UpdateCatalogueEmployeeMenuView {

    private UpdateCatalogueEmployeeMenuPresenter presenter;
    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";
    private String employeeId;

    /**
     * Configures the layout, attaches listeners to menu buttons,
     * injects DAOs, and initializes the presenter.
     * @param savedInstanceState If the activity is being re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_catalogue_employee_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.UpdateCatalogueEmployeeMenu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // DEPENDENCY INJECTION: Connect Presenter to Firebase DAOs
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UserCredentialsDAO userCredentialsDAO = UserCredentialsDAOMemory.getInstance(); // Replace with Firebase equivalent later

        presenter = new UpdateCatalogueEmployeeMenuPresenter(this, employeeDAO, userCredentialsDAO);

        employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        presenter.onViewCreated(employeeId);

        // Listeners
        findViewById(R.id.btnUptCatEmpMenuAssignedRequests).setOnClickListener(v -> presenter.onClickAssignedRequests(employeeId));
        findViewById(R.id.btnUptCatEmpMenuAssignNewRequest).setOnClickListener(v -> presenter.onClickAvailableRequestsToAssign(employeeId));
        findViewById(R.id.btnUptCatEmpMenuProcessAccount).setOnClickListener(v -> presenter.onProcessAccountSelected(employeeId));
        findViewById(R.id.btnUptCatEmpMenuDeleteAccount).setOnClickListener(v -> presenter.onDeleteAccountSelected());

        // Logout Button
        findViewById(R.id.btnUptCatEmpMenuLogout).setOnClickListener(v -> finish());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEmployeeName(String fullName) {
        runOnUiThread(() -> ((TextView) findViewById(R.id.txtUptCatEmpMenuName)).setText(fullName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToAssignedRequests(String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(UpdateCatalogueEmployeeMenuActivity.this, AssignedRequestsToExecuteActivity.class);
            intent.putExtra(EMP_ID_EXTRA, employeeId);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToAvailableRequestsToAssign(String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(UpdateCatalogueEmployeeMenuActivity.this, AvailableRequestsToAssignActivity.class);
            intent.putExtra(EMP_ID_EXTRA, employeeId);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     * Shows a modern {@link MaterialAlertDialogBuilder} with confirmation logic.
     */
    @Override
    public void showDeleteAccountConfirmation() {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Διαγραφή Λογαριασμού")
                    .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε τον λογαριασμό σας; Αυτή η ενέργεια δεν μπορεί να αναιρεθεί.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("ΝΑΙ", (dialog, which) -> presenter.onDeleteAccountConfirmed(employeeId))
                    .setNegativeButton("ΟΧΙ", null)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToLogin() {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToProcessAccount(String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, UserEditDataActivity.class);
            intent.putExtra("user_id", employeeId);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
}