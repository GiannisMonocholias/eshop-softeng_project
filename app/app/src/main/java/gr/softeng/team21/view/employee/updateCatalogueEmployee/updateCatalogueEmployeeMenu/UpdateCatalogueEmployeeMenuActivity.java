package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute.AssignedRequestsToExecuteActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign.AvailableRequestsToAssignActivity;

public class UpdateCatalogueEmployeeMenuActivity extends AppCompatActivity implements UpdateCatalogueEmployeeMenuView{

    private UpdateCatalogueEmployeeMenuPresenter presenter;
    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";

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

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        presenter.onViewCreated(employeeId);


        //Assigned update catalogue requests to this update catalogue employee
        findViewById(R.id.btnUptCatEmpMenuAssignedRequests).setOnClickListener( v ->
                presenter.onClickAssignedRequests(employeeId)
        );

        //Non assigned orders to this update catalogue employee
        findViewById(R.id.btnUptCatEmpMenuAssignNewRequest).setOnClickListener(v ->
                presenter.onClickAvailableRequestsToAssign(employeeId)
        );

        //Account Logout
        findViewById(R.id.btnUptCatEmpMenuLogout).setOnClickListener(v ->{
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

}