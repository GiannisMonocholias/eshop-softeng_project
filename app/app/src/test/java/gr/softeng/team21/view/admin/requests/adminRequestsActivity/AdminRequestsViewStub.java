package gr.softeng.team21.view.admin.requests.adminRequestsActivity;

import java.util.List;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * Stub for AdminRequestsView used in unit testing.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class AdminRequestsViewStub implements AdminRequestsView {
    private List<CatalogueUpdateRequest> loadedRequests;

    @Override
    public void showRequests(List<CatalogueUpdateRequest> requests) {
        this.loadedRequests = requests;
    }

    public List<CatalogueUpdateRequest> getLoadedRequests() { return loadedRequests; }
}