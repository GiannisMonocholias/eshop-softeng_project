package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare.AssignedOrdersToPrepareActivity;
import gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign.AvailableOrdersToAssignActivity;

public class OrderPreparationEmployeeMenuActivity extends AppCompatActivity implements OrdersPreparationEmployeeMenuView{

    OrdersPreparationEmployeeMenuPresenter presenter;
    private static String EMP_ID = "ORDER_PREPARATION_EMPLOYEE_ID";

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

        String employeeId = getIntent().getStringExtra(EMP_ID);


        presenter.onViewCreated(employeeId);





        //Assigned orders to this order preparation employee
        findViewById(R.id.btnOrdPrepEmpAssignedOrders).setOnClickListener(v -> presenter.onClickAssignedOrders(employeeId));


        //Non assigned orders to this order preparation employee
        findViewById(R.id.btnOrdPrepEmpAvailableOrdersToAssign).setOnClickListener(v -> presenter.onClickAvailableOrdersToAssign(employeeId));


        //Account Logout
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


}