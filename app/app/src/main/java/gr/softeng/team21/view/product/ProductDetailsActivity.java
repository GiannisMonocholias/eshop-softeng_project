package gr.softeng.team21.view.product;

<<<<<<< Updated upstream
=======
import android.annotation.SuppressLint;
import android.content.Intent;
>>>>>>> Stashed changes
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

import java.util.HashMap;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.domain.ProductType;
<<<<<<< Updated upstream
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
=======
import gr.softeng.team21.domain.ProductTypesRepository;
import gr.softeng.team21.view.customer.CustomerShoppingCartActivity;
>>>>>>> Stashed changes
import gr.softeng.team21.view.user.User_EditData_activity;

public class ProductDetailsActivity extends AppCompatActivity {
    private int currentQuantity = 1;
    TextView tvName, tvCode, tvPrice, tvDescription, tvFeatures, tvQuantity;
    ImageView imgProduct;
    Button btnAddToCart, btnQuantityminus, btnQuantityplus;


    // Το προϊόν που βλέπουμε
    ProductType foundProduct = null;
    Customer cus = User_EditData_activity.cus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.delivererOrdersList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgProduct = findViewById(R.id.imgDetail);
        tvName = findViewById(R.id.txtProductDetailActivityName);
        tvCode = findViewById(R.id.txtProductDetailActivityCode);
        tvPrice = findViewById(R.id.txtProductDetailActivityPrice);
        tvDescription = findViewById(R.id.txtProductDetailActivityDetailDescription);
        tvFeatures = findViewById(R.id.txtProductDetailActivityDetailFeatures);
        btnAddToCart = findViewById(R.id.btnProductDetailActivityAddCart);
        btnQuantityminus = findViewById(R.id.btnProductDetailActivityQuantityMinus);
        btnQuantityplus = findViewById(R.id.btnProductDetailActivityQuantityPlus);
        tvQuantity = findViewById(R.id.txtProductDetailActivityQuantity);


        String productCode = getIntent().getStringExtra("PRODUCT_CODE");

        if (productCode != null) {
            HashMap<String, ProductType> allProducts = ProductTypeDAOMemory.getInstance().getProducts();
            foundProduct = cus.findProduct(allProducts, productCode);

            // 3. Εμφάνιση
            if (foundProduct != null) {
                tvName.setText(foundProduct.getProductname());
                tvCode.setText("Κωδικός: " + foundProduct.getProductCode());
                tvPrice.setText(foundProduct.getPrice().toString());
                tvDescription.setText(foundProduct.getDescription());
                tvFeatures.setText("Δείτε την περιγραφή για αναλυτικά στοιχεία.");
                imgProduct.setImageResource(getImageResIdByCode(foundProduct.getProductCode()));
            }
        }
        btnAddToCart.setOnClickListener(v -> addToCart());
        btnQuantityplus.setOnClickListener(v -> plus());
        btnQuantityminus.setOnClickListener(v -> minus());
    }

    private void plus() {
        currentQuantity++;
        tvQuantity.setText("" + currentQuantity);
    }

    private void minus() {
        if (currentQuantity > 1) {
            currentQuantity--;
            tvQuantity.setText("" + currentQuantity);
        }
    }

    private void addToCart() {
        try {
            cus.addItemToCart(foundProduct, currentQuantity);
            showShoppingCart();
            Toast.makeText(this, "Προστέθηκαν " + currentQuantity + " τεμάχια στο καλάθι!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showShoppingCart() {
        new AlertDialog.Builder(this)
                .setTitle("Επιτυχής Προσθήκη")
                .setMessage("Το προϊόν προστέθηκε στο καλάθι σας.\nΠώς θέλετε να συνεχίσετε;")
                .setPositiveButton("Προβολή Καλαθιού", (dialog, which) -> openShoppingCart())
                .setNegativeButton("Συνέχεια Αγορών", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void openShoppingCart() {
        Intent intent = new Intent(ProductDetailsActivity.this, CustomerShoppingCartActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Μετάβαση στο Καλάθι...", Toast.LENGTH_SHORT).show();
    }

    private int getImageResIdByCode(String code) {
        String codeimg = code.toLowerCase().replace("-", "_");
        int resId = getResources().getIdentifier(codeimg, "drawable", getPackageName());
        return resId;
    }
}
