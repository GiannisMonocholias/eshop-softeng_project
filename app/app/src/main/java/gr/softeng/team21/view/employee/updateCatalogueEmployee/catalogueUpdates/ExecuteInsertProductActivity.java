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

import java.math.BigDecimal;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.domain.ProductType;

public class ExecuteInsertProductActivity extends AppCompatActivity {

    private TextInputEditText edtCode, edtName, edtPrice, edtDesc;
    private TextView txtDescription;
    private Button btnConfirm;

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

        txtDescription = findViewById(R.id.txtexecuteInsertProductRequestDescription);

        edtCode = findViewById(R.id.edtexecuteInsertProductDataCodeContainer);
        edtName = findViewById(R.id.edtexecuteInsertProductDataNameContainer);
        edtPrice = findViewById(R.id.edtexecuteInsertProductDataPriceContainer);
        edtDesc = findViewById(R.id.edtexecuteInsertProductDataDescriptionContainer);

        btnConfirm = findViewById(R.id.btnexecuteInsertProductInsertConfirm);

        // 2. Λήψη Οδηγιών από το Intent
        String requestDesc = getIntent().getStringExtra("REQUEST_DESC");

        if (requestDesc != null) {
            txtDescription.setText(requestDesc);
        }

        // 3. Κλικ στο κουμπί
        btnConfirm.setOnClickListener(v -> {

            // Α. Λήψη των Strings από τα πεδία
            String code = edtCode.getText().toString().trim();
            String name = edtName.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String description = edtDesc.getText().toString().trim();

            // Β. Απευθείας μετατροπή και δημιουργία αντικειμένων
            // (Προσοχή: Αν το priceStr δεν είναι αριθμός, η εφαρμογή θα κρασάρει εδώ)
            BigDecimal amount = new BigDecimal(priceStr);
            Money price = new Money(amount, "€");

            // Γ. Δημιουργία του Προϊόντος
            //ProductType newProduct = new ProductType(name, description, price, code);

            // Δ. Εδώ θα καλέσεις το DAO για αποθήκευση (όταν το φτιάξεις)
            // repo.save(newProduct);

            // Ε. Επιτυχία και Έξοδος
            Toast.makeText(this, "Το προϊόν " + name + " δημιουργήθηκε!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}