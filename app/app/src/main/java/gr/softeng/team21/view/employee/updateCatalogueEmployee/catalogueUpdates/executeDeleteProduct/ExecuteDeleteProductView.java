package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

/**
 * Defines methods for displaying target product metadata and handling
 * confirmation and feedback dialogs.
 * @author Γιάννης Μονοχολιάς
 */
public interface ExecuteDeleteProductView {
    /**
     * Populates the UI with details of the product intended for deletion.
     * @param name The name of the product.
     * @param code The unique product code.
     * @param description The product description.
     * @param price The formatted price string.
     */
    void setProductDetails(String name, String code, String description, String price);

    /**
     * Displays a confirmation dialog to the user.
     */
    void showConfirmationDialog();

    /**
     * Displays a success alert and handles activity termination.
     * @param message The success message.
     */
    void showSuccessMessage(String message);

    /**
     * Displays an error alert dialog.
     * @param message The error description.
     */
    void showError(String message);
}