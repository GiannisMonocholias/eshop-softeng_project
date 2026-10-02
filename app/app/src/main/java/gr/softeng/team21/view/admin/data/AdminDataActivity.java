package gr.softeng.team21.view.admin.data;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import gr.softeng.team21.R;

/**
 * Activity responsible for displaying and updating the administrator's personal data.
 * Implements MVP and uses OnBackPressedDispatcher for unsaved changes warning.
 * Now includes Profile Image management (Camera & Gallery) using custom photo_menu.xml.
 *
 * @author Alexandros Drakakis
 */
public class AdminDataActivity extends AppCompatActivity implements AdminDataView {

    private AdminDataPresenter presenter;

    private TextInputEditText etUsername, etPassword, etEmail, etFirstName, etLastName, etPhone;
    private TextInputEditText etStreet, etStreetNo, etCity, etZip;
    private Button btnSave;

    // Photo Selection UI
    private ImageView ivProfileImage;
    private FloatingActionButton fabEditPhoto;

    // Photo URI Variables
    private Uri selectedImageUri = null;
    private Uri cameraImageUri = null;

    // Launchers for picking/taking photos
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;

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
        setupPhotoLaunchers();

        presenter = new AdminDataPresenter(this);

        presenter.loadAdminData();

        // Listeners for Photo change
        fabEditPhoto.setOnClickListener(v -> showPhotoMenu());
        ivProfileImage.setOnClickListener(v -> showPhotoMenu());

        // Pass the selected image URI to the presenter when saving
        btnSave.setOnClickListener(v -> presenter.onSaveClicked(selectedImageUri));

        // Back Button management passing the selectedImageUri to detect unsaved photo changes
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                presenter.onBackPressed(selectedImageUri);
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

        ivProfileImage = findViewById(R.id.ivProfileImage);
        fabEditPhoto = findViewById(R.id.fabEditPhoto);
    }

    /**
     * Sets up the ActivityResultLaunchers for Camera and Gallery.
     */
    private void setupPhotoLaunchers() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                loadPreviewImage();
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success && cameraImageUri != null) {
                selectedImageUri = cameraImageUri;
                loadPreviewImage();
            }
        });
    }

    /**
     * Displays a PopupMenu to let the admin choose the photo source,
     * inflating the options from the custom photo_menu.xml resource.
     */
    private void showPhotoMenu() {
        PopupMenu popup = new PopupMenu(this, fabEditPhoto);
        popup.getMenuInflater().inflate(R.menu.photo_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_camera) {
                cameraImageUri = createImageFileUri();
                if (cameraImageUri != null) {
                    cameraLauncher.launch(cameraImageUri);
                }
                return true;
            } else if (id == R.id.action_gallery) {
                galleryLauncher.launch("image/*");
                return true;
            } else if (id == R.id.action_remove) {
                selectedImageUri = null;
                ivProfileImage.setImageResource(R.drawable.ic_person);
                return true;
            }
            return false;
        });
        popup.show();
    }

    /**
     * Creates a temporary file for the camera to store the captured image.
     */
    private Uri createImageFileUri() {
        try {
            File storageDir = getCacheDir();
            File imageFile = new File(storageDir, "temp_admin_profile_" + System.currentTimeMillis() + ".jpg");
            return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imageFile);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Σφάλμα κατά τη δημιουργία αρχείου εικόνας.");
            return null;
        }
    }

    /**
     * Loads the newly selected image into the ImageView using Glide.
     */
    private void loadPreviewImage() {
        if (selectedImageUri != null) {
            Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.ic_person)
                    .into(ivProfileImage);
        }
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
    public void loadProfileImage(String imageUrl) {
        runOnUiThread(() -> {
            if (ivProfileImage != null) {
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(this)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_person)
                            .into(ivProfileImage);
                } else {
                    ivProfileImage.setImageResource(R.drawable.ic_person);
                }
            }
        });
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