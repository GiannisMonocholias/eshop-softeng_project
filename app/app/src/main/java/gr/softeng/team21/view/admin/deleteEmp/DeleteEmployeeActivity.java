package gr.softeng.team21.view.admin.deleteEmp;

import android.content.Intent;
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
import gr.softeng.team21.domain.Employee;

/**
 * Activity responsible for deleting an employee from the list.
 */

public class DeleteEmployeeActivity extends AppCompatActivity implements DeleteEmployeeView {

    //DeleteEmployeeViewModel Object that keeps the presenter instance.
    private DeleteEmployeeViewModel viewModel;

    //Presenter object that executes the logical part of the deletion activity.
    private DeleteEmployeePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_employee);

        viewModel = new ViewModelProvider(this).get(DeleteEmployeeViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnSearchEmp = findViewById(R.id.btnSearchEmp);
        btnSearchEmp.setOnClickListener(v -> searchEmp());

    }


    /**
     * Method that finds the requested employee and sends his personal info to the next activity.
     */
    private void searchEmp(){

        EditText edtUserName = findViewById(R.id.txtDeleteEmpUsername);
        String userNameKeyword = edtUserName.getText().toString();

        EditText edtId = findViewById(R.id.txtDeleteEmpId);
        String idKeyword = edtId.getText().toString();

        Employee employee = presenter.searchEmp(userNameKeyword , idKeyword);

        if (employee != null){

            Intent intent = new Intent(DeleteEmployeeActivity.this , EmpInfoActivity.class);
            intent.putExtra("empName" , employee.getFirstname());
            intent.putExtra("empSurname" , employee.getLastname());
            intent.putExtra("empPhone" , employee.getPhonenumber());

            startActivity(intent);
        }

    }

}