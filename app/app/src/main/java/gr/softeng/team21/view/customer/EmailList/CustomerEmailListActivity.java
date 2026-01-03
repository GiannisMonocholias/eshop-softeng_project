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
import gr.softeng.team21.domain.EmailMessage;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.view.contact.emailDetails.EmailDetailsActivity;
import gr.softeng.team21.view.user.emailComposition.EmailCompositionActivity;
import gr.softeng.team21.view.util.EmailAdapter;

public class CustomerEmailListActivity extends AppCompatActivity implements CustomerEmailListView {

    private CustomerEmailListPresenter presenter;
    private static final String CUSTOMER_ID_EXTRA = "CUSTOMER_ID";

    private EmailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_list);

        View mainView = findViewById(R.id.viewEmailListRoot);
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new CustomerEmailListPresenter(this, CustomerDAOMemory.getInstance());

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEmails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String customerId = getIntent().getStringExtra(CUSTOMER_ID_EXTRA);

        // Setup Adapter
        adapter = new EmailAdapter(new ArrayList<>(), email -> presenter.onEmailSelected(email, customerId));
        recyclerView.setAdapter(adapter);

        // Load Data
        loadEmails(customerId);

        SearchView searchView = findViewById(R.id.searchViewEmails);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.filter(newText);
                }
                return true;
            }
        });

        FloatingActionButton emailMsgComposition = findViewById(R.id.fabNewEmail);
        emailMsgComposition.setOnClickListener(v -> presenter.onCreateNewMsgSelected(customerId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String customerId = getIntent().getStringExtra(CUSTOMER_ID_EXTRA);
        loadEmails(customerId);
    }

    private void loadEmails(String customerId) {
        if (presenter != null && adapter != null && customerId != null) {
            ArrayList<EmailMessage> emailList = presenter.getInbox(customerId);
            emailList.sort((e1, e2) -> e2.getDateSent().compareTo(e1.getDateSent()));
            adapter.updateData(emailList);
        }
    }


    @Override
    public void goToCreateNewMessge(String customerId) {
        Toast.makeText(this, "Δημιουργία Νέου Μηνύματος...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CustomerEmailListActivity.this, EmailCompositionActivity.class);
        intent.putExtra(CUSTOMER_ID_EXTRA, customerId);
        startActivity(intent);
    }

    @Override
    public void goToEmailDetails(String subject, String body, String sender, String receiver, String customerId) {
        Intent intent = new Intent(this, EmailDetailsActivity.class);

        intent.putExtra("SUBJECT_EXTRA", subject);
        intent.putExtra("BODY_EXTRA", body);
        intent.putExtra("SENDER_EXTRA", sender);
        intent.putExtra("RECEIVER_EXTRA", receiver);
        intent.putExtra(CUSTOMER_ID_EXTRA, customerId);

        startActivity(intent);
    }
}