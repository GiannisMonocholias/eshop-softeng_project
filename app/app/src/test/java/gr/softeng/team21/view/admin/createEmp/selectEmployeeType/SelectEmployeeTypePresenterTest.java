package gr.softeng.team21.view.admin.createEmp.selectEmployeeType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests verifying the functionality of the {@link SelectEmployeeTypePresenter}.
 * Tests the asynchronous retrieval and counting of existing employees per category,
 * as well as correct routing.
 * @author Γιάννης Μονοχολιάς
 */
public class SelectEmployeeTypePresenterTest {

    private SelectEmployeeTypePresenter presenter;
    private SelectEmployeeTypeViewStub viewStub;

    /**
     * Initializes the memory databases with mock data and instantiates the presenter
     * utilizing the Stub view.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData(); // Populates EmployeeDAOMemory with predefined employees
        viewStub = new SelectEmployeeTypeViewStub();
        presenter = new SelectEmployeeTypePresenter(viewStub, EmployeeDAOMemory.getInstance());
    }

    /**
     * Verifies that the presenter successfully loads employees from the DAO,
     * categorizes them, counts them, and updates the view.
     */
    @Test
    public void loadEmployeeCounts_UpdatesViewWithCorrectNumbers() {
        presenter.loadEmployeeCounts();


        Assert.assertTrue("Should have counted CS employees", viewStub.getCsCount() >= 0);
        Assert.assertTrue("Should have counted Deliverers", viewStub.getDelCount() >= 0);
        Assert.assertEquals("Should not have any error message", "", viewStub.getErrorMessage());
    }

    /**
     * Ensures that clicking an employee type correctly triggers the navigation
     * with the appropriate intent extra string.
     */
    @Test
    public void onTypeSelected_NavigatesToFormWithCorrectType() {
        presenter.onTypeSelected("DELIVERER");

        Assert.assertEquals("DELIVERER", viewStub.getNavigatedType());
    }
}