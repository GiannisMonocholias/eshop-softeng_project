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
import gr.softeng.team21.view.customer.Payment.CustomerPaymentActivity;

public class CustomerShoppingCartActivity extends AppCompatActivity implements ShoppingCartAdapter.CartListener, CustomerShoppingCartView {

   private TextView tvTotalPrice;
   private Button btnPayment;
   private RecyclerView recyclerView;
   private ShoppingCartAdapter adapter;
    private Customer customer;
    private CustomerShoppingCartPresenter presenter;

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
        recyclerView = findViewById(R.id.recyclerviewCustomerShoppingCartActivity);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refresh();
        btnPayment.setOnClickListener(v -> ContinuePayment());
    }

    private void ContinuePayment() {
        presenter.ContinuePaymentClicked();

    }

    @Override
    public void plus(CartItem item) {
       presenter.plusClicked(item);
    }

    @Override
    public void minus(CartItem item) {
       presenter.minusClicked(item);
    }

    @Override
    public void delete(CartItem item) {
        presenter.deleteClicked(item);
    }

    private void refresh() {
        presenter.refreshClicked();

    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void goToPayment() {
        Intent intent = new Intent(CustomerShoppingCartActivity.this, CustomerPaymentActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void showTotalPrice(String price) {
        tvTotalPrice.setText("Σύνολο: "+price);
    }

    @Override
    public void showCartItems(ArrayList<CartItem> cartItems) {
        adapter = new ShoppingCartAdapter(cartItems, this);
        recyclerView.setAdapter(adapter);
    }
}