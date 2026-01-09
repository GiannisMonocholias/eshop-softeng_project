package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

/**
 * Presenter for the product deletion execution screen.
 * Orchestrates the removal of product types from the catalogue and updates
 * the administrative request status to SERVED upon completion.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteDeleteProductPresenter {
    private ExecuteDeleteProductView view;
    private EmployeeDAO employeeDAO;
    private UpdateRequestDAO updateRequestDAO;
    private ProductTypeDAO productTypeDAO;
    private UpdateCatalogueEmployee loggedInEmployee;
    private CatalogueUpdateRequest currentRequest;

    /**
     * Initializes the presenter with necessary DAOs and the view.
     * @param view The view implementation.
     * @param employeeDAO Data source for employee records.
     * @param updateRequestDAO Data source for update requests.
     * @param productTypeDAO Data source for the product catalogue.
     */
    public ExecuteDeleteProductPresenter(ExecuteDeleteProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Loads the specific request details and employee context into the view.
     * @param employeeId The ID of the employee executing the deletion.
     * @param requestId  The ID of the delete request.
     */
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

    /** Triggered when the user clicks the initial delete button. */
    public void onDeleteButtonClicked() {
        view.showConfirmationDialog();
    }

    /**
     * Executes the permanent deletion of the product type from the catalogue.
     * Updates request status to {@link RequestStatusType#SERVED} and cleans up
     * the employee's assigned task list.
     */
    public void onDeleteConfirmed() {
        if (currentRequest == null || loggedInEmployee == null) return;

        try {
            // Remove the product type from the global catalogue
            productTypeDAO.deleteProductType(currentRequest.getProduct());

            // Mark request as successfully completed
            currentRequest.setStatus(RequestStatusType.SERVED);

            view.showSuccessMessage("Το προϊόν διαγράφηκε επιτυχώς και το αίτημα σημειώθηκε ως εξυπηρετημένο.");

            // Remove from the employee's active task queue
            loggedInEmployee.getAssignedRequests().remove(currentRequest.getId());

        } catch (IllegalArgumentException e) {
            view.showError("Deletion failed: " + e.getMessage());
        }
    }
}