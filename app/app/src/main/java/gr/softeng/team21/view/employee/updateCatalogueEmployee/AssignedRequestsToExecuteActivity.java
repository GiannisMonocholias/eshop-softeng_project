package gr.softeng.team21.view.employee.updateCatalogueEmployee;

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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.AllowedRequest;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.ExecuteDeleteProductActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.ExecuteInsertProductActivity;
import gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.ExecuteProcessProductActivity;
import gr.softeng.team21.view.util.UpdateRequestAdapterTypes;
import gr.softeng.team21.view.util.UpdateRequestsAdapter;

public class AssignedRequestsToExecuteActivity extends AppCompatActivity {

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

        // --- 1. Αρχικοποίηση RecyclerView ---
        RecyclerView recyclerView = findViewById(R.id.rvRequestsAssigned);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- 2. Δημιουργία Dummy Δεδομένων ---
        List<CatalogueUpdateRequest> dummyRequests = new ArrayList<>();
        Date today = new Date();

        // --- ΠΡΟΪΟΝΤΑ ---
        ProductType laptop = new ProductType(
                "Dell XPS 15",
                "Laptop High Performance",
                new Money(new BigDecimal(1200), "€"),
                "DELL-001"
        );

        ProductType mouse = new ProductType(
                "Logitech Mouse",
                "Wireless Mouse",
                new Money(new BigDecimal(25), "€"),
                "LOGI-500"
        );

        // --- ΑΙΤΗΜΑΤΑ ---

        // Αίτημα 1: Αλλαγή Τιμής (PROCESS)
        CatalogueUpdateRequest req1 = new CatalogueUpdateRequest(
                today,
                "Αύξηση τιμής από 400€ σε 500€ λόγω νέας παραλαβής.",
                laptop,
                AllowedRequest.PROCESS_PRODUCT,
                1234
        );
        dummyRequests.add(req1);

        // Αίτημα 2: Διαγραφή (DELETE)
        CatalogueUpdateRequest req2 = new CatalogueUpdateRequest(
                today,
                "Να διαγραφεί το προϊόν από τον κατάλογο λόγω κατάργησης.",
                mouse,
                AllowedRequest.DELETE_PRODUCT,
                3424
        );
        dummyRequests.add(req2);

        // Αίτημα 3: Εισαγωγή (INSERT) - NEW!
        // ΠΡΟΣΟΧΗ: Εδώ βάζουμε null στο προϊόν, γιατί δεν υπάρχει ακόμα!
        CatalogueUpdateRequest req3 = new CatalogueUpdateRequest(
                today,
                "Παρακαλώ προσθέστε το νέο Smartwatch Samsung Galaxy Watch 6 με κωδικό SM-500 και τιμή 299€.",
                null, // null ProductType
                AllowedRequest.INSERT_PRODUCT,
                5566
        );
        dummyRequests.add(req3);

        CatalogueUpdateRequest req4 = new CatalogueUpdateRequest(
                today,
                "Παρακαλώ προσθέστε το νέο Smartwatch Samsung Galaxy Watch 6 με κωδικό SM-500 και τιμή 299€.",
                null, // null ProductType
                AllowedRequest.INSERT_PRODUCT,
                5567
        );
        dummyRequests.add(req4);

        CatalogueUpdateRequest req5 = new CatalogueUpdateRequest(
                today,
                "Παρακαλώ προσθέστε το νέο Smartwatch Samsung Galaxy Watch 6 με κωδικό SM-500 και τιμή 299€.",
                null, // null ProductType
                AllowedRequest.INSERT_PRODUCT,
                5568
        );
        dummyRequests.add(req5);

        // --- 3. Ρύθμιση του Adapter ---
        UpdateRequestsAdapter adapter = new UpdateRequestsAdapter(dummyRequests, UpdateRequestAdapterTypes.EXECUTE_REQUEST , request -> {

            Toast.makeText(AssignedRequestsToExecuteActivity.this,
                    "Επιλέξατε το αίτημα #" + request.getId(),
                    Toast.LENGTH_SHORT).show();

            Intent intent = null;

            switch (request.getType()) {
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
                // 1. Περνάμε τα βασικά του Αιτήματος
                intent.putExtra("REQUEST_ID", request.getId());
                intent.putExtra("REQUEST_DESC", request.getUpdateDescription());

                // 2. Περνάμε τα στοιχεία του Προϊόντος (ΑΝ ΥΠΑΡΧΟΥΝ)
                // Στο INSERT το getProduct() θα είναι null, οπότε αυτό το if θα παραλειφθεί σωστά.
                if (request.getProduct() != null) {

                    intent.putExtra("PROD_NAME", request.getProduct().getProductname());
                    intent.putExtra("PROD_CODE", request.getProduct().getProductCode());
                    intent.putExtra("PROD_DESC", request.getProduct().getDescription());

                    if (request.getProduct().getPrice() != null) {
                        intent.putExtra("PROD_PRICE", request.getProduct().getPrice().getAmount().doubleValue());
                        intent.putExtra("PROD_CURRENCY", request.getProduct().getPrice().getCurrency());
                    }
                }

                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Άγνωστος τύπος αιτήματος", Toast.LENGTH_SHORT).show();
            }

        });

        recyclerView.setAdapter(adapter);
    }
}