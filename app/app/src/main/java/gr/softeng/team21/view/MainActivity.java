package gr.softeng.team21.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import gr.softeng.team21.R;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.user.login.LoginActivity;

/**
 * The main activity of the application.
 * Initializes the in-memory data and provides navigation to the Login screen.
 * @author PAVLOS GRATSANIS
 */
public class MainActivity extends AppCompatActivity {

    /** Button to navigate to the login screen */
    private Button btnEntrance;

    /** Flag to ensure data is prepared only once across the application lifecycle */
    private static boolean isDataPrepared = false;

    /**
     * Called when the activity is first created.
     * Sets up the UI, initializes the memory data (if not already done),
     * and configures the entrance button.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!isDataPrepared) {
            MemoryInitializer.prepareData();
            isDataPrepared = true;
        }
        btnEntrance = findViewById(R.id.btnMainActivityEntrance);
        btnEntrance.setOnClickListener(v -> Entrance());
    }

    /**
     * Navigates to the Login.
     */
    private void Entrance() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}