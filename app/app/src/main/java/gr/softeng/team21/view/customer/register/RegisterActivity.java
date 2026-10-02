package gr.softeng.team21.view.customer.register;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.UserCredentialsDAOFirebase;

/**
 * Activity providing the UI for new customer registration.
 * Manages form inputs, handles profile picture selection via Camera/Gallery using FileProvider,
 * coordinates with {@link RegisterPresenter}, and securely handles UI updates.
 *
 * @author Γιάννης Μονοχολιάς
 */
public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private TextInputEditText edtRegisterUsername, edtRegisterPassword, edtRegisterName;
    private TextInputEditText edtRegisterSurname, edtRegisterEmail, edtRegisterPhone;
    private TextInputEditText edtRegisterStreet, edtRegisterNumber, edtRegisterCity, edtRegisterZip;

    private MaterialButton btnRegister;
    private TextView txtLoginLink;

    // Photo Selection UI
    private ImageView imgRegisterProfile;
    private FloatingActionButton fabEditPhoto;

    private RegisterPresenter presenter;

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
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupPhotoLaunchers();

        // Dependency Injection with Firebase DAOs
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        UserCredentialsDAO credentialsDAO = new UserCredentialsDAOFirebase();

        presenter = new RegisterPresenter(this, customerDAO, credentialsDAO);

        // Handle Photo Fab Click
        fabEditPhoto.setOnClickListener(v -> showPhotoMenu());

        btnRegister.setOnClickListener(v -> {
            String username = getTextFromField(edtRegisterUsername);
            String password = getTextFromField(edtRegisterPassword);
            String firstname = getTextFromField(edtRegisterName);
            String lastname = getTextFromField(edtRegisterSurname);
            String email = getTextFromField(edtRegisterEmail);
            String phone = getTextFromField(edtRegisterPhone);

            // Pass the selectedImageUri to the presenter
            presenter.register(username, firstname, password, lastname, phone, email, selectedImageUri);
        });

        txtLoginLink.setOnClickListener(v -> finish());
    }

    private void initializeViews() {
        edtRegisterUsername = findViewById(R.id.edtRegisterUsername);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        edtRegisterName = findViewById(R.id.edtRegisterName);
        edtRegisterSurname = findViewById(R.id.edtRegisterSurname);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPhone = findViewById(R.id.edtRegisterPhone);

        edtRegisterStreet = findViewById(R.id.edtRegisterStreet);
        edtRegisterNumber = findViewById(R.id.edtRegisterNumber);
        edtRegisterCity = findViewById(R.id.edtRegisterCity);
        edtRegisterZip = findViewById(R.id.edtRegisterZip);

        btnRegister = findViewById(R.id.btnRegister);
        txtLoginLink = findViewById(R.id.txtLoginLink);

        imgRegisterProfile = findViewById(R.id.imgRegisterProfile);
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
     * Displays a PopupMenu to let the user choose the photo source.
     */
    private void showPhotoMenu() {
        PopupMenu popup = new PopupMenu(this, fabEditPhoto);
        popup.getMenu().add("Κάμερα");
        popup.getMenu().add("Συλλογή");
        popup.getMenu().add("Αφαίρεση Φωτογραφίας");

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getTitle().toString()) {
                case "Κάμερα":
                    cameraImageUri = createImageFileUri();
                    if (cameraImageUri != null) {
                        cameraLauncher.launch(cameraImageUri);
                    }
                    break;
                case "Συλλογή":
                    galleryLauncher.launch("image/*");
                    break;
                case "Αφαίρεση Φωτογραφίας":
                    selectedImageUri = null;
                    clearImagePreview();
                    break;
            }
            return true;
        });
        popup.show();
    }

    /**
     * Creates a temporary file for the camera to store the captured image.
     */
    private Uri createImageFileUri() {
        try {
            File storageDir = getCacheDir();
            File imageFile = new File(storageDir, "temp_profile_" + System.currentTimeMillis() + ".jpg");

            return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imageFile);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Σφάλμα κατά τη δημιουργία αρχείου εικόνας.");
            return null;
        }
    }

    /**
     * Loads the selected image into the ImageView using Glide.
     */
    private void loadPreviewImage() {
        if (selectedImageUri != null) {
            Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.ic_person)
                    .into(imgRegisterProfile);
        }
    }

    private String getTextFromField(EditText field) {
        return (field.getText() != null) ? field.getText().toString().trim() : "";
    }

    /** {@inheritDoc} */
    @Override
    public void showSuccessMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    /** {@inheritDoc} */
    @Override
    public void showErrorMessage(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    /** {@inheritDoc} */
    @Override
    public void clearInputFields() {
        runOnUiThread(() -> {
            edtRegisterUsername.setText("");
            edtRegisterPassword.setText("");
            edtRegisterName.setText("");
            edtRegisterSurname.setText("");
            edtRegisterEmail.setText("");
            edtRegisterPhone.setText("");

            edtRegisterStreet.setText("");
            edtRegisterNumber.setText("");
            edtRegisterCity.setText("");
            edtRegisterZip.setText("");

            edtRegisterUsername.requestFocus();
        });
    }

    /** {@inheritDoc} */
    @Override
    public void clearImagePreview() {
        runOnUiThread(() -> {
            selectedImageUri = null;
            if (imgRegisterProfile != null) {
                imgRegisterProfile.setImageResource(R.drawable.ic_person);
            }
        });
    }
}