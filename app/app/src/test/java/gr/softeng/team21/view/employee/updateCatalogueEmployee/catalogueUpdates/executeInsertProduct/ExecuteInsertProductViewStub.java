package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

/**
 * A stub implementation of the {@link ExecuteInsertProductView} interface for unit testing.
 * It simulates the product insertion form, capturing input values and providing
 * mechanisms to verify success, general error messages, and field-specific validation errors.
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
     * Simulates the user entering a product code into the text field.
     * @param codeInput The product code string.
     */
    public void setCodeInput(String codeInput) { this.codeInput = codeInput; }

    /**
     * Simulates the user entering a product name.
     */
    public void setNameInput(String nameInput) { this.nameInput = nameInput; }

    /**
     * Simulates the user entering a price.
     */
    public void setPriceInput(String priceInput) { this.priceInput = priceInput; }

    /**
     * Simulates the user entering a description.
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
     * Captures the description of the original update request to show on the UI.
     */
    @Override
    public void setRequestDescription(String description) {
        this.requestDescription = description;
    }

    /**
     * Captures validation errors related to specific input fields.
     * @param field The name of the field (e.g., "price").
     * @param message The specific error message.
     */
    @Override
    public void showInputError(String field, String message) {
        this.inputErrorField = field;
        this.inputErrorMessage = message;
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

    public String getRequestDescription() { return requestDescription; }
    public String getSuccessMessage() { return successMessage; }
    public String getErrorMessage() { return errorMessage; }
    public String getInputErrorField() { return inputErrorField; }
}