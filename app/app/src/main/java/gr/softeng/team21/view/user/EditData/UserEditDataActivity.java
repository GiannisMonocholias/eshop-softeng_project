package gr.softeng.team21.view.user.EditData;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;

/**
 * Activity responsible for displaying and updating a user's unified personal data profile.
 * Implements MVP for logic and uses a ViewModel strictly as a State Holder for rotations.
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataActivity extends AppCompatActivity implements UserEditDataView {

    private UserEditDataPresenter presenter;
    private UserEditDataStateViewModel stateViewModel;

    private TextInputEditText etUsername, etPassword, etEmail, etFirstName, etLastName, etPhone;
    private TextInputEditText etStreet, etStreetNo, etCity, etZip, etCountry;

    private Button btnSave;
    private View cardToggleAddress;
    private ImageView ivAddressToggleArrow;
    private LinearLayout layoutAddressContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_edit_data);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupAddressToggle();

        // Initialize MVP components
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        presenter = new UserEditDataPresenter(this, customerDAO, employeeDAO);

        // Initialize ViewModel (State Holder)
        stateViewModel = new ViewModelProvider(this).get(UserEditDataStateViewModel.class);

        String userId = getIntent().getStringExtra("user_id");

        // Logic to prevent re-fetching on rotation
        if (!stateViewModel.isDataLoaded) {
            if (userId != null) {
                presenter.loadUserData(userId);
            }
        } else {
            restoreUiFromViewModel();
        }

        btnSave.setOnClickListener(v -> presenter.onSaveClicked(
                getVal(etUsername), getVal(etPassword), getVal(etEmail),
                getVal(etFirstName), getVal(etLastName), getVal(etPhone),
                getVal(etStreet), getVal(etStreetNo), getVal(etCity), getVal(etZip), getVal(etCountry)
        ));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                presenter.onBackPressed(
                        getVal(etUsername), getVal(etPassword), getVal(etEmail),
                        getVal(etFirstName), getVal(etLastName), getVal(etPhone),
                        getVal(etStreet), getVal(etStreetNo), getVal(etCity), getVal(etZip), getVal(etCountry)
                );
            }
        });
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);

        etStreet = findViewById(R.id.etStreet);
        etStreetNo = findViewById(R.id.etStreetNo);
        etCity = findViewById(R.id.etCity);
        etZip = findViewById(R.id.etZip);
        etCountry = findViewById(R.id.etCountry);

        cardToggleAddress = findViewById(R.id.cardToggleAddress);
        ivAddressToggleArrow = findViewById(R.id.ivAddressToggleArrow);
        layoutAddressContainer = findViewById(R.id.layoutAddressContainer);
        btnSave = findViewById(R.id.btnSaveData);
    }

    private void setupAddressToggle() {
        cardToggleAddress.setOnClickListener(v -> {
            if (layoutAddressContainer.getVisibility() == View.GONE) {
                layoutAddressContainer.setVisibility(View.VISIBLE);
                ivAddressToggleArrow.animate().rotation(180f).setDuration(250).start();
            } else {
                layoutAddressContainer.setVisibility(View.GONE);
                ivAddressToggleArrow.animate().rotation(0f).setDuration(250).start();
            }
        });
    }

    /**
     * Standard Android lifecycle method. Called right before screen rotation or activity stop.
     * Saves all current UI text inputs into the ViewModel.
     */
    @Override
    protected void onPause() {
        super.onPause();
        stateViewModel.username = getVal(etUsername);
        stateViewModel.password = getVal(etPassword);
        stateViewModel.email = getVal(etEmail);
        stateViewModel.firstName = getVal(etFirstName);
        stateViewModel.lastName = getVal(etLastName);
        stateViewModel.phone = getVal(etPhone);
        stateViewModel.street = getVal(etStreet);
        stateViewModel.streetNo = getVal(etStreetNo);
        stateViewModel.city = getVal(etCity);
        stateViewModel.zip = getVal(etZip);
        stateViewModel.country = getVal(etCountry);
    }

    private void restoreUiFromViewModel() {
        etUsername.setText(stateViewModel.username);
        etPassword.setText(stateViewModel.password);
        etEmail.setText(stateViewModel.email);
        etFirstName.setText(stateViewModel.firstName);
        etLastName.setText(stateViewModel.lastName);
        etPhone.setText(stateViewModel.phone);
        etStreet.setText(stateViewModel.street);
        etStreetNo.setText(stateViewModel.streetNo);
        etCity.setText(stateViewModel.city);
        etZip.setText(stateViewModel.zip);
        etCountry.setText(stateViewModel.country);
    }

    @Override
    public void showUserData(String username, String password, String email, String firstName,
                             String lastName, String phone, String street, String streetNo,
                             String city, String zip, String country) {
        runOnUiThread(() -> {
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
            etCountry.setText(country);

            // Mark data as loaded in the ViewModel
            stateViewModel.isDataLoaded = true;
        });
    }

    @Override
    public void showMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showUnsavedChangesDialog() {
        runOnUiThread(() -> new MaterialAlertDialogBuilder(this)
                .setTitle("Μη Αποθηκευμένες Αλλαγές")
                .setMessage("Έχετε κάνει αλλαγές που δεν έχουν αποθηκευτεί. Αν αποχωρήσετε, οι αλλαγές θα χαθούν.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("ΑΠΟΧΩΡΗΣΗ", (dialog, which) -> finishView())
                .setNegativeButton("ΑΚΥΡΟ", (dialog, which) -> dialog.dismiss())
                .show());
    }

    @Override
    public void finishView() {
        runOnUiThread(this::finish);
    }

    private String getVal(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}