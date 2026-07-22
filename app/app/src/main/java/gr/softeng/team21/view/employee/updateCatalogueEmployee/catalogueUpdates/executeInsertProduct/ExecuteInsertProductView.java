package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

/**
 * View contract for the product insertion execution screen.
 * Defines methods for retrieving user input from form fields and providing
 * asynchronous validation feedback and process status messages.
 * @author Γιάννης Μονοχολιάς
 */
public interface ExecuteInsertProductView {

    /**
     * Retrieves the product code entered by the user.
     * @return The product's code string.
     */
    String getProductCode();

    /**
     * Retrieves the product name entered by the user.
     * @return The product's name string.
     */
    String getProductName();

    /**
     * Retrieves the product price entered by the user.
     * @return The product's price string.
     */
    String getProductPrice();

    /**
     * Retrieves the product description entered by the user.
     * @return The product's description string.
     */
    String getProductDescription();

    /**
     * Displays the original request description provided by the Admin asynchronously.
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