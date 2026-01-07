package gr.softeng.team21.view.customer.register;

public class RegisterViewStub implements RegisterView {

    private String successMessage = "";
    private String errorMessage = "";
    private boolean inputsCleared = false;

    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    @Override
    public void showErrorMessage(String message) {
        this.errorMessage = message;
    }

    @Override
    public void clearInputFields() {
        this.inputsCleared = true;
    }


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