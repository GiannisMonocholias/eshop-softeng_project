package gr.softeng.team21.view.contact.editdata.Email;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;

/**
 * Activity responsible for editing the user's email address.
 * Implements {@link EmailView} and manages the UI elements for email input, such as button and editText.
 * @author PAVLOS GRATSANIS
 */
public class EmailActivity extends AppCompatActivity implements EmailView {

    private EditText etEmail;
    private Button btnSave;
    private EmailPresenter presenter;

    /**
     * Initializes the activity, sets the UI layout, retrieves the user ID,
     * and initializes the presenter and associated editText and save button.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userId = getIntent().getStringExtra("user_id");

        etEmail = findViewById(R.id.edittxtEmailActivity);
        btnSave = findViewById(R.id.btnEmailActivitySave);

        presenter = new EmailPresenter(this, userId);

        btnSave.setOnClickListener(v -> saveEmail());
    }

    /**
     * Collects input data from the UI fields and calls the presenter to save the changes.
     */
    private void saveEmail() {
        String mailtxt = etEmail.getText().toString().trim();
        presenter.saveEmailClicked(mailtxt);
    }

    /**
     * {@inheritDoc}
     * Completes the input field with the existing email address.
     */
    @Override
    public void setEmail(String email) {
        etEmail.setText(email);
    }

    /**
     * {@inheritDoc}
     * Shows a success message via Toast and finishes the activity.
     */
    @Override
    public void SaveSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * {@inheritDoc}
     * Shows an error message via Toast.
     */
    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * {@inheritDoc}
     * Closes the current activity.
     */
    @Override
    public void finishView() {
        finish();
    }
}