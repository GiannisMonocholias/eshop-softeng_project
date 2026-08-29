package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

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
 * Android Activity providing the UI form for registering a new product into the catalogue.
 * Captures user input via TextInputEditText fields and delegates the persistence and validation logic
 * to the {@link ExecuteInsertProductPresenter}.
 * Implements safe UI updates via runOnUiThread and utilizes Android Material Components.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteInsertProductActivity extends AppCompatActivity implements ExecuteInsertProductView {

    private ExecuteInsertProductPresenter presenter;

    private TextInputEditText edtCode, edtName, edtPrice, edtDesc;
    private TextView txtRequestDescription;
    private Button btnConfirm;

    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";
    private static final String REQ_ID_EXTRA = "REQUEST_ID";

    /**
     * Initializes the Activity, binds XML UI components to local variables, injects DAOs
     * into the presenter, and initiates the asynchronous data loading sequence.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_execute_insert_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.executeInsertProduct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();

        // DEPENDENCY INJECTION: Connect Presenter to Firebase DAOs
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UpdateRequestDAO updateRequestDAO = new UpdateRequestDAOFirebase();
        ProductTypeDAO productTypeDAO = new ProductTypeDAOFirebase();

        presenter = new ExecuteInsertProductPresenter(this, employeeDAO, updateRequestDAO, productTypeDAO);

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        int requestId = getIntent().getIntExtra(REQ_ID_EXTRA, -1);

        // Fetch initialization data asynchronously
        presenter.loadRequestDetails(employeeId, requestId);

        btnConfirm.setOnClickListener(v -> presenter.onConfirmInsert());
    }

    /**
     * Helper method to map XML layout elements to class members.
     */
    private void initializeViews() {
        txtRequestDescription = findViewById(R.id.txtexecuteInsertProductRequestDescription);
        edtCode = findViewById(R.id.edtexecuteInsertProductDataCodeContainer);
        edtName = findViewById(R.id.edtexecuteInsertProductDataNameContainer);
        edtPrice = findViewById(R.id.edtexecuteInsertProductDataPriceContainer);
        edtDesc = findViewById(R.id.edtexecuteInsertProductDataDescriptionContainer);
        btnConfirm = findViewById(R.id.btnexecuteInsertProductInsertConfirm);
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
    public void setRequestDescription(String description) {
        runOnUiThread(() -> txtRequestDescription.setText(description));
    }

    /**
     * {@inheritDoc}
     * Sets an error state on a specific input field and requests focus to guide the user.
     */
    @Override
    public void showInputError(String field, String message) {
        runOnUiThread(() -> {
            switch (field) {
                case "code":
                    edtCode.setError(message);
                    edtCode.requestFocus();
                    break;
                case "name":
                    edtName.setError(message);
                    edtName.requestFocus();
                    break;
                case "price":
                    edtPrice.setError(message);
                    edtPrice.requestFocus();
                    break;
            }
        });
    }

    /**
     * {@inheritDoc}
     * Displays a success dialog using Material Components. Dismissing it terminates the activity.
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
     * Displays a dismissable error dialog using Material Components.
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