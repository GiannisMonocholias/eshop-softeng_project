package gr.softeng.team21.view.admin.data;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import gr.softeng.team21.R;

/**
 * Activity responsible for displaying and updating the administrator's personal data.
 * Implements MVP and uses OnBackPressedDispatcher for unsaved changes warning.
 * @author Αλέξανδρος Δρακάκης
 */
public class AdminDataActivity extends AppCompatActivity implements AdminDataView {

    private AdminDataPresenter presenter;

    private TextInputEditText etUsername, etPassword, etEmail, etFirstName, etLastName, etPhone;
    private TextInputEditText etStreet, etStreetNo, etCity, etZip;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_data);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminData), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();

        presenter = new AdminDataPresenter(this);

        presenter.loadAdminData();

        btnSave.setOnClickListener(v -> presenter.onSaveClicked());

        // Back Button management
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                presenter.onBackPressed();
            }
        });
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etAdminUsername);
        etPassword = findViewById(R.id.etAdminPassword);
        etEmail = findViewById(R.id.etAdminEmail);
        etFirstName = findViewById(R.id.etAdminFirstName);
        etLastName = findViewById(R.id.etAdminLastName);
        etPhone = findViewById(R.id.etAdminPhone);

        etStreet = findViewById(R.id.etAdminStreet);
        etStreetNo = findViewById(R.id.etAdminStreetNo);
        etCity = findViewById(R.id.etAdminCity);
        etZip = findViewById(R.id.etAdminZip);

        btnSave = findViewById(R.id.btnSaveAdminData);
    }

    @Override public String getUsername() { return etUsername.getText() != null ? etUsername.getText().toString() : ""; }
    @Override public String getPassword() { return etPassword.getText() != null ? etPassword.getText().toString() : ""; }
    @Override public String getEmail() { return etEmail.getText() != null ? etEmail.getText().toString() : ""; }
    @Override public String getFirstName() { return etFirstName.getText() != null ? etFirstName.getText().toString() : ""; }
    @Override public String getLastName() { return etLastName.getText() != null ? etLastName.getText().toString() : ""; }
    @Override public String getPhone() { return etPhone.getText() != null ? etPhone.getText().toString() : ""; }
    @Override public String getStreet() { return etStreet.getText() != null ? etStreet.getText().toString() : ""; }
    @Override public String getStreetNo() { return etStreetNo.getText() != null ? etStreetNo.getText().toString() : ""; }
    @Override public String getCity() { return etCity.getText() != null ? etCity.getText().toString() : ""; }
    @Override public String getZip() { return etZip.getText() != null ? etZip.getText().toString() : ""; }

    @Override
    public void setAdminData(String username, String password, String email, String firstName, String lastName, String phone,
                             String street, String streetNo, String city, String zip) {
        etUsername.setText(username);
        etPassword.setText(password);
        etEmail.setText(email);
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etPhone.setText(phone);
        etStreet.setText(street);
        etStreetNo.setText(streetNo);
        etCity.setText(city);
        etZip.setText(zip);
    }

    @Override
    public void showError(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Σφάλμα")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void showSuccessMessage(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Επιτυχία")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void showUnsavedChangesDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Μη Αποθηκευμένες Αλλαγές")
                .setMessage("Έχετε κάνει αλλαγές που δεν έχουν αποθηκευτεί. Αν αποχωρήσετε, οι αλλαγές θα χαθούν.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("ΑΠΟΧΩΡΗΣΗ", (dialog, which) -> presenter.onDiscardChangesConfirmed())
                .setNegativeButton("ΑΚΥΡΟ", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void finishActivity() {
        finish();
    }
}