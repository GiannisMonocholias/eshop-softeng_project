package gr.softeng.team21.memorydao;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;

/**
 * In-memory implementation of the {@link EmailDAO} interface.
 * Stores incoming and outgoing email messages in local lists.
 * Wraps results in {@link CompletableFuture} to satisfy the asynchronous contract
 * of the DAO, making it perfect for instantaneous Unit Testing without network delays.
 *
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDAOMemory implements EmailDAO {
    private ArrayList<EmailMessage> inboxEmails = new ArrayList<>();
    private ArrayList<EmailMessage> sentEmails = new ArrayList<>();

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getInboxEmails() {
        return CompletableFuture.completedFuture(inboxEmails);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getSentEmails() {
        return CompletableFuture.completedFuture(sentEmails);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getUnreadEmails() {
        ArrayList<EmailMessage> unread = new ArrayList<>();
        for(EmailMessage msg : inboxEmails) {
            if(!msg.isRead()) unread.add(msg);
        }
        return CompletableFuture.completedFuture(unread);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getReadEmails() {
        ArrayList<EmailMessage> read = new ArrayList<>();
        for (EmailMessage msg : inboxEmails) {
            if (msg.isRead()) read.add(msg);
        }
        return CompletableFuture.completedFuture(read);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getUnrepliedEmails() {
        ArrayList<EmailMessage> unreplied = new ArrayList<>();
        for(EmailMessage msg : inboxEmails) {
            if(!msg.isReplied()) unreplied.add(msg);
        }
        return CompletableFuture.completedFuture(unreplied);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<ArrayList<EmailMessage>> getRepliedEmails() {
        ArrayList<EmailMessage> replied = new ArrayList<>();
        for (EmailMessage msg : inboxEmails) {
            if (msg.isReplied()) replied.add(msg);
        }
        return CompletableFuture.completedFuture(replied);
    }


    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> saveInboxEmails(EmailMessage msg) {
        inboxEmails.add(msg);
        return CompletableFuture.completedFuture(null);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Void> saveSentEmails(EmailMessage msg) {
        sentEmails.add(msg);
        return CompletableFuture.completedFuture(null);
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Boolean> inInbox(EmailMessage msg) {
        return CompletableFuture.completedFuture(inboxEmails.contains(msg));
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<Boolean> inSent(EmailMessage msg) {
        return CompletableFuture.completedFuture(sentEmails.contains(msg));
    }
}