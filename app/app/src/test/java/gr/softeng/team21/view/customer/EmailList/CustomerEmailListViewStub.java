package gr.softeng.team21.view.customer.EmailList;

/**
 * Stub implementation of {@link CustomerEmailListView} for testing purposes.
 * It provides a mechanism to capture navigation events and the data passed during navigation
 * (such as email details or customer IDs) to verify the presenter's logic.
 * @author PAVLOS GRATSANIS
 */
public class CustomerEmailListViewStub implements CustomerEmailListView {

    private int CreateNewMsgCount = 0;
    private String passedCustomerId = "";

    private int EmailDetailsCount = 0;
    private String detailsSubject, detailsBody, detailsSender, detailsReceiver, detailsId;

    /**
     * {@inheritDoc}
     * Increments the counter for creating a new message and stores the passed customer ID.
     */
    @Override
    public void goToCreateNewMessge(String customerId) {
        CreateNewMsgCount++;
        this.passedCustomerId = customerId;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for viewing email details and stores all passed email attributes.
     */
    @Override
    public void goToEmailDetails(String subject, String body, String sender, String receiver, String customerId) {
        EmailDetailsCount++;
        this.detailsSubject = subject;
        this.detailsBody = body;
        this.detailsSender = sender;
        this.detailsReceiver = receiver;
        this.detailsId = customerId;
    }


    /**
     * Returns the number of times navigation to create a new message was triggered.
     * @return The count.
     */
    public int getCreateNewMsgCount() {
        return CreateNewMsgCount;
    }

    /**
     * Returns the customer ID passed when navigating to create a new message.
     * @return The customer ID string.
     */
    public String getPassedCustomerId() {
        return passedCustomerId;
    }

    /**
     * Returns the number of times navigation to email details was triggered.
     * @return The count.
     */
    public int getEmailDetailsCount() {
        return EmailDetailsCount;
    }

    /**
     * Returns the subject passed to the email details.
     * @return The subject string.
     */
    public String getDetailsSubject() {
        return detailsSubject;
    }

    /**
     * Returns the body passed to the email details.
     * @return The body string.
     */
    public String getDetailsBody() {
        return detailsBody;
    }

    /**
     * Returns the sender passed to the email details.
     * @return The sender string.
     */
    public String getDetailsSender() {
        return detailsSender;
    }

    /**
     * Returns the receiver passed to the email details.
     * @return The receiver string.
     */
    public String getDetailsReceiver() {
        return detailsReceiver;
    }

    /**
     * Returns the customer ID passed to the email details.
     * @return The customer ID string.
     */
    public String getDetailsId() {
        return detailsId;
    }
}