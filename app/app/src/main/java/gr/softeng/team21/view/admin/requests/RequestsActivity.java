package gr.softeng.team21.view.admin.requests;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.view.admin.requests.adminRequestsActivity.AdminRequestsActivity;
import gr.softeng.team21.view.admin.requests.newRequest.NewRequestActivity;

/**
 * Main menu activity that provides the administrator with the options to either
 * create a new catalogue update request or view the history of submitted requests.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class RequestsActivity extends AppCompatActivity {

    /**
     * Initializes the layout and sets up navigation listeners for the menu buttons.
     * @param savedInstanceState The previously saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_requests);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnAddRequests = findViewById(R.id.btnAddRequest);
        btnAddRequests.setOnClickListener(v -> {
            Intent intent = new Intent(RequestsActivity.this, NewRequestActivity.class);
            startActivity(intent);
        });

        Button btnAdminReqs = findViewById(R.id.btnAdminReqs);
        btnAdminReqs.setOnClickListener(v -> {
            Intent intent = new Intent(RequestsActivity.this, AdminRequestsActivity.class);
            startActivity(intent);
        });
    }
}