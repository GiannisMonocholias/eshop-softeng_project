package gr.softeng.team21.view.user.emailComposition;

/**
 * A stub implementation of the {@link EmailCompositionView} interface for unit testing.
 * It simulates the user interface for composing an email, allowing tests to set
 * input values and verify output messages or view state changes.
 * @author Γιάννης Μονοχολιάς
 */
public class EmailCompositionViewStub implements EmailCompositionView {

    private String recipientEmailInput = "";
    private String subjectInput = "";
    private String bodyInput = "";

    private String displayedSenderName = "";
    private String displayedSenderEmail = "";
    private String errorMessage = "";
    private String successMessage = "";
    private boolean finishActivityCalled = false;

    /**
     * Simulates user input for the recipient's email address.
     * @param recipientEmailInput The email address entered by the user.
     */
    public void setRecipientEmailInput(String recipientEmailInput) {
        this.recipientEmailInput = recipientEmailInput;
    }

    /**
     * Simulates user input for the email subject.
     * @param subjectInput The subject entered by the user.
     */
    public void setSubjectInput(String subjectInput) {
        this.subjectInput = subjectInput;
    }

    /**
     * Simulates user input for the email body content.
     * @param bodyInput The message body entered by the user.
     */
    public void setBodyInput(String bodyInput) {
        this.bodyInput = bodyInput;
    }

    @Override
    public String getRecipientEmail() {
        return recipientEmailInput;
    }

    @Override
    public String getSubject() {
        return subjectInput;
    }

    @Override
    public String getBody() {
        return bodyInput;
    }

    /**
     * Captures the sender's details displayed on the UI.
     */
    @Override
    public void setSenderDetails(String name, String email) {
        this.displayedSenderName = name;
        this.displayedSenderEmail = email;
    }

    @Override
    public void showErrorMessage(String message) {
        this.errorMessage = message;
    }

    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    /**
     * Captures whether the view was requested to close/finish.
     */
    @Override
    public void finishActivity() {
        this.finishActivityCalled = true;
    }

    // --- Accessor methods for verification during assertions ---

    public String getDisplayedSenderName() { return displayedSenderName; }
    public String getDisplayedSenderEmail() { return displayedSenderEmail; }
    public String getErrorMessage() { return errorMessage; }
    public String getSuccessMessage() { return successMessage; }
    public boolean isFinishActivityCalled() { return finishActivityCalled; }
}