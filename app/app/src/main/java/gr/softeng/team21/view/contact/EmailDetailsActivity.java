package gr.softeng.team21.view.contact;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import gr.softeng.team21.R;

public class EmailDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_email_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.EmailDetails), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // 1. Βρίσκουμε τα πεδία στην οθόνη
        TextView txtSubject = findViewById(R.id.txtEmailDetailsEmailSubjectValue);
        TextView txtSender = findViewById(R.id.txtEmailDetailsSenderName);
        TextView txtBody = findViewById(R.id.txtEmailDetailsEmailBody);

        // 2. Παίρνουμε τα κείμενα που στείλαμε
        String subject = getIntent().getStringExtra("EXTRA_SUBJECT");
        String sender = getIntent().getStringExtra("EXTRA_SENDER");
        String body = getIntent().getStringExtra("EXTRA_BODY");

        // 3. Τα εμφανίζουμε
        txtSubject.setText(subject);
        txtSender.setText("Από: " + sender);
        txtBody.setText(body);
    }
}