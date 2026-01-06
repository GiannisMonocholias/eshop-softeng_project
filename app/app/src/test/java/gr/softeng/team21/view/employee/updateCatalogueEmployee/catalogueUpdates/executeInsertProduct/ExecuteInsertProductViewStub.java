package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

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

    @Override
    public void setRequestDescription(String description) {
        this.requestDescription = description;
    }

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

    public String getRequestDescription() { return requestDescription; }
    public String getSuccessMessage() { return successMessage; }
    public String getErrorMessage() { return errorMessage; }
    public String getInputErrorField() { return inputErrorField; }
}