package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

import android.util.Log;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

public class ExecuteDeleteProductPresenter {
    private ExecuteDeleteProductView view;
    private EmployeeDAO employeeDAO;
    private UpdateRequestDAO updateRequestDAO;
    private ProductTypeDAO productTypeDAO;
    private UpdateCatalogueEmployee loggedInEmployee;
    private CatalogueUpdateRequest currentRequest;



    public ExecuteDeleteProductPresenter(ExecuteDeleteProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    public void loadRequestDetails(String employeeId, int requestId) {
        this.loggedInEmployee = (UpdateCatalogueEmployee) employeeDAO.getEmployee(employeeId);
        this.currentRequest = updateRequestDAO.getUpdateRequests().get(requestId);


        if (currentRequest == null || loggedInEmployee == null) {
            view.showError("Σφάλμα: Το αίτημα ή ο υπάλληλος δεν βρέθηκαν.");
            return;
        }

        String priceStr = (currentRequest.getProduct().getPrice() != null)
                ? currentRequest.getProduct().getPrice().toString()
                : "-";

        view.setProductDetails(
                currentRequest.getProduct().getProductname(),
                currentRequest.getProduct().getProductCode(),
                currentRequest.getProduct().getDescription(),
                priceStr
        );
    }

    public void onDeleteButtonClicked() {
        view.showConfirmationDialog();
    }

    public void onDeleteConfirmed() {
        if (currentRequest == null || loggedInEmployee == null) return;

        try {
            productTypeDAO.deleteProductType(currentRequest.getProduct());


            currentRequest.setStatus(RequestStatusType.SERVED);

            view.showSuccessMessage("Το προϊόν διαγράφηκε επιτυχώς και το αίτημα ολοκληρώθηκε.");
            loggedInEmployee.getAssignedRequests().remove(currentRequest.getId());
        } catch (IllegalArgumentException e) {
            view.showError("Απέτυχε η διαγραφή: " + e.getMessage());
        }

    }
}
