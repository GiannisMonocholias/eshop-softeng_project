package gr.softeng.team21.memorydao;

import java.util.ArrayList;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;

/**
 * In-memory implementation of the {@link EmailDAO} interface.
 * Stores and categorizes incoming and outgoing email messages in local lists.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDAOMemory implements EmailDAO {
    private ArrayList<EmailMessage> inboxEmails = new ArrayList<EmailMessage>();
    private ArrayList<EmailMessage> sentEmails = new ArrayList<EmailMessage>();

    /**
     * @return all emails currently in the inbox.
     */
    public ArrayList<EmailMessage> getInboxEmails() { return inboxEmails; }

    /**
     * @return all emails that have been sent.
     */
    public ArrayList<EmailMessage> getSentEmails() { return sentEmails; }

    /**
     * Filters and returns all emails in the inbox that have not been read.
     * @return a list of unread EmailMessages.
     */
    public ArrayList<EmailMessage> getUnreadEmails() {
        ArrayList<EmailMessage> unread = new ArrayList<EmailMessage>();
        for(EmailMessage msg : inboxEmails) {
            if(!msg.isRead()) unread.add(msg);
        }
        return unread;
    }

    /**
     * Filters and returns all emails in the inbox that have been read.
     * @return a list of read EmailMessages.
     */
    public ArrayList<EmailMessage> getReadEmails() {
        ArrayList<EmailMessage> read = new ArrayList<EmailMessage>();
        for (EmailMessage msg : inboxEmails) {
            if (msg.isRead()) read.add(msg);
        }
        return read;
    }

    /**
     * Filters and returns all emails that have not received a reply yet.
     * @return a list of unreplied EmailMessages.
     */
    public ArrayList<EmailMessage> getUnrepliedEmails() {
        ArrayList<EmailMessage> unreplied = new ArrayList<EmailMessage>();
        for(EmailMessage msg : inboxEmails) {
            if(!msg.isReplied()) unreplied.add(msg);
        }
        return unreplied;
    }

    /**
     * Filters and returns all emails that have been replied to.
     * @return a list of replied EmailMessages.
     */
    public ArrayList<EmailMessage> getRepliedEmails() {
        ArrayList<EmailMessage> replied = new ArrayList<EmailMessage>();
        for (EmailMessage msg : inboxEmails) {
            if (msg.isReplied()) replied.add(msg);
        }
        return replied;
    }

    /**
     * Saves a new message to the inbox storage.
     * @param msg the message to save.
     */
    public void saveInboxEmails(EmailMessage msg) {
        inboxEmails.add(msg);
    }

    /**
     * Saves a new message to the sent messages storage.
     * @param msg the message to save.
     */
    public void saveSentEmails(EmailMessage msg) {
        sentEmails.add(msg);
    }

    /**
     * Checks if a specific message exists in the inbox.
     * @param msg the message to search for.
     * @return true if found, false otherwise.
     */
    public boolean inInbox(EmailMessage msg) {
        return inboxEmails.contains(msg);
    }

    /**
     * Checks if a specific message exists in the sent items.
     * @param msg the message to search for.
     * @return true if found, false otherwise.
     */
    public boolean inSent(EmailMessage msg) {
        return sentEmails.contains(msg);
    }
}