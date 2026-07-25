package gr.softeng.team21.view.admin.deleteEmp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.view.util.DeleteEmployeeAdapter;

/**
 * Activity presenting a visual list of all active employees.
 * Clicking on an employee routes the admin to the confirmation screen.
 * Implements MVP and uses Firebase DAO via Dependency Injection.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class DeleteEmployeeActivity extends AppCompatActivity implements DeleteEmployeeView {

    private DeleteEmployeePresenter presenter;
    private RecyclerView recyclerView;

    /**
     * Initializes the Activity, sets up the RecyclerView, and injects the Firebase DAO into the presenter.
     * @param savedInstanceState The previously saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_employee);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rvDeleteEmployees);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dependency Injection for Firebase
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        presenter = new DeleteEmployeePresenter(this, employeeDAO);
    }

    /**
     * Refreshes the employee list every time the Activity comes to the foreground.
     */
    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadEmployees();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEmployees(List<Employee> employees) {
        // UI updates must be executed on the Main Thread
        runOnUiThread(() -> {
            DeleteEmployeeAdapter adapter = new DeleteEmployeeAdapter(employees, employee -> {
                Intent intent = new Intent(DeleteEmployeeActivity.this, EmpInfoActivity.class);
                intent.putExtra("EMPLOYEE_USERNAME", employee.getUsername());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        runOnUiThread(() ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        );
    }
}