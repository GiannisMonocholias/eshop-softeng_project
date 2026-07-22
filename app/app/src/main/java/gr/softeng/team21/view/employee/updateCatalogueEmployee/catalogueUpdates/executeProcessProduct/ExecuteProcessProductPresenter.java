package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

import java.math.BigDecimal;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.util.Money;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

/**
 * Presenter for the product modification screen.
 * Handles the asynchronous logic for validating new inputs, updating the domain object,
 * and marking the administrative request as served using Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteProcessProductPresenter {
    private ExecuteProcessProductView view;
    private EmployeeDAO employeeDAO;
    private UpdateRequestDAO updateRequestDAO;
    private ProductTypeDAO productTypeDAO;
    private CatalogueUpdateRequest currentRequest;
    private UpdateCatalogueEmployee loggedInEmployee;
    private ProductType productToEdit;

    /**
     * Initializes the presenter with injected DAOs and the view interface.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO Data access for employee records.
     * @param updateRequestDAO Data access for update requests.
     * @param productTypeDAO Data access for the product catalogue.
     */
    public ExecuteProcessProductPresenter(ExecuteProcessProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Asynchronously loads the request context and the specific product to be edited.
     * @param employeeId The ID of the employee executing the process.
     * @param requestId  The ID of the process request.
     */
    public void loadRequestDetails(String employeeId, int requestId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof UpdateCatalogueEmployee) {
                this.loggedInEmployee = (UpdateCatalogueEmployee) employee;

                updateRequestDAO.getUpdateRequests().thenAccept(requestsMap -> {
                    this.currentRequest = requestsMap.get(requestId);

                    if (currentRequest == null || currentRequest.getProduct() == null) {
                        view.showError("Σφάλμα: Τα στοιχεία του αιτήματος ή του επηρεαζόμενου προϊόντος δεν βρέθηκαν.");
                        return;
                    }

                    this.productToEdit = currentRequest.getProduct();
                    view.setRequestDescription(currentRequest.getUpdateDescription());

                    String priceStr = (productToEdit.getPrice() != null) ?
                            String.valueOf(productToEdit.getPrice().getAmount()) : "";

                    view.setProductData(productToEdit.getProductCode(), productToEdit.getProductname(),
                            priceStr, productToEdit.getDescription());

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
     * Validates the price input and requests user confirmation.
     */
    public void onSaveClicked() {
        String newPriceStr = view.getProductPrice();
        try {
            double priceVal = Double.parseDouble(newPriceStr);
            if (priceVal < 0) throw new NumberFormatException();

            view.showConfirmationDialog();
        } catch (NumberFormatException e) {
            view.showInputError("price", "Please enter a valid numeric price value.");
        }
    }

    /**
     * Asynchronously applies the validated changes to the domain object, updates the
     * product repository, and marks the request status as SERVED.
     * Cleans up the employee's assigned task queue upon success.
     */
    public void onSaveConfirmed() {
        if (currentRequest == null || loggedInEmployee == null || productToEdit == null) return;

        String newCode = view.getProductCode();
        String newName = view.getProductName();
        String newPriceStr = view.getProductPrice();
        String newDesc = view.getProductDescription();

        Money newMoney = new Money(BigDecimal.valueOf(Double.parseDouble(newPriceStr)), "€");

        // Apply domain updates
        productToEdit.setProductcode(newCode);
        productToEdit.setProductname(newName);
        productToEdit.setDescription(newDesc);
        productToEdit.setPrice(newMoney);

        // Persist product updates asynchronously
        productTypeDAO.processProduct(productToEdit).thenAccept(v1 -> {
            currentRequest.setStatus(RequestStatusType.SERVED);

            // Persist the request state change
            updateRequestDAO.addUpdateRequest(currentRequest).thenAccept(v2 -> {
                if (loggedInEmployee.getAssignedRequests() != null) {
                    loggedInEmployee.getAssignedRequests().remove(currentRequest.getId());
                }
                view.showSuccessMessage("Οι αλλαγές αποθηκεύτηκαν επιτυχώς!");
            }).exceptionally(e -> {
                view.showError("Σφάλμα κατά την ενημέρωση του αιτήματος: " + e.getMessage());
                return null;
            });

        }).exceptionally(e -> {
            view.showError("Σφάλμα κατά την αποθήκευση του προϊόντος: " + e.getMessage());
            return null;
        });
    }
}