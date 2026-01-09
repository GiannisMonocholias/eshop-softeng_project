package gr.softeng.team21.view.contact.emailDetails;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

/**
 * Activity for viewing the content of a specific email.
 * Implements {@link EmailDetailsView} to render resolved sender/receiver data
 * passed from the presenter.
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

    private static final String  EMP_ID_EXTRA = "user_id";

    /**
     * Initializes the UI, connects to memory DAOs, and extracts
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

        presenter = new EmailDetailsPresenter(this, EmployeeDAOMemory.getInstance(), CustomerDAOMemory.getInstance());

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

    /**
     * {@inheritDoc}
     * Sets the text for the email subject field.
     */
    @Override
    public void displaySubject(String subject) {
        txtSubject.setText(subject);
    }

    /**
     * {@inheritDoc}
     * Sets the text for the sender's display name.
     */
    @Override
    public void displaySenderName(String sender) {
        txtSenderName.setText(sender);
    }

    /**
     * {@inheritDoc}
     * Sets the text for the receiver's full name.
     */
    @Override
    public void displayReceiverName(String receiver) {
        txtReceiverName.setText(receiver);
    }

    /**
     * {@inheritDoc}
     * Sets the text for the message content.
     */
    @Override
    public void displayBody(String body) {
        txtBody.setText(body);
    }

    /**
     * {@inheritDoc}
     * Displays the technical email address of the sender.
     */
    @Override
    public void displaySenderEmail(String email) {
        txtSenderEmailAddress.setText(email);
    }

    /**
     * {@inheritDoc}
     * Displays the technical email address of the receiver.
     */
    @Override
    public void displayReceiverEmail(String email) {
        txtReceiverEmailAddress.setText(email);
    }
}