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

public class AddressActivity extends AppCompatActivity implements AddressView {

    private EditText etStreet, etNumber, etZip, etCity, etCountry;
    private Button btnSave;
    private AddressPresenter presenter;

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

    private void saveAddress() {
        String street = etStreet.getText().toString().trim();
        String number = etNumber.getText().toString().trim();
        String zip = etZip.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();

        presenter.saveAddressClicked(street, number, zip, city, country);
    }

    @Override
    public void SaveSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAddressDetails(String street, String number, String city, String country,String zip) {
        etStreet.setText(street);
        etNumber.setText(number);
        etZip.setText(zip);
        etCity.setText(city);
        etCountry.setText(country);
    }

    @Override
    public void finishView() {
        finish();
    }
}