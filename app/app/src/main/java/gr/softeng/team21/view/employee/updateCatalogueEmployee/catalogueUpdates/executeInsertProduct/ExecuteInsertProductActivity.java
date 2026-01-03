package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

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

public class ExecuteInsertProductActivity extends AppCompatActivity implements ExecuteInsertProductView {

    private ExecuteInsertProductPresenter presenter;

    private TextInputEditText edtCode, edtName, edtPrice, edtDesc;
    private TextView txtRequestDescription;
    private Button btnConfirm;

    private static final String EMP_ID_EXTRA = "UPDATE_CATALOGUE_EMPLOYEE_ID";
    private static final String REQ_ID_EXTRA = "REQUEST_ID";

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

        presenter = new ExecuteInsertProductPresenter(
                this,
                EmployeeDAOMemory.getInstance(),
                UpdateRequestDAOMemory.getInstance(),
                ProductTypeDAOMemory.getInstance()
        );

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        int requestId = getIntent().getIntExtra(REQ_ID_EXTRA, -1);
        presenter.loadRequestDetails(employeeId, requestId);

        btnConfirm.setOnClickListener(v -> presenter.onConfirmInsert());
    }

    private void initializeViews() {
        txtRequestDescription = findViewById(R.id.txtexecuteInsertProductRequestDescription);

        edtCode = findViewById(R.id.edtexecuteInsertProductDataCodeContainer);
        edtName = findViewById(R.id.edtexecuteInsertProductDataNameContainer);
        edtPrice = findViewById(R.id.edtexecuteInsertProductDataPriceContainer);
        edtDesc = findViewById(R.id.edtexecuteInsertProductDataDescriptionContainer);

        btnConfirm = findViewById(R.id.btnexecuteInsertProductInsertConfirm);
    }


    @Override
    public String getProductCode() {
        return edtCode.getText().toString();
    }

    @Override
    public String getProductName() {
        return edtName.getText().toString();
    }

    @Override
    public String getProductPrice() {
        return edtPrice.getText().toString();
    }

    @Override
    public String getProductDescription() {
        return edtDesc.getText().toString();
    }

    @Override
    public void setRequestDescription(String description) {
        txtRequestDescription.setText(description);
    }

    @Override
    public void showInputError(String field, String message) {
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
    }

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