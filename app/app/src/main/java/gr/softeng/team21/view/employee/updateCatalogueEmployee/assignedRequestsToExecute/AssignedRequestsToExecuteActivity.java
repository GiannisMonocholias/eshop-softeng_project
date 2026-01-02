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

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct.ExecuteDeleteProductActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct.ExecuteInsertProductActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.ExecuteProcessProductActivity;
import gr.softeng.team21.view.util.UpdateRequestAdapterTypes;
import gr.softeng.team21.view.util.UpdateRequestsAdapter;

public class AssignedRequestsToExecuteActivity extends AppCompatActivity implements AssignedRequestsToExecuteView{

    private  AssignedRequestsToExecutePresenter presenter;
    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";
    private static final String REQ_ID_EXTRA = "REQUEST_ID";
    private static final String REQ_DESC = "REQUEST_DESC";
    private static final String PROD_NAME = "PROD_NAME";
    private static final String PROD_CODE = "PROD_CODE";
    private static final String PROD_DESC = "PROD_DESC";
    private static final String PROD_PRICE = "PROD_PRICE";
    private static final String PROD_CURRENCY = "PROD_CURRENCY";



    private RecyclerView recyclerView;


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

        presenter = new AssignedRequestsToExecutePresenter(this, EmployeeDAOMemory.getInstance());

        recyclerView = findViewById(R.id.rvRequestsAssigned);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onResume() {
        super.onResume();

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        ArrayList<CatalogueUpdateRequest> assignedRequestsMap = presenter.loadAssignedRequests(employeeId);

        UpdateRequestsAdapter adapter = new UpdateRequestsAdapter(assignedRequestsMap, UpdateRequestAdapterTypes.EXECUTE_REQUEST, request ->
            presenter.onClickRequest(request)
        );

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void navigateToRequestDetails(String employeeId, CatalogueUpdateRequest request) {
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
}

