package gr.softeng.team21.view.employee.deliverer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import gr.softeng.team21.domain.Address;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Date;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;
import gr.softeng.team21.domain.ShoppingCart;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.view.util.DelivererOrderAdapter;

public class DelivererOrdersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DelivererOrderAdapter adapter;
    private List<Order> pendingOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deliverer_orders_list);

        // Ρύθμιση για να μην κρύβονται τα στοιχεία πίσω από τα system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.delivererOrdersList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Προετοιμασία Δεδομένων (Mock Data)
        initializeMockData();

        // 2. Σύνδεση RecyclerView
        recyclerView = findViewById(R.id.rvDelivererOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Δημιουργία Adapter & Logic Ολοκλήρωσης
        adapter = new DelivererOrderAdapter(pendingOrders, (order, position) -> {
            // Όταν πατηθεί το "ΟΛΟΚΛΗΡΩΣΗ" από τον Adapter, καλείται αυτό:
            showConfirmationDialog(order, position);
        });

        recyclerView.setAdapter(adapter);
    }

    /**
     * Εμφανίζει παράθυρο επιβεβαίωσης.
     */
    private void showConfirmationDialog(Order order, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση Παράδοσης")
                .setMessage("Επιβεβαιώνετε ότι η παραγγελία #" + order.getOrdercode() + " παραδόθηκε και εξοφλήθηκε;")
                .setPositiveButton("ΝΑΙ", (dialog, which) -> {
                    completeOrderProcess(order, position);
                })
                .setNegativeButton("ΑΚΥΡΟ", null)
                .show();
    }

    /**
     * Εκτελεί την ολοκλήρωση της παραγγελίας.
     */
    private void completeOrderProcess(Order order, int position) {
        // --- 1. Ενημέρωση Domain Model ---
        order.setOrderstatus(OrderStatusType.DELIVERED);
        order.setDeliverydate(new Date()); // Η σημερινή ημερομηνία ως ημερομηνία παράδοσης

        // Σημείωση: Το πεδίο "paid" είναι private στο Order.
        // Αν έχεις setter, χρησιμοποίησέ το: order.setPaid(true);
        // Αλλιώς υποθέτουμε ότι καλύφθηκε από το checkbox της UI λογικής.

        // --- 2. Ενημέρωση UI (Λίστα) ---
        pendingOrders.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, pendingOrders.size());

        Toast.makeText(this, "Η παραγγελία ολοκληρώθηκε!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Δημιουργία ψεύτικων δεδομένων με βάση τους Constructors σου.
     */
    private void initializeMockData() {
        pendingOrders = new ArrayList<>();

        // === ΠΑΡΑΓΓΕΛΙΑ 1 (Σημερινή) ===
        Date today = new Date(); // Custom Domain Date
        EmailAddress email1 = new EmailAddress("georgiou@example.com");

        // Constructor: username, firstname, password, lastname, phone, email, id, registDate
        Customer c1 = new Customer("geo1", "Γιώργος", "pass123", "Γεωργίου",
                "6971234567", email1, "CUST001", today);
        c1.setAddress(new Address("Πανεπιστημίου", "10", "Αθήνα","Ελλάδα", "3241")); // Setter γιατί δεν υπάρχει στον constructor

        ShoppingCart cart1 = new ShoppingCart();
        cart1.setCustomer(c1);

        // Constructor: code, submissionDate, status, paid, paymentMethod, deliveryDate, cart
        Order o1 = new Order("5021", today, OrderStatusType.SHIPPED, false,
                PaymentType.CASH, null, cart1);
        o1.setTotal_amount(new Money(new BigDecimal(45.50), "€"));


        Date yesterday = new Date();
        yesterday.changeDays(-1); // Πάμε μια μέρα πίσω
        EmailAddress email2 = new EmailAddress("maria@example.com");

        Customer c2 = new Customer("mar2", "Μαρία", "secret", "Παπαδοπούλου",
                "6989876543", email2, "CUST002", yesterday);
        c2.setAddress(new Address("Σταδίου", "24", "Αθήνα", "Ελλάδα", "4314"));

        ShoppingCart cart2 = new ShoppingCart();
        cart2.setCustomer(c2);

        // Αυτή έχει πληρωθεί ήδη (π.χ. κάρτα), άρα paid=true
        Order o2 = new Order("5022", yesterday, OrderStatusType.SHIPPED, true,
                PaymentType.CARD, null, cart2);
        o2.setTotal_amount(new Money(new BigDecimal(120.00), "€"));

        // Προσθήκη στη λίστα
        pendingOrders.add(o1);
        pendingOrders.add(o2);
    }
}