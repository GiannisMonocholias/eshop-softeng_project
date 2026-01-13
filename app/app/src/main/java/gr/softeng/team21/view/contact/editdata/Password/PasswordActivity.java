package gr.softeng.team21.view.contact.editdata.Password;

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
 * Activity responsible for editing the user's password.
 * Implements {@link PasswordView} and manages the UI elements for password input, such as button and editText.
 * @author PAVLOS GRATSANIS
 */
public class PasswordActivity extends AppCompatActivity implements PasswordView {

    private EditText etPass;
    private Button btnSave;
    private PasswordPresenter presenter;

    /**
     * Initializes the activity, sets the UI layout, retrieves the user ID,
     * and initializes the presenter and associated editText and save button.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Λήψη του user_id
        String userId = getIntent().getStringExtra("user_id");

        etPass = findViewById(R.id.edittxtPasswordActivity);
        btnSave = findViewById(R.id.btnPasswordActivitySave);

        // Δημιουργία του Presenter με userId
        // ΠΡΟΣΟΧΗ: Το presenter πρέπει να αρχικοποιηθεί ΜΕΤΑ το findViewById
        // ώστε να μπορεί να καλέσει την setPassword στο etPass
        presenter = new PasswordPresenter(this, userId);

        btnSave.setOnClickListener(v -> savePassword());
    }

    /**
     * Collects input data from the UI fields and calls the presenter to save the changes.
     */
    private void savePassword() {
        String password = etPass.getText().toString().trim();
        presenter.savePasswordClicked(password);
    }

    /**
     * {@inheritDoc}
     * Completes the input field with the existing password.
     */
    @Override
    public void setPassword(String password) {
        etPass.setText(password);
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