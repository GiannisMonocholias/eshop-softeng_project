package gr.softeng.team21.view.admin.requests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

public class NewRequestPresenterTest {

    NewRequestPresenter presenter;
    NewRequestViewStub view;
    UpdateRequestDAOMemory updateRequestDAOMemory;

    @Before
    public void setUp() throws Exception {

        MemoryInitializer.prepareData();

        updateRequestDAOMemory = UpdateRequestDAOMemory.getInstance();
        view = new NewRequestViewStub();
        presenter = new NewRequestPresenter();
    }

    @Test
    public void createRequest() {

        int before = updateRequestDAOMemory.getUpdateRequests().size();

        presenter.createRequest("Διαγραφή" , "Παντελής έλλειψη" , "Dell..." , "TECH-032");

        int after = updateRequestDAOMemory.getUpdateRequests().size();

        assertEquals(before + 1 , after);
    }
}