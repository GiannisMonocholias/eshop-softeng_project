package gr.softeng.team21.view.employee.orderPreparationEmployee;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.domain.EmployeeState;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.domain.StatusType;
import gr.softeng.team21.view.util.OrderAdapter;
import gr.softeng.team21.view.util.OrderAdapterTypes;

public class AssignedOrdersToPrepareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assigned_orders_to_prepare);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.OrdPrepEmpAssignedOrders), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.OrdPrepEmprecyclerViewAssignedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Date today = new Date(); // Υποθέτουμε ότι ο κενός constructor φέρνει τη σημερινή ημερομηνία
        EmailAddress empEmail = new EmailAddress("employee@team21.gr");
        EmailAddress custEmail = new EmailAddress("customer@gmail.com");

        CustomerServiceEmployee employee = new CustomerServiceEmployee("emp_user", "Μαρία", "secretPass1", "Παπαδοπούλου", "6912345678",
                empEmail, "EMP-112", 200, 1200, 40, EmployeeState.ACTIVE, today);

        Customer customer = new Customer("cust_user", "Νίκος", "pass1234", "Γεωργίου",
                "6987654321", custEmail, "CUST-510", today);


        List<ProductType> products = new ArrayList<>();
        products.add(new ProductType("Dell XPS 15", "Laptop i7, 16GB RAM, 512GB SSD",
                new Money(new BigDecimal(1450.00),"€"),
                "DELL-XPS"));


        products.add(new ProductType("Samsung Galaxy S24", "Smartphone 256GB, 5G, Onyx Black",
                new Money(new BigDecimal(899.90),"€"),
                "SAM-S24"));

        products.add(new ProductType("Logitech MX Master 3S", "Ασύρματο εργονομικό ποντίκι",
                new Money(new BigDecimal(115.50),"€"),
                "LOG-MX3S"));


        List<Order> orders = new ArrayList<>();
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setCustomer(customer);
        cart1.addItem(new CartItem(products.get(2),3));
        cart1.addItem(new CartItem(products.get(0),1));


        orders.add(new Order("5501", new Date(), StatusType.DELAYED, false, PaymentType.CASH,
                null,
                cart1));


        ShoppingCart cart2 = new ShoppingCart();
        cart2.setCustomer(customer);
        cart2.addItem(new CartItem(products.get(0),2));
        cart2.addItem(new CartItem(products.get(1),3));

        orders.add(new Order("5490", new Date(), StatusType.SHIPPED, true, PaymentType.CARD,
                null,
                cart2));

        orders.add(new Order("5491", new Date(), StatusType.NEW, true, PaymentType.CARD,
                null,
                cart2));




        OrderAdapter adapter = new OrderAdapter(orders, OrderAdapterTypes.ASSIGNED_ORDERS_ADAPTER , order -> {
            Intent intent = new Intent(AssignedOrdersToPrepareActivity.this,
                    gr.softeng.team21.view.order.orderDetails.OrderDetailsActivity.class);

            // 1. Κωδικός Παραγγελίας
            intent.putExtra("ORDER_CODE", order.getOrdercode());

            // 2. Όνομα Πελάτη (Συνένωση ονόματος και επιθέτου)
            String customerName = order.getShoppingCart().getCustomer().getFirstname() + " "
                    + order.getShoppingCart().getCustomer().getLastname();
            intent.putExtra("CUSTOMER_NAME", customerName);

            // 3. Ημερομηνία (ως String)
            intent.putExtra("ORDER_DATE", order.getSubmissiondate().toString());

            // 4. Συνολική Τιμή
            // Υπολογισμός του συνόλου επιτόπου (αθροίζοντας τα items)
            double totalAmount = 0.0;
            for (gr.softeng.team21.domain.CartItem item : order.getShoppingCart().getItems()) {
                // Υποθέτουμε ότι το Money επιστρέφει BigDecimal, το κάνουμε double
                double price = item.getProductType().getPrice().getAmount().doubleValue();
                totalAmount += price * item.getQuantity();
            }
            intent.putExtra("ORDER_TOTAL", totalAmount);

            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

    }
}