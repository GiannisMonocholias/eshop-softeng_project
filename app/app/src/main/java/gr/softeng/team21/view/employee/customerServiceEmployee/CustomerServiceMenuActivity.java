package gr.softeng.team21.view.employee.customerServiceEmployee;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;

public class CustomerServiceMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_service_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityCseDashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Incoming messages view button
        findViewById(R.id.btnOrdPrepEmpMenuAssignedOrders).setOnClickListener(v -> {
            Intent intent = new Intent(CustomerServiceMenuActivity.this,EmailListActivity.class);

            startActivity(intent);

        });

        //Order status notifications button
        findViewById(R.id.btnOrdPrepEmpMenuAssignNewOrder).setOnClickListener(v -> {
            Intent intent = new Intent(CustomerServiceMenuActivity.this, OrderStatusActivity.class);

            startActivity(intent);
        });

        //Account Logout
        findViewById(R.id.btnOrdPrepEmpMenuLogout).setOnClickListener(v -> {
            finish();
        });


    }
}

