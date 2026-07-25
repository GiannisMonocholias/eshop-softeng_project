package gr.softeng.team21.view.admin.requests.adminRequestsActivity;

import java.util.List;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Interface defining the UI operations for viewing the list of submitted update requests.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public interface AdminRequestsView {

    /**
     * Updates the UI to display a collection of catalogue update requests.
     * @param requests The list of requests fetched from the database.
     */
    void showRequests(List<CatalogueUpdateRequest> requests);
}