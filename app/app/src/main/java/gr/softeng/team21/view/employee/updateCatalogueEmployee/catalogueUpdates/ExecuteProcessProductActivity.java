package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import gr.softeng.team21.R;

public class ExecuteProcessProductActivity extends AppCompatActivity {
    private TextInputEditText edtCode, edtName, edtPrice, edtDesc;
    private TextView txtDescription;
    Button btnSaveChanges;



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

        txtDescription = findViewById(R.id.txtexecuteProcessProductRequestDescription);

        edtCode = findViewById(R.id.edtexecuteProcessProductDataCodeInput);
        edtName = findViewById(R.id.edtexecuteProcessProductDataNameInput);
        edtPrice = findViewById(R.id.edtexecuteProcessProductDataPriceInput);
        edtDesc = findViewById(R.id.edtexecuteProcessProductDataDescriptionInput);

        btnSaveChanges = findViewById(R.id.btnexecuteProcessProductSave);

        String requestDesc = getIntent().getStringExtra("REQUEST_DESC");

        if (requestDesc != null) {
            txtDescription.setText(requestDesc);
        }

        btnSaveChanges.setOnClickListener(v -> {

            String code = edtCode.getText().toString();
            String name = edtName.getText().toString();
            String priceStr = edtPrice.getText().toString();
            String description = edtDesc.getText().toString();


            Toast.makeText(this, "Το προϊόν " +  name + " τροποποιήθηκε", Toast.LENGTH_SHORT).show();
            finish();
        });


    }
}