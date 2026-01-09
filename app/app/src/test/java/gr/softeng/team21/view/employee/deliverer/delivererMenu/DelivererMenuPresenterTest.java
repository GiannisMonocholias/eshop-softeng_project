package gr.softeng.team21.view.employee.deliverer.delivererMenu;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Unit tests for {@link DelivererMenuPresenter}.
 * This suite ensures that the Deliverer's main menu logic functions correctly,
 * including loading profile data, navigating to task lists, and managing
 * sensitive account-related operations.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererMenuPresenterTest {

    private DelivererMenuPresenter presenter;
    private DelivererMenuViewStub viewStub;
    private EmployeeDAO employeeDAO;

    private static final String EMPLOYEE_ID = "DEL-401";

    /**
     * Sets up the testing environment before each test.
     * Populates memory DAOs and instantiates the presenter with its dependencies.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new DelivererMenuViewStub();
        employeeDAO = EmployeeDAOMemory.getInstance();
        presenter = new DelivererMenuPresenter(viewStub, employeeDAO);
    }

    /**
     * Verifies that the presenter correctly retrieves and displays
     * the Deliverer's name upon view creation.
     */
    @Test
    public void onViewCreatedShowsCorrectName() {
        presenter.onViewCreated(EMPLOYEE_ID);
        Assert.assertEquals("Νίκος Στάμος", viewStub.getShownName());
    }

    /**
     * Verifies that the presenter handles an invalid employee ID gracefully
     * during initialization.
     */
    @Test
    public void onViewCreated_InvalidIdDoesNothing() {
        presenter.onViewCreated("INVALID_ID");
        Assert.assertEquals("", viewStub.getShownName());
    }

    /**
     * Tests if selecting the orders list triggers the correct navigation event.
     */
    @Test
    public void onOrdersListSelectedNavigatesToOrdersList() {
        presenter.onOrdersListSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedOrdersListId());
    }

    /**
     * Tests if selecting the process account option triggers the correct navigation event.
     */
    @Test
    public void onProcessAccountSelectedNavigatesToEditData() {
        presenter.onProcessAccountSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedProcessAccountId());
    }

    /**
     * Verifies that the account deletion flow correctly requests a confirmation
     * from the user via the UI.
     */
    @Test
    public void onDeleteAccountSelectedShowsConfirmationDialog() {
        presenter.onDeleteAccountSelected();
        Assert.assertTrue(viewStub.isDeleteConfirmationShown());
    }

    /**
     * Verifies the successful account deletion process:
     * 1. Confirms existence of the employee in memory.
     * 2. Executes deletion and verifies UI feedback and navigation.
     * 3. Ensures the employee is removed from EmployeeDAO.
     * 4. Ensures the employee's credentials are wiped (throwing SecurityException).
     * @throws SecurityException when trying to validate a deleted user (expected).
     */
    @Test(expected = SecurityException.class)
    public void onDeleteAccountConfirmedSuccessRemovesUserAndNavigates() {
        Assert.assertNotNull(employeeDAO.getEmployee(EMPLOYEE_ID));
        Assert.assertSame(employeeDAO.getEmployee(EMPLOYEE_ID),UserCredentialsDAOMemory.getInstance().validateAndGetUser("n_stamos","pass1246"));

        presenter.onDeleteAccountConfirmed(EMPLOYEE_ID);

        Assert.assertEquals("Ο λογαριασμός διαγράφηκε επιτυχώς.", viewStub.getMessageShown());
        Assert.assertTrue(viewStub.isNavigateToLoginCalled());


        Assert.assertNull(employeeDAO.getEmployee(EMPLOYEE_ID));

        // Attempting to validate should now fail as credentials are removed
        UserCredentialsDAOMemory.getInstance().validateAndGetUser("n_stamos", "pass1246");
    }

    /**
     * Verifies that attempting to confirm deletion for a non-existent ID
     * results in a proper error message.
     */
    @Test
    public void onDeleteAccountConfirmedInvalidIdShowsError() {
        presenter.onDeleteAccountConfirmed("INVALID_ID");

        Assert.assertEquals("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.", viewStub.getMessageShown());
        Assert.assertFalse(viewStub.isNavigateToLoginCalled());
    }
}