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
// Αφαίρεσα τα διπλά/αχρείαστα imports

public class MainActivity extends AppCompatActivity {
    private Button btnEntrance;

    // Σωστή χρήση static για να τρέξει μόνο μια φορά
    private static boolean isDataPrepared = false;

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

    private void Entrance() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}