package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;

/**
 * Cloud Firestore implementation of the {@link EmailDAO} interface.
 * Utilizes a single "emails" collection and leverages Firestore's automatic single-field indexes
 * for highly efficient O(1) filtered querying via whereEqualTo.
 *
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDAOFirebase implements EmailDAO {

    private final CollectionReference emailsRef;

    /**
     * Initializes the Firestore database reference for the centralized emails collection.
     */
    public EmailDAOFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        emailsRef = db.collection("emails");
    }

    /**
     * {@inheritDoc}
     * Queries the automatic Firestore index for the field "to.address".
     */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getEmailsForUser(String receiverEmailAddress) {
        CompletableFuture<ArrayList<EmailMessage>> future = new CompletableFuture<>();

        emailsRef.whereEqualTo("to.address", receiverEmailAddress)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<EmailMessage> emails = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        EmailMessage email = document.toObject(EmailMessage.class);
                        if (email != null) {
                            // Injecting the Document ID into the domain object for future updates
                            email.setEmailId(document.getId());
                            emails.add(email);
                        }
                    }
                    future.complete(emails);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> saveEmail(EmailMessage msg) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Create an empty Document to get the automatic ID before we store it
        DocumentReference newDocRef = emailsRef.document();
        msg.setEmailId(newDocRef.getId());

        newDocRef.set(msg)
                .addOnSuccessListener(aVoid -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> updateEmail(EmailMessage msg) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (msg.getEmailId() == null || msg.getEmailId().isEmpty()) {
            future.completeExceptionally(new IllegalArgumentException("Cannot update email without a valid emailId."));
            return future;
        }

        // Overwrites the existing document with the updated object state (e.g., isRead = true)
        emailsRef.document(msg.getEmailId()).set(msg)
                .addOnSuccessListener(aVoid -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> clear() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeExceptionally(new UnsupportedOperationException("Bulk delete requires Cloud Functions."));
        return future;
    }
}