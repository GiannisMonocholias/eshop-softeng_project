package gr.softeng.team21.memorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;

/**
 * In-memory implementation of the {@link EmailDAO} interface.
 * Employs a custom Hash-based Index structure to simulate Firebase's O(1) query performance
 * for retrieving emails by recipient, entirely eliminating linear list scanning.
 *
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDAOMemory implements EmailDAO {

    private static EmailDAOMemory instance;

    // Primary Storage: O(1) lookups and updates by ID
    private final HashMap<String, EmailMessage> emailsById = new HashMap<>();

    // Index Structure: O(1) retrievals by recipient email address (Simulating Firestore Index)
    private final HashMap<String, ArrayList<EmailMessage>> indexByReceiver = new HashMap<>();

    private int autoIncrementCounter = 1;

    private EmailDAOMemory() {}

    public static EmailDAOMemory getInstance() {
        if (instance == null) {
            instance = new EmailDAOMemory();
        }
        return instance;
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getEmailsForUser(String receiverEmailAddress) {
        // O(1) Indexed Retrieval
        ArrayList<EmailMessage> userEmails = indexByReceiver.getOrDefault(receiverEmailAddress, new ArrayList<>());
        // return a copy for safety
        return CompletableFuture.completedFuture(new ArrayList<>(userEmails));
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> saveEmail(EmailMessage msg) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (msg == null || msg.getTo() == null) {
            future.completeExceptionally(new IllegalArgumentException("Invalid EmailMessage"));
            return future;
        }

        // Generate unique Document ID simulation
        String docId = "MEM-EMAIL-" + (autoIncrementCounter++);
        msg.setEmailId(docId);

        // Save to primary storage
        emailsById.put(docId, msg);

        // Update the Receiver Index
        String receiverEmail = msg.getTo().getAddress();
        indexByReceiver.putIfAbsent(receiverEmail, new ArrayList<>());
        indexByReceiver.get(receiverEmail).add(msg);

        future.complete(null);
        return future;
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> updateEmail(EmailMessage msg) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (msg == null || msg.getEmailId() == null || !emailsById.containsKey(msg.getEmailId())) {
            future.completeExceptionally(new IllegalArgumentException("Email does not exist or missing ID"));
            return future;
        }


        emailsById.put(msg.getEmailId(), msg);

        future.complete(null);
        return future;
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> clear() {
        emailsById.clear();
        indexByReceiver.clear();
        autoIncrementCounter = 1;
        return CompletableFuture.completedFuture(null);
    }
}