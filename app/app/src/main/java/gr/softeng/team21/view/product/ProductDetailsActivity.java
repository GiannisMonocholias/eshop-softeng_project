package gr.softeng.team21.view.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup; // Απαραίτητο για το LayoutParams
import android.widget.Button;
import android.widget.FrameLayout; // Απαραίτητο για το LayoutParams
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.ProductReviewsDao;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.ProductReview;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.ProductReviewsDaoFirebase;
import gr.softeng.team21.firebasedao.ProductTypeDAOFirebase;
import gr.softeng.team21.view.customer.ShoppingCart.CustomerShoppingCartActivity;
import gr.softeng.team21.view.product.Reviews.ProductReviewsFragment;

/**
 * Activity responsible for displaying the detailed information of a specific product.
 * Implements {@link ProductDetailsView} and manages the UI elements,such as TextView,button and ImageView for viewing details,
 * adjusting quantity and adding the product to the shopping cart safely on the UI thread.
 * @author PAVLOS GRATSANIS
 */
public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsView {

    private TextView tvName, tvCode, tvPrice, tvDescription, tvQuantity, tvReviewCount;
    private ImageView imgProduct;
    private Button btnAddToCart, btnQuantityminus, btnQuantityplus, btnProductReviews;

    private ProductDetailsPresenter presenter;
    private String customerId, productCode;
    private RatingBar ratingBar;

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

        customerId = getIntent().getStringExtra("CUSTOMER_ID");
        productCode = getIntent().getStringExtra("PRODUCT_CODE");

        // Dependency Injection for Firebase DAOs
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        ProductTypeDAO productDAO = new ProductTypeDAOFirebase();
        ProductReviewsDao reviewsDao = new ProductReviewsDaoFirebase();
        presenter = new ProductDetailsPresenter(this, customerDAO, productDAO, reviewsDao);

        imgProduct = findViewById(R.id.imgDetail);
        tvName = findViewById(R.id.txtProductDetailActivityName);
        tvCode = findViewById(R.id.txtProductDetailActivityCode);
        tvPrice = findViewById(R.id.txtProductDetailActivityPrice);
        tvDescription = findViewById(R.id.txtProductDetailActivityDetailDescription);
        tvQuantity = findViewById(R.id.txtProductDetailActivityQuantity);

        ratingBar = findViewById(R.id.ratingBarProductDetail);

        btnAddToCart = findViewById(R.id.btnProductDetailActivityAddCart);
        btnQuantityminus = findViewById(R.id.btnProductDetailActivityQuantityMinus);
        btnQuantityplus = findViewById(R.id.btnProductDetailActivityQuantityPlus);
        btnProductReviews = findViewById(R.id.btnProductDetailViewReviews);

        // Initiate asynchronous loading sequence
        presenter.loadInitialData(customerId, productCode);

        btnAddToCart.setOnClickListener(v -> presenter.addToCartClicked());
        btnQuantityplus.setOnClickListener(v -> presenter.plusClicked());
        btnQuantityminus.setOnClickListener(v -> presenter.minusClicked());
        btnProductReviews.setOnClickListener(v -> presenter.productReviewsClicked());
    }

    @Override
    public void showProductDetails(String name, String code, String price, String description, String imgCode) {
        runOnUiThread(() -> {
            tvName.setText(name);
            tvCode.setText("Κωδικός: " + code);
            tvPrice.setText(price);
            tvDescription.setText(description);
            imgProduct.setImageResource(getImageResIdByCode(imgCode));
        });
    }

    @Override
    public void showQuantity(int quantity) {
        runOnUiThread(() -> tvQuantity.setText(String.valueOf(quantity)));
    }

    @Override
    public void showMessage(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showAddToCartSuccess() {
        runOnUiThread(() -> new AlertDialog.Builder(this)
                .setTitle("Επιτυχής Προσθήκη")
                .setMessage("Το προϊόν προστέθηκε στο καλάθι σας.\nΠώς θέλετε να συνεχίσετε;")
                .setPositiveButton("Προβολή Καλαθιού", (dialog, which) -> presenter.openShoppingCartClicked())
                .setNegativeButton("Συνέχεια Αγορών", (dialog, which) -> finish())
                .setCancelable(false)
                .show());
    }

    @Override
    public void goToCart() {
        runOnUiThread(() -> {
            Intent intent = new Intent(ProductDetailsActivity.this, CustomerShoppingCartActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId);
            startActivity(intent);
        });
    }

    @Override
    public void goToProductReviews(ArrayList<ProductReview> reviews) {
        runOnUiThread(() -> {
            FrameLayout fragmentContainer = findViewById(R.id.fragment_container_reviews);
            ViewGroup.LayoutParams params = fragmentContainer.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            fragmentContainer.setLayoutParams(params);
            fragmentContainer.setVisibility(View.VISIBLE);

            ProductReviewsFragment fragment = ProductReviewsFragment.newInstance(reviews);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_reviews, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public void showAverageRating(float average) {
        runOnUiThread(() -> {
            if (productCode != null && ratingBar != null) {
                ratingBar.setRating(average);
            }
        });
    }

    // Προσθήκη για να κρύβεται σωστά το Fragment όταν πατάς "Πίσω"
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container_reviews);
        if (fragmentContainer.getVisibility() == View.VISIBLE) {
            ViewGroup.LayoutParams params = fragmentContainer.getLayoutParams();
            params.width = 1;
            params.height = 1;
            fragmentContainer.setLayoutParams(params);
            fragmentContainer.setVisibility(View.INVISIBLE);
        }
    }

    private int getImageResIdByCode(String code) {
        String codeimg = code.toLowerCase().replace("-", "_");
        return getResources().getIdentifier(codeimg, "drawable", getPackageName());
    }
}