package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

public interface ExecuteProcessProductView {

    String getProductCode();
    String getProductName();
    String getProductPrice();
    String getProductDescription();

    void setProductData(String code, String name, String price, String description);

    void setRequestDescription(String description);

    void showInputError(String field, String message);

    void showSuccessMessage(String message);

    void showError(String message);

    void showConfirmationDialog();
}