package gr.softeng.team21.view.contact.emailDetails;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;

/**
 * Activity for viewing the content of a specific email.
 * Implements {@link EmailDetailsView} to render resolved sender/receiver data
 * passed from the presenter asynchronously onto the UI thread.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDetailsActivity extends AppCompatActivity implements EmailDetailsView {

    private EmailDetailsPresenter presenter;

    private TextView txtSubject;
    private TextView txtSenderName;
    private TextView txtReceiverName;
    private TextView txtSenderEmailAddress;
    private TextView txtReceiverEmailAddress;
    private TextView txtBody;

    private static final String EMP_ID_EXTRA = "user_id";

    /**
     * Initializes the UI, injects Firebase DAOs, and extracts
     * email data from the calling intent.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.EmailDetails), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Dependency Injection with Firebase
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        CustomerDAO customerDAO = new CustomerDAOFirebase();

        presenter = new EmailDetailsPresenter(this, employeeDAO, customerDAO);

        txtSubject = findViewById(R.id.txtEmailDetailsEmailSubjectValue);
        txtSenderName = findViewById(R.id.txtEmailDetailsSenderName);
        txtReceiverName = findViewById(R.id.txtEmailDetailsReceiverName);
        txtSenderEmailAddress = findViewById(R.id.txtEmailDetailsSenderEmail);
        txtReceiverEmailAddress = findViewById(R.id.txtEmailDetailsReceiverEmail);
        txtBody = findViewById(R.id.txtEmailDetailsEmailBody);

        String subject = getIntent().getStringExtra("SUBJECT_EXTRA");
        String senderEmailAddress = getIntent().getStringExtra("SENDER_EXTRA");
        String receiverEmailAddress = getIntent().getStringExtra("RECEIVER_EXTRA");
        String body = getIntent().getStringExtra("BODY_EXTRA");

        String employeeId = getIntent().getStringExtra(EMP_ID_EXTRA);
        presenter.onViewCreated(subject, senderEmailAddress, receiverEmailAddress, body, employeeId);
    }

    /** {@inheritDoc} */
    @Override
    public void displaySubject(String subject) {
        runOnUiThread(() -> txtSubject.setText(subject));
    }

    /** {@inheritDoc} */
    @Override
    public void displaySenderName(String sender) {
        runOnUiThread(() -> txtSenderName.setText(sender));
    }

    /** {@inheritDoc} */
    @Override
    public void displayReceiverName(String receiver) {
        runOnUiThread(() -> txtReceiverName.setText(receiver));
    }

    /** {@inheritDoc} */
    @Override
    public void displayBody(String body) {
        runOnUiThread(() -> txtBody.setText(body));
    }

    /** {@inheritDoc} */
    @Override
    public void displaySenderEmail(String email) {
        runOnUiThread(() -> txtSenderEmailAddress.setText(email));
    }

    /** {@inheritDoc} */
    @Override
    public void displayReceiverEmail(String email) {
        runOnUiThread(() -> txtReceiverEmailAddress.setText(email));
    }
}