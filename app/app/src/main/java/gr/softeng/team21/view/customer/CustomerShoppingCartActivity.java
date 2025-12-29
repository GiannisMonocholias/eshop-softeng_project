package gr.softeng.team21.view.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.view.user.User_EditData_activity;

public class CustomerShoppingCartActivity extends AppCompatActivity {
    ListView listCartItems;
    TextView tvTotalPrice;
    Button btnContinuePayment;
    ArrayAdapter<CartItem> CartAdapter;
    ArrayList<CartItem> cartItems=new ArrayList<>();
    Customer cus = User_EditData_activity.cus;

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
        tvTotalPrice = findViewById(R.id.txtCustomerShoppingCartActivityTotalPrice);
        btnContinuePayment = findViewById(R.id.btnCustomerShoppingCartActivityPayment);
        listCartItems=findViewById(R.id.ViewlistCustomerShoppingCartActivity);

        loadCartData();
        setTotalprice();
        btnContinuePayment.setOnClickListener(v -> ContinuePayment());
    }

    private void ContinuePayment() {
        Intent intent=new Intent(CustomerShoppingCartActivity.this,CustomerPaymentActivity.class);
        startActivity(intent);

    }


    private void loadCartData() {
        cartItems = cus.getShoppingCart().getItems();
        CartAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartItems);
        listCartItems.setAdapter(CartAdapter);

    }

    private void setTotalprice() {
        Money totalCost = cus.getShoppingCart().getTotalCost();
        tvTotalPrice.setText("Σύνολο: "+totalCost.toString());
    }

}