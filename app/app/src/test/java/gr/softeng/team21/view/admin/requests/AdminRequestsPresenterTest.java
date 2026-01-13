package gr.softeng.team21.view.admin.requests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Tests that all the requests that the admin submits
 * are shown in a list.
 */

public class AdminRequestsPresenterTest {

    AdminRequestsPresenter presenter;
    AdminRequestsViewStub view;
    UpdateRequestDAOMemory updateRequestDAOMemory;

    /**
     * In setUp method the memory is prepared with the initialized data
     * and the presenter,view and dao objects are initialized.
     *
     */

    @Before
    public void setUp() {

        MemoryInitializer.prepareData();

        updateRequestDAOMemory = UpdateRequestDAOMemory.getInstance();
        view = new AdminRequestsViewStub();
        presenter = new AdminRequestsPresenter(view , updateRequestDAOMemory);
    }

    /**
     * Here it is checked that all the requests of the list are loaded correctly.
     */

    @Test
    public void loadRequests() {

        ArrayList<CatalogueUpdateRequest> requests = presenter.loadRequests();

        assertEquals(5 , requests.size());
    }
}