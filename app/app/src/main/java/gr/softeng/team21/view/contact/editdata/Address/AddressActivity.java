package gr.softeng.team21.view.contact.editdata.Address;

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
 * Activity responsible for editing the user's address.
 * Implements {@link AddressView} and manages the UI elements for address input, sush as button and editText.
 * @author PAVLOS GRATSANIS
 */
public class AddressActivity extends AppCompatActivity implements AddressView {

    private EditText etStreet, etNumber, etZip, etCity, etCountry;
    private Button btnSave;
    private AddressPresenter presenter;

    /**
     * Initializes the activity, sets the UI layout, retrieves the user ID,
     * * and initializes the presenter and associated editText and save button.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userId = getIntent().getStringExtra("user_id");

        etStreet = findViewById(R.id.edittxtAddressActivityStreet);
        etNumber = findViewById(R.id.edittxtAddressActivityNumber);
        etZip = findViewById(R.id.edittxtAddressActivityZip);
        etCity = findViewById(R.id.edittxtAddressActivityCity);
        etCountry = findViewById(R.id.edittxtAddressActivityCountry);
        btnSave = findViewById(R.id.btnAddressActivitySave);
        presenter = new AddressPresenter(this, userId);

        btnSave.setOnClickListener(v -> saveAddress());
    }

    /**
     * Collects input data from the UI fields and calls the presenter to save the changes.
     */
    private void saveAddress() {
        String street = etStreet.getText().toString().trim();
        String number = etNumber.getText().toString().trim();
        String zip = etZip.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();

        presenter.saveAddressClicked(street, number, zip, city, country);
    }

    /**
     * {@inheritDoc}
     * Shows a success message via Toast and finishes the activity.
     */
    @Override
    public void SaveSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * {@inheritDoc}
     * Shows an error message via Toast.
     */
    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * {@inheritDoc}
     * Populates the input fields with the existing address details.
     */
    @Override
    public void setAddressDetails(String street, String number, String city, String country, String zip) {
        etStreet.setText(street);
        etNumber.setText(number);
        etZip.setText(zip);
        etCity.setText(city);
        etCountry.setText(country);
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