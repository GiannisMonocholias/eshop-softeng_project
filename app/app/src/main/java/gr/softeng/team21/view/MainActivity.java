package gr.softeng.team21.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.R;
import gr.softeng.team21.view.user.login.LoginActivity;
import gr.softeng.team21.firebasedao.FirebaseInitializer;

/**
 * The main activity of the application.
 * Initializes the Firebase data and provides navigation to the Login screen.
 * @author PAVLOS GRATSANIS
 */
public class MainActivity extends AppCompatActivity {

    /** Button to navigate to the login screen */
    private Button btnEntrance;

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


        // ========================================================
        // DATABASE INITIALIZATION
        // ========================================================
        CompletableFuture.runAsync(() -> {
            Log.d("FIREBASE_INIT", "Ξεκινάει η μεταφορά δεδομένων στο Firebase. Παρακαλώ περιμένετε...");

            FirebaseInitializer.prepareData();

            Log.d("FIREBASE_INIT", "ΤΕΛΕΙΑ! Τα δεδομένα αρχικοποιήθηκαν επιτυχώς στο Firestore.");
        }).exceptionally(e -> {
            Log.e("FIREBASE_INIT", "Σφάλμα κατά την αρχικοποίηση: " + e.getMessage());
            return null;
        });
        // ========================================================


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