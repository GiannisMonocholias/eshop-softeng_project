package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView; // <--- Χρειάζεται αυτό το import
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import gr.softeng.team21.R;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.view.user.emailComposition.EmailCompositionActivity;
import gr.softeng.team21.view.contact.emailDetails.EmailDetailsActivity;
import gr.softeng.team21.view.util.EmailAdapter;

public class CustomerServiceEmployeeEmailListActivity extends AppCompatActivity implements CustomerServiceEmployeeEmailListView {

    private CustomerServiceEmployeeEmailListPresenter presenter;
    private static final String EMP_ID_EXTRA = "CUSTOMER_SERVICE_EMPLOYEE_ID";

    private EmailAdapter adapter;

    /**
     * Initializes the UI components, sets up the RecyclerView with an EmailAdapter,
     * and configures the search and creation actions.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewEmailListRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new CustomerServiceEmployeeEmailListPresenter(this, EmployeeDAOMemory.getInstance());

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEmails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        ArrayList<EmailMessage> emailList = presenter.getInbox(employeeId);
        emailList.sort((e1,e2) -> e2.getDateSent().compareTo(e1.getDateSent()));

        adapter = new EmailAdapter(new ArrayList<>(), email -> presenter.onEmailSelected(email, employeeId));
        recyclerView.setAdapter(adapter);

        if (emailList != null) {
            adapter.updateData(emailList);
        }

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
        emailMsgComposition.setOnClickListener(v -> presenter.onCreateNewMsgSelected(employeeId));
    }

    /**
     * Refreshes the email list from the presenter whenever the activity comes to the foreground.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (presenter != null && adapter != null) {
            String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
            ArrayList<EmailMessage> emailList = presenter.getInbox(employeeId);

            emailList.sort((e1, e2) -> e2.getDateSent().compareTo(e1.getDateSent()));

            adapter.updateData(emailList);
        }
    }

    /**
     * {@inheritDoc}
     * Starts the EmailCompositionActivity via an Intent, passing the employee's id as Intent extra.
     */
    @Override
    public void navigateToCreateNewMsg(String employeeId){
        Toast.makeText(this, "Δημιουργία Νέου Μηνύματος...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CustomerServiceEmployeeEmailListActivity.this, EmailCompositionActivity.class);
        intent.putExtra(EMP_ID_EXTRA, employeeId);
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Starts the EmailDetailsActivity, passing all necessary email data via Intent extras.
     */
    @Override
    public void navigateToEmailDetails(String subject, String body, String sender, String receiver, String employeeId){
        Intent intent = new Intent(this, EmailDetailsActivity.class);

        intent.putExtra("SUBJECT_EXTRA", subject); // Σιγουρέψου ότι τα keys ταιριάζουν με το EmailDetailsActivity
        intent.putExtra("BODY_EXTRA", body);
        intent.putExtra("SENDER_EXTRA", sender);
        intent.putExtra("RECEIVER_EXTRA", receiver);
        intent.putExtra("user_id", employeeId);

        startActivity(intent);
    }
}