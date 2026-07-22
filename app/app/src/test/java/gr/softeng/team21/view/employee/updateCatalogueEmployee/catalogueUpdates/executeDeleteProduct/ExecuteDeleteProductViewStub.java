package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

/**
 * A stub implementation of the {@link ExecuteDeleteProductView} interface for unit testing.
 * It simulates the UI for deleting a product asynchronously, allowing verification of the displayed
 * product metadata and the state of confirmation dialogs and feedback messages.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteDeleteProductViewStub implements ExecuteDeleteProductView {

    private String name, code, description, price;
    private boolean confirmationDialogShown = false;
    private String successMessage = "";
    private String errorMessage = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProductDetails(String name, String code, String description, String price) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.price = price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showConfirmationDialog() {
        this.confirmationDialogShown = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    /**
     * {@inheritDoc}
     */
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