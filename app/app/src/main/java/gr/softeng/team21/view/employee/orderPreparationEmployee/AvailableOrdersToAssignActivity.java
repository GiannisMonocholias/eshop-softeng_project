package gr.softeng.team21.view.employee.orderPreparationEmployee;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.domain.EmployeeState;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.domain.StatusType;
import gr.softeng.team21.view.util.OrderAdapter;
import gr.softeng.team21.view.util.OrderAdapterTypes;

public class AvailableOrdersToAssignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_available_orders_to_assign);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.OrdPrepEmpAvailableOrdersToAssign), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.OrdPrepEmprecyclerViewNonAssignedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Date today = new Date();
        EmailAddress empEmail = new EmailAddress("employee@team21.gr");
        EmailAddress custEmail = new EmailAddress("customer@gmail.com");

            CustomerServiceEmployee employee = new CustomerServiceEmployee("emp_user", "Μαρία", "secretPass1", "Παπαδοπούλου", "6912345678",
                empEmail, "EMP-112", 200, 1200, 40, EmployeeState.ACTIVE, today);

            Customer customer = new Customer("cust_user", "Νίκος", "pass1234", "Γεωργίου",
                    "6987654321", custEmail, "CUST-510", today);


            List<Order> orders = new ArrayList<>();
            ShoppingCart cart1 = new ShoppingCart();
            cart1.setCustomer(customer);

            orders.add(new Order("5501", new Date(), StatusType.DELAYED, false, PaymentType.CASH,
                    null,
                    cart1));


            ShoppingCart cart2 = new ShoppingCart();
            cart2.setCustomer(customer);

            orders.add(new Order("5490", new Date(), StatusType.SHIPPED, true, PaymentType.CARD,
                    null,
                    cart2));

            orders.add(new Order("5491", new Date(), StatusType.NEW, true, PaymentType.CARD,
                    null,
                    cart2));


            OrderAdapter adapter = new OrderAdapter(orders, OrderAdapterTypes.ASSIGN_ORDER_ADAPTER, order -> {


            });


            recyclerView.setAdapter(adapter);

        }
}