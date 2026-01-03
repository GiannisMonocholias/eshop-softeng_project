package gr.softeng.team21.view.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.view.customer.ShoppingCart.CustomerShoppingCartActivity;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;

public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsView {

    // UI Elements
    private TextView tvName, tvCode, tvPrice, tvDescription, tvFeatures, tvQuantity;
    private ImageView imgProduct;
    private Button btnAddToCart, btnQuantityminus, btnQuantityplus;

    // Presenter Reference
    private ProductDetailsPresenter presenter;
    private Customer customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String customerId=getIntent().getStringExtra("CUSTOMER_ID");
        customer= CustomerDAOMemory.getInstance().getCustomer(customerId);
        presenter = new ProductDetailsPresenter(this,customer);
        imgProduct = findViewById(R.id.imgDetail);
        tvName = findViewById(R.id.txtProductDetailActivityName);
        tvCode = findViewById(R.id.txtProductDetailActivityCode);
        tvPrice = findViewById(R.id.txtProductDetailActivityPrice);
        tvDescription = findViewById(R.id.txtProductDetailActivityDetailDescription);
        tvFeatures = findViewById(R.id.txtProductDetailActivityDetailFeatures);
        tvQuantity = findViewById(R.id.txtProductDetailActivityQuantity);

        btnAddToCart = findViewById(R.id.btnProductDetailActivityAddCart);
        btnQuantityminus = findViewById(R.id.btnProductDetailActivityQuantityMinus);
        btnQuantityplus = findViewById(R.id.btnProductDetailActivityQuantityPlus);


        String productCode = getIntent().getStringExtra("PRODUCT_CODE");
        presenter.loadProduct(productCode);


        btnAddToCart.setOnClickListener(v -> addToCart());
        btnQuantityplus.setOnClickListener(v -> plus());
        btnQuantityminus.setOnClickListener(v -> minus());
    }

    private void addToCart() {
        presenter.addToCartClicked();
    }

    private void plus() {
presenter.plusClicked();
    }

    private void minus() {
presenter.minusClicked();
    }

    @Override
    public void showProductDetails(String name, String code, String price, String description, String imgCode) {
        tvName.setText(name);
        tvCode.setText("Κωδικός: " + code);
        tvPrice.setText(price);
        tvDescription.setText(description);
        tvFeatures.setText("Δείτε την περιγραφή για αναλυτικά στοιχεία.");
        imgProduct.setImageResource(getImageResIdByCode(imgCode));
    }

    @Override
    public void showQuantity(int quantity) {
        tvQuantity.setText(String.valueOf(quantity));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddToCartSuccess() {
        new AlertDialog.Builder(this)
                .setTitle("Επιτυχής Προσθήκη")
                .setMessage("Το προϊόν προστέθηκε στο καλάθι σας.\nΠώς θέλετε να συνεχίσετε;")
                .setPositiveButton("Προβολή Καλαθιού", (dialog, which) -> presenter.openShoppingCartClicked())
                .setNegativeButton("Συνέχεια Αγορών", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public void goToCart() {
        Intent intent = new Intent(ProductDetailsActivity.this, CustomerShoppingCartActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
        showMessage("Μετάβαση στο Καλάθι...");
    }


    private int getImageResIdByCode(String code) {
        String codeimg = code.toLowerCase().replace("-", "_");
        return getResources().getIdentifier(codeimg, "drawable", getPackageName());
    }
}