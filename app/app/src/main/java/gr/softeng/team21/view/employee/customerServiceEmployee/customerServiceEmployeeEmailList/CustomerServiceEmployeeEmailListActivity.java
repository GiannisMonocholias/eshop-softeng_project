package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

import android.content.Intent;
import android.os.Bundle;
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
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.firebasedao.EmailDAOFirebase;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.view.user.emailComposition.EmailCompositionActivity;
import gr.softeng.team21.view.contact.emailDetails.EmailDetailsActivity;
import gr.softeng.team21.view.util.EmailAdapter;

/**
 * Activity responsible for displaying the list of emails for a Customer Service Employee.
 * Employs Dependency Injection for both EmployeeDAO and EmailDAO.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployeeEmailListActivity extends AppCompatActivity implements CustomerServiceEmployeeEmailListView {
    private CustomerServiceEmployeeEmailListPresenter presenter;
    private static final String EMP_ID_EXTRA = "CUSTOMER_SERVICE_EMPLOYEE_ID";
    private EmailAdapter adapter;
    private String employeeId;

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

        employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);

        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        EmailDAO emailDAO = new EmailDAOFirebase();

        presenter = new CustomerServiceEmployeeEmailListPresenter(this, employeeDAO, emailDAO);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEmails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EmailAdapter(new ArrayList<>(), email -> presenter.onEmailSelected(email, employeeId));
        recyclerView.setAdapter(adapter);

        FloatingActionButton emailMsgComposition = findViewById(R.id.fabNewEmail);
        emailMsgComposition.setOnClickListener(v -> presenter.onCreateNewMsgSelected(employeeId));

        presenter.loadInbox(employeeId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null && employeeId != null) presenter.loadInbox(employeeId);
    }

    @Override
    public void updateEmailList(ArrayList<EmailMessage> emails) {
        runOnUiThread(() -> {
            if (emails != null && adapter != null) {
                emails.sort((e1, e2) -> e2.getDateSent().compareTo(e1.getDateSent()));
                adapter.updateData(emails);
            }
        });
    }

    @Override
    public void showError(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void navigateToCreateNewMsg(String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, EmailCompositionActivity.class);
            intent.putExtra(EMP_ID_EXTRA, employeeId);
            startActivity(intent);
        });
    }

    @Override
    public void navigateToEmailDetails(String subject, String body, String sender, String receiver, String employeeId) {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, EmailDetailsActivity.class);
            intent.putExtra("SUBJECT_EXTRA", subject);
            intent.putExtra("BODY_EXTRA", body);
            intent.putExtra("SENDER_EXTRA", sender);
            intent.putExtra("RECEIVER_EXTRA", receiver);
            intent.putExtra("user_id", employeeId);
            startActivity(intent);
        });
    }
}