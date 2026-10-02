package gr.softeng.team21.view.customer.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.UserCredentialsDAOFirebase;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.view.customer.FindProduct.CustomerFindProductActivity;
import gr.softeng.team21.view.customer.OrdersHistoric.CustomerOrderHistoryFragment;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;
import gr.softeng.team21.view.user.login.LoginActivity;

/**
 * Activity that represents the Customer’s Home Page using a Navigation Drawer.
 * Implements the {@link CustomerHomePageView} to provide navigation within the user interface.
 * UI updates and navigation are executed on the main thread for async compatibility.
 * @author PAVLOS GRATSANIS
 */
public class CustomerHomePageActivity extends AppCompatActivity implements CustomerHomePageView {

    private CustomerHomePagePresenter presenter;
    private String customerId; // Stored as a member variable to be accessible by the launcher[cite: 19]

    // UI Components for the Menu[cite: 19]
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton btnMenu;
    private ImageView imgCustomerProfile; // Added for the profile image[cite: 22]

    // Launcher to detect return from Edit Data and refresh the profile image[cite: 19]
    private ActivityResultLauncher<Intent> editProfileLauncher;

    /**
     * Initializes the activity, sets up the Drawer layout, retrieves the customer ID,
     * and initializes the presenter with the required DAOs.
     * @param savedInstanceState If the activity is being re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        customerId = getIntent().getStringExtra("CUSTOMER_ID");
        if (customerId == null) {
            showMessage("Προσοχή: Ο πελάτης δεν βρέθηκε!");
            goToLogin();
            return;
        }

        // Connection to Firebase[cite: 19]
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        UserCredentialsDAO userCredentialsDAO = new UserCredentialsDAOFirebase();

        presenter = new CustomerHomePagePresenter(this, customerId, customerDAO, userCredentialsDAO);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        btnMenu = findViewById(R.id.btnMenu);
        imgCustomerProfile = findViewById(R.id.imgCustomerProfile); // Map to the XML[cite: 22]

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Setup the ActivityResultLauncher[cite: 19]
        setupLaunchers();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.btnCustomerHomePageFindProduct) {
                    presenter.FindProductClicked();
                } else if (id == R.id.btnCustomerHomePageEditData) {
                    presenter.EditDataClicked();
                } else if (id == R.id.btnCustomerHomePageMessages) {
                    presenter.InboxClicked();
                } else if (id == R.id.btnCustomerHomePageΟrderΗistory) {
                    presenter.OrderHistoryClicked();
                } else if (id == R.id.btnCustomerHomePageDeleteaccount) {
                    presenter.DeleteClicked();
                } else if (id == R.id.btnCustomerHomePageLogout) {
                    presenter.LogoutClicked();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    /**
     * Registers the ActivityResultLauncher to listen for the result of the UserEditDataActivity.
     * Triggers a profile data reload (including the image) upon returning.
     */
    private void setupLaunchers() {
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Reload customer data when returning from the Edit Profile screen[cite: 19]
                    if (presenter != null && customerId != null) {
                        presenter.reloadCustomer(customerId); // We add a reload method to the presenter[cite: 20]
                    }
                }
        );
    }

    /**
     * {@inheritDoc}
     * Uses Glide to load the profile image asynchronously.
     */
    @Override
    public void loadProfileImage(String profileImageUrl) {
        runOnUiThread(() -> {
            if (imgCustomerProfile != null) {
                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                    Glide.with(this)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.ic_person)
                            .into(imgCustomerProfile);
                } else {
                    imgCustomerProfile.setImageResource(R.drawable.ic_person);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void goToLogin() {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void goToEditData(String customerId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, UserEditDataActivity.class);
            intent.putExtra("user_id", customerId);
            // Launch using the editProfileLauncher to trigger refresh on return[cite: 19]
            editProfileLauncher.launch(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void goToFindProduct(String customerId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, CustomerFindProductActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId);
            startActivity(intent);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void goToInbox(String customerId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, gr.softeng.team21.view.customer.EmailList.CustomerEmailListActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId);
            startActivity(intent);
        });
    }

    @Override
    public void goToOrderHistory(String customerId) {
        runOnUiThread(() -> {
            CustomerOrderHistoryFragment fragment = CustomerOrderHistoryFragment.newInstance(customerId);
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDeleteConfirmation() {
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("Διαγραφή Λογαριασμού")
                    .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε τον λογαριασμό σας; Η ενέργεια δεν αναιρείται.")
                    .setPositiveButton("Ναι", (dialog, which) -> presenter.DeleteConfirm())
                    .setNegativeButton("Όχι", (dialog, which) -> dialog.dismiss())
                    .setCancelable(false)
                    .show();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String msg) {
        runOnUiThread(() -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });
    }
}