package gr.softeng.team21.view.customer.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
import gr.softeng.team21.view.customer.FindProduct.CustomerFindProductActivity;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;
import gr.softeng.team21.view.user.login.LoginActivity;

/**
 * Activity that represents the Customer’s Home Page, i.e., the main menu.
 * Implements the {@link CustomerHomePageView} to provide navigation within the user interface, namely
 * (Product Search, Edit Details, View Inbox, and Logout), as well as the Delete Account functionality.
 * It manages UI elements such as the menu buttons that implement these navigation options and this functionality.
 * @author PAVLOS GRATSANIS
 */
public class CustomerHomePageActivity extends AppCompatActivity implements CustomerHomePageView {
    private CustomerHomePagePresenter presenter;
    private  Customer customer;

    private Button btnEditData, btnDeleteAccount, btnFindProduct, btnLogout,btnInbox;

    /**
     * Initializes the activity, sets up the UI layout, retrieves the customer ID,
     *  and initializes the presenter, customer, and the customer's navigation and action buttons.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String customerId=getIntent().getStringExtra("CUSTOMER_ID");
        if (customerId == null) {
            showMessage("Προσοχή: Ο πελάτης δεν βρέθηκε!");
            finish();
            return;
        }
        customer= CustomerDAOMemory.getInstance().getCustomer(customerId);
        presenter = new CustomerHomePagePresenter(this, customer);


        btnEditData = findViewById(R.id.btnCustomerHomePageEditData);
        btnEditData.setOnClickListener(v -> EditData());

        btnDeleteAccount = findViewById(R.id.btnCustomerHomePageDeleteaccount);
        btnDeleteAccount.setOnClickListener(v -> Delete());

        btnFindProduct = findViewById(R.id.btnCustomerHomePageFindProduct);
        btnFindProduct.setOnClickListener(v -> FindProduct());
        btnLogout = findViewById(R.id.btnCustomerHomePageLogout);
        btnLogout.setOnClickListener(v -> Logout());
        btnInbox=findViewById(R.id.btnCustomerHomePageMessages);
        btnInbox.setOnClickListener(v->Inbox());

    }
    /**
     *Calls the corresponding presenter method
     */
    private void Inbox() {
        presenter.InboxClicked();
    }
    /**
     *Calls the corresponding presenter method
     */
    private void Logout() {
        presenter.LogoutClicked();
    }
    /**
     *Calls the corresponding presenter method
     */
    private void FindProduct() {
        presenter.FindProductClicked();
    }
    /**
     *Calls the corresponding presenter method
     */
    private void Delete() {
        presenter.DeleteClicked();
    }
    /**
     *Calls the corresponding presenter method
     */
    private void EditData() {
        presenter.EditDataClicked();
    }


    /**
     * {@inheritDoc}
     * Starts the LoginActivity and clears the activity stack to prevent returning to the home page.
     */
    @Override
    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * {@inheritDoc}
     * Starts the UserEditDataActivity, passing the customer's ID as an Intent extra.
     */
    @Override
    public void goToEditData() {
        String customerId = getIntent().getStringExtra("CUSTOMER_ID");
        Intent intent = new Intent(this, UserEditDataActivity.class);
        intent.putExtra("user_id", customerId);
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Starts the CustomerFindProductActivity (Product Details), passing the product ID and customer ID.
     */
    @Override
    public void goToFindProduct() {
        String customerId = getIntent().getStringExtra("CUSTOMER_ID");
        Intent intent = new Intent(this, CustomerFindProductActivity.class);
        intent.putExtra("CUSTOMER_ID", customerId);
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Starts the CustomerEmailListActivity via an Intent, passing the customer's ID as an extra.
     */
    @Override
    public void goToInbox() {
        Intent intent = new Intent(this, gr.softeng.team21.view.customer.EmailList.CustomerEmailListActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Displays an AlertDialog asking the user to confirm the account deletion irreversibly.
     */
    @Override
    public void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Διαγραφή Λογαριασμού")
                .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε τον λογαριασμό σας; Η ενέργεια δεν αναιρείται.")
                .setPositiveButton("Ναι", (dialog, which) -> presenter.DeleteConfirm())
                .setNegativeButton("Όχι", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    /**
     * {@inheritDoc}
     * Shows a short Toast message to the user with the provided text.
     */
    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}