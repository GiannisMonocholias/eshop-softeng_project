package gr.softeng.team21.view.user.EditData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.view.contact.editdata.Address.AddressActivity;
import gr.softeng.team21.view.contact.editdata.Email.EmailActivity;
import gr.softeng.team21.view.contact.editdata.Password.PasswordActivity;
import gr.softeng.team21.view.contact.editdata.Phone.PhoneActivity;
import gr.softeng.team21.view.contact.editdata.Username.UsernameActivity;
import gr.softeng.team21.view.customer.homePage.CustomerHomePageActivity;

public class UserEditDataActivity extends AppCompatActivity implements UserEditDataView {

    private ListView list;
    private EditText etInput;
    private Customer customer;

private UserEditDataPresenter presenter;
    private final String[] options = {
            "1. Αλλαγή Username",
            "2. Αλλαγή Password",
            "3. Αλλαγή Διεύθυνσης",
            "4. Αλλαγή Email",
            "5. Αλλαγή Τηλεφώνου"
    };

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
        String customerId=getIntent().getStringExtra("CUSTOMER_ID");
        customer= CustomerDAOMemory.getInstance().getCustomer(customerId);
        presenter=new UserEditDataPresenter(this);
        list = findViewById(R.id.ViewlistUserEditData);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> OptionSelected(position));

    }
private void OptionSelected(int position){
        presenter.Selection( position);
}




    @Override
    public void goToUsername() {
        Intent intent = new Intent(UserEditDataActivity.this, UsernameActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void goToPassword() {
        Intent intent = new Intent(UserEditDataActivity.this, PasswordActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void goToAddress() {
        Intent intent = new Intent(UserEditDataActivity.this, AddressActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void goToEmail() {
        Intent intent = new Intent(UserEditDataActivity.this, EmailActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }

    @Override
    public void goToPhone() {
        Intent intent = new Intent(UserEditDataActivity.this, PhoneActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getCustomer_id());
        startActivity(intent);
    }
}

