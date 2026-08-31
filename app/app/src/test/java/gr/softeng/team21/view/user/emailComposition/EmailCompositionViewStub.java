package gr.softeng.team21.view.user.emailComposition;

import java.util.HashMap;
import java.util.Map;

/**
 * A stub implementation of the {@link EmailCompositionView} interface for unit testing.
 * Captures form states, input errors, and success/error notifications.
 *
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

    // Αποθήκευση των errors ανά πεδίο
    private final Map<String, String> inputErrors = new HashMap<>();

    public void setRecipientEmailInput(String recipientEmailInput) {
        this.recipientEmailInput = recipientEmailInput;
    }

    public void setSubjectInput(String subjectInput) {
        this.subjectInput = subjectInput;
    }

    public void setBodyInput(String bodyInput) {
        this.bodyInput = bodyInput;
    }

    @Override
    public String getRecipientEmail() { return recipientEmailInput; }

    @Override
    public String getSubject() { return subjectInput; }

    @Override
    public String getBody() { return bodyInput; }

    @Override
    public void setSenderDetails(String name, String email) {
        this.displayedSenderName = name;
        this.displayedSenderEmail = email;
    }

    @Override
    public void showInputError(String field, String message) {
        inputErrors.put(field, message);
    }

    @Override
    public void showErrorMessage(String message) {
        this.errorMessage = message;
    }

    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

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

    /**
     * Retrieves the mapped error string for a given field identifier.
     */
    public String getInputError(String field) {
        return inputErrors.get(field);
    }
}