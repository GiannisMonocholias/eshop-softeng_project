package gr.softeng.team21.view.admin.requests.newRequest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Unit tests for the {@link NewRequestPresenter}.
 * @author Γιάννης Μονοχολιάς, Αλέξανδρος Δρακάκης
 */
public class NewRequestPresenterTest {

    private NewRequestPresenter presenter;
    private NewRequestActivityViewStub viewStub;
    private UpdateRequestDAOMemory updateRequestDAO;
    private ProductTypeDAOMemory productTypeDAO;

    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new NewRequestActivityViewStub();
        updateRequestDAO = UpdateRequestDAOMemory.getInstance();
        productTypeDAO = ProductTypeDAOMemory.getInstance();

        presenter = new NewRequestPresenter(viewStub, updateRequestDAO, productTypeDAO);
    }

    @Test
    public void createRequestRejectsEmptyFields() {
        presenter.createRequest("", "Test Description", "Test Product", "PRD-1");

        assertNotNull("Should display error for empty fields", viewStub.getErrorMessage());
        assertEquals("Παρακαλώ συμπληρώστε όλα τα πεδία.", viewStub.getErrorMessage());
        assertNull(viewStub.getSuccessMessage());
    }

    @Test
    public void createRequestSuccessfullySubmitsValidData() {
        int initialSize = updateRequestDAO.getUpdateRequests().join().size();

        presenter.createRequest("Τροποποίηση", "Change price", "G.Skill Trident Z5", "TECH-012");

        assertNotNull("Should display success message", viewStub.getSuccessMessage());
        assertEquals(initialSize + 1, updateRequestDAO.getUpdateRequests().join().size());
        assertNull("Should not display error", viewStub.getErrorMessage());
    }


}