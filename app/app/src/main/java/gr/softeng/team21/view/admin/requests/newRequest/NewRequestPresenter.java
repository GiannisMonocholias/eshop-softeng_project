package gr.softeng.team21.view.admin.requests.newRequest;

import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.AllowedRequest;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.view.admin.requests.NewRequestActivityView;


/**
 * Presenter responsible for validating user input and safely submitting new
 * catalogue update requests. Utilizes Dependency Injection for DAOs.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class NewRequestPresenter {

    private final NewRequestActivityView view;
    private final UpdateRequestDAO updateRequestDAO;
    private final ProductTypeDAO productTypeDAO;

    /**
     * Constructs the presenter with the provided view and required Data Access Objects.
     *
     * @param view The UI contract interface.
     * @param updateRequestDAO The DAO managing update requests.
     * @param productTypeDAO The DAO managing available product types.
     */
    public NewRequestPresenter(NewRequestActivityView view, UpdateRequestDAO updateRequestDAO, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.updateRequestDAO = updateRequestDAO;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Validates input fields, verifies the existence of the product (if applicable),
     * and constructs a new request to be saved in the database.
     * @param choice The action type (e.g., "Εισαγωγή", "Διαγραφή", "Τροποποίηση").
     * @param description The detailed description/reason for the request.
     * @param productName The name of the product involved.
     * @param productId The unique code/ID of the product involved.
     */
    public void createRequest(String choice, String description, String productName, String productId) {

        if (choice == null || choice.isEmpty() || description == null || description.isEmpty() || productId == null || productId.isEmpty()) {
            if (view != null) view.showError("Παρακαλώ συμπληρώστε όλα τα πεδία.");
            return;
        }

        productTypeDAO.getProducts().thenAccept(productsMap -> {
            ProductType product = null;

            for (ProductType p : productsMap.values()) {
                if (p.getProductCode() != null && p.getProductCode().equals(productId)) {
                    product = p;
                    break;
                }
            }

            String finalDescription = description;

            if (product == null) {
                if (choice.equals("Εισαγωγή")) {
                    String safeName = (productName != null && !productName.isEmpty()) ? productName : "Νέο Προϊόν";

                    finalDescription += "\n\n[Στοιχεία Εισαγωγής]\nΌνομα: " + safeName + "\nΚωδικός: " + productId;

                    product = new ProductType(safeName, "Εκκρεμεί καταχώρηση", null, productId);
                } else {
                    if (view != null) view.showError("Το προϊόν με κωδικό " + productId + " δεν βρέθηκε.");
                    return;
                }
            }

            AllowedRequest type = AllowedRequest.INSERT_PRODUCT;
            if (choice.equals("Διαγραφή")) type = AllowedRequest.DELETE_PRODUCT;
            if (choice.equals("Τροποποίηση")) type = AllowedRequest.PROCESS_PRODUCT;

            int reqId = (int) (System.currentTimeMillis() % 10000);

            CatalogueUpdateRequest newRequest = new CatalogueUpdateRequest(new Date(), finalDescription, product, type, reqId);

            updateRequestDAO.addUpdateRequest(newRequest);

            if (view != null) view.showSuccessAndClose("Το αίτημα υποβλήθηκε επιτυχώς!");

        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα συστήματος: " + e.getMessage());
            return null;
        });
    }
}