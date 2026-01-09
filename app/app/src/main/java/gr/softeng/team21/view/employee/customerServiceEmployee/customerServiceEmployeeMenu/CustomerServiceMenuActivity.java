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
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;
import gr.softeng.team21.view.user.login.LoginActivity;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListActivity;
import gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus.OrderStatusActivity;

/**
 * Main menu activity for the Customer Service Employee.
 * Handles UI components, click listeners, and navigation via Android Intents.
 * Implements {@link CustomerServiceMenuView} to update the interface based on Presenter logic.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceMenuActivity extends AppCompatActivity implements CustomerServiceMenuView {

    private CustomerServiceMenuPresenter presenter;
    private static final String EMP_ID_EXTRA = "CUSTOMER_SERVICE_EMPLOYEE_ID";
    private String employeeId;

    /**
     * Initializes the activity layout, instantiates the presenter, and
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

        presenter = new CustomerServiceMenuPresenter(this, EmployeeDAOMemory.getInstance());

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

    /** {@inheritDoc} */
    @Override
    public void showEmployeeName(String fullname) {
        ((TextView) findViewById(R.id.txtCustomerServiceEmployeeMenuName)).setText(fullname);
    }

    /** {@inheritDoc}
     * The employee id is passed to the OrderStatusActivity as Intent extra
     * */
    @Override
    public void navigateToOrderStatus(String employeeId){
        Intent intent = new Intent(CustomerServiceMenuActivity.this, OrderStatusActivity.class);
        intent.putExtra(EMP_ID_EXTRA, employeeId);
        startActivity(intent);
    }

    /** {@inheritDoc}
     * The employee id is passed to the CustomerServiceEmployeeEmailListActivity as Intent extra
     * */
    @Override
    public void navigateToEmailInbox(String employeeId){
        Intent intent = new Intent(CustomerServiceMenuActivity.this, CustomerServiceEmployeeEmailListActivity.class);
        intent.putExtra(EMP_ID_EXTRA, employeeId);
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Builds and displays an {@link AlertDialog} to ensure user intent before account deletion.
     */
    @Override
    public void showDeleteAccountConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Διαγραφή Λογαριασμού")
                .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε τον λογαριασμό σας; Αυτή η ενέργεια δεν μπορεί να αναιρεθεί.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onDeleteAccountConfirmed(employeeId);
                    }
                })
                .setNegativeButton("ΟΧΙ", null)
                .show();
    }

    /**
     * {@inheritDoc}
     * Uses Intent flags to clear the activity stack, ensuring the user cannot
     * return to the menu via the back button after logging out.
     */
    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
        finish();
    }

    /** {@inheritDoc}
     * It navigates user to the UserEditDataActivity via Intent.
     * The user id is passed to the UserEditDataActivity as Intent extra
     * */
    @Override
    public void navigateToProcessAccount(String employeeId){
        Intent intent = new Intent(this, UserEditDataActivity.class);

        intent.putExtra("user_id",employeeId);

        startActivity(intent);
    }

    /** {@inheritDoc} */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}