package gr.softeng.team21.view.employee.orderPreparationEmployee;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team21.R;

public class OrderPreparationEmployeeMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_preparation_employee_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.OrdPrepEmpMenu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Assigned orders to this order preparation employee
        findViewById(R.id.btnCustomerServiceEmployeeMenuAssignedOrders).setOnClickListener(v -> {
            Intent intent = new Intent(OrderPreparationEmployeeMenuActivity.this, AssignedOrdersToPrepareActivity.class);

            startActivity(intent);
        });


        //Non assigned orders to this order preparation employee
        findViewById(R.id.btnOrdPrepEmpMenuAssignNewOrder).setOnClickListener(v -> {
            Intent intent = new Intent(OrderPreparationEmployeeMenuActivity.this, AvailableOrdersToAssignActivity.class);

            startActivity(intent);

        });



        //Account Logout
        findViewById(R.id.btnCustomerServiceEmployeeMenuLogout).setOnClickListener(v -> {
            finish();
        });

    }
}