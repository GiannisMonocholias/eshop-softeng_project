package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

/**
 * Defines methods for data binding, retrieving user input from form fields,
 * and managing asynchronous validation and confirmation workflows.
 * @author Γιάννης Μονοχολιάς
 */
public interface ExecuteProcessProductView {

    /**
     * Retrieves the product code input.
     * @return The current product code string.
     */
    String getProductCode();

    /**
     * Retrieves the product name input.
     * @return The current product name string.
     */
    String getProductName();

    /**
     * Retrieves the product price input.
     * @return The current product price string.
     */
    String getProductPrice();

    /**
     * Retrieves the product description input.
     * @return The current product description string.
     */
    String getProductDescription();

    /**
     * Fills the form fields with the current product data asynchronously.
     * @param code The existing product code.
     * @param name The existing product name.
     * @param price The existing price value.
     * @param description The existing product description.
     */
    void setProductData(String code, String name, String price, String description);

    /**
     * Displays the Admin's instructions for the modification.
     * @param description The update request details.
     */
    void setRequestDescription(String description);

    /**
     * Displays a validation error on a specific input field.
     * @param field   The target field name.
     * @param message The error message.
     */
    void showInputError(String field, String message);

    /**
     * Displays a success alert and terminates the activity.
     * @param message The success description.
     */
    void showSuccessMessage(String message);

    /**
     * Displays a general error alert dialog.
     * @param message The error description.
     */
    void showError(String message);

    /**
     * Shows a confirmation dialog before applying changes to the catalogue.
     */
    void showConfirmationDialog();
}