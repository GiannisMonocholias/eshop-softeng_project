package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

public interface ExecuteInsertProductView {
    String getProductCode();
    String getProductName();
    String getProductPrice();
    String getProductDescription();

    void setRequestDescription(String description);

    void showInputError(String field, String message);

    void showSuccessMessage(String message);

    void showError(String message);
}