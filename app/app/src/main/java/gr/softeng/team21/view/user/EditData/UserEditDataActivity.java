package gr.softeng.team21.view.user.EditData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.view.contact.editdata.Address.AddressActivity;
import gr.softeng.team21.view.contact.editdata.Email.EmailActivity;
import gr.softeng.team21.view.contact.editdata.Password.PasswordActivity;
import gr.softeng.team21.view.contact.editdata.Phone.PhoneActivity;
import gr.softeng.team21.view.contact.editdata.Username.UsernameActivity;

/**
 * Activity responsible for displaying the menu options for editing user data.
 * Implements {@link UserEditDataView} and manages the UI elements, such as ListView,
 * to allow navigation to data editing screens (Username, Password, Address, Email, Phone).
 *
 * @author PAVLOS GRATSANIS
 */
public class UserEditDataActivity extends AppCompatActivity implements UserEditDataView {

    private ListView list;
    private UserEditDataPresenter presenter;
    private String userId;

    private final String[] options = {
            "1. Αλλαγή Username",
            "2. Αλλαγή Password",
            "3. Αλλαγή Διεύθυνσης",
            "4. Αλλαγή Email",
            "5. Αλλαγή Τηλεφώνου"
    };

    /**
     * Initializes the activity, sets the UI layout, retrieves the user ID,
     * initializes the presenter, and sets up the ListView with the available editing options.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down.
     */
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

        userId = getIntent().getStringExtra("user_id");
        presenter = new UserEditDataPresenter(this);

        list = findViewById(R.id.ViewlistUserEditData);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> onOptionSelected(position));
    }

    /**
     * Calls the corresponding presenter method to handle the list selection.
     * @param position The position of the item clicked in the list.
     */
    private void onOptionSelected(int position) {
        presenter.handleSelection(position);
    }

    /** {@inheritDoc} */
    @Override
    public void goToUsername() {
        Intent intent = new Intent(UserEditDataActivity.this, UsernameActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    /** {@inheritDoc} */
    @Override
    public void goToPassword() {
        Intent intent = new Intent(UserEditDataActivity.this, PasswordActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    /** {@inheritDoc} */
    @Override
    public void goToAddress() {
        Intent intent = new Intent(UserEditDataActivity.this, AddressActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    /** {@inheritDoc} */
    @Override
    public void goToEmail() {
        Intent intent = new Intent(UserEditDataActivity.this, EmailActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    /** {@inheritDoc} */
    @Override
    public void goToPhone() {
        Intent intent = new Intent(UserEditDataActivity.this, PhoneActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
}