package gr.softeng.team21.view.admin.createEmp.selectEmployeeType;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.view.admin.createEmp.employeeRegistration.EmployeeRegistrationActivity;

/**
 * Activity presenting a grid layout menu for the Administrator to select
 * the type of new employee to register. It fetches and displays the
 * current count of active employees for each category asynchronously.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class SelectEmployeeTypeActivity extends AppCompatActivity implements SelectEmployeeTypeView {

    private SelectEmployeeTypePresenter presenter;

    private TextView txtCountCS, txtCountPrep, txtCountCat, txtCountDel;
    private MaterialCardView cardCS, cardPrep, cardCat, cardDel;

    /**
     * Prepares the Android layout, binds views, injects the DAO, and initiates
     * the asynchronous loading of employee statistics.
     * @param savedInstanceState Application state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_employee_type);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtSelectTypeHeader), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();


        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        presenter = new SelectEmployeeTypePresenter(this, employeeDAO);

        cardCS.setOnClickListener(v -> presenter.onTypeSelected("CUSTOMER_SERVICE"));
        cardPrep.setOnClickListener(v -> presenter.onTypeSelected("ORDER_PREPARATION"));
        cardCat.setOnClickListener(v -> presenter.onTypeSelected("UPDATE_CATALOGUE"));
        cardDel.setOnClickListener(v -> presenter.onTypeSelected("DELIVERER"));
    }

    /**
     * Standard lifecycle method. Reloads counts every time the user returns
     * from the registration form to ensure statistics are up to date.
     */
    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadEmployeeCounts();
    }

    /** Binds the XML components to Java variables. */
    private void initializeViews() {
        cardCS = findViewById(R.id.cardCustomerService);
        cardPrep = findViewById(R.id.cardOrderPrep);
        cardCat = findViewById(R.id.cardCatalogue);
        cardDel = findViewById(R.id.cardDeliverer);

        txtCountCS = findViewById(R.id.txtCountCS);
        txtCountPrep = findViewById(R.id.txtCountPrep);
        txtCountCat = findViewById(R.id.txtCountCat);
        txtCountDel = findViewById(R.id.txtCountDel);
    }

    /** {@inheritDoc} */
    @Override
    public void showEmployeeCounts(int customerServiceCount, int orderPrepCount, int updateCatCount, int delivererCount) {
        runOnUiThread(() -> {
            txtCountCS.setText("Ενεργοί: " + customerServiceCount);
            txtCountPrep.setText("Ενεργοί: " + orderPrepCount);
            txtCountCat.setText("Ενεργοί: " + updateCatCount);
            txtCountDel.setText("Ενεργοί: " + delivererCount);
        });
    }

    /** {@inheritDoc} */
    @Override
    public void showErrorMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    /** {@inheritDoc} */
    @Override
    public void navigateToRegistrationForm(String type) {
        runOnUiThread(() -> {
            Intent intent = new Intent(SelectEmployeeTypeActivity.this, EmployeeRegistrationActivity.class);
            intent.putExtra("EMPLOYEE_TYPE", type);
            startActivity(intent);
        });
    }
}