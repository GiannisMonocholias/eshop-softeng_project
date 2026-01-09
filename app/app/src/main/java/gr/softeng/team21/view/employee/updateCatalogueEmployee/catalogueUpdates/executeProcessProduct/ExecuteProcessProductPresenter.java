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
 * Handles the logic for validating new inputs, updating the domain object,
 * and marking the administrative request as served.
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
     * Initializes the presenter with required DAOs and view interface.
     */
    public ExecuteProcessProductPresenter(ExecuteProcessProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Loads the request context and the specific product to be edited.
     * @param employeeId The ID of the employee executing the process.
     * @param requestId  The ID of the process request.
     */
    public void loadRequestDetails(String employeeId, int requestId) {
        this.loggedInEmployee = (UpdateCatalogueEmployee) employeeDAO.getEmployee(employeeId);
        if (updateRequestDAO.getUpdateRequests() != null) {
            this.currentRequest = updateRequestDAO.getUpdateRequests().get(requestId);
        }

        if (currentRequest == null || loggedInEmployee == null || currentRequest.getProduct() == null) {
            view.showError("Σφάλμα: Τα στοιχεία του υπαλλήλου ή του αιτήματος ή του επηρεαζόμενου προϊόντος δεν βρέθηκαν.");
            return;
        }

        this.productToEdit = currentRequest.getProduct();
        view.setRequestDescription(currentRequest.getUpdateDescription());

        String priceStr = (productToEdit.getPrice() != null) ?
                String.valueOf(productToEdit.getPrice().getAmount()) : "";

        view.setProductData(productToEdit.getProductCode(), productToEdit.getProductname(),
                priceStr, productToEdit.getDescription());
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
            view.showInputError("price", "Please enter a valid price value.");
        }
    }

    /**
     * Applies the validated changes to the domain object and updates the request status.
     * Cleans up the employee's assigned task queue upon success.
     */
    public void onSaveConfirmed() {
        String newCode = view.getProductCode();
        String newName = view.getProductName();
        String newPriceStr = view.getProductPrice();
        String newDesc = view.getProductDescription();

        Money newMoney = new Money(BigDecimal.valueOf(Double.parseDouble(newPriceStr)), "€");

        // Domain updates
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