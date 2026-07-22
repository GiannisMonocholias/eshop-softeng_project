package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

/**
 * Presenter for the product deletion execution screen.
 * Orchestrates the asynchronous removal of product types from the catalogue and updates
 * the administrative request status to SERVED upon completion utilizing Dependency Injection.
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
     * @param view The view implementation (Activity or Stub).
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
     * Asynchronously loads the specific request details and employee context into the view.
     * @param employeeId The ID of the employee executing the deletion.
     * @param requestId  The ID of the delete request.
     */
    public void loadRequestDetails(String employeeId, int requestId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof UpdateCatalogueEmployee) {
                this.loggedInEmployee = (UpdateCatalogueEmployee) employee;

                updateRequestDAO.getUpdateRequests().thenAccept(requestsMap -> {
                    this.currentRequest = requestsMap.get(requestId);

                    if (currentRequest != null) {
                        String priceStr = (currentRequest.getProduct().getPrice() != null)
                                ? currentRequest.getProduct().getPrice().toString()
                                : "-";

                        view.setProductDetails(
                                currentRequest.getProduct().getProductname(),
                                currentRequest.getProduct().getProductCode(),
                                currentRequest.getProduct().getDescription(),
                                priceStr
                        );
                    } else {
                        view.showError("Σφάλμα: Το αίτημα δεν βρέθηκε.");
                    }
                }).exceptionally(e -> {
                    view.showError("Σφάλμα ανάκτησης αιτήματος: " + e.getMessage());
                    return null;
                });
            } else {
                view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }
        }).exceptionally(e -> {
            view.showError("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
            return null;
        });
    }

    /**
     * Triggered when the user clicks the initial delete button.
     */
    public void onDeleteButtonClicked() {
        if (currentRequest == null) {
            view.showError("Δεν υπάρχει ενεργό αίτημα για διαγραφή.");
            return;
        }
        view.showConfirmationDialog();
    }

    /**
     * Executes the permanent, asynchronous deletion of the product type from the catalogue.
     * Updates request status to {@link RequestStatusType#SERVED} and cleans up
     * the employee's assigned task list.
     */
    public void onDeleteConfirmed() {
        if (currentRequest == null || loggedInEmployee == null) return;

        productTypeDAO.deleteProductType(currentRequest.getProduct()).thenAccept(v1 -> {
            // Mark request as successfully completed
            currentRequest.setStatus(RequestStatusType.SERVED);

            // Persist request state update asynchronously
            updateRequestDAO.addUpdateRequest(currentRequest).thenAccept(v2 -> {
                view.showSuccessMessage("Το προϊόν διαγράφηκε επιτυχώς και το αίτημα σημειώθηκε ως εξυπηρετημένο.");
                loggedInEmployee.getAssignedRequests().remove(currentRequest.getId());
            }).exceptionally(e -> {
                view.showError("Σφάλμα κατά την ενημέρωση του αιτήματος: " + e.getMessage());
                return null;
            });

        }).exceptionally(e -> {
            view.showError("Deletion failed: " + e.getMessage());
            return null;
        });
    }
}