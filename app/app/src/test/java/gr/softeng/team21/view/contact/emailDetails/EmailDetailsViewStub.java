package gr.softeng.team21.view.contact.emailDetails;

/**
 * A stub implementation of the {@link EmailDetailsView} interface for unit testing.
 * It captures and stores data passed by the presenter to allow verification
 * of the UI logic without an actual Android Activity.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailDetailsViewStub implements EmailDetailsView {

    private String displayedSubject = "";
    private String displayedSenderName = "";
    private String displayedReceiverName = "";
    private String displayedBody = "";
    private String displayedSenderEmail = "";
    private String displayedReceiverEmail = "";

    /**
     * Captures the subject string for verification.
     * @param subject The email subject.
     */
    @Override
    public void displaySubject(String subject) {
        this.displayedSubject = subject;
    }

    /**
     * Captures the formatted sender name for verification.
     * @param sender The sender's name with role suffix.
     */
    @Override
    public void displaySenderName(String sender) {
        this.displayedSenderName = sender;
    }

    /**
     * Captures the receiver name for verification.
     * @param receiver The receiver's name.
     */
    @Override
    public void displayReceiverName(String receiver) {
        this.displayedReceiverName = receiver;
    }

    /**
     * Captures the message body for verification.
     * @param body The email message body.
     */
    @Override
    public void displayBody(String body) {
        this.displayedBody = body;
    }

    /**
     * Captures the sender email address for verification.
     * @param email The sender's email.
     */
    @Override
    public void displaySenderEmail(String email) {
        this.displayedSenderEmail = email;
    }

    /**
     * Captures the receiver email address for verification.
     * @param email The receiver's email.
     */
    @Override
    public void displayReceiverEmail(String email) {
        this.displayedReceiverEmail = email;
    }

    // --- Accessor methods for verification during assertions ---

    public String getDisplayedSubject() { return displayedSubject; }
    public String getDisplayedSenderName() { return displayedSenderName; }
    public String getDisplayedReceiverName() { return displayedReceiverName; }
    public String getDisplayedBody() { return displayedBody; }
    public String getDisplayedSenderEmail() { return displayedSenderEmail; }
    public String getDisplayedReceiverEmail() { return displayedReceiverEmail; }
}