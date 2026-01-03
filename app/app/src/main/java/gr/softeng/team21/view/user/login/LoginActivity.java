package gr.softeng.team21.view.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.customer.CustomerHomePageActivity;
import gr.softeng.team21.view.customer.register.RegisterActivity;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu.CustomerServiceMenuActivity;
import gr.softeng.team21.view.employee.deliverer.DelivererOrdersListActivity;
import gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu.OrderPreparationEmployeeMenuActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu.UpdateCatalogueEmployeeMenuActivity;
import gr.softeng.team21.view.util.UserType;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MemoryInitializer.prepareData();

        usernameEditText = findViewById(R.id.edtloginUsername);
        passwordEditText = findViewById(R.id.edtloginPassword);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.loginregisterTxtView);


        presenter = new LoginPresenter(this);

        loginButton.setOnClickListener(v -> presenter.onLogin());

        registerTextView.setOnClickListener(v -> presenter.onRegister());
    }

    @Override
    public String getUsername() {
        return usernameEditText.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordEditText.getText().toString();
    }

    @Override
    public TextView getUserNameEdtText(){return usernameEditText;}

    @Override
    public TextView getPasswordEdtText(){return passwordEditText;}


    @Override
    public void showErrorMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateUserToHomePage(UserType userType, User user) {
        Intent intent = null;

        switch (userType){
            case CUSTOMER_SERVICE_EMPLOYEE:
                intent = new Intent(LoginActivity.this, CustomerServiceMenuActivity.class);
                intent.putExtra("CUSTOMER_SERVICE_EMPLOYEE_ID", ((CustomerServiceEmployee)user).getEmployeeId());
                break;
            case DELIVERER:
                intent = new Intent(LoginActivity.this, DelivererOrdersListActivity.class);
                intent.putExtra("DELIVERER_ID", ((Deliverer)user).getEmployeeId());
                break;
            case ORDER_PREPARATION_EMPLOYEE:
                intent = new Intent(LoginActivity.this, OrderPreparationEmployeeMenuActivity.class);
                intent.putExtra("ORDER_PREPARATION_EMPLOYEE_ID", ((OrderPreparationEmployee)user).getEmployeeId());
                break;
            case UPDATE_CATALOGUE_EMPLOYEE:
                intent = new Intent(LoginActivity.this, UpdateCatalogueEmployeeMenuActivity.class);
                intent.putExtra("UPDATE_CATALOGUE_EMPLOYEE_ID", ((UpdateCatalogueEmployee)user).getEmployeeId());
                break;
            case CUSTOMER:
                intent = new Intent(LoginActivity.this, CustomerHomePageActivity.class);
                intent.putExtra("CUSTOMER_ID", ((Customer)user).getCustomer_id());
                break;

        }

        if(intent != null){
            startActivity(intent);
        }
    }

    @Override
    public void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.loginReset();
    }
}