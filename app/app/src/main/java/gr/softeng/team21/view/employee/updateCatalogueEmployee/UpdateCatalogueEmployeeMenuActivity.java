package gr.softeng.team21.view.employee.updateCatalogueEmployee;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;

public class UpdateCatalogueEmployeeMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_catalogue_employee_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.UpdateCatalogueEmployeeMenu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Assigned update catalogue requests to this update catalogue employee
        findViewById(R.id.btnUptCatEmpMenuAssignedRequests).setOnClickListener( v -> {
            Intent intent = new Intent(UpdateCatalogueEmployeeMenuActivity.this, AssignedRequestsToExecuteActivity.class);

            startActivity(intent);
        });

        //Non assigned orders to this update catalogue employee
        findViewById(R.id.btnUptCatEmpMenuAssignNewRequest).setOnClickListener(v -> {
            Intent intent = new Intent(UpdateCatalogueEmployeeMenuActivity.this, AvailableRequestsToAssignActivity.class);

            startActivity(intent);
        });


        //Account Logout
        findViewById(R.id.btnUptCatEmpMenuLogout).setOnClickListener(v ->{
            finish();
        });

    }
}