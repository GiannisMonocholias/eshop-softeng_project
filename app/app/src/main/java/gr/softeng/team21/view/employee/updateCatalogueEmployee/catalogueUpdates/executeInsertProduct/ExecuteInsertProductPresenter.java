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
 * Presenter for the product insertion execution screen.
 * Handles the logic for validating user input, creating a new {@link ProductType}
 * domain object, and updating the request status asynchronously using DAOs.
 * Implements Dependency Injection to decouple business logic from the UI.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteInsertProductPresenter {

    private final ExecuteInsertProductView view;
    private final EmployeeDAO employeeDAO;
    private final UpdateRequestDAO updateRequestDAO;
    private final ProductTypeDAO productTypeDAO;

    private CatalogueUpdateRequest currentRequest;
    private UpdateCatalogueEmployee loggedInEmployee;

    /**
     * Initializes the presenter with injected DAOs and the view interface.
     * @param view The view implementation (Activity or Stub) to handle UI updates.
     * @param employeeDAO Data access object for verifying employee sessions.
     * @param updateRequestDAO Data access object for managing catalogue update requests.
     * @param productTypeDAO Data access object for the product catalogue repository.
     */
    public ExecuteInsertProductPresenter(ExecuteInsertProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Asynchronously loads the specific request instructions and employee context.
     * Replaces full map downloads with an optimized direct ID fetch from the DAO.
     * @param employeeId The unique ID of the employee executing the insert operation.
     * @param requestId The unique ID of the specific insertion request.
     */
    public void loadRequestDetails(String employeeId, int requestId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof UpdateCatalogueEmployee) {
                this.loggedInEmployee = (UpdateCatalogueEmployee) employee;

                // Use the direct getUpdateRequest method instead of fetching the whole map
                updateRequestDAO.getUpdateRequest(requestId).thenAccept(request -> {
                    this.currentRequest = request;

                    if (currentRequest != null) {
                        if (view != null) view.setRequestDescription(currentRequest.getUpdateDescription());
                    } else {
                        if (view != null) view.showError("Σφάλμα: Το αίτημα δεν βρέθηκε.");
                    }
                }).exceptionally(e -> {
                    if (view != null) view.showError("Σφάλμα ανάκτησης αιτήματος: " + e.getMessage());
                    return null;
                });
            } else {
                if (view != null) view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
            return null;
        });
    }

    /**
     * Processes the product insertion logic asynchronously.
     * Validates numeric input for price, instantiates the domain object, persists it
     * via the DAO, and updates the lifecycle status of the associated request.
     */
    public void onConfirmInsert() {
        if (currentRequest == null || loggedInEmployee == null) {
            if (view != null) view.showError("Δεν υπάρχει ενεργό αίτημα προς καταχώρηση.");
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

                // Update domain state locally
                currentRequest.setStatus(RequestStatusType.SERVED);
                loggedInEmployee.incrementTotalCatalogueUpdates();

                // Persist the request state change using updateRequest (overwrites existing document)
                updateRequestDAO.updateRequest(currentRequest).thenAccept(v2 -> {
                    if (view != null) view.showSuccessMessage("Το προϊόν καταχωρήθηκε επιτυχώς!");
                }).exceptionally(e -> {
                    if (view != null) view.showError("Σφάλμα κατά την ενημέρωση του αιτήματος: " + e.getMessage());
                    return null;
                });

            }).exceptionally(e -> {
                if (view != null) view.showError("Error: " + e.getMessage());
                return null;
            });

        } catch (NumberFormatException e) {
            if (view != null) view.showInputError("price", "Please enter a valid price (e.g., 12.50)");
        }
    }
}