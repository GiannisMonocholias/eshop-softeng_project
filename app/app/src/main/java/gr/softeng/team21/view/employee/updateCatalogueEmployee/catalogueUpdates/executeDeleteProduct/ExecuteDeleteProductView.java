package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

public interface ExecuteDeleteProductView {
    void setProductDetails(String name, String code, String description, String price);

    void showConfirmationDialog();

    void showSuccessMessage(String message);
    void showError(String message);
}