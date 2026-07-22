package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct.ExecuteDeleteProductActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct.ExecuteInsertProductActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct.ExecuteProcessProductActivity;
import gr.softeng.team21.view.util.UpdateRequestAdapterTypes;
import gr.softeng.team21.view.util.UpdateRequestsAdapter;

/**
 * Activity that displays the list of catalogue update tasks assigned to the employee.
 * It functions as a router, directing the user to ExecuteInsert, ExecuteDelete,
 * or ExecuteProcess activities. Implements {@link AssignedRequestsToExecuteView}, utilizes
 * runOnUiThread for safe asynchronous UI updates, and implements Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedRequestsToExecuteActivity extends AppCompatActivity implements AssignedRequestsToExecuteView{

    private AssignedRequestsToExecutePresenter presenter;
    private RecyclerView recyclerView;

    // Intent Extras Constants
    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";
    private static final String REQ_ID_EXTRA = "REQUEST_ID";
    private static final String REQ_DESC = "REQUEST_DESC";
    private static final String PROD_NAME = "PROD_NAME";
    private static final String PROD_CODE = "PROD_CODE";
    private static final String PROD_DESC = "PROD_DESC";
    private static final String PROD_PRICE = "PROD_PRICE";
    private static final String PROD_CURRENCY = "PROD_CURRENCY";

    /**
     * Configures layout, binds the RecyclerView, and initializes the presenter with Firebase DAO.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assigned_requests_to_execute);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.executeProcessProduct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // DEPENDENCY INJECTION
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        presenter = new AssignedRequestsToExecutePresenter(this, employeeDAO);

        recyclerView = findViewById(R.id.rvRequestsAssigned);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Refreshes the assigned requests list asynchronously whenever the user returns to this screen.
     */
    @Override
    public void onResume() {
        super.onResume();
        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        presenter.loadAssignedRequests(employeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAssignedRequestsList(ArrayList<CatalogueUpdateRequest> requests) {
        runOnUiThread(() -> {
            if (requests != null) {
                UpdateRequestsAdapter adapter = new UpdateRequestsAdapter(requests, UpdateRequestAdapterTypes.EXECUTE_REQUEST, request ->
                        presenter.onClickRequest(request)
                );
                recyclerView.setAdapter(adapter);
            }
        });
    }

    /**
     * {@inheritDoc}
     * Displays a modern {@link MaterialAlertDialogBuilder} for error handling.
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

    /**
     * {@inheritDoc}
     * Evaluates the request type and prepares the corresponding Intent with
     * product metadata passed as extras securely on the UI thread.
     */
    @Override
    public void navigateToRequestDetails(String employeeId, CatalogueUpdateRequest request) {
        runOnUiThread(() -> {
            Toast.makeText(AssignedRequestsToExecuteActivity.this,
                    "Επιλέξατε το αίτημα #" + request.getId(),
                    Toast.LENGTH_SHORT).show();

            Intent intent = null;

            switch(request.getType()){
                case INSERT_PRODUCT:
                    intent = new Intent(AssignedRequestsToExecuteActivity.this, ExecuteInsertProductActivity.class);
                    break;
                case PROCESS_PRODUCT:
                    intent = new Intent(AssignedRequestsToExecuteActivity.this, ExecuteProcessProductActivity.class);
                    break;
                case DELETE_PRODUCT:
                    intent = new Intent(AssignedRequestsToExecuteActivity.this, ExecuteDeleteProductActivity.class);
                    break;
            }

            if (intent != null) {
                intent.putExtra(EMP_ID_EXTRA, employeeId);
                intent.putExtra(REQ_ID_EXTRA, request.getId());
                intent.putExtra(REQ_DESC, request.getUpdateDescription());

                if (request.getProduct() != null) {
                    intent.putExtra(PROD_NAME, request.getProduct().getProductname());
                    intent.putExtra(PROD_CODE, request.getProduct().getProductCode());
                    intent.putExtra(PROD_DESC, request.getProduct().getDescription());

                    if (request.getProduct().getPrice() != null) {
                        intent.putExtra(PROD_PRICE, request.getProduct().getPrice().getAmount().doubleValue());
                        intent.putExtra(PROD_CURRENCY, request.getProduct().getPrice().getCurrency());
                    }
                }
                startActivity(intent);
            }
        });
    }
}