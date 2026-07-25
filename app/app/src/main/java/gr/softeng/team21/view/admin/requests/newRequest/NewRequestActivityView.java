package gr.softeng.team21.view.admin.requests;

/**
 * Interface defining the UI operations for the New Request creation screen.
 * Handles the display of success or error messages resulting from the submission process.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public interface NewRequestActivityView {

    /**
     * Displays a success message to the user and gracefully closes the current screen.
     * @param message The success message to display.
     */
    void showSuccessAndClose(String message);

    /**
     * Displays an error message to the user (e.g., validation failure or missing product).
     * @param message The specific error description.
     */
    void showError(String message);
}