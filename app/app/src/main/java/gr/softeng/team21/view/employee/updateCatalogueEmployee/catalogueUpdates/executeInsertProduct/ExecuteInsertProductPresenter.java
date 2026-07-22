package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

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
 * Presenter for the product insertion screen.
 * Handles the logic for validating user input, creating a new {@link ProductType}
 * domain object, and updating the request status asynchronously via Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteInsertProductPresenter {
    private ExecuteInsertProductView view;
    private EmployeeDAO employeeDAO;
    private UpdateRequestDAO updateRequestDAO;
    private ProductTypeDAO productTypeDAO;

    private CatalogueUpdateRequest currentRequest;
    private UpdateCatalogueEmployee loggedInEmployee;

    /**
     * Initializes the presenter with injected DAOs and the view interface.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO Data access for employee records.
     * @param updateRequestDAO Data access for update requests.
     * @param productTypeDAO Data access for the product catalogue.
     */
    public ExecuteInsertProductPresenter(ExecuteInsertProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Asynchronously loads the specific request instructions and employee context.
     * @param employeeId The ID of the employee executing the insert.
     * @param requestId The ID of the insert request.
     */
    public void loadRequestDetails(String employeeId, int requestId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof UpdateCatalogueEmployee) {
                this.loggedInEmployee = (UpdateCatalogueEmployee) employee;

                updateRequestDAO.getUpdateRequests().thenAccept(requestsMap -> {
                    this.currentRequest = requestsMap.get(requestId);

                    if (currentRequest != null) {
                        view.setRequestDescription(currentRequest.getUpdateDescription());
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
     * Processes the insertion logic asynchronously. Validates numeric input for price,
     * creates the domain object, and persists it via the DAO.
     */
    public void onConfirmInsert() {
        if (currentRequest == null || loggedInEmployee == null) {
            view.showError("Δεν υπάρχει ενεργό αίτημα προς καταχώρηση.");
            return;
        }

        String code = view.getProductCode();
        String name = view.getProductName();
        String priceStr = view.getProductPrice();
        String desc = view.getProductDescription();

        try {
            double priceVal = Double.parseDouble(priceStr);
            if (priceVal < 0) throw new NumberFormatException();

            // Create Domain Model components
            Money money = new Money(BigDecimal.valueOf(priceVal), "€");
            ProductType newProduct = new ProductType(name, desc, money, code);

            // Persist product and update status asynchronously
            productTypeDAO.addProductType(newProduct).thenAccept(v1 -> {
                currentRequest.setStatus(RequestStatusType.SERVED);

                // Persist the request state change
                updateRequestDAO.addUpdateRequest(currentRequest).thenAccept(v2 -> {
                    view.showSuccessMessage("Το προϊόν καταχωρήθηκε επιτυχώς!");
                    loggedInEmployee.getAssignedRequests().remove(currentRequest.getId());
                }).exceptionally(e -> {
                    view.showError("Σφάλμα κατά την ενημέρωση του αιτήματος: " + e.getMessage());
                    return null;
                });

            }).exceptionally(e -> {
                view.showError("Error: " + e.getMessage());
                return null;
            });

        } catch (NumberFormatException e) {
            view.showInputError("price", "Please enter a valid price (e.g., 12.50)");
        }
    }
}