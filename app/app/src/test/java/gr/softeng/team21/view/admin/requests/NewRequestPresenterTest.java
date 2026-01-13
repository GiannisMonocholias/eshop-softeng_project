package gr.softeng.team21.view.admin.requests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Here we test that each new request that the admin submits
 * is added in the list.
 */

public class NewRequestPresenterTest {

    NewRequestPresenter presenter;
    NewRequestViewStub view;
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
        view = new NewRequestViewStub();
        presenter = new NewRequestPresenter();
    }

    /**
     * We check that the createRequest method creates successfully
     * a request and adds it in the requests' list.
     */

    @Test
    public void createRequest() {

        int before = updateRequestDAOMemory.getUpdateRequests().size();

        presenter.createRequest("Διαγραφή" , "Παντελής έλλειψη" , "Dell..." , "TECH-032");

        int after = updateRequestDAOMemory.getUpdateRequests().size();

        assertEquals(before + 1 , after);
    }
}