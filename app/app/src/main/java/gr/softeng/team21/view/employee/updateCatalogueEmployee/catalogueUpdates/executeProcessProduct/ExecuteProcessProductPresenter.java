package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

import android.util.Log;

import java.math.BigDecimal;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

public class ExecuteProcessProductPresenter {
    private ExecuteProcessProductView view;
    private EmployeeDAO employeeDAO;
    private UpdateRequestDAO updateRequestDAO;
    private ProductTypeDAO productTypeDAO;
    private CatalogueUpdateRequest currentRequest;
    private UpdateCatalogueEmployee loggedInEmployee;
    private ProductType productToEdit;

    public ExecuteProcessProductPresenter(ExecuteProcessProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    public void loadRequestDetails(String employeeId, int requestId) {
        this.loggedInEmployee = (UpdateCatalogueEmployee) employeeDAO.getEmployee(employeeId);
        if (updateRequestDAO.getUpdateRequests() != null) {
            this.currentRequest = updateRequestDAO.getUpdateRequests().get(requestId);
        }
        if (currentRequest == null || loggedInEmployee == null || currentRequest.getProduct() == null) {
            view.showError("Σφάλμα: Τα στοιχεία δεν βρέθηκαν.");
            return;
        }
        this.productToEdit = currentRequest.getProduct();
        view.setRequestDescription(currentRequest.getUpdateDescription());
        String priceStr = (productToEdit.getPrice() != null) ?
                String.valueOf(productToEdit.getPrice().getAmount()) : "";
        view.setProductData(productToEdit.getProductCode(), productToEdit.getProductname(), priceStr, productToEdit.getDescription());
    }


    public void onSaveClicked() {
        String newPriceStr = view.getProductPrice();

        try {
            double priceVal = Double.parseDouble(newPriceStr);
            if (priceVal < 0) throw new NumberFormatException();

            view.showConfirmationDialog();

        } catch (NumberFormatException e) {
            view.showInputError("price", "Παρακαλώ εισάγετε έγκυρη τιμή");
        }
    }


    public void onSaveConfirmed() {
        String newCode = view.getProductCode();
        String newName = view.getProductName();
        String newPriceStr = view.getProductPrice();
        String newDesc = view.getProductDescription();


        Money newMoney = new Money(BigDecimal.valueOf(Double.parseDouble(newPriceStr)), "€");


        productToEdit.setProductcode(newCode);
        productToEdit.setProductname(newName);
        productToEdit.setDescription(newDesc);
        productToEdit.setPrice(newMoney);



        currentRequest.setStatus(RequestStatusType.SERVED);

        if (loggedInEmployee.getAssignedRequests() != null) {
            loggedInEmployee.getAssignedRequests().remove(currentRequest.getId());
        }

        view.showSuccessMessage("Οι αλλαγές αποθηκεύτηκαν επιτυχώς!");
    }
}