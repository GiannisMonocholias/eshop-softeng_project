package gr.softeng.team21.view.admin.requests.adminRequestsActivity;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Presenter responsible for fetching all submitted catalogue update requests
 * from the database asynchronously to be displayed to the administrator.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class AdminRequestsPresenter {

    private final AdminRequestsView view;
    private final UpdateRequestDAO updateRequestDAO;

    /**
     * Constructs the presenter with the required view and DAO through Dependency Injection.
     * @param view The UI contract interface.
     * @param updateRequestDAO The Data Access Object for handling requests.
     */
    public AdminRequestsPresenter(AdminRequestsView view, UpdateRequestDAO updateRequestDAO) {
        this.view = view;
        this.updateRequestDAO = updateRequestDAO;
    }

    /**
     * Retrieves all update requests from the database asynchronously and forwards them to the view.
     */
    public void loadRequests() {
        // Unpack the asynchronous map
        updateRequestDAO.getUpdateRequests().thenAccept(requestsMap -> {

            List<CatalogueUpdateRequest> reqs = new ArrayList<>(requestsMap.values());

            if (view != null) {
                view.showRequests(reqs);
            }

        }).exceptionally(e -> {
            System.err.println("Σφάλμα φόρτωσης αιτημάτων: " + e.getMessage());
            return null;
        });
    }
}