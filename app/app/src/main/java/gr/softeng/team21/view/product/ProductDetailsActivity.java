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

/**
 * Activity responsible for displaying the detailed information of a specific product.
 * Implements {@link ProductDetailsView} and manages the UI elements,such as TextView,button and ImageView for viewing details,
 * adjusting quantity and adding the product to the shopping cart.
 * @author PAVLOS GRATSANIS
 */
public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsView {

    private TextView tvName, tvCode, tvPrice, tvDescription, tvQuantity;
    private ImageView imgProduct;
    private Button btnAddToCart, btnQuantityminus, btnQuantityplus;

    private ProductDetailsPresenter presenter;
    private Customer customer;

    /**
     * Initializes the activity, sets the UI layout, retrieves the customer and product IDs from the Intent,
     * and initializes the presenter and associated UI elements.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
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

    /**
     *Calls the corresponding presenter method
     */
    private void addToCart() {
        presenter.addToCartClicked();
    }

    /**
     *Calls the corresponding presenter method
     */
    private void plus() {
        presenter.plusClicked();
    }

    /**
     *Calls the corresponding presenter method
     */
    private void minus() {
        presenter.minusClicked();
    }

    /**
     * {@inheritDoc}
     * Updates the UI with the product's details (name, code, price, description, image).
     */
    @Override
    public void showProductDetails(String name, String code, String price, String description, String imgCode) {
        tvName.setText(name);
        tvCode.setText("Κωδικός: " + code);
        tvPrice.setText(price);
        tvDescription.setText(description);
        imgProduct.setImageResource(getImageResIdByCode(imgCode));
    }

    /**
     * {@inheritDoc}
     * Updates the quantity TextView with the current selected quantity.
     */
    @Override
    public void showQuantity(int quantity) {
        tvQuantity.setText(String.valueOf(quantity));
    }

    /**
     * {@inheritDoc}
     * Shows a short Toast message to the user.
     */
    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * {@inheritDoc}
     * Displays an AlertDialog confirming the product addition and offering navigation options.
     */
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

    /**
     * {@inheritDoc}
     * Navigates to the CustomerShoppingCartActivity via an Intent, passing the customer's ID as an extra.
     */
    @Override
    public void goToCart() {
        Intent intent = new Intent(ProductDetailsActivity.this, CustomerShoppingCartActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
        showMessage("Μετάβαση στο Καλάθι...");
    }

    /**
     * Helper method for a specific image from the drawable folder based on the product code string.
     * @param code The product code.
     * @return The resource ID of the image.
     */
    private int getImageResIdByCode(String code) {
        String codeimg = code.toLowerCase().replace("-", "_");
        return getResources().getIdentifier(codeimg, "drawable", getPackageName());
    }
}