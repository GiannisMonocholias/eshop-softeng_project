package gr.softeng.team21.view.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.domain.AuthenticationSystem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.domain.User;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.view.admin.AdminPanelActivity;
import gr.softeng.team21.view.customer.homePage.CustomerHomePageActivity;
import gr.softeng.team21.view.customer.register.RegisterActivity;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu.CustomerServiceMenuActivity;
import gr.softeng.team21.view.employee.deliverer.delivererMenu.DelivererMenuActivity;
import gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu.OrderPreparationEmployeeMenuActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu.UpdateCatalogueEmployeeMenuActivity;
import gr.softeng.team21.view.util.UserType;

/**
 * Activity providing the primary Login UI.
 * Implements {@link LoginView} and routes users to role-specific activities
 * based on their authentication status. Incorporates Dependency Injection
 * and secures UI updates with runOnUiThread.
 * @author Γιάννης Μονοχολιάς
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private LoginPresenter presenter;

    /**
     * Initializes the layout, binds UI components, injects dependencies,
     * and connects the presenter.
     * @param savedInstanceState If the activity is being re-initialized.
     */
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

        usernameEditText = findViewById(R.id.edtloginUsername);
        passwordEditText = findViewById(R.id.edtloginPassword);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.loginregisterTxtView);

        // DEPENDENCY INJECTION
        UserCredentialsDAO credentialsDAO = UserCredentialsDAOMemory.getInstance(); // Will be UserCredentialsDAOFirebase() later
        AuthenticationSystem authSystem = new AuthenticationSystem(credentialsDAO);

        presenter = new LoginPresenter(this, authSystem);

        loginButton.setOnClickListener(v -> presenter.onLogin());
        registerTextView.setOnClickListener(v -> presenter.onRegister());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return usernameEditText.getText().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return passwordEditText.getText().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showErrorMessage(String title, String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showSuccessMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    /**
     * {@inheritDoc}
     * Factory-style navigation logic that launches the appropriate dashboard
     * and passes the correct unique ID extra (Customer ID or Employee ID).
     */
    @Override
    public void navigateUserToHomePage(UserType userType, User user) {
        runOnUiThread(() -> {
            Intent intent = null;

            switch (userType){
                case CUSTOMER_SERVICE_EMPLOYEE:
                    intent = new Intent(LoginActivity.this, CustomerServiceMenuActivity.class);
                    intent.putExtra("CUSTOMER_SERVICE_EMPLOYEE_ID", ((CustomerServiceEmployee)user).getEmployeeId());
                    break;
                case DELIVERER:
                    intent = new Intent(LoginActivity.this, DelivererMenuActivity.class);
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
                case ADMIN:
                    intent = new Intent(LoginActivity.this, AdminPanelActivity.class);
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToRegister() {
        runOnUiThread(() -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Standard Android lifecycle method. Resets the login fields
     * every time the user navigates back to the login screen.
     */
    @Override
    public void onResume(){
        super.onResume();
        presenter.loginReset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetFields() {
        runOnUiThread(() -> {
            usernameEditText.setText("");
            passwordEditText.setText("");
        });
    }
}