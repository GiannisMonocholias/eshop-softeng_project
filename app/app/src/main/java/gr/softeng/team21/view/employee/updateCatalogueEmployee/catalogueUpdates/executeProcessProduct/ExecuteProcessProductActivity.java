package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.firebasedao.ProductTypeDAOFirebase;
import gr.softeng.team21.firebasedao.UpdateRequestDAOFirebase;

/**
 * Activity for processing a product modification request.
 * Provides a form-based UI for updating product attributes and implements
 * {@link ExecuteProcessProductView} for communication with the presenter.
 * Secures UI updates via runOnUiThread and uses Material Components.
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
     * Initializes UI components, sets up window insets, injects DAOs, and triggers
     * the asynchronous loading of request details via the presenter.
     * @param savedInstanceState If the activity is being re-initialized.
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

        // DEPENDENCY INJECTION: Connect Presenter to Firebase DAOs
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UpdateRequestDAO updateRequestDAO = new UpdateRequestDAOFirebase();
        ProductTypeDAO productTypeDAO = new ProductTypeDAOFirebase();

        presenter = new ExecuteProcessProductPresenter(this, employeeDAO, updateRequestDAO, productTypeDAO);

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        int requestId = getIntent().getIntExtra(REQ_ID_EXTRA, -1);

        // Fetch data asynchronously
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductCode() {
        return edtCode.getText() != null ? edtCode.getText().toString() : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductName() {
        return edtName.getText() != null ? edtName.getText().toString() : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductPrice() {
        return edtPrice.getText() != null ? edtPrice.getText().toString() : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductDescription() {
        return edtDesc.getText() != null ? edtDesc.getText().toString() : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProductData(String code, String name, String price, String description) {
        runOnUiThread(() -> {
            edtCode.setText(code);
            edtName.setText(name);
            edtPrice.setText(price);
            edtDesc.setText(description);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRequestDescription(String description) {
        runOnUiThread(() -> txtDescription.setText(description));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showInputError(String field, String message) {
        runOnUiThread(() -> {
            switch (field) {
                case "code": edtCode.setError(message); edtCode.requestFocus(); break;
                case "name": edtName.setError(message); edtName.requestFocus(); break;
                case "price": edtPrice.setError(message); edtPrice.requestFocus(); break;
            }
        });
    }

    /**
     * {@inheritDoc}
     * Shows a success {@link MaterialAlertDialogBuilder} and returns the user to the previous screen.
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
     * Displays a confirmation {@link MaterialAlertDialogBuilder} to ensure the user
     * wants to overwrite catalogue data.
     */
    @Override
    public void showConfirmationDialog() {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Επιβεβαίωση Αποθήκευσης")
                    .setMessage("Είστε σίγουροι ότι θέλετε να αποθηκεύσετε τις αλλαγές;")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("ΝΑΙ", (dialog, which) -> presenter.onSaveConfirmed())
                    .setNegativeButton("ΟΧΙ", null)
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