package gr.softeng.team21.view.admin.requests;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import gr.softeng.team21.R;
import gr.softeng.team21.view.admin.AdminPanelActivity;

/**
 * This activty gives to the admin the choice to check the submitted requests or to create a new request.
 */

public class RequestsActivity extends AppCompatActivity {

    RequestsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_requests);

        presenter = new RequestsPresenter();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnAddRequests = findViewById(R.id.btnAddRequest);
        btnAddRequests.setOnClickListener(v -> {
            Intent intent = new Intent(RequestsActivity.this , NewRequestActivity.class);
            startActivity(intent);
        });

        Button btnAdminReqs = findViewById(R.id.btnAdminReqs);
        btnAdminReqs.setOnClickListener(v -> {
            Intent intent = new Intent(RequestsActivity.this , AdminRequestsActivity.class);
            startActivity(intent);
        });
    }
}