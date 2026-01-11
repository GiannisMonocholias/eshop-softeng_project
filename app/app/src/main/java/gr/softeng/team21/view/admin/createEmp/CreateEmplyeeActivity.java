package gr.softeng.team21.view.admin.createEmp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.EmailAddress;

/**
 * Activity responsible for creating a new employee.
 *
 */

public class CreateEmplyeeActivity extends AppCompatActivity implements CreateEmplyeeView {

    /**
     * ViewModel that holds and manages the presenter instance
     * across configuration changes.
     */
    private CreateEmployeeViewModel viewModel;

    /**
     * Presenter responsible for handling the business logic
     * related to employee creation.
     */
    private CreateEmployeePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_emplyee);

        viewModel = new ViewModelProvider(this).get(CreateEmployeeViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnSaveData = findViewById(R.id.btnSaveEmp);
        btnSaveData.setOnClickListener(v -> saveData());

    }


    /**
     * Collects input data from the UI fields and
     * delegates the employee creation process to the presenter.
     */
    @Override
    public void saveData(){

        EditText edtUserName = findViewById(R.id.txtAdminUserName);
        String userNameKeyword = edtUserName.getText().toString();

        EditText edtEmail = findViewById(R.id.txtAdminEmail);
        EmailAddress EmailKeyword = new EmailAddress(edtEmail.getText().toString());

        EditText edtFirstName = findViewById(R.id.txtAdminFirstName);
        String firstNameKeyword = edtFirstName.getText().toString();

        EditText edtLastName = findViewById(R.id.txtAdminLastName);
        String lastNameKeyword = edtLastName.getText().toString();

        EditText edtPhone = findViewById(R.id.txtAdminPhone);
        String phoneKeyword = edtPhone.getText().toString();

        EditText edtAddress = findViewById(R.id.txtAdminAddress);
        String addressKeyword = edtAddress.getText().toString();

        EditText edtPassword = findViewById(R.id.txtEmpPassword);
        String passwordKeyword = edtPassword.getText().toString();

        EditText edtId = findViewById(R.id.txtEmpId);
        String idKeyword = edtId.getText().toString();

        EditText edtSalary = findViewById(R.id.txtEmpSalary);
        int salaryKeyword = Integer.parseInt(edtSalary.getText().toString());

        viewModel.getPresenter().saveData(userNameKeyword , EmailKeyword , firstNameKeyword , lastNameKeyword , phoneKeyword , addressKeyword , passwordKeyword , idKeyword , salaryKeyword);

    }
}