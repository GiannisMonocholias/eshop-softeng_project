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
 * the administrative request status to SERVED via DAO without holding domain-level lists.
 *
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteDeleteProductPresenter {

    private final ExecuteDeleteProductView view;
    private final EmployeeDAO employeeDAO;
    private final UpdateRequestDAO updateRequestDAO;
    private final ProductTypeDAO productTypeDAO;
    private UpdateCatalogueEmployee loggedInEmployee;
    private CatalogueUpdateRequest currentRequest;

    /**
     * Initializes the presenter with necessary DAOs and the view interface.
     *
     * @param view The View contract implementation handling UI updates.
     * @param employeeDAO Data source for employee session validation.
     * @param updateRequestDAO Data source for managing catalogue update requests.
     * @param productTypeDAO Data source for the product catalogue repository.
     */
    public ExecuteDeleteProductPresenter(ExecuteDeleteProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Asynchronously loads the specific request details and employee context,
     * formatting the product data before passing it to the view.
     *
     * @param employeeId The ID of the employee executing the deletion.
     * @param requestId  The specific ID of the delete request to execute.
     */
    public void loadRequestDetails(String employeeId, int requestId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof UpdateCatalogueEmployee) {
                this.loggedInEmployee = (UpdateCatalogueEmployee) employee;
                updateRequestDAO.getUpdateRequest(requestId).thenAccept(request -> {
                    this.currentRequest = request;
                    if (currentRequest != null) {
                        String priceStr = (currentRequest.getProduct().getPrice() != null)
                                ? currentRequest.getProduct().getPrice().toString() : "-";
                        if (view != null) {
                            view.setProductDetails(
                                    currentRequest.getProduct().getProductname(),
                                    currentRequest.getProduct().getProductCode(),
                                    currentRequest.getProduct().getDescription(),
                                    priceStr
                            );
                        }
                    } else {
                        if (view != null) view.showError("Σφάλμα: Το αίτημα δεν βρέθηκε.");
                    }
                }).exceptionally(e -> {
                    if (view != null) view.showError("Σφάλμα ανάκτησης αιτήματος: " + e.getMessage());
                    return null;
                });
            } else {
                if (view != null) view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
            return null;
        });
    }

    /**
     * Triggered when the user clicks the initial delete button on the UI.
     * Validates active request presence and requests a confirmation dialog from the view.
     */
    public void onDeleteButtonClicked() {
        if (currentRequest == null) {
            if (view != null) view.showError("Δεν υπάρχει ενεργό αίτημα για διαγραφή.");
            return;
        }
        if (view != null) view.showConfirmationDialog();
    }

    /**
     * Executes the permanent, asynchronous deletion of the product type from the catalogue DAO.
     * Automatically updates the request status to {@link RequestStatusType#SERVED} and persists
     * this state change asynchronously in the UpdateRequestDAO.
     */
    public void onDeleteConfirmed() {
        if (currentRequest == null || loggedInEmployee == null) return;

        productTypeDAO.deleteProductType(currentRequest.getProduct()).thenAccept(v1 -> {
            // Update domain properties locally
            currentRequest.setStatus(RequestStatusType.SERVED);
            loggedInEmployee.incrementTotalCatalogueUpdates();

            // Save state asynchronously via DAO overwrite operation
            updateRequestDAO.updateRequest(currentRequest).thenAccept(v2 -> {
                if (view != null) view.showSuccessMessage("Το προϊόν διαγράφηκε επιτυχώς και το αίτημα σημειώθηκε ως εξυπηρετημένο.");
            }).exceptionally(e -> {
                if (view != null) view.showError("Σφάλμα κατά την ενημέρωση του αιτήματος: " + e.getMessage());
                return null;
            });

        }).exceptionally(e -> {
            if (view != null) view.showError("Deletion failed: " + e.getMessage());
            return null;
        });
    }
}