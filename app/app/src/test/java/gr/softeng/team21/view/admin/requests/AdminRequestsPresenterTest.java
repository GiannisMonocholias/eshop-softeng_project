package gr.softeng.team21.view.admin.requests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

public class AdminRequestsPresenterTest {

    AdminRequestsPresenter presenter;
    AdminRequestsViewStub view;
    UpdateRequestDAOMemory updateRequestDAOMemory;

    @Before
    public void setUp() throws Exception {

        MemoryInitializer.prepareData();

        updateRequestDAOMemory = UpdateRequestDAOMemory.getInstance();
        view = new AdminRequestsViewStub();
        presenter = new AdminRequestsPresenter(view , updateRequestDAOMemory);
    }

    @Test
    public void loadRequests() {

        ArrayList<CatalogueUpdateRequest> requests = presenter.loadRequests();

        assertEquals(5 , requests.size());
    }
}