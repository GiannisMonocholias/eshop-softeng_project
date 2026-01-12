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
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.view.customer.ShoppingCart.CustomerShoppingCartActivity;
import gr.softeng.team21.view.product.ProductDetailsActivity;

/**
 * Activity responsible for searching and listing products for the customer.
 * Implements the {@link CustomerFindProductView} to provide navigation within the user interface, namely
 * (Product Details and Shopping Cart), as well as the Product Search functionality.
 * It manages UI elements such as the search view, the product list, and th
 * @author PAVLOS GRATSANIS
 */
public class CustomerFindProductActivity extends AppCompatActivity implements CustomerFindProductView {

    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<ProductType> adapter;
    private CustomerFindProductPresenter presenter;
    private String customerId;
    private ImageButton btnShoppingCart;
    private Customer customer;
    private TextView txtShoppingCartQuantity;

    /**
     * Initializes the activity, sets up the UI layout, retrieves the customer ID,
     * and initializes the presenter, customer, and the search and navigation components.
     * Specifically, it configures the search view, the product list,the TextView for the for ShoppingCart quantity ,and the shopping cart action button.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
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

        customerId = getIntent().getStringExtra("CUSTOMER_ID");
        customer = CustomerDAOMemory.getInstance().getCustomer(customerId);
        presenter = new CustomerFindProductPresenter(this, customer);

        searchView = findViewById(R.id.searchCustomerFindProductActivity);
        listView = findViewById(R.id.ViewlistCustomerFindProductActivity);
        btnShoppingCart = findViewById(R.id.btnCustomerFindProductActivityShoppingCart);
        txtShoppingCartQuantity = findViewById(R.id.txtCustomerFindProductActivityShoppingCartQuantity);

        btnShoppingCart.setOnClickListener(v -> openShoppingCart());

        loadList();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.filter(newText);
                return true;
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ProductType selectedProduct = adapter.getItem(position);
            presenter.ProductClicked(selectedProduct);
        });
    }

    /**
     * Refreshes the ShoppingCart status  whenever the activity resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        presenter.updateShoppingCartStatus();
    }

    /**
     *Calls the corresponding presenter method
     */
    private void openShoppingCart() {
        presenter.openShoppingCartClicked();
    }

    /**
     *Calls the corresponding presenter method
     */
    private void loadList() {
        presenter.loadList();
    }

    /**
     * {@inheritDoc}
     * Starts the ProductDetailsActivity for the selected product.
     */
    @Override
    public void goToProductDetails(String productCode) {
        Intent intent = new Intent(CustomerFindProductActivity.this, ProductDetailsActivity.class);
        intent.putExtra("PRODUCT_CODE", productCode);
        intent.putExtra("CUSTOMER_ID", customerId);
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Updates the ListView with the provided list of products.
     */
    @Override
    public void showProducts(ArrayList<ProductType> products) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        listView.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     * Starts the CustomerShoppingCartActivity  via an Intent, passing the customer's ID as an extra.
     */
    @Override
    public void goToShoppingCart() {
        Intent intent = new Intent(CustomerFindProductActivity.this, CustomerShoppingCartActivity.class);
        intent.putExtra("CUSTOMER_ID", customerId);
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Displays a Toast message indicating the cart is empty.
     */
    @Override
    public void showEmptyShoppingCartMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * {@inheritDoc}
     * Updates the TextView showing the total quantity of items in the cart.
     */
    @Override
    public void updateShoppingCartQuantity(int quantity) {
        txtShoppingCartQuantity.setText(String.valueOf(quantity));
    }
}