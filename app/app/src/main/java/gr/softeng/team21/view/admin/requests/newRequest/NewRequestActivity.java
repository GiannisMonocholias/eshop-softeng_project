package gr.softeng.team21.view.admin.requests.newRequest;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.firebasedao.ProductTypeDAOFirebase;
import gr.softeng.team21.firebasedao.UpdateRequestDAOFirebase;
import gr.softeng.team21.view.admin.requests.NewRequestActivityView;

/**
 * Activity presenting a modern form for administrators to submit new catalogue
 * update requests. Uses Material Design components for enhanced UX.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class NewRequestActivity extends AppCompatActivity implements NewRequestActivityView {

    private NewRequestPresenter presenter;
    private AutoCompleteTextView spinnerAction;
    private TextInputEditText txtReqDescription, txtProductName, txtProductId;

    /**
     * Initializes the form fields, sets up the Material Dropdown menu, and injects DAOs.
     * @param savedInstanceState The previously saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        // Dependency Injection with Firebase DAOs
        UpdateRequestDAO updateRequestDAO = new UpdateRequestDAOFirebase();
        ProductTypeDAO productTypeDAO = new ProductTypeDAOFirebase();
        presenter = new NewRequestPresenter(this, updateRequestDAO, productTypeDAO);

        spinnerAction = findViewById(R.id.spinnerAction);
        txtReqDescription = findViewById(R.id.txtRequestDescription);
        txtProductName = findViewById(R.id.txtRequestProductName);
        txtProductId = findViewById(R.id.txtRequestProductId);

        // Setup Dropdown Array
        String[] actions = {"Εισαγωγή", "Διαγραφή", "Τροποποίηση"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, actions);
        spinnerAction.setAdapter(adapter);

        Button btnNewRequest = findViewById(R.id.btnCreateRequest);
        btnNewRequest.setOnClickListener(v -> submitForm());
    }

    /**
     * Collects data from the input fields and triggers the presenter's submission logic.
     */
    private void submitForm() {
        String choice = spinnerAction.getText().toString();
        String description = txtReqDescription.getText() != null ? txtReqDescription.getText().toString() : "";
        String productName = txtProductName.getText() != null ? txtProductName.getText().toString() : "";
        String productId = txtProductId.getText() != null ? txtProductId.getText().toString() : "";

        presenter.createRequest(choice, description, productName, productId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showSuccessAndClose(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            finish();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
}