package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

public class ExecuteDeleteProductViewStub implements ExecuteDeleteProductView {

    private String name, code, description, price;
    private boolean confirmationDialogShown = false;
    private String successMessage = "";
    private String errorMessage = "";

    @Override
    public void setProductDetails(String name, String code, String description, String price) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.price = price;
    }

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


    public String getName() { return name; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }

    public boolean isConfirmationDialogShown() { return confirmationDialogShown; }
    public String getSuccessMessage() { return successMessage; }
    public String getErrorMessage() { return errorMessage; }
}