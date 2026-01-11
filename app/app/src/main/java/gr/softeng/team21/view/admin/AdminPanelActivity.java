package gr.softeng.team21.view.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Admin;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.view.admin.changeQuantity.ChangeQuantityProductsActivity;
import gr.softeng.team21.view.admin.createEmp.CreateEmplyeeActivity;
import gr.softeng.team21.view.admin.data.AdminDataActivity;
import gr.softeng.team21.view.admin.deleteEmp.DeleteEmployeeActivity;
import gr.softeng.team21.view.admin.requests.AdminRequestsActivity;
import gr.softeng.team21.view.admin.requests.RequestsActivity;

/**
 * Home screen of the admin that shows the menu of the functions that he can execute.
 */

public class AdminPanelActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MemoryInitializer.prepareData();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_panel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnEditData = findViewById(R.id.btnEditData);
        btnEditData.setOnClickListener(v -> editData());

        btnEditData.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this , AdminDataActivity.class);
            startActivity(intent);
        });

        Button btnRequests = findViewById(R.id.btnRequests);
        btnRequests.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this , RequestsActivity.class);
            startActivity(intent);
        });

        Button btnCreateEmp = findViewById(R.id.btnCreateEmp);
        btnCreateEmp.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this , CreateEmplyeeActivity.class);
            startActivity(intent);
        });

        Button btnDeleteEmp = findViewById(R.id.btnDeleteEmp);
        btnDeleteEmp.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this , DeleteEmployeeActivity.class);
            startActivity(intent);
        });

        Button btnQuantities = findViewById(R.id.btnQuantities);
        btnQuantities.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this , ChangeQuantityProductsActivity.class);
            startActivity(intent);
        });

    }

    private void editData(){

    }

}