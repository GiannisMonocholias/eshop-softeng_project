package gr.softeng.team21.view.admin.changeQuantity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.firebasedao.ProductTypeDAOFirebase;
import gr.softeng.team21.view.util.ChangeQuantityProductsAdapter;

/**
 * Activity that shows a list of products in which the admin is able to change the available quantities.
 * Implements MVP and handles UI updates safely via runOnUiThread using FirebaseDAO.
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public class ChangeQuantityProductsActivity extends AppCompatActivity implements ChangeQuantityProductsView {

    private ChangeQuantityProductsPresenter presenter;
    private ChangeQuantityProductsAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_quantity_products);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dependency Injection: Ενσωμάτωση του FirebaseDAO
        ProductTypeDAO productTypeDAO = new ProductTypeDAOFirebase();
        presenter = new ChangeQuantityProductsPresenter(this, productTypeDAO);

        // Fetch data asynchronously
        presenter.loadProducts();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showProducts(ArrayList<ProductType> products) {
        runOnUiThread(() -> {
            adapter = new ChangeQuantityProductsAdapter(products);
            recyclerView.setAdapter(adapter);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        runOnUiThread(() -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Σφάλμα")
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}