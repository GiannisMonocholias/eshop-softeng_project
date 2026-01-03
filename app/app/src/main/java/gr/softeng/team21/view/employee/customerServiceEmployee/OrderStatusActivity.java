package gr.softeng.team21.view.employee.customerServiceEmployee;

import android.os.Bundle;
import android.widget.Toast;

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
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.domain.EmployeeState;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.view.util.OrderAdapter;

import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.StatusType;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.view.util.OrderAdapterTypes;

public class OrderStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_status);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerViewOrders), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // 1. Βρίσκουμε το RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 2. Φτιάχνουμε δεδομένα (Mock Data)
        List<Order> orders = getDummyOrders();

        // 3. Δημιουργούμε τον Adapter και ορίζουμε τι θα γίνεται στα Κουμπιά
        OrderAdapter adapter = new OrderAdapter(orders, OrderAdapterTypes.NOTIFY_ORDER_ADAPTER , order -> {
            // 1. Βρίσκουμε τον πελάτη
            Customer customer = order.getShoppingCart().getCustomer();

             //2. Έλεγχος για null
            if (customer == null) {
                Toast.makeText(OrderStatusActivity.this, "Σφάλμα: Δεν βρέθηκε πελάτης!", Toast.LENGTH_SHORT).show();
                return; // Σταματάμε εδώ αν δεν υπάρχει πελάτης
            }

            // 3. Λογική ανάλογα με το Status
            if (order.getOrderstatus() == StatusType.DELAYED) {
                // Αν είναι σε καθυστέρηση -> Delay Email
                employee.notifyCustomerDelay(order, customer);
                // Προαιρετικά: Ένα μήνυμα επιβεβαίωσης
                Toast.makeText(OrderStatusActivity.this, "Εστάλη μήνυμα καθυστέρησης.", Toast.LENGTH_SHORT).show();
            }
            else if (order.getOrderstatus() == StatusType.SHIPPED) {
                // Αν είναι απεσταλμένο -> Ready Email
                employee.notifyCustomerReady(order, customer);
                // Προαιρετικά: Ένα μήνυμα επιβεβαίωσης
                Toast.makeText(OrderStatusActivity.this, "Εστάλη μήνυμα παραλαβής.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(OrderStatusActivity.this, "Καμία ενέργεια για αυτό το στάδιο.", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Συνδέουμε τον Adapter με τη λίστα
       recyclerView.setAdapter(adapter);

    }


    private List<Order> getDummyOrders() {
        List<Order> list = new ArrayList<>();

        // Δημιουργούμε έναν πελάτη για να τον συνδέσουμε με τις παραγγελίες
        createDummyCustomerAndEmployee();


        ShoppingCart cart1 = new ShoppingCart();
        cart1.setCustomer(customer);

        list.add(new Order(
                "5501",
                new Date(),
                StatusType.DELAYED,
                false,
                PaymentType.CASH,
                null,
                cart1
        ));


        ShoppingCart cart2 = new ShoppingCart();
        cart2.setCustomer(customer);

        list.add(new Order(
                "5490",
                new Date(),
                StatusType.SHIPPED,
                true,
                PaymentType.CARD,
                null,
                cart2
        ));

        list.add(new Order(
                "5491",
                new Date(),
                StatusType.NEW,
                true,
                PaymentType.CARD,
                null,
                cart2
        ));

        return  list;
    }
    CustomerServiceEmployee employee;
    Customer customer;

    void createDummyCustomerAndEmployee() {
        Date today = new Date(); // Υποθέτουμε ότι ο κενός constructor φέρνει τη σημερινή ημερομηνία
        EmailAddress empEmail = new EmailAddress("employee@team21.gr");
        EmailAddress custEmail = new EmailAddress("customer@gmail.com");

        employee = new CustomerServiceEmployee("emp_user", "Μαρία", "secretPass1", "Παπαδοπούλου", "6912345678",
                empEmail, "EMP-101", 200, 1200, 40, EmployeeState.ACTIVE, today);

        customer = new Customer("cust_user", "Νίκος", "pass1234", "Γεωργίου",
                "6987654321", custEmail, "CUST-500", today);


    }
}