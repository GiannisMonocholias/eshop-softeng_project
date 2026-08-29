package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

/**
 * A stub implementation of the {@link ExecuteProcessProductView} interface for unit testing.
 * It simulates the product update form UI, capturing data input and verifying the
 * asynchronous dispatch of product details, field validation errors, and confirmation dialogs.
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

    /**
     * Simulates the user entering a product code into the stub text field.
     * @param codeInput The product code string.
     */
    public void setCodeInput(String codeInput) { this.codeInput = codeInput; }

    /**
     * Simulates the user entering a product name into the stub text field.
     * @param nameInput The product name string.
     */
    public void setNameInput(String nameInput) { this.nameInput = nameInput; }

    /**
     * Simulates the user entering a price into the stub text field.
     * @param priceInput The product price string.
     */
    public void setPriceInput(String priceInput) { this.priceInput = priceInput; }

    /**
     * Simulates the user entering a description into the stub text field.
     * @param descInput The product description string.
     */
    public void setDescInput(String descInput) { this.descInput = descInput; }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductCode() { return codeInput; }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductName() { return nameInput; }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductPrice() { return priceInput; }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductDescription() { return descInput; }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProductData(String code, String name, String price, String description) {
        this.codeInput = code;
        this.nameInput = name;
        this.priceInput = price;
        this.descInput = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRequestDescription(String description) {
        this.descriptionShown = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showInputError(String field, String message) {
        this.errorField = field;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void showConfirmationDialog() {
        this.confirmationDialogShown = true;
    }

    // --- Accessor methods for verification during test assertions ---

    /**
     * @return The stored description string intended for the UI.
     */
    public String getDescriptionShown() { return descriptionShown; }

    /**
     * @return The text content of a dispatched success message.
     */
    public String getSuccessMessage() { return successMessage; }

    /**
     * @return The text content of a dispatched error message.
     */
    public String getErrorMessage() { return errorMessage; }

    /**
     * @return The field identifier that was flagged with an input error.
     */
    public String getErrorField() { return errorField; }

    /**
     * @return True if the confirmation dialog was requested, false otherwise.
     */
    public boolean isConfirmationDialogShown() { return confirmationDialogShown; }
}