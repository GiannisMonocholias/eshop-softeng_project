package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;
import gr.softeng.team21.view.user.login.LoginActivity;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListActivity;
import gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus.OrderStatusActivity;

/**
 * Main menu activity for the Customer Service Employee.
 * Handles UI components, click listeners, and navigation via Android Intents.
 * Implements {@link CustomerServiceMenuView} and uses runOnUiThread to safely update
 * the interface based on asynchronous Presenter logic.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceMenuActivity extends AppCompatActivity implements CustomerServiceMenuView {

    private CustomerServiceMenuPresenter presenter;
    private static final String EMP_ID_EXTRA = "CUSTOMER_SERVICE_EMPLOYEE_ID";
    private String employeeId;

    /**
     * Initializes the activity layout, injects DAOs into the presenter, and
     * attaches listeners to the menu buttons.
     * @param savedInstanceState The saved state bundle if the activity is re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_service_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // DEPENDENCY INJECTION
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UserCredentialsDAO userCredentialsDAO = UserCredentialsDAOMemory.getInstance(); // Replace with Firebase equivalent later

        presenter = new CustomerServiceMenuPresenter(this, employeeDAO, userCredentialsDAO);

        employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);

        presenter.onViewCreated(employeeId);

        findViewById(R.id.btnCustomerServiceEmployeeMenuEmailInbox).setOnClickListener(v ->
                presenter.onInboxSelected(employeeId)
        );

        findViewById(R.id.btnCustomerServiceEmployeeMenuNotifyOrdersStatus).setOnClickListener(v ->
                presenter.onOrderStatusSelected(employeeId)
        );

        findViewById(R.id.btnCustomerServiceEmployeeMenuProcessAccount).setOnClickListener( v ->
                presenter.onProcessAccountSelected(employeeId)
        );

        findViewById(R.id.btnCustomerServiceEmployeeMenuDeleteAccount).setOnClickListener(v ->
                presenter.onDeleteAccountSelected()
        );

        findViewById(R.id.btnCustomerServiceEmployeeMenuLogout).setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEmployeeName(String fullname) {
        runOnUiThread(() -> {
            ((TextView) findViewById(R.id.txtCustomerServiceEmployeeMenuName)).setText(fullname);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToOrderStatus(String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(CustomerServiceMenuActivity.this, OrderStatusActivity.class);
            intent.putExtra(EMP_ID_EXTRA, employeeId);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToEmailInbox(String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(CustomerServiceMenuActivity.this, CustomerServiceEmployeeEmailListActivity.class);
            intent.putExtra(EMP_ID_EXTRA, employeeId);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDeleteAccountConfirmation() {
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
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