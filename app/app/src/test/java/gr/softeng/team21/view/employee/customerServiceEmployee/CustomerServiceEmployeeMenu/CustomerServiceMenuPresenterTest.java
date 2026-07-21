package gr.softeng.team21.view.employee.customerServiceEmployee.CustomerServiceEmployeeMenu;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;
import gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu.CustomerServiceMenuPresenter;

/**
 * Unit tests for {@link CustomerServiceMenuPresenter}.
 * This suite ensures the main menu logic for customer service employees functions correctly,
 * including data display, navigation triggers, and asynchronous operations like account deletion
 * employing Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceMenuPresenterTest {

    private CustomerServiceMenuPresenter presenter;
    private CustomerServiceEmployeeMenuViewStub viewStub;
    private EmployeeDAO employeeDAO;
    private UserCredentialsDAO userCredentialsDAO;
    private static final String EMPLOYEE_ID = "CSR-101";

    /**
     * Prepares data and initializes the presenter with its dependencies before each test.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new CustomerServiceEmployeeMenuViewStub();
        employeeDAO = EmployeeDAOMemory.getInstance();
        userCredentialsDAO = UserCredentialsDAOMemory.getInstance();

        presenter = new CustomerServiceMenuPresenter(viewStub, employeeDAO, userCredentialsDAO);
    }

    /**
     * Verifies that the correct employee name is displayed when the view is initialized.
     */
    @Test
    public void onViewCreatedShowsCorrectName() {
        presenter.onViewCreated(EMPLOYEE_ID);

        Assert.assertEquals("Μαρία Αλεξάνδρου", viewStub.getShownEmployeeName());
    }

    /**
     * Verifies that if an invalid ID is provided during initialization,
     * no name is shown (graceful failure).
     */
    @Test
    public void onViewCreatedInvalidIdDoesNothing() {
        presenter.onViewCreated("NON_EXISTENT_ID");

        Assert.assertEquals("", viewStub.getShownEmployeeName());
    }

    /**
     * Tests if selecting the Inbox option triggers the correct navigation.
     */
    @Test
    public void onInboxSelectedNavigatesToInbox() {
        presenter.onInboxSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigateToInboxId());
    }

    /**
     * Tests if selecting the Order Status option triggers the correct navigation.
     */
    @Test
    public void onOrderStatusSelectedNavigatesToOrderStatus() {
        presenter.onOrderStatusSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigateToOrderStatusId());
    }

    /**
     * Tests if selecting the Process Account option triggers the correct navigation.
     */
    @Test
    public void onProcessAccountSelectedNavigatesToProcessAccount() {
        presenter.onProcessAccountSelected(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigateToProcessAccountId());
    }

    /**
     * Verifies that clicking delete account triggers a confirmation dialog on the UI.
     */
    @Test
    public void onDeleteAccountSelectedShowsConfirmation() {
        presenter.onDeleteAccountSelected();
        Assert.assertTrue(viewStub.isDeleteConfirmationShown());
    }

    /**
     * Verifies the full asynchronous account deletion workflow:
     * 1. Confirms the employee exists initially using join().
     * 2. Executes deletion via presenter.
     * 3. Checks for success message and redirection.
     * 4. Ensures employee is removed from both EmployeeDAO and CredentialsDAO.
     * @throws SecurityException when credentials validation fails after deletion (expected).
     */
    @Test(expected = SecurityException.class)
    public void onDeleteAccountConfirmedDeletesEmployeeAndCredentialsSuccess() {
        Employee empBefore = employeeDAO.getEmployee(EMPLOYEE_ID).join();
        Assert.assertNotNull(empBefore);
        Assert.assertSame(empBefore, UserCredentialsDAOMemory.getInstance().validateAndGetUser(empBefore.getUsername(), empBefore.getPassword()));

        // Confirm deletion for existing employeeId
        presenter.onDeleteAccountConfirmed(EMPLOYEE_ID);

        Assert.assertEquals("Ο λογαριασμός διαγράφηκε επιτυχώς.", viewStub.getMessageShown());
        Assert.assertTrue(viewStub.isNavigateToLoginCalled());

        Assert.assertNull(employeeDAO.getEmployee(EMPLOYEE_ID).join());

        // This call should throw SecurityException because credentials were deleted
        UserCredentialsDAOMemory.getInstance().validateAndGetUser(empBefore.getUsername(), empBefore.getPassword());
    }

    /**
     * Verifies that attempting to confirm deletion for a non-existing ID
     * results in an error message.
     */
    @Test
    public void onDeleteAccountConfirmedDeletesEmployeeAndCredentialsFailure() {
        // Non existing employeeId
        presenter.onDeleteAccountConfirmed("Non_existing_id");
        Assert.assertEquals("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.", viewStub.getMessageShown());
    }
}