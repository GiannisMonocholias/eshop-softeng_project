package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;
import gr.softeng.team21.view.util.UpdateRequestAdapterTypes;
import gr.softeng.team21.view.util.UpdateRequestsAdapter;

/**
 * Activity that displays a list of unassigned catalogue update requests.
 * Provides the UI for Catalogue Employees to pick up new tasks.
 * Implements {@link AvailableRequestsToAssignView}.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableRequestsToAssignActivity extends AppCompatActivity implements AvailableRequestsToAssignView{

    private AvailableRequestsToAssignPresenter presenter;
    UpdateRequestsAdapter adapter;
    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";

    /**
     * Initializes the UI, sets up the RecyclerView, and loads data via the presenter.
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

        presenter = new AvailableRequestsToAssignPresenter(this, EmployeeDAOMemory.getInstance(), UpdateRequestDAOMemory.getInstance());


        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        ArrayList<CatalogueUpdateRequest> availableRequests = presenter.loadAvailableRequests(employeeId);

        RecyclerView recyclerView = findViewById(R.id.rvRequestsAvailableToAssign);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new UpdateRequestsAdapter(availableRequests, UpdateRequestAdapterTypes.ASSIGN_REQUEST, request -> {
            presenter.onRequestClicked(request);
        });

        recyclerView.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Ενημέρωση")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("OK", null) // Κλείνει απλά το κουτάκι
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

    /**
     * {@inheritDoc}
     * Visually removes the request from the list after successful assignment.
     */
    @Override
    public void onRequestAssignedSuccess(CatalogueUpdateRequest request) {
        if (adapter != null) {
            adapter.removeRequest(request);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * {@inheritDoc}
     * Displays an Android {@link AlertDialog} to confirm the assignment transaction.
     */
    @Override
    public void showConfirmationDialog(CatalogueUpdateRequest request, String confirmationMessage) {
        new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση Ανάληψης αιτήματος")
                .setMessage(confirmationMessage)
                .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onRequestConfirmed(request);
                    }
                })
                .setNegativeButton("ΟΧΙ", null)
                .show();
    }

}