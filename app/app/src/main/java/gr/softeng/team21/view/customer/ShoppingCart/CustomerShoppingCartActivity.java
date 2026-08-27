package gr.softeng.team21.view.customer.ShoppingCart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.view.customer.FindProduct.CustomerFindProductActivity;
import gr.softeng.team21.view.customer.Payment.CustomerPaymentActivity;
import gr.softeng.team21.view.util.ShoppingCartAdapter;

/**
 * Activity responsible for displaying and managing the Customer's Shopping Cart.
 * Implements {@link CustomerShoppingCartView} and the {@link ShoppingCartAdapter.CartListener} to manage UI elements
 * such as the RecyclerView for items, the total price textView, and the payment button asynchronously.
 * @author PAVLOS GRATSANIS
 */
public class CustomerShoppingCartActivity extends AppCompatActivity implements ShoppingCartAdapter.CartListener, CustomerShoppingCartView {

    private TextView tvTotalPrice;
    private Button btnPayment, btnBackToSearch;
    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;
    private CustomerShoppingCartPresenter presenter;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_shopping_cart);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        customerId = getIntent().getStringExtra("CUSTOMER_ID");

        tvTotalPrice = findViewById(R.id.txtCustomerShoppingCartActivityTotalPrice);
        btnPayment = findViewById(R.id.btnCustomerShoppingCartActivityPayment);
        btnBackToSearch = findViewById(R.id.btnCustomerShoppingCartActivityBackToSearch);
        recyclerView = findViewById(R.id.recyclerviewCustomerShoppingCartActivity);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dependency Injection - Firebase
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        presenter = new CustomerShoppingCartPresenter(this, customerDAO);

        // Load data asynchronously
        presenter.loadInitialData(customerId);

        btnPayment.setOnClickListener(v -> presenter.ContinuePaymentClicked());
        btnBackToSearch.setOnClickListener(v -> presenter.BackToSearchClicked());
    }

    /** {@inheritDoc} */
    @Override
    public void plus(CartItem item) {
        presenter.plusClicked(item);
    }

    /** {@inheritDoc} */
    @Override
    public void minus(CartItem item) {
        presenter.minusClicked(item);
    }

    /** {@inheritDoc} */
    @Override
    public void delete(CartItem item) {
        presenter.deleteClicked(item);
    }

    /** {@inheritDoc} */
    @Override
    public void showMessage(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_LONG).show());
    }

    /** {@inheritDoc} */
    @Override
    public void goToPayment() {
        runOnUiThread(() -> {
            Intent intent = new Intent(CustomerShoppingCartActivity.this, CustomerPaymentActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId);
            startActivity(intent);
        });
    }

    /** {@inheritDoc} */
    @Override
    public void showTotalPrice(String price) {
        runOnUiThread(() -> tvTotalPrice.setText("Σύνολο: " + price));
    }

    /** {@inheritDoc} */
    @Override
    public void showCartItems(ArrayList<CartItem> cartItems) {
        runOnUiThread(() -> {
            adapter = new ShoppingCartAdapter(cartItems, this);
            recyclerView.setAdapter(adapter);
        });
    }

    /** {@inheritDoc} */
    @Override
    public void goBack() {
        runOnUiThread(() -> {
            Intent intent = new Intent(CustomerShoppingCartActivity.this, CustomerFindProductActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId);
            startActivity(intent);
        });
    }
}