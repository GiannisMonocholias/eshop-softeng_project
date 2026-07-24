package gr.softeng.team21.view.admin.adminMenu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.view.admin.changeQuantity.ChangeQuantityProductsActivity;
import gr.softeng.team21.view.admin.createEmp.selectEmployeeType.SelectEmployeeTypeActivity;
import gr.softeng.team21.view.admin.data.AdminDataActivity;
import gr.softeng.team21.view.admin.deleteEmp.DeleteEmployeeActivity;
import gr.softeng.team21.view.admin.requests.RequestsActivity;

/**
 * Home screen of the admin that shows the menu of the functions they can execute.
 * Implements {@link AdminMenuView} and routes intents using the {@link AdminMenuPresenter}.
 * UI changes are secured with runOnUiThread.
 * @author Αλέξανρδος Δρακάκης
 */
public class AdminMenuActivity extends AppCompatActivity implements AdminMenuView {

    private AdminMenuPresenter presenter;

    /**
     * Initializes the layout, configures edge-to-edge display, and sets up
     * the click listeners for the dashboard buttons.
     * @param savedInstanceState If the activity is being re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_admin_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new AdminMenuPresenter(this);

        Button btnEditData = findViewById(R.id.btnEditData);
        Button btnRequests = findViewById(R.id.btnRequests);
        Button btnCreateEmp = findViewById(R.id.btnCreateEmp);
        Button btnDeleteEmp = findViewById(R.id.btnDeleteEmp);
        Button btnQuantities = findViewById(R.id.btnQuantities);

        btnEditData.setOnClickListener(v -> presenter.onEditDataClicked());
        btnRequests.setOnClickListener(v -> presenter.onRequestsClicked());
        btnCreateEmp.setOnClickListener(v -> presenter.onCreateEmployeeClicked());
        btnDeleteEmp.setOnClickListener(v -> presenter.onDeleteEmployeeClicked());
        btnQuantities.setOnClickListener(v -> presenter.onChangeQuantitiesClicked());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToEditData() {
        runOnUiThread(() -> {
            Intent intent = new Intent(AdminMenuActivity.this, AdminDataActivity.class);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToRequests() {
        runOnUiThread(() -> {
            Intent intent = new Intent(AdminMenuActivity.this, RequestsActivity.class);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToCreateEmployee() {
        runOnUiThread(() -> {
            Intent intent = new Intent(AdminMenuActivity.this, SelectEmployeeTypeActivity.class);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToDeleteEmployee() {
        runOnUiThread(() -> {
            Intent intent = new Intent(AdminMenuActivity.this, DeleteEmployeeActivity.class);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToChangeQuantities() {
        runOnUiThread(() -> {
            Intent intent = new Intent(AdminMenuActivity.this, ChangeQuantityProductsActivity.class);
            startActivity(intent);
        });
    }
}