package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.firebasedao.UpdateRequestDAOFirebase;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory; // Placeholder until Firebase equivalent is ready

/**
 * Activity providing the UI for executing a product deletion request.
 * Displays target product information and requires explicit user confirmation
 * before performing the destructive operation.
 * Implements {@link ExecuteDeleteProductView} using safe runOnUiThread
 * and Material Components.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteDeleteProductActivity extends AppCompatActivity implements ExecuteDeleteProductView {

    private ExecuteDeleteProductPresenter presenter;

    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";
    private static final String REQ_ID_EXTRA = "REQUEST_ID";

    /**
     * Initializes the Activity, binds UI components, injects DAOs, and triggers the asynchronous
     * loading of request data via the presenter.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_execute_delete_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.executeProcessProduct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // DEPENDENCY INJECTION: Connect Presenter to DAOs
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UpdateRequestDAO updateRequestDAO = new UpdateRequestDAOFirebase();
        ProductTypeDAO productTypeDAO = ProductTypeDAOMemory.getInstance(); // Or new ProductTypeDAOFirebase() when migrated

        presenter = new ExecuteDeleteProductPresenter(this, employeeDAO, updateRequestDAO, productTypeDAO);

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        int requestId = getIntent().getIntExtra(REQ_ID_EXTRA, -1);

        // Fetch data asynchronously
        presenter.loadRequestDetails(employeeId, requestId);

        findViewById(R.id.btnExecuteDeleteProductConfirmDelete).setOnClickListener(v -> presenter.onDeleteButtonClicked());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProductDetails(String name, String code, String description, String price) {
        runOnUiThread(() -> {
            ((TextView) findViewById(R.id.txtexecuteDeleteProductNameValue)).setText(name);
            ((TextView) findViewById(R.id.txtexecuteDeleteProductCodeValue)).setText(code);
            ((TextView) findViewById(R.id.txtexecuteDeleteProductDescriptionValue)).setText(description);
            ((TextView) findViewById(R.id.txtexecuteDeleteProductPriceValue)).setText(price);
        });
    }

    /**
     * {@inheritDoc}
     * Shows a destructive confirmation {@link MaterialAlertDialogBuilder} to ensure user intent.
     */
    @Override
    public void showConfirmationDialog() {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Επιβεβαίωση Διαγραφής")
                    .setMessage("Είστε σίγουρος ότι θέλετε να διαγράψετε οριστικά αυτό το προϊόν; Η ενέργεια δεν αναιρείται.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("ΝΑΙ", (dialog, which) -> presenter.onDeleteConfirmed())
                    .setNegativeButton("ΟΧΙ", null)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
     * Displays a success dialog using Material Components. Upon dismissal, terminates the activity.
     */
    @Override
    public void showSuccessMessage(String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Επιτυχία")
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Σφάλμα")
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}