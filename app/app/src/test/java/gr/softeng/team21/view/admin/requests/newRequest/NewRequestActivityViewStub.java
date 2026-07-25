package gr.softeng.team21.view.admin.requests.newRequest;

import gr.softeng.team21.view.admin.requests.NewRequestActivityView;

/**
 * Stub for NewRequestActivityView used in unit testing.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class NewRequestActivityViewStub implements NewRequestActivityView {
    private String successMessage;
    private String errorMessage;

    @Override
    public void showSuccessAndClose(String message) { this.successMessage = message; }

    @Override
    public void showError(String message) { this.errorMessage = message; }

    public String getSuccessMessage() { return successMessage; }
    public String getErrorMessage() { return errorMessage; }
}