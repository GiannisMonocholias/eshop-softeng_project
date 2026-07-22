package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

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
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;
import gr.softeng.team21.view.user.login.LoginActivity;
import gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare.AssignedOrdersToPrepareActivity;
import gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign.AvailableOrdersToAssignActivity;

/**
 * Android Activity serving as the dashboard for Order Preparation Employee.
 * Manages UI components and implements the {@link OrdersPreparationEmployeeMenuView} interface
 * to handle navigation and user feedback via runOnUiThread.
 * Uses Material Design components and incorporates Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationEmployeeMenuActivity extends AppCompatActivity implements OrdersPreparationEmployeeMenuView {

    private OrdersPreparationEmployeeMenuPresenter presenter;
    private static final String EMP_ID = "ORDER_PREPARATION_EMPLOYEE_ID";
    private String employeeId;

    /**
     * Sets up the activity layout, injects DAOs into the presenter, and
     * attaches click listeners to all menu buttons.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_preparation_employee_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.OrdPrepEmpMenu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // DEPENDENCY INJECTION
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UserCredentialsDAO userCredentialsDAO = UserCredentialsDAOMemory.getInstance(); // Replace with Firebase equivalent later

        presenter = new OrdersPreparationEmployeeMenuPresenter(this, employeeDAO, userCredentialsDAO);

        employeeId = getIntent().getStringExtra(EMP_ID);

        presenter.onViewCreated(employeeId);

        // Listeners
        findViewById(R.id.btnOrdPrepEmpAssignedOrders).setOnClickListener(v ->
                presenter.onClickAssignedOrders(employeeId)
        );

        findViewById(R.id.btnOrdPrepEmpAvailableOrdersToAssign).setOnClickListener(v ->
                presenter.onClickAvailableOrdersToAssign(employeeId)
        );

        findViewById(R.id.btnOrdPrepEmpProcessAccount).setOnClickListener(v ->
                presenter.onProcessAccountSelected(employeeId)
        );

        findViewById(R.id.btnOrdPrepEmpDeleteAccount).setOnClickListener(v ->
                presenter.onDeleteAccountSelected()
        );

        // Logout
        findViewById(R.id.btnCustomerServiceEmployeeMenuLogout).setOnClickListener(v -> finish());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEmployeeName(String fullName) {
        runOnUiThread(() -> {
            ((TextView) findViewById(R.id.txtCustomerServiceEmployeeMenuName)).setText(fullName);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToAssignedOrders(String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(OrderPreparationEmployeeMenuActivity.this, AssignedOrdersToPrepareActivity.class);
            intent.putExtra(EMP_ID, employeeId);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToAvailableOrdersToAssign(String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(OrderPreparationEmployeeMenuActivity.this, AvailableOrdersToAssignActivity.class);
            intent.putExtra(EMP_ID, employeeId);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     * Displays a modernized {@link MaterialAlertDialogBuilder} to confirm the
     * action of deleting an account.
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