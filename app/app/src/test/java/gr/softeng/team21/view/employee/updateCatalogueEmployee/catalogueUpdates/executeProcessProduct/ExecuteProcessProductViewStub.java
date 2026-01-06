package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

public class ExecuteProcessProductViewStub implements ExecuteProcessProductView {

    // Inputs (Τι πληκτρολογεί ο χρήστης)
    private String codeInput = "";
    private String nameInput = "";
    private String priceInput = "";
    private String descInput = "";

    // Outputs (Τι δείχνει η οθόνη)
    private String descriptionShown = "";
    private String successMessage = "";
    private String errorMessage = "";
    private String errorField = "";
    private boolean confirmationDialogShown = false;

    // Setters για να "γράφουμε" στα πεδία από το Test
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
    public void setProductData(String code, String name, String price, String description) {
        // Όταν φορτώνει τα αρχικά δεδομένα, τα αποθηκεύουμε στα inputs
        // ώστε να προσομοιώσουμε ότι εμφανίζονται στα πεδία
        this.codeInput = code;
        this.nameInput = name;
        this.priceInput = price;
        this.descInput = description;
    }

    @Override
    public void setRequestDescription(String description) {
        this.descriptionShown = description;
    }

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

    @Override
    public void showConfirmationDialog() {
        this.confirmationDialogShown = true;
    }

    // --- Getters for Tests ---
    public String getDescriptionShown() { return descriptionShown; }
    public String getSuccessMessage() { return successMessage; }
    public String getErrorMessage() { return errorMessage; }
    public String getErrorField() { return errorField; }
    public boolean isConfirmationDialogShown() { return confirmationDialogShown; }
}