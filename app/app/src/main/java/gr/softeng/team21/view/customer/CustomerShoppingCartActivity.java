package gr.softeng.team21.view.customer;

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
import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.view.user.User_EditData_activity;

public class CustomerShoppingCartActivity extends AppCompatActivity implements ShoppingCartAdapter.CartListener {

    TextView tvTotalPrice;
    Button btnPayment;
    RecyclerView recyclerView;
    ShoppingCartAdapter adapter;
    Customer cus = User_EditData_activity.cus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_shopping_cart);
<<<<<<< Updated upstream
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.delivererOrdersList), (v, insets) -> {
=======

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
>>>>>>> Stashed changes
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTotalPrice = findViewById(R.id.txtCustomerShoppingCartActivityTotalPrice);
        btnPayment = findViewById(R.id.btnCustomerShoppingCartActivityPayment);
        recyclerView = findViewById(R.id.recyclerviewCustomerShoppingCartActivity);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCartData();
        setTotalprice();

        btnPayment.setOnClickListener(v -> ContinuePayment());
    }

    private void ContinuePayment() {
        if (cus.getShoppingCart().getItems().isEmpty()) {
            Toast.makeText(this, "Το καλάθι είναι άδειο!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(CustomerShoppingCartActivity.this, CustomerPaymentActivity.class);
            startActivity(intent);
        }
    }

    private void loadCartData() {
        List<CartItem> items = new ArrayList<>(cus.getShoppingCart().getItems());
        adapter = new ShoppingCartAdapter(items,this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void plus(CartItem item) {
        try {
            cus.addItemToCart(item.getProductType(), 1);
            refresh();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void minus(CartItem item) {
        try {
            cus.removeItemFromCart(item.getProductType(), 1);
            refresh();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void delete(CartItem item) {
        try {
            cus.removeItemFromCart(item.getProductType(), item.getQuantity());
            refresh();
            Toast.makeText(this, "Αφαιρέθηκε", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void refresh() {
        loadCartData();
        setTotalprice();
    }

    private void setTotalprice() {
        if (cus.getShoppingCart() != null) {
            Money totalCost = cus.getShoppingCart().getTotalCost();
            tvTotalPrice.setText("Σύνολο: " + String.format("%.2f €", totalCost.getAmount()));
        }
    }
}