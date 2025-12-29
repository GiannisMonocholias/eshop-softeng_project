package gr.softeng.team21.view.employee.customerServiceEmployee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team21.R;
import gr.softeng.team21.domain.EmailAddress;
import gr.softeng.team21.domain.EmailMessage;
import gr.softeng.team21.view.user.EmailCompositionActivity;
import gr.softeng.team21.view.contact.EmailDetailsActivity;
import gr.softeng.team21.view.util.EmailAdapter;

public class EmailListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerViewEmails), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // 1. Βρίσκουμε το RecyclerView από το XML
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEmails);

        // 2. Του λέμε να τα δείχνει σε λίστα (το ένα κάτω από το άλλο)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Φτιάχνουμε ψεύτικα δεδομένα για δοκιμή (αφού δεν έχουμε ακόμα Database)
        List<EmailMessage> fakeEmails = new ArrayList<>();
        EmailAddress customer1 = new EmailAddress("Juancho@gmail.com");
        EmailAddress customer2 = new EmailAddress("KennethFaried@yahoo.gr");
        EmailAddress customer3 = new EmailAddress("KendrickNunn@outlook.com");
        EmailAddress customer4 = new EmailAddress("KostasSloukαs@upatras.gr");

        fakeEmails.add(new EmailMessage(customer1,customer2,
                "Πρόβλημα με την RTX 4060",
                "Καλησπέρα, παρέλαβα την κάρτα γραφικών χθες, αλλά μόλις ανοίγω βαρύ παιχνίδι οι ανεμιστήρες κάνουν έναν περίεργο θόρυβο και η οθόνη μαυρίζει. Καλύπτεται από DOA;"));

        fakeEmails.add(new EmailMessage(customer3,customer1,
                "Συμβατότητα RAM με Motherboard",
                "Γεια σας, θέλω να αγοράσω τις μνήμες Corsair Vengeance DDR5. Είναι συμβατές με τη μητρική MSI Z790 που έχετε στο site ή χρειάζομαι DDR4;"));

        fakeEmails.add(new EmailMessage(customer4,customer3,
                "Παραγγελία #55921 - Laptop Dell XPS",
                "Η παραγγελία μου φαίνεται 'Σε επεξεργασία' εδώ και 4 μέρες. Το χρειάζομαι επειγόντως για τη δουλειά μου. Πότε θα γίνει η αποστολή;"));

        fakeEmails.add(new EmailMessage(customer4,customer2,
                "Επιστροφή Monitor λόγω Dead Pixels",
                "Αγόρασα την οθόνη LG 27'' και παρατήρησα 2 καμένα pixels στο κέντρο της οθόνης. Ποια είναι η διαδικασία για αντικατάσταση;"));

        fakeEmails.add(new EmailMessage(customer2,customer4,
                "Ζήτηση Προσφοράς για 5 Workstations",
                "Καλημέρα, ενδιαφερόμαστε για εξοπλισμό του νέου μας γραφείου. Μπορείτε να μας στείλετε προσφορά για 5 πύργους με i7 επεξεργαστή και 32GB RAM;"));

        fakeEmails.add(new EmailMessage(customer1,customer2,
                "Πρόβλημα με την RTX 4060",
                "Καλησπέρα, παρέλαβα την κάρτα γραφικών χθες, αλλά μόλις ανοίγω βαρύ παιχνίδι οι ανεμιστήρες κάνουν έναν περίεργο θόρυβο και η οθόνη μαυρίζει. Καλύπτεται από DOA;"));


        // 4. Φτιάχνουμε τον Adapter και τον συνδέουμε
        EmailAdapter adapter = new EmailAdapter(fakeEmails, email -> {
//            // Αυτό τρέχει όταν πατάς πάνω σε ένα email
//            Toast.makeText(this, "Επέλεξες: " + email.getSubject(), Toast.LENGTH_SHORT).show();
//            // Εδώ αργότερα θα ανοίγεις την οθόνη απάντησης (Reply)


                Intent intent = new Intent(EmailListActivity.this, EmailDetailsActivity.class);

                // Στέλνουμε τα 3 βασικά κείμενα ξεχωριστά
                intent.putExtra("EXTRA_SUBJECT", email.getSubject());
                intent.putExtra("EXTRA_BODY", email.getBody());

                // Προσοχή: Επειδή ο Sender είναι αντικείμενο, τον μετατρέπουμε σε String
                intent.putExtra("EXTRA_SENDER", email.getFrom().toString());

                startActivity(intent);
        });

        // 5. Βάζουμε τον Adapter στο RecyclerView
        recyclerView.setAdapter(adapter);

        // 6. Ρυθμίζουμε το κουμπί Νέου Μηνύματος (FAB)
        FloatingActionButton fab = findViewById(R.id.fabNewEmail);
        fab.setOnClickListener(v -> {
            Toast.makeText(this, "Δημιουργία Νέου Μηνύματος...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EmailListActivity.this, EmailCompositionActivity.class);

            startActivity(intent);
        });
    }
}