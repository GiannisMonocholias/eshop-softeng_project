package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletionException;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Unit tests for {@link OrdersPreparationEmployeeMenuPresenter}.
 * This suite verifies the core functionality of the Order Preparation Employee menu,
 * including data initialization, navigation logic, and secure account deletion handling asynchronously.
 * @author Γιάννης Μονοχολιάς
 */
public class OrdersPreparationEmployeeMenuPresenterTest {

    private OrdersPreparationEmployeeMenuPresenter presenter;
    private OrderPreparationEmployeeMenuViewStub viewStub;
    private EmployeeDAO employeeDAO;
    private UserCredentialsDAO userCredentialsDAO;

    private static final String EMPLOYEE_ID = "PREP-201";

    /**
     * Sets up the testing environment before each test.
     * Prepares memory data and instantiates the presenter with its dependencies.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();

        viewStub = new OrderPreparationEmployeeMenuViewStub();
        employeeDAO = EmployeeDAOMemory.getInstance();
        userCredentialsDAO = UserCredentialsDAOMemory.getInstance();

        presenter = new OrdersPreparationEmployeeMenuPresenter(viewStub, employeeDAO, userCredentialsDAO);
    }

    /**
     * Verifies that the correct employee name is retrieved and displayed
     * when the view is created.
     */
    @Test
    public void onViewCreatedShowsCorrectName() {
        presenter.onViewCreated(EMPLOYEE_ID);
        Assert.assertEquals("Γιώργος Νικολάου", viewStub.getShownName());
    }

    /**
     * Tests if selecting "Assigned Orders" triggers the correct navigation.
     */
    @Test
    public void onClickAssignedOrdersNavigatesCorrectly() {
        presenter.onClickAssignedOrders(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedAssignedOrdersId());
    }

    /**
     * Tests if selecting "Available Orders" triggers the correct navigation.
     */
    @Test
    public void onClickAvailableOrdersToAssignNavigatesCorrectly() {
        presenter.onClickAvailableOrdersToAssign(EMPLOYEE_ID);
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedAvailableOrdersId());
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
     * Verifies that the delete account action triggers a confirmation dialog on the UI.
     */
    @Test
    public void onDeleteAccountSelectedShowsConfirmationDialog() {
        presenter.onDeleteAccountSelected();
        Assert.assertTrue(viewStub.isDeleteConfirmationShown());
    }

    /**
     * Verifies the full account deletion workflow:
     * 1. Confirms the employee exists in memory and credentials are valid.
     * 2. Executes deletion.
     * 3. Ensures the employee is removed from the DAO and navigation to login occurs.
     * 4. Confirms credentials are wiped by expecting a CompletionException.
     */
    @Test(expected = CompletionException.class)
    public void onDeleteAccountConfirmedRemovesEmployeeAndNavigates() {
        Employee employee = employeeDAO.getEmployee(EMPLOYEE_ID).join();
        Assert.assertNotNull(employee);
        Assert.assertSame(employee, UserCredentialsDAOMemory.getInstance().validateAndGetUser("g_nikolaou","pass1240").join());

        presenter.onDeleteAccountConfirmed(EMPLOYEE_ID);

        Assert.assertEquals("Ο λογαριασμός διαγράφηκε επιτυχώς.", viewStub.getMessageShown());
        Assert.assertTrue(viewStub.isNavigateToLoginCalled());

        Assert.assertNull(employeeDAO.getEmployee(EMPLOYEE_ID).join());

        // This should trigger the expected CompletionException wrapping the SecurityException
        UserCredentialsDAOMemory.getInstance().validateAndGetUser("g_nikolaou", "pass1240").join();
    }

    /**
     * Verifies that an appropriate error message is shown if account deletion
     * is attempted for a non-existing ID.
     */
    @Test
    public void onDeleteAccountConfirmedShowsMessageForNonExistingId(){
        presenter.onDeleteAccountConfirmed("Non_existing_id");
        Assert.assertEquals("Σφάλμα: Ο υπάλληλος δεν βρέθηκε.", viewStub.getMessageShown());
    }
}