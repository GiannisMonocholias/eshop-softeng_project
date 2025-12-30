package gr.softeng.team21.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.view.contact.editdata.AddressActivity;
import gr.softeng.team21.view.contact.editdata.EmailActivity;
import gr.softeng.team21.view.contact.editdata.PasswordActivity;
import gr.softeng.team21.view.contact.editdata.PhoneActivity;
import gr.softeng.team21.view.contact.editdata.UsernameActivity;

public class User_EditData_activity extends AppCompatActivity {

    ListView list;
    EditText etInput;

public static Customer cus=new Customer(
            "giannispap", "Giannis", "pass1234", "Papadopoulos",
                    "697123456", new EmailAddress("giannis7@gmail.com"), "CUST-001", new Date());

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.delivererOrdersList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        list = findViewById(R.id.ViewlistUserEditData);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
            list.setAdapter(adapter);
            list.setOnItemClickListener((parent, view, position, id) -> Selection(position));

    }

    private void Selection(int position) {
        Intent intent = null;

        switch (position) {
            case 0: // 1. Username
                intent = new Intent(User_EditData_activity.this, UsernameActivity.class);
                break;

            case 1: // 2. Password
                intent = new Intent(User_EditData_activity.this, PasswordActivity.class);
                break;

            case 2: // 3. Address
                intent = new Intent(User_EditData_activity.this, AddressActivity.class);
                break;

            case 3: // 4. Email
                intent = new Intent(User_EditData_activity.this, EmailActivity.class);
                break;

            case 4: // 5. Phone
                intent = new Intent(User_EditData_activity.this, PhoneActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}

