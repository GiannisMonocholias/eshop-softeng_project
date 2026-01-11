package gr.softeng.team21.view.admin.deleteEmp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;

/**
 * This Activity shows personal information of the employee and from here the admin
 * submits the final deletion.
 */

public class EmpInfo extends AppCompatActivity implements EmpInfoView {

    //Presenter is responsible for the logical part of deleting an employee.
    private EmpInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emp_info);

        presenter = new EmpInfoPresenter();

        Intent intent = getIntent();

        String empName = intent.getStringExtra("empName");
        String empSurname = intent.getStringExtra("empSurname");
        String empPhone = intent.getStringExtra("empPhone");

        TextView txtFname = findViewById(R.id.txtDeleteFname);
        TextView txtLname = findViewById(R.id.txtDeleteLname);
        TextView txtPhone = findViewById(R.id.txtDeletePhone);

        txtFname.setText(empName);
        txtLname.setText(empSurname);
        txtPhone.setText(empPhone);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnDeleteEmp2 = findViewById(R.id.btnDeleteEmp2);
        btnDeleteEmp2.setOnClickListener(v -> deleteEmp());



    }

    /**
     * Here the admin checks the information of the employee that he wants to delete.
     * If they are correct he deletes his profile.
     */

    public void deleteEmp() {

        TextView edtFirstName = findViewById(R.id.txtDeleteFname);
        String FnameKeyword = edtFirstName.getText().toString();

        TextView edtLastName = findViewById(R.id.txtDeleteLname);
        String LnameKeyword = edtLastName.getText().toString();

        TextView edtPhone = findViewById(R.id.txtDeletePhone);
        String PhoneKeyword = edtPhone.getText().toString();

        presenter.deleteEmp(FnameKeyword, LnameKeyword, PhoneKeyword);
    }

}