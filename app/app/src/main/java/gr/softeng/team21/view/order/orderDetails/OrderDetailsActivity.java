package gr.softeng.team21.view.order.orderDetails;

import android.os.Bundle;
import android.widget.TextView;

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
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.view.util.StockProductAdapter;

public class OrderDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_preparation_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.order_preparation_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


// --- 1. ΑΡΧΙΚΟΠΟΙΗΣΗ UI ΣΤΟΙΧΕΙΩΝ (TEXTVIEWS) ---
        // Βεβαιώσου ότι έχεις αυτά τα IDs στο XML σου (activity_order_details.xml)
        // Αν δεν τα έχεις βάλει ακόμα, σχολίασε αυτές τις γραμμές για να μην κρασάρει.
        TextView txtOrderCode = findViewById(R.id.txtOrderDetailsOrderIdValue);
        TextView txtCustomerName = findViewById(R.id.txtOrderDetailsOrderCustomerNameValue);
        TextView txtOrderDate = findViewById(R.id.txtOrderDetailsOrderSubmissionDateValue);
        TextView txtOrderTotal = findViewById(R.id.txtOrderDetailsOrderPriceValue);

        // --- 2. ΛΗΨΗ ΒΑΣΙΚΩΝ ΔΕΔΟΜΕΝΩΝ ΑΠΟ INTENT ---
        String code = getIntent().getStringExtra("ORDER_CODE");
        String name = getIntent().getStringExtra("CUSTOMER_NAME");
        String date = getIntent().getStringExtra("ORDER_DATE");
        double total = getIntent().getDoubleExtra("ORDER_TOTAL", 0.0);

        // --- 3. ΕΜΦΑΝΙΣΗ ΔΕΔΟΜΕΝΩΝ ---
        if (txtOrderCode != null) txtOrderCode.setText(code);
        if (txtCustomerName != null) txtCustomerName.setText(name);
        if (txtOrderDate != null) txtOrderDate.setText(date);
        if (txtOrderTotal != null) txtOrderTotal.setText(String.format("%.2f €", total));

        // --- 4. ΡΥΘΜΙΣΗ RECYCLERVIEW ME MOCK (ΨΕΥΤΙΚΑ) ΔΕΔΟΜΕΝΑ ---
        RecyclerView recyclerView = findViewById(R.id.rvOrderDetailsProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Δημιουργία λίστας με ψεύτικα δεδομένα για να τεστάρεις την εμφάνιση
        List<CartItem> mockItems = new ArrayList<>();

        // Προϊόν 1 (Laptop)
        ProductType p1 = new ProductType("Dell XPS 15", "Laptop High End", new Money(new BigDecimal(1500), "€"), "DELL-001");
        mockItems.add(new CartItem(p1, 1)); // Ζητάει 1

        // Προϊόν 2 (Mouse)
        ProductType p2 = new ProductType("Logitech Mouse", "Wireless", new Money(new BigDecimal(50), "€"), "LOGI-002");
        mockItems.add(new CartItem(p2, 60)); // Ζητάει 60 (Θα βγει κόκκινο γιατί στοκ=50)

        // Προϊόν 3 (Keyboard)
        ProductType p3 = new ProductType("Razer Keyboard", "Mechanical", new Money(new BigDecimal(120), "€"), "RAZ-003");
        mockItems.add(new CartItem(p3, 2)); // Ζητάει 2
     
        // Σύνδεση με τον Adapter
        StockProductAdapter adapter = new StockProductAdapter(mockItems);
        recyclerView.setAdapter(adapter);


    }
}