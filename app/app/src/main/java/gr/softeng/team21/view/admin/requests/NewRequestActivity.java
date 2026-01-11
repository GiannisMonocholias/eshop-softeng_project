package gr.softeng.team21.view.admin.requests;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team21.R;

/**
 * In this activity the admin has the ability to create a new request.
 */

public class NewRequestActivity extends AppCompatActivity implements NewRequestActivityView {

    NewRequestPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        presenter = new NewRequestPresenter();

        Spinner spinner = findViewById(R.id.spinnerAction);

        String[] actions = {"Εισαγωγή", "Διαγραφή", "Τροποποίηση"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                actions
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String selectedAction = spinner.getSelectedItem().toString();

        Button btnNewRequest = findViewById(R.id.btnCreateRequest);
        btnNewRequest.setOnClickListener(v -> createRequest());
    }


    /**
     * CreateRequest() takes the user input and passes them to the method of the presenter in order to create a new request.
     */
    @Override
    public void createRequest() {

        Spinner spinner = findViewById(R.id.spinnerAction);
        String choice = spinner.getSelectedItem().toString();

        EditText txtReqDescription = findViewById(R.id.txtRequestDescription);
        String descriptionKeyword = txtReqDescription.getText().toString();

        EditText txtProductName = findViewById(R.id.txtRequestProductName);
        String productNameKeyword = txtProductName.getText().toString();

        EditText txtProductId = findViewById(R.id.txtRequestProductId);
        String productIdKeyword = txtProductId.getText().toString();

        presenter.createRequest(choice , descriptionKeyword , productNameKeyword , productIdKeyword);
    }

}
