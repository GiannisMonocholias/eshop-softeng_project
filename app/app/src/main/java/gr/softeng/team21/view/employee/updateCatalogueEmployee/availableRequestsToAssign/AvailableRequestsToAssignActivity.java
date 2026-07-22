package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import android.content.DialogInterface;
import android.os.Bundle;

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
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.firebasedao.UpdateRequestDAOFirebase;
import gr.softeng.team21.view.util.UpdateRequestAdapterTypes;
import gr.softeng.team21.view.util.UpdateRequestsAdapter;

/**
 * Activity that displays a list of unassigned catalogue update requests.
 * Provides the UI for Catalogue Employees to pick up new tasks.
 * Implements {@link AvailableRequestsToAssignView} and securely updates the interface
 * using runOnUiThread. Integrates Material Components and Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableRequestsToAssignActivity extends AppCompatActivity implements AvailableRequestsToAssignView {

    private AvailableRequestsToAssignPresenter presenter;
    private UpdateRequestsAdapter adapter;
    private RecyclerView recyclerView;

    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";

    /**
     * Initializes the UI, sets up the RecyclerView, injects DAOs into the Presenter,
     * and triggers asynchronous data loading.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_available_requests_to_assign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.executeProcessProduct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rvRequestsAvailableToAssign);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // DEPENDENCY INJECTION: Connect Presenter to Firebase DAOs
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        UpdateRequestDAO updateRequestDAO = new UpdateRequestDAOFirebase();

        presenter = new AvailableRequestsToAssignPresenter(this, employeeDAO, updateRequestDAO);

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);

        // Trigger asynchronous load
        presenter.loadAvailableRequests(employeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAvailableRequestsList(ArrayList<CatalogueUpdateRequest> requests) {
        runOnUiThread(() -> {
            if (requests != null) {
                adapter = new UpdateRequestsAdapter(requests, UpdateRequestAdapterTypes.ASSIGN_REQUEST, request -> {
                    presenter.onRequestClicked(request);
                });
                recyclerView.setAdapter(adapter);
            }
        });
    }

    /**
     * {@inheritDoc}
     * Displays a modern {@link MaterialAlertDialogBuilder}.
     */
    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Ενημέρωση")
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
     * Displays an error {@link MaterialAlertDialogBuilder}.
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
     */
    @Override
    public void onRequestAssignedSuccess(CatalogueUpdateRequest request) {
        runOnUiThread(() -> {
            if (adapter != null) {
                adapter.removeRequest(request);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateList() {
        runOnUiThread(() -> {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * {@inheritDoc}
     * Displays a {@link MaterialAlertDialogBuilder} to confirm the assignment transaction.
     */
    @Override
    public void showConfirmationDialog(CatalogueUpdateRequest request, String confirmationMessage) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Επιβεβαίωση Ανάληψης αιτήματος")
                    .setMessage(confirmationMessage)
                    .setPositiveButton("ΝΑΙ", (dialog, which) -> presenter.onRequestConfirmed(request))
                    .setNegativeButton("ΟΧΙ", null)
                    .show();
        });
    }
}