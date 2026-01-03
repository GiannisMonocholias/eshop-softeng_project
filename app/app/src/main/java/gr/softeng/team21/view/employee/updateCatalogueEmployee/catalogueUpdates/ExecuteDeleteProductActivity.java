package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates;

import android.content.DialogInterface; // Χρειάζεται για το listener
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog; // <--- ΣΗΜΑΝΤΙΚΟ IMPORT
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;

public class ExecuteDeleteProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_execute_delete_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.executeProcessProduct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ο Κώδικας για το κουμπί με την προειδοποίηση
        findViewById(R.id.btnExecuteDeleteProductConfirmDelete).setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("Επιβεβαίωση Διαγραφής")
                    .setMessage("Είστε σίγουρος ότι θέλετε να διαγράψετε οριστικά αυτό το προϊόν;")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            performDeletion();
                        }
                    })
                    .setNegativeButton("ΟΧΙ", null) // Το null σημαίνει "μην κάνεις τίποτα, απλά κλείσε"
                    .show(); // Εμφάνισε το παράθυρο
        });
    }

    private void performDeletion() {
        // Κάλεσε εδώ το Repository σου για διαγραφή...
        // repo.deleteProduct(...);

        Toast.makeText(this, "Το προϊόν διαγράφηκε επιτυχώς.", Toast.LENGTH_SHORT).show();
        finish(); // Κλείνει το Activity και επιστρέφει πίσω
    }
}