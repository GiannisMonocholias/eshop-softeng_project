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
 * Presenter for the product modification (process) screen.
 * Handles the asynchronous logic for validating new inputs, updating the domain object,
 * persisting changes to the product repository, and marking the administrative
 * request as SERVED via DAOs. Completely decoupled from domain-level RAM lists.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteProcessProductPresenter {

    private final ExecuteProcessProductView view;
    private final EmployeeDAO employeeDAO;
    private final UpdateRequestDAO updateRequestDAO;
    private final ProductTypeDAO productTypeDAO;

    private CatalogueUpdateRequest currentRequest;
    private UpdateCatalogueEmployee loggedInEmployee;
    private ProductType productToEdit;

    /**
     * Initializes the presenter with injected DAOs and the view interface.
     * @param view The View contract implementation handling UI updates.
     * @param employeeDAO Data access object for validating employee sessions.
     * @param updateRequestDAO Data access object for managing catalogue update requests.
     * @param productTypeDAO Data access object for the product catalogue repository.
     */
    public ExecuteProcessProductPresenter(ExecuteProcessProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Asynchronously loads the request context and the specific product to be edited.
     * Replaces the inefficient full-map download with a direct, indexed DAO fetch.
     * @param employeeId The unique ID of the employee executing the modification.
     * @param requestId  The unique ID of the specific process request.
     */
    public void loadRequestDetails(String employeeId, int requestId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof UpdateCatalogueEmployee) {
                this.loggedInEmployee = (UpdateCatalogueEmployee) employee;

                // Optimized specific document fetch
                updateRequestDAO.getUpdateRequest(requestId).thenAccept(request -> {
                    this.currentRequest = request;

                    if (currentRequest == null || currentRequest.getProduct() == null) {
                        if (view != null) view.showError("Σφάλμα: Τα στοιχεία του αιτήματος ή του επηρεαζόμενου προϊόντος δεν βρέθηκαν.");
                        return;
                    }

                    this.productToEdit = currentRequest.getProduct();

                    if (view != null) {
                        view.setRequestDescription(currentRequest.getUpdateDescription());

                        String priceStr = (productToEdit.getPrice() != null) ?
                                String.valueOf(productToEdit.getPrice().getAmount()) : "";

                        view.setProductData(productToEdit.getProductCode(), productToEdit.getProductname(),
                                priceStr, productToEdit.getDescription());
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
     * Triggered when the user clicks the save button.
     * Validates the price input to ensure it is a positive numeric value before requesting
     * user confirmation via the view.
     */
    public void onSaveClicked() {
        if (view == null) return;

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
     * product repository, and overwrites the request status as SERVED in the database.
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

            // Update request state locally
            currentRequest.setStatus(RequestStatusType.SERVED);
            loggedInEmployee.incrementTotalCatalogueUpdates();

            // Persist the request state change using overwrite (updateRequest)
            updateRequestDAO.updateRequest(currentRequest).thenAccept(v2 -> {
                if (view != null) view.showSuccessMessage("Οι αλλαγές αποθηκεύτηκαν επιτυχώς!");
            }).exceptionally(e -> {
                if (view != null) view.showError("Σφάλμα κατά την ενημέρωση του αιτήματος: " + e.getMessage());
                return null;
            });

        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα κατά την αποθήκευση του προϊόντος: " + e.getMessage());
            return null;
        });
    }
}