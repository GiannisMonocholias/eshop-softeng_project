package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

/**
 * View interface contract for the product modification (process) screen.
 * Defines methods for data binding, retrieving user input from form fields,
 * and managing asynchronous validation and confirmation UI workflows.
 * @author Γιάννης Μονοχολιάς
 */
public interface ExecuteProcessProductView {

    /**
     * Retrieves the product code currently entered in the input field.
     * @return The current product code string.
     */
    String getProductCode();

    /**
     * Retrieves the product name currently entered in the input field.
     * @return The current product name string.
     */
    String getProductName();

    /**
     * Retrieves the product price currently entered in the input field.
     * @return The current product price string.
     */
    String getProductPrice();

    /**
     * Retrieves the product description currently entered in the input field.
     * @return The current product description string.
     */
    String getProductDescription();

    /**
     * Pre-fills the form fields with the existing product data asynchronously upon load.
     * @param code The existing product code to display.
     * @param name The existing product name to display.
     * @param price The existing price value to display.
     * @param description The existing product description to display.
     */
    void setProductData(String code, String name, String price, String description);

    /**
     * Displays the administrator's instructions for the modification request.
     * @param description The update request details text.
     */
    void setRequestDescription(String description);

    /**
     * Highlights a validation error on a specific input field (e.g., non-numeric prices).
     * @param field   The target UI field name identifier.
     * @param message The specific error message to present to the user.
     */
    void showInputError(String field, String message);

    /**
     * Displays a success alert indicating the changes were saved, followed by terminating the activity.
     * @param message The success description text.
     */
    void showSuccessMessage(String message);

    /**
     * Displays a general error alert dialog for system or database failures.
     * @param message The error description text.
     */
    void showError(String message);

    /**
     * Shows a confirmation dialog ensuring the user intends to apply the changes to the catalogue.
     */
    void showConfirmationDialog();
}