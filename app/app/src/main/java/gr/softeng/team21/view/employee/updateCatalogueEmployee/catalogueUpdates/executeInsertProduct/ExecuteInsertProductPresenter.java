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
 * domain object, and updating the request status to SERVED.
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
     * Initializes the presenter with required DAOs and the view interface.
     */
    public ExecuteInsertProductPresenter(ExecuteInsertProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Loads the specific request instructions and employee context.
     * @param employeeId The ID of the employee executing the insert.
     * @param requestId The ID of the insert request.
     */
    public void loadRequestDetails(String employeeId, int requestId) {
        this.loggedInEmployee = (UpdateCatalogueEmployee) employeeDAO.getEmployee(employeeId);

        if (updateRequestDAO.getUpdateRequests() != null) {
            this.currentRequest = updateRequestDAO.getUpdateRequests().get(requestId);
        }

        if (currentRequest == null || loggedInEmployee == null) {
            view.showError("Σφάλμα: Τα στοιχεία του υπαλλήλου ή του αιτήματος δεν βρέθηκαν.");
            return;
        }

        view.setRequestDescription(currentRequest.getUpdateDescription());
    }

    /**
     * Processes the insertion logic. Validates numeric input for price,
     * creates the domain object, and persists it via the DAO.
     */
    public void onConfirmInsert() {
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

            // Persist and update status
            productTypeDAO.addProductType(newProduct);
            currentRequest.setStatus(RequestStatusType.SERVED);

            view.showSuccessMessage("Το προϊόν καταχωρήθηκε επιτυχώς!");

            // Cleanup employee's task queue
            loggedInEmployee.getAssignedRequests().remove(currentRequest.getId());

        } catch (NumberFormatException e) {
            view.showInputError("price", "Please enter a valid price (e.g., 12.50)");
        } catch (IllegalArgumentException e) {
            view.showError("Error: " + e.getMessage());
        }
    }
}