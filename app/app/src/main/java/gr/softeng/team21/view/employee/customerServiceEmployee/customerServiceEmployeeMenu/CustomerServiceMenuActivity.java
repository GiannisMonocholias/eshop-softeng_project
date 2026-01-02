package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu;

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
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList.CustomerServiceEmployeeEmailListActivity;
import gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus.OrderStatusActivity;

public class CustomerServiceMenuActivity extends AppCompatActivity implements  CustomerServiceMenuView{
    private CustomerServiceMenuPresenter presenter;
    private static final String  EMP_ID_EXTRA = "CUSTOMER_SERVICE_EMPLOYEE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_service_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityCseDashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new CustomerServiceMenuPresenter(this, EmployeeDAOMemory.getInstance());

        // fetch employee id from the previous activity
        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);

        // the presenter prepares the screen for the specific employee
        presenter.onViewCreated(employeeId);




        //Incoming messages view button
        findViewById(R.id.btnCustomerServiceEmployeeMenuEmailInbox).setOnClickListener(v -> presenter.onInboxSelected(employeeId));

        //Order status notifications button
        findViewById(R.id.btnCustomerServiceEmployeeMenuNotifyOrdersStatus).setOnClickListener(v -> presenter.onOrderStatusSelected(employeeId));

        //Account Logout
        findViewById(R.id.btnCustomerServiceEmployeeMenuLogout).setOnClickListener(v -> {
            finish();
        });


    }


    @Override
    public void showEmployeeName(String fullname) {
        ((TextView) findViewById(R.id.txtCustomerServiceEmployeeMenuName)).setText(fullname);
    }

    @Override
    public void navigateToOrderStatus(String employeeId){
        Intent intent = new Intent(CustomerServiceMenuActivity.this, OrderStatusActivity.class);

        intent.putExtra(EMP_ID_EXTRA, employeeId) ;

        startActivity(intent);
    }

    @Override
    public void navigateToEmailInbox(String employeeId){
        Intent intent = new Intent(CustomerServiceMenuActivity.this, CustomerServiceEmployeeEmailListActivity.class);

        intent.putExtra(EMP_ID_EXTRA, employeeId) ;

        startActivity(intent);
    }
}

