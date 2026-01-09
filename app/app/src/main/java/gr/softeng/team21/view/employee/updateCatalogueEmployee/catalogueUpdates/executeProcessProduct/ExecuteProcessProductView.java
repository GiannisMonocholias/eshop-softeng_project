package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

/**
 * Defines methods for data binding, retrieving user input from form fields,
 * and managing validation/confirmation workflows.
 * @author Γιάννης Μονοχολιάς
 */
public interface ExecuteProcessProductView {

    /**
     * @return product code input
     */
    String getProductCode();

    /**
     * @return product name input
     */
    String getProductName();

    /**
     * @return product price input
     */
    String getProductPrice();

    /**
     * @return product description input
     */
    String getProductDescription();

    /**
     * Fills the form fields with the current product data.
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