package gr.softeng.team21.view.employee.updateCatalogueEmployee.updateCatalogueEmployeeMenu;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Unit tests for {@link UpdateCatalogueEmployeeMenuPresenter}.
 * This suite verifies the core functionality of the Update Catalogue Employee menu,
 * ensuring proper navigation to request management and secure handling of account operations.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateCatalogueEmployeeMenuPresenterTest {

    private UpdateCatalogueEmployeeMenuPresenter presenter;
    private UpdateCatalogueEmployeeMenuViewStub viewStub;
    private EmployeeDAO employeeDAO;

    private static final String EMPLOYEE_ID = "CAT-301";

    /**
     * Sets up the testing environment before each test case.
     * Prepares memory data and instantiates the presenter with its dependencies.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new UpdateCatalogueEmployeeMenuViewStub();
        employeeDAO = EmployeeDAOMemory.getInstance();
        presenter = new UpdateCatalogueEmployeeMenuPresenter(viewStub, employeeDAO);
    }

    /**
     * Verifies that the correct employee name is retrieved and displayed
     * upon view creation.
     */
    @Test
    public void onViewCreatedShowsCorrectName() {
        presenter.onViewCreated(EMPLOYEE_ID);
        Assert.assertEquals("Δήμητρα Γεωργίου", viewStub.getShownName());
    }

    /**
     * Tests if selecting "Assigned Requests" triggers the correct navigation.
     */
    @Test
    public void onClickAssignedRequestsNavigatesCorrectly() {
        presenter.onClickAssignedRequests(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedAssignedRequestsId());
    }

    /**
     * Tests if selecting "Process Account" triggers the correct navigation.
     */
    @Test
    public void onProcessAccountSelectedNavigatesCorrectly() {
        presenter.onProcessAccountSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedProcessAccountId());
    }

    /**
     * Tests if selecting "Available Requests" triggers the correct navigation.
     */
    @Test
    public void onClickAvailableRequestsToAssignNavigatesCorrectly() {
        presenter.onClickAvailableRequestsToAssign(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedAvailableRequestsId());
    }

    /**
     * Verifies that the delete account action triggers a confirmation dialog on the UI.
     */
    @Test
    public void onDeleteAccountSelectedShowsConfirmation() {
        presenter.onDeleteAccountSelected();
        Assert.assertTrue(viewStub.isDeleteConfirmationShown());
    }

    /**
     * Verifies the successful account deletion workflow:
     * 1. Confirms the employee exists in memory and credentials are valid.
     * 2. Executes deletion.
     * 3. Ensures the employee is removed from the DAO and navigation to login occurs.
     * 4. Confirms credentials are wiped by expecting a SecurityException.
     * @throws SecurityException when validating credentials after deletion (Expected).
     */
    @Test(expected = SecurityException.class)
    public void onDeleteAccountConfirmedRemovesEmployeeAndNavigates() {
        Assert.assertNotNull(employeeDAO.getEmployee(EMPLOYEE_ID));
        Assert.assertSame(employeeDAO.getEmployee(EMPLOYEE_ID), UserCredentialsDAOMemory.getInstance().validateAndGetUser("d_georgiou", "pass1243"));

        presenter.onDeleteAccountConfirmed(EMPLOYEE_ID);

        Assert.assertEquals("Ο λογαριασμός διαγράφηκε επιτυχώς.", viewStub.getMessageShown());
        Assert.assertTrue(viewStub.isNavigateToLoginCalled());

        Assert.assertNull(employeeDAO.getEmployee(EMPLOYEE_ID));

        // This should trigger the expected SecurityException
        UserCredentialsDAOMemory.getInstance().validateAndGetUser("d_georgiou", "pass1243");
    }

    /**
     * Verifies that an appropriate error message is shown if account deletion
     * is attempted for a non-existing ID.
     */
    @Test
    public void onDeleteAccountConfirmedInvalidId_ShowsErrorMessage() {
        presenter.onDeleteAccountConfirmed("INVALID_ID");

        Assert.assertEquals("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.", viewStub.getMessageShown());
        Assert.assertFalse(viewStub.isNavigateToLoginCalled());
    }
}