package gr.softeng.team21.view.contact.editdata;

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
import gr.softeng.team21.domain.Address;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.view.user.User_EditData_activity;

public class AddressActivity extends AppCompatActivity {
    EditText etStreet,etNumber,etZip,etCity,etCountry;
    Button btnSave;
    private Customer curcustomer= User_EditData_activity.cus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.delivererOrdersList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etStreet=findViewById(R.id.edittxtAddressActivityStreet);
        etNumber=findViewById(R.id.edittxtAddressActivityNumber);
        etZip=findViewById(R.id.edittxtAddressActivityZip);
        etCity=findViewById(R.id.edittxtAddressActivityCity);
        etCountry=findViewById(R.id.edittxtAddressActivityCountry);
        btnSave=findViewById(R.id.btnAddressActivitySave);
        btnSave.setOnClickListener(v->saveAddress());
    }

    private void saveAddress() {
        String street = etStreet.getText().toString().trim();
        String number = etNumber.getText().toString().trim();
        String zip = etZip.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String country=etCountry.getText().toString().trim();

        if (street.isEmpty() || number.isEmpty() || zip.isEmpty() || city.isEmpty() || country.isEmpty()) {
            Toast.makeText(this, "Παρακαλώ συμπληρώστε όλα τα πεδία", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            Address newaddress=new Address(street,number,city,country,zip);
            curcustomer.editData("3",null,newaddress,null);
            Toast.makeText(this, "Η διεύθυνση ενημερώθηκε!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}



