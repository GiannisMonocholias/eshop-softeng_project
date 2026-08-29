package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

/**
 * A stub implementation of the {@link ExecuteInsertProductView} interface for unit testing.
 * It mimics the behavior of the product insertion form, capturing setter inputs and providing
 * mechanisms to verify success, asynchronous error messages, and specific field validation triggers.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteInsertProductViewStub implements ExecuteInsertProductView {

    private String codeInput = "";
    private String nameInput = "";
    private String priceInput = "";
    private String descInput = "";

    private String requestDescription = "";
    private String successMessage = "";
    private String errorMessage = "";
    private String inputErrorField = "";
    private String inputErrorMessage = "";

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
    public void setRequestDescription(String description) {
        this.requestDescription = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showInputError(String field, String message) {
        this.inputErrorField = field;
        this.inputErrorMessage = message;
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

    // --- Accessor methods for verification during testing assertions ---

    /**
     * @return The stored description string intended for the UI.
     */
    public String getRequestDescription() { return requestDescription; }

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
    public String getInputErrorField() { return inputErrorField; }
}