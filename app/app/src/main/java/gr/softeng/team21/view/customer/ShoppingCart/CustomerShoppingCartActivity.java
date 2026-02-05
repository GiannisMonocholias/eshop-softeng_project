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
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.view.customer.FindProduct.CustomerFindProductActivity;
import gr.softeng.team21.view.customer.Payment.CustomerPaymentActivity;
import gr.softeng.team21.view.util.ShoppingCartAdapter;

/**
 * Activity responsible for displaying and managing the Customer's Shopping Cart.
 * Implements {@link CustomerShoppingCartView} and the {@link ShoppingCartAdapter.CartListener} to manage UI elements
 * such as the RecyclerView for items, the total price textView, and the payment button.
 * @author PAVLOS GRATSANIS
 */
public class CustomerShoppingCartActivity extends AppCompatActivity implements ShoppingCartAdapter.CartListener, CustomerShoppingCartView {

    private TextView tvTotalPrice;
    private Button btnPayment,btnBackToSearch;
    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;
    private Customer customer;
    private CustomerShoppingCartPresenter presenter;


    /**
     * Initializes the activity, sets up the UI layout, retrieves the customer ID,
     * and initializes the presenter, customer, payment navigation button and the textView totalprice.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
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
        String customerId=getIntent().getStringExtra("CUSTOMER_ID");
        customer= CustomerDAOMemory.getInstance().getCustomer(customerId);
        presenter=new CustomerShoppingCartPresenter(this,customer);

        tvTotalPrice = findViewById(R.id.txtCustomerShoppingCartActivityTotalPrice);
        btnPayment = findViewById(R.id.btnCustomerShoppingCartActivityPayment);
        btnBackToSearch=findViewById(R.id.btnCustomerShoppingCartActivityBackToSearch);
        recyclerView = findViewById(R.id.recyclerviewCustomerShoppingCartActivity);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refresh();
        btnPayment.setOnClickListener(v -> ContinuePayment());
        btnBackToSearch.setOnClickListener(v -> BackToSearch());
    }

    private void BackToSearch() {
        presenter.BackToSearchClicked();
    }

    /**
     *Calls the corresponding presenter method
     */
    private void ContinuePayment() {
        presenter.ContinuePaymentClicked();

    }

    /**
     * {@inheritDoc}
     * Delegates the quantity increase action to the presenter.
     */
    @Override
    public void plus(CartItem item) {
        presenter.plusClicked(item);
    }

    /**
     * {@inheritDoc}
     * Delegates the quantity decrease action to the presenter.
     */
    @Override
    public void minus(CartItem item) {
        presenter.minusClicked(item);
    }

    /**
     * {@inheritDoc}
     * Delegates the item deletion action to the presenter.
     */
    @Override
    public void delete(CartItem item) {
        presenter.deleteClicked(item);
    }

    /**
     *Calls the corresponding presenter method
     */
    private void refresh() {
        presenter.refreshClicked();

    }


    /**
     * {@inheritDoc}
     * Shows a short Toast message to the user with the provided text.
     */
    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

    /**
     * {@inheritDoc}
     * Starts the CustomerPaymentActivity via an Intent, passing the customer's ID as an extra.
     */
    @Override
    public void goToPayment() {
        Intent intent = new Intent(CustomerShoppingCartActivity.this, CustomerPaymentActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Updates the total price TextView with the formatted price string.
     */
    @Override
    public void showTotalPrice(String price) {
        tvTotalPrice.setText("Σύνολο: "+price);
    }

    /**
     * {@inheritDoc}
     * Updates the RecyclerView adapter with the current list of cart items.
     */
    @Override
    public void showCartItems(ArrayList<CartItem> cartItems) {
        adapter = new ShoppingCartAdapter(cartItems, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void goBack() {
        Intent intent = new Intent(CustomerShoppingCartActivity.this, CustomerFindProductActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }
}