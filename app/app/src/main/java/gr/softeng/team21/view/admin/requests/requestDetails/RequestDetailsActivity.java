package gr.softeng.team21.view.admin.requests.requestDetails;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team21.R;

/**
 * Activity for displaying the complete details of a selected catalogue update request.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class RequestDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        TextView txtDetType = findViewById(R.id.txtDetType);
        TextView txtDetProduct = findViewById(R.id.txtDetProduct);
        TextView txtDetDesc = findViewById(R.id.txtDetDesc);

        // Λήψη δεδομένων από το Intent
        String reqId = getIntent().getStringExtra("REQ_ID");
        String type = getIntent().getStringExtra("REQ_TYPE");
        String date = getIntent().getStringExtra("REQ_DATE");
        String product = getIntent().getStringExtra("REQ_PRODUCT");
        String desc = getIntent().getStringExtra("REQ_DESC");

        txtDetType.setText("#" + reqId + " - " + type);
        txtDetProduct.setText(product + "\nΥποβλήθηκε: " + date);
        txtDetDesc.setText(desc);
    }
}