package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

import java.math.BigDecimal;
import java.util.Currency;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.Money;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;

public class ExecuteInsertProductPresenter {
    private ExecuteInsertProductView view;

    // DAOs
    private EmployeeDAO employeeDAO;
    private UpdateRequestDAO updateRequestDAO;
    private ProductTypeDAO productTypeDAO;

    private CatalogueUpdateRequest currentRequest;
    private UpdateCatalogueEmployee loggedInEmployee;

    public ExecuteInsertProductPresenter(ExecuteInsertProductView view, EmployeeDAO employeeDAO, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    public void loadRequestDetails(String employeeId, int requestId) {
        this.loggedInEmployee = (UpdateCatalogueEmployee) employeeDAO.getEmployee(employeeId);

        if (updateRequestDAO.getUpdateRequests() != null) {
            this.currentRequest = updateRequestDAO.getUpdateRequests().get(requestId);
        }

        if (currentRequest == null || loggedInEmployee == null) {
            view.showError("Σφάλμα: Τα στοιχεία δεν βρέθηκαν.");
            return;
        }

        view.setRequestDescription(currentRequest.getUpdateDescription());
    }

    public void onConfirmInsert() {
        String code = view.getProductCode();
        String name = view.getProductName();
        String priceStr = view.getProductPrice();
        String desc = view.getProductDescription();


        try {
            double priceVal = Double.parseDouble(priceStr);
            if (priceVal < 0) throw new NumberFormatException();

            Money money = new Money(BigDecimal.valueOf(priceVal), "€");

            ProductType newProduct = new ProductType(name, desc, money, code);


            productTypeDAO.addProductType(newProduct);

            currentRequest.setStatus(RequestStatusType.SERVED);


            view.showSuccessMessage("Το προϊόν καταχωρήθηκε επιτυχώς!");
            loggedInEmployee.getAssignedRequests().remove(currentRequest.getId());
        } catch (NumberFormatException e) {
            view.showInputError("price", "Παρακαλώ εισάγετε έγκυρη τιμή (π.χ. 12.50)");
        } catch (IllegalArgumentException e) {
            view.showError("Σφάλμα: " + e.getMessage());
        } catch (Exception e) {
            view.showError("Απροσδόκητο σφάλμα: " + e.getMessage());
        }
    }
}