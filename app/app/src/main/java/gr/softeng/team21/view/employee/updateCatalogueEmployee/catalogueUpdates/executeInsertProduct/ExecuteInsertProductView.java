package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

/**
 * View contract for the product insertion execution screen.
 * Defines methods for retrieving user input from form fields and providing
 * validation feedback and process status messages.
 * @author Γιάννης Μονοχολιάς
 */
public interface ExecuteInsertProductView {

    // Getters for Presenter data retrieval

    /**
     * @return product's code
     */
    String getProductCode();

    /**
     * @return product's name
     */
    String getProductName();

    /**
     * @return product's price
     */
    String getProductPrice();

    /**
     * @return product's description
     */
    String getProductDescription();

    /**
     * Displays the original request description provided by the Admin.
     * @param description The instruction text for the update.
     */
    void setRequestDescription(String description);

    /**
     * Highlights validation errors on specific input fields.
     * @param field   The name of the field (e.g., "price").
     * @param message The error message to display.
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
}