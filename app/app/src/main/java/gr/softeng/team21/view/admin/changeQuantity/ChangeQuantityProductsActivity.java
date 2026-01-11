package gr.softeng.team21.view.admin.changeQuantity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.view.util.ChangeQuantityProductsAdapter;

/**
 * Activity that shows a list of products in which the admin is able to change the available quantities.
 */

public class ChangeQuantityProductsActivity extends AppCompatActivity implements ChangeQuantityProductsView {

    // presenter initialization. Presenter is responsible to execute the logical part of the activity.
    private ChangeQuantityProductsPresenter presenter;

    // adapter initialization. Adapter is responsible to connect the list of products with the RecyclerView.
    ChangeQuantityProductsAdapter adapter;

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

        presenter = new ChangeQuantityProductsPresenter(this , ProductTypeDAOMemory.getInstance());

        RecyclerView recyclerView = findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<ProductType> availableProducts = presenter.loadProducts();

        adapter = new ChangeQuantityProductsAdapter(availableProducts);
        recyclerView.setAdapter(adapter);
    }
}