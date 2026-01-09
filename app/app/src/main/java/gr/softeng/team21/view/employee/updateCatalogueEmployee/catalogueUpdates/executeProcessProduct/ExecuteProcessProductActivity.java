package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import gr.softeng.team21.R;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Activity for processing a product modification request.
 * Provides a form-based UI for updating product attributes and implements
 * {@link ExecuteProcessProductView} for communication with the presenter.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteProcessProductActivity extends AppCompatActivity implements ExecuteProcessProductView {

    private ExecuteProcessProductPresenter presenter;

    // UI Components
    private TextInputEditText edtCode, edtName, edtPrice, edtDesc;
    private TextView txtDescription;
    private Button btnSaveChanges;

    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";
    private static final String REQ_ID_EXTRA = "REQUEST_ID";

    /**
     * Initializes UI components, sets up window insets, and triggers
     * the loading of request details via the presenter.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_execute_process_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.executeProcessProduct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();

        presenter = new ExecuteProcessProductPresenter(this, EmployeeDAOMemory.getInstance(), UpdateRequestDAOMemory.getInstance(), ProductTypeDAOMemory.getInstance());

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        int requestId = getIntent().getIntExtra(REQ_ID_EXTRA, -1);

        presenter.loadRequestDetails(employeeId, requestId);

        btnSaveChanges.setOnClickListener(v -> presenter.onSaveClicked());
    }

    /**
     * Binds class variables to XML layout IDs.
     */
    private void initializeViews() {
        txtDescription = findViewById(R.id.txtexecuteProcessProductRequestDescription);

        edtCode = findViewById(R.id.edtexecuteProcessProductDataCodeInput);
        edtName = findViewById(R.id.edtexecuteProcessProductDataNameInput);
        edtPrice = findViewById(R.id.edtexecuteProcessProductDataPriceInput);
        edtDesc = findViewById(R.id.edtexecuteProcessProductDataDescriptionInput);

        btnSaveChanges = findViewById(R.id.btnexecuteProcessProductSave);
    }

    // Input retrieval methods

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductCode() {
        return edtCode.getText().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductName() {
        return edtName.getText().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductPrice() {
        return edtPrice.getText().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductDescription() {
        return edtDesc.getText().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProductData(String code, String name, String price, String description) {
        edtCode.setText(code);
        edtName.setText(name);
        edtPrice.setText(price);
        edtDesc.setText(description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRequestDescription(String description) {
        txtDescription.setText(description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showInputError(String field, String message) {
        switch (field) {
            case "code": edtCode.setError(message); edtCode.requestFocus(); break;
            case "name": edtName.setError(message); edtName.requestFocus(); break;
            case "price": edtPrice.setError(message); edtPrice.requestFocus(); break;
        }
    }

    /**
     * {@inheritDoc}
     * Shows a success Android dialog and returns the user to the previous screen.
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
     * Displays a confirmation Android {@link AlertDialog} to ensure the user
     * wants to overwrite catalogue data.
     */
    @Override
    public void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση Αποθήκευσης")
                .setMessage("Είστε σίγουροι ότι θέλετε να αποθηκεύσετε τις αλλαγές;")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onSaveConfirmed();
                    }
                })
                .setNegativeButton("ΟΧΙ", null)
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