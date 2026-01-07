package gr.softeng.team21.view.customer.FindProduct;

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

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.view.product.ProductDetailsActivity;

public class CustomerFindProductActivity extends AppCompatActivity implements CustomerFindProductView {

    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<ProductType> adapter;
    private CustomerFindProductPresenter presenter;
    private  String customerId;

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
        presenter = new CustomerFindProductPresenter(this);
        searchView = findViewById(R.id.searchProductActivityHeader);
        listView = findViewById(R.id.ViewlistProductActivity);
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

    private void loadList() {
        presenter.loadList();
    }

    @Override
    public void goToProductDetails(String productCode) {
        Intent intent = new Intent(CustomerFindProductActivity.this, ProductDetailsActivity.class);
        intent.putExtra("PRODUCT_CODE",productCode);
        intent.putExtra("CUSTOMER_ID", customerId);
        startActivity(intent);
    }

    @Override
    public void showProducts(ArrayList<ProductType> products) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        listView.setAdapter(adapter);
    }
}