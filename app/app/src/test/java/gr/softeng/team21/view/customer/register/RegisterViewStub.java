package gr.softeng.team21.view.customer.register;

/**
 * A stub implementation of the {@link RegisterView} interface for unit testing.
 * It provides a mechanism to capture UI feedback (success/error messages) and
 * track the state of UI actions (clearing fields) during the registration flow.
 * @author Γιάννης Μονοχολιάς
 */
public class RegisterViewStub implements RegisterView {

    private String successMessage = "";
    private String errorMessage = "";
    private boolean inputsCleared = false;

    /**
     * Captures the success message sent by the presenter.
     * @param message The confirmation message to be displayed.
     */
    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    /**
     * Captures the error message sent by the presenter.
     * @param message The error feedback to be displayed.
     */
    @Override
    public void showErrorMessage(String message) {
        this.errorMessage = message;
    }

    /**
     * Marks the input clearing action as performed.
     */
    @Override
    public void clearInputFields() {
        this.inputsCleared = true;
    }

    // --- Accessor methods for verification during assertions ---

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean areInputsCleared() {
        return inputsCleared;
    }
}