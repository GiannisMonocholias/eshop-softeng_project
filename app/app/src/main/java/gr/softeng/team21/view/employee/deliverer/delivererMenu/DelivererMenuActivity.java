package gr.softeng.team21.view.employee.deliverer.delivererMenu;

import android.content.DialogInterface;
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
import gr.softeng.team21.view.employee.deliverer.delivererOrdersList.DelivererOrdersListActivity;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;
import gr.softeng.team21.view.user.login.LoginActivity;

/**
 * Main Activity representing the Deliverer's dashboard.
 * Manages UI interactions and implements the {@link DelivererMenuView}
 * to handle navigation and feedback asynchronously using runOnUiThread.
 * Uses Material Design components for dialogs and incorporates Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererMenuActivity extends AppCompatActivity implements DelivererMenuView {

    private DelivererMenuPresenter presenter;
    private static final String EMP_ID_EXTRA = "DELIVERER_ID";
    private String employeeId;

    /**
     * Initializes the layout, injects DAOs into the presenter, binds UI listeners,
     * and notifies the presenter that the view has been created.
     * @param savedInstanceState The saved state bundle if the activity is re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deliverer_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityDelivererDashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // DEPENDENCY INJECTION
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UserCredentialsDAO userCredentialsDAO = UserCredentialsDAOMemory.getInstance(); // Will be replaced by Firebase equivalent later

        presenter = new DelivererMenuPresenter(this, employeeDAO, userCredentialsDAO);

        employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);

        presenter.onViewCreated(employeeId);

        findViewById(R.id.btnDelivererMenuOrdersList).setOnClickListener(v ->
                presenter.onOrdersListSelected(employeeId)
        );

        findViewById(R.id.btnDelivererMenuProcessAccount).setOnClickListener(v ->
                presenter.onProcessAccountSelected(employeeId)
        );

        findViewById(R.id.btnDelivererMenuDeleteAccount).setOnClickListener(v ->
                presenter.onDeleteAccountSelected()
        );

        findViewById(R.id.btnDelivererMenuLogout).setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEmployeeName(String fullname) {
        runOnUiThread(() -> {
            ((TextView) findViewById(R.id.txtDelivererMenuName)).setText(fullname);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToOrdersList(String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(DelivererMenuActivity.this, DelivererOrdersListActivity.class);
            intent.putExtra(EMP_ID_EXTRA, employeeId);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     * Shows a {@link MaterialAlertDialogBuilder} for a modernized and secure confirmation flow.
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
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }
}