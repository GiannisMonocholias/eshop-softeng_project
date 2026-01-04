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
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.view.MainActivity;
import gr.softeng.team21.view.customer.FindProduct.CustomerFindProductActivity;
import gr.softeng.team21.view.user.EditData.UserEditDataActivity;
import gr.softeng.team21.view.user.login.LoginActivity;

public class CustomerHomePageActivity extends AppCompatActivity implements CustomerHomePageView {
    private CustomerHomePagePresenter presenter;
    private  Customer customer;

    private Button btnEditData, btnDeleteAccount, btnFindProduct, btnLogout,btnInbox;

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
          showMessage("Προσοχή: Ο πελάτης δεν βρέθηκε στη μνήμη!");
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

    private void Inbox() {
        presenter.InboxClicked();
    }

    private void Logout() {
        presenter.LogoutClicked();
    }

    private void FindProduct() {
        presenter.FindProductClicked();
    }

    private void Delete() {
        presenter.DeleteClicked();
    }

    private void EditData() {
        presenter.EditDataClicked();
    }


    @Override
    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToEditData() {
        Intent intent = new Intent(this, UserEditDataActivity.class);
        intent.putExtra("user_id", customer.getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void goToFindProduct() {
        Intent intent = new Intent(this, CustomerFindProductActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToInbox() {
        Intent intent = new Intent(this, gr.softeng.team21.view.customer.EmailList.CustomerEmailListActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

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
    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}