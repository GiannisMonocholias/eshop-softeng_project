package gr.softeng.team21.view.customer.FindProduct;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.ProductTypeDAOFirebase;
import gr.softeng.team21.view.customer.ShoppingCart.CustomerShoppingCartActivity;
import gr.softeng.team21.view.product.ProductDetailsActivity;

/**
 * Activity responsible for searching and listing products for the customer.
 * Uses modern asynchronous patterns to communicate with Firebase DAOs.
 * @author PAVLOS GRATSANIS
 */
public class CustomerFindProductActivity extends AppCompatActivity implements CustomerFindProductView {

    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<ProductType> adapter;
    private CustomerFindProductPresenter presenter;
    private String customerId;
    private ImageButton btnShoppingCart;
    private TextView txtShoppingCartQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_find_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchView = findViewById(R.id.searchCustomerFindProductActivity);
        listView = findViewById(R.id.ViewlistCustomerFindProductActivity);
        btnShoppingCart = findViewById(R.id.btnCustomerFindProductActivityShoppingCart);
        txtShoppingCartQuantity = findViewById(R.id.txtCustomerFindProductActivityShoppingCartQuantity);

        customerId = getIntent().getStringExtra("CUSTOMER_ID");

        // Dependency Injection - Firebase DAOs
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        ProductTypeDAO productDAO = new ProductTypeDAOFirebase();
        presenter = new CustomerFindProductPresenter(this, customerDAO, productDAO);

        btnShoppingCart.setOnClickListener(v -> presenter.openShoppingCartClicked());

        // Εκκίνηση της ασύγχρονης φόρτωσης (Αλυσίδα Πελάτης -> Προϊόντα)
        presenter.loadInitialData(customerId);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.filter(newText);
                return true;
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (adapter != null) {
                ProductType selectedProduct = adapter.getItem(position);
                presenter.ProductClicked(selectedProduct);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.updateShoppingCartStatus();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void showProducts(ArrayList<ProductType> products) {
        runOnUiThread(() -> {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
            listView.setAdapter(adapter);
        });
    }

    /** {@inheritDoc} */
    @Override
    public void showError(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    /** {@inheritDoc} */
    @Override
    public void goToProductDetails(String productCode) {
        Intent intent = new Intent(CustomerFindProductActivity.this, ProductDetailsActivity.class);
        intent.putExtra("PRODUCT_CODE", productCode);
        intent.putExtra("CUSTOMER_ID", customerId);
        startActivity(intent);
    }

    /** {@inheritDoc} */
    @Override
    public void goToShoppingCart() {
        Intent intent = new Intent(CustomerFindProductActivity.this, CustomerShoppingCartActivity.class);
        intent.putExtra("CUSTOMER_ID", customerId);
        startActivity(intent);
    }

    /** {@inheritDoc} */
    @Override
    public void showEmptyShoppingCartMessage(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    /** {@inheritDoc} */
    @Override
    public void updateShoppingCartQuantity(int quantity) {
        runOnUiThread(() -> txtShoppingCartQuantity.setText(String.valueOf(quantity)));
    }
}