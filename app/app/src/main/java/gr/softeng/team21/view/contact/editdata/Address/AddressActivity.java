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
import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.firebasedao.CustomerDAOFirebase;
import gr.softeng.team21.firebasedao.EmployeeDAOFirebase;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

/**
 * Activity responsible for editing the user's address.
 * Implements {@link AddressView} and manages the UI elements for address input, such as button and editText.
 * Handles UI updates on the main thread for asynchronous DAO compatibility.
 * @author PAVLOS GRATSANIS
 */
public class AddressActivity extends AppCompatActivity implements AddressView {

    private EditText etStreet, etNumber, etZip, etCity, etCountry;
    private Button btnSave;
    private AddressPresenter presenter;

    /**
     * Initializes the activity, sets the UI layout, retrieves the user ID,
     * injects the DAOs, and initializes the presenter.
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

        // Firebase DAOs
        CustomerDAO customerDAO = new CustomerDAOFirebase();
        EmployeeDAO employeeDAO = new EmployeeDAOFirebase();

        presenter = new AddressPresenter(this, userId, customerDAO, employeeDAO);

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

        presenter.saveAddressClicked(street, number, city, country, zip);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void SaveSuccess(String msg) {
        runOnUiThread(() -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String msg) {
        runOnUiThread(() -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAddressDetails(String street, String number, String city, String country, String zip) {
        runOnUiThread(() -> {
            etStreet.setText(street);
            etNumber.setText(number);
            etCity.setText(city);
            etCountry.setText(country);
            etZip.setText(zip);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finishView() {
        runOnUiThread(this::finish);
    }
}