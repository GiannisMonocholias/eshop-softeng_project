package gr.softeng.team21.view.admin.requests.adminRequestsActivity;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Unit tests for the {@link AdminRequestsPresenter}.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class AdminRequestsPresenterTest {

    private AdminRequestsPresenter presenter;
    private AdminRequestsViewStub viewStub;
    private UpdateRequestDAOMemory updateRequestDAO;

    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new AdminRequestsViewStub();
        updateRequestDAO = UpdateRequestDAOMemory.getInstance();

        presenter = new AdminRequestsPresenter(viewStub, updateRequestDAO);
    }

    @Test
    public void loadRequestsSuccessfullyPopulatesView() {
        presenter.loadRequests();

        List<CatalogueUpdateRequest> requests = viewStub.getLoadedRequests();

        assertNotNull("Requests list should not be null", requests);
        assertFalse("Requests list should not be empty based on MemoryInitializer", requests.isEmpty());
        assertEquals(updateRequestDAO.getUpdateRequests().join().size(), requests.size());
    }
}