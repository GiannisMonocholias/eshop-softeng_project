package gr.softeng.team21.view.customer;

import gr.softeng.team21.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import gr.softeng.team21.domain.Initializer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.view.product.ProductDetailsActivity;


public class Customer_FindProduct_Activity extends AppCompatActivity {

    SearchView searchView;
    ListView listView;

    // Η λίστα που βλέπει ο χρήστης
    ArrayList<ProductType> displayedProducts = new ArrayList<>();
    ArrayAdapter<ProductType> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_find_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityCseDashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchView = findViewById(R.id.searchProductActivityHeader);
        listView = findViewById(R.id.ViewlistProductActivity);

        Initializer.InitializeProducts();

        // 2. Αρχική Εμφάνιση
        refreshList();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayedProducts);
        listView.setAdapter(adapter);

        // 3. Αναζήτηση
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        // --- 4. ΕΔΩ ΚΑΛΟΥΜΕ ΤΟ PRODUCT DETAILS ---
        listView.setOnItemClickListener((parent, view, position, id) -> {

            // Βρίσκουμε ποιο προϊόν πατήθηκε
            ProductType selectedProduct = displayedProducts.get(position);

            // Δημιουργούμε το Intent για τη νέα οθόνη (ProductDetailsActivity)
            Intent intent = new Intent(Customer_FindProduct_Activity.this, ProductDetailsActivity.class);

            // Στέλνουμε τον ΚΩΔΙΚΟ (ώστε η άλλη οθόνη να βρει την εικόνα και τα στοιχεία)
            intent.putExtra("PRODUCT_CODE", selectedProduct.getProductCode());

            // Ξεκινάμε την οθόνη
            startActivity(intent);
        });
    }

    private void refreshList() {
        displayedProducts.clear();
        displayedProducts.addAll(ProductTypeDAOMemory.getInstance().getProducts().values());
    }

    private void filter(String text) {
        displayedProducts.clear();
        ArrayList<ProductType> allProducts = new ArrayList<>(ProductTypeDAOMemory.getInstance().getProducts().values());

        if (text.isEmpty()) {
            displayedProducts.addAll(allProducts);
        } else {
            String searchText = text.toLowerCase();
            for (ProductType item : allProducts) {
                if (item.getProductname().toLowerCase().contains(searchText)) {
                    displayedProducts.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}