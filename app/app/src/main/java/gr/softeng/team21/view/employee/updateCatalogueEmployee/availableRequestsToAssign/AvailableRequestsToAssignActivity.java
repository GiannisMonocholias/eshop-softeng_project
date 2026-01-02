package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.AllowedRequest;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;
import gr.softeng.team21.view.util.UpdateRequestAdapterTypes;
import gr.softeng.team21.view.util.UpdateRequestsAdapter;

public class AvailableRequestsToAssignActivity extends AppCompatActivity implements AvailableRequestsToAssignView{

    private AvailableRequestsToAssignPresenter presenter;
    UpdateRequestsAdapter adapter;
    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";


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


    @Override
    public void showMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Ενημέρωση")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("OK", null) // Κλείνει απλά το κουτάκι
                .show();
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Σφάλμα")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onRequestAssignedSuccess(CatalogueUpdateRequest request) {
        if (adapter != null) {
            adapter.removeRequest(request);
        }
    }

    @Override
    public void updateList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

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