package gr.softeng.team21.view.contact.editdata.Username;

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

public class UsernameActivity extends AppCompatActivity implements UsernameView {

    private EditText etUsername;
    private Button btnSave;
    private UsernamePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_username);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userId = getIntent().getStringExtra("user_id");

        etUsername = findViewById(R.id.edittxtUsernameActivity);
        btnSave = findViewById(R.id.btnUsernameActivitySave);

        presenter = new UsernamePresenter(this, userId);

        btnSave.setOnClickListener(v -> saveUsername());
    }

    private void saveUsername() {
        String name = etUsername.getText().toString().trim();
        presenter.saveUsernameClicked(name);
    }

    @Override
    public void setUsername(String username) {
        etUsername.setText(username);
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
    public void finishView() {
        finish();
    }
}