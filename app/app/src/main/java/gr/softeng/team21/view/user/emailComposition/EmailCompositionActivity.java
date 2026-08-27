package gr.softeng.team21.view.user.emailComposition;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * Activity providing the UI for drafting and sending internal emails.
 * Implements {@link EmailCompositionView} securely on the UI thread.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailCompositionActivity extends AppCompatActivity implements EmailCompositionView {

    // UI Components
    private TextView txtSenderName, txtSenderEmail;
    private EditText edtRecipient, edtSubject, edtBody;
    private Button btnSend;

    private EmailCompositionPresenter presenter;

    /**
     * Initializes UI components, instantiates the presenter with Firebase DAOs,
     * and identifies the sender ID from the calling intent.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_composition);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewEmailCompsition), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtSenderName = findViewById(R.id.txtEmailDetailsSenderName);
        txtSenderEmail = findViewById(R.id.txtEmailDetailsSenderEmail);
        edtRecipient = findViewById(R.id.edtEmailDetailsReceiver);
        edtSubject = findViewById(R.id.edtEmailSubject);
        edtBody = findViewById(R.id.edtEmailBody);
        btnSend = findViewById(R.id.btnEmailSend);

        // Dependency Injection with Firebase DAOs
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();

        presenter = new EmailCompositionPresenter(this, customerDAO, employeeDAO);

        String userId = null;
        if (getIntent().hasExtra("CUSTOMER_SERVICE_EMPLOYEE_ID")) {
            userId = getIntent().getStringExtra("CUSTOMER_SERVICE_EMPLOYEE_ID");
        } else if (getIntent().hasExtra("CUSTOMER_ID")) {
            userId = getIntent().getStringExtra("CUSTOMER_ID");
        } else if (getIntent().hasExtra("DELIVERER_ID")) {
            userId = getIntent().getStringExtra("DELIVERER_ID");
        } else if (getIntent().hasExtra("ORDER_PREPARATION_EMPLOYEE_ID")) {
            userId = getIntent().getStringExtra("ORDER_PREPARATION_EMPLOYEE_ID");
        }

        if (userId != null) {
            presenter.onViewCreated(userId);
        } else {
            Toast.makeText(this, "Σφάλμα: Δεν βρέθηκε ID χρήστη.", Toast.LENGTH_LONG).show();
            finish();
        }

        btnSend.setOnClickListener(v -> presenter.onSendClicked());
    }

    /** {@inheritDoc} */
    @Override
    public String getRecipientEmail() {
        return edtRecipient.getText().toString().trim();
    }

    /** {@inheritDoc} */
    @Override
    public String getSubject() {
        return edtSubject.getText().toString().trim();
    }

    /** {@inheritDoc} */
    @Override
    public String getBody() {
        return edtBody.getText().toString().trim();
    }

    /** {@inheritDoc} */
    @Override
    public void setSenderDetails(String name, String email) {
        runOnUiThread(() -> {
            txtSenderName.setText(name);
            txtSenderEmail.setText(email);
        });
    }

    /** {@inheritDoc} */
    @Override
    public void showErrorMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    /** {@inheritDoc} */
    @Override
    public void showSuccessMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    /** {@inheritDoc} */
    @Override
    public void finishActivity() {
        runOnUiThread(this::finish);
    }
}