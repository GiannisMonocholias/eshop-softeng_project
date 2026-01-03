package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

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
import gr.softeng.team21.view.user.login.LoginActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute.AssignedRequestsToExecuteActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign.AvailableRequestsToAssignActivity;

public class UpdateCatalogueEmployeeMenuActivity extends AppCompatActivity implements UpdateCatalogueEmployeeMenuView {

    private UpdateCatalogueEmployeeMenuPresenter presenter;
    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";
    private String employeeId;

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

        presenter = new UpdateCatalogueEmployeeMenuPresenter(this, EmployeeDAOMemory.getInstance());
        employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        presenter.onViewCreated(employeeId);


        // Assigned Requests Button
        findViewById(R.id.btnUptCatEmpMenuAssignedRequests).setOnClickListener(v ->
                presenter.onClickAssignedRequests(employeeId)
        );

        // Available Requests Button
        findViewById(R.id.btnUptCatEmpMenuAssignNewRequest).setOnClickListener(v ->
                presenter.onClickAvailableRequestsToAssign(employeeId)
        );

        // Delete Account Button
        findViewById(R.id.btnUptCatEmpMenuDeleteAccount).setOnClickListener(v ->
                presenter.onDeleteAccountSelected()
        );

        // Logout Button
        findViewById(R.id.btnUptCatEmpMenuLogout).setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void showEmployeeName(String fullName){
        ((TextView)findViewById(R.id.txtUptCatEmpMenuName)).setText(fullName);
    }

    @Override
    public void navigateToAssignedRequests(String employeeId) {
        Intent intent = new Intent(UpdateCatalogueEmployeeMenuActivity.this, AssignedRequestsToExecuteActivity.class);
        intent.putExtra(EMP_ID_EXTRA, employeeId);
        startActivity(intent);
    }

    @Override
    public void navigateToAvailableRequestsToAssign(String employeeId) {
        Intent intent = new Intent(UpdateCatalogueEmployeeMenuActivity.this, AvailableRequestsToAssignActivity.class);
        intent.putExtra(EMP_ID_EXTRA, employeeId);
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
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}