package gr.softeng.team21.view.customer.EmailList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import gr.softeng.team21.R;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.view.contact.emailDetails.EmailDetailsActivity;
import gr.softeng.team21.view.user.emailComposition.EmailCompositionActivity;
import gr.softeng.team21.view.util.EmailAdapter;

/**
 * Activity responsible for displaying the list of emails for a Customer.
 * Implements the {@link CustomerEmailListView} and handles async updates from the Presenter.
 * @author PAVLOS GRATSANIS
 */
public class CustomerEmailListActivity extends AppCompatActivity implements CustomerEmailListView {

    private CustomerEmailListPresenter presenter;
    private static final String CUSTOMER_ID_EXTRA = "CUSTOMER_ID";
    private RecyclerView recyclerView;
    private FloatingActionButton emailMsgComposition;
    private SearchView searchView;
    private EmailAdapter adapter;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_list);

        View mainView = findViewById(R.id.viewEmailListRoot);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Dependency Injection for Firebase
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        presenter = new CustomerEmailListPresenter(this, customerDAO);

        recyclerView = findViewById(R.id.recyclerViewEmails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customerId = getIntent().getStringExtra(CUSTOMER_ID_EXTRA);
        adapter = new EmailAdapter(new ArrayList<>(), email -> presenter.onEmailSelected(email, customerId));
        recyclerView.setAdapter(adapter);

        presenter.loadInbox(customerId);

        searchView = findViewById(R.id.searchViewEmails);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) adapter.filter(newText);
                return true;
            }
        });

        emailMsgComposition = findViewById(R.id.fabNewEmail);
        emailMsgComposition.setOnClickListener(v -> presenter.onCreateNewMsgSelectedClicked(customerId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null && customerId != null) {
            presenter.loadInbox(customerId);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void showEmails(ArrayList<EmailMessage> emails) {
        runOnUiThread(() -> {
            emails.sort((e1, e2) -> e2.getDateSent().compareTo(e1.getDateSent()));
            if (adapter != null) {
                adapter.updateData(emails);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void showError(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    /** {@inheritDoc} */
    @Override
    public void goToCreateNewMessge(String customerId) {
        Intent intent = new Intent(CustomerEmailListActivity.this, EmailCompositionActivity.class);
        intent.putExtra(CUSTOMER_ID_EXTRA, customerId);
        startActivity(intent);
    }

    /** {@inheritDoc} */
    @Override
    public void goToEmailDetails(String subject, String body, String sender, String receiver, String customerId) {
        Intent intent = new Intent(this, EmailDetailsActivity.class);
        intent.putExtra("SUBJECT_EXTRA", subject);
        intent.putExtra("BODY_EXTRA", body);
        intent.putExtra("SENDER_EXTRA", sender);
        intent.putExtra("RECEIVER_EXTRA", receiver);
        intent.putExtra("user_id", customerId);
        startActivity(intent);
    }
}