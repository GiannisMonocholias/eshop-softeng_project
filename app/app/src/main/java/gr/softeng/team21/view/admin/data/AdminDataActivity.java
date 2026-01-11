package gr.softeng.team21.view.admin.data;

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

/**
 * Activity responsible for displaying and updating
 * the administrator's personal data.
 */

public class AdminDataActivity extends AppCompatActivity implements AdminDataView {

    /**
     * ViewModel that holds the presenter instance.
     */
    private AdminDataViewModel viewModel;

    /**
     * Presenter responsible for handling the business logic
     * related to updating admin's data.
     */
    private AdminDataPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_data);

        viewModel = new ViewModelProvider(this).get(AdminDataViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnSaveData = findViewById(R.id.btnSaveAdminData);
        btnSaveData.setOnClickListener(v -> saveData());
    }

    /**
     * Collects data from the UI input fields
     * and forwards them to the presenter for validation and storage.
     */

    private void saveData(){

        EditText edtUserName = findViewById(R.id.txtAdminUserName);
        String userNameKeyword = edtUserName.getText().toString();

        EditText edtEmail = findViewById(R.id.txtAdminEmail);
        String EmailKeyword = edtEmail.getText().toString();

        EditText edtFirstName = findViewById(R.id.txtAdminFirstName);
        String firstNameKeyword = edtFirstName.getText().toString();

        EditText edtLastName = findViewById(R.id.txtAdminLastName);
        String lastNameKeyword = edtLastName.getText().toString();

        EditText edtPhone = findViewById(R.id.txtAdminPhone);
        String phoneKeyword = edtPhone.getText().toString();

        EditText edtAddress = findViewById(R.id.txtAdminAddress);
        String addressKeyword = edtAddress.getText().toString();

        viewModel.getPresenter().saveData(userNameKeyword , EmailKeyword , firstNameKeyword , lastNameKeyword , phoneKeyword , addressKeyword);
    }


}