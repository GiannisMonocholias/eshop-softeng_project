package gr.softeng.team21.view.employee.updateCatalogueEmployee;

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
import gr.softeng.team21.view.util.UpdateRequestAdapterTypes;
import gr.softeng.team21.view.util.UpdateRequestsAdapter;

public class AvailableRequestsToAssignActivity extends AppCompatActivity {

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

// --- 1. Αρχικοποίηση RecyclerView ---
        RecyclerView recyclerView = findViewById(R.id.rvRequestsAvailableToAssign);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- 2. Δημιουργία Dummy Δεδομένων ---
        List<CatalogueUpdateRequest> dummyRequests = new ArrayList<>();
        Date today = new Date(); // Υποθέτουμε ότι η κλάση Date έχει κενό constructor για τη σημερινή

        // Προϊόν 1
        ProductType laptop = new ProductType(
                "Dell XPS 15",
                "Laptop High Performance",
                new Money(new BigDecimal(1200), "€"),
                "DELL-001"
        );

        // Προϊόν 2
        ProductType mouse = new ProductType(
                "Logitech Mouse",
                "Wireless Mouse",
                new Money(new BigDecimal(25), "€"),
                "LOGI-500"
        );

        // Αίτημα 1: Αλλαγή Τιμής
        // Υποθέτω τον constructor: requestId, date, type, product, description
        // Προσαρμόστε τη σειρά των ορισμάτων αν είναι διαφορετική στον constructor σας
        CatalogueUpdateRequest req1 = new CatalogueUpdateRequest(today,"Αύξηση τιμής από 400€ σε 500€ λόγω νέας παραλαβής από προμηθευτή.",
                laptop, AllowedRequest.PROCESS_PRODUCT, 1234);
        dummyRequests.add(req1);

        // Αίτημα 2: Αλλαγή Διαθεσιμότητας/Περιγραφής
        CatalogueUpdateRequest req2 = new CatalogueUpdateRequest(today,"Διόρθωση ορθογραφικού λάθους στην περιγραφή.",
                mouse, AllowedRequest.PROCESS_PRODUCT,3424
        );
        dummyRequests.add(req2);


        // --- 3. Ρύθμιση του Adapter ---
        // Χρησιμοποιούμε τον Adapter που φτιάξαμε μόνο για Assigned Requests (χωρίς enum τύπου στον constructor)
        UpdateRequestsAdapter adapter = new UpdateRequestsAdapter(dummyRequests, UpdateRequestAdapterTypes.ASSIGN_REQUEST , request -> {

            Toast.makeText(AvailableRequestsToAssignActivity.this,
                    "Επιλέξατε το αίτημα #" + request.getId(),
                    Toast.LENGTH_SHORT).show();

        });

        recyclerView.setAdapter(adapter);

    }
}