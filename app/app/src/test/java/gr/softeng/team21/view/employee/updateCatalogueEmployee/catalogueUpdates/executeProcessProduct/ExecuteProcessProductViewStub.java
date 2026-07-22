package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

/**
 * A stub implementation of the {@link ExecuteProcessProductView} interface for unit testing.
 * It simulates the product update form, capturing data input and verifying the
 * asynchronous display of product details, error messages, and confirmation dialogs.
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
     * Simulates the user entering a product code into the text field.
     * @param codeInput The product code string.
     */
    public void setCodeInput(String codeInput) { this.codeInput = codeInput; }

    /**
     * Simulates the user entering a product name.
     * @param nameInput The product name string.
     */
    public void setNameInput(String nameInput) { this.nameInput = nameInput; }

    /**
     * Simulates the user entering a price.
     * @param priceInput The product price string.
     */
    public void setPriceInput(String priceInput) { this.priceInput = priceInput; }

    /**
     * Simulates the user entering a description.
     * @param descInput The product description string.
     */
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

    // --- Accessor methods for verification during testing ---
    public String getDescriptionShown() { return descriptionShown; }
    public String getSuccessMessage() { return successMessage; }
    public String getErrorMessage() { return errorMessage; }
    public String getErrorField() { return errorField; }
    public boolean isConfirmationDialogShown() { return confirmationDialogShown; }
}