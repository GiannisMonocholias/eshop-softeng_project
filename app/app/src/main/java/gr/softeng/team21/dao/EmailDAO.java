package gr.softeng.team21.dao;

import java.util.ArrayList;

import gr.softeng.team21.contact.EmailMessage;

public interface EmailDAO {

    /**
     * @return all emails currently in the inbox.
     */
    public ArrayList<EmailMessage> getInboxEmails();

    /**
     * @return all emails that have been sent.
     */
    ArrayList<EmailMessage> getSentEmails();

    /**
     * Filters and returns all emails in the inbox that have not been read.
     * @return a list of unread EmailMessages.
     */
    ArrayList<EmailMessage> getUnreadEmails();

    /**
     * Filters and returns all emails in the inbox that have been read.
     * @return a list of read EmailMessages.
     */
    ArrayList<EmailMessage> getReadEmails();

    /**
     * Filters and returns all emails that have not received a reply yet.
     * @return a list of unreplied EmailMessages.
     */
    ArrayList<EmailMessage> getUnrepliedEmails();

    /**
     * Filters and returns all emails that have been replied to.
     * @return a list of replied EmailMessages.
     */
    ArrayList<EmailMessage> getRepliedEmails();

    /**
     * Saves a new message to the inbox storage.
     * @param msg the message to save.
     */
    void saveInboxEmails(EmailMessage msg);

    /**
     * Saves a new message to the sent messages storage.
     * @param msg the message to save.
     */
    void saveSentEmails(EmailMessage msg);

    /**
     * Checks if a specific message exists in the inbox.
     * @param msg the message to search for.
     * @return true if found, false otherwise.
     */
    boolean inInbox(EmailMessage msg);

    /**
     * Checks if a specific message exists in the sent items.
     * @param msg the message to search for.
     * @return true if found, false otherwise.
     */
    boolean inSent(EmailMessage msg);

}
