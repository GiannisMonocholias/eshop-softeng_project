package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

/**
 * A stub implementation of the {@link ExecuteProcessProductView} interface for unit testing.
 * It simulates the product update form, capturing data input and verifying the
 * display of product details, error messages, and confirmation dialogs.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteProcessProductViewStub implements ExecuteProcessProductView {

    private String codeInput = "";
    private String nameInput = "";
    private String priceInput = "";
    private String descInput = "";

    private String descriptionShown = "";
    private String successMessage = "";
    private String errorMessage = "";
    private String errorField = "";
    private boolean confirmationDialogShown = false;


    public void setCodeInput(String codeInput) { this.codeInput = codeInput; }
    public void setNameInput(String nameInput) { this.nameInput = nameInput; }
    public void setPriceInput(String priceInput) { this.priceInput = priceInput; }
    public void setDescInput(String descInput) { this.descInput = descInput; }

    @Override
    public String getProductCode() { return codeInput; }

    @Override
    public String getProductName() { return nameInput; }

    @Override
    public String getProductPrice() { return priceInput; }

    @Override
    public String getProductDescription() { return descInput; }

    /**
     * Populates the view fields with existing product data for editing.
     */
    @Override
    public void setProductData(String code, String name, String price, String description) {
        this.codeInput = code;
        this.nameInput = name;
        this.priceInput = price;
        this.descInput = description;
    }

    /**
     * Captures the administrative request description.
     */
    @Override
    public void setRequestDescription(String description) {
        this.descriptionShown = description;
    }

    /**
     * Records which input field caused a validation error.
     * @param field The field name (e.g., "price").
     */
    @Override
    public void showInputError(String field, String message) {
        this.errorField = field;
    }

    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    /**
     * Marks that the final confirmation dialog was triggered.
     */
    @Override
    public void showConfirmationDialog() {
        this.confirmationDialogShown = true;
    }

    // --- Accessor methods for verification during testing ---
    public String getDescriptionShown() { return descriptionShown; }
    public String getSuccessMessage() { return successMessage; }
    public String getErrorMessage() { return errorMessage; }
    public String getErrorField() { return errorField; }
    public boolean isConfirmationDialogShown() { return confirmationDialogShown; }
}