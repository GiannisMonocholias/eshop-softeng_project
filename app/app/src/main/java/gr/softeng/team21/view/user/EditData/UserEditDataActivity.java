package gr.softeng.team21.view.user.EditData;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import gr.softeng.team21.R;
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ImageStorageDAO;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.firebasedao.ImageStorageDAOFirebase;

/**
 * Activity responsible for displaying and updating a user's unified personal data profile.
 * Implements MVP for business logic and uses a ViewModel strictly as a State Holder for configuration changes.
 * Integrates camera and gallery functionalities for profile picture management.
 *
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

    private ShapeableImageView ivProfileImage;
    private FloatingActionButton fabEditPhoto;

    // Variables for image selection
    private Uri selectedImageUri = null;
    private Uri cameraUri = null; // Holds the URI for the image captured by the camera

    // Launchers for handling external app results (Gallery and Camera)
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;

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
        setupLaunchers(); // Initialize Camera & Gallery launchers
        setupPhotoEditMenu(); // Setup the PopupMenu for the FAB

        // Initialize MVP components with Firebase DAOs
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();
        ImageStorageDAO imageStorageDAO = new ImageStorageDAOFirebase();

        presenter = new UserEditDataPresenter(this, customerDAO, employeeDAO, imageStorageDAO);

        // Initialize ViewModel (State Holder)
        stateViewModel = new ViewModelProvider(this).get(UserEditDataStateViewModel.class);

        String userId = getIntent().getStringExtra("user_id");

        // Logic to prevent re-fetching data on screen rotation
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
                getVal(etStreet), getVal(etStreetNo), getVal(etCity), getVal(etZip), getVal(etCountry),
                stateViewModel.isPhotoRemoved,
                selectedImageUri,
                userId
        ));

        // Handle native back button press to check for unsaved changes
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

    /**
     * Binds UI components to their respective views in the XML layout.
     */
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

        ivProfileImage = findViewById(R.id.ivProfileImage);
        fabEditPhoto = findViewById(R.id.fabEditPhoto);
    }

    /**
     * Initializes the ActivityResultLaunchers responsible for opening the device's Camera and Gallery,
     * and handling the image returned by the user.
     */
    private void setupLaunchers() {
        // Gallery Mode Launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        stateViewModel.isPhotoRemoved = false;
                        ivProfileImage.setImageURI(uri);
                    }
                }
        );

        // Camera Mode Launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && cameraUri != null) {
                        selectedImageUri = cameraUri;
                        stateViewModel.isPhotoRemoved = false;
                        ivProfileImage.setImageURI(cameraUri);
                    }
                }
        );
    }

    /**
     * Displays a PopupMenu with options to take a photo, select from gallery, or remove the photo
     * when the FloatingActionButton is clicked.
     */
    private void setupPhotoEditMenu() {
        fabEditPhoto.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.photo_menu, popup.getMenu());

            // Force icons to show in the PopupMenu (Required for Android 10+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true);
            }

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_camera) {
                    launchCamera();
                    return true;
                } else if (id == R.id.action_gallery) {
                    galleryLauncher.launch("image/*");
                    return true;
                } else if (id == R.id.action_remove) {
                    ivProfileImage.setImageResource(R.drawable.ic_person);
                    stateViewModel.isPhotoRemoved = true;
                    selectedImageUri = null;
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    /**
     * Creates a temporary file in the cache directory and launches the Camera application
     * using a secure FileProvider URI.
     */
    private void launchCamera() {
        File photoFile = new File(getCacheDir(), "camera_photo_" + System.currentTimeMillis() + ".jpg");
        // Generate secure URI via FileProvider
        cameraUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", photoFile);
        cameraLauncher.launch(cameraUri);
    }

    /**
     * Sets up the expandable/collapsible layout for the address section.
     */
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
     * Saves all current UI text inputs and states into the ViewModel.
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

    /**
     * Restores the UI state from the ViewModel after a configuration change (e.g., rotation).
     */
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

        // Restore the profile image state based on user actions or fetched URL
        if (stateViewModel.isPhotoRemoved) {
            ivProfileImage.setImageResource(R.drawable.ic_person);
        } else if (selectedImageUri != null) {
            ivProfileImage.setImageURI(selectedImageUri);
        } else if (stateViewModel.profileImageUrl != null && !stateViewModel.profileImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(stateViewModel.profileImageUrl)
                    .placeholder(R.drawable.ic_person)
                    .into(ivProfileImage);
        }
    }

    @Override
    public void showUserData(String username, String password, String email, String firstName,
                             String lastName, String phone, String street, String streetNo,
                             String city, String zip, String country, String profileImageUrl) {
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

            // Update ViewModel state
            stateViewModel.isDataLoaded = true;
            stateViewModel.profileImageUrl = profileImageUrl;

            // Asynchronously load the profile image using Glide if a URL exists
            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                Glide.with(this)
                        .load(profileImageUrl)
                        .placeholder(R.drawable.ic_person)
                        .into(ivProfileImage);
            } else {
                ivProfileImage.setImageResource(R.drawable.ic_person);
            }
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

    /**
     * Helper method to retrieve text from a TextInputEditText safely.
     *
     * @param et The TextInputEditText to extract text from.
     * @return The trimmed string value or an empty string if the view is null or empty.
     */
    private String getVal(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}