package gr.softeng.team21.view.customer.EmailList;

import java.util.ArrayList;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Stub implementation of {@link CustomerEmailListView} for testing purposes.
 * Captures asynchronous email loads and navigation events.
 * @author PAVLOS GRATSANIS
 */
public class CustomerEmailListViewStub implements CustomerEmailListView {

    private int createNewMsgCount = 0;
    private String passedCustomerId = "";
    private int emailDetailsCount = 0;
    private String detailsSubject, detailsBody, detailsSender, detailsReceiver, detailsId;

    private ArrayList<EmailMessage> loadedEmails;
    private String errorMessage;

    @Override
    public void showEmails(ArrayList<EmailMessage> emails) {
        this.loadedEmails = emails;
    }

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    @Override
    public void goToCreateNewMessge(String customerId) {
        createNewMsgCount++;
        this.passedCustomerId = customerId;
    }

    @Override
    public void goToEmailDetails(String subject, String body, String sender, String receiver, String customerId) {
        emailDetailsCount++;
        this.detailsSubject = subject;
        this.detailsBody = body;
        this.detailsSender = sender;
        this.detailsReceiver = receiver;
        this.detailsId = customerId;
    }

    // --- Getters for Tests ---
    public ArrayList<EmailMessage> getLoadedEmails() { return loadedEmails; }
    public String getErrorMessage() { return errorMessage; }
    public int getCreateNewMsgCount() { return createNewMsgCount; }
    public String getPassedCustomerId() { return passedCustomerId; }
    public int getEmailDetailsCount() { return emailDetailsCount; }
    public String getDetailsSubject() { return detailsSubject; }
    public String getDetailsBody() { return detailsBody; }
    public String getDetailsSender() { return detailsSender; }
    public String getDetailsReceiver() { return detailsReceiver; }
    public String getDetailsId() { return detailsId; }
}