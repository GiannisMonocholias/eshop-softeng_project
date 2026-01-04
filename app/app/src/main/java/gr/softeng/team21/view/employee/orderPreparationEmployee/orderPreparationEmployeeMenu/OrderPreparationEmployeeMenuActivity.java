package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

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
import gr.softeng.team21.view.user.login.LoginActivity; // Βεβαιώσου για το σωστό import
import gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare.AssignedOrdersToPrepareActivity;
import gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign.AvailableOrdersToAssignActivity;

public class OrderPreparationEmployeeMenuActivity extends AppCompatActivity implements OrdersPreparationEmployeeMenuView {

    OrdersPreparationEmployeeMenuPresenter presenter;
    private static final String EMP_ID = "ORDER_PREPARATION_EMPLOYEE_ID";
    private String employeeId; // Το αποθηκεύουμε σε πεδίο για να το βλέπουν όλες οι μέθοδοι

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

        presenter = new OrdersPreparationEmployeeMenuPresenter(this, EmployeeDAOMemory.getInstance());

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
        findViewById(R.id.btnCustomerServiceEmployeeMenuLogout).setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void showEmployeeName(String fullName){
        ((TextView)findViewById(R.id.txtCustomerServiceEmployeeMenuName)).setText(fullName);
    }

    @Override
    public void navigateToAssignedOrders(String employeeId) {
        Intent intent = new Intent(OrderPreparationEmployeeMenuActivity.this, AssignedOrdersToPrepareActivity.class);
        intent.putExtra(EMP_ID, employeeId);
        startActivity(intent);
    }

    @Override
    public void navigateToAvailableOrdersToAssign(String employeeId) {
        Intent intent = new Intent(OrderPreparationEmployeeMenuActivity.this, AvailableOrdersToAssignActivity.class);
        intent.putExtra(EMP_ID, employeeId);
        startActivity(intent);
    }


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

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToProcessAccount(String employeeId){
        Intent intent = new Intent(this, UserEditDataActivity.class);

        intent.putExtra("user_id",employeeId);

        startActivity(intent);
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}