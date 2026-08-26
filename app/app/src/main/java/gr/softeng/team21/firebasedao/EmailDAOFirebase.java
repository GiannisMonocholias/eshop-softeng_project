package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;

/**
 * Cloud Firestore implementation of the {@link EmailDAO} interface.
 * Handles the asynchronous reading and writing of email messages to Firestore collections.
 */
public class EmailDAOFirebase implements EmailDAO {

    private final CollectionReference inboxRef;
    private final CollectionReference sentRef;

    /**
     * Default constructor initializing the Firestore database references
     * for both the inbox and sent email collections.
     */
    public EmailDAOFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        inboxRef = db.collection("emails_inbox");
        sentRef = db.collection("emails_sent");
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getInboxEmails() {
        return fetchEmailsFromCollection(inboxRef);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getSentEmails() {
        return fetchEmailsFromCollection(sentRef);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getUnreadEmails() {
        return getInboxEmails().thenApply(emails -> {
            ArrayList<EmailMessage> unread = new ArrayList<>();
            for (EmailMessage msg : emails) {
                if (!msg.isRead()) unread.add(msg);
            }
            return unread;
        });
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getReadEmails() {
        return getInboxEmails().thenApply(emails -> {
            ArrayList<EmailMessage> read = new ArrayList<>();
            for (EmailMessage msg : emails) {
                if (msg.isRead()) read.add(msg);
            }
            return read;
        });
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getUnrepliedEmails() {
        return getInboxEmails().thenApply(emails -> {
            ArrayList<EmailMessage> unreplied = new ArrayList<>();
            for (EmailMessage msg : emails) {
                if (!msg.isReplied()) unreplied.add(msg);
            }
            return unreplied;
        });
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getRepliedEmails() {
        return getInboxEmails().thenApply(emails -> {
            ArrayList<EmailMessage> replied = new ArrayList<>();
            for (EmailMessage msg : emails) {
                if (msg.isReplied()) replied.add(msg);
            }
            return replied;
        });
    }

    /** {@inheritDoc} */
    @Override
    public void saveInboxEmails(EmailMessage msg) {
        // Firestore creates a unique document ID automatically using .add()
        inboxRef.add(msg);
    }

    /** {@inheritDoc} */
    @Override
    public void saveSentEmails(EmailMessage msg) {
        sentRef.add(msg);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Boolean> inInbox(EmailMessage msg) {
        return getInboxEmails().thenApply(emails -> emails.contains(msg));
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Boolean> inSent(EmailMessage msg) {
        return getSentEmails().thenApply(emails -> emails.contains(msg));
    }

    /**
     * Helper method to fetch a list of emails from a specific Firestore collection asynchronously.
     *
     * @param collectionRef The CollectionReference to fetch data from.
     * @return A CompletableFuture that completes with the fetched list of EmailMessages.
     */
    private CompletableFuture<ArrayList<EmailMessage>> fetchEmailsFromCollection(CollectionReference collectionRef) {
        CompletableFuture<ArrayList<EmailMessage>> future = new CompletableFuture<>();

        collectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<EmailMessage> emails = new ArrayList<>();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                EmailMessage email = document.toObject(EmailMessage.class);
                if (email != null) {
                    emails.add(email);
                }
            }
            future.complete(emails);
        }).addOnFailureListener(e -> {
            future.completeExceptionally(e);
        });

        return future;
    }
}