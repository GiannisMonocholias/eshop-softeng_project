package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

/**
 * A stub implementation of the {@link ExecuteDeleteProductView} interface for unit testing.
 * It simulates the UI for deleting a product, allowing verification of the displayed
 * product metadata and the state of confirmation dialogs and feedback messages.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteDeleteProductViewStub implements ExecuteDeleteProductView {

    private String name, code, description, price;
    private boolean confirmationDialogShown = false;
    private String successMessage = "";
    private String errorMessage = "";

    /**
     * Captures product details passed by the presenter for display verification.
     */
    @Override
    public void setProductDetails(String name, String code, String description, String price) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.price = price;
    }

    /**
     * Marks that the deletion confirmation dialog was requested to be shown.
     */
    @Override
    public void showConfirmationDialog() {
        this.confirmationDialogShown = true;
    }

    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    // --- Accessor methods for verification during testing ---

    public String getName() { return name; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }

    public boolean isConfirmationDialogShown() { return confirmationDialogShown; }
    public String getSuccessMessage() { return successMessage; }
    public String getErrorMessage() { return errorMessage; }
}