package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

import android.content.DialogInterface; // Χρειάζεται για το listener
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog; // <--- ΣΗΜΑΝΤΙΚΟ IMPORT
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Activity providing the UI for executing a product deletion request.
 * Displays target product information and requires explicit user confirmation
 * before performing the destructive operation.
 * Implements {@link ExecuteDeleteProductView}
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteDeleteProductActivity extends AppCompatActivity implements ExecuteDeleteProductView{

    private ExecuteDeleteProductPresenter presenter;

    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";
    private static final String REQ_ID_EXTRA = "REQUEST_ID";


    /**
     * Initializes the Activity, binds UI components, and triggers the loading
     * of request data via the presenter.
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


        presenter = new ExecuteDeleteProductPresenter(this, EmployeeDAOMemory.getInstance(), UpdateRequestDAOMemory.getInstance(), ProductTypeDAOMemory.getInstance());

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        int requestId = getIntent().getIntExtra(REQ_ID_EXTRA, -1);

        presenter.loadRequestDetails(employeeId, requestId);


        findViewById(R.id.btnExecuteDeleteProductConfirmDelete).setOnClickListener(v -> {
            presenter.onDeleteButtonClicked();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProductDetails(String name, String code, String description, String price) {

        ((TextView) findViewById(R.id.txtexecuteDeleteProductNameValue)).setText(name);
        ((TextView) findViewById(R.id.txtexecuteDeleteProductCodeValue)).setText(code);
        ((TextView) findViewById(R.id.txtexecuteDeleteProductDescriptionValue)).setText(description);
        ((TextView) findViewById(R.id.txtexecuteDeleteProductPriceValue)).setText(price);
    }

    /**
     * {@inheritDoc}
     * Shows a destructive confirmation Android {@link AlertDialog} to ensure user intent.
     */
    @Override
    public void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση Διαγραφής")
                .setMessage("Είστε σίγουρος ότι θέλετε να διαγράψετε οριστικά αυτό το προϊόν; Η ενέργεια δεν αναιρείται.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onDeleteConfirmed();
                    }
                })
                .setNegativeButton("ΟΧΙ", null)
                .show();
    }

    /**
     * {@inheritDoc}
     * Displays a success dialog. Upon dismissal, terminates the activity.
     */
    @Override
    public void showSuccessMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Επιτυχία")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Σφάλμα")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", null)
                .show();
    }
}